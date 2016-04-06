package com.taobao.openimui.sample;

import android.app.Application;
import android.content.Intent;
import android.os.Debug;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.IYWP2PPushListener;
import com.alibaba.mobileim.IYWTribePushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.LoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactCacheUpdateListener;
import com.alibaba.mobileim.contact.IYWContactOperateNotifyListener;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.login.YWPwdType;
import com.alibaba.mobileim.ui.chat.widget.YWSmilyMgr;
import com.alibaba.mobileim.utility.IMAutoLoginInfoStoreUtil;
import com.alibaba.tcms.env.EnvManager;
import com.alibaba.tcms.env.TcmsEnvType;
import com.alibaba.openIMUIDemo.LoginActivity;
import com.alibaba.tcms.env.YWEnvManager;
import com.alibaba.tcms.env.YWEnvType;
import com.taobao.openimui.common.Notification;
import com.taobao.openimui.contact.ContactCacheUpdateListenerImpl;
import com.taobao.openimui.contact.ContactOperateNotifyListenerImpl;
import com.taobao.openimui.demo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SDK 初始化和登录
 *
 * @author jing.huai
 */
public class LoginSampleHelper {

    private static LoginSampleHelper sInstance = new LoginSampleHelper();

    public static LoginSampleHelper getInstance() {
        return sInstance;
    }

    // 应用APPKEY，这个APPKEY是申请应用时获取的
    public static  String APP_KEY = "23015524";

    //以下两个内容是测试环境使用，开发无需关注
//    public static final String APP_KEY_TEST = "4272";  //60026702

    public static final String APP_KEY_TEST = "60028148";  //60026702


    public static YWEnvType sEnvType = YWEnvType.ONLINE;

    // openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
    private YWIMKit mIMKit;

    private YWConnectionListenerImpl mYWConnectionListenerImpl = new YWConnectionListenerImpl();
    private Application mApp;

    private List<Map<YWTribe, YWTribeMember>> mTribeInviteMessages = new ArrayList<Map<YWTribe, YWTribeMember>>();

    public YWIMKit getIMKit() {
        return mIMKit;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKit(String userId, String appKey) {
        mIMKit = YWAPI.getIMKitInstance(userId, appKey);
        addConnectionListener();
        addPushMessageListener();
        //添加联系人通知和更新监听 todo 在初始化后、登录前添加监听，离线的联系人系统消息才能触发监听器
        addContactListeners();

    }

    private YWLoginState mAutoLoginState = YWLoginState.idle;

    public YWLoginState getAutoLoginState() {
        return mAutoLoginState;
    }

    public void setAutoLoginState(YWLoginState state) {
        this.mAutoLoginState = state;
    }

    /**
     * 初始化SDK
     *
     * @param context
     */
    public void initSDK_Sample(Application context) {
        mApp = context;
        sEnvType = YWEnvManager.getEnv(context);
        //初始化IMKit
		final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
		final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)){
//		final String userId = IMAutoLoginInfoStoreUtil.getLoginUserId();
			LoginSampleHelper.getInstance().initIMKit(userId, appkey);
//		final String appkey = IMAutoLoginInfoStoreUtil.getAppkey();
//			NotificationInitSampleHelper.init();//重复初始化了
		}
//		if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(appkey)){
        TcmsEnvType type = EnvManager.getInstance().getCurrentEnvType(mApp);
        if(type==TcmsEnvType.ONLINE || type == TcmsEnvType.PRE){
            YWAPI.init(mApp, APP_KEY);
        }
        else if(type==TcmsEnvType.TEST){
            YWAPI.init(mApp, APP_KEY_TEST);
        }

        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        initAutoLoginStateCallback();


        //添加自定义表情的初始化
        YWSmilyMgr.setSmilyInitNotify(new YWSmilyMgr.SmilyInitNotify() {
            @Override
            public void onDefaultSmilyInitOk() {
                SmilyCustomSample.addNewEmojiSmiley();
                SmilyCustomSample.addNewImageSmiley();

                //最后要清空通知，防止memory leak
                YWSmilyMgr.setSmilyInitNotify(null);
            }
        });
    }

    //将自动登录的状态广播出去
    private void sendAutoLoginState(YWLoginState loginState) {
        Intent intent = new Intent(LoginActivity.AUTO_LOGIN_STATE_ACTION);
        intent.putExtra("state", loginState.getValue());
        LocalBroadcastManager.getInstance(YWChannel.getApplication()).sendBroadcast(intent);
    }

    /**
     * 登录操作
     *
     * @param userId   用户id
     * @param password 密码
     * @param callback 登录操作结果的回调
     */
    //------------------请特别注意，OpenIMSDK会自动对所有输入的用户ID转成小写处理-------------------
    //所以开发者在注册用户信息时，尽量用小写
    public void login_Sample(String userId, String password, String appKey,
                             IWxCallback callback) {

        if (mIMKit == null) {
            return;
        }


        YWLoginParam loginParam = YWLoginParam.createLoginParam(userId,
                password);
        if (TextUtils.isEmpty(appKey) || appKey.equals(YWConstants.YWSDKAppKey)
                || appKey.equals(YWConstants.YWSDKAppKeyCnHupan)) {
            loginParam.setServerType(LoginParam.ACCOUNTTYPE_WANGXIN);
            loginParam.setPwdType(YWPwdType.pwd);
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();

        mLoginService.login(loginParam, callback);
    }

    //设置连接状态的监听
    private void addConnectionListener() {
        if (mIMKit == null) {
            return;
        }

        YWIMCore imCore = mIMKit.getIMCore();
        imCore.removeConnectionListener(mYWConnectionListenerImpl);
        imCore.addConnectionListener(mYWConnectionListenerImpl);
    }

    private class YWConnectionListenerImpl implements IYWConnectionListener {

        @Override
        public void onReConnecting() {

        }

        @Override
        public void onReConnected() {

//				YWLog.i("LoginSampleHelper", "onReConnected");


        }

        @Override
        public void onDisconnect(int arg0, String arg1) {
            if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF) {
                sendAutoLoginState(YWLoginState.disconnect);
                //在其它终端登录，当前用户被踢下线
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.disconnect);
                Toast.makeText(DemoApplication.getContext(), "被踢下线", Toast.LENGTH_LONG).show();
                YWLog.i("LoginSampleHelper", "被踢下线");
                Intent intent = new Intent(DemoApplication.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DemoApplication.getContext().startActivity(intent);
            }
        }
    }

    /**
     * 添加新消息到达监听，该监听应该在登录之前调用以保证登录后可以及时收到消息
     */
    private void addPushMessageListener(){
        if (mIMKit == null) {
            return;
        }

        IYWConversationService conversationService = mIMKit.getConversationService();
        //添加单聊消息监听，先删除再添加，以免多次添加该监听
        conversationService.removeP2PPushListener(mP2PListener);
        conversationService.addP2PPushListener(mP2PListener);

        //添加群聊消息监听，先删除再添加，以免多次添加该监听
        conversationService.removeTribePushListener(mTribeListener);
        conversationService.addTribePushListener(mTribeListener);
    }

    private IYWContactOperateNotifyListener mContactOperateNotifyListener = new ContactOperateNotifyListenerImpl();

    private IYWContactCacheUpdateListener mContactCacheUpdateListener = new ContactCacheUpdateListenerImpl();

    /**
     * 联系人相关操作通知回调，SDK使用方可以实现此接口来接收联系人操作通知的监听
     * 所有方法都在UI线程调用
     * SDK会自动处理这些事件，一般情况下，用户不需要监听这个事件
     * @author shuheng
     *
     */
    private void addContactListeners(){
        //添加联系人通知和更新监听，先删除再添加，以免多次添加该监听
        removeContactListeners();
        if(mIMKit!=null){
            if(mContactOperateNotifyListener!=null)
                mIMKit.getContactService().addContactOperateNotifyListener(mContactOperateNotifyListener);
            if(mContactCacheUpdateListener!=null)
                mIMKit.getContactService().addContactCacheUpdateListener(mContactCacheUpdateListener);

        }
    }

    private void removeContactListeners(){
        if(mIMKit!=null){
            if(mContactOperateNotifyListener!=null)
                mIMKit.getContactService().removeContactOperateNotifyListener(mContactOperateNotifyListener);
            if(mContactCacheUpdateListener!=null)
                mIMKit.getContactService().removeContactCacheUpdateListener(mContactCacheUpdateListener);

        }
    }

    private IYWP2PPushListener mP2PListener = new IYWP2PPushListener() {
        @Override
        public void onPushMessage(IYWContact contact, YWMessage message) {
            if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS){
                if (message.getMessageBody() instanceof YWCustomMessageBody) {
                    YWCustomMessageBody messageBody = (YWCustomMessageBody) message.getMessageBody();
                    if (messageBody.getTransparentFlag() == 1) {
                        String content = messageBody.getContent();
                        try {
                            JSONObject object = new JSONObject(content);
                            if (object.has("text")){
                                String text = object.getString("text");
                                Notification.showToastMsgLong(DemoApplication.getContext(), "透传消息，content = " + text);
                            }
                        } catch (JSONException e){

                        }
                    }
                }
            }
        }
    };

    private IYWTribePushListener mTribeListener = new IYWTribePushListener() {
        @Override
        public void onPushMessage(YWTribe tribe, YWMessage message) {
            //TODO 收到群消息
        }
    };

    /**
     * 登出
     */
    public void loginOut_Sample() {
        if (mIMKit == null) {
            return;
        }


        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });
    }

    /**
     * 开发者不需要关注此方法，纯粹是DEMO自动登录的需要
     */
    private void initAutoLoginStateCallback() {
        YWChannel.setAutoLoginCallBack(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                mAutoLoginState = YWLoginState.success;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onError(int code, String info) {
                mAutoLoginState = YWLoginState.fail;
                sendAutoLoginState(mAutoLoginState);
            }

            @Override
            public void onProgress(int progress) {
                mAutoLoginState = YWLoginState.logining;
                sendAutoLoginState(mAutoLoginState);
            }
        });
    }

    public List<Map<YWTribe, YWTribeMember>> getTribeInviteMessages() {
        return mTribeInviteMessages;
    }
}

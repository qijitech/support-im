package com.taobao.openimui.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.GoodsInfo;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactProfileCallback;
import com.alibaba.mobileim.contact.IYWCrossContactProfileCallback;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomMessageBody;
import com.alibaba.mobileim.conversation.YWGeoMessageBody;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWPushInfo;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
import com.alibaba.mobileim.fundamental.widget.WxAlertDialog;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.kit.common.IMUtility;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.common.Notification;
import com.taobao.openimui.demo.DemoApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面(单聊和群聊界面)的定制点(根据需要实现相应的接口来达到自定义聊天界面)，不设置则使用openIM默认的实现
 * 1.CustomChattingTitleAdvice 自定义聊天窗口标题 2. OnUrlClickChattingAdvice 自定义聊天窗口中
 * 当消息是url是点击的回调。用于isv处理url的打开处理。不处理则用第三方浏览器打开 如果需要定制更多功能，需要实现更多开放的接口
 * 需要.继承BaseAdvice .实现相应的接口
 * <p/>
 * 另外需要在Application中绑定
 * AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT,
 * ChattingOperationCustomSample.class);
 *
 * @author jing.huai
 */
public class ChattingOperationCustomSample extends IMChattingPageOperateion {

    private static final String TAG = "ChattingOperationCustomSample";

    // 默认写法
    public ChattingOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 单聊ui界面，点击url的事件拦截 返回true;表示自定义处理，返回false，由默认处理
     *
     * @param fragment 可以通过 fragment.getActivity拿到Context
     * @param message  点击的url所属的message
     * @param url      点击的url
     */
    @Override
    public boolean onUrlClick(Fragment fragment, YWMessage message, String url,
                              YWConversation conversation) {
        Notification.showToastMsgLong(fragment.getActivity(), "用户点击了url:" + url);
        if(!url.startsWith("http")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        fragment.startActivity(intent);

        return true;
    }

    /**
     * 是否显示默认的Item，照片，相册
     * @param conversation
     * @return
     */
    @Override
    public boolean showDefaultBarItems(YWConversation conversation){
        return true;//显示
    }

    private static int ITEM_ID_1 = 0x1;
    private static int ITEM_ID_2 = 0x2;
    private static int ITEM_ID_3 = 0X3;

    /**
     * 用于增加聊天窗口 下方回复栏的操作区的item ReplyBarItem itemId:唯一标识 建议从1开始
     * ItemImageRes：显示的图片 ItemLabel：文字 label YWConversation
     * conversation：当前会话，通过conversation.getConversationType() 区分个人单聊，与群聊天
     */
    @Override
    public List<ReplyBarItem> getReplybarItems(Fragment pointcut,
                                               YWConversation conversation) {
        List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();
        if (conversation.getConversationType() == YWConversationType.P2P) {
            ReplyBarItem replyBarItem = new ReplyBarItem();
            replyBarItem.setItemId(ITEM_ID_1);
            replyBarItem.setItemImageRes(R.drawable.demo_reply_bar_location);
            replyBarItem.setItemLabel("位置");
            replyBarItems.add(replyBarItem);

            ReplyBarItem replyBarItem2 = new ReplyBarItem();
            replyBarItem2.setItemId(ITEM_ID_2);
            replyBarItem2.setItemImageRes(R.drawable.demo_reply_bar_profile_card);
            replyBarItem2.setItemLabel("名片");
            replyBarItems.add(replyBarItem2);

            ReplyBarItem replyBarItem3 = new ReplyBarItem();
            replyBarItem3.setItemId(ITEM_ID_3);
            replyBarItem3.setItemImageRes(R.drawable.demo_reply_bar_profile_card);
            replyBarItem3.setItemLabel("透传消息");
            replyBarItems.add(replyBarItem3);

        } else if (conversation.getConversationType() == YWConversationType.Tribe) {
            ReplyBarItem replyBarItem = new ReplyBarItem();
            replyBarItem.setItemId(ITEM_ID_1);
            replyBarItem.setItemImageRes(R.drawable.demo_reply_bar_hi);
            replyBarItem.setItemLabel("Say-Hi");
            replyBarItems.add(replyBarItem);

        }

        return replyBarItems;

    }

    private static YWConversation mConversation;

    /**
     * 当自定义的item点击时的回调
     */
    @Override
    public void onReplyBarItemClick(Fragment pointcut, ReplyBarItem item,
                                    YWConversation conversation) {
        if (conversation.getConversationType() == YWConversationType.P2P) {
            if (item.getItemId() == ITEM_ID_1) {
                sendGeoMessage(conversation);

            } else if (item.getItemId() == ITEM_ID_2) {
                Activity context = pointcut.getActivity();
                Intent intent = new Intent(context, SelectContactToSendCardActivity.class);
                context.startActivity(intent);
                mConversation = conversation;
            } else if (item.getItemId() == ITEM_ID_3){
                showTransparentMessageDialog(pointcut.getActivity(), conversation);
            }
        } else if (conversation.getConversationType() == YWConversationType.Tribe) {
            if (item.getItemId() == ITEM_ID_1) {
                sendTribeCustomMessage(conversation);
            }

        }
    }

    private void showTransparentMessageDialog(Activity context, final YWConversation conversation){
        View view = View.inflate(context, R.layout.demo_dialog_transparent_message, null);
        final EditText text = (EditText) view.findViewById(R.id.content);
        AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle("阅后即焚")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = text.getText().toString();
                        if (TextUtils.isEmpty(content)) {
                            content = "我是透传消息, 不会展示在UI!!";
                        }
                        YWCustomMessageBody messageBody = new YWCustomMessageBody();
                        JSONObject object = new JSONObject();
                        try {
                            object.put("customizeMessageType", "yuehoujifen");
                            object.put("text", content);
                        } catch (JSONException e) {

                        }
                        //TODO 设置透传标记，1表示透传消息，0表示非透传消息，默认为0，如果想要发送透传消息必须调用该方法，并且参数必须传1
                        messageBody.setTransparentFlag(1);
                        messageBody.setContent(object.toString());
                        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
                        conversation.getMessageSender().sendMessage(message, 120, null);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        if (!ad.isShowing()){
            ad.show();
        }
    }

    /**
     * 返回自定义的拍照Item,开发者通过该方法可以实现修改拍照ReplyBarItem的Icon和文案
     * @return
     *      返回null则默认使用sdk的UI
     *
     */
    @Override
    public ReplyBarItem getCustomPhotoReplyBarItem() {
//        ReplyBarItem photoReplyBarItem = new ReplyBarItem();
//        photoReplyBarItem.setItemImageRes(R.drawable.__leak_canary_icon);
//        photoReplyBarItem.setItemLabel("照相");
//        return photoReplyBarItem;
        return null;
    }

    /**
     * 返回自定义的照片选择Item,开发者通过该方法可以实现修改照片选择ReplyBarItem的Icon和文案
     * @return
     *      返回null则默认使用sdk的UI
     *
     */
    @Override
    public ReplyBarItem getCustomAlbumReplyBarItem() {
//        ReplyBarItem albumReplyBarItem = new ReplyBarItem();
//        albumReplyBarItem.setItemImageRes(R.drawable.__leak_canary_icon);
//        albumReplyBarItem.setItemLabel("照片");
//        return albumReplyBarItem;
        return null;
    }


    public static ISelectContactListener selectContactListener = new ISelectContactListener() {
        @Override
        public void onSelectCompleted(List<String> contacts) {
            if (contacts != null && contacts.size() > 0) {
                for (String userId : contacts) {
                    sendP2PCustomMessage(userId);
                }
            }
        }
    };

    @Override
    public int getFastReplyResId(YWConversation conversation) {
        return R.drawable.aliwx_reply_bar_face_bg;
    }

    @Override
    public boolean onFastReplyClick(Fragment pointcut, YWConversation ywConversation) {
        return false;
    }

    @Override
    public int getRecordResId(YWConversation conversation) {
        return 0;
    }

    @Override
    public boolean onRecordItemClick(Fragment pointcut, YWConversation ywConversation) {
        return false;
    }

    public static int count = 1;

    /**
     * 发送群自定义消息
     */
    public static void sendTribeCustomMessage(YWConversation conversation) {
        // 创建自定义消息的messageBody对象
        YWCustomMessageBody messageBody = new YWCustomMessageBody();

        // 请注意这里不一定要是JSON格式，这里纯粹是为了演示的需要
        JSONObject object = new JSONObject();
        try {
            object.put("customizeMessageType", "Greeting");
        } catch (JSONException e) {
        }

        messageBody.setContent(object.toString());// 用户要发送的自定义消息，SDK不关心具体的格式，比如用户可以发送JSON格式
        messageBody.setSummary("您收到一个招呼");// 可以理解为消息的标题，用于显示会话列表和消息通知栏
        //创建群聊自定义消息，创建单聊自定义消息和群聊自定义消息的接口不是同一个，切记不要用错！！
        YWMessage message =  YWMessageChannel.createTribeCustomMessage(messageBody);
        //发送群聊自定义消息
        conversation.getMessageSender().sendMessage(message,
                120, null);
    }

    /**
     * 发送单聊自定义消息
     * @param userId
     */
    public static void sendP2PCustomMessage(String userId) {
        //创建自定义消息的messageBody对象
        YWCustomMessageBody messageBody = new YWCustomMessageBody();

        //定义自定义消息协议，用户可以根据自己的需求完整自定义消息协议，不一定要用JSON格式，这里纯粹是为了演示的需要
        JSONObject object = new JSONObject();
        try {
            object.put("customizeMessageType", "CallingCard");
            object.put("personId", userId);
        } catch (JSONException e) {

        }

        messageBody.setContent(object.toString()); // 用户要发送的自定义消息，SDK不关心具体的格式，比如用户可以发送JSON格式
        messageBody.setSummary("[名片]"); // 可以理解为消息的标题，用于显示会话列表和消息通知栏
        //创建单聊自定义消息，创建单聊自定义消息和群聊自定义消息的接口不是同一个，切记不要用错！！
        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
        //发送单聊自定义消息
        mConversation.getMessageSender().sendMessage(message, 120, null);
    }

    /**
     * 发送单聊地理位置消息
     */
    public static void sendGeoMessage(YWConversation conversation) {
        conversation.getMessageSender().sendMessage(
                YWMessageChannel.createGeoMessage(30.2743790000,
                        120.1422530000, "浙江省杭州市西湖区"), 120, null);
    }

    /**
     * 显示地理位置消息，该方法存在性能问题，已废弃，后续请使用以下四个方法替代：
     * {@link ChattingOperationCustomSample#getCustomViewTypeCount()}, {@link ChattingOperationCustomSample#getCustomViewType(YWMessage)},
     * {@link ChattingOperationCustomSample#needHideHead(int)},{@link ChattingOperationCustomSample#getCustomGeoMessageView(Fragment, YWMessage)}
     * @param fragment
     * @param message  地理位置消息
     * @return
     */
    @Override
    public View getCustomGeoMessageView(Fragment fragment, YWMessage message) {

        YWGeoMessageBody messageBody = (YWGeoMessageBody) message.getMessageBody();
        LinearLayout layout = (LinearLayout) View.inflate(
                DemoApplication.getContext(),
                R.layout.demo_geo_message_layout, null);
        TextView textView = (TextView) layout.findViewById(R.id.content);
        textView.setText(messageBody.getAddress());
        return layout;
    }

    /**
     * 显示自定义消息，该方法存在性能问题，已废弃，后续请使用以下四个方法替代：
     * {@link ChattingOperationCustomSample#getCustomViewTypeCount()}, {@link ChattingOperationCustomSample#getCustomViewType(YWMessage)},
     * {@link ChattingOperationCustomSample#needHideHead(int)},{@link ChattingOperationCustomSample#getCustomGeoMessageView(Fragment, YWMessage)}
     * @param fragment  聊天窗口fragment
     * @param msg       需要进行自定义view的消息
     * @return  自定义view
     */
    @Override
    public View getCustomMessageView(Fragment fragment, YWMessage msg) {
        String msgType = null;
        try {
            String content = msg.getMessageBody().getContent();
            JSONObject object = new JSONObject(content);
            msgType = object.getString("customizeMessageType");
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(msgType)) {
            return null;
        }
        if (msgType.equals("Greeting")) {
            TextView greeting = new TextView(fragment.getActivity());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            greeting.setLayoutParams(params);
            greeting.setBackgroundResource(R.drawable.demo_greeting_message);
            return greeting;
        }
        return null;
    }


    /**
     * 不显示头像的自定义消息View，该方法存在性能问题，已废弃，后续请使用以下四个方法替代：
     * {@link ChattingOperationCustomSample#getCustomViewTypeCount()}, {@link ChattingOperationCustomSample#getCustomViewType(YWMessage)},
     * {@link ChattingOperationCustomSample#needHideHead(int)},{@link ChattingOperationCustomSample#getCustomGeoMessageView(Fragment, YWMessage)}
     * @param fragment      聊天窗口fragment
     * @param message       需要进行自定义view的消息
     * @param conversation  当前消息所在会话
     * @return  自定义view
     */
    @Override
    public View getCustomMessageViewWithoutHead(Fragment fragment, YWMessage message, YWConversation conversation) {
        String msgType = null;
        String userId = null;
        try {
            String content = message.getMessageBody().getContent();
            JSONObject object = new JSONObject(content);
            msgType = object.getString("customizeMessageType");
            userId = object.getString("personId");
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(msgType)) {
            return null;
        }
        if (msgType.equals("CallingCard") && !TextUtils.isEmpty(userId)) {
            RelativeLayout layout = (RelativeLayout) View.inflate(fragment.getActivity(), R.layout.demo_custom_msg_layout_without_head, null);
            ImageView head = (ImageView) layout.findViewById(R.id.head);
            TextView tv = (TextView) layout.findViewById(R.id.name);
            tv.setText(userId);

            YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
            IYWContact contact = imKit.getContactService().getContactProfileInfo(userId, LoginSampleHelper.getInstance().getIMKit().getIMCore().getAppKey());
            if (contact != null) {
                String nick = contact.getShowName();
                if (!TextUtils.isEmpty(nick)) {
                    tv.setText(nick);
                }
                String avatarPath = contact.getAvatarPath();
                if (avatarPath != null) {
                    YWContactHeadLoadHelper helper = new YWContactHeadLoadHelper(fragment.getActivity(), null);
                    helper.setCustomHeadView(head, R.drawable.aliwx_head_default, avatarPath);
                }
            }
            return layout;
        }
        return null;
    }

    /**
     * 定制自定义消息点击事件，该方法已废弃，后续请使用{@link ChattingOperationCustomSample#onMessageClick(Fragment, YWMessage)}
     */
    @Override
    public void onCustomMessageClick(Fragment fragment, YWMessage message) {
        Toast.makeText(DemoApplication.getContext(), "onCustomMessageClick",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 定制自定义消息长按事件，该方法已废弃，后续请使用{@link ChattingOperationCustomSample#onMessageLongClick(Fragment, YWMessage)}
     * @param fragment
     * @param message
     */
    @Override
    public void onCustomMessageLongClick(Fragment fragment, YWMessage message) {
        Toast.makeText(DemoApplication.getContext(), "你长按了自定义消息",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 定制地理位置消息点击事件，该方法已废弃，后续请使用{@link ChattingOperationCustomSample#onMessageClick(Fragment, YWMessage)}
     * @param fragment
     * @param message
     */
    @Override
    public void onGeoMessageClick(Fragment fragment, YWMessage message) {
        Toast.makeText(DemoApplication.getContext(), "onCustomMessageLongClick",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 定制地理位置消息长按事件，该方法已废弃，后续请使用{@link ChattingOperationCustomSample#onMessageLongClick(Fragment, YWMessage)}
     * @param fragment
     * @param message
     */
    @Override
    public void onGeoMessageLongClick(Fragment fragment, YWMessage message) {
        Toast.makeText(DemoApplication.getContext(), "onGeoMessageLongClick",
                Toast.LENGTH_SHORT).show();
    }


    /**
     * 定制点击消息事件, 每一条消息的点击事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的点击事件
     * @param fragment  聊天窗口fragment对象
     * @param message   被点击的消息
     * @return true:使用用户自定义的消息点击事件，false：使用默认的消息点击事件
     */
    @Override
    public boolean onMessageClick(Fragment fragment, YWMessage message) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO){
            Notification.showToastMsg(fragment.getActivity(), "你点击了地理位置消息消息");
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS){
            Notification.showToastMsg(fragment.getActivity(), "你点击了自定义消息");
            return true;
        }
        return false;
    }

    /**
     * 定制长按消息事件，每一条消息的长按事件都会回调该方法，开发者根据消息类型，对不同类型的消息设置不同的长按事件
     * @param fragment  聊天窗口fragment对象
     * @param message   被点击的消息
     * @return true:使用用户自定义的长按消息事件，false：使用默认的长按消息事件
     */
    @Override
    public boolean onMessageLongClick(final Fragment fragment, final YWMessage message) {
        Notification.showToastMsg(fragment.getActivity(), "触发了自定义MessageLongClick事件");

        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_IMAGE || //自定义图片长按的事件处理，无复制选项。
                message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GIF
                ) {

            new YWAlertDialog.Builder(fragment.getActivity()).setTitle(getShowName(WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()))).setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()).getMessageLoader().deleteMessage(message);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_AUDIO) {
            String[] items = new String[1];
            if (mUserInCallMode) { //当前为听筒模式
                items[0] = "使用扬声器模式";
            } else { //当前为扬声器模式
                items[0] = "使用听筒模式";
            }
            new YWAlertDialog.Builder(fragment.getActivity()).setTitle(getShowName(WXAPI.getInstance().getConversationManager().getConversationByConversationId(message.getConversationId()))).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mUserInCallMode) {
                        mUserInCallMode = false;
                    } else {
                        mUserInCallMode = true;
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO){
            Notification.showToastMsg(fragment.getActivity(), "你长按了地理位置消息");
            return true;
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS){
            Notification.showToastMsg(fragment.getActivity(), "你长按了自定义消息");
            return true;
        }

        return false;
    }

    public String getShowName(YWConversation conversation) {
        String conversationName;
        //added by 照虚  2015-3-26,有点无奈
        if (conversation == null) {
            return "";
        }

        if (conversation.getConversationType() == YWConversationType.Tribe) {
            conversationName = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
        } else {
            IYWContact contact = ((YWP2PConversationBody) conversation.getConversationBody()).getContact();
            String userId = contact.getUserId();
            String appkey = contact.getAppKey();
            conversationName = userId;
            IYWCrossContactProfileCallback callback = WXAPI.getInstance().getCrossContactProfileCallback();
            if (callback != null) {
                IYWContact iContact = callback.onFetchContactInfo(userId, appkey);
                if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                    conversationName = iContact.getShowName();
                    return conversationName;
                }
            } else {
                IYWContactProfileCallback profileCallback = WXAPI.getInstance().getContactProfileCallback();
                if (profileCallback != null) {
                    IYWContact iContact = profileCallback.onFetchContactInfo(userId);
                    if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                        conversationName = iContact.getShowName();
                        return conversationName;
                    }
                }
            }
            IYWContact iContact = WXAPI.getInstance().getWXIMContact(userId);
            if (iContact != null && !TextUtils.isEmpty(iContact.getShowName())) {
                conversationName = iContact.getShowName();
            }
        }
        return conversationName;
    }

    /**
     * 获取url对应的商品详情信息，当openIM发送或者接收到url消息时会首先调用{@link ChattingOperationCustomSample#getCustomUrlView(Fragment, YWMessage, String, YWConversation)},
     * 若getCustomUrlView()返回null，才会回调调用该方法获取商品详情，若getCustomUrlView()返回非null的view对象，则直接用此view展示url消息，不再回调该方法。因此，如果希望该方法被调用,
     * 请确保{@link ChattingOperationCustomSample#getCustomUrlView(Fragment, YWMessage, String, YWConversation)}返回null。
     * @param fragment  可以通过 fragment.getActivity拿到Context
     * @param message   url所属的message
     * @param url       url
     * @param ywConversation message所属的conversion
     * @return  商品信息
     */
    @Override
    public GoodsInfo getGoodsInfoFromUrl(Fragment fragment, YWMessage message, String url, YWConversation ywConversation) {
        if (url.equals("https://www.taobao.com/ ")) {
            Bitmap bitmap = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.pic_1_18);
            GoodsInfo info = new GoodsInfo("我的淘宝宝贝", "60.03", "86.25", "8.00", bitmap);
            return info;
        }
        return null;
    }

    /**
     * 获取url对应的自定义view,当openIM发送或者接收到url消息时会回调该方法获取该url的自定义view。若开发者实现了该方法并且返回了一个view对象，openIM将会使用该view展示对应的url消息。
     * @param fragment  可以通过 fragment.getActivity拿到Context
     * @param message   url所属的message
     * @param url       url
     * @param ywConversation message所属的conversion
     * @return  自定义Url view
     */
    @Override
    public View getCustomUrlView(Fragment fragment, YWMessage message, String url, YWConversation ywConversation) {
        if (url.equals("https://www.baidu.com/ ")) {
            LinearLayout layout = (LinearLayout) View.inflate(
                    DemoApplication.getContext(),
                    R.layout.demo_custom_tribe_msg_layout, null);
            TextView textView = (TextView) layout.findViewById(R.id.msg_content);
            textView.setText("I'm from getCustomUrlView!");
            return layout;
        }
        return null;
    }


    /**
     * 开发者可以根据用户操作设置该值
     */
    private static boolean mUserInCallMode = false;

    /**
     * 是否使用听筒模式播放语音消息
     *
     * @param fragment
     * @param message
     * @return true：使用听筒模式， false：使用扬声器模式
     */
    @Override
    public boolean useInCallMode(Fragment fragment, YWMessage message) {
        return mUserInCallMode;
    }

    /**
     * 当打开聊天窗口时，自动发送该字符串给对方
     * @param fragment      聊天窗口fragment
     * @param conversation  当前会话
     * @return 自动发送的内容（注意，内容empty则不自动发送）
     */
    @Override
    public String messageToSendWhenOpenChatting(Fragment fragment, YWConversation conversation) {
        //p2p、客服和店铺会话处理，否则不处理，
        int mCvsType = conversation.getConversationType().getValue();
        if (mCvsType == YWConversationType.P2P.getValue() || mCvsType == YWConversationType.SHOP.getValue()) {
//            return "你好";
            return null;
        } else {
            return null;
        }

    }

    /**
     * 当打开聊天窗口时，自动发送该消息给对方
     * @param fragment      聊天窗口fragment
     * @param conversation  当前会话
     * @param isConversationFirstCreated  是否是首次创建
     * @return 自动发送的消息（注意，内容empty则不自动发送）
     */
    @Override
    public YWMessage ywMessageToSendWhenOpenChatting(Fragment fragment, YWConversation conversation, boolean isConversationFirstCreated) {
//        YWMessageBody messageBody = new YWMessageBody();
//        messageBody.setSummary("WithoutHead");
//        messageBody.setContent("hi，我是单聊自定义消息之好友名片");
//        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
//        return message;
        return null;
    }


    /***************** 以下是定制自定义消息view的示例代码 ****************/

    //自定义消息view的种类数
    private final int typeCount = 3;

    /** 自定义viewType，viewType的值必须从0开始，然后依次+1递增，且viewType的个数必须等于typeCount，切记切记！！！***/
    //地理位置消息
    private final int type_0 = 0;

    //群自定义消息(Say-Hi消息)
    private final int type_1 = 1;

    //单聊自定义消息(名片消息)
    private final int type_2 = 2;


    /**
     * 自定义消息view的种类数
     * @return  自定义消息view的种类数
     */
    @Override
    public int getCustomViewTypeCount() {
        return typeCount;
    }

    /**
     * 自定义消息view的类型，开发者可以根据自己的需求定制多种自定义消息view，这里需要根据消息返回view的类型
     * @param message 需要自定义显示的消息
     * @return  自定义消息view的类型
     */
    @Override
    public int getCustomViewType(YWMessage message) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO){
            return type_0;
        }else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
            String msgType = null;
            try {
                String content = message.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                msgType = object.getString("customizeMessageType");
            } catch (Exception e) {

            }
            if (!TextUtils.isEmpty(msgType)) {
                if (msgType.equals("Greeting")) {
                    return type_1;
                } else if (msgType.equals("CallingCard")) {
                    return type_2;
                }
            }
        }
        return super.getCustomViewType(message);
    }

    /**
     * 是否需要隐藏头像
     * @param viewType 自定义view类型
     * @return true: 隐藏头像  false：不隐藏头像
     */
    @Override
    public boolean needHideHead(int viewType) {
        if (viewType == type_2) {
            return true;
        }
        return super.needHideHead(viewType);
    }

    /**
     * 根据viewType获取自定义view
     * @param fragment      聊天窗口fragment
     * @param message       当前需要自定义view的消息
     * @param convertView   自定义view
     * @param viewType      自定义view的类型
     * @param headLoadHelper    头像加载管理器，用户可以调用该对象的方法加载头像
     * @return  自定义view
     */
    @Override
    public View getCustomView(Fragment fragment, YWMessage message, View convertView, int viewType, YWContactHeadLoadHelper headLoadHelper) {
        YWLog.i(TAG, "getCustomView, type = " + viewType);
        if (viewType == type_0){ //地理位置消息
            ViewHolder0 holder = null;
            if (convertView == null){
                holder = new ViewHolder0();
                convertView = View.inflate(fragment.getActivity(), R.layout.demo_geo_message_layout, null);
                holder.address = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(holder);
                YWLog.i(TAG, "getCustomView, convertView == null");
            } else {
                holder = (ViewHolder0)convertView.getTag();
                YWLog.i(TAG, "getCustomView, convertView != null");
            }
            YWGeoMessageBody messageBody = (YWGeoMessageBody) message.getMessageBody();
            holder.address.setText(messageBody.getAddress());
            return convertView;
        }else if (viewType == type_1) {  //群聊自定义消息(Say-Hi消息)
            ViewHolder1 holder = null;
            if (convertView == null) {
                YWLog.i(TAG, "getCustomView, convertView == null");
                holder = new ViewHolder1();
                convertView = new TextView(fragment.getActivity());
                holder.greeting = (TextView)convertView;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.greeting.setLayoutParams(params);
                holder.greeting.setBackgroundResource(R.drawable.demo_greeting_message);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1)convertView.getTag();
                YWLog.i(TAG, "getCustomView, convertView != null");
            }
            return convertView;

        } else if (viewType == type_2) {  //单聊自定义消息(名片消息)
            String userId = null;
            try {
                String content = message.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                userId = object.getString("personId");
            } catch (Exception e) {
            }

            ViewHolder2 holder = null;
            if (convertView == null) {
                holder = new ViewHolder2();
                convertView = View.inflate(fragment.getActivity(), R.layout.demo_custom_msg_layout_without_head, null);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
                YWLog.i(TAG, "getCustomView, convertView == null");
            } else {
                holder = (ViewHolder2) convertView.getTag();
                YWLog.i(TAG, "getCustomView, convertView != null");
            }
            holder.name.setText(userId);

            YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
            IYWContact contact = imKit.getContactService().getContactProfileInfo(userId, imKit.getIMCore().getAppKey());
            if (contact != null) {
                String nick = contact.getShowName();
                if (!TextUtils.isEmpty(nick)) {
                    holder.name.setText(nick);
                }
                String avatarPath = contact.getAvatarPath();
                if (avatarPath != null) {
                    headLoadHelper.setCustomHeadView(holder.head, R.drawable.aliwx_head_default, avatarPath);
                }
            }
            return convertView;
        }
        return super.getCustomView(fragment, message, convertView, viewType, headLoadHelper);
    }

    public class ViewHolder0 {
        TextView address;
    }

    public class ViewHolder1 {
        TextView greeting;
    }

    public class ViewHolder2 {
        ImageView head;
        TextView name;
    }


    /**************** 以上是定制自定义消息view的示例代码 ****************/

    /**
     * 双击放大文字消息的开关
     * @param fragment
     * @return true:开启双击放大文字 false: 关闭双击放大文字
     */
    @Override
    public boolean enableDoubleClickEnlargeMessageText(Fragment fragment) {
        return true;
    }

    /**
     * 数字字符串点击事件,开发者可以根据自己的需求定制
     * @param activity
     * @param clickString 被点击的数字string
     * @param widget 被点击的TextView
     * @return false:不处理
     *         true:需要开发者在return前添加自己实现的响应逻辑代码
     */
    @Override
    public boolean onNumberClick(final Activity activity, final String clickString, final View widget) {
        ArrayList<String> menuList = new ArrayList<String>();
        menuList.add("呼叫");
        menuList.add("添加到手机通讯录");
        menuList.add("复制到剪贴板");
        final String[] items = new String[menuList.size()];
        menuList.toArray(items);
        Dialog alertDialog = new WxAlertDialog.Builder(activity)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (TextUtils.equals(items[which], "呼叫")) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + clickString));
                            activity.startActivity(intent);
                        } else if (TextUtils.equals(items[which], "添加到手机通讯录")) {
                            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                            intent.setType("vnd.android.cursor.item/person");
                            intent.setType("vnd.android.cursor.item/contact");
                            intent.setType("vnd.android.cursor.item/raw_contact");
                            intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, clickString);
                            activity.startActivity(intent);

                        } else if (TextUtils.equals(items[which], "复制到剪贴板")) {
                            ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboardManager.setText(clickString);
                        }
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                widget.invalidate();
            }
        });
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        return true;
    }
}
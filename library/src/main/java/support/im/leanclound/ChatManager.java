package support.im.leanclound;

import android.content.Context;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import support.im.data.ConversationModel;
import support.im.data.ConversationType;
import support.im.data.SimpleUser;
import support.im.data.SupportUser;
import support.im.utilities.SupportLog;
import support.ui.app.SupportApp;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatManager {

  private static ChatManager chatManager;

  private volatile AVIMClient mIMClient;
  private volatile String mClientId;
  private Context mContext;

  private ChatManager(Context context) {
    mContext = context;
  }

  public static synchronized ChatManager getInstance() {
    if (chatManager == null) {
      chatManager = new ChatManager(SupportApp.appContext());
    }
    return chatManager;
  }

  /**
   * 设置是否打印 leanchatlib 的日志，发布应用的时候要关闭
   * 日志 TAG 为 leanchatlib，可以获得一些异常日志
   */
  public static void setDebugEnabled(boolean debugEnabled) {
    SupportLog.debugEnabled = debugEnabled;
  }

  /**
   * 请在应用一启动(Application onCreate)的时候就调用，因为 SDK 一启动，就会去连接聊天服务器
   * 这里包含了一些需要设置的 handler
   */
  public void initialize() {
    // 消息处理 handler
    AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
        new SupportMessageHandler(mContext));

    // 与网络相关的 handler
    AVIMClient.setClientEventHandler(SupportClientEventHandler.getInstance());

    // 和 ConversationModel 相关的事件的 handler
    AVIMMessageManager.setConversationEventHandler(ConversationEventHandler.getInstance());

    // 签名
    //AVIMClient.setSignatureFactory(new SignatureFactory());
  }

  public String getClientId() {
    return mClientId;
  }

  public boolean isLogin() {
    return null != mIMClient;
  }

  /**
   * 连接聊天服务器，用 userId 登录，在进入MainActivity 前调用
   *
   * @param callback AVException 常发生于网络错误、签名错误
   */
  public void openClient(String clientId, final AVIMClientCallback callback) {
    mClientId = checkNotNull(clientId, "clientId cannot be null");
    //ConversationManager.getInstance().initialize(clientId);
    mIMClient = AVIMClient.getInstance(clientId);
    mIMClient.open(new AVIMClientCallback() {
      @Override public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          SupportClientEventHandler.getInstance().setConnectAndNotify(false);
        } else {
          SupportClientEventHandler.getInstance().setConnectAndNotify(true);
        }
        if (callback != null) {
          callback.done(avimClient, e);
        }
      }
    });
  }

  /**
   * 用户注销的时候调用，close 之后消息不会推送过来，也不可以进行发消息等操作
   *
   * @param callback AVException 常见于网络错误
   */
  public void closeWithCallback(final AVIMClientCallback callback) {
    mIMClient.close(new AVIMClientCallback() {

      @Override public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          SupportLog.logException(e);
        }
        if (callback != null) {
          callback.done(avimClient, e);
        }
      }
    });
    mIMClient = null;
    mClientId = null;
  }

  public void createSingleConversation(SimpleUser simpleToUser, AVIMConversationCreatedCallback callback) {
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    SupportUser supportUser = SupportUser.getCurrentUser();
    SimpleUser fromUser = supportUser.toSimpleUser();
    List<SimpleUser> members = Lists.newArrayList(simpleToUser, fromUser);
    attrs.put(ConversationModel.ATTRS_MEMBERS, members);
    final String memberId = simpleToUser.getObjectId();
    mIMClient.createConversation(Lists.newArrayList(memberId), "", attrs, false, true, callback);
  }

  public void createSingleConversation(SupportUser toUser, AVIMConversationCreatedCallback callback) {
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    SimpleUser simpleToUser = toUser.toSimpleUser();
    SupportUser supportUser = SupportUser.getCurrentUser();
    SimpleUser fromUser = supportUser.toSimpleUser();
    List<SimpleUser> members = Lists.newArrayList(simpleToUser, fromUser);
    attrs.put(ConversationModel.ATTRS_MEMBERS, members);
    final String memberId = toUser.getObjectId();
    mIMClient.createConversation(Lists.newArrayList(memberId), "", attrs, false, true, callback);
  }

  /**
   * 获取 AVIMConversationQuery，用来查询对话
   */
  public AVIMConversationQuery getConversationQuery() {
    return mIMClient.getQuery();
  }

}

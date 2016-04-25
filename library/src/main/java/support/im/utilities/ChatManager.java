package support.im.utilities;

import android.content.Context;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager {

  private static ChatManager chatManager;

  private volatile AVIMClient imClient;
  private volatile String selfId;

  private RoomsTable roomsTable;

  private ChatManager() {}

  public static synchronized ChatManager getInstance() {
    if (chatManager == null) {
      chatManager = new ChatManager();
    }
    return chatManager;
  }

  /**
   * 设置是否打印 leanchatlib 的日志，发布应用的时候要关闭
   * 日志 TAG 为 leanchatlib，可以获得一些异常日志
   *
   * @param debugEnabled
   */
  public static void setDebugEnabled(boolean debugEnabled) {
    LogUtils.debugEnabled = debugEnabled;
  }

  /**
   * 请在应用一启动(Application onCreate)的时候就调用，因为 SDK 一启动，就会去连接聊天服务器
   * 这里包含了一些需要设置的 handler
   * @param context
   */
  public void init(Context context) {

    // 消息处理 handler
    AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));

    // 与网络相关的 handler
    AVIMClient.setClientEventHandler(LeanchatClientEventHandler.getInstance());

    // 和 Conversation 相关的事件的 handler
    AVIMMessageManager.setConversationEventHandler(ConversationEventHandler.getInstance());

    // 签名
    //AVIMClient.setSignatureFactory(new SignatureFactory());
  }

  public String getSelfId() {
    return selfId;
  }

  public RoomsTable getRoomsTable() {
    return roomsTable;
  }

  public boolean isLogin() {
    return null != imClient;
  }

  /**
   * 连接聊天服务器，用 userId 登录，在进入MainActivity 前调用
   *
   * @param callback AVException 常发生于网络错误、签名错误
   */
  public void openClient(Context context, String userId, final AVIMClientCallback callback) {
    this.selfId = userId;
    roomsTable = RoomsTable.getInstanceByUserId(context, userId);

    imClient = AVIMClient.getInstance(this.selfId);
    imClient.open(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          LeanchatClientEventHandler.getInstance().setConnectAndNotify(false);
        } else {
          LeanchatClientEventHandler.getInstance().setConnectAndNotify(true);
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
    imClient.close(new AVIMClientCallback() {

      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          LogUtils.logException(e);
        }
        if (callback != null) {
          callback.done(avimClient, e);
        }
      }
    });
    imClient = null;
    selfId = null;
  }

  public void createGroupConversation(List<String> memberIds, AVIMConversationCreatedCallback callback) {
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Group.getValue());
    attrs.put("name", getConversationName(memberIds));
    imClient.createConversation(memberIds, "", attrs, false, true, callback);
  }

  public void createSingleConversation(String memberId, AVIMConversationCreatedCallback callback) {
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    imClient.createConversation(Arrays.asList(memberId), "", attrs, false, true, callback);
  }

  /**
   * 获取 AVIMConversationQuery，用来查询对话
   *
   * @return
   */
  public AVIMConversationQuery getConversationQuery() {
    return imClient.getQuery();
  }

  //ChatUser
  public List<Room> findRecentRooms() {
    return ChatManager.getInstance().getRoomsTable().selectRooms();
  }

  private String getConversationName(List<String> userIds) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < userIds.size(); i++) {
      String id = userIds.get(i);
      if (i != 0) {
        sb.append(",");
      }
      sb.append(ThirdPartUserUtils.getInstance().getUserName(id));
    }
    if (sb.length() > 50) {
      return sb.subSequence(0, 50).toString();
    } else {
      return sb.toString();
    }
  }
}

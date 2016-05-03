package support.im.leanclound;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import de.greenrobot.event.EventBus;
import java.util.List;
import support.im.leanclound.event.ConversationChangeEvent;
import support.im.utilities.SupportLog;

/**
 * 和 ConversationModel 相关的事件的 handler
 * 需要应用主动调用  AVIMMessageManager.setConversationEventHandler
 */
public class ConversationEventHandler extends AVIMConversationEventHandler {

  private static ConversationEventHandler eventHandler;

  public static synchronized ConversationEventHandler getInstance() {
    if (null == eventHandler) {
      eventHandler = new ConversationEventHandler();
    }
    return eventHandler;
  }

  private ConversationEventHandler() {}

  @Override
  public void onOfflineMessagesUnread(AVIMClient client, AVIMConversation conversation, int unreadCount) {
    SupportLog.d("onOfflineMessagesUnread");
    super.onOfflineMessagesUnread(client, conversation, unreadCount);
  }

  @Override
  public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
    SupportLog.d("onMemberLeft");
    refreshCacheAndNotify(conversation);
  }

  @Override
  public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
    SupportLog.d("onMemberJoined");
    refreshCacheAndNotify(conversation);
  }

  private void refreshCacheAndNotify(AVIMConversation conversation) {
    ConversationChangeEvent conversationChangeEvent = new ConversationChangeEvent(conversation);
    EventBus.getDefault().post(conversationChangeEvent);
  }

  @Override
  public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
    SupportLog.d("onKicked");
    refreshCacheAndNotify(conversation);
  }

  @Override
  public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
    SupportLog.d("onInvited");
    refreshCacheAndNotify(conversation);
  }
}

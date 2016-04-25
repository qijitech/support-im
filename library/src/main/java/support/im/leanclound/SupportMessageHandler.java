package support.im.leanclound;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import de.greenrobot.event.EventBus;
import support.im.R;
import support.im.data.ConversationType;
import support.im.leanclound.event.ImTypeMessageEvent;
import support.im.utilities.ConversationHelper;
import support.im.utilities.SupportLog;
import support.im.utilities.NotificationUtils;

/**
 * AVIMTypedMessage的Handler，socket过来的AVIMTypedMessage都会通过此Handler与应用交互
 * 需要应用主动调用 AVIMMessageManager.registerMessageHandler来注册
 * 当然，自定义的消息也可以通过这种方式来处理
 */
public class SupportMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

  private Context mContext;

  public SupportMessageHandler(Context context) {
    mContext = context.getApplicationContext();
  }

  @Override public void onMessage(AVIMTypedMessage message, AVIMConversation conversation,
      AVIMClient client) {
    if (message == null || message.getMessageId() == null) {
      SupportLog.d("may be SDK Bug, message or message id is null");
      return;
    }

    if (!ConversationHelper.isValidConversation(conversation)) {
      SupportLog.d("receive msg from invalid conversation");
    }

    if (ChatManager.getInstance().getSelfId() == null) {
      SupportLog.d("selfId is null, please call setupManagerWithUserId ");
      client.close(null);
    } else {
      if (!client.getClientId().equals(ChatManager.getInstance().getSelfId())) {
        client.close(null);
      } else {
        ChatManager.getInstance().getConversationsDatabase().saveConversation(message.getConversationId());
        if (!message.getFrom().equals(client.getClientId())) {
          if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
            sendNotification(message, conversation);
          }
          ChatManager.getInstance().getConversationsDatabase().increaseUnreadCount(message.getConversationId());
          sendEvent(message, conversation);
        }
      }
    }
  }

  @Override public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation,
      AVIMClient client) {
    super.onMessageReceipt(message, conversation, client);
  }

  /**
   * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
   * 稍后应该加入 db
   * @param message
   * @param conversation
   */
  private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
    ImTypeMessageEvent event = new ImTypeMessageEvent();
    event.message = message;
    event.conversation = conversation;
    EventBus.getDefault().post(event);
  }

  private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
    if (null != conversation && null != message) {
      String notificationContent = message instanceof AVIMTextMessage ?
          ((AVIMTextMessage) message).getText() : mContext.getString(R.string.support_im_un_support_message_type);

      String userName = ThirdPartUserUtils.getInstance().getUserName(message.getFrom());
      String title = (TextUtils.isEmpty(userName) ? "" : userName);

      Intent intent = new Intent();
      intent.setAction("com.avoscloud.chat.intent.client_notification");
      intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
      intent.putExtra(Constants.MEMBER_ID, message.getFrom());
      if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
        intent.putExtra(Constants.NOTOFICATION_TAG, Constants.NOTIFICATION_SINGLE_CHAT);
      } else {
        intent.putExtra(Constants.NOTOFICATION_TAG, Constants.NOTIFICATION_SINGLE_CHAT);
      }
      NotificationUtils.showNotification(mContext, title, notificationContent, null, intent);
    }
  }
}

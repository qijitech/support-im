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
import support.im.Injection;
import support.im.R;
import support.im.data.Conv;
import support.im.data.ConversationType;
import support.im.data.SimpleUser;
import support.im.data.cache.CacheManager;
import support.im.data.source.UsersRepository;
import support.im.leanclound.event.ImTypeMessageEvent;
import support.im.utilities.ConversationHelper;
import support.im.utilities.DatabaseUtils;
import support.im.utilities.NotificationUtils;
import support.im.utilities.SupportLog;
import support.ui.app.SupportApp;

/**
 * AVIMTypedMessage的Handler，socket过来的AVIMTypedMessage都会通过此Handler与应用交互
 * 需要应用主动调用 AVIMMessageManager.registerMessageHandler来注册
 * 当然，自定义的消息也可以通过这种方式来处理
 */
public class SupportMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {

  private Context mContext;
  private UsersRepository mUsersRepository;

  public SupportMessageHandler(Context context) {
    mContext = context.getApplicationContext();
    mUsersRepository = Injection.provideUsersRepository(context);
  }

  @Override public void onMessage(AVIMTypedMessage message, AVIMConversation conversation,
      AVIMClient client) {
    if (message == null || message.getMessageId() == null) {
      SupportLog.d("may be SDK Bug, message or message id is null");
      return;
    }

    // 判断AVIMConversation是否合法
    if (!ConversationHelper.isValidConversation(conversation)) {
      SupportLog.d("receive msg from invalid conversation");
    }

    // 判断是否已经打开
    final String localClientId = ChatManager.getInstance().getClientId();
    if (localClientId == null) {
      SupportLog.d("clientId is null, please call ChatManager openClient");
      client.close(null);
      return;
    }
    final String serverClientId = client.getClientId();
    if (!localClientId.equals(serverClientId)) {
      client.close(null);
      return;
    }

    CacheManager.getInstance().cacheConversation(conversation);
    Conv conv = DatabaseUtils.saveConversation(conversation, message, localClientId);
    if (!message.getFrom().equals(client.getClientId())) {
      DatabaseUtils.updateConversationUnreadCount(conversation);
      if (NotificationUtils.isShowNotification(conversation.getConversationId())) {
        sendNotification(message, conversation);
      }

      DatabaseUtils.updateConversationUnreadCount(conversation);
      CacheManager.getInstance().cacheConversation(conversation);
      sendEvent(message, conversation, conv);
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
   * @param aVIMConversation
   */
  private void sendEvent(AVIMTypedMessage message, AVIMConversation aVIMConversation, Conv conversation) {
    ImTypeMessageEvent event = new ImTypeMessageEvent();
    event.message = message;
    event.mAVIMConversation = aVIMConversation;
    event.mConversation = conversation;
    EventBus.getDefault().post(event);
  }

  private void sendNotification(AVIMTypedMessage message, AVIMConversation conversation) {
    if (null != conversation && null != message) {
      String notificationContent = message instanceof AVIMTextMessage ?
          ((AVIMTextMessage) message).getText() : mContext.getString(R.string.support_im_un_support_message_type);

      SimpleUser simpleUser = CacheManager.getInstance().getCacheSimpleUser(message.getFrom());
      String userName = null;
      if (simpleUser != null) {
        userName = simpleUser.getDisplayName();
      }
      String title = (TextUtils.isEmpty(userName) ? "" : userName);
      Intent intent = new Intent();
      intent.setAction(SupportApp.getInstance().getString(R.string.support_im_client_notification_action));
      intent.putExtra(Constants.EXTRA_CONVERSATION_ID, conversation.getConversationId());
      intent.putExtra(Constants.EXTRA_MEMBER_ID, message.getFrom());
      if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
        intent.putExtra(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_SINGLE_CHAT);
      } else {
        intent.putExtra(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_SINGLE_CHAT);
      }
      NotificationUtils.showNotification(mContext, title, notificationContent, null, intent);
    }
  }
}

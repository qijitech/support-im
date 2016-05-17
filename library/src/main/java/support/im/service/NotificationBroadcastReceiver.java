package support.im.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import support.im.chats.ChatsActivity;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.newcontacts.NewContactsActivity;

/**
 * 因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    if (!ChatManager.getInstance().isLogin()) {
      startLoginActivity(context);
      return;
    }
    String tag = intent.getStringExtra(Constants.NOTIFICATION_TAG);
    if (Constants.NOTIFICATION_GROUP_CHAT.equals(tag)) {
      startChatActivity(context, intent);
    } else if (Constants.NOTIFICATION_SINGLE_CHAT.equals(tag)) {
      startChatActivity(context, intent);
    } else if (Constants.NOTIFICATION_SYSTEM.equals(tag)) {
      startNewFriendActivity(context, intent);
    }
  }

  /**
   * 如果 app 上下文已经缺失，则跳转到登陆页面，走重新登陆的流程
   */
  private void startLoginActivity(Context context) {
    //Intent startActivityIntent = new Intent(context, EntrySplashActivity.class);
    //startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    //context.startActivity(startActivityIntent);
  }

  /**
   * 跳转至聊天页面
   */
  private void startChatActivity(Context context, Intent intent) {
    Intent startActivityIntent = new Intent(context, ChatsActivity.class);
    startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (intent.hasExtra(Constants.EXTRA_MEMBER_ID)) {
      startActivityIntent.putExtra(Constants.EXTRA_MEMBER_ID, intent.getStringExtra(Constants.EXTRA_MEMBER_ID));
    } else {
      startActivityIntent.putExtra(Constants.EXTRA_CONVERSATION_ID, intent.getStringExtra(Constants.EXTRA_CONVERSATION_ID));
    }
    context.startActivity(startActivityIntent);
  }

  private void startNewFriendActivity(Context context, Intent intent) {
    Intent startActivityIntent = new Intent(context, NewContactsActivity.class);
    startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(startActivityIntent);
  }
}

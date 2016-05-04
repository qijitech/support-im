package support.im.utilities;

import android.content.ContentValues;
import android.content.Context;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.provider.SupportImContract.Conversations;

public final class DatabaseUtils {

  public static void saveConversation(Context context, AVIMConversation avimConversation, AVIMMessage message, String clientId) {
    ContentValues values = new ContentValues();
    values.put(Conversations.CLIENT_ID, clientId);
    values.put(Conversations.CONVERSATION_ID, avimConversation.getConversationId());
    values.put(Conversations.DISPLAY_NAME, ConversationHelper.titleOfConversation(avimConversation));
    values.put(Conversations.LAST_MESSAGE_TIME, message.getTimestamp());
    values.put(Conversations.MESSAGE, message.getContent());
    context.getContentResolver().insert(Conversations.CONTENT_URI, values);
  }
}

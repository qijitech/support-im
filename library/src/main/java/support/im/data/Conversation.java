package support.im.data;

import android.database.Cursor;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.data.cache.AVIMConversationCache;
import support.im.data.source.local.ConversationsPersistenceContract;
import support.im.leanclound.ChatManager;

public class Conversation {

  public String mId;
  public String mConversationId;
  public AVIMMessage mLastMessage;
  public int mUnreadCount;

  public long getLastModifyTime() {
    if (mLastMessage != null) {
      return mLastMessage.getTimestamp();
    }

    AVIMConversation conversation = getConversation();
    if (null != conversation && null != conversation.getUpdatedAt()) {
      return conversation.getUpdatedAt().getTime();
    }

    return 0;
  }

  public AVIMConversation getConversation() {
    return AVIMConversationCache.getCacheConversation(mId);
  }


  public static Conversation createConversationFromCursor(Cursor cursor) {
    Conversation conversation = new Conversation();
    conversation.mId = cursor.getString(cursor.getColumnIndex(ConversationsPersistenceContract.Conversations._ID));
    conversation.mConversationId = cursor.getString(cursor.getColumnIndex(ConversationsPersistenceContract.Conversations.COLUMN_NAME_CONVERSATION_ID));
    conversation.mUnreadCount = cursor.getInt(cursor.getColumnIndex(ConversationsPersistenceContract.Conversations.COLUMN_NAME_UNREAD_COUNT));
    return conversation;
  }

  public static Conversation createConversation(AVIMConversation avimConversation) {
    Conversation conversation = new Conversation();
    conversation.mConversationId = avimConversation.getConversationId();
    conversation.mUnreadCount = 1;
    conversation = ChatManager.getInstance().getConversationsDatabase().saveConversation(conversation);
    return conversation;
  }

}

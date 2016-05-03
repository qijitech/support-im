package support.im.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import support.im.data.Conversation;
import support.im.data.source.local.ConversationsPersistenceContract.Conversations;

public class ConversationsDatabase {

  private ConversationsDbHelper mDbHelper;
  private static Map<String, ConversationsDatabase> sDatabases = new ConcurrentHashMap<>();

  private ConversationsDatabase(ConversationsDbHelper dbHelper) {
    mDbHelper = dbHelper;
  }

  public synchronized static ConversationsDatabase databaseWithClientId(Context context, String clientId) {
    ConversationsDatabase database = sDatabases.get(clientId);
    if (database == null) {
      database = new ConversationsDatabase(new ConversationsDbHelper(context.getApplicationContext(), clientId));
      sDatabases.put(clientId, database);
    }
    return database;
  }

  public List<Conversation> loadConversations() {
    SQLiteDatabase db = mDbHelper.getReadableDatabase();
    Cursor cursor = null;
    try {
      List<Conversation> conversations = Lists.newArrayList();
      cursor = db.query(Conversations.TABLE_NAME, null, null, null, null, null, null);
      while (cursor != null && cursor.moveToNext()) {
        Conversation conversation = Conversation.createConversationFromCursor(cursor);
        conversations.add(conversation);
      }
      return conversations;
    } finally {
      if (cursor != null) {
        cursor.close();
        mDbHelper.close();
      }
    }
  }

  public Conversation saveConversation(Conversation conversation) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    try {
      ContentValues cv = new ContentValues();
      cv.put(Conversations.COLUMN_NAME_CONVERSATION_ID, conversation.mConversationId);
      cv.put(Conversations.COLUMN_NAME_UNREAD_COUNT, 1);
      long id = db.insertWithOnConflict(Conversations.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
      conversation.mId = String.valueOf(id);
      return conversation;
    } finally {
      db.close();
    }
  }

  public long saveConversation(String conversationId) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    try {
      ContentValues cv = new ContentValues();
      cv.put(Conversations.COLUMN_NAME_CONVERSATION_ID, conversationId);
      return db.insertWithOnConflict(Conversations.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    } finally {
      db.close();
    }
  }

  public void deleteConversation(String conversationId) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    try {
      ContentValues cv = new ContentValues();
      cv.put(Conversations.COLUMN_NAME_CONVERSATION_ID, conversationId);
      db.delete(Conversations.TABLE_NAME, getWhereClause(Conversations.COLUMN_NAME_CONVERSATION_ID), new String[] {conversationId});
    } finally {
      db.close();
    }
  }

  /**
   * 此处的消息未读数量仅仅指的是本机的未读消息数量，并没有存储到 server 端
   * 在收到消息时消息未读数量 + 1
   * @param conversationId
   */
  public void increaseUnreadCount(String conversationId) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    try {
      db.execSQL(Conversations.SQL.UPDATE_CONVERSATIONS_INCREASE_UNREAD_COUNT_WHERE_CONVERSATION_ID,
          new String[] { conversationId });
    } finally {
      db.close();
    }
  }

  /**
   * 清除未读消息数量
   * @param conversationId
   */
  public void clearUnread(String conversationId) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    try {
      ContentValues cv = new ContentValues();
      cv.put(Conversations.COLUMN_NAME_UNREAD_COUNT, 0);
      db.update(Conversations.TABLE_NAME, cv, getWhereClause(Conversations.COLUMN_NAME_CONVERSATION_ID),
          new String[]{conversationId});
    } finally {
      db.close();
    }

  }

  private static String getWhereClause(String... columns) {
    List<String> conditions = Lists.newArrayList();
    for (String column : columns) {
      conditions.add(column + " = ? ");
    }
    return TextUtils.join(" AND ", conditions);
  }
}

package support.im.database;

import android.content.Context;
import com.google.common.collect.Lists;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SupportsDbHelper {

  private SupportsDatabase mDbHelper;
  private static Map<String, SupportsDbHelper> sDatabases = new ConcurrentHashMap<>();

  private SupportsDbHelper(SupportsDatabase database) {
    mDbHelper = database;
  }

  public synchronized static SupportsDbHelper dbHelper(Context context, String clientId) {
    SupportsDbHelper database = sDatabases.get(clientId);
    if (database == null) {
      database = new SupportsDbHelper(new SupportsDatabase(context.getApplicationContext(), clientId));
      sDatabases.put(clientId, database);
    }
    return database;
  }

  public List<Conversation> loadConversations() {
    SquidCursor<Conversation> squidCursor = mDbHelper.query(Conversation.class, Query.select());
    try {
      List<Conversation> conversations = Lists.newArrayList();
      while (squidCursor.moveToNext()) {
        Conversation conversation2 = new Conversation();
        conversation2.readPropertiesFromCursor(squidCursor);
        conversations.add(conversation2);
      }
      return conversations;
    } finally {
      squidCursor.close();
    }
  }

  public Conversation saveOrUpdate(String conversationId) {
    Conversation conversation = findByConversationId(conversationId);
    if (conversation == null) { // save
      conversation = new Conversation();
      conversation.setConversationId(conversationId);
    }
    return  saveOrUpdate(conversation);
  }

  public Conversation saveOrUpdate(Conversation conversation) {
    Conversation c = findByConversationId(conversation.getConversationId());
    if (c != null) { // save
      conversation.setId(c.getId());
    }
    mDbHelper.persist(conversation);
    return  conversation;
  }

  private static final String UPDATE_CONVERSATIONS_INCREASE_UNREAD_COUNT =
      "UPDATE " + Conversation.TABLE.getName() + " SET " + Conversation.UN_READ_COUNT.getName() + " = "
          + Conversation.UN_READ_COUNT.getName() + " + 1 WHERE " + Conversation.CONVERSATION_ID.getName() + " =?";

  public void increaseUnreadCount(String conversationId) {
    mDbHelper.execSqlOrThrow(UPDATE_CONVERSATIONS_INCREASE_UNREAD_COUNT, new Object[]{conversationId});
  }

  private Conversation findByConversationId(String conversationId) {
    Query query = Query.select().where(Conversation.CONVERSATION_ID.eq(conversationId));
    SquidCursor<Conversation> squidCursor = mDbHelper.query(Conversation.class, query);
    try {
      if (squidCursor.moveToNext()) {
        Conversation conversation = new Conversation();
        conversation.readPropertiesFromCursor(squidCursor);
        return conversation;
      }
    } finally {
      squidCursor.close();
    }
    return null;
  }
}

package support.im.database;

import android.content.Context;
import com.google.common.collect.Lists;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import support.im.data.Conversation2;

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

  public List<Conversation2> loadConversations() {
    SquidCursor<Conversation2> squidCursor = mDbHelper.query(Conversation2.class, Query.select());
    try {
      List<Conversation2> conversations = Lists.newArrayList();
      while (squidCursor.moveToNext()) {
        Conversation2 conversation2 = new Conversation2();
        conversation2.readPropertiesFromCursor(squidCursor);
        conversations.add(conversation2);
      }
      return conversations;
    } finally {
      squidCursor.close();
    }
  }

  public Conversation2 saveOrUpdate(String conversationId) {
    Conversation2 conversation = findByConversationId(conversationId);
    if (conversation == null) { // save
      conversation = new Conversation2();
      conversation.setConversationId(conversationId);
    } else {
      conversation.setUnReadCount(conversation.getUnReadCount() + 1);
    }
    mDbHelper.persist(conversation);
    return  conversation;
  }

  public Conversation2 saveOrUpdate(Conversation2 conversation) {
    mDbHelper.persist(conversation);
    return  conversation;
  }

  private Conversation2 findByConversationId(String conversationId) {
    Query query = Query.select().where(Conversation2.CONVERSATION_ID.eq(conversationId));
    SquidCursor<Conversation2> squidCursor = mDbHelper.query(Conversation2.class, query);
    try {
      if (squidCursor.moveToNext()) {
        Conversation2 conversation2 = new Conversation2();
        conversation2.readPropertiesFromCursor(squidCursor);
        return conversation2;
      }
    } finally {
      squidCursor.close();
    }
    return null;
  }
}

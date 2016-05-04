package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import support.im.data.Conversation;
import support.im.data.source.SimpleConversationsDataSource;
import support.ui.app.SupportApp;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource extends SimpleConversationsDataSource {

  private SupportImDbHelper mDbHelper;
  private static Map<String, ConversationsLocalDataSource> sDatabases = new ConcurrentHashMap<>();

  // Prevent direct instantiation.
  public ConversationsLocalDataSource(String clientId) {
    mDbHelper = SupportImDbHelper.databaseWithClientId(SupportApp.appContext(), clientId);
  }

  public synchronized static ConversationsLocalDataSource getInstance(String clientId) {
    ConversationsLocalDataSource database = sDatabases.get(clientId);
    if (database == null) {
      database = new ConversationsLocalDataSource(clientId);
      sDatabases.put(clientId, database);
    }
    return database;
  }

  @Override public void loadConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    // 获取本地conversations
    final List<Conversation> conversations = mDbHelper.loadConversations();
    if (conversations.isEmpty()) {
      callback.onConversationsNotFound();
      return;
    }
    callback.onConversationsLoaded(conversations);
  }

  @Override public void saveConversation(@NonNull Conversation conversation) {
    mDbHelper.saveConversation(conversation);
  }
}

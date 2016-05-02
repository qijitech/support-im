package support.im.leanclound;

import android.content.Context;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.local.ConversationsDatabase;
import support.ui.app.SupportApp;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationManager {

  private static ConversationManager conversationManager;

  private ConversationsDatabase mConversationsDatabase;
  private volatile String mClientId;
  private Context mContext;

  private ConversationManager(Context context) {
    mContext = context;
  }

  public static synchronized ConversationManager getInstance() {
    if (conversationManager == null) {
      conversationManager = new ConversationManager(SupportApp.appContext());
    }
    return conversationManager;
  }

  public void initialize(String clientId) {
    mClientId = checkNotNull(clientId, "clientId cannot be null");
    if (mConversationsDatabase == null) {
      mConversationsDatabase = ConversationsDatabase.databaseWithUserId(mContext, clientId);
    }
  }

  public ConversationsDatabase getConversationsDatabase() {
    checkNotNull(mClientId, "please invoke initialize(String) ");
    if (mConversationsDatabase == null) {
      mConversationsDatabase = ConversationsDatabase.databaseWithUserId(mContext, mClientId);
    }
    return mConversationsDatabase;
  }

  public List<Conversation> findRecentConversations() {
    return getConversationsDatabase().loadConversations();
  }

}

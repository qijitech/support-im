package support.im.leanclound;

import com.avos.avoscloud.AVException;
import java.util.ArrayList;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.utilities.AVIMConversationCacheUtils;

public class ConversationManager {

  private static ConversationManager conversationManager;

  public ConversationManager() {
  }

  public static synchronized ConversationManager getInstance() {
    if (conversationManager == null) {
      conversationManager = new ConversationManager();
    }
    return conversationManager;
  }

  public void findAndCacheConversations(final ConversationsDataSource.LoadConversationCallback callback) {
    final List<Conversation> conversations = ChatManager.getInstance().findRecentConversations();
    List<String> conversationIds = new ArrayList<>();
    for (Conversation conversation : conversations) {
      conversationIds.add(conversation.mConversationId);
    }

    if (conversationIds.size() > 0) {
      AVIMConversationCacheUtils.cacheConversations(conversationIds, new AVIMConversationCacheUtils.CacheConversationCallback() {
        @Override
        public void done(AVException e) {
          callback.onConversationsLoaded(conversations);
        }
      });
    } else {
      callback.onConversationsLoaded(conversations);
    }
  }
}

package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import support.im.data.Conversation;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsRepository implements ConversationsDataSource {

  private static ConversationsRepository INSTANCE = null;

  private final ConversationsDataSource mConversationsLocalDataSource;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  Map<String, Conversation> mCachedConversations;

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */
  boolean mCacheIsDirty = false;

  // Prevent direct instantiation.
  private ConversationsRepository(@NonNull ConversationsDataSource conversationsDataSource) {
    mConversationsLocalDataSource = checkNotNull(conversationsDataSource);
  }

  public static ConversationsRepository getInstance(ConversationsDataSource conversationsDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsRepository(conversationsDataSource);
    }
    return INSTANCE;
  }

  /**
   * Used to force {@link #getInstance(ConversationsDataSource)} to create a new instance
   * next time it's called.
   */
  public static void destroyInstance() {
    INSTANCE = null;
  }

  @Override public void loadConversations(@NonNull final LoadConversationCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedConversations != null && !mCacheIsDirty) {
      callback.onConversationsLoaded(new ArrayList<>(mCachedConversations.values()));
      return;
    }

    mConversationsLocalDataSource.loadConversations(new LoadConversationCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        refreshCache(conversations);
        callback.onConversationsLoaded(new ArrayList<>(mCachedConversations.values()));
      }

      @Override public void onDataNotAvailable(AVIMException e) {
      }
    });
  }

  @Override public void refreshConversations() {

  }

  private void refreshCache(List<Conversation> conversations) {
    if (mCachedConversations == null) {
      mCachedConversations = new LinkedHashMap<>();
    }
    mCachedConversations.clear();
    for (Conversation conversation : conversations) {
      mCachedConversations.put(conversation.mId, conversation);
    }
    mCacheIsDirty = false;
  }
}

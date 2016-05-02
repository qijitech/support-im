package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import support.im.data.Conversation;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsRepository implements ConversationsDataSource {

  private static ConversationsRepository INSTANCE = null;

  private final ConversationsDataSource mConversationsLocalDataSource;
  private final ConversationsDataSource mConversationsRemoteDataSource;

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
  private ConversationsRepository(@NonNull ConversationsDataSource conversationsLocalDataSource,
      @NonNull ConversationsDataSource conversationsRemoteDataSource) {
    mConversationsLocalDataSource = checkNotNull(conversationsLocalDataSource);
    mConversationsRemoteDataSource = checkNotNull(conversationsRemoteDataSource);
  }

  public static ConversationsRepository getInstance(
      ConversationsDataSource conversationsLocalDataSource,
      ConversationsDataSource conversationsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsRepository(conversationsLocalDataSource,
          conversationsRemoteDataSource);
    }
    return INSTANCE;
  }

  /**
   * Used to force {@link #getInstance(ConversationsDataSource, ConversationsDataSource)} to create a new instance
   * next time it's called.
   */
  public static void destroyInstance() {
    INSTANCE = null;
  }

  @Override public void loadCachedConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedConversations != null && !mCacheIsDirty) {
      callback.onConversationsLoaded(new ArrayList<>(mCachedConversations.values()));
      return;
    }

    mConversationsLocalDataSource.loadCachedConversations(new LoadConversationsCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        callback.onConversationsLoaded(conversations);
      }
      @Override public void onConversationsNotFound() {
        callback.onConversationsNotFound();
      }
      @Override public void onDataNotAvailable(AVIMException e) {
        callback.onDataNotAvailable(e);
      }
    });

  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull final GetLastMessageCallback callback) {
    mConversationsRemoteDataSource.getLastMessage(conversation, new GetLastMessageCallback() {
      @Override public void onLastMessageLoaded(AVIMMessage avimMessage) {
        callback.onLastMessageLoaded(avimMessage);
      }
      @Override public void onLastMessageNotFound() {
        callback.onLastMessageNotFound();
      }
      @Override public void onDataNotAvailable(AVIMException e) {
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void loadServerConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedConversations != null && !mCacheIsDirty) {
      callback.onConversationsLoaded(new ArrayList<>(mCachedConversations.values()));
      return;
    }

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getAVIMConversationsFromRemoteDataSource(callback);
    }
  }

  private void getAVIMConversationsFromRemoteDataSource(final LoadConversationsCallback callback) {
    mConversationsRemoteDataSource.loadServerConversations(new LoadConversationsCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        refreshConversationCache(conversations);
        callback.onConversationsLoaded(conversations);
      }

      @Override public void onConversationsNotFound() {
        callback.onConversationsNotFound();
      }

      @Override public void onDataNotAvailable(AVIMException e) {
        callback.onDataNotAvailable(e);
      }
    });
  }

  private void refreshConversationCache(List<Conversation> conversations) {
    if (mCachedConversations == null) {
      mCachedConversations = Maps.newLinkedHashMap();
    }
    mCachedConversations.clear();
    for (Conversation conversation : conversations) {
      mCachedConversations.put(conversation.mConversationId, conversation);
    }
    mCacheIsDirty = false;
  }

  @Override public void refreshConversations() {
    mCacheIsDirty = true;
  }
}

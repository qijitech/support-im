package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import support.im.data.Conv;
import support.im.data.cache.CacheManager;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsRepository implements ConversationsDataSource {

  private static ConversationsRepository INSTANCE = null;

  private final ConversationsDataSource mConversationsLocalDataSource;
  private final ConversationsDataSource mConversationsRemoteDataSource;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  Map<String, Conv> mCachedConversations;

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

  @Override public void findConversations(@NonNull List<String> ids,
      @NonNull AVIMConversationQueryCallback callback) {
    mConversationsRemoteDataSource.findConversations(ids, callback);
  }

  @Override public void loadConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedConversations != null && !mCacheIsDirty) {
      callback.onConversationsLoaded(Lists.newArrayList(mCachedConversations.values()));
      return;
    }

    mConversationsLocalDataSource.loadConversations(new LoadConversationsCallback() {
      @Override public void onConversationsLoaded(final List<Conv> conversations) {
        refreshConversationCache(conversations);
        // 判断本地是否有AVIMConversation缓存
        List<String> unCachedConversationIds = Lists.newArrayList();
        for (Conv conversation : conversations) {
          final String conversationId = conversation.getConversationId();
          if (!CacheManager.hasCacheConversation(conversationId)) {
            unCachedConversationIds.add(conversationId);
          }
        }

        // 如果有就不用像服务器获取
        if (unCachedConversationIds.isEmpty()) {
          callback.onConversationsLoaded(conversations);
          return;
        }

        findConversations(unCachedConversationIds, new AVIMConversationQueryCallback() {
          @Override public void done(List<AVIMConversation> aVIMConversations, AVIMException e) {
            if (AVExceptionHandler.handAVException(e, false)) {
              for (AVIMConversation aVIMConversation : aVIMConversations) {
                CacheManager.cacheConversation(aVIMConversation);
              }
              callback.onConversationsLoaded(conversations);
            }
          }
        });
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
      @NonNull GetLastMessageCallback callback) {
    mConversationsRemoteDataSource.getLastMessage(conversation, callback);
  }

  @Override public void saveConversation(@NonNull Conv conversation) {
    mConversationsLocalDataSource.saveConversation(conversation);
  }

  @Override public void refreshConversations() {
    mCacheIsDirty = true;
  }

  private void refreshConversationCache(List<Conv> conversations) {
    if (mCachedConversations == null) {
      mCachedConversations = Maps.newLinkedHashMap();
    }
    mCachedConversations.clear();
    for (Conv conversation : conversations) {
      mCachedConversations.put(conversation.getConversationId(), conversation);
    }
    mCacheIsDirty = false;
  }
}

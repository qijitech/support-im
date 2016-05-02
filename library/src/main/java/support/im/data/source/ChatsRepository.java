package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsRepository implements ChatsDataSource {

  private static ChatsRepository INSTANCE = null;

  private final ChatsDataSource mChatsRemoteDataSource;

  boolean mCacheIsDirty = false;
  Map<String, AVIMMessage> mCachedMessages;

  // Prevent direct instantiation.
  private ChatsRepository(@NonNull ChatsDataSource chatsRemoteDataSource) {
    mChatsRemoteDataSource = checkNotNull(chatsRemoteDataSource);
  }

  public static ChatsRepository getInstance(ChatsDataSource chatsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ChatsRepository(chatsRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override public void loadMessages(@NonNull final LoadMessagesCallback callback) {
    checkNotNull(callback);
    // Respond immediately with cache if available and not dirty
    if (mCachedMessages != null && !mCacheIsDirty) {
      callback.onMessagesLoaded(Lists.<AVIMMessage>newArrayList(mCachedMessages.values()));
      return;
    }

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getMessagesFromRemoteDataSource(callback);
    }

  }

  private void getMessagesFromRemoteDataSource(@NonNull final LoadMessagesCallback callback) {
    mChatsRemoteDataSource.loadMessages(new LoadMessagesCallback() {
      @Override public void onMessagesLoaded(List<AVIMMessage> messages) {
        refreshCache(messages);
        callback.onMessagesLoaded(messages);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  @Override public void loadMessages(@NonNull String messageId, long timestamp, int limit,
      @NonNull LoadMessagesCallback callback) {

  }

  private void refreshCache(List<AVIMMessage> messages) {
    if (mCachedMessages == null) {
      mCachedMessages = Maps.newLinkedHashMap();
    }
    mCachedMessages.clear();
    for (AVIMMessage message : messages) {
      mCachedMessages.put(message.getMessageId(), message);
    }
    mCacheIsDirty = false;
  }

  @Override public void refreshMessages() {

  }
}

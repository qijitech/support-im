package support.im.data.source;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import support.im.data.User;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsRepository implements ChatsDataSource {

  private static ChatsRepository INSTANCE = null;

  private final ChatsDataSource mChatsRemoteDataSource;

  boolean mCacheIsDirty = false;
  ArrayMap<String, LinkedHashMap<String, AVIMMessage>> mCachedMessages;

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

  @Override public void loadMessages(@NonNull final AVIMConversation aVIMConversation, @NonNull final String messageId,
      long timestamp, int limit,
      @NonNull final LoadMessagesCallback callback) {

    checkNotNull(callback);
    // Respond immediately with cache if available and not dirty
    LinkedHashMap<String, AVIMMessage> cachedMessage = null;
    if (!mCacheIsDirty && mCachedMessages != null &&
        (cachedMessage = mCachedMessages.get(aVIMConversation.getConversationId())) != null) {
      callback.onMessagesLoaded(Lists.<AVIMMessage>newArrayList(cachedMessage.values()));
      return;
    }

    mChatsRemoteDataSource.loadMessages(aVIMConversation, messageId, timestamp, limit, new LoadMessagesCallback() {
      @Override public void onMessagesLoaded(List<AVIMMessage> messages) {
        refreshCache(aVIMConversation, messages);
        callback.onMessagesLoaded(messages);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  private void refreshCache(@NonNull AVIMConversation aVIMConversation, List<AVIMMessage> messages) {
    if (mCachedMessages == null) {
      mCachedMessages = new ArrayMap<>();
    }
    LinkedHashMap<String, AVIMMessage> cachedMessage = mCachedMessages.get(aVIMConversation.getConversationId());
    if (cachedMessage == null) {
      cachedMessage = Maps.newLinkedHashMap();
      mCachedMessages.put(aVIMConversation.getConversationId(), cachedMessage);
    }
    for (AVIMMessage message : messages) {
      cachedMessage.put(message.getMessageId(), message);
    }
    mCacheIsDirty = false;
  }

  @Override public void refreshMessages() {
    mCacheIsDirty = true;
  }

  @Override public void sendMessage(@NonNull AVIMConversation aVIMConversation, @NonNull AVIMMessage message,
      @NonNull final GetMessageCallback callback) {
    checkNotNull(aVIMConversation);
    checkNotNull(message);
    checkNotNull(callback);

    mChatsRemoteDataSource.sendMessage(aVIMConversation, message, new GetMessageCallback() {
      @Override public void onMessageLoaded(AVIMMessage message) {
        callback.onMessageLoaded(message);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  @Override public void createConversation(@NonNull User toUser,
      @NonNull final AVIMConversationCreatedCallback callback) {
    checkNotNull(toUser);
    checkNotNull(callback);
    mChatsRemoteDataSource.createConversation(toUser, callback);
  }
}

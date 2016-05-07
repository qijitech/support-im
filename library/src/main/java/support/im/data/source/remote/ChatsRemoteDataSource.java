package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import support.im.data.ConversationType;
import support.im.data.User;
import support.im.data.source.ChatsDataSource;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsRemoteDataSource implements ChatsDataSource {


  private ChatsRemoteDataSource() {
  }

  private static ChatsRemoteDataSource INSTANCE = null;

  public static ChatsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ChatsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void loadMessages(@NonNull AVIMConversation aVIMConversation,
      @NonNull final LoadMessagesCallback callback) {
    checkNotNull(aVIMConversation);
    checkNotNull(callback);
    aVIMConversation.queryMessages(new AVIMMessagesQueryCallback() {
      @Override public void done(List<AVIMMessage> messages, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessagesLoaded(messages);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void loadMessages(@NonNull AVIMConversation aVIMConversation, @NonNull String messageId, long timestamp, int limit,
      @NonNull final LoadMessagesCallback callback) {
    checkNotNull(aVIMConversation);
    checkNotNull(messageId);
    aVIMConversation.queryMessages(messageId, timestamp, limit, new AVIMMessagesQueryCallback() {
      @Override public void done(List<AVIMMessage> messages, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessagesLoaded(messages);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void refreshMessages() {

  }

  @Override public void sendMessage(@NonNull AVIMConversation aVIMConversation, @NonNull final AVIMTypedMessage message,
      @NonNull final GetMessageCallback callback) {
    checkNotNull(aVIMConversation);
    checkNotNull(message);
    checkNotNull(callback);

    aVIMConversation.sendMessage(message, new AVIMConversationCallback() {
      @Override public void done(AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessageLoaded(message);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void createConversation(@NonNull User toUser,
      @NonNull final AVIMConversationCreatedCallback callback) {
    checkNotNull(toUser);
    checkNotNull(callback);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    final String memberId = toUser.getObjectId();
    ChatManager.getInstance().getAVIMClient().createConversation(Lists.newArrayList(memberId),
        toUser.getDisplayName(), attrs, false/*isTransient*/, true /*isUnique*/, callback);
  }
}

package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import java.util.List;
import support.im.data.source.ChatsDataSource;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsRemoteDataSource implements ChatsDataSource {

  private final AVIMConversation mAVIMConversation;

  private ChatsRemoteDataSource(@NonNull AVIMConversation aVIMConversation) {
    mAVIMConversation = checkNotNull(aVIMConversation);
  }

  private static ChatsRemoteDataSource INSTANCE = null;

  public static ChatsRemoteDataSource getInstance(AVIMConversation aVIMConversation) {
    if (INSTANCE == null) {
      INSTANCE = new ChatsRemoteDataSource(aVIMConversation);
    }
    return INSTANCE;
  }

  @Override public void loadMessages(@NonNull final LoadMessagesCallback callback) {
    mAVIMConversation.queryMessages(new AVIMMessagesQueryCallback() {
      @Override public void done(List<AVIMMessage> messages, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessagesLoaded(messages);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void loadMessages(@NonNull String messageId, long timestamp, int limit,
      @NonNull final LoadMessagesCallback callback) {
    checkNotNull(messageId);
    mAVIMConversation.queryMessages(messageId, timestamp, limit, new AVIMMessagesQueryCallback() {
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
}

package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;

public class ConversationsRemoteDataSource implements ConversationsDataSource {

  private static ConversationsRemoteDataSource INSTANCE;

  // Prevent direct instantiation.
  public ConversationsRemoteDataSource() {
  }

  public static ConversationsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void loadConversations(@NonNull final LoadConversationCallback callback) {
  }

  @Override public void loadConversations(@NonNull List<Conversation> conversations,
      @NonNull LoadConversationCallback callback) {

  }

  @Override public void loadAVIMConversations(@NonNull final LoadAVIMConversationsCallback callback) {
    AVIMConversationQuery conversationQuery = ChatManager.getInstance().getConversationQuery();
    conversationQuery.setLimit(1000);
    conversationQuery.setQueryPolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
    conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
      @Override public void done(List<AVIMConversation> aVIMConversations, AVIMException e) {
        if (!AVExceptionHandler.handAVException(e, false)) {
          callback.onDataNotAvailable(e);
        }
        if (aVIMConversations != null && aVIMConversations.size() > 0) {
          callback.onAVIMConversationsLoaded(aVIMConversations);
        } else {
          callback.onAVIMConversationsNotFound();
        }
      }
    });
  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull final GetLastMessageCallback callback) {
    conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
      @Override
      public void done(AVIMMessage avimMessage, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onLastMessageLoaded(avimMessage);
        } else {
          callback.onDataNotAvailable(e);
        }
      }
    });
  }

  @Override public void refreshConversations() {

  }
}

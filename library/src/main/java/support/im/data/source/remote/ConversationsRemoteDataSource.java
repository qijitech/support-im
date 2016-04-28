package support.im.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.cache.AVIMConversationCache;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.local.ConversationsDatabase;
import support.im.leanclound.ChatManager;
import support.im.leanclound.ConversationManager;
import support.im.leanclound.SupportImClientManager;
import support.im.utilities.AVExceptionHandler;

public class ConversationsRemoteDataSource implements ConversationsDataSource {

  private static ConversationsRemoteDataSource INSTANCE;

  private ConversationManager mConversationManager;

  // Prevent direct instantiation.
  public ConversationsRemoteDataSource(Context context) {
    mConversationManager = ConversationManager.getInstance();
  }

  public static ConversationsRemoteDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsRemoteDataSource(context);
    }
    return INSTANCE;
  }

  @Override public void loadConversations(@NonNull final LoadConversationCallback callback) {
    AVIMConversationCache.findConversations(new AVIMConversationQueryCallback() {
      @Override public void done(List<AVIMConversation> conversations, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          final ConversationsDatabase database = ChatManager.getInstance().getConversationsDatabase();
          for (AVIMConversation conversation : conversations) {
            conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
              @Override
              public void done(AVIMMessage avimMessage, AVIMException e) {
                if (AVExceptionHandler.handAVException(e, false) && avimMessage != null) {
                  database.saveConversation(avimMessage.getConversationId());
                  database.increaseUnreadCount(avimMessage.getConversationId());
                  Conversation c = new Conversation();
                  c.mLastMessage = avimMessage;
                  c.mUnreadCount = 1;
                  c.mConversationId = avimMessage.getConversationId();
                }
              }
            });
          }
        } else {
          callback.onDataNotAvailable(e);
        }
      }
    });
  }

  @Override public void refreshConversations() {

  }
}

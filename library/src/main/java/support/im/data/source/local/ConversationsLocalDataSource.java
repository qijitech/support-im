package support.im.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.leanclound.ConversationManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource implements ConversationsDataSource {

  private static ConversationsLocalDataSource INSTANCE;

  private Context mContext;

  private ConversationManager mConversationManager;

  // Prevent direct instantiation.
  public ConversationsLocalDataSource(Context context) {
    mContext = checkNotNull(context);
    mConversationManager = ConversationManager.getInstance();
  }

  public static ConversationsLocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsLocalDataSource(context);
    }
    return INSTANCE;
  }

  @Override public void loadConversations(@NonNull final LoadConversationCallback callback) {
    mConversationManager.findAndCacheConversations(new LoadConversationCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        for (final Conversation conversation : conversations) {
          AVIMConversation conv = conversation.getConversation();
          if (conv != null) {
            conv.getLastMessage(new AVIMSingleMessageQueryCallback() {
              @Override
              public void done(AVIMMessage avimMessage, AVIMException e) {
                if (avimMessage != null) {
                  conversation.mLastMessage = avimMessage;
                }
              }
            });
          }
        }
        callback.onConversationsLoaded(conversations);
      }
      @Override public void onDataNotAvailable() {
        callback.onDataNotAvailable();
      }
    });
  }

  @Override public void refreshConversations() {

  }
}

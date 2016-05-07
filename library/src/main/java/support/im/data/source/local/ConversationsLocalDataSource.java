package support.im.data.source.local;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.SimpleConversationsDataSource;
import support.im.leanclound.ChatManager;
import support.im.utilities.DatabaseUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource extends SimpleConversationsDataSource {

  private String mClientId;

  // Prevent direct instantiation.
  public ConversationsLocalDataSource(String clientId) {
    mClientId = clientId;
  }

  private static ConversationsLocalDataSource INSTANCE = null;

  public static ConversationsLocalDataSource getInstance(String clientId) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsLocalDataSource(clientId);
    }
    return INSTANCE;
  }

  @Override public void loadConversation(@NonNull String userObjectId,
      final LoadConversationCallback callback) {
    DatabaseUtils.findRecentConv(mClientId, userObjectId, new DatabaseUtils.FindConversationCallback() {
      @Override public void onSuccess(Conversation conversation) {
        if (conversation == null) {
          callback.onConversationNotFound();
          return;
        }
        callback.onConversationLoaded(conversation);
      }
    });
  }

  @Override public void loadConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    DatabaseUtils.findRecentConv(mClientId, new DatabaseUtils.FindConversationsCallback() {
      @Override public void onSuccess(List<Conversation> conversations) {
        if (conversations.isEmpty()) {
          callback.onConversationsNotFound();
          return;
        }
        callback.onConversationsLoaded(conversations);
      }
    });
  }

  @Override public void saveConversation(@NonNull AVIMConversation avimConversation,
      @NonNull AVIMMessage avimMessage) {
    DatabaseUtils.saveConversation(avimConversation, avimMessage, ChatManager.getInstance().getClientId());
  }
}

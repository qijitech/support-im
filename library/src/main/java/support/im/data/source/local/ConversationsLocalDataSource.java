package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.Conv;
import support.im.data.source.SimpleConversationsDataSource;
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
    DatabaseUtils.findRecentConv(mClientId, userObjectId, new DatabaseUtils.FindConvCallback() {
      @Override public void onSuccess(Conv conv) {
        if (conv == null) {
          callback.onConversationNotFound();
          return;
        }
        callback.onConversationLoaded(conv);
      }
    });
  }

  @Override public void loadConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    DatabaseUtils.findRecentConv(mClientId, new DatabaseUtils.FindConvsCallback() {
      @Override public void onSuccess(List<Conv> convs) {
        if (convs.isEmpty()) {
          callback.onConversationsNotFound();
          return;
        }
        callback.onConversationsLoaded(convs);
      }
    });
  }

  @Override public void saveConversation(@NonNull Conv conversation) {
  }
}

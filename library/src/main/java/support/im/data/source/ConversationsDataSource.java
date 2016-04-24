package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.Conversation;

public interface ConversationsDataSource {

  interface LoadConversationCallback {
    void onConversationsLoaded(List<Conversation> conversations);
    void onDataNotAvailable();
  }

  void loadConversations(@NonNull LoadConversationCallback callback);

  void refreshConversations();
}

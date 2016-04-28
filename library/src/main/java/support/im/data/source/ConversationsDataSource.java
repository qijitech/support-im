package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMException;
import java.util.List;
import support.im.data.Conversation;

public interface ConversationsDataSource {

  interface LoadConversationCallback {
    void onConversationsLoaded(List<Conversation> conversations);

    void onDataNotAvailable(AVIMException e);
  }

  void loadConversations(@NonNull LoadConversationCallback callback);

  void refreshConversations();
}

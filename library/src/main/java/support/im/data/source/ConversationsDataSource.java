package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import java.util.List;
import support.im.data.Conversation;

public interface ConversationsDataSource {

  interface LoadConversationCallback {
    void onConversationLoaded(Conversation conversation);
    void onConversationNotFound();
  }

  interface LoadConversationsCallback {
    void onConversationsLoaded(List<Conversation> conversations);
    void onConversationsNotFound();
    void onDataNotAvailable(AVIMException e);
  }

  interface GetLastMessageCallback {
    void onLastMessageLoaded(AVIMMessage avimMessage);
    void onLastMessageNotFound();
    void onDataNotAvailable(AVIMException e);
  }

  void findConversations(@NonNull List<String> ids,
      @NonNull final AVIMConversationQueryCallback callback);

  void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback);

  void saveConversation(@NonNull Conversation conversation);

  void loadConversations(@NonNull LoadConversationsCallback callback);

  void loadConversation(@NonNull String userObjectId, LoadConversationCallback callback);

  void refreshConversations();


}

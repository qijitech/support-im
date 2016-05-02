package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.data.Conversation;

public interface ConversationsDataSource {

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

  void loadCachedConversations(@NonNull LoadConversationsCallback callback);

  void loadServerConversations(@NonNull LoadConversationsCallback callback);

  void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback);

  void refreshConversations();
}

package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.data.Conversation;

public interface ConversationsDataSource {

  interface LoadConversationCallback {
    void onConversationsLoaded(List<Conversation> conversations);

    void onConversationsNotFound();

    void onDataNotAvailable(AVIMException e);
  }

  interface GetLastMessageCallback {
    void onLastMessageLoaded(AVIMMessage avimMessage);

    void onLastMessageNotFound();

    void onDataNotAvailable(AVIMException e);
  }

  interface LoadAVIMConversationsCallback {
    void onAVIMConversationsLoaded(List<AVIMConversation> avimConversations);

    void onAVIMConversationsNotFound();

    void onDataNotAvailable(AVIMException e);
  }

  void loadConversations(@NonNull LoadConversationCallback callback);

  void loadConversations(@NonNull List<Conversation> conversations,
      @NonNull LoadConversationCallback callback);

  void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback);

  void loadAVIMConversations(@NonNull LoadAVIMConversationsCallback callback);

  void refreshConversations();
}

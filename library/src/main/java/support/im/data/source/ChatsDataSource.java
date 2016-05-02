package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import java.util.List;

public interface ChatsDataSource {

  interface LoadMessagesCallback {
    void onMessagesLoaded(List<AVIMMessage> messages);

    void onDataNotAvailable(AVException exception);
  }

  interface GetMessageCallback {
    void onMessageLoaded(AVIMMessage message);

    void onDataNotAvailable(AVException exception);
  }

  void sendMessage(@NonNull AVIMConversation aVIMConversation,
      @NonNull AVIMTypedMessage message,
      GetMessageCallback callback);

  void loadMessages(@NonNull AVIMConversation aVIMConversation, @NonNull LoadMessagesCallback callback);

  void loadMessages(
      @NonNull AVIMConversation aVIMConversation,
      @NonNull final String messageId,
      final long timestamp, final int limit,
      @NonNull LoadMessagesCallback callback);

  void refreshMessages();
}

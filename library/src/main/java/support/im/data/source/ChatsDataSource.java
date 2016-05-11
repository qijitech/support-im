package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import java.util.List;
import support.im.data.User;

public interface ChatsDataSource {

  interface LoadMessagesCallback {
    void onMessagesLoaded(List<AVIMMessage> messages);
    void onDataNotAvailable(AVException exception);
  }

  interface GetMessageCallback {
    void onMessageLoaded(AVIMMessage message);
    void onDataNotAvailable(AVException exception);
  }

  /**
   * 发送消息
   */
  void sendMessage(@NonNull AVIMConversation aVIMConversation,
      @NonNull AVIMMessage message,
      GetMessageCallback callback);

  /**
   * 获取最新20条数据,根据
   */
  void loadMessages(@NonNull AVIMConversation aVIMConversation,
      @NonNull LoadMessagesCallback callback);

  /**
   * 获取更多数据
   */
  void loadMessages(
      @NonNull AVIMConversation aVIMConversation,
      @NonNull final String messageId,
      final long timestamp, final int limit,
      @NonNull LoadMessagesCallback callback);

  /**
   * 创建一个Conversation
   * @param toUser
   * @param callback
   */
  void createConversation(@NonNull User toUser, @NonNull AVIMConversationCreatedCallback callback);

  /**
   * 是否需要刷新数据
   */
  void refreshMessages();
}

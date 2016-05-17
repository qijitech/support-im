package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import support.im.data.ConversationType;
import support.im.data.User;
import support.im.data.source.ChatsDataSource;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsRemoteDataSource implements ChatsDataSource {

  private ChatsRemoteDataSource() {
  }

  private static ChatsRemoteDataSource INSTANCE = null;

  public static ChatsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ChatsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override
  public void loadMessages(@NonNull AVIMConversation aVIMConversation, @NonNull String messageId,
      long timestamp, int limit, @NonNull final LoadMessagesCallback callback) {
    checkNotNull(aVIMConversation);
    aVIMConversation.queryMessages(messageId, timestamp, limit, new AVIMMessagesQueryCallback() {
      @Override public void done(List<AVIMMessage> messages, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessagesLoaded(messages);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void refreshMessages() {

  }

  @Override public void sendMessage(@NonNull AVIMConversation aVIMConversation,
      @NonNull final AVIMMessage message, @NonNull final GetMessageCallback callback) {
    checkNotNull(aVIMConversation);
    checkNotNull(message);
    checkNotNull(callback);

    aVIMConversation.sendMessage(message, new AVIMConversationCallback() {
      @Override public void done(AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onMessageLoaded(message);
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void createConversation(@NonNull User toUser,
      @NonNull final AVIMConversationCreatedCallback callback) {
    checkNotNull(toUser);
    checkNotNull(callback);

    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    final String memberId = toUser.getObjectId();
    ChatManager.getInstance()
        .getAVIMClient()
        .createConversation(Lists.newArrayList(memberId), toUser.getDisplayName(), attrs, false/*isTransient*/,
            true /*isUnique*/, callback);
  }

  @Override public void createGroupConversation(@NonNull List<User> toUserGroup,
      @NonNull AVIMConversationCreatedCallback callback) {
    checkNotNull(toUserGroup);
    checkNotNull(callback);
    List<String> list = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    for (User user : toUserGroup) {
      list.add(user.getObjectId());
      builder.append(user.getDisplayName());
      builder.append("、");
    }
    builder.deleteCharAt(builder.length() - 1);
    String conversationName = builder.toString();
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Group.getValue());
    ChatManager.getInstance()
        .getAVIMClient()
        .createConversation(list, conversationName, attrs, false/*isTransient*/, true/*isUnique*/,
            callback);
  }
}

package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.source.SimpleConversationsDataSource;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.utilities.AVExceptionHandler;

public class ConversationsRemoteDataSource extends SimpleConversationsDataSource {

  private static ConversationsRemoteDataSource INSTANCE;

  // Prevent direct instantiation.
  public ConversationsRemoteDataSource() {
  }

  public static ConversationsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull final GetLastMessageCallback callback) {
    conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
      @Override
      public void done(AVIMMessage avimMessage, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          callback.onLastMessageLoaded(avimMessage);
        } else {
          callback.onDataNotAvailable(e);
        }
      }
    });
  }

  @Override public void findConversations(@NonNull List<String> ids,
      @NonNull AVIMConversationQueryCallback callback) {
    AVIMConversationQuery conversationQuery = ChatManager.getInstance().getConversationQuery();
    if (ids.size() > 0 && null != conversationQuery) {
      conversationQuery.setQueryPolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
      conversationQuery.whereContainsIn(Constants.OBJECT_ID, ids);
      conversationQuery.setLimit(1000);
      conversationQuery.findInBackground(callback);
      return;
    }
    callback.done(Lists.<AVIMConversation>newArrayList(), null);
  }
}

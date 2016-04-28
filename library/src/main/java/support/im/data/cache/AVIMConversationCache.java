package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import java.util.List;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;

public class AVIMConversationCache {

  private static ArrayMap<String, AVIMConversation> sAVIMConversations;

  static {
    sAVIMConversations = new ArrayMap<>();
  }

  public static void cacheConversation(AVIMConversation conversation) {
    sAVIMConversations.put(conversation.getConversationId(), conversation);
  }

  public static AVIMConversation getCacheConversation(String conversationId) {
    return sAVIMConversations.get(conversationId);
  }

  public static void findConversations(final AVIMConversationQueryCallback callback) {
    AVIMConversationQuery conversationQuery = ChatManager.getInstance().getConversationQuery();
    conversationQuery.setLimit(1000);
    conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
      @Override public void done(List<AVIMConversation> conversations, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          for (AVIMConversation conversation : conversations) {
            cacheConversation(conversation);
          }
        }
        callback.done(conversations, e);
      }
    });
  }
}

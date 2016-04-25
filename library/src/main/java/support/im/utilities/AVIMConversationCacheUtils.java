package support.im.utilities;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;

public class AVIMConversationCacheUtils {

  private static Map<String, AVIMConversation> conversationMap;

  static {
    conversationMap = Maps.newHashMap();
  }

  public static AVIMConversation getCacheConversation(String conversationId) {
    return conversationMap.get(conversationId);
  }

  public static void findConversationsByConversationIds(List<String> ids, AVIMConversationQueryCallback callback) {
    AVIMConversationQuery conversationQuery = ChatManager.getInstance().getConversationQuery();
    if (ids.size() > 0 && null != conversationQuery) {
      conversationQuery.whereContainsIn(Constants.OBJECT_ID, ids);
      conversationQuery.setLimit(1000);
      conversationQuery.findInBackground(callback);
    } else if (null != callback) {
      callback.done(new ArrayList<AVIMConversation>(), null);
    }
  }

  public static void cacheConversations(List<String> ids, final CacheConversationCallback callback) {
    List<String> unCachedIds = Lists.newArrayList();
    for (String id : ids) {
      if (!conversationMap.containsKey(id)) {
        unCachedIds.add(id);
      }
    }

    if (unCachedIds.isEmpty()) {
      if (null != callback) {
        callback.done(null);
        return;
      }
    }

    findConversationsByConversationIds(unCachedIds, new AVIMConversationQueryCallback() {
      @Override
      public void done(List<AVIMConversation> list, AVIMException e) {
        if (null == e) {
          for (AVIMConversation conversation : list) {
            conversationMap.put(conversation.getConversationId(), conversation);
          }
        }
        callback.done(e);
      }
    });
  }

  public static abstract class CacheConversationCallback {
    public abstract void done(AVException e);
  }
}

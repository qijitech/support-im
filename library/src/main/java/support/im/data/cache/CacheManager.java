package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.User;

public class CacheManager {

  private final static ArrayMap<String, AVIMConversation> sAVIMConversationsCache =
      new ArrayMap<>();
  private final static ArrayMap<String, User> sUsersCache = new ArrayMap<>();

  public static void cacheConversation(AVIMConversation conversation) {
    if (conversation != null) {
      sAVIMConversationsCache.put(conversation.getConversationId(), conversation);
    }
  }

  public static void cacheConversations(List<AVIMConversation> conversations) {
    if (conversations != null) {
      for (AVIMConversation c : conversations) {
        cacheConversation(c);
      }
    }
  }

  public static AVIMConversation getCacheConversation(String conversationId) {
    return sAVIMConversationsCache.get(conversationId);
  }

  public static List<AVIMConversation> getCacheConversations() {
    return Lists.newArrayList(sAVIMConversationsCache.values());
  }

  public static boolean hasCacheConversations() {
    return sAVIMConversationsCache.size() > 0;
  }

  public static boolean hasCacheConversation(AVIMConversation conversation) {
    return hasCacheConversation(conversation.getConversationId());
  }

  public static boolean hasCacheConversation(String conversationId) {
    return sAVIMConversationsCache.containsKey(conversationId);
  }

  public static void cacheUser(User user) {
    if (user != null) {
      sUsersCache.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<User> users) {
    if (users != null) {
      for (User user : users) {
        cacheUser(user);
      }
    }
  }

  public static User getCacheUser(String objectId) {
    return sUsersCache.get(objectId);
  }

  public static List<User> getCacheUsers() {
    return Lists.newArrayList(sUsersCache.values());
  }

  public static boolean hasCacheUsers() {
    return sUsersCache.size() > 0;
  }

  public static boolean hasCacheUser(User user) {
    return hasCacheUser(user.getObjectId());
  }

  public static boolean hasCacheUser(String objectId) {
    return sUsersCache.containsKey(objectId);
  }

  public static void clearUsers() {
    sUsersCache.clear();
  }

  public static void clearConversations() {
    sAVIMConversationsCache.clear();
  }
}

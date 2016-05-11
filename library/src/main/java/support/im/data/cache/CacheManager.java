package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.User;

public class CacheManager {

  private final static ArrayMap<String, AVIMConversation> sAVIMConversationsCache =
      new ArrayMap<>();
  private final static ArrayMap<String, Conversation> sConversationsCache =
      new ArrayMap<>();
  private final static ArrayMap<String, User> sUsersCache = new ArrayMap<>();

  public static void cacheAVIMConversation(AVIMConversation conversation) {
    if (conversation != null) {
      sAVIMConversationsCache.put(conversation.getConversationId(), conversation);
    }
  }

  public static void cacheAVIMConversations(List<AVIMConversation> conversations) {
    if (conversations != null) {
      for (AVIMConversation c : conversations) {
        cacheAVIMConversation(c);
      }
    }
  }

  public static AVIMConversation getCacheAVIMConversation(String conversationId) {
    return sAVIMConversationsCache.get(conversationId);
  }

  public static List<AVIMConversation> getCacheAVIMConversations() {
    return Lists.newArrayList(sAVIMConversationsCache.values());
  }

  public static boolean hasCacheAVIMConversations() {
    return sAVIMConversationsCache.size() > 0;
  }

  public static boolean hasCacheAVIMConversation(AVIMConversation conversation) {
    return hasCacheAVIMConversation(conversation.getConversationId());
  }

  public static boolean hasCacheAVIMConversation(String conversationId) {
    return sAVIMConversationsCache.containsKey(conversationId);
  }

  public static void cacheConversation(Conversation conversation) {
    if (conversation != null) {
      sConversationsCache.put(conversation.getConversationId(), conversation);
    }
  }

  public static void cacheConversations(List<Conversation> conversations) {
    if (conversations != null) {
      for (Conversation c : conversations) {
        cacheConversation(c);
      }
    }
  }

  public static Conversation getCacheConversation(String conversationId) {
    return sConversationsCache.get(conversationId);
  }

  public static List<Conversation> getCacheConversations() {
    return Lists.newArrayList(sConversationsCache.values());
  }

  public static boolean hasCacheConversations() {
    return sConversationsCache.size() > 0;
  }

  public static boolean hasCacheAVIMConversation(Conversation conversation) {
    return hasCacheConversation(conversation.getConversationId());
  }

  public static boolean hasCacheConversation(String conversationId) {
    return sConversationsCache.containsKey(conversationId);
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

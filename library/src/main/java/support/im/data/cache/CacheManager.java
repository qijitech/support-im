package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SimpleUser;

public class CacheManager {

  private ArrayMap<String, SimpleUser> mSimpleUserCache = new ArrayMap<>();
  private ArrayMap<String, SimpleUser> mContactsCache = new ArrayMap<>();
  private ArrayMap<String, AVIMConversation> mAVIMConversationsCache = new ArrayMap<>();

  private static CacheManager INSTANCE = null;

  public static CacheManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CacheManager();
    }
    return INSTANCE;
  }

  @SuppressWarnings("unchecked") private CacheManager() {
  }

  public void cacheSimpleUser(SimpleUser simpleUser) {
    if (simpleUser != null) {
      mSimpleUserCache.put(simpleUser.getUserId(), simpleUser);
    }
  }

  public void cacheSimpleUsers(List<SimpleUser> simpleUsers) {
    if (simpleUsers != null) {
      for (SimpleUser simpleUser : simpleUsers) {
        cacheSimpleUser(simpleUser);
      }
    }
  }

  public SimpleUser getCacheSimpleUser(String userId) {
    return mSimpleUserCache.get(userId);
  }

  public List<SimpleUser> getCacheSimpleUsers() {
    return Lists.newArrayList(mSimpleUserCache.values());
  }

  public boolean hasCacheSimpleUser(String userId) {
    return mSimpleUserCache.containsKey(userId);
  }

  public boolean hasCacheSimpleUser(SimpleUser simpleUser) {
    return hasCacheSimpleUser(simpleUser.getUserId());
  }

  public void cacheContact(SimpleUser contact) {
    if (contact != null) {
      mContactsCache.put(contact.getUserId(), contact);
    }
  }

  public void cacheContacts(List<SimpleUser> contacts) {
    if (contacts != null) {
      for (SimpleUser contact : contacts) {
        cacheContact(contact);
      }
    }
  }

  public SimpleUser getCacheContact(String userId) {
    return mContactsCache.get(userId);
  }

  public List<SimpleUser> getCacheContacts() {
    return Lists.newArrayList(mContactsCache.values());
  }

  public boolean hasCacheContact(String contactId) {
    return mContactsCache.containsKey(contactId);
  }

  public boolean hasCacheContact(SimpleUser contact) {
    return hasCacheContact(contact.getUserId());
  }

  public void cacheConversation(AVIMConversation conversation) {
    if (conversation != null) {
      mAVIMConversationsCache.put(conversation.getConversationId(), conversation);
    }
  }

  public void cacheConversations(List<AVIMConversation> conversations) {
    if (conversations != null) {
      for (AVIMConversation c : conversations) {
        cacheConversation(c);
      }
    }
  }

  public AVIMConversation getCacheConversation(String conversationId) {
    return mAVIMConversationsCache.get(conversationId);
  }

  public List<AVIMConversation> getCacheConversations() {
    return Lists.newArrayList(mAVIMConversationsCache.values());
  }

  public boolean hasCacheConversations() {
    return mAVIMConversationsCache.size() > 0;
  }

  public boolean hasCacheConversation(AVIMConversation conversation) {
    return mAVIMConversationsCache.containsKey(conversation.getConversationId());
  }

}

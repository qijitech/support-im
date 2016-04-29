package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.common.collect.Lists;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheBuilder;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheLogUtils;
import java.util.List;
import support.im.data.SimpleUser;
import support.ui.app.SupportApp;

public class CacheManager {

  private static final int DISK_CACHE_SIZE = 100;
  private static final int RAM_CACHE_SIZE = 50;
  private static final int CACHE_APP_VERSION = 1;
  private static final String CACHE_PREFIX = "support.im_";
  private static final String SIMPLE_USER_CACHE_NAME = CACHE_PREFIX + "simple_user";
  private static final String CONTACTS_CACHE_NAME = CACHE_PREFIX + "contacts";
  private static final String CONVERSATIONS_CACHE_NAME = CACHE_PREFIX + "conversations";

  private DualCache<ArrayMap<String, SimpleUser>> mSimpleUserCache;
  private DualCache<ArrayMap<String, SimpleUser>> mContactsCache;
  private DualCache<ArrayMap<String, AVIMConversation>> mAVIMConversationsCache;

  private static CacheManager INSTANCE = null;

  public static CacheManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CacheManager();
    }
    return INSTANCE;
  }

  @SuppressWarnings("unchecked") private CacheManager() {
    DualCacheLogUtils.enableLog();
    DualCacheContextUtils.setContext(SupportApp.appContext());

    ArrayMap<String, SimpleUser> simpleUsers = new ArrayMap<>();
    Class<ArrayMap<String, SimpleUser>> simpleUserCacheClazz =
        (Class<ArrayMap<String, SimpleUser>>) simpleUsers.getClass();
    mSimpleUserCache = new DualCacheBuilder<>(SIMPLE_USER_CACHE_NAME, CACHE_APP_VERSION,
        simpleUserCacheClazz).useDefaultSerializerInRam(RAM_CACHE_SIZE)
        .useDefaultSerializerInDisk(DISK_CACHE_SIZE, true);

    Class<ArrayMap<String, SimpleUser>> contactsCacheClazz =
        (Class<ArrayMap<String, SimpleUser>>) simpleUsers.getClass();
    mContactsCache = new DualCacheBuilder<>(CONTACTS_CACHE_NAME, CACHE_APP_VERSION,
        contactsCacheClazz).useDefaultSerializerInRam(RAM_CACHE_SIZE)
        .useDefaultSerializerInDisk(DISK_CACHE_SIZE, true);

    ArrayMap<String, AVIMConversation> avimConversations = new ArrayMap<>();
    Class<ArrayMap<String, AVIMConversation>> avimConversationsCacheClazz =
        (Class<ArrayMap<String, AVIMConversation>>) avimConversations.getClass();
    mAVIMConversationsCache = new DualCacheBuilder<>(CONVERSATIONS_CACHE_NAME, CACHE_APP_VERSION,
        avimConversationsCacheClazz).useDefaultSerializerInRam(RAM_CACHE_SIZE)
        .useDefaultSerializerInDisk(DISK_CACHE_SIZE, true);
  }

  public void cacheSimpleUser(SimpleUser simpleUser) {
    ArrayMap<String, SimpleUser> simpleUsers = getSimpleUsers();
    simpleUsers.put(simpleUser.getUserId(), simpleUser);
    putSimpleUsers(simpleUsers);
  }

  public void cacheSimpleUsers(List<SimpleUser> simpleUsers) {
    if (simpleUsers != null) {
      ArrayMap<String, SimpleUser> simpleUserArrayMap = getSimpleUsers();
      for (SimpleUser simpleUser : simpleUsers) {
        simpleUserArrayMap.put(simpleUser.getUserId(), simpleUser);
      }
      putSimpleUsers(simpleUserArrayMap);
    }
  }

  public SimpleUser getCacheSimpleUser(String userId) {
    ArrayMap<String, SimpleUser> simpleUsers = getSimpleUsers();
    return simpleUsers.get(userId);
  }

  public List<SimpleUser> getCacheSimpleUsers() {
    return Lists.newArrayList(getSimpleUsers().values());
  }

  public boolean hasCacheSimpleUser(String userId) {
    ArrayMap<String, SimpleUser> simpleUsers = getSimpleUsers();
    return simpleUsers.containsKey(userId);
  }

  public boolean hasCacheSimpleUser(SimpleUser simpleUser) {
    return hasCacheSimpleUser(simpleUser.getUserId());
  }

  public void cacheContact(SimpleUser contact) {
    ArrayMap<String, SimpleUser> contacts = getContacts();
    contacts.put(contact.getUserId(), contact);
    putContacts(contacts);
  }

  public void cacheContacts(List<SimpleUser> contacts) {
    if (contacts != null) {
      ArrayMap<String, SimpleUser> contactsArrayMap = getContacts();
      for (SimpleUser contact : contacts) {
        contactsArrayMap.put(contact.getUserId(), contact);
      }
      putContacts(contactsArrayMap);
    }
  }

  public SimpleUser getCacheContact(String userId) {
    return getContacts().get(userId);
  }

  public List<SimpleUser> getCacheContacts() {
    return Lists.newArrayList(getContacts().values());
  }

  public boolean hasCacheContact(String contactId) {
    return getContacts().containsKey(contactId);
  }

  public boolean hasCacheContact(SimpleUser contact) {
    return hasCacheContact(contact.getUserId());
  }

  public void cacheConversation(AVIMConversation conversation) {
    ArrayMap<String, AVIMConversation> conversations = getConversations();
    conversations.put(conversation.getConversationId(), conversation);
    putConversations(conversations);
  }

  public void cacheConversations(List<AVIMConversation> conversations) {
    if (conversations != null) {
      ArrayMap<String, AVIMConversation> conversationArrayMap = getConversations();
      for (AVIMConversation c : conversations) {
        conversationArrayMap.put(c.getConversationId(), c);
      }
      putConversations(conversationArrayMap);
    }
  }

  public AVIMConversation getCacheConversation(String conversationId) {
    return getConversations().get(conversationId);
  }

  public List<AVIMConversation> getCacheConversations() {
    return Lists.newArrayList(getConversations().values());
  }

  public boolean hasCacheConversation(AVIMConversation conversation) {
    return getConversations().containsKey(conversation.getConversationId());
  }

  //// Private Method
  private ArrayMap<String, SimpleUser> getSimpleUsers() {
    ArrayMap<String, SimpleUser> simpleUsers = mSimpleUserCache.get(SIMPLE_USER_CACHE_NAME);
    return simpleUsers == null ? new ArrayMap<String, SimpleUser>(0) : simpleUsers;
  }

  public void putSimpleUsers(ArrayMap<String, SimpleUser> simpleUsers) {
    mSimpleUserCache.put(SIMPLE_USER_CACHE_NAME, simpleUsers);
  }

  private ArrayMap<String, SimpleUser> getContacts() {
    ArrayMap<String, SimpleUser> simpleUsers = mContactsCache.get(CONTACTS_CACHE_NAME);
    return simpleUsers == null ? new ArrayMap<String, SimpleUser>(0) : simpleUsers;
  }

  public void putContacts(ArrayMap<String, SimpleUser> contacts) {
    mContactsCache.put(CONTACTS_CACHE_NAME, contacts);
  }

  private ArrayMap<String, AVIMConversation> getConversations() {
    ArrayMap<String, AVIMConversation> conversations = mAVIMConversationsCache.get(CONVERSATIONS_CACHE_NAME);
    return conversations == null ? new ArrayMap<String, AVIMConversation>(0) : conversations;
  }

  public void putConversations(ArrayMap<String, AVIMConversation> conversations) {
    mAVIMConversationsCache.put(CONVERSATIONS_CACHE_NAME, conversations);
  }
}

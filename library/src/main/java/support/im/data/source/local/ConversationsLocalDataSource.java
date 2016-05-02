package support.im.data.source.local;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.leanclound.ConversationManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource implements ConversationsDataSource {

  private static ConversationsLocalDataSource INSTANCE;

  private ConversationManager mConversationManager;

  // Prevent direct instantiation.
  public ConversationsLocalDataSource() {
    mConversationManager = ConversationManager.getInstance();
  }

  public static ConversationsLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void loadCachedConversations(@NonNull final LoadConversationsCallback callback) {
    checkNotNull(callback);

    // 获取本地conversations
    final List<Conversation> conversations = mConversationManager.findRecentConversations();
    if (conversations.isEmpty()) {
      callback.onConversationsNotFound();
      return;
    }

    callback.onConversationsLoaded(conversations);

    // 判断本地是否有AVIMConversation缓存
    //List<String> unCachedConversationIds = Lists.newArrayList();
    //for (Conversation conversation : conversations) {
    //  final String conversationId = conversation.mConversationId;
    //  if (!CacheManager.getInstance().hasCacheConversation(conversationId)) {
    //    unCachedConversationIds.add(conversationId);
    //  }
    //}
    //
    //// 如果有就不用像服务器获取
    //if (unCachedConversationIds.isEmpty()) {
    //  callback.onConversationsLoaded(conversations);
    //  return;
    //}
    //
    //findConversationsByConversationIds(unCachedConversationIds, new AVIMConversationQueryCallback() {
    //  @Override public void done(List<AVIMConversation> aVIMConversations, AVIMException e) {
    //    if (AVExceptionHandler.handAVException(e, false)) {
    //      for (AVIMConversation aVIMConversation : aVIMConversations) {
    //        CacheManager.getInstance().cacheConversation(aVIMConversation);
    //      }
    //      callback.onConversationsLoaded(conversations);
    //    }
    //  }
    //});
  }

  public static void findConversationsByConversationIds(List<String> ids, final AVIMConversationQueryCallback callback) {
    AVIMConversationQuery conversationQuery = ChatManager.getInstance().getConversationQuery();
    if (ids.size() > 0 && null != conversationQuery) {
      conversationQuery.whereContainsIn(Constants.OBJECT_ID, ids);
      conversationQuery.setLimit(1000);
      conversationQuery.findInBackground(callback);
      return;
    }
    callback.done(Lists.<AVIMConversation>newArrayList(), null);
  }

  @Override
  public void loadServerConversations(@NonNull LoadConversationsCallback callback) {
    // ConversationsRemoteDataSource
  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback) {

  }

  @Override public void refreshConversations() {

  }
}

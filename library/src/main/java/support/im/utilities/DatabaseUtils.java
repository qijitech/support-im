package support.im.utilities;

import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Lists;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DBBatchSaveQueue;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import java.util.List;
import support.im.data.AppDatabase;
import support.im.data.Conversation;
import support.im.data.Conversation_Table;
import support.im.data.User;
import support.im.data.User_Table;

public final class DatabaseUtils {

  public static Conversation saveConversation(AVIMConversation avimConversation, AVIMMessage avimMessage, String clientId) {
    final String conversationId = avimConversation.getConversationId();
    Conversation conversation = findByConversationId(conversationId);
    if (conversation != null) {
      conversation.setFromPeerId(avimMessage.getFrom());
      conversation.setLastMessage(avimMessage);
      conversation.setLatestMsgTime(avimMessage.getTimestamp());
      conversation.setLatestMsg(avimMessage.getContent());
      conversation.setMembers(JSON.toJSONString(avimConversation.getMembers()));
      conversation.setName(avimConversation.getName());
      conversation.setType(ConversationHelper.getType(avimConversation));
      conversation.update();
    } else {
      conversation = new Conversation.Builder()
          .conversationId(conversationId)
          .clientId(clientId)
          .members(JSON.toJSONString(avimConversation.getMembers()))
          .type(ConversationHelper.getType(avimConversation))
          .name(avimConversation.getName())
          .creator(avimConversation.getCreator())
          .firstMsgId(avimMessage.getMessageId())
          .firstMsgTime(avimMessage.getTimestamp())
          .latestMsgTime(avimMessage.getTimestamp())
          .latestMsg(avimMessage.getContent())
          .fromPeerId(avimMessage.getFrom())
          .isTransient(avimConversation.isTransient())
          .unreadCount(1)
          .build();
      conversation.insert();
    }
    return conversation;
  }

  public static Conversation findByConversationId(String conversationId) {
    Where<Conversation> where = SQLite.select().from(Conversation.class)
        .where(Conversation_Table.conversation_id.eq(conversationId));
    return where.querySingle();
  }

  public static void updateConversationUnreadCount(AVIMConversation avimConversation) {
    SQLite.update(Conversation.class)
        .set(Conversation_Table.unread_count.concatenate(1))
        .where(Conversation_Table.conversation_id.eq(avimConversation.getConversationId()));
  }

  public static void findRecentConv(String clientId, @NonNull final FindConversationsCallback convCallback) {
    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(Conversation.class)
            .where(Conversation_Table.client_id.eq(clientId)).orderBy(
                OrderBy.fromProperty(Conversation_Table.latest_msg_time).descending()))
        .queryResult(new QueryTransaction.QueryResultCallback<Conversation>() {
          @Override public void onQueryResult(QueryTransaction transaction,
              @NonNull CursorResult<Conversation> tResult) {
            List<Conversation> models = tResult.toListClose();
            if (models == null) {
              models = Lists.newArrayList();
            }
            convCallback.onSuccess(models);
          }}).build())
        .build().execute();
  }

  public static void findRecentConv(String clientId, String fromUser, @NonNull final FindConversationCallback callback) {
    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(Conversation.class)
            .where(Conversation_Table.client_id.eq(clientId))
            .and(Conversation_Table.creator.eq(fromUser))
            .and(Conversation_Table.type.eq(0))
            .limit(1))
            .queryResult(new QueryTransaction.QueryResultCallback<Conversation>() {
              @Override public void onQueryResult(QueryTransaction transaction,
                  @NonNull CursorResult<Conversation> tResult) {
                List<Conversation> models = tResult.toListClose();
                if (models == null || models.isEmpty()) {
                  callback.onSuccess(null);
                  return;
                }
                callback.onSuccess(models.get(0));
              }}).build())
        .build().execute();
  }

  public static void saveUser(final User user) {
    saveUser(user, null);
  }

  public static void saveUser(final User user, final SaveUserCallback saveUserCallback) {
    if (user == null) {
      return;
    }

    DBBatchSaveQueue saveQueue = FlowManager.getDatabase(AppDatabase.class)
        .getTransactionManager().getSaveQueue();
    saveQueue.setSuccessListener(new Transaction.Success() {
      @Override public void onSuccess(Transaction transaction) {
        if (saveUserCallback != null) {
          saveUserCallback.onSuccess(user);
        }
      }
    });
    saveQueue.add(user);
  }

  public static void saveUsers(final List<User> users, final SaveUsersCallback saveCallback) {
    if (users == null || users.isEmpty()) {
      if (saveCallback != null) {
        saveCallback.onSuccess(Lists.<User>newArrayList());
      }
      return;
    }

    DBBatchSaveQueue saveQueue = FlowManager.getDatabase(AppDatabase.class)
        .getTransactionManager().getSaveQueue();
    saveQueue.setSuccessListener(new Transaction.Success() {
      @Override public void onSuccess(Transaction transaction) {
        if (saveCallback != null) {
          saveCallback.onSuccess(users);
        }
      }
    });
    saveQueue.addAll(users);
  }

  public static void findUserByObjectIds(List<String> objectIds, final FindUsersCallback findUsersCallback) {
    if (objectIds == null || objectIds.isEmpty()) {
      findUsersCallback.onSuccess(Lists.<User>newArrayList());
      return;
    }

    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(User.class)
            .where(User_Table.object_id.in(objectIds)))
            .queryResult(new QueryTransaction.QueryResultCallback<User>() {
              @Override public void onQueryResult(QueryTransaction transaction,
                  @NonNull CursorResult<User> tResult) {
                List<User> models = tResult.toListClose();
                if (models == null) {
                  models = Lists.newArrayList();
                }
                findUsersCallback.onSuccess(models);
              }}).build())
        .build().execute();
  }

  public static void findUserByObjectId(String objectId, final FindUserCallback findUserCallback) {
    if (objectId == null) {
      findUserCallback.onSuccess(null);
      return;
    }

    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(User.class)
            .where(User_Table.object_id.eq(objectId)).limit(1))
            .queryResult(new QueryTransaction.QueryResultCallback<User>() {
              @Override public void onQueryResult(QueryTransaction transaction,
                  @NonNull CursorResult<User> tResult) {
                List<User> models = tResult.toListClose();
                if (models == null || models.isEmpty()) {
                  findUserCallback.onSuccess(null);
                } else {
                  findUserCallback.onSuccess(models.get(0));
                }
              }}).build())
        .build().execute();
  }

  public static interface FindUserCallback {
    void onSuccess(User user);
  }

  public static interface FindUsersCallback {
    void onSuccess(List<User> users);
  }

  public static interface SaveUsersCallback {
    void onSuccess(List<User> users);
  }

  public static interface SaveUserCallback {
    void onSuccess(User user);
  }

  public static interface FindConversationsCallback {
    void onSuccess(List<Conversation> conversations);
  }

  public static interface FindConversationCallback {
    void onSuccess(Conversation conversation);
  }

}

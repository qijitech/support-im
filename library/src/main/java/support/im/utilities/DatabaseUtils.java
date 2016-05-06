package support.im.utilities;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
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
import support.im.data.Conv;
import support.im.data.Conv_Table;
import support.im.data.ConversationType;
import support.im.data.User;
import support.im.data.User_Table;

public final class DatabaseUtils {

  public static Conv saveConversation(AVIMConversation avimConversation, String clientId) {
    final String conversationId = avimConversation.getConversationId();
    Conv conv = findByConvId(conversationId);
    if (conv != null) {
      conv.update();
    } else {
      conv = new Conv.Builder()
          .conversationId(conversationId)
          .clientId(clientId)
          .fromObjectId(avimConversation.getCreator())
          .build();
      conv.insert();
    }
    return conv;
  }

  public static Conv saveConversation(AVIMConversation avimConversation, AVIMMessage avimMessage, String clientId) {
    final String conversationId = avimConversation.getConversationId();
    Conv conv = findByConvId(conversationId);
    String message = avimMessage.getContent();
    if (avimMessage instanceof AVIMTextMessage) {
      message = ((AVIMTextMessage)avimMessage).getText();
    }
    if (conv != null) {
      conv.setLatestMsgTime(avimMessage.getTimestamp());
      conv.setMessage(message);
      conv.update();
    } else {
      conv = new Conv.Builder()
          .conversationId(conversationId)
          .clientId(clientId)
          .message(message)
          .type((Integer) avimConversation.getAttribute(ConversationType.TYPE_KEY))
          .fromObjectId(avimConversation.getCreator())
          .firstMsgId(avimMessage.getMessageId())
          .firstMsgTime(avimMessage.getTimestamp())
          .latestMsgTime(avimMessage.getTimestamp())
          .build();
      conv.insert();
    }
    return conv;
  }

  public static Conv findByConvId(String conversationId) {
    Where<Conv> where = SQLite.select().from(Conv.class)
        .where(Conv_Table.conv_id.eq(conversationId));
    return where.querySingle();
  }

  public static void updateConversationUnreadCount(AVIMConversation avimConversation) {
    SQLite.update(Conv.class)
        .set(Conv_Table.unread_count.concatenate(1))
        .where(Conv_Table.conv_id.eq(avimConversation.getConversationId()));
  }

  public static void findRecentConv(String clientId, @NonNull final FindConvsCallback convCallback) {
    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(Conv.class)
            .where(Conv_Table.client_id.eq(clientId)).orderBy(OrderBy.fromProperty(Conv_Table.latest_msg_time).descending()))
        .queryResult(new QueryTransaction.QueryResultCallback<Conv>() {
          @Override public void onQueryResult(QueryTransaction transaction,
              @NonNull CursorResult<Conv> tResult) {
            List<Conv> models = tResult.toListClose();
            if (models == null) {
              models = Lists.newArrayList();
            }
            convCallback.onSuccess(models);
          }}).build())
        .build().execute();
  }

  public static void findRecentConv(String clientId, String fromUser, @NonNull final FindConvCallback convCallback) {
    FlowManager.getDatabase(AppDatabase.class)
        .beginTransactionAsync(new QueryTransaction.Builder<>(SQLite.select().from(Conv.class)
            .where(Conv_Table.client_id.eq(clientId))
            .and(Conv_Table.conv_id.eq(fromUser)) // TODO
            .limit(1))
            .queryResult(new QueryTransaction.QueryResultCallback<Conv>() {
              @Override public void onQueryResult(QueryTransaction transaction,
                  @NonNull CursorResult<Conv> tResult) {
                List<Conv> models = tResult.toListClose();
                if (models == null || models.isEmpty()) {
                  models = Lists.newArrayList();
                  convCallback.onSuccess(null);
                  return;
                }
                convCallback.onSuccess(models.get(0));
              }}).build())
        .build().execute();
  }

  public static void saveUser(final User user) {
    if (user != null) {
      user.save();
    }
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
            .where(User_Table.objectId.in(objectIds)))
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
            .where(User_Table.objectId.eq(objectId)).limit(1))
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

  public static interface FindConvsCallback {
    void onSuccess(List<Conv> convs);
  }

  public static interface FindConvCallback {
    void onSuccess(Conv conv);
  }

}

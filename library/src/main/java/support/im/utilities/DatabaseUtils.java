package support.im.utilities;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Lists;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import java.util.List;
import support.im.data.AppDatabase;
import support.im.data.Conv;
import support.im.data.Conv_Table;

public final class DatabaseUtils {

  public static void saveConversation(AVIMConversation avimConversation, AVIMMessage message, String clientId) {
    final String conversationId = avimConversation.getConversationId();
    Conv conv = findByConvId(conversationId);
    if (conv != null) {
      conv.setLatestMsgTime(message.getTimestamp());
      conv.setMessage(message.getContent());
      conv.update();
    } else {
      conv = new Conv.Builder()
          .conversationId(conversationId)
          .clientId(clientId)
          .message(message.getContent())
          .firstMsgId(message.getMessageId())
          .firstMsgTime(message.getTimestamp())
          .latestMsgTime(message.getTimestamp())
          .build();
      conv.insert();
    }
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

  public static void findRecentConv(String clientId, @NonNull final FindConvCallback convCallback) {
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


  public static interface FindConvCallback {
    void onSuccess(List<Conv> convs);
  }
}

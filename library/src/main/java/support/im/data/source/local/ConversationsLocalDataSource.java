package support.im.data.source.local;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Lists;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource implements ConversationsDataSource {

  private static ConversationsLocalDataSource INSTANCE;

  private Context mContext;

  // Prevent direct instantiation.
  public ConversationsLocalDataSource(Context context) {
    mContext = checkNotNull(context);
  }

  public static ConversationsLocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsLocalDataSource(context);
    }
    return INSTANCE;
  }

  @Override public void loadConversations(@NonNull LoadConversationCallback callback) {
    Conversation conversation = new Conversation();
    conversation.mId = "1111";
    conversation.mUnreadCount = 10;
    AVIMMessage message = new AVIMMessage(conversation.mId, "Tom");
    message.setContent("测试");
    message.setTimestamp(SystemClock.currentThreadTimeMillis() / 1000);
    conversation.mLastMessage = message;
    callback.onConversationsLoaded(Lists.newArrayList(conversation));
  }

  @Override public void refreshConversations() {

  }
}

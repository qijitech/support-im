package support.im.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import support.im.data.source.ConversationsDataSource;
import support.im.leanclound.ConversationManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsLocalDataSource implements ConversationsDataSource {

  private static ConversationsLocalDataSource INSTANCE;

  private Context mContext;

  private ConversationManager mConversationManager;

  // Prevent direct instantiation.
  public ConversationsLocalDataSource(Context context) {
    mContext = checkNotNull(context);
    mConversationManager = ConversationManager.getInstance();
  }

  public static ConversationsLocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new ConversationsLocalDataSource(context);
    }
    return INSTANCE;
  }

  @Override public void loadConversations(@NonNull final LoadConversationCallback callback) {

  }

  @Override public void loadAVIMConversations(@NonNull LoadAVIMConversationsCallback callback) {

  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback) {

  }

  @Override public void refreshConversations() {

  }
}

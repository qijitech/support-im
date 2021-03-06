package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import java.util.List;
import support.im.data.Conversation;

public class SimpleConversationsDataSource implements ConversationsDataSource {

  @Override public void findConversations(@NonNull List<String> ids,
      @NonNull AVIMConversationQueryCallback callback) {

  }

  @Override public void getLastMessage(@NonNull AVIMConversation conversation,
      @NonNull GetLastMessageCallback callback) {

  }

  @Override public Conversation saveConversation(@NonNull AVIMConversation avimConversation,
      @NonNull AVIMMessage avimMessage) {
    return null;
  }

  @Override public void loadConversations(@NonNull LoadConversationsCallback callback) {

  }

  @Override
  public void loadConversation(@NonNull String userObjectId, LoadConversationCallback callback) {

  }

  @Override public void refreshConversations() {

  }

  @Override public void removeConversation(String conversationId) {
    
  }
}

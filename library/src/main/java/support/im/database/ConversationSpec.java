package support.im.database;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

@TableModelSpec(className = "Conversation", tableName = "conversations")
public class ConversationSpec {

  @ColumnSpec(name = "conversation_id", constraints = "NOT NULL") String conversationId;

  @ColumnSpec(name = "title") String title;

  @ColumnSpec(name = "unread_count", defaultValue = "0") int unReadCount;

  @ColumnSpec(name = "last_message_time") long lastMessageTime;
}

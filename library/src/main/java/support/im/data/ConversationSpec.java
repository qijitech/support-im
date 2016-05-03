package support.im.data;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

@TableModelSpec(className = "Conversation2", tableName = "conversations")
public class ConversationSpec {

  @ColumnSpec(name = "conversation_id", constraints = "NOT NULL") String conversationId;

  @ColumnSpec(name = "unread_count", defaultValue = "1") int unReadCount;
}

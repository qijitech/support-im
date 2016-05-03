package support.im.leanclound.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import support.im.data.ConversationModel;

public class ImTypeMessageEvent {
  public ConversationModel mConversation;
  public AVIMTypedMessage message;
  public AVIMConversation mAVIMConversation;
}

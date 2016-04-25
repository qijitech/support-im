package support.im.leanclound.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

public class ImTypeMessageEvent {
  public AVIMTypedMessage message;
  public AVIMConversation conversation;
}

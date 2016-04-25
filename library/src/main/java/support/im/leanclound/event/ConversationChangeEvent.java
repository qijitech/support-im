package support.im.leanclound.event;

import com.avos.avoscloud.im.v2.AVIMConversation;

//name, member change event
public class ConversationChangeEvent {
  private AVIMConversation conv;

  public ConversationChangeEvent(AVIMConversation conv) {
    this.conv = conv;
  }

  public AVIMConversation getConv() {
    return conv;
  }
}

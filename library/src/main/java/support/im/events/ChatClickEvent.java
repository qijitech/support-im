package support.im.events;

import com.avos.avoscloud.im.v2.AVIMMessage;

public class ChatClickEvent {

  public AVIMMessage mAvimMessage;

  public ChatClickEvent() {
  }

  public ChatClickEvent(AVIMMessage avimMessage) {
    this.mAvimMessage = avimMessage;
  }
}

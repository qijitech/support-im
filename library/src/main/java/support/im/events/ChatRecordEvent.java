package support.im.events;

public class ChatRecordEvent {

  /**
   * 录音本地路径
   */
  public String audioPath;

  /**
   * 录音长度
   */
  public int audioDuration;

  public ChatRecordEvent(String path, int duration) {
    audioDuration = duration;
    audioPath = path;
  }
}

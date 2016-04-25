package support.im.leanclound.event;

public class ConnectionChangeEvent {
  public boolean isConnect;
  public ConnectionChangeEvent(boolean isConnect) {
    this.isConnect = isConnect;
  }
}
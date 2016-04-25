package support.im.leanclound;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import de.greenrobot.event.EventBus;
import support.im.leanclound.event.ConnectionChangeEvent;

public class SupportClientEventHandler extends AVIMClientEventHandler {

  private static SupportClientEventHandler eventHandler;

  public static synchronized SupportClientEventHandler getInstance() {
    if (null == eventHandler) {
      eventHandler = new SupportClientEventHandler();
    }
    return eventHandler;
  }

  private SupportClientEventHandler() {
  }

  private volatile boolean connect = false;

  /**
   * 是否连上聊天服务
   */
  public boolean isConnect() {
    return connect;
  }

  public void setConnectAndNotify(boolean isConnect) {
    connect = isConnect;
    EventBus.getDefault().post(new ConnectionChangeEvent(connect));
  }

  @Override public void onConnectionPaused(AVIMClient avimClient) {
    setConnectAndNotify(false);
  }

  @Override public void onConnectionResume(AVIMClient avimClient) {
    setConnectAndNotify(true);
  }

  @Override public void onClientOffline(AVIMClient avimClient, int i) {

  }
}

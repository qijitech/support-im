package support.im.leanclound.contacts;

import android.annotation.SuppressLint;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import support.im.data.SupportUser;

@AVClassName("AddRequest")
@SuppressLint("ParcelCreator") public class AddRequest extends AVObject {

  public static final int STATUS_WAIT = 0;
  public static final int STATUS_DONE = 1;
  public static final int STATUS_REFUSED = 2;

  public static final String FROM_USER = "fromUser";
  public static final String TO_USER = "toUser";
  public static final String STATUS = "status";

  /**
   * 标记接收方是否已读该消息
   */
  public static final String IS_READ = "isRead";

  public AddRequest() {
  }

  public SupportUser getFromUser() {
    return getAVUser(FROM_USER, SupportUser.class);
  }

  public void setFromUser(SupportUser fromUser) {
    put(FROM_USER, fromUser);
  }

  public SupportUser getToUser() {
    return getAVUser(TO_USER, SupportUser.class);
  }

  public void setToUser(SupportUser toUser) {
    put(TO_USER, toUser);
  }

  public int getStatus() {
    return getInt(STATUS);
  }

  public void setStatus(int status) {
    put(STATUS, status);
  }

  public boolean isRead() {
    return getBoolean(IS_READ);
  }

  public void setIsRead(boolean isRead) {
    put(IS_READ, isRead);
  }

}

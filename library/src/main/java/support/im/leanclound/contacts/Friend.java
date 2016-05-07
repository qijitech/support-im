package support.im.leanclound.contacts;

import android.annotation.SuppressLint;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import support.im.data.SupportUser;

@AVClassName("Friend") @SuppressLint("ParcelCreator") public class Friend extends AVObject {

  public static final String USER = "user";
  public static final String FRIEND = "friend";
  public static final String DELETED = "deleted";

  public Friend() {
  }

  public void setUser(SupportUser user) {
    put(USER, user);
  }

  public SupportUser getUser() {
    return getAVUser(USER, SupportUser.class);
  }

  public void setFriend(SupportUser user) {
    put(FRIEND, user);
  }

  public SupportUser getFriend() {
    return getAVUser(FRIEND, SupportUser.class);
  }

  public void setDeleted(boolean deleted) {
    put(DELETED, deleted);
  }

  public boolean isDeleted() {
    return getBoolean(DELETED);
  }
}

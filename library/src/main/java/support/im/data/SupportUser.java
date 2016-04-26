package support.im.data;

import android.annotation.SuppressLint;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import java.util.UUID;

@SuppressLint("ParcelCreator") public class SupportUser extends AVUser {

  public static final String USER_ID = "user_id";
  public static final String NICKNAME = "nickname";
  public static final String AVATAR = "avatar";

  public static void register(String name, String nickname, String password, SignUpCallback callback) {
    SupportUser user = new SupportUser();
    user.put(USER_ID, UUID.randomUUID());
    user.setUsername(name);
    user.setNickname(nickname);
    user.setPassword(password);
    user.signUpInBackground(callback);
  }

  public void setNickname(String nickname) {
    put(NICKNAME, nickname);
  }

  public String getAvatarUrl() {
    AVFile avatar = getAVFile(AVATAR);
    if (avatar != null) {
      return avatar.getUrl();
    } else {
      return null;
    }
  }

  public static String getCurrentUserId () {
    SupportUser currentUser = getCurrentUser(SupportUser.class);
    return (null != currentUser ? currentUser.getObjectId() : null);
  }

  public static SupportUser getCurrentUser() {
    return getCurrentUser(SupportUser.class);
  }

}

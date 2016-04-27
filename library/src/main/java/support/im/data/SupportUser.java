package support.im.data;

import android.annotation.SuppressLint;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import support.im.utilities.SupportLog;

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

  public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy, FindCallback<SupportUser>
      findCallback) {
    try {
      AVQuery<SupportUser> q = followeeQuery(SupportUser.class);
      q.setCachePolicy(cachePolicy);
      q.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
      q.findInBackground(findCallback);
    } catch (Exception e) {
      SupportLog.logException(e);
    }

  }
}

package support.im.data;

import android.annotation.SuppressLint;
import android.net.Uri;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.common.base.Strings;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import support.im.utilities.SupportLog;

@SuppressLint("ParcelCreator") public class SupportUser extends AVUser {

  public static final String USERNAME = "username";
  public static final String USER_ID = "userId";
  public static final String DISPLAY_NAME = "displayName";
  public static final String AVATAR = "avatar";
  public static final String INSTALLATION = "installation";

  private String mSortLetters;

  public void setSortLetters(String sortLetters) {
    mSortLetters = sortLetters;
  }

  public String getSortLetters() {
    return mSortLetters;
  }

  public static void register(String username, String password, String nickname,
      SignUpCallback callback) {
    SupportUser user = new SupportUser();
    user.setAvatar("http://img1.imgtn.bdimg.com/it/u=1248462995,728310824&fm=21&gp=0.jpg");
    user.setUserId(UUID.randomUUID().toString());
    user.setUsername(username);
    user.setPassword(password);
    user.setDisplayName(nickname);
    user.signUpInBackground(callback);
  }

  public void setDisplayName(String displayName) {
    put(DISPLAY_NAME, displayName);
  }

  //public String getAvatarUrl() {
  //  AVFile avatar = getAVFile(AVATAR);
  //  if (avatar != null) {
  //    return avatar.getUrl();
  //  } else {
  //    return null;
  //  }
  //}

  public String getAvatar() {
    return (String) get(AVATAR);
  }

  public String getDisplayName() {
    return (String) get(DISPLAY_NAME);
  }

  public String getUserId() {
    return (String) get(USER_ID);
  }

  public void setAvatar(String avatar) {
    put(AVATAR, avatar);
  }

  public void setUserId(String userId) {
    put(USER_ID, userId);
  }

  public Uri toAvatarUri() {
    final String avatar = getAvatar();
    if (!Strings.isNullOrEmpty(avatar)) {
      return Uri.parse(avatar);
    }
    return null;
  }

  public static String getCurrentUserId() {
    SupportUser currentUser = getCurrentUser(SupportUser.class);
    return (null != currentUser ? currentUser.getObjectId() : null);
  }

  public static SupportUser getCurrentUser() {
    return getCurrentUser(SupportUser.class);
  }

  public void updateUserInstallation(SaveCallback callback) {
    AVInstallation installation = AVInstallation.getCurrentInstallation();
    if (installation != null) {
      put(INSTALLATION, installation);
      saveInBackground(callback);
    }
  }

  public void findFriendsWithCachePolicy(AVQuery.CachePolicy cachePolicy,
      FindCallback<SupportUser> findCallback) {
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

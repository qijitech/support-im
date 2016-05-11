package support.im.data;

import android.annotation.SuppressLint;
import android.net.Uri;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;

@SuppressLint("ParcelCreator") public class SupportUser extends AVUser {

  public static final String USERNAME = "username";
  public static final String USER_ID = "userId";
  public static final String DISPLAY_NAME = "displayName";
  public static final String AVATAR = "avatar";
  public static final String INSTALLATION = "installation";

  public static void register(String username,
      String password,
      String nickname, String avatar,
      SignUpCallback callback) {
    SupportUser user = new SupportUser();
    user.setAvatar(avatar);
    user.setUserId(UUID.randomUUID().toString());
    user.setUsername(username);
    user.setPassword(password);
    user.setDisplayName(nickname);
    user.signUpInBackground(callback);
  }

  /**
   * 用户第三方注册, 密码可以不用填写
   */
  public static void register2LeanCloud(SupportUser supportUser, SignUpCallback callback) {
    if (supportUser != null && callback != null) {
      supportUser.signUpInBackground(callback);
    }
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

  public User toUser() {
    User user = new User();
    user.setUserId(getUserId());
    user.setObjectId(getObjectId());
    user.setDisplayName(getDisplayName());
    user.setUsername(getUsername());
    user.setAvatar(getAvatar());
    return user;
  }

  public static List<User> toUsers(List<SupportUser> supportUsers) {
    if (supportUsers == null) {
      return Lists.newArrayList();
    }
    List<User> users = Lists.newArrayListWithCapacity(supportUsers.size());
    for (SupportUser supportUser : supportUsers) {
      users.add(supportUser.toUser());
    }
    return users;
  }

  public static class Builder {
    private String userId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }
    public Builder password(String password) {
      this.password = password;
      return this;
    }
    public Builder nickname(String nickname) {
      this.nickname = nickname;
      return this;
    }
    public Builder avatar(String avatar) {
      this.avatar = avatar;
      return this;
    }

    /**
     * username和password可以不用传
     * userId用于在点击时需要获取本应用的用户详情的时候需要用到
     * nickname用于显示
     * avatar用于显示头像
     */
    public SupportUser build() {
      SupportUser user = new SupportUser();
      user.setUserId(this.userId);
      user.setUsername(this.username);
      user.setDisplayName(this.nickname);
      user.setPassword(this.password);
      user.setAvatar(this.avatar);
      return user;
    }
  }

}

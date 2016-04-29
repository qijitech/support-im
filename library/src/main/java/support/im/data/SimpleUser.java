package support.im.data;

import android.net.Uri;
import com.google.common.base.Strings;

public class SimpleUser {

  private String mObjectId;
  private String mUserId;
  private String mDisplayName;
  private String mAvatar;

  private String mSortLetters;

  public void setSortLetters(String sortLetters) {
    mSortLetters = sortLetters;
  }

  public String getSortLetters() {
    return mSortLetters;
  }

  public Uri toAvatarUri() {
    final String avatar = getAvatar();
    if (!Strings.isNullOrEmpty(avatar)) {
      return Uri.parse(avatar);
    }
    return null;
  }

  public SimpleUser() {
  }

  public SimpleUser(String objectId, String userId, String displayName, String avatar) {
    mObjectId = objectId;
    mUserId = userId;
    mDisplayName = displayName;
    mAvatar = avatar;
  }

  public String getObjectId() {
    return mObjectId;
  }

  public void setObjectId(String objectId) {
    mObjectId = objectId;
  }

  public String getUserId() {
    return mUserId;
  }

  public void setUserId(String userId) {
    mUserId = userId;
  }

  public String getDisplayName() {
    return mDisplayName;
  }

  public void setDisplayName(String displayName) {
    mDisplayName = displayName;
  }

  public String getAvatar() {
    return mAvatar;
  }

  public void setAvatar(String avatar) {
    mAvatar = avatar;
  }
}

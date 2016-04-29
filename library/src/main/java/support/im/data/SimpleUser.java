package support.im.data;

public class SimpleUser {

  private String mObjectId;
  private String mUserId;
  private String mDisplayName;
  private String mAvatar;

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

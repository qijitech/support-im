package support.im.data;

import android.net.Uri;
import com.google.common.base.Strings;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class, name = "users")
public class User extends BaseModel {

  // 用户id
  @Column(name = "user_id", getterName = "getUserId") @PrimaryKey private String userId;
  // LeanCloud 保存的id
  @Column(name = "object_id") private String objectId;
  // 用户名称
  @Column(name = "display_name") private String displayName;
  // 用户头像
  @Column(name = "avatar") private String avatar;
  // 用户名
  @Column(name = "username") private String username;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Uri toAvatarUri() {
    final String avatar = getAvatar();
    if (!Strings.isNullOrEmpty(avatar)) {
      return Uri.parse(avatar);
    }
    return null;
  }
}

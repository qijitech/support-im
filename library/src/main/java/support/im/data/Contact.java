package support.im.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class, name = "contacts") public class Contact
    extends BaseModel {

  // 当前用户id
  @PrimaryKey
  @Column(name = "user_id") String userId;
  // 当前用户LeanCloud id
  @Column(name = "object_id") String objectId;
  // 用户排序字段
  @Column(name = "sort_letters", defaultValue = "\"#\"") String sortLetters;
  // 朋友id
  @PrimaryKey
  @ForeignKey(saveForeignKeyModel = true,references = {
      @ForeignKeyReference(
          columnType = String.class,
          columnName = "friend_id",
          foreignKeyColumnName = "user_id",
          referencedFieldIsPrivate = true,
          referencedGetterName = "getUserId"
      )
  }) User friend;

  // 是否已经删除
  @Column(name = "deleted", getterName = "isDeleted") private boolean deleted;

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

  public String getSortLetters() {
    return sortLetters;
  }

  public void setSortLetters(String sortLetters) {
    this.sortLetters = sortLetters;
  }

  public User getFriend() {
    return friend;
  }

  public void setFriend(User friend) {
    this.friend = friend;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}

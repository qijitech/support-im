package support.im.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;

@Table(database = AppDatabase.class, name = "contacts") public class Contact
    extends BaseModel {

  // 当前用户id
  @PrimaryKey
  @Column(name = "user_id") String userId;
  // 朋友id
  @PrimaryKey
  @ForeignKey(references = {
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

package support.im.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class, name = "add_requests") public class AddRequest
    extends BaseModel {

  // 当前用户id
  @PrimaryKey @Column(name = "user_id") String userId;

  @PrimaryKey @ForeignKey(saveForeignKeyModel = true, references = {
      @ForeignKeyReference(
          columnType = String.class,
          columnName = "from_user_id",
          foreignKeyColumnName = "user_id",
          referencedFieldIsPrivate = true,
          referencedGetterName = "getUserId")
  }) User fromUser;

  @Column(name = "status", defaultValue = "0") public int status;
  @Column(name = "is_read" ,getterName = "isRead") public boolean isRead;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public User getFromUser() {
    return fromUser;
  }

  public void setFromUser(User fromUser) {
    this.fromUser = fromUser;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public boolean isRead() {
    return isRead;
  }

  public void setRead(boolean read) {
    isRead = read;
  }
}

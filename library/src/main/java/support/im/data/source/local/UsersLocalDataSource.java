package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.User;
import support.im.data.source.SimpleUsersDataSource;
import support.im.utilities.DatabaseUtils;

public class UsersLocalDataSource extends SimpleUsersDataSource {

  private static UsersLocalDataSource INSTANCE = null;

  public static UsersLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new UsersLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void searchUser(@NonNull String username, @NonNull GetUserCallback callback) {

  }

  @Override public void saveUsers(List<User> users) {
    DatabaseUtils.saveUsers(users, new DatabaseUtils.SaveUserCallback() {
      @Override public void onSuccess(List<User> users) {
      }
    });
  }

  @Override public void fetchUser(String objectId, final GetUserCallback callback) {
    if (objectId == null ) {
      callback.onUserNotFound();
      return;
    }
    DatabaseUtils.findUserByObjectId(objectId, new DatabaseUtils.FindUserCallback() {
      @Override public void onSuccess(User user) {
        if (user == null) {
          callback.onUserNotFound();
        } else {
          callback.onUserLoaded(user);
        }
      }
    });
  }

  @Override public void saveUser(User user) {
    DatabaseUtils.saveUser(user);
  }

  @Override public void fetchUsers(List<String> objectIds, final LoadUsersCallback callback) {
    if (objectIds == null || objectIds.isEmpty()) {
      callback.onUserNotFound();
      return;
    }
    DatabaseUtils.findUserByObjectIds(objectIds, new DatabaseUtils.FindUsersCallback() {
      @Override public void onSuccess(List<User> users) {
        if (users.isEmpty()) {
          callback.onUserNotFound();
          return;
        }
        callback.onUserLoaded(users);
      }
    });
  }

  @Override public void refreshUsers() {

  }
}

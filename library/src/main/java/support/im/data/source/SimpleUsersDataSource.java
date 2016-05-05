package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.User;

public abstract class SimpleUsersDataSource implements UsersDataSource {

  @Override public void searchUser(@NonNull String username, @NonNull GetUserCallback callback) {

  }

  @Override public void fetchUsers(List<String> objectIds, LoadUsersCallback callback) {

  }

  @Override public void fetchUser(String objectId, GetUserCallback callback) {

  }

  @Override public void saveUsers(List<User> users) {

  }

  @Override public void saveUser(User user) {

  }

  @Override public void refreshUsers() {

  }
}

package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.data.User;

public interface UsersDataSource {

  interface LoadUsersCallback {
    void onUserLoaded(List<User> users);
    void onUserNotFound();
    void onDataNotAvailable(AVException exception);
  }

  interface GetUserCallback {
    void onUserLoaded(User user);
    void onUserNotFound();
    void onDataNotAvailable(AVException exception);
  }

  void searchUser(@NonNull String username, @NonNull GetUserCallback callback);
  void fetchUsers(final List<String> objectIds, final LoadUsersCallback callback);
  void fetchUser(final String objectId, final GetUserCallback callback);
  void saveUsers(List<User> users);
  void saveUser(User user);
  void refreshUsers();
}

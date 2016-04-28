package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.data.SupportUser;

public interface UsersDataSource {

  interface LoadUsersCallback {
    void onUserLoaded(List<SupportUser> users);

    void onDataNotAvailable(AVException exception);
  }

  interface GetUserCallback {
    void onUserLoaded(SupportUser user);

    void onUserNotFound();

    void onDataNotAvailable(AVException exception);
  }

  void searchUser(@NonNull String username, @NonNull GetUserCallback callback);

  void fetchUsers(final List<String> userIds, final LoadUsersCallback callback);
}

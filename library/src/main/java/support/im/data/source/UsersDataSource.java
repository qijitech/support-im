package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import support.im.data.SupportUser;

public interface UsersDataSource {

  interface GetUserCallback {
    void onUserLoaded(SupportUser user);
    void onUserNotFound();
    void onDataNotAvailable(AVException exception);
  }

  void searchUser(@NonNull String username, @NonNull GetUserCallback callback);

}

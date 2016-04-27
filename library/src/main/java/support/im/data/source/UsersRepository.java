package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import support.im.data.SupportUser;
import support.im.data.cache.UsersCache;

import static com.google.common.base.Preconditions.checkNotNull;

public class UsersRepository implements UsersDataSource {

  private static UsersRepository INSTANCE = null;
  private final UsersDataSource mUsersRemoteDataSource;

  // Prevent direct instantiation.
  private UsersRepository(@NonNull UsersDataSource usersRemoteDataSource) {
    mUsersRemoteDataSource = checkNotNull(usersRemoteDataSource);
  }

  public static UsersRepository getInstance(UsersDataSource usersRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new UsersRepository(usersRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override public void searchUser(@NonNull String username, @NonNull final GetUserCallback callback) {
    checkNotNull(username);
    checkNotNull(callback);

    mUsersRemoteDataSource.searchUser(username, new GetUserCallback() {
      @Override public void onUserLoaded(SupportUser user) {
        UsersCache.cacheUser(user);
        callback.onUserLoaded(user);
      }

      @Override public void onUserNotFound() {
        callback.onUserNotFound();
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }
}

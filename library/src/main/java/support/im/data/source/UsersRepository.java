package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import support.im.data.SimpleUser;
import support.im.data.cache.CacheManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class UsersRepository implements UsersDataSource {

  private static UsersRepository INSTANCE = null;
  private final UsersDataSource mUsersRemoteDataSource;
  private final UsersDataSource mUsersLocalDataSource;

  // Prevent direct instantiation.
  private UsersRepository(@NonNull UsersDataSource usersLocalDataSource,
      @NonNull UsersDataSource usersRemoteDataSource) {
    mUsersLocalDataSource = checkNotNull(usersLocalDataSource);
    mUsersRemoteDataSource = checkNotNull(usersRemoteDataSource);
  }

  public static UsersRepository getInstance(UsersDataSource usersLocalDataSource, UsersDataSource usersRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new UsersRepository(usersLocalDataSource, usersRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override
  public void searchUser(@NonNull String username, @NonNull final GetUserCallback callback) {
    checkNotNull(username);
    checkNotNull(callback);

    mUsersRemoteDataSource.searchUser(username, new GetUserCallback() {
      @Override public void onUserLoaded(SimpleUser user) {
        CacheManager.getInstance().cacheSimpleUser(user);
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

  @Override public void fetchUsers(final List<String> objectIds, final LoadUsersCallback callback) {
    Set<String> unCachedIds = Sets.newHashSet();
    for (String objectId : objectIds) {
      if (!CacheManager.getInstance().hasCacheSimpleUser(objectId)) {
        unCachedIds.add(objectId);
      }
    }

    if (unCachedIds.isEmpty()) {
      getUsersFromCache(objectIds, callback);
      return;
    }

    mUsersRemoteDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<SimpleUser> users) {
        CacheManager.getInstance().cacheSimpleUsers(users);
        getUsersFromCache(objectIds, callback);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  private void getUsersFromCache(List<String> objectIds, final LoadUsersCallback callback) {
    mUsersLocalDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<SimpleUser> users) {
        callback.onUserLoaded(users);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }
}

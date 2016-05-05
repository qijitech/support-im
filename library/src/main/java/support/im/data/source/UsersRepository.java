package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.User;
import support.im.data.cache.CacheManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class UsersRepository extends SimpleUsersDataSource {

  private static UsersRepository INSTANCE = null;
  private final UsersDataSource mUsersRemoteDataSource;
  private final UsersDataSource mUsersLocalDataSource;

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */
  boolean mCacheIsDirty;

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
      @Override public void onUserLoaded(User user) {
        saveUser(user);
        CacheManager.cacheUser(user);
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

  @Override public void saveUsers(List<User> users) {
    mUsersLocalDataSource.saveUsers(users);
  }

  @Override public void fetchUser(final String objectId, final GetUserCallback callback) {
    if (CacheManager.hasCacheUser(objectId)) {
      callback.onUserLoaded(CacheManager.getCacheUser(objectId));
      return;
    }
    mUsersLocalDataSource.fetchUser(objectId, new GetUserCallback() {
      @Override public void onUserLoaded(User user) {
        CacheManager.cacheUser(user);
        callback.onUserLoaded(user);
      }
      @Override public void onUserNotFound() {
        getRemoteUser(objectId, callback);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        getRemoteUser(objectId, callback);
      }
    });
  }

  private void getRemoteUser(String objectId, final GetUserCallback callback) {
    mUsersRemoteDataSource.fetchUser(objectId, new GetUserCallback() {
      @Override public void onUserLoaded(User user) {
        saveUser(user);
        CacheManager.cacheUser(user);
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
    checkNotNull(callback);

    if (CacheManager.hasCacheUsers() && !mCacheIsDirty) {
      List<String> unCachedIds = Lists.newArrayList();
      List<User> cachedUser = Lists.newArrayListWithCapacity(objectIds.size());
      for (String objectId : objectIds) {
        if (!CacheManager.hasCacheUser(objectId)) {
          unCachedIds.add(objectId);
        } else {
          cachedUser.add(CacheManager.getCacheUser(objectId));
        }
      }
      if (unCachedIds.isEmpty()) {
        callback.onUserLoaded(cachedUser);
        return;
      }
      getUsersFromCache(cachedUser, unCachedIds, callback);
      return;
    }

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getUsersFromRemoteDataSource(objectIds, callback);
      return;
    }
    getUsersFromCache(objectIds, callback);
  }

  private void getUsersFromRemoteDataSource(
      final List<User> cachedUsers, List<String> objectIds, @NonNull final LoadUsersCallback callback) {
    mUsersRemoteDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        refreshLocalDataSource(users);
        users.addAll(cachedUsers);
        callback.onUserLoaded(users);
      }
      @Override public void onUserNotFound() {
        callback.onUserNotFound();
      }
      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  private void getUsersFromRemoteDataSource(List<String> objectIds, @NonNull final LoadUsersCallback callback) {
    mUsersRemoteDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        refreshLocalDataSource(users);
        callback.onUserLoaded(users);
      }
      @Override public void onUserNotFound() {
        callback.onUserNotFound();
      }
      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  private void getUsersFromCache(final List<User> cachedUsers, final List<String> objectIds, final LoadUsersCallback callback) {
    mUsersLocalDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        users.addAll(cachedUsers);
        callback.onUserLoaded(users);
      }
      @Override public void onUserNotFound() {
        getUsersFromRemoteDataSource(cachedUsers, objectIds, callback);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        getUsersFromRemoteDataSource(cachedUsers, objectIds, callback);
      }
    });
  }

  private void getUsersFromCache(final List<String> objectIds, final LoadUsersCallback callback) {
    mUsersLocalDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        callback.onUserLoaded(Lists.newArrayList(users));
      }
      @Override public void onUserNotFound() {
        getUsersFromRemoteDataSource(objectIds, callback);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        getUsersFromRemoteDataSource(objectIds, callback);
      }
    });
  }

  private void refreshLocalDataSource(List<User> users) {
    saveUsers(users);
  }

  @Override public void refreshUsers() {
    mCacheIsDirty = true;
  }

  private void refreshCache(List<User> users) {
    CacheManager.cacheUsers(users);
    mCacheIsDirty = false;
  }

  @Override public void saveUser(User user) {
    mUsersLocalDataSource.saveUser(user);
  }
}

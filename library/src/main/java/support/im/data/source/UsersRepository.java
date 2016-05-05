package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import support.im.data.User;

import static com.google.common.base.Preconditions.checkNotNull;

public class UsersRepository extends SimpleUsersDataSource {

  private static UsersRepository INSTANCE = null;
  private final UsersDataSource mUsersRemoteDataSource;
  private final UsersDataSource mUsersLocalDataSource;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  Map<String, User> mCachedUsers;

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

  @Override public void fetchUser(String objectId, GetUserCallback callback) {
    if (mCachedUsers != null && mCachedUsers.containsKey(objectId)) {
      callback.onUserLoaded(mCachedUsers.get(objectId));
      return;
    }
    mUsersLocalDataSource.fetchUser(objectId, new GetUserCallback() {
      @Override public void onUserLoaded(User user) {
        if (mCachedUsers != null) {
          mCachedUsers.put(user.getObjectId(), user);
        }
      }
      @Override public void onUserNotFound() {
      }
      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

  @Override public void fetchUsers(final List<String> objectIds, final LoadUsersCallback callback) {
    checkNotNull(callback);

    if (mCachedUsers != null && !mCacheIsDirty) {
      List<String> unCachedIds = Lists.newArrayList();
      for (String objectId : objectIds) {
        if (!mCachedUsers.containsKey(objectId)) {
          unCachedIds.add(objectId);
        }
      }
      if (unCachedIds.isEmpty()) {
        callback.onUserLoaded(Lists.newArrayList(mCachedUsers.values()));
        return;
      }
      getUsersFromCache(unCachedIds, callback);
      return;
    }

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getUsersFromRemoteDataSource(objectIds, callback);
      return;
    }
    getUsersFromCache(objectIds, callback);
  }

  private void getUsersFromRemoteDataSource(List<String> objectIds, @NonNull final LoadUsersCallback callback) {
    mUsersRemoteDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        refreshLocalDataSource(users);
        callback.onUserLoaded(Lists.newArrayList(mCachedUsers.values()));
      }
      @Override public void onUserNotFound() {
        callback.onUserNotFound();
      }
      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }
    });
  }

  private void getUsersFromCache(final List<String> objectIds, final LoadUsersCallback callback) {
    mUsersLocalDataSource.fetchUsers(objectIds, new LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        refreshCache(users);
        callback.onUserLoaded(Lists.newArrayList(mCachedUsers.values()));
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
    if (mCachedUsers == null) {
      mCachedUsers = new LinkedHashMap<>();
    }
    for (User user : users) {
      mCachedUsers.put(user.getObjectId(), user);
    }
    mCacheIsDirty = false;
  }

  @Override public void saveUser(User user) {
    mUsersLocalDataSource.saveUser(user);
  }
}

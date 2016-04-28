package support.im.data.source.local;

import android.support.annotation.NonNull;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SupportUser;
import support.im.data.cache.UsersCache;
import support.im.data.source.UsersDataSource;

public class UsersLocalDataSource implements UsersDataSource {

  private static UsersLocalDataSource INSTANCE = null;

  public static UsersLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new UsersLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void searchUser(@NonNull String username, @NonNull GetUserCallback callback) {

  }

  @Override public void fetchUsers(List<String> objectIds, LoadUsersCallback callback) {
    List<SupportUser> users = Lists.newArrayList();
    for (String objectId : objectIds) {
      if (UsersCache.hasCachedUser(objectId)) {
        users.add(UsersCache.getCachedUser(objectId));
      }
    }
    callback.onUserLoaded(users);
  }
}

package support.im.data.source.local;

import android.support.annotation.NonNull;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SimpleUser;
import support.im.data.SupportUser;
import support.im.data.cache.CacheManager;
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

  @Override public void fetchUsers(List<String> userIds, LoadUsersCallback callback) {
    List<SimpleUser> users = Lists.newArrayList();
    for (String userId : userIds) {
      if (CacheManager.getInstance().hasCacheSimpleUser(userId)) {
        users.add(CacheManager.getInstance().getCacheSimpleUser(userId));
      }
    }
    callback.onUserLoaded(users);
  }
}

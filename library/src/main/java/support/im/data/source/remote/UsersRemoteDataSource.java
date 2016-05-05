package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import java.util.List;
import support.im.data.SupportUser;
import support.im.data.User;
import support.im.data.source.SimpleUsersDataSource;
import support.im.data.source.UsersDataSource;
import support.im.leanclound.Constants;
import support.im.utilities.AVExceptionHandler;

public class UsersRemoteDataSource extends SimpleUsersDataSource {

  private static UsersRemoteDataSource INSTANCE = null;

  public static UsersRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new UsersRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override
  public void searchUser(@NonNull String username, @NonNull final GetUserCallback callback) {
    AVQuery<SupportUser> query = SupportUser.getQuery(SupportUser.class);
    query.whereContains(SupportUser.USERNAME, username);
    query.limit(1);
    query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
    query.findInBackground(new FindCallback<SupportUser>() {
      @Override public void done(List<SupportUser> users, AVException e) {
        if (AVExceptionHandler.handAVException(e)) {
          if (users != null && users.size() >= 1) {
            callback.onUserLoaded(users.get(0).toUser());
            return;
          }
          callback.onUserNotFound();
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }

  @Override public void saveUsers(List<User> users) {

  }

  @Override public void fetchUsers(final List<String> objectIds, final LoadUsersCallback callback) {
    AVQuery<SupportUser> q = SupportUser.getQuery(SupportUser.class);
    q.whereContainedIn(Constants.OBJECT_ID, objectIds);
    q.setLimit(1000);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<SupportUser>() {
      @Override public void done(List<SupportUser> users, AVException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          if (users == null || users.isEmpty()) {
            callback.onUserNotFound();
            return;
          }
          callback.onUserLoaded(SupportUser.toUsers(users));
          return;
        }
        callback.onDataNotAvailable(e);
      }
    });
  }
}

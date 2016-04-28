package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import java.util.List;
import java.util.concurrent.TimeUnit;
import support.im.data.SupportUser;
import support.im.data.source.ContactsDataSource;
import support.im.utilities.SupportLog;

public class ContactsRemoteDataSource implements ContactsDataSource {

  private static ContactsRemoteDataSource INSTANCE = null;

  public static ContactsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull final LoadContactsCallback callback) {
    try {
      AVQuery.CachePolicy policy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE;
      SupportUser currentUser = SupportUser.getCurrentUser();
      AVQuery<SupportUser> query = currentUser.followeeQuery(SupportUser.class);
      query.setCachePolicy(policy);
      query.setMaxCacheAge(TimeUnit.MINUTES.toMillis(1));
      query.findInBackground(new FindCallback<SupportUser>() {
        @Override public void done(List<SupportUser> supportUsers, AVException e) {
          if (null != e) {
            callback.onDataNotAvailable(e);
            return;
          }
          callback.onContactsLoaded(supportUsers);
        }
      });
    } catch (Exception e) {
      SupportLog.logException(e);
    }
  }

  @Override public void refreshContacts() {
  }
}

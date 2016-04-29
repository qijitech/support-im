package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.data.SimpleUser;
import support.im.data.SupportUser;
import support.im.data.cache.CacheManager;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsRepository implements ContactsDataSource {

  private static ContactsRepository INSTANCE = null;

  private final ContactsDataSource mContactsRemoteDataSource;
  private final ContactsDataSource mContactsLocalDataSource;

  // Prevent direct instantiation.
  private ContactsRepository(@NonNull ContactsDataSource contactsLocalDataSource,
      @NonNull ContactsDataSource contactsRemoteDataSource) {
    mContactsLocalDataSource = checkNotNull(contactsLocalDataSource);
    mContactsRemoteDataSource = checkNotNull(contactsRemoteDataSource);
  }

  public static ContactsRepository getInstance(ContactsDataSource contactsLocalDataSource,
      ContactsDataSource contactsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRepository(contactsLocalDataSource, contactsRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull final LoadContactsCallback callback) {
    mContactsLocalDataSource.getContacts(new LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SimpleUser> contacts) {
        callback.onContactsLoaded(contacts);
      }
      @Override public void notLoggedIn() {
      }
      @Override public void onDataNotAvailable(AVException exception) {
      }
    });

    mContactsRemoteDataSource.getContacts(new LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SimpleUser> users) {
        CacheManager.getInstance().cacheContacts(users);
        callback.onContactsLoaded(users);
      }

      @Override public void notLoggedIn() {
        callback.notLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {

      }
    });
  }

  @Override public void refreshContacts() {

  }
}

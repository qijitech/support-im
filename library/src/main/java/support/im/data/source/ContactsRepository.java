package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.SupportUser;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsRepository implements ContactsDataSource {

  private static ContactsRepository INSTANCE = null;

  private final ContactsDataSource mContactsRemoteDataSource;

  // Prevent direct instantiation.
  private ContactsRepository(@NonNull ContactsDataSource contactsRemoteDataSource) {
    mContactsRemoteDataSource = checkNotNull(contactsRemoteDataSource);
  }

  public static ContactsRepository getInstance(ContactsDataSource contactsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRepository(contactsRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull final LoadContactsCallback callback) {
    mContactsRemoteDataSource.getContacts(new LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SupportUser> users) {
        callback.onContactsLoaded(users);
      }

      @Override public void notLoggedIn() {
        callback.notLoggedIn();
      }
    });
  }

  @Override public void refreshContacts() {

  }
}

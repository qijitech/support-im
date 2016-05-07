package support.im.data.source;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.data.Contact;

public interface ContactsDataSource {

  interface LoadContactsCallback {
    void onContactsLoaded(List<Contact> contacts);
    void onContactsNotFound();
    void notLoggedIn();
    void onDataNotAvailable(AVException exception);
  }

  interface SaveContactsCallback {
    void onSuccess(List<Contact> contacts);
  }

  interface SaveContactCallback {
    void onSuccess(Contact contact);
  }

  void getContacts(@NonNull String currentUserId, @NonNull LoadContactsCallback callback);
  void saveContacts(List<Contact> contacts, @NonNull SaveContactsCallback callback);
  void saveContact(Contact contact, @NonNull SaveContactCallback callback);

  void refreshContacts();
}

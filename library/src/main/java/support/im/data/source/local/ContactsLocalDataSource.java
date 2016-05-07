package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.Contact;
import support.im.data.source.SimpleContactsDataSource;
import support.im.utilities.DatabaseUtils;

public class ContactsLocalDataSource extends SimpleContactsDataSource {

  private static ContactsLocalDataSource INSTANCE = null;

  public static ContactsLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactsLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull String currentClientId, @NonNull
  final LoadContactsCallback callback) {
    DatabaseUtils.findContacts(currentClientId, new DatabaseUtils.ContactsCallback() {
      @Override public void onSuccess(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
          callback.onContactsNotFound();
          return;
        }
        callback.onContactsLoaded(contacts);
      }
    });
  }

  @Override public void saveContact(Contact contact, @NonNull final SaveContactCallback callback) {
    DatabaseUtils.saveContact(contact, new DatabaseUtils.ContactCallback() {
      @Override public void onSuccess(Contact contact) {
        callback.onSuccess(contact);
      }
    });
  }

  @Override
  public void saveContacts(List<Contact> contacts, @NonNull final SaveContactsCallback callback) {
    DatabaseUtils.saveContacts(contacts, new DatabaseUtils.ContactsCallback() {
      @Override public void onSuccess(List<Contact> contacts) {
        if (callback != null) {
          callback.onSuccess(contacts);
        }
      }
    });
  }
}

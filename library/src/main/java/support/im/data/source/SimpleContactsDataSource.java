package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.Contact;

public abstract class SimpleContactsDataSource implements ContactsDataSource {

  @Override public void getContacts(@NonNull String currentClientId, @NonNull LoadContactsCallback callback) {

  }

  @Override
  public void saveContacts(List<Contact> contacts, @NonNull SaveContactsCallback callback) {

  }

  @Override public void saveContact(Contact contact, @NonNull SaveContactCallback callback) {

  }

  @Override public void refreshContacts() {

  }
}

package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.SimpleUser;
import support.im.data.cache.CacheManager;
import support.im.data.source.ContactsDataSource;

public class ContactsLocalDataSource implements ContactsDataSource {

  private static ContactsLocalDataSource INSTANCE = null;

  public static ContactsLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactsLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull LoadContactsCallback callback) {
    List<SimpleUser> contacts = CacheManager.getInstance().getCacheContacts();
    callback.onContactsLoaded(contacts);
  }

  @Override public void refreshContacts() {

  }
}

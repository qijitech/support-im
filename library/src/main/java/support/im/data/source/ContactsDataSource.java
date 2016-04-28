package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.SupportUser;

public interface ContactsDataSource {

  interface LoadContactsCallback {
    void onContactsLoaded(List<SupportUser> contacts);

    void notLoggedIn();
  }

  void getContacts(@NonNull LoadContactsCallback callback);

  void refreshContacts();
}

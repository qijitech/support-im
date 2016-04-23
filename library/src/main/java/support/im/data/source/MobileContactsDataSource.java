package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.MobileContact;

public interface MobileContactsDataSource {

  interface LoadMobileContactsCallback {
    void onMobileContactsLoaded(List<MobileContact> tasks);
  }

  void getMobileContacts(@NonNull LoadMobileContactsCallback callback);

  void refreshMobileContacts();
}

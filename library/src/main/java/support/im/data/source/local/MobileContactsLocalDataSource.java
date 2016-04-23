package support.im.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import support.im.data.source.MobileContactsDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsLocalDataSource implements MobileContactsDataSource {

  private static MobileContactsLocalDataSource INSTANCE;

  // Prevent direct instantiation.
  public MobileContactsLocalDataSource(Context context) {
    checkNotNull(context);
  }

  public static MobileContactsLocalDataSource getInstance(@NonNull Context context) {
    if (INSTANCE == null) {
      INSTANCE = new MobileContactsLocalDataSource(context);
    }
    return INSTANCE;
  }
  
  @Override public void getMobileContacts(@NonNull LoadMobileContactsCallback callback) {

  }

  @Override public void refreshMobileContacts() {
    // Not required because the {@link MobileContactsRepository} handles the logic of refreshing the
    // tasks from all the available data sources.
  }
}

package support.im.data.source;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import support.im.data.MobileContact;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsRepository implements MobileContactsDataSource {

  private static MobileContactsRepository INSTANCE = null;

  private final MobileContactsDataSource mMobileContactsLocalDataSource;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  Map<String, MobileContact> mCachedMobileContacts;

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */
  boolean mCacheIsDirty = false;

  // Prevent direct instantiation.
  private MobileContactsRepository(@NonNull MobileContactsDataSource mobileContactsLocalDataSource) {
    mMobileContactsLocalDataSource = checkNotNull(mobileContactsLocalDataSource);
  }

  public static MobileContactsRepository getInstance(MobileContactsDataSource mobileContactsLocalDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new MobileContactsRepository(mobileContactsLocalDataSource);
    }
    return INSTANCE;
  }

  /**
   * Used to force {@link #getInstance(MobileContactsDataSource)} to create a new instance
   * next time it's called.
   */
  public static void destroyInstance() {
    INSTANCE = null;
  }

  @Override public void getMobileContacts(@NonNull final LoadMobileContactsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedMobileContacts != null && !mCacheIsDirty) {
      callback.onMobileContactsLoaded(new ArrayList<>(mCachedMobileContacts.values()));
      return;
    }

    // Query the local storage if available. If not, query the network.
    mMobileContactsLocalDataSource.getMobileContacts(new LoadMobileContactsCallback() {
      @Override public void onMobileContactsLoaded(List<MobileContact> tasks) {
        System.out.println(tasks);
        refreshCache(tasks);
        callback.onMobileContactsLoaded(tasks);
      }
    });
  }

  private void refreshCache(List<MobileContact> mobileContacts) {
    if (mCachedMobileContacts == null) {
      mCachedMobileContacts = new LinkedHashMap<>();
    }
    mCachedMobileContacts.clear();
    for (MobileContact mobileContact : mobileContacts) {
      mCachedMobileContacts.put(mobileContact.getId(), mobileContact);
    }
    mCacheIsDirty = false;
  }

  @Override public void refreshMobileContacts() {
    mCacheIsDirty = true;
  }
}

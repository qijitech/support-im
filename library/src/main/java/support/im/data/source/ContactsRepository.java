package support.im.data.source;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Contact;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsRepository extends SimpleContactsDataSource {

  private static ContactsRepository INSTANCE = null;

  private final ContactsDataSource mContactsLocalDataSource;
  private final ContactsDataSource mContactsRemoteDataSource;

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */
  boolean mCacheIsDirty = false;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  ArrayMap<String, Contact> mCachedContacts;

  // Prevent direct instantiation.
  private ContactsRepository(@NonNull ContactsDataSource contactsLocalDataSource,
      @NonNull ContactsDataSource contactsRemoteDataSource) {
    mContactsLocalDataSource = checkNotNull(contactsLocalDataSource);
    mContactsRemoteDataSource = checkNotNull(contactsRemoteDataSource);
  }

  public static ContactsRepository getInstance(ContactsDataSource contactsLocalDataSource, ContactsDataSource contactsRemoteDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRepository(contactsLocalDataSource, contactsRemoteDataSource);
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull String currentClientId, @NonNull final LoadContactsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedContacts != null && !mCacheIsDirty) {
      callback.onContactsLoaded(Lists.newArrayList(mCachedContacts.values()));
      return;
    }

    getContactsFromLocalDataSource(currentClientId, callback);

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getContactsFromRemoteDataSource(currentClientId, callback);
    }
  }

  @Override
  public void saveContacts(List<Contact> contacts, @NonNull SaveContactsCallback callback) {
    mContactsLocalDataSource.saveContacts(contacts, callback);
  }

  private void getContactsFromLocalDataSource(@NonNull final String currentClientId, @NonNull final LoadContactsCallback callback) {
    mContactsLocalDataSource.getContacts(currentClientId, new LoadContactsCallback() {
      @Override public void onContactsLoaded(List<Contact> contacts) {
        refreshContactsCache(contacts);
        callback.onContactsLoaded(contacts);
      }
      @Override public void onContactsNotFound() {
        getContactsFromRemoteDataSource(currentClientId, callback);
      }
      @Override public void notLoggedIn() {
      }
      @Override public void onDataNotAvailable(AVException exception) {
        getContactsFromRemoteDataSource(currentClientId, callback);
      }
    });
  }

  private void getContactsFromRemoteDataSource(@NonNull final String currentClientId, final LoadContactsCallback callback) {
    mContactsRemoteDataSource.getContacts(currentClientId, new LoadContactsCallback() {
      @Override public void onContactsLoaded(final List<Contact> contacts) {
        saveContacts(contacts, null);
        refreshContactsCache(contacts);
        callback.onContactsLoaded(contacts);
      }

      @Override public void notLoggedIn() {
        callback.notLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {
        callback.onDataNotAvailable(exception);
      }

      @Override public void onContactsNotFound() {
        callback.onContactsNotFound();
      }
    });
  }

  @Override public void refreshContacts() {
    mCacheIsDirty = true;
  }

  private void refreshContactsCache(List<Contact> contacts) {
    if (mCachedContacts == null) {
      mCachedContacts = new ArrayMap<>();
    }
    mCachedContacts.clear();
    for (Contact contact : contacts) {
      mCachedContacts.put(contact.getObjectId(), contact);
    }
    mCacheIsDirty = false;
  }
}

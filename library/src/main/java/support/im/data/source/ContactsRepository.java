package support.im.data.source;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SimpleUser;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsRepository implements ContactsDataSource {

  private static ContactsRepository INSTANCE = null;

  private final ContactsDataSource mContactsRemoteDataSource;

  private final UsersRepository mUsersRepository;

  /**
   * Marks the cache as invalid, to force an update the next time data is requested. This variable
   * has package local visibility so it can be accessed from tests.
   */
  boolean mCacheIsDirty = false;

  /**
   * This variable has package local visibility so it can be accessed from tests.
   */
  ArrayMap<String, SimpleUser> mCachedContacts;

  // Prevent direct instantiation.
  private ContactsRepository(@NonNull ContactsDataSource contactsRemoteDataSource,
      @NonNull UsersRepository usersRepository) {
    mContactsRemoteDataSource = checkNotNull(contactsRemoteDataSource);
    mUsersRepository = checkNotNull(usersRepository);
  }

  public static ContactsRepository getInstance(ContactsDataSource contactsRemoteDataSource, UsersRepository usersRepository) {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRepository(contactsRemoteDataSource, usersRepository);
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull final LoadContactsCallback callback) {
    checkNotNull(callback);

    // Respond immediately with cache if available and not dirty
    if (mCachedContacts != null && !mCacheIsDirty) {
      callback.onContactsLoaded(Lists.newArrayList(mCachedContacts.values()));
      return;
    }

    if (mCacheIsDirty) {
      // If the cache is dirty we need to fetch new data from the network.
      getContactsFromRemoteDataSource(callback);
    }
  }

  private void getContactsFromRemoteDataSource(final LoadContactsCallback callback) {
    mContactsRemoteDataSource.getContacts(new LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SimpleUser> users) {
        final List<String> objectIds = Lists.newArrayList();
        for (SimpleUser user : users) {
          objectIds.add(user.getObjectId());
        }
        mUsersRepository.fetchUsers(objectIds, new UsersDataSource.LoadUsersCallback() {
          @Override public void onUserLoaded(List<SimpleUser> users) {
            refreshContactsCache(users);
            callback.onContactsLoaded(users);
          }

          @Override public void onDataNotAvailable(AVException exception) {
            callback.onDataNotAvailable(exception);
          }
        });
      }

      @Override public void notLoggedIn() {
        callback.notLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

  @Override public void refreshContacts() {
    mCacheIsDirty = true;
  }

  private void refreshContactsCache(List<SimpleUser> contacts) {
    if (mCachedContacts == null) {
      mCachedContacts = new ArrayMap<>();
    }
    mCachedContacts.clear();
    for (SimpleUser contact : contacts) {
      mCachedContacts.put(contact.getUserId(), contact);
    }
    mCacheIsDirty = false;
  }
}

package support.im.contacts;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import support.im.data.Contact;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private PinyinComparator pinyinComparator;

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;

  private final String mCurrentClientId;
  private boolean mFirstLoad = true;

  public ContactsPresenter(@NonNull String currentClientId, @NonNull ContactsRepository conversationsRepository, ContactsContract.View contactsView) {
    mCurrentClientId = checkNotNull(currentClientId);
    mContactsView = checkNotNull(contactsView);
    mContactsRepository = checkNotNull(conversationsRepository);

    pinyinComparator = new PinyinComparator();

    mContactsView.setPresenter(this);
  }

  @Override public void loadContacts(boolean forceUpdate) {
    loadContacts(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  private void loadContacts(boolean forceUpdate, final boolean showLoadingUI) {
    if (showLoadingUI) {
      mContactsView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      mContactsRepository.refreshContacts();
    }

    mContactsRepository.getContacts(mCurrentClientId, new ContactsDataSource.LoadContactsCallback() {
      @Override public void onContactsLoaded(List<Contact> contacts) {
        processContacts(contacts);
        if (!mContactsView.isActive()) {
          return;
        }
        mContactsView.showContacts(contacts);
      }

      @Override public void onContactsNotFound() {
        mContactsView.showNoContacts();
      }

      @Override public void notLoggedIn() {
        mContactsView.showNotLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {
        mContactsView.onDataNotAvailable(exception);
      }
    });
  }

  private void processContacts(List<Contact> contacts) {
    List<User> users = Lists.newArrayList();
    for (Contact contact : contacts) {
      users.add(contact.getFriend());
    }
    CacheManager.cacheUsers(users);
    Collections.sort(contacts, pinyinComparator);
  }

  @Override public void start() {
    loadContacts(false);
  }
}

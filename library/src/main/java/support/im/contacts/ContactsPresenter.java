package support.im.contacts;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import java.util.Collections;
import java.util.List;
import support.im.data.Contact;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private PinyinComparator pinyinComparator;

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;

  private final String mCurrentUserId;
  private boolean mFirstLoad = true;

  public ContactsPresenter(@NonNull String currentId, @NonNull ContactsRepository conversationsRepository, ContactsContract.View contactsView) {
    mCurrentUserId = checkNotNull(currentId);
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

    mContactsRepository.getContacts(mCurrentUserId, new ContactsDataSource.LoadContactsCallback() {
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
    Collections.sort(contacts, pinyinComparator);
  }

  @Override public void start() {
    loadContacts(false);
  }
}

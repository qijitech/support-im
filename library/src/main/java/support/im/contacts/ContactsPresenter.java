package support.im.contacts;

import java.util.List;
import support.im.data.SupportUser;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;

  public ContactsPresenter(ContactsRepository conversationsRepository,
      ContactsContract.View contactsView) {
    mContactsView = checkNotNull(contactsView);
    mContactsRepository = checkNotNull(conversationsRepository);
    mContactsView.setPresenter(this);
  }

  @Override public void loadContacts(boolean forceUpdate) {
    mContactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SupportUser> contacts) {
        if (!mContactsView.isActive()) {
          return;
        }

        mContactsView.showContacts(contacts);
      }

      @Override public void notLoggedIn() {
        mContactsView.showNotLoggedIn();
      }
    });
  }

  @Override public void start() {
    loadContacts(false);
  }
}

package support.im.contacts;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.base.Strings;
import java.util.Collections;
import java.util.List;
import support.im.data.SimpleUser;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;
import support.im.mobilecontact.pinyin.CharacterParser;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private CharacterParser characterParser;
  private PinyinComparator pinyinComparator;

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;

  private boolean mFirstLoad = true;

  public ContactsPresenter(@NonNull ContactsRepository conversationsRepository, ContactsContract.View contactsView) {
    mContactsView = checkNotNull(contactsView);
    mContactsRepository = checkNotNull(conversationsRepository);

    characterParser = CharacterParser.getInstance();
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

    mContactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SimpleUser> contacts) {
        processUsers(contacts);
        if (!mContactsView.isActive()) {
          return;
        }
        mContactsView.showContacts(contacts);
      }

      @Override public void notLoggedIn() {
        mContactsView.showNotLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

  private void processUsers(List<SimpleUser> contacts) {
    for (SimpleUser u : contacts) {
      final String nickname = u.getDisplayName();
      if (!Strings.isNullOrEmpty(nickname)) {
        String pinyin = characterParser.getSelling(u.getDisplayName());
        String sortString = pinyin.substring(0, 1).toUpperCase();
        if (sortString.matches("[A-Z]")) {
          u.setSortLetters(sortString.toUpperCase());
          continue;
        }
      }
      u.setSortLetters("#");
    }
    Collections.sort(contacts, pinyinComparator);
  }

  @Override public void start() {
    loadContacts(false);
  }
}

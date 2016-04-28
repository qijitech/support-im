package support.im.contacts;

import com.google.common.base.Strings;
import java.util.Collections;
import java.util.List;
import support.im.data.SupportUser;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;
import support.im.mobilecontact.pinyin.CharacterParser;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private CharacterParser characterParser;
  private PinyinComparator pinyinComparator;

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;

  public ContactsPresenter(ContactsRepository conversationsRepository,
      ContactsContract.View contactsView) {
    mContactsView = checkNotNull(contactsView);
    mContactsRepository = checkNotNull(conversationsRepository);

    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();

    mContactsView.setPresenter(this);
  }

  @Override public void loadContacts(boolean forceUpdate) {
    mContactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SupportUser> contacts) {
        for (SupportUser u : contacts) {
          final String nickname = u.getNickname();
          if (!Strings.isNullOrEmpty(nickname)) {
            String pinyin = characterParser.getSelling(u.getNickname());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
              u.setSortLetters(sortString.toUpperCase());
            }
            continue;
          }
          u.setSortLetters("#");
        }
        Collections.sort(contacts, pinyinComparator);
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

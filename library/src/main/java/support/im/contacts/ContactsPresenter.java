package support.im.contacts;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import support.im.data.SupportUser;
import support.im.data.source.ContactsDataSource;
import support.im.data.source.ContactsRepository;
import support.im.data.source.UsersDataSource;
import support.im.data.source.UsersRepository;
import support.im.mobilecontact.pinyin.CharacterParser;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContactsPresenter implements ContactsContract.Presenter {

  private CharacterParser characterParser;
  private PinyinComparator pinyinComparator;

  private final ContactsContract.View mContactsView;
  private final ContactsRepository mContactsRepository;
  private final UsersRepository mUsersRepository;

  public ContactsPresenter(@NonNull ContactsRepository conversationsRepository,
      @NonNull UsersRepository usersRepository, ContactsContract.View contactsView) {
    mContactsView = checkNotNull(contactsView);
    mContactsRepository = checkNotNull(conversationsRepository);
    mUsersRepository = checkNotNull(usersRepository);

    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();

    mContactsView.setPresenter(this);
  }

  @Override public void loadContacts(boolean forceUpdate) {
    mContactsRepository.getContacts(new ContactsDataSource.LoadContactsCallback() {
      @Override public void onContactsLoaded(List<SupportUser> contacts) {
        final List<String> objectIds = Lists.newArrayList();
        for (SupportUser user : contacts) {
          objectIds.add(user.getObjectId());
        }
        mUsersRepository.fetchUsers(objectIds, new UsersDataSource.LoadUsersCallback() {
          @Override public void onUserLoaded(List<SupportUser> users) {
            processUsers(users);
            if (!mContactsView.isActive()) {
              return;
            }

            mContactsView.showContacts(users);
          }

          @Override public void onDataNotAvailable(AVException exception) {
            if (!mContactsView.isActive()) {
              return;
            }
            mContactsView.onDataNotAvailable(exception);
          }
        });
      }

      @Override public void notLoggedIn() {
        mContactsView.showNotLoggedIn();
      }

      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

  private void processUsers(List<SupportUser> contacts) {
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
  }

  @Override public void start() {
    loadContacts(false);
  }
}

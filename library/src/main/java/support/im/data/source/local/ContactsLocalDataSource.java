package support.im.data.source.local;

import android.support.annotation.NonNull;
import java.util.List;
import support.im.data.Contact;
import support.im.data.SupportUser;
import support.im.data.source.SimpleContactsDataSource;
import support.im.mobilecontact.pinyin.CharacterParser;
import support.im.utilities.DatabaseUtils;

public class ContactsLocalDataSource extends SimpleContactsDataSource {

  private static ContactsLocalDataSource INSTANCE = null;
  private CharacterParser characterParser;

  private ContactsLocalDataSource() {
    characterParser = new CharacterParser();
  }

  public static ContactsLocalDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactsLocalDataSource();
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull String currentClientId, @NonNull
  final LoadContactsCallback callback) {
    DatabaseUtils.findContacts(currentClientId, new DatabaseUtils.ContactsCallback() {
      @Override public void onSuccess(List<Contact> contacts) {
        if (contacts == null || contacts.isEmpty()) {
          callback.onContactsNotFound();
          return;
        }
        callback.onContactsLoaded(contacts);
      }
    });
  }

  @Override public void saveContact(SupportUser currentUser, SupportUser supportUser, @NonNull final SaveContactCallback callback) {
    Contact contact = new Contact();
    contact.setObjectId(currentUser.getObjectId());
    contact.setUserId(currentUser.getUserId());
    contact.setFriend(supportUser.toUser());
    contact.setDeleted(false);
    String pinyin = characterParser.getSelling(supportUser.getDisplayName());
    String sortString = pinyin.substring(0, 1).toUpperCase();
    if (sortString.matches("[A-Z]")) {
      contact.setSortLetters(sortString.toUpperCase());
    } else {
      contact.setSortLetters("#");
    }
    DatabaseUtils.saveContact(contact, new DatabaseUtils.ContactCallback() {
      @Override public void onSuccess(Contact contact) {
        callback.onSuccess(contact);
      }
    });
  }

  @Override
  public void saveContacts(List<Contact> contacts, @NonNull final SaveContactsCallback callback) {
    DatabaseUtils.saveContacts(contacts, new DatabaseUtils.ContactsCallback() {
      @Override public void onSuccess(List<Contact> contacts) {
        if (callback != null) {
          callback.onSuccess(contacts);
        }
      }
    });
  }
}

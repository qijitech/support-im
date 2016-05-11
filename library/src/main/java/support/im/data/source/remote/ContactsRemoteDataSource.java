package support.im.data.source.remote;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.TimeUnit;
import support.im.data.Contact;
import support.im.data.SupportUser;
import support.im.data.source.SimpleContactsDataSource;
import support.im.mobilecontact.pinyin.CharacterParser;

public class ContactsRemoteDataSource extends SimpleContactsDataSource {

  private static ContactsRemoteDataSource INSTANCE = null;
  private CharacterParser characterParser;

  private ContactsRemoteDataSource() {
    characterParser = new CharacterParser();
  }

  public static ContactsRemoteDataSource getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ContactsRemoteDataSource();
    }
    return INSTANCE;
  }

  @Override public void getContacts(@NonNull final String currentClientId, @NonNull final LoadContactsCallback callback) {
    SupportUser supportUser = SupportUser.getCurrentUser();
    AVQuery<SupportUser> friendAVQuery = null;
    try {
      friendAVQuery = supportUser.followeeQuery(SupportUser.class);
      friendAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
      friendAVQuery.include("followee");
      friendAVQuery.setLimit(1000);
      friendAVQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(30));
      friendAVQuery.findInBackground(new FindCallback<SupportUser>() {
        @Override public void done(List<SupportUser> friends, AVException e) {
          if (friends == null || friends.isEmpty()) {
            callback.onContactsNotFound();
            return;
          }
          List<Contact> contacts = Lists.newArrayListWithCapacity(friends.size());
          for (SupportUser friend : friends) {
            Contact contact = new Contact();
            contact.setObjectId(friend.getObjectId());
            contact.setFriend(friend.toUser());
            contact.setDeleted(false);
            contact.setUserId(currentClientId);
            String pinyin = characterParser.getSelling(friend.getDisplayName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
              contact.setSortLetters(sortString.toUpperCase());
            } else {
              contact.setSortLetters("#");
            }
            contacts.add(contact);
          }
          callback.onContactsLoaded(contacts);
        }
      });
    } catch (AVException e) {
      e.printStackTrace();
      callback.onContactsNotFound();
    }
  }
}

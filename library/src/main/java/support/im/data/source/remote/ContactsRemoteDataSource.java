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
import support.im.data.User;
import support.im.data.source.SimpleContactsDataSource;
import support.im.leanclound.contacts.Friend;
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

  @Override public void getContacts(@NonNull String currentClientId, @NonNull final LoadContactsCallback callback) {
    AVQuery<Friend> friendAVQuery = AVQuery.getQuery(Friend.class);
    friendAVQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    friendAVQuery.whereEqualTo(Friend.USER, SupportUser.getCurrentUser());
    friendAVQuery.include(Friend.FRIEND);
    friendAVQuery.include(Friend.USER);
    friendAVQuery.setLimit(1000);
    friendAVQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(30));
    friendAVQuery.findInBackground(new FindCallback<Friend>() {
      @Override public void done(List<Friend> friends, AVException e) {
        if (friends == null || friends.isEmpty()) {
          callback.onContactsNotFound();
          return;
        }
        List<Contact> contacts = Lists.newArrayListWithCapacity(friends.size());
        for (Friend friend : friends) {
          Contact contact = new Contact();
          User user = friend.getUser().toUser();
          User friendUser = friend.getFriend().toUser();
          contact.setObjectId(user.getObjectId());
          contact.setFriend(friendUser);
          contact.setDeleted(friend.isDeleted());
          contact.setUserId(user.getUserId());
          String pinyin = characterParser.getSelling(friendUser.getDisplayName());
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
  }
}

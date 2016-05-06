package support.im.leanclound.contacts;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import java.util.List;
import support.im.data.SupportUser;

public class FriendManager {

  private static FriendManager friendManager;

  public static synchronized FriendManager getInstance() {
    if (friendManager == null) {
      friendManager = new FriendManager();
    }
    return friendManager;
  }

  private FriendManager() {
  }

  public static void addFriend(SupportUser friendUser, final SaveCallback callback) {
    SupportUser currentUser = SupportUser.getCurrentUser();
    final Friend friend = new Friend();
    friend.setUser(currentUser);
    friend.setFriend(friendUser);
    friend.setDeleted(false);
    friend.saveInBackground(new SaveCallback() {
      @Override public void done(AVException e) {
        if (callback != null) {
          callback.done(e);
        }
      }
    });
  }

  public static void fetchFriends(boolean isForce, final FindCallback<Friend> findCallback) {
    AVQuery.CachePolicy policy = (isForce ? AVQuery.CachePolicy.NETWORK_ELSE_CACHE : AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
    AVQuery<Friend> friendAVQuery = AVQuery.getQuery(Friend.class);
    friendAVQuery.setCachePolicy(policy);
    friendAVQuery.whereEqualTo(Friend.USER, SupportUser.getCurrentUser());
    friendAVQuery.setLimit(1000);
    friendAVQuery.findInBackground(new FindCallback<Friend>() {
      @Override public void done(List<Friend> list, AVException e) {
        findCallback.done(list, e);
      }
    });
  }
}

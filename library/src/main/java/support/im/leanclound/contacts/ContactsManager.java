package support.im.leanclound.contacts;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import java.util.ArrayList;
import java.util.List;
import support.im.data.SupportUser;
import support.im.utilities.UserCacheUtils;

public class ContactsManager {

  private static volatile List<String> friendIds = new ArrayList<String>();

  public static List<String> getFriendIds() {
    return friendIds;
  }

  public static void setFriendIds(List<String> friendList) {
    friendIds.clear();
    if (friendList != null) {
      friendIds.addAll(friendList);
    }
  }

  public static void fetchFriends(boolean isForce, final FindCallback<SupportUser> findCallback) {
    AVQuery.CachePolicy policy = (isForce ? AVQuery.CachePolicy.NETWORK_ELSE_CACHE : AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
    SupportUser.getCurrentUser()
        .findFriendsWithCachePolicy(policy, new FindCallback<SupportUser>() {
          @Override public void done(List<SupportUser> list, AVException e) {
            if (null != e) {
              findCallback.done(null, e);
            } else {
              final List<String> userIds = new ArrayList<String>();
              for (SupportUser user : list) {
                userIds.add(user.getObjectId());
              }
              UserCacheUtils.fetchUsers(userIds, new UserCacheUtils.CacheUserCallback() {
                @Override public void done(List<SupportUser> list1, Exception e) {
                  setFriendIds(userIds);
                  findCallback.done(list1, null);
                }
              });
            }
          }
        });
  }

}

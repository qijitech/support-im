package support.im.utilities;

import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import support.im.data.SupportUser;
import support.im.leanclound.Constants;

public class UserCacheUtils {

  private static Map<String, SupportUser> userMap;

  static {
    userMap = new HashMap<String, SupportUser>();
  }

  public static SupportUser getCachedUser(String objectId) {
    return userMap.get(objectId);
  }

  public static boolean hasCachedUser(String objectId) {
    return userMap.containsKey(objectId);
  }

  public static void cacheUser(SupportUser user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      userMap.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<SupportUser> users) {
    if (null != users) {
      for (SupportUser user : users) {
        cacheUser(user);
      }
    }
  }

  public static void fetchUsers(List<String> ids) {
    fetchUsers(ids, null);
  }

  public static void fetchUsers(final List<String> ids, final CacheUserCallback cacheUserCallback) {
    Set<String> uncachedIds = new HashSet<String>();
    for (String id : ids) {
      if (!userMap.containsKey(id)) {
        uncachedIds.add(id);
      }
    }

    if (uncachedIds.isEmpty()) {
      if (null != cacheUserCallback) {
        cacheUserCallback.done(getUsersFromCache(ids), null);
        return;
      }
    }

    AVQuery<SupportUser> q = SupportUser.getQuery(SupportUser.class);
    q.whereContainedIn(Constants.OBJECT_ID, uncachedIds);
    q.setLimit(1000);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<SupportUser>() {
      @Override public void done(List<SupportUser> list, AVException e) {
        if (null == e) {
          for (SupportUser user : list) {
            userMap.put(user.getObjectId(), user);
          }
        }
        if (null != cacheUserCallback) {
          cacheUserCallback.done(getUsersFromCache(ids), e);
        }
      }
    });
  }

  public static List<SupportUser> getUsersFromCache(List<String> ids) {
    List<SupportUser> userList = new ArrayList<SupportUser>();
    for (String id : ids) {
      if (userMap.containsKey(id)) {
        userList.add(userMap.get(id));
      }
    }
    return userList;
  }

  public static abstract class CacheUserCallback {
    public abstract void done(List<SupportUser> userList, Exception e);
  }
}

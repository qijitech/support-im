package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SupportUser;

import static com.google.common.base.Preconditions.checkNotNull;

public final class UsersCache {

  private static ArrayMap<String, SupportUser> sCacheUsers;

  static {
    sCacheUsers = new ArrayMap<>();
  }

  public static SupportUser getCachedUser(String objectId) {
    checkNotNull(objectId);
    return sCacheUsers.get(objectId);
  }

  public static boolean hasCachedUser(String objectId) {
    return sCacheUsers.containsKey(objectId);
  }

  public static void cacheUser(SupportUser user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      sCacheUsers.put(user.getObjectId(), user);
    }
  }

  public static void cacheUsers(List<SupportUser> users) {
    if (null != users) {
      for (SupportUser user : users) {
        cacheUser(user);
      }
    }
  }

  public static List<SupportUser> getUsersFromCache(List<String> ids) {
    List<SupportUser> userList = Lists.newArrayList();
    for (String id : ids) {
      if (sCacheUsers.containsKey(id)) {
        userList.add(sCacheUsers.get(id));
      }
    }
    return userList;
  }
}

package support.im.data.cache;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.SupportUser;

public final class ContactsCache {

  private static ArrayMap<String, SupportUser> sCacheUsers;

  static {
    sCacheUsers = new ArrayMap<>();
  }

  public static List<SupportUser> getCachedContacts() {
    return Lists.newArrayList(sCacheUsers.values());
  }

  public static void cacheContact(SupportUser user) {
    if (null != user && !TextUtils.isEmpty(user.getObjectId())) {
      sCacheUsers.put(user.getObjectId(), user);
    }
  }

  public static void cacheContacts(List<SupportUser> users) {
    if (null != users) {
      for (SupportUser user : users) {
        cacheContact(user);
      }
    }
  }
}

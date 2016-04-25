package support.im.leanclound;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

public class ThirdPartDataCache {
  private Map<String, ThirdPartUserUtils.ThirdPartUser> userMap;

  private ThirdPartDataCache() {
    userMap = new HashMap<>();
  }

  private static ThirdPartDataCache thirdPartDataCache;

  public static ThirdPartDataCache getInstance() {
    if (null == thirdPartDataCache) {
      thirdPartDataCache = new ThirdPartDataCache();
    }
    return thirdPartDataCache;
  }

  public ThirdPartUserUtils.ThirdPartUser getCachedUser(String objectId) {
    return userMap.get(objectId);
  }

  public boolean hasCachedUser(String id) {
    return userMap.containsKey(id);
  }

  public void cacheUser(String id, ThirdPartUserUtils.ThirdPartUser user) {
    if (!TextUtils.isEmpty(id) && null != user) {
      userMap.put(id, user);
    }
  }
}

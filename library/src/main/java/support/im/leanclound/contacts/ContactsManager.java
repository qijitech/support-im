package support.im.leanclound.contacts;

import java.util.ArrayList;
import java.util.List;

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
}

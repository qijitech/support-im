package support.im.data;

import java.util.List;

/**
 * Created by wangh on 2016-5-18-0018.
 */
public class GroupUsers {

  public List<User> mUsers;

  public List<User> getUsers() {
    return mUsers;
  }

  public void setUsers(List<User> users) {
    mUsers = users;
  }

  public GroupUsers(List<User> users) {
    mUsers = users;
  }

  public GroupUsers() {
  }
}

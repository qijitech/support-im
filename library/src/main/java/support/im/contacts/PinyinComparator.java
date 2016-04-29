package support.im.contacts;

import java.util.Comparator;
import support.im.data.SimpleUser;

public class PinyinComparator implements Comparator<SimpleUser> {

  public int compare(SimpleUser o1, SimpleUser o2) {
    if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
      return -1;
    } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
      return 1;
    } else {
      return o1.getSortLetters().compareTo(o2.getSortLetters());
    }
  }
}

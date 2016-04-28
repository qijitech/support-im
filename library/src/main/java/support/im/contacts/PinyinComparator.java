package support.im.contacts;

import java.util.Comparator;
import support.im.data.SupportUser;

public class PinyinComparator implements Comparator<SupportUser> {

  public int compare(SupportUser o1, SupportUser o2) {
    if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
      return -1;
    } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
      return 1;
    } else {
      return o1.getSortLetters().compareTo(o2.getSortLetters());
    }
  }
}

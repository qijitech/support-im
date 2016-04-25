package support.im.mobilecontact.pinyin;

import java.util.Comparator;
import support.im.data.MobileContact;

public class PinyinComparator implements Comparator<MobileContact> {

  public int compare(MobileContact o1, MobileContact o2) {
    if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
      return -1;
    } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
      return 1;
    } else {
      return o1.getSortLetters().compareTo(o2.getSortLetters());
    }
  }
}

package support.im.data;

/**
 * Created by wangh on 2016-5-6-0006.
 */
public class CTextItem {

  public static final int FLAG_EXIT_GROUP = -1;

  public String content;
  public int flag;

  public CTextItem(int flag) {
    this.flag = flag;
  }

  public CTextItem(String content) {
    this.content = content;
  }
}

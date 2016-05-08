package support.im.emoticons;

public class FuncItem {

  public static final int TAG_CAMERA = 0;
  public static final int TAG_GALLERY = 1;
  public static final int TAG_LOCATION = 2;

  public int icon;
  public String funcName;
  public int tag;

  public FuncItem(int icon, String funcName, int tag) {
    this.icon = icon;
    this.funcName = funcName;
    this.tag = tag;
  }
}

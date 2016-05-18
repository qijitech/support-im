package support.im.picker.event;

/**
 * Created by wangh on 2016-5-17-0017.
 */
public class CheckEvent {
  public boolean isChecked;
  public int position;

  public CheckEvent(boolean isCancel, int position) {
    this.isChecked = isCancel;
    this.position = position;
  }
}

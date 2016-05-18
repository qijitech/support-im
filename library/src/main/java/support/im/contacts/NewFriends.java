package support.im.contacts;

import support.im.R;
import support.im.leanclound.contacts.AddRequestManager;

public class NewFriends {

  public int mDrawableRes;
  public int mTextRes;
  public int unReadCount;

  private NewFriends(int drawableRes, int textRes, int unReadCount) {
    mDrawableRes = drawableRes;
    mTextRes = textRes;
    this.unReadCount = unReadCount;
  }

  public void setUnReadCount(int unReadCount) {
    this.unReadCount = unReadCount;
  }

  public static NewFriends newContacts(int unReadCount) {
    return new NewFriends(R.drawable.support_im_contacts_new_contact, R.string.support_im_contacts_new_contact, unReadCount);
  }
}

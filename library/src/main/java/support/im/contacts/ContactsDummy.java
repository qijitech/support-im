package support.im.contacts;

import support.im.R;

public class ContactsDummy {

  public int mDrawableRes;
  public int mTextRes;

  private ContactsDummy(int drawableRes, int textRes) {
    mDrawableRes = drawableRes;
    mTextRes = textRes;
  }

  public static ContactsDummy group() {
    return new ContactsDummy(R.drawable.support_im_contacts_group, R.string.support_im_contacts_group);
  }
}

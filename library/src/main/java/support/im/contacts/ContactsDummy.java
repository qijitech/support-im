package support.im.contacts;

import support.im.R;

public class ContactsDummy {

  public static final int NEW_CONTACTS = 0;
  public static final int GROUP = 1;

  public int mDrawableRes;
  public int mTextRes;
  public int mTag;

  private ContactsDummy(int drawableRes, int textRes, int tag) {
    mDrawableRes = drawableRes;
    mTextRes = textRes;
    mTag = tag;
  }

  public static ContactsDummy newContacts() {
    return new ContactsDummy(R.drawable.support_im_contacts_new_contact, R.string.support_im_contacts_new_contact, NEW_CONTACTS);
  }

  public static ContactsDummy group() {
    return new ContactsDummy(R.drawable.support_im_contacts_group, R.string.support_im_contacts_group, GROUP);
  }
}

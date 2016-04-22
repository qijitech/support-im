package support.im.addcontact;

import support.im.R;
import support.ui.SupportFragment;

public class AddContactFragment extends SupportFragment {

  public static AddContactFragment create() {
    return new AddContactFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.add_contact;
  }
}

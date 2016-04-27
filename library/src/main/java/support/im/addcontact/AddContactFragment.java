package support.im.addcontact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import support.im.R;
import support.ui.SupportFragment;

public class AddContactFragment extends SupportFragment {

  public static AddContactFragment create() {
    return new AddContactFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.add_contacts_frag;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}

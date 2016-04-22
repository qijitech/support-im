package support.im.addcontact;

import android.support.v4.app.Fragment;
import support.ui.SupportSinglePaneActivity;

public class AddContactActivity extends SupportSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return AddContactFragment.create();
  }
}

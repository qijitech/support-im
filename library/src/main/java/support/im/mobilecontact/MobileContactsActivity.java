package support.im.mobilecontact;

import android.support.v4.app.Fragment;
import support.ui.SupportSinglePaneActivity;

public class MobileContactsActivity extends SupportSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return MobileContactsFragment.create();
  }
}

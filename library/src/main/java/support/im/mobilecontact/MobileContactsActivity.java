package support.im.mobilecontact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import support.im.Injection;
import support.ui.app.SupportSinglePaneActivity;

public class MobileContactsActivity extends SupportSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return MobileContactsFragment.create();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new MobileContactsPresenter(Injection.provideMobileContactsRepository(this),
        (MobileContactsFragment) getFragment());
  }
}
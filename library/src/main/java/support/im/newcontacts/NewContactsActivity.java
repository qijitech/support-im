package support.im.newcontacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import support.im.Injection;
import support.ui.SupportSinglePaneActivity;

public class NewContactsActivity extends SupportSinglePaneActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new NewContactsPresenter(Injection.provideAddRequestsRepository(this),
        (NewContactsContract.View) getFragment());
  }

  @Override protected Fragment onCreatePane() {
    return NewContactsFragment.create();
  }
}

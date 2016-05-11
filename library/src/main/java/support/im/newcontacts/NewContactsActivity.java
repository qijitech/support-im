package support.im.newcontacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import support.im.Injection;
import support.ui.app.SupportSinglePaneActivity;

public class NewContactsActivity extends SupportSinglePaneActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    new NewContactsPresenter(Injection.provideAddRequestsRepository(this),Injection.provideContactsRepository(this),
        (NewContactsContract.View) getFragment());
  }

  @Override protected Fragment onCreatePane() {
    return NewContactsFragment.create();
  }
}

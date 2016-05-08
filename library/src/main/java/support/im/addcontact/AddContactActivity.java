package support.im.addcontact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import support.im.Injection;
import support.ui.app.SupportSinglePaneActivity;

public class AddContactActivity extends SupportSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return AddContactFragment.create();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new AddContactsPresenter(Injection.provideUsersRepository(this), (AddContactFragment)getFragment());
  }
}

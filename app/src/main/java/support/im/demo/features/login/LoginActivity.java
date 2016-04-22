package support.im.demo.features.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import support.im.demo.Injection;
import support.im.demo.R;
import support.im.demo.BaseSinglePaneActivity;

public class LoginActivity extends BaseSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return LoginFragment.create();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle(R.string.label_login);

    new LoginPresenter(Injection.provideAuthRepository(this),
        (LoginFragment)getFragment());
  }
}

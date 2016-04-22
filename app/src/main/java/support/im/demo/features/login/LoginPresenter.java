package support.im.demo.features.login;

import android.support.annotation.NonNull;
import support.im.demo.data.source.AuthDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginPresenter implements LoginContract.Presenter {

  private final AuthDataSource mAuthDataSource;
  private final LoginContract.View mLoginView;

  public LoginPresenter(@NonNull AuthDataSource authDataSource,
      @NonNull LoginContract.View loginView) {
    mAuthDataSource = checkNotNull(authDataSource, "authDataSource cannot be null!");
    mLoginView = checkNotNull(loginView, "loginView cannot be null!");
    mLoginView.setPresenter(this);
  }

  @Override public void start() {
    // Nothing to do
  }

  @Override public void loginWithPlatform(PlatformType platformType) {
    mAuthDataSource.loginWithPlatform(platformType);
  }

  @Override public void loginWithMobile() {
    mLoginView.showLoginMobileUi();
  }
}

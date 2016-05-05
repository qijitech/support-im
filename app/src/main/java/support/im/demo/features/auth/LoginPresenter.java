package support.im.demo.features.auth;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import support.im.data.SupportUser;
import support.im.demo.data.source.AuthDataSource;
import support.im.leanclound.ChatManager;

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
    startLogin();
  }

  private void startLogin() {
    SupportUser supportUser = SupportUser.getCurrentUser();
    if (supportUser != null) {
      ChatManager.getInstance().openClient(supportUser.getObjectId(), new AVIMClientCallback() {
        @Override public void done(AVIMClient avimClient, AVIMException e) {
          SupportUser.getCurrentUser().updateUserInstallation(new SaveCallback() {
            @Override public void done(AVException e) {
              if (mLoginView.isActive()) {
                mLoginView.showMainUi();
              }
            }
          });
        }
      });
    }
  }

  @Override public void loginWithPlatform(PlatformType platformType) {
    mAuthDataSource.loginWithPlatform(platformType);
  }

  @Override public void loginWithMobile() {
    mLoginView.showLoginMobileUi();
  }

  @Override public void register() {
    mLoginView.showRegisterUi();
  }
}

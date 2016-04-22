package support.im.demo.data.source;

import support.im.demo.data.User;
import support.im.demo.features.login.PlatformType;

public interface AuthDataSource {

  interface AuthCallback {
    void onLoginSuccess(User user);

    void onError();
  }

  void loginWithPlatform(PlatformType platformType);

  void loginWithMobile();
}

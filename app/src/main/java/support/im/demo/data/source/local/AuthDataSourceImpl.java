package support.im.demo.data.source.local;

import support.im.demo.data.source.AuthDataSource;
import support.im.demo.features.login.PlatformType;

public class AuthDataSourceImpl implements AuthDataSource {

  private static AuthDataSourceImpl INSTANCE;

  public static AuthDataSourceImpl getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AuthDataSourceImpl();
    }
    return INSTANCE;
  }

  @Override public void loginWithPlatform(PlatformType platformType) {

  }

  @Override public void loginWithMobile() {

  }
}

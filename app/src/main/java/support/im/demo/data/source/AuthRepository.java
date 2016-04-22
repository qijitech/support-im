package support.im.demo.data.source;

import android.support.annotation.NonNull;
import support.im.demo.features.login.PlatformType;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthRepository implements AuthDataSource {

  private static AuthRepository INSTANCE;

  private final AuthDataSource mAuthLocalDataSource;

  public static AuthRepository getInstance(AuthDataSource authDataSource) {
    if (INSTANCE == null) {
      INSTANCE = new AuthRepository(authDataSource);
    }
    return INSTANCE;
  }

  // Prevent direct instantiation.
  private AuthRepository(@NonNull AuthDataSource authLocalDataSource) {
    mAuthLocalDataSource = checkNotNull(authLocalDataSource);
  }

  @Override public void loginWithPlatform(PlatformType platformType) {

  }

  @Override public void loginWithMobile() {

  }
}

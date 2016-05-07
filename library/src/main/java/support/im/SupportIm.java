package support.im;

import android.app.Activity;

public final class SupportIm {

  public static String sApplicationId;
  public static String sClientKey;
  public static boolean sDebugEnabled;
  public static Class<? extends Activity> sDefaultPushCallback;

  public static void initialize(
      String applicationId,
      String clientKey,
      Class<? extends Activity> defaultPushCallback,
      boolean debugEnabled
  ) {
    sApplicationId = applicationId;
    sClientKey = clientKey;
    sDefaultPushCallback = defaultPushCallback;
    sDebugEnabled = debugEnabled;
  }

}

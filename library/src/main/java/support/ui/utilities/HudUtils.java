package support.ui.utilities;

import android.content.Context;
import com.kaopiz.kprogresshud.KProgressHUD;

public final class HudUtils {

  private static KProgressHUD mHud;

  public static KProgressHUD showHud(Context context) {
    return mHud = showHud(context, null);
  }

  public static KProgressHUD showHud(Context context, String label) {
    return mHud = showHud(context, label, false);
  }

  public static KProgressHUD showHud(Context context, String label, boolean isCancellable) {
    return KProgressHUD.create(context)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(label)
        .setCancellable(isCancellable)
        .setDimAmount(0.5F)
        .show();
  }

  public static void dismissHud() {
    if (mHud != null && mHud.isShowing()) {
      mHud.dismiss();
    }
    mHud = null;
  }
}

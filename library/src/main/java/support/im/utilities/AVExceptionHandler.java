package support.im.utilities;

import com.avos.avoscloud.AVException;
import support.im.R;
import support.ui.app.SupportApp;
import support.ui.utilities.ToastUtils;

public class AVExceptionHandler {

  public static boolean handAVException(AVException e) {
    if (e != null) {
      ToastUtils.toast(getLocalizedMessage(e));
      return false;
    } else {
      return true;
    }
  }

  public static String getLocalizedMessage(AVException e) {
    if (e != null) {
      Throwable t = e.getCause();
      if (t instanceof java.net.UnknownHostException) {
        return SupportApp.getInstance().getString(R.string.support_im_error_unknown_host);
      }

      if (t instanceof AVException) {
        final int code = e.getCode();
        if (code == 0) {
          return SupportApp.getInstance().getString(R.string.support_im_error_unknown_host);
        }
        if (code == 202) {
          return SupportApp.getInstance().getString(R.string.support_im_error_user_has_been_taken);
        }
      }

      return e.getLocalizedMessage();
    }
    return null;
  }
}

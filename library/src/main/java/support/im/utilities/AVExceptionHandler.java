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
        final String detailMessage = e.getLocalizedMessage();
        if (detailMessage != null && detailMessage.endsWith("Connection Lost")) {
          return SupportApp.getInstance().getString(R.string.support_im_error_unknown_host);
        }
      }

      return e.getLocalizedMessage();
    }
    return null;
  }
}

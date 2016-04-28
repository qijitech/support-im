package support.im.utilities;

import com.avos.avoscloud.AVException;
import support.im.R;
import support.ui.app.SupportApp;
import support.ui.utilities.ToastUtils;

public class AVExceptionHandler {

  public static boolean handAVException(AVException e) {
    return handAVException(e, true);
  }

  public static boolean handAVException(AVException e, boolean showToast) {
    if (e != null) {
      if (showToast) {
        ToastUtils.toast(getLocalizedMessage(e));
      }
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

      final int code = e.getCode();
      if (code == 0) {
        return SupportApp.getInstance().getString(R.string.support_im_error_unknown_host);
      }
      if (code == AVException.USER_DOESNOT_EXIST) {
        return SupportApp.getInstance().getString(R.string.support_im_error_user_doesnot_exist);
      }
      if (code == AVException.USERNAME_PASSWORD_MISMATCH) {
        return SupportApp.getInstance().getString(R.string.support_im_error_username_password_mismatch);
      }
      if (code == AVException.USERNAME_TAKEN) {
        return SupportApp.getInstance().getString(R.string.support_im_error_user_has_been_taken);
      }

      return e.getLocalizedMessage();
    }
    return null;
  }
}

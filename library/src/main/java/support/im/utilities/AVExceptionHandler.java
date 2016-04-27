package support.im.utilities;

import android.content.Context;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import support.ui.utilities.ToastUtils;

public class AVExceptionHandler {

  public static boolean handAVException(AVException e) {
    if (e != null) {
      ToastUtils.toast(e.getLocalizedMessage());
      return false;
    } else {
      return true;
    }
  }
}

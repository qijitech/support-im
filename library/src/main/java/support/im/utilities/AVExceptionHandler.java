package support.im.utilities;

import android.content.Context;
import android.widget.Toast;
import com.avos.avoscloud.AVException;

public class AVExceptionHandler {

  public static boolean handAVException(Context context, AVException e) {
    if (e != null) {
      Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      return false;
    } else {
      return true;
    }
  }
}

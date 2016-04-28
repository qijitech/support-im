package support.im.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import support.im.R;

public final class DialogUtils {

  public static void simpleDialog(@NonNull Context context, @NonNull String content) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(content);
    builder.setPositiveButton(R.string.support_im_dialog_ok, null);
    builder.create().show();
  }

}

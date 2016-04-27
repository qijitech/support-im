package support.im.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.MaterialDialog;
import support.im.R;

public final class DialogUtils {

  public static void simpleDialog(@NonNull Context context, @NonNull String content) {
    new MaterialDialog.Builder(context)
        .content(content)
        .positiveText(context.getString(R.string.support_im_dialog_ok))
        .show();
  }

}

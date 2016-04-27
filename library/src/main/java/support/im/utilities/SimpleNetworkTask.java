package support.im.utilities;

import android.content.Context;
import support.ui.utilities.ToastUtils;

public abstract class SimpleNetworkTask extends NetworkAsyncTask {

  protected SimpleNetworkTask(Context context) {
    super(context);
  }

  protected SimpleNetworkTask(Context context, boolean showHud) {
    super(context, showHud);
  }

  @Override protected void onPost(Exception e) {
    if (e != null) {
      e.printStackTrace();
      ToastUtils.toast(e.getMessage());
    } else {
      onSucceed();
    }
  }

  protected abstract void onSucceed();
}

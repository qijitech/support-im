package support.im.demo;

import android.support.annotation.LayoutRes;
import com.avos.avoscloud.AVAnalytics;
import support.ui.SupportActivity;

public class BaseActivity extends SupportActivity {

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    AVAnalytics.trackAppOpened(getIntent());
  }
}

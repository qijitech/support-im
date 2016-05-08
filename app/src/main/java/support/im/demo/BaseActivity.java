package support.im.demo;

import com.avos.avoscloud.AVAnalytics;
import support.ui.app.SupportActivity;

public class BaseActivity extends SupportActivity {

  @Override protected void onStart() {
    super.onStart();
    AVAnalytics.trackAppOpened(getIntent());
  }
}

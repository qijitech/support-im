package support.im.demo;

import com.avos.avoscloud.AVAnalytics;
import support.ui.SupportActivity;

public class BaseActivity extends SupportActivity {

  @Override protected void onStart() {
    super.onStart();
    AVAnalytics.trackAppOpened(getIntent());
  }
}

package support.im.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.avos.avoscloud.AVAnalytics;
import support.ui.app.SupportSinglePaneActivity;

public abstract class BaseSinglePaneActivity extends SupportSinglePaneActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AVAnalytics.trackAppOpened(getIntent());
  }
}

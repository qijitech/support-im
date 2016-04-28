package support.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import support.im.events.EmptyEvent;

public class SupportActivity extends AppCompatActivity {

  @Override public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }

  @Override protected void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  public void onEvent(EmptyEvent event) {
  }
}

package support.im.demo;

import com.avos.avoscloud.AVOSCloud;
import pl.tajchert.nammu.Nammu;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(appContext(), BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey);
    Nammu.init(appContext());
  }
}
package support.im.demo;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import pl.tajchert.nammu.Nammu;
import support.im.data.SupportUser;
import support.im.leanclound.ChatManager;
import support.im.leanclound.contacts.AddRequest;
import support.im.service.PushManager;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    AVOSCloud.initialize(appContext(), BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey);
    Nammu.init(appContext());

    SupportUser.alwaysUseSubUserClass(SupportUser.class);
    AVObject.registerSubclass(AddRequest.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    PushManager.getInstance().init(appContext());
    AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);
    AVAnalytics.enableCrashReport(this, !BuildConfig.DEBUG);

    ChatManager.getInstance().init(this);
    ChatManager.setDebugEnabled(BuildConfig.DEBUG);
  }
}
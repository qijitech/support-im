package support.im.demo;

import android.support.multidex.MultiDex;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import pl.tajchert.nammu.Nammu;
import support.im.data.SupportUser;
import support.im.demo.features.main.MainActivity;
import support.im.leanclound.ChatManager;
import support.im.leanclound.contacts.AddRequest;
import support.im.service.PushManager;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    MultiDex.install(this);

    AVOSCloud.initialize(appContext(), BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey);
    Nammu.init(appContext());

    SupportUser.alwaysUseSubUserClass(SupportUser.class);
    AVObject.registerSubclass(AddRequest.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    PushManager.getInstance().initialize(appContext(), MainActivity.class);
    AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);
    AVAnalytics.enableCrashReport(this, !BuildConfig.DEBUG);

    ChatManager.getInstance().initialize();
    ChatManager.setDebugEnabled(BuildConfig.DEBUG);

    Fresco.initialize(appContext());

    Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build());
  }
}
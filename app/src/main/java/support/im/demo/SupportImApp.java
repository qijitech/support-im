package support.im.demo;

import android.content.Context;
import android.support.multidex.MultiDex;
import com.facebook.stetho.Stetho;
import support.im.SupportIm;
import support.im.demo.features.main.MainActivity;

public class SupportImApp extends support.im.SupportImApp {

  @Override public void onCreate() {
    SupportIm.initialize(BuildConfig.LeanCloundAppId, BuildConfig.LeanCloundAppKey,
        MainActivity.class, BuildConfig.DEBUG);
    super.onCreate();
    MultiDex.install(this);

    Stetho.initialize(Stetho.newInitializerBuilder(this)
        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
        .build());
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }
}
package support.im;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import pl.tajchert.nammu.Nammu;
import support.im.data.SupportUser;
import support.im.leanclound.ChatManager;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.Friend;
import support.im.service.PushManager;
import support.ui.app.SupportApp;

public class SupportImApp extends SupportApp {

  @Override public void onCreate() {
    super.onCreate();
    Nammu.init(appContext());
    FlowManager.init(new FlowConfig.Builder(this).build());

    AVOSCloud.initialize(appContext(), SupportIm.sApplicationId, SupportIm.sClientKey);
    SupportUser.alwaysUseSubUserClass(SupportUser.class);
    AVObject.registerSubclass(AddRequest.class);
    AVObject.registerSubclass(Friend.class);

    // 节省流量
    AVOSCloud.setLastModifyEnabled(true);

    PushManager.getInstance().initialize(appContext(), SupportIm.sDefaultPushCallback);
    AVOSCloud.setDebugLogEnabled(SupportIm.sDebugEnabled);
    AVAnalytics.enableCrashReport(this, !SupportIm.sDebugEnabled);

    ChatManager.getInstance().initialize();
    ChatManager.setDebugEnabled(SupportIm.sDebugEnabled);

    Fresco.initialize(appContext());
  }
}

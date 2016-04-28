package support.im.service;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.PushService;
import com.google.common.collect.Maps;
import java.util.Map;
import support.im.data.SupportUser;

public class PushManager {

  public final static String AVOS_ALERT = "alert";

  private final static String AVOS_PUSH_ACTION = "action";
  public static final String INSTALLATION_CHANNELS = "channels";
  private static PushManager sPushManager;
  private Context mContext;
  private Class<? extends Activity> mDefaultActivity;


  public synchronized static PushManager getInstance() {
    if (sPushManager == null) {
      sPushManager = new PushManager();
    }
    return sPushManager;
  }

  public void initialize(Context context, Class<? extends Activity> defaultActivity) {
    mContext = context;
    mDefaultActivity = defaultActivity;
    PushService.setDefaultPushCallback(context, defaultActivity);
    subscribeCurrentUserChannel();
  }

  private void subscribeCurrentUserChannel() {
    String currentUserId = SupportUser.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.subscribe(mContext, currentUserId, mDefaultActivity);
    }
  }

  public void unSubscribeCurrentUserChannel() {
    String currentUserId = SupportUser.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.unsubscribe(mContext, currentUserId);
    }
  }

  public void pushMessage(String userId, String message, String action) {
    AVQuery<AVInstallation> query = AVInstallation.getQuery();
    query.whereContains(INSTALLATION_CHANNELS, userId);
    AVPush push = new AVPush();
    push.setQuery(query);

    Map<String, Object> dataMap = Maps.newHashMap();
    dataMap.put(AVOS_ALERT, message);
    dataMap.put(AVOS_PUSH_ACTION, action);
    push.setData(dataMap);
    push.sendInBackground();
  }
}

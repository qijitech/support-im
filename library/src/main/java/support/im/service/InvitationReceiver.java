package support.im.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import de.greenrobot.event.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import support.im.R;
import support.im.events.InvitationEvent;
import support.im.leanclound.Constants;
import support.im.utilities.NotificationUtils;
import support.im.utilities.SupportLog;

/**
 * 好友邀请推送
 */
public class InvitationReceiver extends BroadcastReceiver {
  public final static String AVOS_DATA = "com.avoscloud.Data";

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (!TextUtils.isEmpty(action)) {
      if (action.equals(context.getString(R.string.support_im_add_request_invitation_action))) {
        String avosData = intent.getStringExtra(AVOS_DATA);
        if (!TextUtils.isEmpty(avosData)) {
          try {
            JSONObject json = new JSONObject(avosData);
            String alertStr = json.getString(PushManager.AVOS_ALERT);
            Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
            notificationIntent.putExtra(Constants.NOTOFICATION_TAG, Constants.NOTIFICATION_SYSTEM);
            NotificationUtils.showNotification(context, "SupportIm", alertStr, notificationIntent);
          } catch (JSONException e) {
            SupportLog.logException(e);
          }
        }
      }
    }
    EventBus.getDefault().post(new InvitationEvent());
  }
}

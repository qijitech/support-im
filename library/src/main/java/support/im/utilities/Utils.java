package support.im.utilities;

import android.content.Context;
import android.net.Uri;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
import support.im.leanclound.EmotionHelper;

public final class Utils {

  private Utils() {

  }

  public static Uri buildStaticMapUrl(AVGeoPoint location) {
    final String locString = location.getLongitude() + "," + location.getLatitude();
    StringBuffer sb = new StringBuffer("http://restapi.amap.com/v3/staticmap?key=ee95e52bf08006f63fd29bcfbcf21df0");
    sb.append("&zoom=17&size=200*150&scale=2");
    sb.append("&location=").append(locString);
    sb.append("&markers=mid,0xFF0000,A:").append(locString);
    return Uri.parse(sb.toString());
  }

  public static String millisecsToDateString(long timestamp) {
    long gap = System.currentTimeMillis() - timestamp;
    if (gap < 1000 * 60 * 60 * 24) {
      String s = (new PrettyTime()).format(new Date(timestamp));
      return s;
    } else {
      SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
      return format.format(new Date(timestamp));
    }
  }

  public static void closeQuietly(Closeable closeable) {
    try {
      closeable.close();
    } catch (Exception e) {
    }
  }

  public static CharSequence getMessageeShorthand(Context context, AVIMMessage message) {
    if (message instanceof AVIMTypedMessage) {
      AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(((AVIMTypedMessage) message).getMessageType());
      switch (type) {
        case TextMessageType:
          return EmotionHelper.replace(context, ((AVIMTextMessage) message).getText());
        case ImageMessageType:
          return "[图片]";
        case LocationMessageType:
          return "[位置]";
        case AudioMessageType:
          return "[语音]";
        default:
          return "[未知]";
      }
    } else {
      return message.getContent();
    }
  }
}

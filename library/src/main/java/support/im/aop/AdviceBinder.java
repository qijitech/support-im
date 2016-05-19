package support.im.aop;

import java.util.HashMap;
import java.util.Map;
import support.im.utilities.SupportLog;

public class AdviceBinder {

  private static Map<String, String> adviceBindMap = new HashMap();
  private static String TAG = "AdviceBinder";

  public AdviceBinder() {
  }

  public static void bindAdvice(PointCutEnum pointCutEnum, Class<? extends BaseAdvice> baseAdvice) {
    if (baseAdvice != null && baseAdvice.getName() != null) {
      SupportLog.d(TAG + "bindCustom", "执行绑定" + baseAdvice.getName());
      adviceBindMap.put(pointCutEnum.getName(), baseAdvice.getName());
    }
  }

  public static String getAdviceByPointCutName(PointCutEnum pointCutEnum) {
    return adviceBindMap.get(pointCutEnum.getName());
  }
}

package support.im.aop;

import android.text.TextUtils;
import java.lang.reflect.Constructor;

public class AdviceObjectInitUtil {

  public AdviceObjectInitUtil() {
  }

  public static BaseAdvice initAdvice(PointCutEnum pointEnum, Pointcut pointCut) {
    String adviceName = AdviceBinder.getAdviceByPointCutName(pointEnum);
    if (!TextUtils.isEmpty(adviceName)) {
      Object pointObj = getInstance(adviceName, Pointcut.class, pointCut);
      if (pointObj instanceof BaseAdvice) {
        return (BaseAdvice) pointObj;
      }
    }
    return null;
  }

  private static Object getInstance(String name, Class pointCutclass, Object pointCut) {
    Class[] classParas = new Class[] { pointCutclass };
    Object[] paras = new Object[] { pointCut };
    Object o = null;
    try {
      Class ex = getClass(name);
      Constructor con = ex.getDeclaredConstructor(classParas);
      try {
        con.setAccessible(true);
        o = con.newInstance(paras);
      } catch (Exception var9) {
      }
    } catch (Exception var10) {
    }
    return o;
  }

  private static Class getClass(String className) {
    Class c = null;
    try {
      c = Class.forName(className);
    } catch (ClassNotFoundException var3) {
    }
    return c;
  }
}

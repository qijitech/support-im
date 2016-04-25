package support.im.utilities;

import support.ui.utilities.FileLog;

public final class SupportLog {

  public static final String LOGTAG = "Support-Im";
  public static boolean debugEnabled;

  private static String getDebugInfo() {
    Throwable stack = new Throwable().fillInStackTrace();
    StackTraceElement[] trace = stack.getStackTrace();
    int n = 2;
    return trace[n].getClassName() + " " + trace[n].getMethodName() + "()" + ":" + trace[n].getLineNumber() + " ";
  }

  private static String getLogInfoByArray(String[] infos) {
    StringBuilder sb = new StringBuilder();
    for (String info : infos) {
      sb.append(info);
      sb.append(" ");
    }
    return sb.toString();
  }

  public static void d(String... s) {
    if (debugEnabled) {
      FileLog.d(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
    }
  }

  public static void e(String... s) {
    if (debugEnabled) {
      FileLog.e(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
    }
  }

  public static void w(String... s) {
    if (debugEnabled) {
      FileLog.w(LOGTAG, getDebugInfo() + getLogInfoByArray(s));
    }
  }

  public static void logException(Throwable tr) {
    if (debugEnabled) {
      FileLog.e(LOGTAG, getDebugInfo(), tr);
    }
  }

}

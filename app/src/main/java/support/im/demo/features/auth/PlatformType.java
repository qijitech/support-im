package support.im.demo.features.auth;

import java.util.HashMap;
import java.util.Map;

public enum PlatformType {
  /**
   * 微信
   */
  WECHAT("wechat"),

  /**
   * 腾讯QQ
   */
  QQ("qq"),

  /**
   * 新浪微博
   */
  WEIBO("weibo");

  private final String mValue;

  PlatformType(String value) {
    mValue = value;
  }

  public String toString() {
    return mValue;
  }

  private static final Map<String, PlatformType> STRING_MAPPING = new HashMap<>();

  static {
    for (PlatformType calorieType : PlatformType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  public static PlatformType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }

}

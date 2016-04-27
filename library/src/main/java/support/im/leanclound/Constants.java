package support.im.leanclound;

public class Constants {

  private static final String CONSTANTS_PREFIX = "support.im_";

  public static final String OBJECT_ID = "objectId";
  public static final int PAGE_SIZE = 10;
  public static final String CREATED_AT = "createdAt";
  public static final String UPDATED_AT = "updatedAt";

  public static final String MEMBER_ID = getPrefixConstant("member_id");
  public static final String CONVERSATION_ID = getPrefixConstant("conversation_id");

  //Notification
  public static final String NOTOFICATION_TAG = getPrefixConstant("notification_tag");
  public static final String NOTIFICATION_SINGLE_CHAT = Constants.getPrefixConstant("notification_single_chat");

  public static String getPrefixConstant(String str) {
    return CONSTANTS_PREFIX + str;
  }
}

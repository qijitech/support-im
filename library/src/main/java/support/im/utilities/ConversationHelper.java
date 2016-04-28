package support.im.utilities;

import com.avos.avoscloud.im.v2.AVIMConversation;
import support.im.data.ConversationType;
import support.im.leanclound.ChatManager;

public final class ConversationHelper {

  public static boolean isValidConversation(AVIMConversation conversation) {
    if (conversation == null) {
      SupportLog.d("invalid reason : conversation is null");
      return false;
    }

    if (conversation.getMembers() == null || conversation.getMembers().size() == 0) {
      SupportLog.d("invalid reason : conversation members null or empty");
      return false;
    }

    Object type = conversation.getAttribute(ConversationType.TYPE_KEY);
    if (type == null) {
      SupportLog.d("invalid reason : type is null");
      return false;
    }

    int typeInt = (Integer) type;
    if (typeInt == ConversationType.Single.getValue()) {
      if (conversation.getMembers().size() != 2 ||
          conversation.getMembers().contains(ChatManager.getInstance().getClientId()) == false) {
        SupportLog.d("invalid reason : oneToOne conversation not correct");
        return false;
      }
    } else if (typeInt == ConversationType.Group.getValue()) {

    } else {
      SupportLog.d("invalid reason : typeInt wrong");
      return false;
    }
    return true;
  }

  public static ConversationType typeOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      Object typeObject = conversation.getAttribute(ConversationType.TYPE_KEY);
      int typeInt = (Integer) typeObject;
      return ConversationType.fromInt(typeInt);
    } else {
      SupportLog.e("invalid conversation ");
      // 因为 Group 不需要取 otherId，检查没那么严格，避免导致崩溃
      return ConversationType.Group;
    }
  }

}

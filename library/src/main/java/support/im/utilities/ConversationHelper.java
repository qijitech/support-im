package support.im.utilities;

import com.avos.avoscloud.im.v2.AVIMConversation;
import support.im.data.ConversationType;

public final class ConversationHelper {

  public static boolean isValidConversation(AVIMConversation conversation) {
    if (conversation == null) {
      Log.d("invalid reason : conversation is null");
      return false;
    }

    if (conversation.getMembers() == null || conversation.getMembers().size() == 0) {
      Log.d("invalid reason : conversation members null or empty");
      return false;
    }

    Object type = conversation.getAttribute(ConversationType.TYPE_KEY);
    if (type == null) {
      Log.d("invalid reason : type is null");
      return false;
    }

    int typeInt = (Integer) type;
    if (typeInt == ConversationType.Single.getValue()) {
      if (conversation.getMembers().size() != 2 ||
          conversation.getMembers().contains(ChatManager.getInstance().getSelfId()) == false) {
        Log.d("invalid reason : oneToOne conversation not correct");
        return false;
      }
    } else if (typeInt == ConversationType.Group.getValue()) {

    } else {
      Log.d("invalid reason : typeInt wrong");
      return false;
    }
    return true;
  }
}

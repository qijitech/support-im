package support.im.utilities;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.JSONHelper;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.ConversationType;
import support.im.data.SimpleUser;
import support.im.leanclound.ChatManager;
import support.im.leanclound.ThirdPartUserUtils;

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
      if (conversation.getMembers().size() != 2
          || conversation.getMembers().contains(ChatManager.getInstance().getClientId()) == false) {
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

  /**
   * 获取单聊对话的另外一个人的 userId
   *
   * @return 如果非法对话，则为 selfId
   */
  public static String otherIdOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      if (typeOfConversation(conversation) == ConversationType.Single) {
        List<String> members = conversation.getMembers();
        if (members.size() == 2) {
          if (members.get(0).equals(ChatManager.getInstance().getClientId())) {
            return members.get(1);
          } else {
            return members.get(0);
          }
        }
      }
    }
    // 尽管异常，返回可以使用的 userId
    return ChatManager.getInstance().getClientId();
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

  public static String nameOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      if (typeOfConversation(conversation) == ConversationType.Single) {
        String otherId = otherIdOfConversation(conversation);
        String userName = ThirdPartUserUtils.getInstance().getUserName(otherId);
        return (TextUtils.isEmpty(userName) ? "对话" : userName);
      } else {
        return conversation.getName();
      }
    } else {
      return "";
    }
  }

  public static String displayNameOfConversation(AVIMConversation conversation) {
    if (!isValidConversation(conversation)) {
      return "";
    }

    if (typeOfConversation(conversation) == ConversationType.Group) {
      return conversation.getName();
    }

    // 单聊
    String otherId = otherIdOfConversation(conversation);
    Object object = conversation.getAttribute(Conversation.ATTRS_MEMBERS);
    if (object instanceof JSONArray) {
      JSONArray jsonArray = (JSONArray) object;
      final int size = jsonArray.size();
      for(int i = 0; i < size;i++){
        JSONObject jsonObject = (JSONObject)jsonArray.get(i);
        SimpleUser user = JSON.toJavaObject(jsonObject,SimpleUser.class);
        if (otherId.equals(user.getObjectId())) {
          return user.getDisplayName();
        }
      }
    }
    return "对话";
  }

}

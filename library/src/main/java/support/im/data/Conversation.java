package support.im.data;

import com.avos.avoscloud.im.v2.AVIMMessage;

public class Conversation {

  public String mId;
  public AVIMMessage mLastMessage;
  public int mUnreadCount;

  public long getLastModifyTime() {
    if (mLastMessage != null) {
      return mLastMessage.getTimestamp();
    }

    //AVIMConversation conversation = getConversation();
    //if (null != conversation && null != conversation.getUpdatedAt()) {
    //  return conversation.getUpdatedAt().getTime();
    //}

    return 0;
  }

  //public AVIMConversation getConversation() {
    //return AVIMConversationCacheUtils.getCacheConversation(mId);
  //}

}

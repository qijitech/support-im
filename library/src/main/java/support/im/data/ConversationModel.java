package support.im.data;

import android.annotation.SuppressLint;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.data.cache.CacheManager;
import support.im.database.Conversation;

@SuppressLint("ParcelCreator") public class ConversationModel extends support.im.database.Conversation {

  public static final String ATTRS_MEMBERS = "members";

  public ConversationModel() {
  }

  public ConversationModel(Conversation conversation) {
    setConversationId(conversation.getConversationId());
    setId(conversation.getId());
    setTitle(conversation.getTitle());
    setLastMessageTime(conversation.getLastMessageTime());
    setUnReadCount(conversation.getUnReadCount());
  }

  public AVIMMessage mLastMessage;

  public long getLastModifyTime() {
    if (mLastMessage != null) {
      return mLastMessage.getTimestamp();
    }

    AVIMConversation conversation = getConversation();
    if (null != conversation && null != conversation.getUpdatedAt()) {
      return conversation.getUpdatedAt().getTime();
    }

    return 0;
  }

  public AVIMConversation getConversation() {
    return CacheManager.getInstance().getCacheConversation(getConversationId());
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ConversationModel that = (ConversationModel) o;
    return com.google.common.base.Objects.equal(getConversationId(), that.getConversationId());
  }

  @Override public int hashCode() {
    return com.google.common.base.Objects.hashCode(super.hashCode(), getConversationId());
  }
}

package support.im.data;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import support.im.data.cache.CacheManager;

@Table(database = AppDatabase.class, name = "conversations")
public class Conversation extends BaseModel {

  // 会话id
  @PrimaryKey @Column(name = "conversation_id") private String conversationId;
  // 当前用户id
  @Column(name = "client_id") private String clientId;
  // 会话未读数量
  @Column(name = "unread_count") private int unreadCount;
  // 会话名称（用户名称或者群名称）
  @Column(name = "name") private String name;
  // 创建人id
  @Column(name = "creator") private String creator;
  // 最新消息是谁发送的
  @Column(name = "from_peer_id") private String fromPeerId;
  // 成员
  @Column(name = "members") private String members;
  // 0->false 1-true
  @Column(name = "is_transient", getterName = "isTransient", defaultValue = "0") private boolean isTransient;
  // 会话类型 0-单聊 1-群聊
  @Column(name = "type", defaultValue = "0") private int type;
  // 第一条消息id
  @Column(name = "first_msg_id") private String firstMsgId;
  // 第一条消息时间
  @Column(name = "first_msg_time") private long firstMsgTime;
  // 最新一条消息时间
  @Column(name = "latest_msg") private String latestMsg;
  // 最新一条消息时间
  @Column(name = "latest_msg_time") private long latestMsgTime;

  @ColumnIgnore private AVIMMessage lastMessage;

  public AVIMConversation getConversation() {
    return CacheManager.getCacheConversation(getConversationId());
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public void setUnreadCount(int unreadCount) {
    this.unreadCount = unreadCount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getFromPeerId() {
    return fromPeerId;
  }

  public void setFromPeerId(String fromPeerId) {
    this.fromPeerId = fromPeerId;
  }

  public String getMembers() {
    return members;
  }

  public void setMembers(String members) {
    this.members = members;
  }

  public boolean isTransient() {
    return isTransient;
  }

  public void setIsTransient(boolean isTransient) {
    this.isTransient = isTransient;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getFirstMsgId() {
    return firstMsgId;
  }

  public void setFirstMsgId(String firstMsgId) {
    this.firstMsgId = firstMsgId;
  }

  public long getFirstMsgTime() {
    return firstMsgTime;
  }

  public void setFirstMsgTime(long firstMsgTime) {
    this.firstMsgTime = firstMsgTime;
  }

  public String getLatestMsg() {
    return latestMsg;
  }

  public void setLatestMsg(String latestMsg) {
    this.latestMsg = latestMsg;
  }

  public long getLatestMsgTime() {
    return latestMsgTime;
  }

  public void setLatestMsgTime(long latestMsgTime) {
    this.latestMsgTime = latestMsgTime;
  }

  public AVIMMessage getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(AVIMMessage lastMessage) {
    this.lastMessage = lastMessage;
  }

  public static class Builder {
    private String conversationId;
    private String clientId;
    private int unreadCount;
    private String name;
    private String creator;
    private String fromPeerId;
    private String members;
    private boolean isTransient;
    private int type;
    private String firstMsgId;
    private long firstMsgTime;
    private long latestMsgTime;
    private String latestMsg;

    public Builder conversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder unreadCount(int unreadCount) {
      this.unreadCount = unreadCount;
      return this;
    }

    public Builder firstMsgId(String firstMsgId) {
      this.firstMsgId = firstMsgId;
      return this;
    }

    public Builder firstMsgTime(long firstMsgTime) {
      this.firstMsgTime = firstMsgTime;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder creator(String creator) {
      this.creator = creator;
      return this;
    }

    public Builder latestMsg(String latestMsg) {
      this.latestMsg = latestMsg;
      return this;
    }

    public Builder type(int type) {
      this.type = type;
      return this;
    }

    public Builder latestMsgTime(long latestMsgTime) {
      this.latestMsgTime = latestMsgTime;
      return this;
    }

    public Builder isTransient(boolean isTransient) {
      this.isTransient = isTransient;
      return this;
    }

    public Builder members(String members) {
      this.members = members;
      return this;
    }

    public Builder fromPeerId(String fromPeerId) {
      this.fromPeerId = fromPeerId;
      return this;
    }

    public Conversation build() {
      Conversation conversation = new Conversation();
      conversation.conversationId = conversationId;
      conversation.clientId = clientId;
      conversation.unreadCount = unreadCount;
      conversation.name = name;
      conversation.creator = creator;
      conversation.fromPeerId = fromPeerId;
      conversation.members = members;
      conversation.isTransient = isTransient;
      conversation.type = type;
      conversation.firstMsgId = firstMsgId;
      conversation.firstMsgTime = firstMsgTime;
      conversation.latestMsg = latestMsg;
      conversation.latestMsgTime = latestMsgTime;
      return conversation;
    }
  }
}
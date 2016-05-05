package support.im.data;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ColumnIgnore;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class Conv extends BaseModel {

  @PrimaryKey @Column(name = "conv_id") private String conversationId;
  @Column(name = "client_id") private String clientId;
  @Column(name = "unread_count") private int unreadCount;
  @Column(name = "display_name") private String displayName;
  @Column(name = "message") private String message;
  @Column(name = "first_msg_id") private String firstMsgId;
  @Column(name = "first_msg_time") private long firstMsgTime;
  @Column(name = "latest_msg_time") private long latestMsgTime;

  @ColumnIgnore public AVIMMessage lastMessage;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getConversationId() {
    return conversationId;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public void setUnreadCount(int unreadCount) {
    this.unreadCount = unreadCount;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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
    private String displayName;
    private String message;
    private String firstMsgId;
    private long firstMsgTime;
    private long latestMsgTime;

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

    public Builder displayName(String displayName) {
      this.displayName = displayName;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder latestMsgTime(long latestMsgTime) {
      this.latestMsgTime = latestMsgTime;
      return this;
    }

    public Conv build() {
      Conv conv = new Conv();
      conv.conversationId = conversationId;
      conv.clientId = clientId;
      conv.unreadCount = unreadCount;
      conv.displayName = displayName;
      conv.message = message;
      conv.firstMsgId = firstMsgId;
      conv.firstMsgTime = firstMsgTime;
      conv.latestMsgTime = latestMsgTime;
      return conv;
    }
  }
}

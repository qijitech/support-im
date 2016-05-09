package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import support.im.leanclound.ChatManager;
import support.ui.adapters.BaseEasyViewHolderFactory;
import support.ui.adapters.EasyViewHolder;

public class ChatsViewHolderFactory extends BaseEasyViewHolderFactory {

  private final int ITEM_INCOMING = 100;
  private final int ITEM_INCOMING_TEXT = 101;
  private final int ITEM_INCOMING_IMAGE = 102;
  private final int ITEM_INCOMING_AUDIO = 103;
  private final int ITEM_INCOMING_LOCATION = 104;

  private final int ITEM_OUTGOING = 200;
  private final int ITEM_OUTGOING_TEXT = 201;
  private final int ITEM_OUTGOING_IMAGE = 202;
  private final int ITEM_OUTGOING_AUDIO = 203;
  private final int ITEM_OUTGOING_LOCATION = 204;

  ChatManager chatManager;

  public ChatsViewHolderFactory(Context context) {
    super(context);
    chatManager = ChatManager.getInstance();
  }

  @Override public EasyViewHolder create(int viewType, ViewGroup parent) {
    switch (viewType) {
      case ITEM_INCOMING_TEXT:
        return new ChatsIncomingTextViewHolder(parent.getContext(), parent);
      case ITEM_INCOMING_IMAGE:
        return new ChatsIncomingImageViewHolder(parent.getContext(), parent);
      case ITEM_INCOMING_AUDIO:
        return new ChatsIncomingAudioViewHolder(parent.getContext(), parent);
      case ITEM_INCOMING_LOCATION:
        return new ChatsIncomingLocationViewHolder(parent.getContext(), parent);
      case ITEM_OUTGOING_TEXT:
        return new ChatsOutgoingTextViewHolder(parent.getContext(), parent);
      case ITEM_OUTGOING_IMAGE:
        return new ChatsOutgoingImageViewHolder(parent.getContext(), parent);
      case ITEM_OUTGOING_AUDIO:
        return new ChatsOutgoingAudioViewHolder(parent.getContext(), parent);
      case ITEM_OUTGOING_LOCATION:
        return new ChatsOutgoingLocationViewHolder(parent.getContext(), parent);
      case ITEM_OUTGOING:
        return new ChatsOutgoingImageViewHolder(parent.getContext(), parent);
      case ITEM_INCOMING:
        return new ChatsIncomingTextViewHolder(parent.getContext(), parent);
      default:
        return super.create(viewType, parent);
    }
  }

  @Override public int itemViewType(Object object) {
    if (object instanceof AVIMTypedMessage) {
      AVIMTypedMessage typedMessage = (AVIMTypedMessage) object;
      boolean isMe = fromMe(typedMessage);
      if (typedMessage.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
        return isMe ? ITEM_OUTGOING_TEXT : ITEM_INCOMING_TEXT;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.AudioMessageType.getType()) {
        return isMe ? ITEM_OUTGOING_AUDIO : ITEM_INCOMING_AUDIO;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.ImageMessageType.getType()) {
        return isMe ? ITEM_OUTGOING_IMAGE : ITEM_INCOMING_IMAGE;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.LocationMessageType.getType()) {
        return isMe ? ITEM_OUTGOING_LOCATION : ITEM_INCOMING_LOCATION;
      } else {
        return isMe ? ITEM_OUTGOING : ITEM_INCOMING;
      }
    }
    return super.itemViewType(object);
  }

  private boolean fromMe(AVIMTypedMessage msg) {
    String selfId = chatManager.getClientId();
    return msg.getFrom() != null && msg.getFrom().equals(selfId);
  }
}

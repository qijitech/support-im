package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import support.im.leanclound.ChatManager;
import support.ui.adapters.BaseEasyViewHolderFactory;
import support.ui.adapters.EasyViewHolder;

public class ChatsViewHolderFactory extends BaseEasyViewHolderFactory {

  private final int ITEM_LEFT = 100;
  private final int ITEM_LEFT_TEXT = 101;
  private final int ITEM_LEFT_IMAGE = 102;
  private final int ITEM_LEFT_AUDIO = 103;
  private final int ITEM_LEFT_LOCATION = 104;

  private final int ITEM_RIGHT = 200;
  private final int ITEM_RIGHT_TEXT = 201;
  private final int ITEM_RIGHT_IMAGE = 202;
  private final int ITEM_RIGHT_AUDIO = 203;
  private final int ITEM_RIGHT_LOCATION = 204;

  public ChatsViewHolderFactory(Context context) {
    super(context);
  }

  @Override public EasyViewHolder create(int viewType, ViewGroup parent) {
    switch (viewType) {
      case ITEM_LEFT_TEXT:
        return new ChatsTextViewHolder(parent.getContext(), parent, true);
      case ITEM_LEFT_IMAGE:
        return new ChatsImageViewHolder(parent.getContext(), parent, true);
      case ITEM_LEFT_AUDIO:
        return new ChatsAudioViewHolder(parent.getContext(), parent, true);
      case ITEM_LEFT_LOCATION:
        return new ChatsLocationViewHolder(parent.getContext(), parent, true);
      case ITEM_RIGHT_TEXT:
        return new ChatsTextViewHolder(parent.getContext(), parent, false);
      case ITEM_RIGHT_IMAGE:
        return new ChatsImageViewHolder(parent.getContext(), parent, false);
      case ITEM_RIGHT_AUDIO:
        return new ChatsAudioViewHolder(parent.getContext(), parent, false);
      case ITEM_RIGHT_LOCATION:
        return new ChatsLocationViewHolder(parent.getContext(), parent, false);
      default:
        return super.create(viewType, parent);
    }
  }

  @Override public int itemViewType(Object object) {
    if (object instanceof AVIMTypedMessage) {
      AVIMTypedMessage typedMessage = (AVIMTypedMessage) object;
      boolean isMe = fromMe(typedMessage);
      if (typedMessage.getMessageType() == AVIMReservedMessageType.TextMessageType.getType()) {
        return isMe ? ITEM_RIGHT_TEXT : ITEM_LEFT_TEXT;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.AudioMessageType.getType()) {
        return isMe ? ITEM_RIGHT_AUDIO : ITEM_LEFT_AUDIO;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.ImageMessageType.getType()) {
        return isMe ? ITEM_RIGHT_IMAGE : ITEM_LEFT_IMAGE;
      } else if (typedMessage.getMessageType() == AVIMReservedMessageType.LocationMessageType.getType()) {
        return isMe ? ITEM_RIGHT_LOCATION : ITEM_LEFT_LOCATION;
      } else {
        return isMe ? ITEM_RIGHT : ITEM_LEFT;
      }
    }
    return super.itemViewType(object);
  }

  private boolean fromMe(AVIMTypedMessage msg) {
    ChatManager chatManager = ChatManager.getInstance();
    String selfId = chatManager.getClientId();
    return msg.getFrom() != null && msg.getFrom().equals(selfId);
  }
}

package support.im.chats;

import android.content.Context;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import support.im.chats.viewholder.ChatsCommonViewHolder;
import support.im.chats.viewholder.ChatsIncomingViewHolder;
import support.im.leanclound.ChatManager;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;

public class ChatsAdapter extends EasyRecyclerAdapter {

  private boolean isShowDisplayName = true;
  private final String mSelfClientId;
  // 时间间隔最小为十分钟
  private final static long TIME_INTERVAL = 1000 * 60 * 3;

  public ChatsAdapter(Context context) {
    super(context);
    mSelfClientId = ChatManager.getInstance().getClientId();
  }

  @Override public void onBindViewHolder(EasyViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    if (holder instanceof ChatsCommonViewHolder) {
      ChatsCommonViewHolder chatsViewHolder = (ChatsCommonViewHolder) holder;
      chatsViewHolder.showTimeView(shouldShowTime(position));
    }
    if (holder instanceof ChatsIncomingViewHolder) {
      ChatsIncomingViewHolder chatsViewHolder = (ChatsIncomingViewHolder) holder;
      chatsViewHolder.showDisplayName(isShowDisplayName);
    }
  }

  private boolean shouldShowTime(int position) {
    if (position == 0) {
      return true;
    }
    final Object lastMessageObject = get(position-1);
    final Object messageObject = get(position);
    if (lastMessageObject instanceof AVIMTextMessage && messageObject instanceof AVIMMessage) {
      AVIMMessage message = (AVIMMessage) messageObject;
      AVIMMessage lastMessage = (AVIMMessage) lastMessageObject;
      long lastTime = lastMessage.getTimestamp();
      long curTime = message.getTimestamp();
      return curTime - lastTime > TIME_INTERVAL;
    }
    return false;
  }

  private boolean fromMe(AVIMTypedMessage msg) {
    return msg.getFrom() != null && msg.getFrom().equals(mSelfClientId);
  }

  public void shouldShowDisplayName(boolean shouldShow) {
    isShowDisplayName = shouldShow;
  }

  public AVIMMessage getFirstMessage() {
    if (getItemCount() > 0) {
      return (AVIMMessage) get(0);
    } else {
      return null;
    }
  }

}

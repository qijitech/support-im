package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.R;
import support.ui.adapters.EasyViewHolder;

public class ChatViewHolder extends EasyViewHolder<AVIMMessage> {

  public ChatViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_right_item);
  }

  @Override public void bindTo(int position, AVIMMessage value) {

  }
}

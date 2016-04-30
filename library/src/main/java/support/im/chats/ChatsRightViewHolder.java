package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.R;
import support.ui.adapters.EasyViewHolder;

public class ChatsRightViewHolder extends EasyViewHolder<AVIMMessage> {

  TextView mMessageTextView;

  public ChatsRightViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_right_item);
    mMessageTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_message);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    mMessageTextView.setText("梦想合伙人");
  }
}

package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import support.im.R;
import support.im.emoticons.ChatsUtils;

public class ChatsIncomingTextViewHolder extends ChatsIncomingViewHolder {

  protected TextView mMessageTextView;

  public ChatsIncomingTextViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_incoming_item_text);
    mMessageTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_content);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) value;
      ChatsUtils.spannableEmoticonFilter(mMessageTextView, textMessage.getText());
    }
  }
}

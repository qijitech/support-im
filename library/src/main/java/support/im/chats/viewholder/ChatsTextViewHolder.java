package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import support.im.R;
import support.im.leanclound.EmotionHelper;

public abstract class ChatsTextViewHolder extends ChatsOutgoingViewHolder {

  protected TextView mMessageTextView;

  public ChatsTextViewHolder(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
    mMessageTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_content);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) value;
      mMessageTextView.setText(EmotionHelper.replace(mContext, textMessage.getText()));
    }
  }
}

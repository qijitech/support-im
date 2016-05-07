package support.im.chats.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import support.im.R;
import support.im.leanclound.EmotionHelper;

public class ChatsTextViewHolder extends ChatsViewHolder {

  protected TextView mContentTextView;

  public ChatsTextViewHolder(Context context, ViewGroup parent, boolean isLeft) {
    super(context, parent, isLeft);
  }

  @Override protected void setupView() {
    super.setupView();
    if (mIsLeft) {
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_incoming_item_text, null));
    } else {
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_outgoing_item_text, null));
    }
    mContentTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_content);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) value;
      mContentTextView.setText(EmotionHelper.replace(mContext, textMessage.getText()));
    }
  }
}

package support.im.chats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_left_item_text, null));
      mContentTextView = (TextView) itemView.findViewById(R.id.text_support_im_chats_item_left_content);
    } else {
      mContentContainer.addView(View.inflate(mContext, R.layout.chats_right_item_text, null));
      mContentTextView = (TextView) itemView.findViewById(R.id.text_support_im_chats_item_right_content);
    }
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) value;
      mContentTextView.setText(EmotionHelper.replace(mContext, textMessage.getText()));
    }
  }
}

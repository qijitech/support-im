package support.im.chats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import support.im.R;

public class ChatsLocationViewHolder extends ChatsViewHolder {

  protected TextView mLocationTextView;

  public ChatsLocationViewHolder(Context context, ViewGroup parent, boolean isLeft) {
    super(context, parent, isLeft);
  }

  @Override protected void setupView() {
    super.setupView();
    mContentContainer.addView(View.inflate(mContext, R.layout.chats_item_location, null));
    mLocationTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_location);
    mContentContainer.setBackgroundResource(mIsLeft ? R.drawable.support_im_chats_left_content_bg
        : R.drawable.support_im_chats_right_content_bg);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMLocationMessage) {
      final AVIMLocationMessage locMsg = (AVIMLocationMessage) value;
      mLocationTextView.setText(locMsg.getText());
    }
  }
}

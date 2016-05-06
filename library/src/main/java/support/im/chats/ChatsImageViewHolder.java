package support.im.chats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import support.im.R;

public class ChatsImageViewHolder extends ChatsViewHolder {

  protected ImageView mContentImageView;

  public ChatsImageViewHolder(Context context, ViewGroup parent, boolean isLeft) {
    super(context, parent, isLeft);
  }

  @Override protected void setupView() {
    super.setupView();
    mContentContainer.addView(View.inflate(mContext, R.layout.chats_item_image, null));
    mContentImageView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_content);
    if (mIsLeft) {
      mContentContainer.setBackgroundResource(R.drawable.si_selector_incoming_photo);
    } else {
      mContentContainer.setBackgroundResource(R.drawable.si_selector_outgoing_photo);
    }
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMImageMessage) {

    }
  }
}

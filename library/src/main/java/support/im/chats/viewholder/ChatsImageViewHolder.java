package support.im.chats.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;

public class ChatsImageViewHolder extends ChatsViewHolder {

  protected SimpleDraweeView mContentImageView;

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
      AVIMImageMessage avimImageMessage = (AVIMImageMessage) value;
      String localFilePath = avimImageMessage.getLocalFilePath();
      if (!TextUtils.isEmpty(localFilePath)) {
        mContentImageView.setImageURI(Uri.parse("file://" + localFilePath));
      } else {
        mContentImageView.setImageURI(Uri.parse(avimImageMessage.getFileUrl()));
      }
    }
  }
}

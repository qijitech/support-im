package support.im.chats.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.utilities.FrescoDisplayUtils;
import support.im.utilities.ThumbnailUtils;

public class ChatsIncomingImageViewHolder extends ChatsIncomingViewHolder {

  protected SimpleDraweeView mContentImageView;
  protected FrameLayout mImageViewContainer;

  public ChatsIncomingImageViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_incoming_item_image);
    mContentImageView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_content);
    mImageViewContainer = ButterKnife.findById(itemView, R.id.container_support_im_chats_item_content);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMImageMessage) {
      AVIMImageMessage avimImageMessage = (AVIMImageMessage) value;
      String localFilePath = avimImageMessage.getLocalFilePath();
      final int width = avimImageMessage.getWidth();
      final int height = avimImageMessage.getHeight();
      int[] thumbSize = ThumbnailUtils.getThumbSize(width, height);
      LinearLayout.LayoutParams params =
          (LinearLayout.LayoutParams) mImageViewContainer.getLayoutParams();
      params.width = thumbSize[0];
      params.height = thumbSize[1];
      mImageViewContainer.setLayoutParams(params);

      if (!TextUtils.isEmpty(localFilePath)) {
        FrescoDisplayUtils.showThumb(Uri.parse("file://" + localFilePath), mContentImageView, thumbSize[0], thumbSize[1]);
      } else {
        FrescoDisplayUtils.showThumb(Uri.parse(avimImageMessage.getFileUrl()), mContentImageView, thumbSize[0], thumbSize[1]);
      }
    }
  }
}

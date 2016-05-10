package support.im.chats.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.utilities.FrescoDisplayUtils;
import support.im.utilities.ThumbnailUtils;
import support.ui.utilities.ScreenUtils;

public class ChatsOutgoingImageViewHolder extends ChatsOutgoingViewHolder {

  protected SimpleDraweeView mContentImageView;
  private static int screenWidth;
  public ChatsOutgoingImageViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_outgoing_item_image);
    screenWidth = ScreenUtils.getScreenWidth();
    mContentImageView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_content);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMImageMessage) {
      AVIMImageMessage avimImageMessage = (AVIMImageMessage) value;
      String localFilePath = avimImageMessage.getLocalFilePath();
      final int width = avimImageMessage.getWidth();
      final int height = avimImageMessage.getHeight();
      int[] thumbSize = ThumbnailUtils.getThumbSize(width, height);
      if (!TextUtils.isEmpty(localFilePath)) {
        FrescoDisplayUtils.showThumb(Uri.parse("file://" + localFilePath), mContentImageView, 200, 400);
      } else {
        FrescoDisplayUtils.showThumb(Uri.parse(avimImageMessage.getFileUrl()), mContentImageView, 200, 400);
      }
    }
  }

}

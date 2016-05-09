package support.im.chats.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.Map;
import support.im.R;
import support.im.utilities.FrescoDisplayUtils;
import support.ui.utilities.AndroidUtilities;
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
      if (!TextUtils.isEmpty(localFilePath)) {
        FrescoDisplayUtils.showThumb(Uri.parse("file://" + localFilePath), mContentImageView, 200, 400);
      } else {
        FrescoDisplayUtils.showThumb(Uri.parse(avimImageMessage.getFileUrl()), mContentImageView, 200, 400);
      }
    }
  }

  // TODO
  private int[] getThumbSize(AVIMImageMessage avimImageMessage) {
    Map<String, Object> fileMetaData = avimImageMessage.getFileMetaData();
    int width = (Integer) fileMetaData.get("width");
    int height = (Integer) fileMetaData.get("height");

    if (width > screenWidth) {

    }
    return new int[] { width, height };
  }

}

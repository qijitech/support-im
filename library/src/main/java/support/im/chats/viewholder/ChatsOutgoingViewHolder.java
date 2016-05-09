package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import support.im.R;
import support.im.data.SupportUser;
import support.ui.utilities.ViewUtils;

public abstract class ChatsOutgoingViewHolder extends ChatsCommonViewHolder {

  // status views
  protected FrameLayout mStatusContainer;
  protected ProgressBar mStatusProgressBar;
  protected TextView mStatusTextView;
  protected ImageView mStatusErrorView;

  public ChatsOutgoingViewHolder(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
    mStatusContainer = ButterKnife.findById(itemView, R.id.container_support_im_chats_item_status);
    mStatusProgressBar = ButterKnife.findById(itemView, R.id.progress_support_im_chats_item_status);
    mStatusTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_status);
    mStatusErrorView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_error);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    mAvatarView.setImageURI(SupportUser.getCurrentUser().toAvatarUri());
    setupStatus(value);
  }

  private void setupStatus(AVIMMessage message) {
    switch (message.getMessageStatus()) {
      case AVIMMessageStatusFailed:
        ViewUtils.setGone(mStatusContainer, false);
        ViewUtils.setGone(mStatusProgressBar, true);
        ViewUtils.setGone(mStatusTextView, true);
        ViewUtils.setGone(mStatusErrorView, true);
        break;
      case AVIMMessageStatusSent:
        ViewUtils.setGone(mStatusContainer, false);
        ViewUtils.setGone(mStatusProgressBar, true);
        ViewUtils.setGone(mStatusTextView, false);
        ViewUtils.setGone(mStatusErrorView, true);
        break;
      case AVIMMessageStatusSending:
        ViewUtils.setGone(mStatusContainer, false);
        ViewUtils.setGone(mStatusProgressBar, false);
        ViewUtils.setGone(mStatusTextView, true);
        ViewUtils.setGone(mStatusErrorView, true);
        break;
      case AVIMMessageStatusNone:
      case AVIMMessageStatusReceipt:
        ViewUtils.setGone(mStatusContainer, true);
        break;
    }
  }
}

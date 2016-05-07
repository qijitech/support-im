package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.SupportUser;
import support.im.data.cache.CacheManager;
import support.im.utilities.Utils;
import support.ui.adapters.EasyViewHolder;
import support.ui.utilities.ViewUtils;

public class ChatsViewHolder extends EasyViewHolder<AVIMMessage> {

  protected AVIMMessage mMessage;

  protected FrameLayout mContentContainer;
  protected TextView mSentAtTextView;
  protected TextView mDisplayNameTextView;
  protected SimpleDraweeView mAvatarView;

  // status views
  protected FrameLayout mStatusContainer;
  protected ProgressBar mStatusProgressBar;
  protected TextView mStatusTextView;
  protected ImageView mStatusErrorView;

  protected boolean mIsLeft;
  protected Context mContext;

  public ChatsViewHolder(Context context, ViewGroup parent, boolean isLeft) {
    super(context, parent, isLeft ? R.layout.chats_incoming_item : R.layout.chats_outgoing_item);
    mIsLeft = isLeft;
    mContext = context;
    setupView();
  }

  protected void setupView() {
    mContentContainer = ButterKnife.findById(itemView, R.id.container_support_im_chats_item_content);
    mContentContainer.removeAllViews();
    mSentAtTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_sent_at);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_avatar);
    if (mIsLeft) {
      mDisplayNameTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_display_name);
    } else {
      mStatusContainer = ButterKnife.findById(itemView, R.id.container_support_im_chats_item_status);
      mStatusProgressBar = ButterKnife.findById(itemView, R.id.progress_support_im_chats_item_status);
      mStatusTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_status);
      mStatusErrorView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_error);
    }
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    mMessage = value;
    String objectId = value.getFrom();
    if (mIsLeft) {
      if (CacheManager.hasCacheUser(objectId)) {
        mAvatarView.setImageURI(CacheManager.getCacheUser(objectId).toAvatarUri());
      }
    } else {
      mAvatarView.setImageURI(SupportUser.getCurrentUser().toAvatarUri());
    }

    mSentAtTextView.setText(Utils.millisecsToDateString(value.getTimestamp()));
    if (!mIsLeft) {
      setupStatus(value);
    }
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

  public void showTimeView(boolean isShow) {
    ViewUtils.setGone(mSentAtTextView, !isShow);
  }

  public void showDisplayName(boolean isShow) {
    ViewUtils.setGone(mDisplayNameTextView, !isShow);
  }

}

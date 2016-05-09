package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.cache.CacheManager;
import support.im.utilities.Utils;
import support.ui.adapters.EasyViewHolder;
import support.ui.utilities.ViewUtils;

public abstract class ChatsCommonViewHolder extends EasyViewHolder<AVIMMessage> {

  protected AVIMMessage mMessage;
  protected TextView mSentAtTextView;
  protected SimpleDraweeView mAvatarView;
  protected Context mContext;

  public ChatsCommonViewHolder(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
    mContext = context;
    mSentAtTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_sent_at);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_chats_item_avatar);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    mMessage = value;
    String objectId = value.getFrom();
    if (CacheManager.hasCacheUser(objectId)) {
      mAvatarView.setImageURI(CacheManager.getCacheUser(objectId).toAvatarUri());
    } else {
      mAvatarView.setImageURI(null);
    }
  }

  public void showTimeView(boolean isShow) {
    ViewUtils.setGone(mSentAtTextView, !isShow);
    if (isShow) {
      mSentAtTextView.setText(Utils.millisecsToDateString(mMessage.getTimestamp()));
    }
  }
}

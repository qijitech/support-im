package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.ui.utilities.ViewUtils;

public abstract class ChatsIncomingViewHolder extends ChatsCommonViewHolder {

  protected TextView mDisplayNameTextView;

  public ChatsIncomingViewHolder(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
    mDisplayNameTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_display_name);
  }

  public void showDisplayName(boolean isShow) {
    ViewUtils.setGone(mDisplayNameTextView, !isShow);
    if (isShow) {
      String objectId = mMessage.getFrom();
      User user = CacheManager.getCacheUser(objectId);
      mDisplayNameTextView.setText(user.getDisplayName());
    }
  }

}

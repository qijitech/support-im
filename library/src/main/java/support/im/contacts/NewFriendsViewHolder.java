package support.im.contacts;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.ui.adapters.EasyViewHolder;
import support.ui.utilities.ViewUtils;

public class NewFriendsViewHolder extends EasyViewHolder<NewFriends> {

  ImageView mAvatarView;
  TextView mTitleTextView;
  TextView mBadgeTextView;

  public NewFriendsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.contacts_item_new_friends);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_contacts_dummy_avatar);
    mTitleTextView = ButterKnife.findById(itemView, R.id.text_support_im_contacts_dummy_text);
    mBadgeTextView = ButterKnife.findById(itemView, R.id.text_si_contacts_new_friends_badge);
  }

  @Override public void bindTo(int position, NewFriends value) {
    mAvatarView.setImageResource(value.mDrawableRes);
    mTitleTextView.setText(value.mTextRes);
    if (value.unReadCount > 0) {
      ViewUtils.setGone(mBadgeTextView, false);
      mBadgeTextView.setText(String.valueOf(value.unReadCount));
    } else {
      ViewUtils.setGone(mBadgeTextView, true);
    }
  }
}
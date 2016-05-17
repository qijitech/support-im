package support.im.choose.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-17-0017.
 */
public class CheckedContactViewHolder extends EasyViewHolder<User> {
  private SimpleDraweeView mSupportUiCheckedAvatar;

  public CheckedContactViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_checked_contacts);
    ButterKnife.bind(this, itemView);
    mSupportUiCheckedAvatar = ButterKnife.findById(itemView, R.id.support_ui_checked_avatar);
  }

  @Override public void bindTo(int position, User value) {
    mSupportUiCheckedAvatar.setImageURI(value.toAvatarUri());
  }
}

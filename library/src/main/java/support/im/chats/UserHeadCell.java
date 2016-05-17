package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.User;
import support.im.utilities.FrescoDisplayUtils;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class UserHeadCell extends EasyViewHolder<User> {

  SimpleDraweeView mUserAvatar;
  TextView mUserName;

  public UserHeadCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_account_header);
    ButterKnife.bind(this, itemView);
    mUserAvatar = ButterKnife.findById(itemView, R.id.support_ui_sv_user_avatar);
    mUserName = ButterKnife.findById(itemView, R.id.support_ui_tv_user_name);
  }

  @Override public void bindTo(int position, User value) {
    // TODO: 2016-5-5-0005  
    FrescoDisplayUtils.showThumb(value.toAvatarUri(), mUserAvatar,
        (int) SupportApp.dimen(R.dimen.si_chats_item_avatar_size),
        (int) SupportApp.dimen(R.dimen.si_chats_item_avatar_size));
    mUserName.setText(value.getDisplayName());
  }
}

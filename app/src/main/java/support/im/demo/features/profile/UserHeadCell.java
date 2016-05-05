package support.im.demo.features.profile;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.demo.R;
import support.im.demo.data.User;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class UserHeadCell extends EasyViewHolder<User> {

  @Bind(R.id.sv_user_avatar) SimpleDraweeView mUserAvatar;
  @Bind(R.id.tv_user_name) TextView mUserName;

  public UserHeadCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_account_header);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, User value) {
    // TODO: 2016-5-5-0005  
  }
}

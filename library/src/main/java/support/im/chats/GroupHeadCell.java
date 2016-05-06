package support.im.chats;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import support.im.R;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;
import support.ui.utilities.AndroidUtilities;

/**
 * Created by wangh on 2016-5-6-0006.
 * todo://entry is NOT User
 */
public class GroupHeadCell extends EasyViewHolder<User> {

  LinearLayout mGroupAvatarContainer;
  TextView mMemberNumber;
  Context mContext;

  public GroupHeadCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_group_header);
    mContext = context;
    ButterKnife.bind(this, itemView);
    mGroupAvatarContainer = ButterKnife.findById(itemView, R.id.support_ui_avatar_container);
    mMemberNumber = ButterKnife.findById(itemView, R.id.support_ui_group_member_number);
  }

  @Override public void bindTo(int position, User value) {

  }

  private void initContainer(List<String> urls) {
    View view = null;
    SimpleDraweeView simpleDraweeView = null;
    if (urls.size() >= 6) {
      for (int i = 0; i <= 6; i++) {
        view = View.inflate(mContext, R.layout.view_avatar, null);
        simpleDraweeView = (SimpleDraweeView) view;
        if (i == 6) {
          ImageView imageView = new ImageView(mContext);
          imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
          imageView.setImageResource(R.drawable.icon_group_avatar_more);
          LinearLayout.LayoutParams params =
              new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                  ViewGroup.LayoutParams.WRAP_CONTENT);
          params.gravity = Gravity.BOTTOM;
          mGroupAvatarContainer.addView(imageView, params);
        } else {
          simpleDraweeView.setImageURI(Uri.parse(urls.get(i)));
          mGroupAvatarContainer.addView(view,
              new LinearLayout.LayoutParams(AndroidUtilities.dp(48), AndroidUtilities.dp(48)));
        }
      }
    } else {
      for (String url : urls) {
        view = View.inflate(mContext, R.layout.view_avatar, null);
        simpleDraweeView = (SimpleDraweeView) view;
        simpleDraweeView.setImageURI(Uri.parse(url));
        mGroupAvatarContainer.addView(view,
            new LinearLayout.LayoutParams(AndroidUtilities.dp(48), AndroidUtilities.dp(48)));
      }
    }
  }
}

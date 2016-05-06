package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-6-0006.
 */
public class GroupHeadCell extends EasyViewHolder<User> {
  public GroupHeadCell(Context context, ViewGroup parent, int layoutId) {
    super(context, parent, layoutId);
  }

  @Override public void bindTo(int position, User value) {

  }
}

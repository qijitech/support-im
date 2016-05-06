package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.RTextItem;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-6-0006.
 */
public class GroupNameCell extends EasyViewHolder<RTextItem> {
  TextView mGroupName;

  public GroupNameCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_group_name);
    ButterKnife.bind(this, itemView);
    mGroupName = ButterKnife.findById(itemView, R.id.support_ui_group_name);
  }

  @Override public void bindTo(int position, RTextItem value) {

  }
}

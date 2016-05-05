package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import support.im.demo.R;
import support.im.demo.features.profile.data.IconItem;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class IconItemCell extends EasyViewHolder<IconItem> {
  public IconItemCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_add_member);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, IconItem value) {

  }
}

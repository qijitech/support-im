package support.im.chats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.IconItem;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-5-0005.
 */
public class IconItemCell extends EasyViewHolder<IconItem> {
  public IconItemCell(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_add_member);
    ButterKnife.bind(this, itemView);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        de.greenrobot.event.EventBus.getDefault().post(new IconItem());
      }
    });
  }

  @Override public void bindTo(int position, IconItem value) {

  }
}

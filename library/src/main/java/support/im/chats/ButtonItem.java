package support.im.chats;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import support.im.R;
import support.im.data.CTextItem;
import support.im.events.CTextItemEvent;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-6-0006.
 */
public class ButtonItem extends EasyViewHolder<CTextItem> {

  TextView mBtnText;

  public ButtonItem(Context context, final ViewGroup parent) {
    super(context, parent, R.layout.item_button);
    ButterKnife.bind(this, itemView);
    mBtnText = ButterKnife.findById(itemView, R.id.support_ui_button_text);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        EventBus.getDefault().post(new CTextItemEvent(CTextItem.FLAG_EXIT_GROUP));
      }
    });
  }

  @Override public void bindTo(int position, CTextItem value) {

  }
}

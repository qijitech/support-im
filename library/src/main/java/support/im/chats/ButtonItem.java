package support.im.chats;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.CTextItem;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-6-0006.
 */
public class ButtonItem extends EasyViewHolder<CTextItem> {

  TextView mBtnText;

  public ButtonItem(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_button);
    ButterKnife.bind(this, itemView);
    mBtnText = ButterKnife.findById(itemView, R.id.support_ui_button_text);
  }

  @Override public void bindTo(int position, CTextItem value) {

  }
}

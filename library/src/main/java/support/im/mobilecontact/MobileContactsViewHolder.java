package support.im.mobilecontact;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.MobileContact;
import support.ui.adapters.EasyViewHolder;

public class MobileContactsViewHolder extends EasyViewHolder<MobileContact> {

  TextView mDisplayNameTextView;

  public MobileContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.mobile_contact_item);
    mDisplayNameTextView = ButterKnife.findById(itemView, R.id.text_mobile_contact_display_name);
  }

  @Override public void bindTo(int position, MobileContact value) {
    mDisplayNameTextView.setText(value.getName());
  }
}

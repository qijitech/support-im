package support.im.mobilecontact;

import android.content.Context;
import android.view.ViewGroup;
import support.im.R;
import support.im.data.MobileContact;
import support.ui.adapters.EasyViewHolder;

public class MobileContactsViewHolder extends EasyViewHolder<MobileContact> {

  public MobileContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.mobile_contact_item);
  }

  @Override public void bindTo(int position, MobileContact value) {

  }
}

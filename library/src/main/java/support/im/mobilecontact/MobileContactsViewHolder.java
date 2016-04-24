package support.im.mobilecontact;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import java.util.List;
import support.im.R;
import support.im.data.MobileContact;
import support.ui.adapters.EasyViewHolder;
import support.ui.utilities.ViewUtils;

public class MobileContactsViewHolder extends EasyViewHolder<MobileContact> {

  TextView mDisplayNameTextView;
  TextView mPhoneTextView;

  public MobileContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.mobile_contact_item);
    mDisplayNameTextView = ButterKnife.findById(itemView, R.id.text_mobile_contact_display_name);
    mPhoneTextView = ButterKnife.findById(itemView, R.id.text_mobile_contact_phone);
  }

  @Override public void bindTo(int position, MobileContact value) {
    mDisplayNameTextView.setText(value.getName());
    if (value.hasPhoneNumber()) {
      ViewUtils.setGone(mPhoneTextView, false);
      final List<MobileContact.PhoneNumber> phoneNumbers = value.getPhoneNumbers();
      StringBuilder sb = new StringBuilder();
      for (MobileContact.PhoneNumber phoneNumber: phoneNumbers) {
        sb.append(phoneNumber.getNumber()).append("\n");
      }
      sb.delete(sb.lastIndexOf("\n"), sb.length());
      mPhoneTextView.setText(sb.toString());
    } else {
      ViewUtils.setGone(mPhoneTextView, true);
    }
  }
}

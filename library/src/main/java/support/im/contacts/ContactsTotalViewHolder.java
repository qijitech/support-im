package support.im.contacts;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;

public class ContactsTotalViewHolder extends EasyViewHolder<ContactsTotal> {

  TextView mTotalTextView;

  public ContactsTotalViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.contacts_item_total);
    mTotalTextView = ButterKnife.findById(itemView, R.id.text_support_im_contacts_total);
  }

  @Override public void bindTo(int position, ContactsTotal value) {
    mTotalTextView.setText(SupportApp.getInstance().getString(R.string.support_im_contacts_total, value.mContactsTotal));
  }
}

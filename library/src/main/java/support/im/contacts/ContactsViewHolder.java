package support.im.contacts;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.SimpleUser;
import support.ui.adapters.EasyViewHolder;

public class ContactsViewHolder extends EasyViewHolder<SimpleUser> {

  SimpleDraweeView mAvatarView;
  TextView mNicknameTextView;
  SimpleUser mUser;

  public ContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.contacts_item);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_contacts_avatar);
    mNicknameTextView = ButterKnife.findById(itemView, R.id.text_support_im_contacts_nickname);
  }

  @Override public void bindTo(int position, SimpleUser value) {
    mUser = value;
    mAvatarView.setImageURI(value.toAvatarUri());
    mNicknameTextView.setText(value.getDisplayName());
  }
}

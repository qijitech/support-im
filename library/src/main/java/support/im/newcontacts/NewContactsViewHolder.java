package support.im.newcontacts;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.SupportUser;
import support.im.leanclound.contacts.AddRequest;
import support.ui.adapters.EasyViewHolder;

public class NewContactsViewHolder extends EasyViewHolder<AddRequest> {

  SimpleDraweeView mAvatarView;
  TextView mNicknameTextView;
  Button mActionBtn;

  public NewContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.new_contacts_item);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_new_contacts_avatar);
    mNicknameTextView = ButterKnife.findById(itemView, R.id.text_support_im_new_contacts_nickname);
    mActionBtn = ButterKnife.findById(itemView, R.id.btn_support_im_new_contacts_action);
  }

  @Override public void bindTo(int position, AddRequest value) {
    SupportUser supportUser = value.getFromUser();
    int status = value.getStatus();
    if (status == AddRequest.STATUS_WAIT) {
      mActionBtn.setText(R.string.support_im_add_request_agree);
      mActionBtn.setEnabled(true);
    } else if (status == AddRequest.STATUS_DONE) {
      mActionBtn.setText(R.string.support_im_add_request_agreed);
      mActionBtn.setEnabled(true);
    } else if (status == AddRequest.STATUS_REFUSED) {
      mActionBtn.setText(R.string.support_im_add_request_refused);
      mActionBtn.setEnabled(false);
    }
    mAvatarView.setImageURI(supportUser.toAvatarUri());
    mNicknameTextView.setText(supportUser.getNickname());
  }
}

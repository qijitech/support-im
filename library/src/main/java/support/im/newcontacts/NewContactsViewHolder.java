package support.im.newcontacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import de.greenrobot.event.EventBus;
import support.im.R;
import support.im.data.SupportUser;
import support.im.events.NewContactAgreeEvent;
import support.im.leanclound.contacts.AddRequest;
import support.ui.adapters.EasyViewHolder;

public class NewContactsViewHolder extends EasyViewHolder<AddRequest> implements
    View.OnClickListener {

  SimpleDraweeView mAvatarView;
  TextView mNicknameTextView;
  Button mActionBtn;
  private AddRequest mAddRequest;

  public NewContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.new_contacts_item);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_new_contacts_avatar);
    mNicknameTextView = ButterKnife.findById(itemView, R.id.text_support_im_new_contacts_nickname);
    mActionBtn = ButterKnife.findById(itemView, R.id.btn_support_im_new_contacts_action);
    mActionBtn.setOnClickListener(this);
  }

  @Override public void bindTo(int position, AddRequest value) {
    mAddRequest = value;
    SupportUser supportUser = value.getFromUser();
    int status = value.getStatus();
    if (status == AddRequest.STATUS_WAIT) {
      mActionBtn.setText(R.string.support_im_add_request_agree);
      mActionBtn.setEnabled(true);
    } else if (status == AddRequest.STATUS_DONE) {
      mActionBtn.setEnabled(false);
      mActionBtn.setText(R.string.support_im_add_request_agreed);
    } else if (status == AddRequest.STATUS_REFUSED) {
      mActionBtn.setText(R.string.support_im_add_request_refused);
      mActionBtn.setEnabled(false);
    }
    mAvatarView.setImageURI(supportUser.toAvatarUri());
    mNicknameTextView.setText(supportUser.getDisplayName());
  }

  @Override public void onClick(View v) {
    int status = mAddRequest.getStatus();
    if (status == AddRequest.STATUS_WAIT) {
      EventBus.getDefault().post(new NewContactAgreeEvent(mAddRequest));
    }
  }
}

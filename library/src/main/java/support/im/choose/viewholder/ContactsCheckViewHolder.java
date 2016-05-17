package support.im.choose.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import de.greenrobot.event.EventBus;
import support.im.R;
import support.im.choose.event.CheckEvent;
import support.im.data.User;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by wangh on 2016-5-16-0016.
 */
public class ContactsCheckViewHolder extends EasyViewHolder<User> {

  private CheckBox mCheckBox;
  private SimpleDraweeView mSimpleDraweeView;
  private TextView mTextView;

  public ContactsCheckViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.item_contacts_check);
    ButterKnife.bind(this, itemView);
    mCheckBox = ButterKnife.findById(itemView, R.id.support_ui_contacts_checkbox);
    mSimpleDraweeView = ButterKnife.findById(itemView, R.id.support_ui_contacts_avatar);
    mTextView = ButterKnife.findById(itemView, R.id.support_ui_contacts_username);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (mCheckBox.isChecked()) {
          mCheckBox.setChecked(false);
          EventBus.getDefault().post(new CheckEvent(false, getAdapterPosition()));
        } else {
          mCheckBox.setChecked(true);
          EventBus.getDefault().post(new CheckEvent(true, getAdapterPosition()));
        }
      }
    });
  }

  @Override public void bindTo(int position, User value) {
    mSimpleDraweeView.setImageURI(value.toAvatarUri());
    mTextView.setText(value.getDisplayName());
  }
}

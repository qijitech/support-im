package support.im.chats.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import support.im.R;

public class ChatsOutgoingLocationViewHolder extends ChatsOutgoingViewHolder {

  protected TextView mLocationTextView;

  public ChatsOutgoingLocationViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.chats_outgoing_item_location);
    mLocationTextView = ButterKnife.findById(itemView, R.id.text_support_im_chats_item_location);
  }

  @Override public void bindTo(int position, AVIMMessage value) {
    super.bindTo(position, value);
    if (value instanceof AVIMLocationMessage) {
      final AVIMLocationMessage locMsg = (AVIMLocationMessage) value;
      mLocationTextView.setText(locMsg.getText());
    }
  }
}

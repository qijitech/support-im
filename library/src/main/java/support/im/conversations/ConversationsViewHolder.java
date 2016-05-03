package support.im.conversations;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.R;
import support.im.data.ConversationModel;
import support.im.data.SimpleUser;
import support.im.utilities.ConversationHelper;
import support.im.utilities.Utils;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;

public class ConversationsViewHolder extends EasyViewHolder<ConversationModel> {

  SimpleDraweeView mAvatarImageView;
  //TextView mUnreadTextView;
  TextView mNameTextView;
  TextView mLatestTimeTextView;
  TextView mMessageTextView;

  public ConversationsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.conversations_item);
    mAvatarImageView = ButterKnife.findById(itemView, R.id.image_support_im_conversations_avatar);
    //mUnreadTextView = ButterKnife.findById(itemView, R.id.text_conversation_unread_count);
    mNameTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_name);
    mLatestTimeTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_latest_time);
    mMessageTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_message);
  }

  @Override public void bindTo(int position, ConversationModel value) {
    //mUnreadTextView.setText(String.valueOf(value.mUnreadCount));
    AVIMConversation conversation = value.getConversation();
    if (conversation == null) {
      return;
    }
    SimpleUser simpleUser = ConversationHelper.getSimpleUser(conversation);
    if (simpleUser != null) {
      mAvatarImageView.setImageURI(simpleUser.toAvatarUri());
    }
    mNameTextView.setText(ConversationHelper.displayNameOfConversation(conversation));
    if (value.mLastMessage != null) {
      mMessageTextView.setText(Utils.getMessageeShorthand(SupportApp.appContext(), value.mLastMessage));
    } else {
      mMessageTextView.setText("");
    }
    mLatestTimeTextView.setText(Utils.millisecsToDateString(value.getLastModifyTime()));
  }
}

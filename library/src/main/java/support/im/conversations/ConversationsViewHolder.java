package support.im.conversations;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.im.R;
import support.im.data.Conversation;
import support.ui.adapters.EasyViewHolder;

public class ConversationsViewHolder extends EasyViewHolder<Conversation> {

  ImageView mAvatarImageView;
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

  @Override public void bindTo(int position, Conversation value) {
    //mUnreadTextView.setText(String.valueOf(value.mUnreadCount));
    mNameTextView.setText(value.mLastMessage.getFrom());
    mMessageTextView.setText(value.mLastMessage.getContent());
  }
}

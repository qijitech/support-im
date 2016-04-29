package support.im.conversations;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import support.im.R;
import support.im.data.Conversation;
import support.im.utilities.ConversationHelper;
import support.im.utilities.Utils;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;

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
    AVIMConversation conversation = value.getConversation();
    mNameTextView.setText(ConversationHelper.displayNameOfConversation(conversation));
    conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
      @Override public void done(AVIMMessage avimMessage, AVIMException e) {
        if (avimMessage != null) {
          mLatestTimeTextView.setText(Utils.millisecsToDateString(avimMessage.getTimestamp()));
          mMessageTextView.setText(Utils.getMessageeShorthand(SupportApp.appContext(), avimMessage));
        } else {
          mLatestTimeTextView.setText("");
          mMessageTextView.setText("");
        }
      }
    });
    if (value.mLastMessage != null) {
      mMessageTextView.setText(Utils.getMessageeShorthand(SupportApp.appContext(), value.mLastMessage));
    }
    mLatestTimeTextView.setText(Utils.millisecsToDateString(value.getLastModifyTime()));
  }
}

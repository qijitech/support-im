package support.im.conversations;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.facebook.drawee.view.SimpleDraweeView;
import support.im.Injection;
import support.im.R;
import support.im.data.Conversation;
import support.im.data.ConversationType;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.im.data.source.UsersRepository;
import support.im.utilities.ConversationHelper;
import support.im.utilities.Utils;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;

public class ConversationsViewHolder extends EasyViewHolder<Conversation> {

  SimpleDraweeView mAvatarImageView;
  //TextView mUnreadTextView;
  TextView mNameTextView;
  TextView mLatestTimeTextView;
  TextView mMessageTextView;

  private UsersRepository mUsersRepository;

  public ConversationsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.conversations_item);
    mUsersRepository = Injection.provideUsersRepository(context);
    mAvatarImageView = ButterKnife.findById(itemView, R.id.image_support_im_conversations_avatar);
    //mUnreadTextView = ButterKnife.findById(itemView, R.id.text_conversation_unread_count);
    mNameTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_name);
    mLatestTimeTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_latest_time);
    mMessageTextView = ButterKnife.findById(itemView, R.id.text_support_im_conversations_message);
  }

  @Override public void bindTo(int position, Conversation value) {
    //mUnreadTextView.setText(String.valueOf(value.mUnreadCount));
    AVIMConversation conversation = value.getConversation();
    if (conversation == null) {
      return;
    }

    if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
      String userId = ConversationHelper.otherIdOfConversation(conversation);
      if (CacheManager.hasCacheUser(userId)) {
        final User user = CacheManager.getCacheUser(userId);
        mAvatarImageView.setImageURI(user.toAvatarUri());
        mNameTextView.setText(user.getDisplayName());
      } else {
        mAvatarImageView.setImageURI(null);
        mNameTextView.setText("对话");
      }
    } else { // group
    }

    if (value.getLatestMsg() != null) {
      mMessageTextView.setText(value.getLatestMsg());
    } else {
      mMessageTextView.setText("");
    }
    if (value.getLastMessage() != null) {
      mMessageTextView.setText(Utils.getMessageeShorthand(SupportApp.appContext(), value.getLastMessage()));
    }
    mLatestTimeTextView.setText(Utils.millisecsToDateString(value.getLatestMsgTime()));
  }
}

package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.Injection;
import support.im.data.ConversationType;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.location.Location;
import support.im.utilities.ConversationHelper;
import support.im.utilities.NotificationUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsFragment extends BaseChatsFragment implements ChatsContract.View {

  ChatsContract.Presenter mPresenter;
  protected String mCurrentClientId;
  protected String mUserObjectId;
  protected String mConversationId;

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initialize();
    new ChatsPresenter(mConversationId, mUserObjectId, Injection.provideChatsRepository(),
        Injection.provideConversationsRepository(mCurrentClientId),
        this);
  }

  private void initialize() {
    mCurrentClientId = ChatManager.getInstance().getClientId();
    Bundle extras = getArguments();
    if (extras == null) {
      return;
    }
    if (extras.containsKey(Constants.EXTRA_MEMBER_ID)) {
      mUserObjectId = extras.getString(Constants.EXTRA_MEMBER_ID);
      return;
    }
    // 来源conversations列表,直接读取Conversation
    if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
      mConversationId = extras.getString(Constants.EXTRA_CONVERSATION_ID);
    }
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mPresenter != null) {
      mPresenter.start();
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (null != mConversationId) {
      NotificationUtils.addTag(mConversationId);
    }
  }

  @Override public void onPause() {
    super.onPause();
    if (null != mConversationId) {
      NotificationUtils.removeTag(mConversationId);
    }
  }

  private void shouldShowDisplayName(boolean shouldShow) {
    mAdapter.shouldShowDisplayName(shouldShow);
  }

  @Override protected void onSendImage(String imageUri) {
    if (!TextUtils.isEmpty(imageUri)) {
      mPresenter.sendTextMessage("[img]" + imageUri);
    }
  }

  @Override protected void sendAudio(String audioPath) {
    if (!TextUtils.isEmpty(audioPath)) {
      mPresenter.sendAudioMessage(audioPath);
    }
  }

  @Override protected void sendImage(String imagePath) {
    mPresenter.sendImageMessage(imagePath);
  }

  @Override protected void sendLocation(Location location) {
    mPresenter.sendLocationMessage(location);
  }

  @Override protected void onSendBtnClick(String message) {
    mPresenter.sendTextMessage(message);
    //mPresenter.sendImageMessage("/storage/emulated/0/XhsEmoticonsKeyboard/Emoticons/wxemoticons/icon_002_cover.png");
  }

  ///////////////// ChatsContact View

  @Override public void updateUI(AVIMConversation avimConversation) {
    final String title = ConversationHelper.titleOfConversation(avimConversation);
    if (!TextUtils.isEmpty(title)) {
      getActivity().setTitle(title);
    }
    shouldShowDisplayName(ConversationHelper.typeOfConversation(avimConversation) == ConversationType.Group);
  }

  @Override public void setLoadingIndicator(boolean active) {
    mContentPresenter.displayLoadView();
  }

  @Override public void notifyItemInserted(AVIMMessage message) {
    mAdapter.add(message);
    scrollToBottom();
    mContentPresenter.displayContentView();
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
    mContentPresenter.displayContentView();
  }

  @Override public void showMessages(List<AVIMMessage> messages) {
    mAdapter.addAll(messages);
    mContentPresenter.displayContentView();
    scrollToBottom();
  }

  @Override public void appendMessages(List<AVIMMessage> messages) {
    mAdapter.add(messages);
  }

  @Override public void showNoMessages() {
    if (mAdapter.isEmpty()) {
      mContentPresenter.displayEmptyView();
    }
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void onDataNotAvailable(String error, AVException exception) {
    mAdapter.notifyDataSetChanged();
    if (mAdapter.isEmpty()) {
      mContentPresenter.displayEmptyView();
    }
  }

  @Override public void setPresenter(ChatsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}

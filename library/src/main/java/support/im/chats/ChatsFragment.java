package support.im.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import java.util.List;
import support.im.Injection;
import support.im.data.ConversationType;
import support.im.data.cache.CacheManager;
import support.im.leanclound.ChatManager;
import support.im.leanclound.Constants;
import support.im.utilities.ConversationHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsFragment extends BaseChatsFragment implements ChatsContract.View {

  ChatsContract.Presenter mPresenter;
  protected AVIMConversation mAVIMConversation;
  protected String mCurrentClientId;

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initialize();
  }

  private void initialize() {
    mCurrentClientId = ChatManager.getInstance().getClientId();
    Bundle extras = getArguments();
    if (extras == null) {
      return;
    }
    if (extras.containsKey(Constants.EXTRA_MEMBER_ID)) {
      return;
    }

    // 来源conversations列表,直接读取Conversation
    if (extras.containsKey(Constants.EXTRA_CONVERSATION_ID)) {
      String conversationId = extras.getString(Constants.EXTRA_CONVERSATION_ID);
      mAVIMConversation = CacheManager.getCacheConversation(conversationId);
      shouldShowDisplayName(ConversationHelper.typeOfConversation(mAVIMConversation) != ConversationType.Single);
      getActivity().setTitle(ConversationHelper.titleOfConversation(mAVIMConversation));
      new ChatsPresenter(Injection.provideChatsRepository(), this, mAVIMConversation);
    }
  }

  @Override public void onResume() {
    super.onResume();
    if (mPresenter != null) {
      mPresenter.start();
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

  @Override protected void onSendBtnClick(String message) {
    mPresenter.sendTextMessage(message);
  }

  ///////////////// ChatsContact View
  @Override public void setLoadingIndicator(boolean active) {
    mContentPresenter.displayLoadView();
  }

  @Override public void notifyItemInserted(AVIMTypedMessage message) {
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

  @Override public void showNoMessages() {
    mContentPresenter.displayEmptyView();
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void onDataNotAvailable(String error, AVException exception) {
    mContentPresenter.displayEmptyView();
  }

  @Override public void setPresenter(ChatsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}

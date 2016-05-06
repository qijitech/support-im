package support.im.chats;

import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import java.util.List;
import support.im.Injection;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsFragment extends BaseChatsFragment implements ChatsContract.View {

  ChatsContract.Presenter mPresenter;

  public static ChatsFragment create() {
    return new ChatsFragment();
  }

  public void setConversation(AVIMConversation avimConversation) {
    new ChatsPresenter(Injection.provideChatsRepository(), this, avimConversation);
    mPresenter.start();
  }

  public void shouldShowDisplayName(boolean shouldShow) {
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
  }

  @Override public void notifyItemInserted(AVIMTypedMessage message) {
    mAdapter.add(message);
    scrollToBottom();
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
  }

  @Override public void showMessages(List<AVIMMessage> messages) {
    mAdapter.addAll(messages);
    scrollToBottom();
  }

  @Override public void showNoMessages() {
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void onDataNotAvailable(String error, AVException exception) {
  }

  @Override public void setPresenter(ChatsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}

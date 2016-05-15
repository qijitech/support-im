package support.im.conversations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.Injection;
import support.im.R;
import support.im.SupportRecyclerViewFragment;
import support.im.chats.ChatsActivity;
import support.im.data.Conversation;
import support.im.leanclound.ChatManager;
import support.im.leanclound.event.ImTypeMessageEvent;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsFragment extends SupportRecyclerViewFragment implements ConversationsContract.View {

  private ConversationsContract.Presenter mPresenter;

  public static ConversationsFragment create() {
    return new ConversationsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter.bind(Conversation.class, ConversationsViewHolder.class);
    mAdapter.setOnClickListener(this);
    new ConversationsPresenter(Injection.provideConversationsRepository(ChatManager.getInstance().getClientId()),
        Injection.provideUsersRepository(getContext()),
        this);
  }

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.support_im_conversations_title);
    if (mPresenter != null) {
      mPresenter.start();
    }
  }

  @SuppressWarnings("unused") public void onEvent(ImTypeMessageEvent event) {
    Conversation conversation = event.mConversation;
    final List<Object> objects = mAdapter.getItems();
    for (Object object : objects) {
      Conversation conv = (Conversation) object;
      if (conv.getConversationId().equals(conversation.getConversationId())) {
        mAdapter.remove(conv);
        break;
      }
    }
    mAdapter.add(conversation, 0);
    contentPresenter.displayContentView();
  }


  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void notifyDataSetChanged(List<Conversation> conversations) {
    mAdapter.addAll(conversations);
    contentPresenter.displayContentView();
  }

  @Override public void notifyDataSetChanged() {
    mAdapter.notifyDataSetChanged();
  }

  @Override public void notifyItemChanged(Conversation conversation) {
    final int position = mAdapter.getIndex(conversation);
    mAdapter.update(conversation, position);
  }

  @Override public void showNoConversations() {
    contentPresenter.displayEmptyView();
  }

  @Override public void showError(String error, AVException e) {
    contentPresenter.displayErrorView();
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void setPresenter(ConversationsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void onItemClick(int position, View view) {
    Conversation conversation = (Conversation) mAdapter.get(position);
    ChatsActivity.startChatsWithConversationId(getContext(), conversation.getConversationId());
  }
}

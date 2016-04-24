package support.im.conversations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.List;
import support.im.Injection;
import support.im.R;
import support.im.data.Conversation;
import support.ui.SupportRecyclerViewFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsFragment extends SupportRecyclerViewFragment implements ConversationsContract.View {

  private ConversationsContract.Presenter mPresenter;

  public static ConversationsFragment create() {
    return new ConversationsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter.bind(Conversation.class, ConversationsViewHolder.class);
    new ConversationsPresenter(Injection.provideConversationsRepository(getContext()), this);
  }

  @Override public void onResume() {
    super.onResume();
    getActivity().setTitle(R.string.support_im_conversations_title);
    mPresenter.start();
  }

  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void showConversations(List<Conversation> conversations) {
    mAdapter.addAll(conversations);
    contentPresenter.displayContentView();
  }

  @Override public void showNoConversations() {
    contentPresenter.displayEmptyView();
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void setPresenter(ConversationsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}

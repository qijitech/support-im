package support.im.conversations;

import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.ConversationsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsPresenter implements ConversationsContract.Presenter {

  private final ConversationsContract.View mConversationsView;
  private final ConversationsRepository mConversationsRepository;

  private boolean mFirstLoad = true;

  public ConversationsPresenter(ConversationsRepository conversationsRepository,
      ConversationsContract.View conversationsView) {
    mConversationsView = checkNotNull(conversationsView);
    mConversationsRepository = checkNotNull(conversationsRepository);
    mConversationsView.setPresenter(this);
  }

  @Override public void start() {
    loadConversations(false);
  }

  @Override public void loadConversations(boolean forceUpdate) {
    loadConversations(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  private void loadConversations(boolean forceUpdate, final boolean showLoadingUI) {
    // The view may not be able to handle UI updates anymore
    if (!mConversationsView.isActive()) {
      return;
    }
    if (showLoadingUI) {
      mConversationsView.setLoadingIndicator(false);
    }

    mConversationsRepository.loadConversations(new ConversationsDataSource.LoadConversationCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        if (!mConversationsView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          mConversationsView.setLoadingIndicator(false);
        }
        processConversations(conversations);
      }

      @Override public void onDataNotAvailable() {

      }
    });
  }

  private void processConversations(List<Conversation> conversations) {
    if (conversations.isEmpty()) {
      // Show a message indicating there are no tasks for that filter type.
      processEmptyConversations();
    } else {
      // Show the list of tasks
      mConversationsView.showConversations(conversations);
    }
  }

  private void processEmptyConversations() {
    mConversationsView.showNoConversations();
  }
}

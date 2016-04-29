package support.im.conversations;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.ConversationsRepository;
import support.im.utilities.AVExceptionHandler;

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

    if (forceUpdate) {
      mConversationsRepository.refreshConversations();
    }

    mConversationsRepository.loadAVIMConversations(new ConversationsDataSource.LoadAVIMConversationsCallback() {
      @Override public void onAVIMConversationsLoaded(List<AVIMConversation> avimConversations) {
        if (avimConversations == null || avimConversations.size() <= 0) {
          if (mConversationsView.isActive()) {
            mConversationsView.showNoConversations();
          }
          return;
        }
        final int size = avimConversations.size();
        List<Conversation> conversations = Lists.newArrayListWithCapacity(size);
        for (int index = 0; index<size ;index++) {
          AVIMConversation avimConversation = avimConversations.get(index);
          final Conversation conversation = Conversation.createConversation(avimConversation);
          conversations.add(conversation);
        }
        if (mConversationsView.isActive()) {
          mConversationsView.showConversations(conversations);
        }
      }

      @Override public void onAVIMConversationsNotFound() {
        if (mConversationsView.isActive()) {
          mConversationsView.showNoConversations();
        }
      }

      @Override public void onDataNotAvailable(AVIMException e) {
        if (mConversationsView.isActive()) {
          mConversationsView.showError(AVExceptionHandler.getLocalizedMessage(e), e);
        }
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

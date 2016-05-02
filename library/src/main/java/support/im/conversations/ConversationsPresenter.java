package support.im.conversations;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.Collections;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.ConversationsRepository;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsPresenter implements ConversationsContract.Presenter {

  private final ConversationsContract.View mConversationsView;
  private final ConversationsRepository mConversationsRepository;
  private final ConversationsComparator mConversationsComparator;
  private boolean mFirstLoad = true;

  public ConversationsPresenter(ConversationsRepository conversationsRepository,
      ConversationsContract.View conversationsView) {
    mConversationsView = checkNotNull(conversationsView);
    mConversationsRepository = checkNotNull(conversationsRepository);

    mConversationsComparator = new ConversationsComparator();
    mConversationsView.setPresenter(this);
  }

  @Override public void start() {
    loadConversations(false);
  }

  @Override public void loadConversations(boolean forceUpdate) {
    loadServerConversations(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  private void loadServerConversations(boolean forceUpdate, final boolean showLoadingUI) {
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

    mConversationsRepository.loadServerConversations(new ConversationsDataSource.LoadConversationsCallback() {
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        // sorted
        Collections.sort(conversations, mConversationsComparator);

        if (!mConversationsView.isActive()) {
          return;
        }

        mConversationsView.notifyDataSetChanged(conversations);

        for (final Conversation conversation : conversations) {
          AVIMConversation aVIMConversation = conversation.getConversation();
          if (aVIMConversation != null) {
            mConversationsRepository.getLastMessage(aVIMConversation, new ConversationsDataSource.GetLastMessageCallback() {
              @Override public void onLastMessageLoaded(AVIMMessage avimMessage) {
                conversation.mLastMessage = avimMessage;
                if (mConversationsView.isActive()) {
                  mConversationsView.notifyItemChanged(conversation);
                }
              }
              @Override public void onLastMessageNotFound() {
              }
              @Override public void onDataNotAvailable(AVIMException e) {
              }
            });
          }
        }
      }

      @Override public void onConversationsNotFound() {
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

}

package support.im.conversations;

import com.avos.avoscloud.im.v2.AVIMException;
import java.util.List;
import support.im.data.Conv;
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
    loadConversations(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  private void loadConversations(boolean forceUpdate, final boolean showLoadingUI) {
    if (!mConversationsView.isActive()) {
      return;
    }

    if (showLoadingUI) {
      mConversationsView.setLoadingIndicator(false);
    }

    if (forceUpdate) {
      mConversationsRepository.refreshConversations();
    }

    mConversationsRepository.loadConversations(new ConversationsDataSource.LoadConversationsCallback() {
      @Override public void onConversationsLoaded(List<Conv> conversations) {
        processConversations(conversations);
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

  private void processConversations(List<Conv> conversations) {
    if (!mConversationsView.isActive()) {
      return;
    }
    mConversationsView.notifyDataSetChanged(conversations);
  }

}

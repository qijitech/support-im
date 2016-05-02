package support.im.chats;

import android.support.annotation.NonNull;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.data.source.ChatsDataSource;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsPresenter implements ChatsContract.Presenter {

  private final ChatsContract.View mChatsView;
  private final ChatsDataSource mChatsRepository;

  private boolean mFirstLoad = true;

  public ChatsPresenter(@NonNull ChatsDataSource chatsRepository,
      @NonNull ChatsContract.View chatsView) {
    mChatsRepository = checkNotNull(chatsRepository, "chatsRepository cannot be null");
    mChatsView = checkNotNull(chatsView, "chatsView cannot be null!");

    mChatsView.setPresenter(this);
  }

  @Override public void start() {
    fetchMessages(false);
  }

  @Override public void fetchMessages(boolean forceUpdate) {
    fetchMessages(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  private void fetchMessages(boolean forceUpdate, final boolean showLoadingUI) {
    if (showLoadingUI) {
      mChatsView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      mChatsRepository.refreshMessages();
    }

    mChatsRepository.loadMessages(new ChatsDataSource.LoadMessagesCallback() {
      @Override public void onMessagesLoaded(List<AVIMMessage> messages) {
        // The view may not be able to handle UI updates anymore
        if (!mChatsView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          mChatsView.setLoadingIndicator(false);
        }

        processMessage(messages);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        mChatsView.onDataNotAvailable(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
  }

  @Override public void fetchMessages(String messageId, long timestamp, int limit) {

  }

  private void processMessage(List<AVIMMessage> messages) {
    if (messages.isEmpty()) {
      // Show a message indicating there are no tasks for that filter type.
      mChatsView.showNoMessages();
    } else {
      // Show the list of messages
      mChatsView.showMessages(messages);
    }
  }
}

package support.im.chats;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import java.util.List;
import support.im.data.Conv;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.im.data.source.ChatsDataSource;
import support.im.data.source.ConversationsDataSource;
import support.im.leanclound.ChatManager;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.DatabaseUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsPresenter implements ChatsContract.Presenter {

  private final ChatsContract.View mChatsView;
  private final ChatsDataSource mChatsRepository;
  private final ConversationsDataSource mConversationsRepository;

  private boolean mFirstLoad = true;
  private final String mUserObjectId;
  private final String mConversationId;
  private AVIMConversation mAVIMConversation;

  public ChatsPresenter(
      String userObjectId,
      String conversationId,
      @NonNull ChatsDataSource chatsRepository,
      @NonNull ConversationsDataSource conversationsRepository,
      @NonNull ChatsContract.View chatsView) {
    mUserObjectId = userObjectId;
    mConversationId = conversationId;
    mChatsRepository = checkNotNull(chatsRepository, "chatsRepository cannot be null");
    mConversationsRepository = checkNotNull(conversationsRepository, "chatsRepository cannot be null");
    mChatsView = checkNotNull(chatsView, "chatsView cannot be null!");
    mChatsView.setPresenter(this);
  }

  @Override public void loadAVIMConversation() {
    if (!TextUtils.isEmpty(mConversationId)) {
      mAVIMConversation = CacheManager.getCacheConversation(mConversationId);
      if (mChatsView.isActive()) {
        mChatsView.updateAVIMConversation(mAVIMConversation);
        fetchMessages(false);
      }
    } else if (!TextUtils.isEmpty(mUserObjectId)){
      mConversationsRepository.loadConversation(mUserObjectId, new ConversationsDataSource.LoadConversationCallback() {
        @Override public void onConversationLoaded(Conv conv) {
          if (conv != null) {
            mAVIMConversation = CacheManager.getCacheConversation(conv.getConversationId());
            if (mChatsView.isActive()) {
              mChatsView.updateAVIMConversation(mAVIMConversation);
              fetchMessages(false);
            }
          }
        }
        @Override public void onConversationNotFound() {
        }
      });
    }
  }

  @Override public void start() {
    loadAVIMConversation();
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

    if (mAVIMConversation == null) {
      mChatsView.showNoMessages();
      return;
    }

    mChatsRepository.loadMessages(mAVIMConversation, new ChatsDataSource.LoadMessagesCallback() {
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

  @Override public void sendTextMessage(String content) {
    AVIMTextMessage message = new AVIMTextMessage();
    message.setText(content);
    sendMessage(message);
  }

  private void sendMessage(final AVIMTypedMessage message) {
    if (!mChatsView.isActive()) {
      return;
    }
    mChatsView.notifyItemInserted(message);

    if (mAVIMConversation == null) {
      // create
      User user = CacheManager.getCacheUser(mUserObjectId);
      ChatManager.getInstance().createSingleConversation(user, new AVIMConversationCreatedCallback() {
        @Override public void done(final AVIMConversation avimConversation, AVIMException e) {
          if (!AVExceptionHandler.handAVException(e, false)) {
            return;
          }
          mAVIMConversation = avimConversation;
          mChatsRepository.sendMessage(avimConversation, message, new ChatsDataSource.GetMessageCallback() {
            @Override public void onMessageLoaded(AVIMMessage message) {
              DatabaseUtils.saveConversation(avimConversation, message, ChatManager.getInstance().getClientId());
              if (!mChatsView.isActive()) {
                return;
              }
              mChatsView.notifyDataSetChanged();
            }

            @Override public void onDataNotAvailable(AVException exception) {
              if (!mChatsView.isActive()) {
                return;
              }
              mChatsView.onDataNotAvailable(AVExceptionHandler.getLocalizedMessage(exception), exception);
            }
          });
        }
      });
      return;
    }

    mChatsRepository.sendMessage(mAVIMConversation, message, new ChatsDataSource.GetMessageCallback() {
      @Override public void onMessageLoaded(AVIMMessage message) {
        if (!mChatsView.isActive()) {
          return;
        }
        mChatsView.notifyDataSetChanged();
      }

      @Override public void onDataNotAvailable(AVException exception) {
        if (!mChatsView.isActive()) {
          return;
        }
        mChatsView.onDataNotAvailable(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
  }
}

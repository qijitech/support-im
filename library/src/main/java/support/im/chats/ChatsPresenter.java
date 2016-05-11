package support.im.chats;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.SupportUser;
import support.im.data.User;
import support.im.data.cache.CacheManager;
import support.im.data.source.ChatsDataSource;
import support.im.data.source.ConversationsDataSource;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsPresenter implements ChatsContract.Presenter {

  private final ChatsContract.View mChatsView;
  private final ChatsDataSource mChatsRepository;
  private final ConversationsDataSource mConversationsRepository;

  private boolean mFirstLoad = true;
  private SupportUser mCurrentUser;
  private final String mUserObjectId;
  private final String mConversationId;
  private AVIMConversation mAVIMConversation;
  private Conversation mConversation;

  private ArrayList<AVIMMessage> mMessages = Lists.newArrayList();

  public ChatsPresenter(
      String userObjectId,
      String conversationId,
      @NonNull ChatsDataSource chatsRepository,
      @NonNull ConversationsDataSource conversationsRepository,
      @NonNull ChatsContract.View chatsView) {
    mCurrentUser = SupportUser.getCurrentUser();
    mUserObjectId = userObjectId;
    mConversationId = conversationId;
    mChatsRepository = checkNotNull(chatsRepository, "chatsRepository cannot be null");
    mConversationsRepository = checkNotNull(conversationsRepository, "chatsRepository cannot be null");
    mChatsView = checkNotNull(chatsView, "chatsView cannot be null!");
    mChatsView.setPresenter(this);
  }

  @Override public void loadAVIMConversation() {
    if (!TextUtils.isEmpty(mConversationId)) {
      mAVIMConversation = CacheManager.getCacheAVIMConversation(mConversationId);
      mConversation = CacheManager.getCacheConversation(mConversationId);
      if (mChatsView.isActive()) {
        mChatsView.updateUI(mAVIMConversation);
        fetchMessages(false);
      }
    } else if (!TextUtils.isEmpty(mUserObjectId)){
      mConversationsRepository.loadConversation(mUserObjectId, new ConversationsDataSource.LoadConversationCallback() {
        @Override public void onConversationLoaded(Conversation conversation) {
          if (conversation != null) {
            mConversation = conversation;
            CacheManager.cacheConversation(conversation);
            mAVIMConversation = CacheManager.getCacheAVIMConversation(conversation.getConversationId());
            if (mChatsView.isActive()) {
              mChatsView.updateUI(mAVIMConversation);
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
    AVIMMessage message = getFirstMessage();
    if (message == null) {
    }
  }

  private AVIMMessage getFirstMessage() {
    if (mMessages == null || mMessages.isEmpty()) {
      return null;
    }
    return mMessages.get(0);
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
    message.setFrom(mCurrentUser.getObjectId());
    message.setText(content);
    sendMessage(message);
  }

  @Override public void sendImageMessage(String imagePath) {
    try {
      AVIMImageMessage imageMessage = new AVIMImageMessage(imagePath);
      imageMessage.setFrom(mCurrentUser.getObjectId());
      sendMessage(imageMessage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendMessage(final AVIMMessage message) {
    if (!mChatsView.isActive()) {
      return;
    }
    //mChatsView.addMessage(message);
    //mChatsView.notifyItemInserted(message);

    if (mAVIMConversation == null) {
      User toUser = CacheManager.getCacheUser(mUserObjectId);
      mChatsRepository.createConversation(toUser, new AVIMConversationCreatedCallback() {
        @Override public void done(AVIMConversation avimConversation, AVIMException e) {
          if (AVExceptionHandler.handAVException(e, false)) {
            mAVIMConversation = avimConversation;
            sendMessage(message);
            mConversationsRepository.saveConversation(mAVIMConversation, message);
          }
        }
      });
      return;
    }

    mChatsRepository.sendMessage(mAVIMConversation, message, new ChatsDataSource.GetMessageCallback() {
      @Override public void onMessageLoaded(AVIMMessage message) {
        if (!mChatsView.isActive()) {
          return;
        }
        mConversationsRepository.saveConversation(mAVIMConversation, message);
        mChatsView.notifyItemInserted(message);
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

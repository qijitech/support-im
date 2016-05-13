package support.im.chats;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
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
import support.im.leanclound.ChatManager;
import support.im.location.Location;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.DatabaseUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChatsPresenter implements ChatsContract.Presenter {

  private final ChatsContract.View mChatsView;
  private final ChatsDataSource mChatsRepository;
  private final ConversationsDataSource mConversationsRepository;

  private boolean mFirstLoad = true;
  private SupportUser mCurrentUser;
  private AVIMConversation mAVIMConversation;
  private Conversation mConversation;

  private String mConversationId;
  private String mUserObjectId;

  private ArrayList<AVIMMessage> mMessages = Lists.newArrayList();

  public ChatsPresenter(
      String conversationId,
      String userObjectId,
      @NonNull ChatsDataSource chatsRepository,
      @NonNull ConversationsDataSource conversationsRepository,
      @NonNull ChatsContract.View chatsView) {
    mConversationId = conversationId;
    mUserObjectId = userObjectId;
    mCurrentUser = SupportUser.getCurrentUser();
    mChatsRepository = checkNotNull(chatsRepository, "chatsRepository cannot be null");
    mConversationsRepository = checkNotNull(conversationsRepository, "chatsRepository cannot be null");
    mChatsView = checkNotNull(chatsView, "chatsView cannot be null!");
    mChatsView.setPresenter(this);
  }

  @Override public void getConversation(String userObjectId) {
    final User user = CacheManager.getCacheUser(userObjectId);
    mChatsRepository.createConversation(user, new AVIMConversationCreatedCallback() {
      @Override public void done(AVIMConversation avimConversation, AVIMException e) {
        if (AVExceptionHandler.handAVException(e, false)) {
          mAVIMConversation = avimConversation;
          mConversation = DatabaseUtils.createConversation(avimConversation, user, ChatManager.getInstance().getClientId());
          CacheManager.cacheConversation(mConversation);
          CacheManager.cacheAVIMConversation(avimConversation);
          if (mChatsView.isActive()) {
            mChatsView.updateUI(avimConversation);
          }
          fetchMessages(false);
        }
      }
    });
  }

  @Override public void getAVIMConversation(String conversationId) {
    if (!TextUtils.isEmpty(conversationId)) {
      mAVIMConversation = CacheManager.getCacheAVIMConversation(conversationId);
      mConversation = CacheManager.getCacheConversation(conversationId);
      if (mChatsView.isActive()) {
        mChatsView.updateUI(mAVIMConversation);
      }
      fetchMessages(false);
    }
  }

  @Override public void start() {
    if (mConversationId != null) {
      getAVIMConversation(mConversationId);
    } else {
      getConversation(mUserObjectId);
    }
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

    if (mConversation == null) {
      mChatsView.showNoMessages();
      return;
    }

    String messageId = mConversation.getLatestMsgId();
    mChatsRepository.loadMessages(mAVIMConversation, messageId, 0, 20, new ChatsDataSource.LoadMessagesCallback() {
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

  @Override public void loadMoreMessages() {
    AVIMMessage message = getFirstMessage();
    String messageId;
    long timestamp;
    if (message == null) { // 从conversation种获取latest
      messageId = mConversation.getLatestMsgId();
      timestamp = mConversation.getLatestMsgTime();
    } else {
      messageId = message.getMessageId();
      timestamp = message.getTimestamp();
    }
    mChatsRepository.loadMessages(mAVIMConversation, messageId, timestamp, 20, new ChatsDataSource.LoadMessagesCallback() {
      @Override public void onMessagesLoaded(List<AVIMMessage> messages) {
        if (!mChatsView.isActive()) {
          return;
        }
        // Show the list of messages
        mChatsView.appendMessages(messages);
      }
      @Override public void onDataNotAvailable(AVException exception) {
        mChatsView.onDataNotAvailable(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
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

  @Override public void sendLocationMessage(Location location) {
    AVIMLocationMessage locationMessage = new AVIMLocationMessage();
    locationMessage.setLocation(new AVGeoPoint(location.latitude, location.longitude));
    locationMessage.setText(location.snippet);
    sendMessage(locationMessage);
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
    mChatsView.notifyItemInserted(message);

    mChatsRepository.sendMessage(mAVIMConversation, message, new ChatsDataSource.GetMessageCallback() {
      @Override public void onMessageLoaded(AVIMMessage message) {
        if (!mChatsView.isActive()) {
          return;
        }
        mConversationsRepository.saveConversation(mAVIMConversation, message);
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

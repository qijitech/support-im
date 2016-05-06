package support.im.conversations;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.google.common.collect.Lists;
import java.util.List;
import support.im.data.Conversation;
import support.im.data.ConversationType;
import support.im.data.User;
import support.im.data.source.ConversationsDataSource;
import support.im.data.source.ConversationsRepository;
import support.im.data.source.UsersDataSource;
import support.im.data.source.UsersRepository;
import support.im.utilities.AVExceptionHandler;
import support.im.utilities.ConversationHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConversationsPresenter implements ConversationsContract.Presenter {

  private final ConversationsContract.View mConversationsView;
  private final ConversationsRepository mConversationsRepository;
  private final UsersRepository mUsersRepository;
  private boolean mFirstLoad = true;

  public ConversationsPresenter(ConversationsRepository conversationsRepository,
      UsersRepository usersRepository,
      ConversationsContract.View conversationsView) {
    mConversationsView = checkNotNull(conversationsView);
    mConversationsRepository = checkNotNull(conversationsRepository);
    mUsersRepository = checkNotNull(usersRepository);

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
      @Override public void onConversationsLoaded(List<Conversation> conversations) {
        if (mConversationsView.isActive()) {
          mConversationsView.notifyDataSetChanged(conversations);
        }
        updateLastMessage(conversations);
        cacheRelatedUsers(conversations);
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

  private void updateLastMessage(List<Conversation> conversations) {
    if (conversations != null) {
      for (final Conversation conv : conversations) {
        AVIMConversation conversation = conv.getConversation();
        mConversationsRepository.getLastMessage(conversation, new ConversationsDataSource.GetLastMessageCallback() {
          @Override public void onLastMessageLoaded(AVIMMessage avimMessage) {
            conv.setLastMessage(avimMessage);
            if (mConversationsView.isActive()) {
              mConversationsView.notifyItemChanged(conv);
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

  private void cacheRelatedUsers(List<Conversation> conversations) {
    List<String> needCacheUsers = Lists.newArrayList();
    if (conversations != null) {
      for (Conversation conv : conversations) {
        AVIMConversation conversation = conv.getConversation();
        if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
          needCacheUsers.add(ConversationHelper.otherIdOfConversation(conversation));
        }
      }
    }
    mUsersRepository.fetchUsers(needCacheUsers, new UsersDataSource.LoadUsersCallback() {
      @Override public void onUserLoaded(List<User> users) {
        if (mConversationsView.isActive()) {
          mConversationsView.notifyDataSetChanged();
        }
      }
      @Override public void onUserNotFound() {
      }
      @Override public void onDataNotAvailable(AVException exception) {
      }
    });
  }

}

package support.im.conversations;

import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.Conversation;

public interface ConversationsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showConversations(List<Conversation> conversations);

    void updateConversation(Conversation conversation, int position);

    void showNoConversations();

    void showError(String error, AVException e);

    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void loadConversations(boolean forceUpdate);
  }
}

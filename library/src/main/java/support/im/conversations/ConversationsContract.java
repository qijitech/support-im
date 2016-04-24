package support.im.conversations;

import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.Conversation;

public interface ConversationsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showConversations(List<Conversation> conversations);

    void showNoConversations();

    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void loadConversations(boolean forceUpdate);
  }
}

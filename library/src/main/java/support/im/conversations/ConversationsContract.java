package support.im.conversations;

import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.Conv;

public interface ConversationsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void notifyDataSetChanged(List<Conv> conversations);

    void notifyItemChanged(Conv conversation);

    void showNoConversations();

    void showError(String error, AVException e);

    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void loadConversations(boolean forceUpdate);
  }
}

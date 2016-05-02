package support.im.chats;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.SimpleUser;

public interface ChatsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showMessages(List<AVIMMessage> messages);

    void showNoMessages();

    boolean isActive();

    void onDataNotAvailable(String error, AVException exception);
  }

  interface Presenter extends BasePresenter {
    void fetchMessages(boolean forceUpdate);

    void fetchMessages(final String messageId, final long timestamp, final int limit);
  }
}

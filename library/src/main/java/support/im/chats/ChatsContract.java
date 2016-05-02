package support.im.chats;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;

public interface ChatsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void notifyItemInserted(AVIMTypedMessage message);
    void notifyDataSetChanged();

    void showMessages(List<AVIMMessage> messages);

    void showNoMessages();
    boolean isActive();

    void onDataNotAvailable(String error, AVException exception);
  }

  interface Presenter extends BasePresenter {
    void sendTextMessage(String content);

    void fetchMessages(boolean forceUpdate);

    void fetchMessages(final String messageId, final long timestamp, final int limit);
  }
}

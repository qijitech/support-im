package support.im.chats;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.location.Location;

public interface ChatsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    // send message
    void notifyItemInserted(AVIMMessage message);

    // 网络操作完成后
    void notifyDataSetChanged();

    // 第一次获取消息
    void showMessages(List<AVIMMessage> messages);

    // 获取历史消息
    void appendMessages(List<AVIMMessage> messages);

    // 没有消息
    void showNoMessages();

    // 更新UI
    void updateUI(AVIMConversation avimConversation);

    boolean isActive();

    void onDataNotAvailable(String error, AVException exception);
  }

  interface Presenter extends BasePresenter {
    void getAVIMConversation(String conversationId);

    void getConversation(final String userObjectId);

    void sendLocationMessage(Location location);

    void sendImageMessage(String imagePath);

    void sendTextMessage(String content);

    void sendAudioMessage(String audioPath);

    void fetchMessages(boolean forceUpdate);

    void loadMoreMessages();

    String getConversationId();
  }
}

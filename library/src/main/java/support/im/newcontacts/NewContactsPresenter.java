package support.im.newcontacts;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import support.im.R;
import support.im.data.SupportUser;
import support.im.data.source.AddRequestsDataSource;
import support.im.data.source.AddRequestsRepository;
import support.im.leanclound.ChatManager;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.AddRequestManager;
import support.im.utilities.AVExceptionHandler;
import support.ui.app.SupportApp;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewContactsPresenter implements NewContactsContract.Presenter {

  private final AddRequestsRepository mAddRequestsRepository;

  private final NewContactsContract.View mNewContactView;

  public NewContactsPresenter(AddRequestsRepository addRequestsRepository,
      NewContactsContract.View newContactView) {
    mAddRequestsRepository = checkNotNull(addRequestsRepository);
    mNewContactView = checkNotNull(newContactView);
    mNewContactView.setPresenter(this);
  }

  @Override public void start() {
    loadAddRequests(false, 0, 30);
  }

  @Override public void loadAddRequests(boolean isRefresh, int skip, int limit) {
    mNewContactView.setLoadingIndicator(mNewContactView.isActive());
    mAddRequestsRepository.loadAddRequests(skip, limit, new AddRequestsDataSource.LoadAddRequestsCallback() {
      @Override public void onAddRequestsLoaded(List<AddRequest> addRequests) {
        AddRequestManager.getInstance().markAddRequestsRead(addRequests);
        List<AddRequest> addRequestsToShow = Lists.newArrayList();
        for (AddRequest addRequest : addRequests) {
          if (addRequest.getFromUser() != null) {
            addRequestsToShow.add(addRequest);
          }
        }
        if (!mNewContactView.isActive()) {
          return;
        }
        mNewContactView.showAddRequests(addRequestsToShow);
      }

      @Override public void onAddRequestsNotFound() {
        mNewContactView.showNoAddRequests();
      }

      @Override public void onDataNotAvailable(AVException exception) {
        mNewContactView.displayError(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
  }

  @Override public void agreeAddRequest(AddRequest addRequest) {
    if (mNewContactView.isActive()) {
      mNewContactView.showHud();
    }
    mAddRequestsRepository.agreeAddRequest(addRequest, new AddRequestsDataSource.AddRequestCallback() {
      @Override public void onAddRequestLoaded(AddRequest addRequest) {
        if (!mNewContactView.isActive()) {
          return;
        }
        SupportUser fromUser = addRequest.getFromUser();
        if (fromUser != null) {
          ChatManager.getInstance().createSingleConversation(fromUser, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
              if (e == null) {
                AVIMTextMessage message = new AVIMTextMessage();
                Map<String, Object> attributes = Maps.newLinkedHashMap();
                message.setText(SupportApp.getInstance().getString(R.string.support_im_when_agree_request));
                avimConversation.sendMessage(message, null);
              }
            }
          });
        }
        mNewContactView.dismissHud();
        addRequest.setStatus(AddRequest.STATUS_DONE);
        mNewContactView.showAddRequestAgreed(addRequest);
      }

      @Override public void onDataNotAvailable(AVException exception) {
        mNewContactView.dismissHud();
        mNewContactView.showError(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
  }

  @Override public void deleteAddRequest(AddRequest addRequest) {

  }

  @Override public void sendAgreeRequestMessage(String toUserId) {

  }

}

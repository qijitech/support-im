package support.im.newcontacts;

import android.support.v4.app.Fragment;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.data.source.AddRequestsDataSource;
import support.im.data.source.AddRequestsRepository;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.AddRequestManager;

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
        if (!mNewContactView.isActive()) {
          return;
        }
        mNewContactView.showAddRequests(addRequests);
      }

      @Override public void onAddRequestsNotFound() {

      }

      @Override public void onDataNotAvailable(AVException exception) {

      }
    });
  }

  @Override public void agreeAddRequest(AddRequest addRequest) {

  }

  @Override public void deleteAddRequest(AddRequest addRequest) {

  }

  @Override public void sendAgreeRequestMessage(String toUserId) {

  }

}

package support.im.newcontacts;

import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.leanclound.contacts.AddRequest;

public interface NewContactsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showHud(boolean active);

    void dismissHud();

    void showAddRequests(List<AddRequest> addRequests);

    void showNoAddRequests();

    void displayError(String error, AVException exception);

    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void loadAddRequests(final boolean isRefresh, int skip, int limit);

    void agreeAddRequest(AddRequest addRequest);

    void deleteAddRequest(AddRequest addRequest);

    void sendAgreeRequestMessage(String toUserId);
  }
}

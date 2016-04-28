package support.im.contacts;

import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.SupportUser;

public interface ContactsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);

    void showContacts(List<SupportUser> contacts);

    void showNotLoggedIn();

    void showNoContacts();

    boolean isActive();

    void onDataNotAvailable(AVException exception);
  }

  interface Presenter extends BasePresenter {
    void loadContacts(boolean forceUpdate);
  }
}

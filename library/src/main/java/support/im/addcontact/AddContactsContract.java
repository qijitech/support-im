package support.im.addcontact;

import com.avos.avoscloud.AVException;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.User;

public interface AddContactsContract {

  interface View extends BaseView<Presenter> {
    void showHud(boolean active);

    void dismissHud();

    void showUser(User user);

    void showNoUser();

    void displayError(String error, AVException exception);

    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void searchContact(String searchKey);
  }
}

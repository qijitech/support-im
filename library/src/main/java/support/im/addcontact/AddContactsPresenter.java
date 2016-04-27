package support.im.addcontact;

import com.avos.avoscloud.AVException;
import support.im.data.SupportUser;
import support.im.data.source.UsersDataSource;
import support.im.data.source.UsersRepository;
import support.im.utilities.AVExceptionHandler;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddContactsPresenter implements AddContactsContract.Presenter {

  private final UsersRepository mUsersRepository;

  private final AddContactsContract.View mAddContactView;

  public AddContactsPresenter(UsersRepository usersRepository,
      AddContactsContract.View addContactView) {
    mUsersRepository = checkNotNull(usersRepository);
    mAddContactView = checkNotNull(addContactView);
    mAddContactView.setPresenter(this);
  }

  @Override public void searchContact(String searchKey) {
    mAddContactView.showHud(true);
    mUsersRepository.searchUser(searchKey, new UsersDataSource.GetUserCallback() {
      @Override public void onUserLoaded(SupportUser user) {
        // The view may not be able to handle UI updates anymore
        if (!mAddContactView.isActive()) {
          return;
        }
        mAddContactView.dismissHud();
        mAddContactView.showUser(user);
      }
      @Override public void onUserNotFound() {
        if (!mAddContactView.isActive()) {
          return;
        }
        mAddContactView.dismissHud();
        mAddContactView.showNoUser();
      }
      @Override public void onDataNotAvailable(AVException exception) {
        if (!mAddContactView.isActive()) {
          return;
        }
        mAddContactView.dismissHud();
        mAddContactView.displayError(AVExceptionHandler.getLocalizedMessage(exception), exception);
      }
    });
  }

  @Override public void start() {
  }
}

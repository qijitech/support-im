package support.im.mobilecontact;

import java.util.List;
import support.im.BasePresenter;
import support.im.BaseView;
import support.im.data.MobileContact;

public interface MobileContactsContract {

  interface View extends BaseView<Presenter> {
    void setLoadingIndicator(boolean active);
    void showMobileContacts(List<MobileContact> mobileContacts);
    void showNoMobileContacts();
    boolean isActive();
  }

  interface Presenter extends BasePresenter {
    void loadMobileContacts(boolean forceUpdate);
  }
}

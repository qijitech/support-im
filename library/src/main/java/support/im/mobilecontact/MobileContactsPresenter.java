package support.im.mobilecontact;

import java.util.ArrayList;
import java.util.List;
import support.im.data.MobileContact;
import support.im.data.source.MobileContactsDataSource;
import support.im.data.source.MobileContactsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsPresenter implements MobileContactsContract.Presenter {

  private final MobileContactsRepository mMobileContactsRepository;
  private final MobileContactsContract.View mMobileContactsView;

  private boolean mFirstLoad = true;

  public MobileContactsPresenter(MobileContactsRepository mobileContactsRepository,
      MobileContactsContract.View mobileContactsView) {
    mMobileContactsRepository = checkNotNull(mobileContactsRepository);
    mMobileContactsView = checkNotNull(mobileContactsView);
    mobileContactsView.setPresenter(this);
  }

  @Override public void start() {
    loadMobileContacts(false);
  }

  @Override public void loadMobileContacts(boolean forceUpdate) {
    // Simplification for sample: a network reload will be forced on first load.
    loadMobileContacts(forceUpdate || mFirstLoad, true);
    mFirstLoad = false;
  }

  /**
   * @param forceUpdate   Pass in true to refresh the data in the {@link MobileContactsDataSource}
   * @param showLoadingUI Pass in true to display a loading icon in the UI
   */
  private void loadMobileContacts(boolean forceUpdate, final boolean showLoadingUI) {
    if (showLoadingUI) {
      mMobileContactsView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      mMobileContactsRepository.refreshMobileContacts();
    }

    mMobileContactsRepository.getMobileContacts(new MobileContactsDataSource.LoadMobileContactsCallback() {
      @Override public void onMobileContactsLoaded(List<MobileContact> tasks) {
        List<MobileContact> mobileContactToShow = new ArrayList<>();
        mobileContactToShow.addAll(tasks);
        // The view may not be able to handle UI updates anymore
        if (!mMobileContactsView.isActive()) {
          return;
        }
        if (showLoadingUI) {
          mMobileContactsView.setLoadingIndicator(false);
        }
        processMobileContacts(mobileContactToShow);
      }
    });
  }

  private void processMobileContacts(List<MobileContact> mobileContacts) {
    if (mobileContacts.isEmpty()) {
      // Show a message indicating there are no tasks for that filter type.
      processEmptyTasks();
    } else {
      // Show the list of tasks
      mMobileContactsView.showMobileContacts(mobileContacts);
    }
  }

  private void processEmptyTasks() {
    mMobileContactsView.showNoMobileContacts();
  }
}

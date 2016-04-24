package support.im.mobilecontact;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.List;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import support.im.data.MobileContact;
import support.ui.SupportRecyclerViewFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsFragment extends SupportRecyclerViewFragment
    implements MobileContactsContract.View {

  private MobileContactsContract.Presenter mPresenter;

  public static MobileContactsFragment create() {
    return new MobileContactsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter.bind(MobileContact.class, MobileContactsViewHolder.class);
  }

  @Override public void onResume() {
    super.onResume();
    loadContacts();
  }

  @Override public void setPresenter(MobileContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void setLoadingIndicator(boolean active) {
    if (getView() == null) {
      return;
    }
    contentPresenter.displayLoadView();
  }

  @Override public void showMobileContacts(List<MobileContact> mobileContacts) {
    if (getView() == null) {
      return;
    }
    mAdapter.addAll(mobileContacts);
    contentPresenter.displayContentView();
  }

  @Override public void showNoMobileContacts() {
    if (getView() == null) {
      return;
    }
    contentPresenter.displayEmptyView();
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  public void loadContacts() {
    if (Nammu.checkPermission(Manifest.permission.READ_CONTACTS)) {
      mPresenter.start();
    } else {
      Nammu.askForPermission(getActivity(), Manifest.permission.READ_CONTACTS, new PermissionCallback() {
        @Override public void permissionGranted() {
          mPresenter.start();
        }

        @Override public void permissionRefused() {
          getActivity().finish();
        }
      });
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}

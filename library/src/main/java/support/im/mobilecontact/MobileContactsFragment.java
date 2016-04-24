package support.im.mobilecontact;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import java.util.List;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import support.im.data.MobileContact;
import support.ui.SupportRecyclerViewFragment;
import support.ui.adapters.EasyRecyclerAdapter;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsFragment extends SupportRecyclerViewFragment
    implements MobileContactsContract.View {

  private static final int RC_CONTACTS_PERM = 1000;

  private MobileContactsContract.Presenter mPresenter;

  private EasyRecyclerAdapter mAdapter;

  public static MobileContactsFragment create() {
    return new MobileContactsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.bind(MobileContact.class, MobileContactsViewHolder.class);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onResume() {
    super.onResume();
    loadContacts();
  }

  @Override public void setPresenter(MobileContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void setLoadingIndicator(boolean active) {

  }

  @Override public void showMobileContacts(List<MobileContact> tasks) {

  }

  @Override public void showNoMobileContacts() {

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

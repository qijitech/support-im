package support.im.mobilecontact;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.util.List;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import support.im.R;
import support.im.data.MobileContact;
import support.ui.SupportRecyclerViewFragment;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.widget.SideBar;

import static com.google.common.base.Preconditions.checkNotNull;

public class MobileContactsFragment extends SupportRecyclerViewFragment
    implements MobileContactsContract.View {

  private MobileContactsContract.Presenter mPresenter;

  private FrameLayout mContentView;
  private SideBar mSideBar;
  private TextView mUserDialog;

  protected MobileContactsAdapter mMobileContactsAdapter;

  public static MobileContactsFragment create() {
    return new MobileContactsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.mobile_contacts;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMobileContactsAdapter = new MobileContactsAdapter(getContext());
    mMobileContactsAdapter.bind(MobileContact.class, MobileContactsViewHolder.class);
  }

  protected void setAdapter() {
    mRecyclerView.setAdapter(mMobileContactsAdapter);
  }

  @Override protected View getAttachContentView() {
    return mContentView;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mContentView = ButterKnife.findById(view, R.id.support_ui_content_view);
    mSideBar = ButterKnife.findById(view, R.id.contact_sidebar);
    mUserDialog = ButterKnife.findById(view, R.id.contact_dialog);
    mSideBar.setTextView(mUserDialog);

    mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

      @Override
      public void onTouchingLetterChanged(String s) {
        int position = mMobileContactsAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          mRecyclerView.scrollToPosition(position);
        }
      }
    });

    final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mMobileContactsAdapter);
    mRecyclerView.addItemDecoration(headersDecor);
    mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        headersDecor.invalidateHeaders();
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    loadContacts();
  }

  @Override public void setPresenter(MobileContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void showMobileContacts(List<MobileContact> mobileContacts) {
    mMobileContactsAdapter.addAll(mobileContacts);
    contentPresenter.displayContentView();
  }

  @Override public void showNoMobileContacts() {
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

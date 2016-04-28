package support.im.newcontacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.avos.avoscloud.AVException;
import java.util.List;
import support.im.leanclound.contacts.AddRequest;
import support.ui.SupportRecyclerViewFragment;

import static com.google.common.base.Preconditions.checkNotNull;

public class NewContactsFragment extends SupportRecyclerViewFragment implements NewContactsContract.View {

  NewContactsContract.Presenter mPresenter;

  public static NewContactsFragment create() {
    return new NewContactsFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter.bind(AddRequest.class, NewContactsViewHolder.class);
  }

  @Override public void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void showHud(boolean active) {

  }

  @Override public void dismissHud() {

  }

  @Override public void showAddRequests(List<AddRequest> addRequests) {
    mAdapter.addAll(addRequests);
    contentPresenter.displayContentView();
  }

  @Override public void showNoAddRequests() {
    contentPresenter.displayEmptyView();
  }

  @Override public void displayError(String error, AVException exception) {
    contentPresenter.displayErrorView();
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void setPresenter(NewContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }
}

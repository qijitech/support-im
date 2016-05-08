package support.im.newcontacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.avos.avoscloud.AVException;
import de.greenrobot.event.EventBus;
import java.util.List;
import support.im.events.InvitationEvent;
import support.im.events.NewContactAgreeEvent;
import support.im.leanclound.contacts.AddRequest;
import support.im.leanclound.contacts.AddRequestManager;
import support.im.SupportRecyclerViewFragment;
import support.im.utilities.HudUtils;
import support.ui.utilities.ToastUtils;

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
    EventBus.getDefault().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  @SuppressWarnings("unused") public void onEvent(InvitationEvent event) {
    AddRequestManager.getInstance().unreadRequestsIncrement();
    mPresenter.start();
  }

  @SuppressWarnings("unused") public void onEvent(NewContactAgreeEvent event) {
    mPresenter.agreeAddRequest(event.mAddRequest);
  }

  @Override public void setLoadingIndicator(boolean active) {
    contentPresenter.displayLoadView();
  }

  @Override public void showHud() {
    HudUtils.showHud(getContext());
  }

  @Override public void dismissHud() {
    HudUtils.dismissHud();
  }

  @Override public void showAddRequests(List<AddRequest> addRequests) {
    mAdapter.addAll(addRequests);
    contentPresenter.displayContentView();
  }

  @Override public void showNoAddRequests() {
    contentPresenter.displayEmptyView();
  }

  @Override public void showAddRequestAgreed(AddRequest addRequest) {
    mAdapter.notifyDataSetChanged();
  }

  @Override public void showError(String error, AVException exception) {
    ToastUtils.toast(error);
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

package support.im.addcontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import support.im.R;
import support.im.data.SimpleUser;
import support.im.detail.UserDetailActivity;
import support.im.leanclound.Constants;
import support.im.mobilecontact.MobileContactsActivity;
import support.im.utilities.DialogUtils;
import support.ui.SupportFragment;
import support.ui.utilities.HudUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddContactFragment extends SupportFragment implements View.OnClickListener,
    AddContactsContract.View,
    MaterialSearchView.OnQueryTextListener {

  Toolbar mToolbar;
  MaterialSearchView mSearchView;

  AddContactsContract.Presenter mPresenter;

  public static AddContactFragment create() {
    return new AddContactFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.add_contacts;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mToolbar = ButterKnife.findById(view, R.id.toolbar);

    mSupportActivity.setSupportActionBar(mToolbar);
    if (mSupportActivity.getSupportActionBar() != null) {
      mSupportActivity.setSupportActionBar(mToolbar);
      mSupportActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    mSearchView = ButterKnife.findById(view, R.id.search_view_contacts);
    mSearchView.setVoiceSearch(false);
    mSearchView.setEllipsize(true);
    mSearchView.setSuggestions(new String[]{});
    mSearchView.setSubmitOnClick(true);
    mSearchView.setSuggestionIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_search_primary));
    mSearchView.setHint(getString(R.string.support_im_add_contacts_search_hint));

    mSearchView.setOnQueryTextListener(this);
    ButterKnife.findById(view, R.id.container_add_contacts_search).setOnClickListener(this);
    ButterKnife.findById(view, R.id.container_add_contacts_mobile).setOnClickListener(this);
  }

  @Override public void onResume() {
    super.onResume();
    mPresenter.start();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mSearchView = null;
    mToolbar = null;
  }

  @Override public void onClick(View view) {
    final int viewId = view.getId();
    if (viewId == R.id.container_add_contacts_search) {
      mSearchView.showSearch(true);
      return;
    }
    if (viewId == R.id.container_add_contacts_mobile) {
      startActivity(new Intent(getContext(), MobileContactsActivity.class));
    }
  }

  @Override public void showHud(boolean active) {
    if (active) {
      HudUtils.showHud(getContext(), getString(R.string.support_im_searching));
    }
  }

  @Override public void dismissHud() {
    HudUtils.dismissHud();
  }

  @Override public void showUser(SimpleUser user) {
    Intent intent = new Intent(getContext(), UserDetailActivity.class);
    intent.putExtra(Constants.EXTRA_USER_ID, user.getUserId());
    startActivity(intent);
  }

  @Override public void showNoUser() {
    DialogUtils.simpleDialog(getContext(), getString(R.string.support_im_user_not_found));
  }

  @Override public void displayError(String error, AVException exception) {
    DialogUtils.simpleDialog(getContext(), error);
  }

  @Override public boolean isActive() {
    return isAdded();
  }

  @Override public void setPresenter(AddContactsContract.Presenter presenter) {
    mPresenter = checkNotNull(presenter);
  }

  @Override public boolean onQueryTextSubmit(String query) {
    mPresenter.searchContact(query);
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    mSearchView.setSuggestions(new String[]{newText});
    return false;
  }
}

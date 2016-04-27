package support.im.addcontact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.ButterKnife;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import support.im.R;
import support.im.leanclound.contacts.ContactsManager;
import support.im.mobilecontact.MobileContactsActivity;
import support.ui.SupportFragment;

public class AddContactFragment extends SupportFragment implements View.OnClickListener {

  Toolbar mToolbar;
  MaterialSearchView mSearchView;

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
    mSearchView.setHint(getString(R.string.support_im_add_contacts_search_hint));
    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        ContactsManager.searchUser(0, query);
        //Do some magic
        return true;
      }
      @Override public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });

    ButterKnife.findById(view, R.id.container_add_contacts_search).setOnClickListener(this);
    ButterKnife.findById(view, R.id.container_add_contacts_mobile).setOnClickListener(this);
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
}

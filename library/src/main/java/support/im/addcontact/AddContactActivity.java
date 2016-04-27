package support.im.addcontact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import support.im.R;
import support.ui.SupportSinglePaneActivity;

public class AddContactActivity extends SupportSinglePaneActivity {

  Toolbar mToolbar;
  MaterialSearchView mSearchView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mToolbar = ButterKnife.findById(this, R.id.toolbar);

    setSupportActionBar(mToolbar);
    if (getSupportActionBar() != null) {
      setSupportActionBar(mToolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    mSearchView = ButterKnife.findById(this, R.id.search_view_contacts);
    mSearchView.setVoiceSearch(false);
    mSearchView.setEllipsize(true);

    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        //Do some magic
        return true;
      }
      @Override public boolean onQueryTextChange(String newText) {
        //Do some magic
        return false;
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.contact_search_menus, menu);
    MenuItem item = menu.findItem(R.id.action_search);
    mSearchView.setMenuItem(item);
    return true;
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mSearchView = null;
    mToolbar = null;
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    if (mSearchView.isSearchOpen()) {
      mSearchView.closeSearch();
    } else {
      super.onBackPressed();
    }
  }

  @Override protected Fragment onCreatePane() {
    return AddContactFragment.create();
  }

  @Override protected int getContentViewResId() {
    return R.layout.add_contact_act;
  }

}

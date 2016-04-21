package support.im.demo.features.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.ArrayList;
import java.util.List;
import support.im.contacts.ContactsFragment;
import support.im.conversations.ConversationsFragment;
import support.im.demo.AccountsFragment;
import support.im.demo.R;
import support.im.demo.BaseActivity;
import support.im.demo.BaseFragment;

public class MainActivity extends BaseActivity
    implements BaseFragment.FragmentNavigation {

  //Better convention to properly name the indices what they are in your app
  private final int INDEX_CONVERSIONS = FragNavController.TAB1;
  private final int INDEX_CONTACTS = FragNavController.TAB2;
  private final int INDEX_ACCOUNTS = FragNavController.TAB3;

  private BottomBar mBottomBar;
  private FragNavController mNavController;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupFragNav();
    setupBottomBar(savedInstanceState);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    // Necessary to restore the BottomBar's state, otherwise we would
    // lose the current tab on orientation change.
    mBottomBar.onSaveInstanceState(outState);
  }

  private void setupFragNav() {
    List<Fragment> fragments = new ArrayList<>(5);
    fragments.add(ConversationsFragment.create());
    fragments.add(ContactsFragment.create());
    fragments.add(AccountsFragment.newInstance());
    mNavController = new FragNavController(getSupportFragmentManager(), R.id.container, fragments);
  }

  private void setupBottomBar(Bundle savedInstanceState) {
    mBottomBar = BottomBar.attach(this, savedInstanceState);
    mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
      @Override public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
          case R.id.menu_conversation:
            mNavController.switchTab(INDEX_CONVERSIONS);
            break;
          case R.id.menu_contact:
            mNavController.switchTab(INDEX_CONTACTS);
            break;
          case R.id.menu_accounts:
            mNavController.switchTab(INDEX_ACCOUNTS);
            break;
        }
      }

      @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
        mNavController.clearStack();
      }
    });

    // Make a Badge for the first tab, with red background color and a value of "13".
    BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(INDEX_CONVERSIONS, "#FF0000", 13);
    // Control the badge's visibility
    unreadMessages.show();

    //mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
  }

  @Override public void onBackPressed() {
    if (mNavController.getCurrentStack().size() > 1) {
      mNavController.pop();
    } else {
      super.onBackPressed();
    }
  }

  @Override public void pushFragment(Fragment fragment) {
    mNavController.push(fragment);
  }
}

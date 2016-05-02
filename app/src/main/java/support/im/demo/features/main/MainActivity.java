package support.im.demo.features.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.google.common.collect.Lists;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;
import java.util.ArrayList;
import support.im.contacts.ContactsFragment;
import support.im.conversations.ConversationsFragment;
import support.im.demo.AccountsFragment;
import support.im.demo.BaseActivity;
import support.im.demo.R;

public class MainActivity extends BaseActivity {

  //Better convention to properly name the indices what they are in your app
  private static final int INDEX_CONVERSIONS = 0;
  private static final int INDEX_CONTACTS = 1;
  private static final int INDEX_ACCOUNTS = 2;
  private static final int DEFAULT_POSITION = INDEX_CONVERSIONS;
  private BottomBar mBottomBar;
  private FragmentNavigator mNavigator;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupFragmentNavigator();
    setupBottomBar(savedInstanceState);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    // Necessary to restore the BottomBar's state, otherwise we would
    // lose the current tab on orientation change.
    mBottomBar.onSaveInstanceState(outState);
    mNavigator.onSaveInstanceState(outState);
  }

  private void setupFragmentNavigator() {
    mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.container);
    // set default tab position
    mNavigator.setDefaultPosition(DEFAULT_POSITION);
  }

  private void setupBottomBar(Bundle savedInstanceState) {
    mBottomBar = BottomBar.attach(this, savedInstanceState);
    mBottomBar.setItemsFromMenu(R.menu.menu_bottombar, new OnMenuTabClickListener() {
      @Override public void onMenuTabSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
          case R.id.menu_conversation:
            mNavigator.showFragment(INDEX_CONVERSIONS);
            mBottomBar.selectTabAtPosition(INDEX_CONVERSIONS, true);
            break;
          case R.id.menu_contact:
            mNavigator.showFragment(INDEX_CONTACTS);
            mBottomBar.selectTabAtPosition(INDEX_CONTACTS, true);
            break;
          case R.id.menu_accounts:
            mNavigator.showFragment(INDEX_ACCOUNTS);
            mBottomBar.selectTabAtPosition(INDEX_ACCOUNTS, true);
            break;
        }
      }

      @Override public void onMenuTabReSelected(@IdRes int menuItemId) {
        mNavigator.removeAllFragment(true);
      }
    });

    // Make a Badge for the first tab, with red background color and a value of "13".
    BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(INDEX_CONVERSIONS, "#FF0000", 13);
    // Control the badge's visibility
    unreadMessages.show();

    //mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
  }

  private static class FragmentAdapter implements FragmentNavigatorAdapter {
    private ArrayList<Fragment> mFragments = Lists.newArrayList();

    FragmentAdapter() {
      mFragments.clear();
      mFragments.add(ConversationsFragment.create());
      mFragments.add(ContactsFragment.create());
      mFragments.add(AccountsFragment.create());
    }

    @Override public Fragment onCreateFragment(int position) {
      return mFragments.get(position);
    }

    @Override public String getTag(int position) {
      return mFragments.get(position).getClass().getSimpleName();
    }

    @Override public int getCount() {
      return mFragments.size();
    }
  }
}

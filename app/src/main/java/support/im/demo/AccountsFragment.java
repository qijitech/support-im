package support.im.demo;

import android.support.v4.app.Fragment;

public class AccountsFragment extends BaseFragment {

  public static Fragment newInstance() {
    return new AccountsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.accounts;
  }
}

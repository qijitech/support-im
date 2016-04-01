package support.im;

import android.support.v4.app.Fragment;

/**
 * Created by YuGang Yang on 04 01, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public class AccountsFragment extends SupportImFragment {

  public static Fragment newInstance() {
    return new AccountsFragment();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_accounts;
  }
}

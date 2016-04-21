package support.im.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by YuGang Yang on 04 01, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public abstract class SupportImFragment extends Fragment {

  FragmentNavigation mFragmentNavigation;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(getFragmentLayout(), container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof FragmentNavigation) {
      mFragmentNavigation = (FragmentNavigation) context;
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mFragmentNavigation = null;
  }

  /**
   * Every fragment has to inflate a layout in the onCreateView method. We have added this method
   * to
   * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
   * inflate in this method when extends StarterFragment.
   */
  protected abstract int getFragmentLayout();

  public interface FragmentNavigation {
    void pushFragment(Fragment fragment);
  }
}

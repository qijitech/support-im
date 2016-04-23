package support.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;
import support.im.R;

public class SupportRecyclerViewFragment extends SupportFragment {

  protected RecyclerView mRecyclerView;

  @Override protected int getFragmentLayout() {
    return R.layout.support_ui_recycler_view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = ButterKnife.findById(view, R.id.support_ui_recycler_view);
  }
}

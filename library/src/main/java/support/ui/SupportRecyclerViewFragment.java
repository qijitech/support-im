package support.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import support.im.R;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.content.ContentPresenter;
import support.ui.content.EmptyView;
import support.ui.content.ErrorView;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;

@RequiresContent public class SupportRecyclerViewFragment extends SupportFragment implements
    EmptyView.OnEmptyViewClickListener,
    ErrorView.OnErrorViewClickListener {

  ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());

  protected ContentPresenter contentPresenter;
  protected RecyclerView mRecyclerView;
  protected FrameLayout mContainer;
  protected EasyRecyclerAdapter mAdapter;

  @Override protected int getFragmentLayout() {
    return R.layout.support_ui_recycler_view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new EasyRecyclerAdapter(getContext());
    contentPresenter = factory.createContentPresenter();
    contentPresenter.onCreate(getContext());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = ButterKnife.findById(view, R.id.support_ui_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
    mContainer = ButterKnife.findById(view, R.id.support_ui_content_container);
    setAdapter();
  }

  @Override public void onResume() {
    super.onResume();
    contentPresenter.attachContainer(mContainer);
    contentPresenter.attachContentView(getAttachContentView());
    contentPresenter.setOnEmptyViewClickListener(this);
    contentPresenter.setOnErrorViewClickListener(this);
  }

  protected View getAttachContentView() {
    return mRecyclerView;
  }

  protected void setAdapter() {
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    contentPresenter.onDestroy();
  }

  @Override public void onEmptyViewClick(View view) {

  }

  @Override public void onErrorViewClick(View view) {

  }
}

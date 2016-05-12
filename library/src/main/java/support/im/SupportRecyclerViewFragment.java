package support.im;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import de.greenrobot.event.EventBus;
import support.im.leanclound.event.ConnectionChangeEvent;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportFragment;
import support.ui.content.ContentPresenter;
import support.ui.content.EmptyView;
import support.ui.content.ErrorView;
import support.ui.content.ReflectionContentPresenterFactory;
import support.ui.content.RequiresContent;
import support.ui.utilities.ViewUtils;

@RequiresContent public class SupportRecyclerViewFragment extends SupportFragment implements
    EasyViewHolder.OnItemClickListener,
    EmptyView.OnEmptyViewClickListener,
    ErrorView.OnErrorViewClickListener {

  ReflectionContentPresenterFactory factory =
      ReflectionContentPresenterFactory.fromViewClass(getClass());

  protected ContentPresenter contentPresenter;
  protected RecyclerView mRecyclerView;
  protected FrameLayout mContainer;
  protected View mContentView;
  protected EasyRecyclerAdapter mAdapter;

  View mClientStateView;

  @Override protected int getFragmentLayout() {
    return R.layout.support_ui_recycler_view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAdapter = new EasyRecyclerAdapter(getContext());
    mAdapter.setOnClickListener(this);
    contentPresenter = factory.createContentPresenter();
    contentPresenter.onCreate(getContext());
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = ButterKnife.findById(view, R.id.support_ui_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).size(1).build());
    mContainer = ButterKnife.findById(view, R.id.support_ui_content_container);
    mContentView = ButterKnife.findById(view, R.id.support_ui_content_view);
    mClientStateView = ButterKnife.findById(view, R.id.container_client_state);
    setAdapter();
    EventBus.getDefault().register(this);
  }

  @Override public void onResume() {
    super.onResume();
    contentPresenter.attachContainer(mContainer);
    contentPresenter.attachContentView(getAttachContentView());
    contentPresenter.setOnEmptyViewClickListener(this);
    contentPresenter.setOnErrorViewClickListener(this);
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  protected View getAttachContentView() {
    return mContentView;
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

  @Override public void onItemClick(int position, View view) {

  }

  public void onEvent(ConnectionChangeEvent event) {
    ViewUtils.setGone(mClientStateView, !event.isConnect);
  }

}

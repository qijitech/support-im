package support.im.location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.ButterKnife;
import com.amap.api.maps2d.MapView;
import support.im.R;
import support.ui.app.SupportActivity;

public class LocationActivity extends SupportActivity {

  public CoordinatorLayout mCoordinatorLayout;
  public RecyclerView mRecyclerView;
  public MapView mMapView;

  public BottomSheetBehavior mBehavior;
  private LocationAdapter mLocationAdapter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.locations);
    initialize();
    setupViews();
    setupRecyclerView();
    setupBottomSheetBehavior();

    mMapView.onCreate(savedInstanceState);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
  }

  @Override protected void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  private void initialize() {
    mLocationAdapter = new LocationAdapter();
  }

  private void setupBottomSheetBehavior() {
    mBehavior = BottomSheetBehavior.from(mRecyclerView);
    mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override public void onStateChanged(@NonNull View bottomSheet, int newState) {
        //if(newState == BottomSheetBehavior.STATE_COLLAPSED||newState == BottomSheetBehavior.STATE_HIDDEN){
        //  mMapView.setVisibility(View.GONE);
        //}
      }
      @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        //mMapView.setVisibility(View.VISIBLE);
        //ViewCompat.setAlpha(mMapView, slideOffset);
      }
    });
  }

  private void setupRecyclerView() {
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mLocationAdapter);
  }

  private void setupViews() {
    mCoordinatorLayout = ButterKnife.findById(this, R.id.coordinator_layout);
    mRecyclerView = ButterKnife.findById(this, android.R.id.list);
    mMapView = ButterKnife.findById(this, R.id.map_view);
  }
}

package support.im.location;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import butterknife.ButterKnife;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import support.im.R;
import support.ui.app.SupportActivity;

public class LocationActivity extends SupportActivity implements LocationSource,
    AMapLocationListener {

  public CoordinatorLayout mCoordinatorLayout;
  public RecyclerView mRecyclerView;
  public MapView mMapView;
  private AMap mAMap;

  public BottomSheetBehavior mBehavior;
  private LocationAdapter mLocationAdapter;

  private OnLocationChangedListener mListener;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.locations);
    initialize();
    setupViews();
    setupRecyclerView();
    setupBottomSheetBehavior();
    mMapView.onCreate(savedInstanceState);
    setupMap();
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
    deactivate();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  private void initialize() {
    mLocationAdapter = new LocationAdapter();
  }

  private void setupMap() {
    mAMap = mMapView.getMap();
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.si_map_pin));// 设置小蓝点的图标
    myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
    myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
    myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    mAMap.setMyLocationStyle(myLocationStyle);
    mAMap.setMyLocationEnabled(true);
    mAMap.setLocationSource(this);// 设置定位监听

    UiSettings uiSettings = mAMap.getUiSettings();
    uiSettings.setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
    uiSettings.setScaleControlsEnabled(true);
    uiSettings.setZoomControlsEnabled(false);
    uiSettings.setCompassEnabled(false);
    uiSettings.setMyLocationButtonEnabled(true);
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

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    if (mListener != null && aMapLocation != null) {
      if (aMapLocation.getErrorCode() == 0) {
        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
      } else {
        String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
        Log.e("AmapErr",errText);
      }
    }
  }
  @Override public void activate(OnLocationChangedListener onLocationChangedListener) {
    mListener = onLocationChangedListener;
    if (mLocationClient == null) {
      mLocationClient = new AMapLocationClient(this);
      mLocationOption = new AMapLocationClientOption();
      //设置定位监听
      mLocationClient.setLocationListener(this);
      //设置为高精度定位模式
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      //设置定位参数
      mLocationClient.setLocationOption(mLocationOption);
      // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
      // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
      // 在定位结束后，在合适的生命周期调用onDestroy()方法
      // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
      mLocationClient.startLocation();
    }
  }
  @Override public void deactivate() {
    mListener = null;
    if (mLocationClient != null) {
      mLocationClient.stopLocation();
      mLocationClient.onDestroy();
    }
    mLocationClient = null;
  }
}

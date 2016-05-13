package support.im.location;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.ButterKnife;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import support.im.R;
import support.ui.app.SupportActivity;

public class LocationActivity extends SupportActivity implements LocationSource,
    AMapLocationListener, View.OnClickListener {

  public MapView mMapView;
  private AMap mAMap;
  private OnLocationChangedListener mListener;
  private AMapLocationClient mLocationClient;
  private LatLng myLocation;
  private LatLng mLocation;

  public static void startLocation(Context context, double latitude, double longitude) {
    Intent intent = new Intent(context, LocationActivity.class);
    intent.putExtra("latitude", latitude);
    intent.putExtra("longitude", longitude);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initialize();
    setContentView(R.layout.location);
    setupViews();
    setupMap();
    setupMyLocationStyle();
    setupMarker();
    mMapView.onCreate(savedInstanceState);
  }

  private void initialize() {
    final Intent intent = getIntent();
    if (intent != null) {
      double latitude = intent.getDoubleExtra("latitude", 0);
      double longitude = intent.getDoubleExtra("longitude", 0);
      mLocation = new LatLng(latitude, longitude);
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
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

  private void setupMap() {
    mAMap = mMapView.getMap();
    mAMap.setLocationSource(this);// 设置定位监听
    mAMap.setMyLocationEnabled(true);

    UiSettings uiSettings = mAMap.getUiSettings();
    uiSettings.setZoomControlsEnabled(false);
    uiSettings.setScaleControlsEnabled(true);
    uiSettings.setMyLocationButtonEnabled(false);// 是否显示定位按钮

    CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(16);
    mAMap.moveCamera(cameraUpdate);
  }

  private void setupMarker() {
    if (mLocation != null) {
      mAMap.addMarker(new MarkerOptions().position(mLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.si_map_pin)));
      CameraUpdate update = CameraUpdateFactory.changeLatLng(mLocation);
      mAMap.animateCamera(update);
    }
  }

  private void setupViews() {
    mMapView = ButterKnife.findById(this, R.id.map_view);
    ButterKnife.findById(this, R.id.fab_my_location).setOnClickListener(this);
  }

  private void setupMyLocationStyle() {
    // 自定义系统定位蓝点
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    // 自定义定位蓝点图标
    //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
    myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.img_location_now));
    myLocationStyle.strokeWidth(0);
    myLocationStyle.radiusFillColor(Color.TRANSPARENT);
    //自定义精度范围的圆形边框宽度
    myLocationStyle.strokeWidth(0);
    // 将自定义的 myLocationStyle 对象添加到地图上
    mAMap.setMyLocationStyle(myLocationStyle);
  }

  @Override public void activate(OnLocationChangedListener onLocationChangedListener) {
    mListener = onLocationChangedListener;
    if (mLocationClient == null) {

      mLocationClient = new AMapLocationClient(this);
      AMapLocationClientOption locationOption = new AMapLocationClientOption();
      //设置定位监听
      mLocationClient.setLocationListener(this);
      //设置为高精度定位模式
      locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      //设置定位参数
      mLocationClient.setLocationOption(locationOption);
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

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
      myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }
  }

  @Override public void onClick(View v) {
    if (v.getId() == R.id.btn_location_route) {

      return;
    }

    if (v.getId() == R.id.fab_my_location) {
      if (myLocation != null) {
        CameraUpdate update = CameraUpdateFactory.changeLatLng(myLocation);
        mAMap.animateCamera(update);
      }
    }
  }
}

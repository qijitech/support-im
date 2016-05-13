package support.im.location;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import butterknife.ButterKnife;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import java.util.List;
import support.im.R;
import support.ui.app.SupportActivity;

public class LocationActivity extends SupportActivity implements LocationSource,
    AMapLocationListener, PoiSearch.OnPoiSearchListener, AMap.OnCameraChangeListener,
    GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener, LocationAdapter.LocationDelegate {

  public static final String EXTRA_LOCATION = "location";

  public CoordinatorLayout mCoordinatorLayout;
  public RecyclerView mRecyclerView;
  public MapView mMapView;
  private AMap mAMap;

  public BottomSheetBehavior mBehavior;
  private LocationAdapter mLocationAdapter;

  private OnLocationChangedListener mListener;
  private AMapLocationClient mLocationClient;

  private AMapLocationClientOption mLocationOption;
  private PoiResult poiResult; // poi返回的结果
  private int currentPage = 0;// 当前页面，从0开始计数
  private PoiSearch.Query query;// Poi查询条件类
  private PoiSearch poiSearch;// POI搜索

  private List<PoiItem> poiItems;// poi数据

  private AMapLocation mCurrentMapLocation;
  private LatLng myLocation = null;
  private Marker centerMarker;
  private boolean isMovingMarker = false;
  private ValueAnimator animator = null;
  private GeocodeSearch geocodeSearch;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.locations);
    initialize();
    setupViews();
    setupRecyclerView();
    setupBottomSheetBehavior();
    mMapView.onCreate(savedInstanceState);
    setupMap();
    setupLocationStyle();
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
    mLocationAdapter.setLocationDelegate(this);
  }

  private void setupMap() {
    if (mAMap == null) {
      mAMap = mMapView.getMap();
    }
    mAMap.setLocationSource(this);// 设置定位监听
    mAMap.setMyLocationEnabled(true);

    CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(15);
    mAMap.moveCamera(cameraUpdate);
    mAMap.setOnCameraChangeListener(this);

    UiSettings uiSettings = mAMap.getUiSettings();
    uiSettings.setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
    uiSettings.setZoomControlsEnabled(false);
    uiSettings.setMyLocationButtonEnabled(false);

    geocodeSearch = new GeocodeSearch(this);
    geocodeSearch.setOnGeocodeSearchListener(this);
  }

  private void setupLocationStyle() {
    // 自定义系统定位蓝点
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.img_location_now));
    myLocationStyle.strokeWidth(0);
    myLocationStyle.radiusFillColor(Color.TRANSPARENT);
    mAMap.setMyLocationStyle(myLocationStyle);
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
    ButterKnife.findById(this, R.id.fab_my_location).setOnClickListener(this);
    mCoordinatorLayout = ButterKnife.findById(this, R.id.coordinator_layout);
    mRecyclerView = ButterKnife.findById(this, android.R.id.list);
    mMapView = ButterKnife.findById(this, R.id.map_view);
  }

  @Override public void onClick(View v) {
    CameraUpdate update = CameraUpdateFactory.changeLatLng(myLocation);
    mAMap.animateCamera(update);
  }

  private void addChooseMarker() {
    if (centerMarker == null) {
      MarkerOptions centerMarkerOption = new MarkerOptions().position(myLocation)
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.si_map_pin));
      centerMarker = mAMap.addMarker(centerMarkerOption);
    }
    centerMarker.setPositionByPixels(mMapView.getWidth() / 2, mMapView.getHeight() / 2);
  }

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
      if (mListener != null) {
        mListener.onLocationChanged(aMapLocation);
      }
      mCurrentMapLocation = aMapLocation;
      myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
      addChooseMarker();
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

  @Override public void onPoiSearched(PoiResult poiResult, int rcode) {
    if (rcode == 1000) {
      if (poiResult != null && poiResult.getQuery() != null) {
        this.poiResult = poiResult;
        if (poiResult.getQuery().equals(query)) {
          poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
          mLocationAdapter.replace(poiItems);
          if (poiItems != null && poiItems.size() > 0) {
          }
        }
      }
    }
  }

  @Override public void onPoiItemSearched(PoiItem poiItem, int rcode) {
  }

  @Override public void onCameraChange(CameraPosition cameraPosition) {
    if (centerMarker != null) {
      setMovingMarker();
    }
  }

  @Override public void onCameraChangeFinish(CameraPosition cameraPosition) {
    LatLonPoint point = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
    RegeocodeQuery query = new RegeocodeQuery(point, 50, GeocodeSearch.AMAP);
    geocodeSearch.getFromLocationAsyn(query);
    if (centerMarker != null) {
      animMarker();
    }
  }

  private void endAnim() {
    if (animator != null && animator.isRunning())
      animator.end();
  }

  @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
    if(regeocodeResult != null&& regeocodeResult.getRegeocodeAddress() != null){
      endAnim();
      RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
      mLocationAdapter.setCustomLocation(regeocodeAddress);
      searchBound(regeocodeAddress);
      //String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
      //String shortAdd = formatAddress.replace(regeocodeAddress.getProvince(), "").replace(regeocodeAddress.getCity(), "").replace(regeocodeAddress.getDistrict(), "");
      //tvCurLocation.setText(shortAdd);
    } else{
      //ToastUtil.show(AmapActivity.this, R.string.no_result);
    }
  }

  @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
  }

  private void animMarker() {
    isMovingMarker = false;
    if (animator != null) {
      animator.start();
      return;
    }
    animator = ValueAnimator.ofFloat(mMapView.getHeight()/2, mMapView.getHeight()/2 - 30);
    animator.setInterpolator(new DecelerateInterpolator());
    animator.setDuration(150);
    animator.setRepeatCount(1);
    animator.setRepeatMode(ValueAnimator.REVERSE);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        Float value = (Float) animation.getAnimatedValue();
        centerMarker.setPositionByPixels(mMapView.getWidth() / 2, Math.round(value));
      }
    });
    animator.start();
  }

  private void setMovingMarker() {
    if (isMovingMarker)
      return;

    isMovingMarker = true;
  }

  private void searchBound(RegeocodeAddress regeocodeAddress) {
    query = new PoiSearch.Query("",
        "050000|060000|070000|080000|090000|100000|110000|120000|130000|140000|150000|160000|170000",
        regeocodeAddress.getCity());

    query.setPageNum(currentPage);
    query.setPageSize(20);
    query.setCityLimit(true);
    poiSearch = new PoiSearch(this, query);
    poiSearch.setOnPoiSearchListener(this);
    poiSearch.searchPOIAsyn();
  }

  private void doSearchQuery() {
    //currentPage = 0;
    //query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
    //query.setPageSize(10);// 设置每页最多返回多少条poiitem
    //query.setPageNum(currentPage);// 设置查第一页
    //query.setCityLimit(true);
    //
    //poiSearch = new PoiSearch(this, query);
    //poiSearch.setOnPoiSearchListener(this);
    //poiSearch.searchPOIAsyn();
  }

  @Override public void didSelectLocation(Location location) {
    Intent intent = new Intent();
    intent.putExtra(EXTRA_LOCATION, location);
    setResult(RESULT_OK, intent);
    finish();
  }

}

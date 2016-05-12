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
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import java.util.List;
import support.im.R;
import support.ui.app.SupportActivity;

public class LocationActivity extends SupportActivity implements LocationSource,
    AMapLocationListener, PoiSearch.OnPoiSearchListener {

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
    mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
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
      mCurrentMapLocation = aMapLocation;
      if (aMapLocation.getErrorCode() == 0) {
        mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
        mLocationAdapter.setCustomLocation(aMapLocation);
        searchBound(aMapLocation);
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

  @Override public void onPoiSearched(PoiResult poiResult, int rcode) {
    if (rcode == 1000) {
      if (poiResult != null && poiResult.getQuery() != null) {
        this.poiResult = poiResult;
        if (poiResult.getQuery().equals(query)) {
          poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
          List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
          mLocationAdapter.addData(poiItems);
          if (poiItems != null && poiItems.size() > 0) {

          }
        }
      }
    }
  }

  @Override public void onPoiItemSearched(PoiItem poiItem, int rcode) {
  }

  private void searchBound(AMapLocation aMapLocation) {
    query = new PoiSearch.Query("",
        "050000|060000|070000|080000|090000|100000|110000|120000|130000|140000|150000|160000|170000",
        aMapLocation.getCity());

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

}

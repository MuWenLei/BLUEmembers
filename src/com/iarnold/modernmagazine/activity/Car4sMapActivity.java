package com.iarnold.modernmagazine.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-21
 * @description 4s汽车地图
 */
public class Car4sMapActivity extends BaseActivity {

	private ImageView back;

	private RelativeLayout mapLayout;

	private MapView mMapView;

	private BaiduMap mBaiduMap;

	private String key;

	private int index;// 0 代表4s店 1代表停车场

	boolean isFirstLoc = true;// 是否首次定位

	private LocationClient mLocClient;

	private LocationMode mCurrentMode;

	private BitmapDescriptor mCurrentMarker;

	private PoiSearch mPoiSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		mPoiSearch = PoiSearch.newInstance();
		setContentView(R.layout.activity_car4s_map);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		mapLayout = (RelativeLayout) findViewById(R.id.car4s_map);
		Intent intent = getIntent();
		key = intent.getStringExtra("key");
		index = intent.getIntExtra("inde", 0);
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// 当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
		} else {

			mMapView = new MapView(this, new BaiduMapOptions());
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mapLayout.addView(mMapView);
		mBaiduMap = mMapView.getMap();

		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(14);
		mBaiduMap.animateMapStatus(u);
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();

			}
		});

		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

		MyLocationListenner myListener = new MyLocationListenner();
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationEnabled(true);

		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option5 = new LocationClientOption();
		option5.setOpenGps(true);// 打开gps
		option5.setCoorType("bd09ll"); // 设置坐标类型
		option5.setScanSpan(1000);
		mLocClient.setLocOption(option5);
		mLocClient.start();

		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_landing_arrow);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				String name = marker.getExtraInfo().getString("name");
				ToastUtils.displayShortToast(Car4sMapActivity.this, name);
				return false;
			}
		});

	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mBaiduMap == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).accuracy(0).build();
			mBaiduMap.setMyLocationData(locData);
			if ((isFirstLoc) && mBaiduMap != null) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				mPoiSearch.searchNearby((new PoiNearbySearchOption()).radius(5000).pageCapacity(20)
						.keyword(key).location(new LatLng(location.getLatitude(),
								location.getLongitude())).pageNum(0));
//				mPoiSearch.searchInCity(new PoiCitySearchOption().city("北京").keyword("4s店").pageNum(20));
				// mSearch.reverseGeoCode(new
				// ReverseGeoCodeOption().location(ll));
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

	}

	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {
			if (result.error == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				ToastUtils.displayShortToast(Car4sMapActivity.this, "抱歉，未找到结果");
				return;
			} else if (result.error == SearchResult.ERRORNO.NO_ERROR
					|| result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
				if (result.getAllPoi() == null) {
					ToastUtils.displayShortToast(Car4sMapActivity.this,
							"抱歉，暂无此地信息");
					return;
				}
				if (result.getAllPoi().size() != 0) {
					setMark(result.getAllPoi());
					// String[] temps = new String[result.getAllPoi().size()];
					// for (int i = 0; i < result.getAllPoi().size(); i++) {
					// // if
					// (ToosUtils.isStringEmpty(result.getAllPoi().get(i).name))
					// {
					// // temps[i] = result.getAllPoi().get(i).;
					// // }
					// }
					//
					// SelectDialog dialog = new SelectDialog(
					// LocateSearchActivity.this, "选择详细地址",
					// result.getAllPoi(), handler);
				} else {
					ToastUtils.displayShortToast(Car4sMapActivity.this,
							"抱歉，暂无此地信息");
				}
			}
		}

		public void onGetPoiDetailResult(PoiDetailResult result) {
			// 获取Place详情页检索结果
		}
	};

	private void setMark(List<PoiInfo> vos) {
		BitmapDescriptor bitmap = null;
		if (index == 0) {
			bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding);
		} else if (index == 1) {
			bitmap = BitmapDescriptorFactory.fromResource(R.drawable.mark);
		}

		for (int i = 0; i < vos.size(); i++) {

			if (vos.get(i).location != null
					&& !ToosUtils.isStringEmpty(vos.get(i).name)) {
				OverlayOptions option = new MarkerOptions().position(
						vos.get(i).location).icon(bitmap);
				Marker marker = (Marker) mBaiduMap.addOverlay(option);
				Bundle bundle = new Bundle();
				bundle.putString("name", vos.get(i).name);
				marker.setExtraInfo(bundle);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		mPoiSearch.destroy();
	}
}

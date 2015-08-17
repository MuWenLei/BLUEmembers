package com.iarnold.modernmagazine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iarnold.modernmagazine.model.MainEntity;
import com.iarnold.modernmagazine.utils.Constant;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-6
 * @description 欢迎页
 */
public class WelcomeActivity extends BaseActivity implements
		OnGetGeoCoderResultListener {

	private LocationClient mLocationClient;

	private LocationMode tempMode = LocationMode.Hight_Accuracy;

	private String tempcoor = "gcj02";

	public MyLocationListener mMyLocationListener;

	private BDLocation bdLocation = null;

	private GeoCoder mSearch = null;

	private String city;

	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		SDKInitializer.initialize(getApplicationContext());
		mLocationClient = new LocationClient(getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		new Thread() {
			public void run() {
				try {
					sleep(3000);
					if (!flag) {
						if (ToosUtils.isStringEmpty(city)) {
							city = "北京";
						}
						getInfo();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location != null) {
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(new LatLng(location.getLatitude(), location
								.getLongitude())));

			}

		}

	}

	/**
	 * 获取首页的信息
	 */
	private void getInfo() {
		flag = true;
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		LogManager.LogShow("-----", Constant.ROOT_PATH + "/root.php?state="
				+ city, LogManager.ERROR);
		utils.send(HttpMethod.GET, Constant.ROOT_PATH + "/root.php?state="
				+ city, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				setValues();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Gson gson = new Gson();
				try {
					LogManager.LogShow(BATG, arg0.result, LogManager.ERROR);
					MainEntity entity = gson.fromJson(arg0.result,
							MainEntity.class);
					if (entity != null) {
						Intent intent = new Intent(WelcomeActivity.this,
								MainActivity.class);
//						intent.putExtra("entity", entity);
						ShareDataTool.saveMainInfo(WelcomeActivity.this, entity);
						intent.putExtra("city", city);
						startActivity(intent);
						finish();
					} else {
//						setValues();
					}
				} catch (Exception e) {
					e.printStackTrace();
//					setValues();
				}

			}

		});

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			city = "北京";
		} else {
			city = result.getAddressDetail().city;
			LogManager.LogShow(BATG, city, LogManager.ERROR);
			city = city.substring(0, city.length() - 1);
		}
		if (!flag) {
			if (ToosUtils.isStringEmpty(city)) {
				city = "北京";
			}
			getInfo();
		}
	}

//	private void setValues() {
//		MainEntity mainEntity = new MainEntity();
//		List<String> list = new ArrayList<String>();
//		list.add("assets/main_image.png");
//		list.add("");
//		List<List<String>> list2 = new ArrayList<List<String>>();
//		list2.add(list);
//		mainEntity.carousel = list2;
//
//		List<String> wList = new ArrayList<String>();
//		wList.add("");
//		wList.add("");
//		wList.add("https://www.bluemembers.com.cn/Mall/Index?pageName=Mall");
//		wList.add("北京");
//		wList.add("BEIJING");
//		mainEntity.weather = wList;
//
//		mainEntity.blue = "https://www.bluemembers.com.cn/Account/LogonPage?returnUrl=%2FMyCenter%2FIndex";
//		mainEntity.hlife = "https://www.hlife.com";
//		mainEntity.driving = "http://clubquestion.club-beijing-hyundai.com/hlife_app/driving_list.php";
//		mainEntity.film = "http://clubquestion.club-beijing-hyundai.com/hlife_app/film_list.php";
//		mainEntity.food = "http://clubquestion.club-beijing-hyundai.com/hlife_app/food_list.php";
//		mainEntity.ques = "http://clubquestion.club-beijing-hyundai.com/ques.php";
//		mainEntity.ownerlife = "http://clubquestion.club-beijing-hyundai.com/hlife_app/ownerlife.php";
//		mainEntity.magazine = "http://clubquestion.club-beijing-hyundai.com/hlife_app/magazine_list.php";
//		mainEntity.userlogin = "http://clubquestion.club-beijing-hyundai.com/login.php";
//		mainEntity.userinfo = "http://clubquestion.club-beijing-hyundai.com/getuserinfo.php";
//		mainEntity.userregister = "https://www.bluemembers.com.cn/Account/AccountRegister";
//		mainEntity.userresetpasswd = "https://www.bluemembers.com.cn/Account/ResetPasswd";
//		mainEntity.event = "http://clubquestion.club-beijing-hyundai.com/event.php";
//		mainEntity.eventreport = "http://clubquestion.club-beijing-hyundai.com/hlife_app/getevent.php";
//
//		Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//		intent.putExtra("entity", mainEntity);
//		intent.putExtra("city", city);
//		startActivity(intent);
//		finish();
//
//	}
}

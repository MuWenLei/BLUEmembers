package com.iarnold.modernmagazine.utils;

import java.util.ArrayList;
import java.util.List;

import com.iarnold.modernmagazine.model.MainEntity;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {

	private static final String TAG = MyApplication.class.getName();

	private List<Activity> list = new ArrayList<Activity>();

	private static MyApplication instance;

//	private MainEntity mainEntity;

	// private String lifeUrl;
	//
	// private String magaUrl;

	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

	public void exit() {
		for (Activity activity : list) {
			activity.finish();
		}
		System.exit(0);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

	}

	// /**
	// * 保存车主生活的url
	// * @param url
	// */
	// public void saveLifeUrl(String url) {
	// lifeUrl = url;
	// }
	// /**
	// * 获取车主生活url
	// * @return
	// */
	// public String getLifeUrl(){
	// return lifeUrl;
	// }
	//
	// /**
	// * 保存电子杂志
	// * @param url
	// */
	// public void saveMagaUrl(String url) {
	// magaUrl = url;
	// }
	// /**
	// * 获取电子杂志url
	// * @return
	// */
	// public String getMagaUrl(){
	// return magaUrl;
	// }
//	/**
//	 * 保存接口信息
//	 * 
//	 * @param mainEntity
//	 */
//	public void saveMainEntity(MainEntity mainEntity) {
//		this.mainEntity = mainEntity;
//	}
//	/**
//	 * 获取接口信息
//	 * @return
//	 */
//	public MainEntity getMainEntity() {
//		return mainEntity;
//	}

}

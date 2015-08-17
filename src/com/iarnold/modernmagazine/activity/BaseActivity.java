package com.iarnold.modernmagazine.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.iarnold.modernmagazine.utils.MyApplication;

/**
 * @author Mu
 * @date  2015-6-16
 * @description  基本Activity
 */
public class BaseActivity extends FragmentActivity {
	private static final int notifiId = 11;
	public static final String BATG="BLUEmembers";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);

	}

}

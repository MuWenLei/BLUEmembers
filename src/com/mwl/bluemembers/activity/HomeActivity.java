package com.mwl.bluemembers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mwl.bluemembers.R;
import com.mwl.bluemembers.activity.fragment.LifeFragment;
import com.mwl.bluemembers.activity.fragment.MagazineFragment;
import com.mwl.bluemembers.activity.fragment.PersonFragment;
import com.mwl.bluemembers.dialog.LoginDialog;
import com.mwl.bluemembers.utils.ShareDataTool;
import com.mwl.bluemembers.utils.ToosUtils;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * @author Mu
 * @date 2015-6-17
 * @description 二级主页
 */
public class HomeActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private RadioGroup group;

	private RadioButton magazine;

	private RadioButton life;

	private RadioButton person;

	private View magazinelin;

	private View lifelin;

	private View personlin;

	private static FragmentManager fMgr;

	private UMSocialService mController;

	private int index;// 那一页 0 电子杂志 1 车主生活 2个人中心

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 111) {
				index = 2;
				magazine.setChecked(false);
				life.setChecked(false);
				person.setChecked(true);
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("MagazineFragment"));
				PersonFragment personFragment = new PersonFragment();
				ft.add(R.id.home_fragmnet, personFragment, "LifeFragment");
				ft.addToBackStack("LifeFragment");
				ft.commit();
			}
			//
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		index = getIntent().getIntExtra("index", 0);
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		initView();

	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		group = (RadioGroup) findViewById(R.id.home_group);
		magazine = (RadioButton) findViewById(R.id.home_magazine);
		life = (RadioButton) findViewById(R.id.home_life);
		person = (RadioButton) findViewById(R.id.home_person);
		magazinelin = findViewById(R.id.home_magazinelin);
		lifelin = findViewById(R.id.home_lifelin);
		personlin = findViewById(R.id.home_personlin);

		fMgr = getSupportFragmentManager();
		initFragment();

		magazine.setChecked(true);
		life.setChecked(false);
		person.setChecked(false);

		back.setOnClickListener(this);
		personlin.setOnClickListener(this);
		magazinelin.setOnClickListener(this);
		lifelin.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (index == 0) {
			magazine.setChecked(true);
			life.setChecked(false);
			person.setChecked(false);
			if ((fMgr.findFragmentByTag("MagazineFragment") != null && fMgr
					.findFragmentByTag("MagazineFragment").isVisible())) {
				return;
			}
			popAllFragmentsExceptTheBottomOne();
		} else if (index == 1) {
			magazine.setChecked(false);
			life.setChecked(true);
			person.setChecked(false);
			popAllFragmentsExceptTheBottomOne();
			FragmentTransaction ft1 = fMgr.beginTransaction();
			ft1.hide(fMgr.findFragmentByTag("MagazineFragment"));
			LifeFragment lifeFragment = new LifeFragment();
			ft1.add(R.id.home_fragmnet, lifeFragment, "LifeFragment");
			ft1.addToBackStack("LifeFragment");
			ft1.commit();
		} else if (index == 2) {
			magazine.setChecked(false);
			life.setChecked(false);
			person.setChecked(true);
			popAllFragmentsExceptTheBottomOne();
			FragmentTransaction ft = fMgr.beginTransaction();
			ft.hide(fMgr.findFragmentByTag("MagazineFragment"));
			PersonFragment personFragment = new PersonFragment();
			ft.add(R.id.home_fragmnet, personFragment, "LifeFragment");
			ft.addToBackStack("LifeFragment");
			ft.commit();
		}
	}

	/**
	 * 初始化首个Fragment
	 */
	private void initFragment() {
		FragmentTransaction ft = fMgr.beginTransaction();
		MagazineFragment magazineFragment = new MagazineFragment();
		ft.add(R.id.home_fragmnet, magazineFragment, "MagazineFragment");
		ft.addToBackStack("MagazineFragment");
		ft.commit();
	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fMgr.getBackStackEntryCount() - 1; i < count; i++) {
			fMgr.popBackStack();

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.home_magazinelin:
			index = 0;
			magazine.setChecked(true);
			life.setChecked(false);
			person.setChecked(false);

			if ((fMgr.findFragmentByTag("MagazineFragment") != null && fMgr
					.findFragmentByTag("MagazineFragment").isVisible())) {
				return;
			}
			popAllFragmentsExceptTheBottomOne();
			break;
		case R.id.home_lifelin:
			index = 1;
			magazine.setChecked(false);
			life.setChecked(true);
			person.setChecked(false);
			popAllFragmentsExceptTheBottomOne();
			FragmentTransaction ft1 = fMgr.beginTransaction();
			ft1.hide(fMgr.findFragmentByTag("MagazineFragment"));
			LifeFragment lifeFragment = new LifeFragment();
			ft1.add(R.id.home_fragmnet, lifeFragment, "LifeFragment");
			ft1.addToBackStack("LifeFragment");
			ft1.commit();
			break;
		case R.id.home_personlin:
			if (ToosUtils
					.isStringEmpty(ShareDataTool.getKey(HomeActivity.this))) {
				LoginDialog dialog = new LoginDialog(HomeActivity.this,
						handler, mController);
			} else {
				index = 2;
				magazine.setChecked(false);
				life.setChecked(false);
				person.setChecked(true);
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction ft = fMgr.beginTransaction();
				ft.hide(fMgr.findFragmentByTag("MagazineFragment"));
				PersonFragment personFragment = new PersonFragment();
				ft.add(R.id.home_fragmnet, personFragment, "LifeFragment");
				ft.addToBackStack("LifeFragment");
				ft.commit();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	// 点击返回按钮
	@Override
	public void onBackPressed() {
		finish();
	}
}

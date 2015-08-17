package com.iarnold.modernmagazine.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarnold.modernmagazine.adapter.GalleryAdapter;
import com.iarnold.modernmagazine.dialog.LoginDialog;
import com.iarnold.modernmagazine.model.DetailEntity;
import com.iarnold.modernmagazine.model.MainEntity;
import com.iarnold.modernmagazine.utils.Constant;
import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.MyApplication;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.TimeUtils;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.iarnold.modernmagazine.view.MyGallery;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mining.app.zxing.decoding.Intents.Share;
import com.iarnold.modernmagazine.R;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * @author Mu
 * @date 2015-6-15
 * @description 主页
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private ImageView store;

	private ImageView blue;

	private ImageView life;

	private ImageView driving;

	private ImageView shadow;

	private ImageView cate;

	private ImageView favorable;

	private ImageView que;

	private ImageView more;

	private ImageView magazine;

	private ImageView carLife;

	private ImageView person;

	private TextView weatherTem;

	private ImageView weatherTemImage;

	private ImageView weatherImage;

	private TextView date;

	private int width;

	protected final int UPDATE_GALLERY = 10; // Grallery更新

	private GalleryAdapter adapter;

	private MyGallery gallery = null;

	private LinearLayout lin = null;

	private Timer timer;

	private TimerTask task;

	private BitmapUtils bitmapUtils;

	private boolean flag = true;

	private boolean canExit = false;

	// private String city;

	private TextView address;

	private UMSocialService mController;

	private String latitude;

	private String longitude;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_GALLERY:
				if (gallery.getSelectedItemPosition() == 0) {
					gallery.setSelection(gallery.getFirstVisiblePosition() + 1);
				} else {
					gallery.setSelection(gallery.getFirstVisiblePosition() + 2);
				}
				break;
			case 1000:
				canExit = false;
				break;
			case 111:
				Intent intent = new Intent(MainActivity.this,
						HomeActivity.class);
				intent.putExtra("index", 2);
				startActivity(intent);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		width = DensityUtil.getScreenWidth(this);
		bitmapUtils = new BitmapUtils(this);
		longitude = getIntent().getStringExtra("longitude");
		latitude = getIntent().getStringExtra("latitude");
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		initView();
		initData();

	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		store = (ImageView) findViewById(R.id.main_store);
		blue = (ImageView) findViewById(R.id.main_blue);
		life = (ImageView) findViewById(R.id.main_life);
		driving = (ImageView) findViewById(R.id.main_driving);
		shadow = (ImageView) findViewById(R.id.main_shadow);
		cate = (ImageView) findViewById(R.id.main_cate);
		favorable = (ImageView) findViewById(R.id.main_favorable);
		que = (ImageView) findViewById(R.id.main_que);
		more = (ImageView) findViewById(R.id.main_more);
		magazine = (ImageView) findViewById(R.id.main_bom_0);
		carLife = (ImageView) findViewById(R.id.main_bom_1);
		person = (ImageView) findViewById(R.id.main_bom_2);
		gallery = (MyGallery) findViewById(R.id.gallery);
		lin = (LinearLayout) findViewById(R.id.lin);
		weatherTem = (TextView) findViewById(R.id.main_weather);
		weatherTemImage = (ImageView) findViewById(R.id.main_temimage);
		weatherImage = (ImageView) findViewById(R.id.main_weatherimage);
		date = (TextView) findViewById(R.id.main_time);
		address = (TextView) findViewById(R.id.main_address);

		LayoutParams blueParams = (LayoutParams) blue.getLayoutParams();
		blueParams.width = (int) (0.306 * width);
		blueParams.height = (int) (0.306 * width);
		blueParams.leftMargin = (int) (0.119 * width);
		blue.setLayoutParams(blueParams);

		LayoutParams lifeParams = (LayoutParams) life.getLayoutParams();
		lifeParams.width = (int) (0.306 * width);
		lifeParams.height = (int) (0.306 * width);
		lifeParams.rightMargin = (int) (0.119 * width);
		life.setLayoutParams(lifeParams);

		LayoutParams drivingParams = (LayoutParams) driving.getLayoutParams();
		drivingParams.width = (int) (0.306 * width);
		drivingParams.height = (int) (0.306 * width);
		drivingParams.leftMargin = (int) (0.119 * width);
		driving.setLayoutParams(drivingParams);

		LayoutParams shadowParams = (LayoutParams) shadow.getLayoutParams();
		shadowParams.width = (int) (0.306 * width);
		shadowParams.height = (int) (0.306 * width);
		shadowParams.rightMargin = (int) (0.119 * width);
		shadow.setLayoutParams(shadowParams);

		LayoutParams cateParams = (LayoutParams) cate.getLayoutParams();
		cateParams.width = (int) (0.306 * width);
		cateParams.height = (int) (0.306 * width);
		cateParams.leftMargin = (int) (0.119 * width);
		cate.setLayoutParams(cateParams);

		LayoutParams favorableParams = (LayoutParams) favorable
				.getLayoutParams();
		favorableParams.width = (int) (0.306 * width);
		favorableParams.height = (int) (0.306 * width);
		favorableParams.rightMargin = (int) (0.119 * width);
		favorable.setLayoutParams(favorableParams);

		LayoutParams queParams = (LayoutParams) que.getLayoutParams();
		queParams.width = (int) (0.306 * width);
		queParams.height = (int) (0.306 * width);
		queParams.leftMargin = (int) (0.119 * width);
		que.setLayoutParams(queParams);

		LayoutParams moreParams = (LayoutParams) more.getLayoutParams();
		moreParams.width = (int) (0.306 * width);
		moreParams.height = (int) (0.306 * width);
		moreParams.rightMargin = (int) (0.119 * width);
		more.setLayoutParams(moreParams);

		back.setVisibility(View.GONE);
		store.setOnClickListener(this);
		blue.setOnClickListener(this);
		life.setOnClickListener(this);
		driving.setOnClickListener(this);
		shadow.setOnClickListener(this);
		cate.setOnClickListener(this);
		favorable.setOnClickListener(this);
		que.setOnClickListener(this);
		more.setOnClickListener(this);
		magazine.setOnClickListener(this);
		carLife.setOnClickListener(this);
		person.setOnClickListener(this);

	}

	private void initData() {

		String fontPath = "fonts/Nipic_11436939_20141210091216551798.ttf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		address.setTypeface(tf);
		// address.setText(Cn2Spell.converterToSpell(city));
		address.setText(ShareDataTool.getWeather(MainActivity.this).get(4));

		if (ToosUtils.isStringEmpty(ShareDataTool.getWeather(MainActivity.this)
				.get(0))) {
			weatherTem.setVisibility(View.INVISIBLE);
			weatherTemImage.setVisibility(View.INVISIBLE);
		} else {
			weatherTem.setVisibility(View.VISIBLE);
			weatherTemImage.setVisibility(View.VISIBLE);
			weatherTem.setText(ShareDataTool.getWeather(MainActivity.this).get(
					0));
		}

		if (ToosUtils.isStringEmpty(ShareDataTool.getWeather(MainActivity.this)
				.get(1))) {
			weatherImage.setVisibility(View.INVISIBLE);
		} else {
			weatherImage.setVisibility(View.VISIBLE);
			bitmapUtils.display(weatherImage,
					ShareDataTool.getWeather(MainActivity.this).get(1));
		}
		date.setText(ShareDataTool.getWeather(MainActivity.this).get(3)
				+ TimeUtils.getDate());
		addBunne(ShareDataTool.getCarousel(this));

		getInfo();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_store:
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(ShareDataTool.getWeather(MainActivity.this)
					.get(2))); // 这里面是需要调转的rul
			it = Intent.createChooser(it, null);
			startActivity(it);
			break;
		case R.id.main_blue:
			Intent it2 = new Intent(Intent.ACTION_VIEW);
			it2.setData(Uri.parse(ShareDataTool.getBlue(MainActivity.this))); // 这里面是需要调转的rul
			it2 = Intent.createChooser(it2, null);
			startActivity(it2);

			break;
		case R.id.main_life:
			Intent it3 = new Intent(Intent.ACTION_VIEW);
			it3.setData(Uri.parse(ShareDataTool.getHlife(MainActivity.this))); // 这里面是需要调转的rul
			it3 = Intent.createChooser(it3, null);
			startActivity(it3);
			break;
		case R.id.main_driving:
			Intent intent = new Intent(MainActivity.this, DetailActivity.class);
			intent.putExtra("url", ShareDataTool.getDriving(MainActivity.this));
			startActivity(intent);
			break;
		case R.id.main_shadow:
			Intent intent2 = new Intent(MainActivity.this, DetailActivity.class);
			intent2.putExtra("url", ShareDataTool.getFilm(MainActivity.this));
			startActivity(intent2);
			break;
		case R.id.main_cate:
			Intent intent3 = new Intent(MainActivity.this, DetailActivity.class);
			intent3.putExtra("url", ShareDataTool.getFood(MainActivity.this));
			startActivity(intent3);
			break;
		case R.id.main_favorable:
			Intent intent10 = new Intent(MainActivity.this, QRActivity.class);
			startActivity(intent10);
			// Intent intent4=new Intent(MainActivity.this,
			// DetailActivity.class);
			// intent4.putExtra("url", ownerlifeUrl);
			// startActivity(intent4);
			break;
		case R.id.main_que:
			Intent intent8 = new Intent(MainActivity.this, WebActivity.class);
			intent8.putExtra("url", ShareDataTool.getQues(MainActivity.this));
			startActivity(intent8);
			break;
		case R.id.main_more:

			break;
		case R.id.main_bom_0:
			Intent intent7 = new Intent(MainActivity.this, HomeActivity.class);
			intent7.putExtra("index", 0);
			startActivity(intent7);
			break;
		case R.id.main_bom_1:
			Intent intent5 = new Intent(MainActivity.this, HomeActivity.class);
			intent5.putExtra("index", 1);
			startActivity(intent5);
			break;
		case R.id.main_bom_2:
			if (ToosUtils
					.isStringEmpty(ShareDataTool.getKey(MainActivity.this))) {
				LoginDialog dialog = new LoginDialog(MainActivity.this,
						handler, mController);
			} else {
				Intent intent9 = new Intent(MainActivity.this,
						HomeActivity.class);
				intent9.putExtra("index", 2);
				startActivity(intent9);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 加载bunne图片
	 */
	private void addBunne(final List<List<String>> images) {
		int m = DensityUtil.dip2px(this, 3);
		for (int i = 0; i < images.size(); i++) {
			ImageView image = (ImageView) LayoutInflater.from(this).inflate(
					R.layout.detail_point, null);
			image.setPadding(m, 0, m, 0);
			lin.addView(image);
		}

		if (images.size() == 1) {
			lin.setVisibility(View.INVISIBLE);
		} else {
			lin.setVisibility(View.VISIBLE);
		}
		adapter = new GalleryAdapter(this, gallery, images, width);
		gallery.setAdapter(adapter);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!ToosUtils.isStringEmpty(images.get(
						position % images.size()).get(1))) {
					Intent it = new Intent(Intent.ACTION_VIEW);

					it.setData(Uri.parse(images.get(position % images.size())
							.get(1))); // 这里面是需要调转的rul

					it = Intent.createChooser(it, null);

					startActivity(it);
				}

			}
		});
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView iv;
				int count = lin.getChildCount();
				for (int i = 0; i < count; i++) {
					iv = (ImageView) lin.getChildAt(i);
					if (i == position % images.size()) {
						iv.setImageResource(R.drawable.circle_select);
					} else {
						iv.setImageResource(R.drawable.circle_normal);
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				msg.what = UPDATE_GALLERY;
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 1000, 3000);
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
		if (canExit) {
			MyApplication.getInstance().exit();
			finish();
		} else {
			ToastUtils.displayShortToast(this, "再按一次退出程序");
			canExit = true;
			handler.sendEmptyMessageDelayed(1000, 2000);
		}
	}

	/**
	 * 获取首页的信息
	 */
	private void getInfo() {
		flag = true;
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
//		LogManager.LogShow("-----", Constant.ROOT_PATH + "/root.php?state="
//				+ city, LogManager.ERROR);
		utils.send(HttpMethod.GET,
				"http://www.bluemembershlife.com/weather.php?latitude="
						+ latitude + "&longitude=" + longitude,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							LogManager.LogShow(BATG, arg0.result,
									LogManager.ERROR);
							java.lang.reflect.Type type = new TypeToken<List<String>>() {
							}.getType();
							List<String> strings = gson.fromJson(arg0.result,
									type);
							String fontPath = "fonts/Nipic_11436939_20141210091216551798.ttf";
							Typeface tf = Typeface.createFromAsset(getAssets(),
									fontPath);
							address.setTypeface(tf);
							// address.setText(Cn2Spell.converterToSpell(city));
							address.setText(strings.get(4));
							if (ToosUtils.isStringEmpty(strings.get(0))) {
								weatherTem.setVisibility(View.INVISIBLE);
								weatherTemImage.setVisibility(View.INVISIBLE);
							} else {
								weatherTem.setVisibility(View.VISIBLE);
								weatherTemImage.setVisibility(View.VISIBLE);
								weatherTem.setText(strings.get(0));
							}

							if (ToosUtils.isStringEmpty(strings.get(1))) {
								weatherImage.setVisibility(View.INVISIBLE);
							} else {
								weatherImage.setVisibility(View.VISIBLE);
								bitmapUtils.display(weatherImage,
										strings.get(1));
							}
							date.setText(strings.get(3) + TimeUtils.getDate());
							// storeUrl = strings.get(2);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				});

	}
}

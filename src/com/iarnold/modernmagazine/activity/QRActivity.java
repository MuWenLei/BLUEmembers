package com.iarnold.modernmagazine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.MyApplication;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mining.app.zxing.decoding.Intents.Share;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-21
 * @description 二维码扫描
 */
public class QRActivity extends BaseActivity implements OnClickListener {

	private ImageView back;

	private ImageView qrimage;

	// private ImageView image1;
	//
	// private ImageView image2;

	private int width;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		width = DensityUtil.getScreenWidth(this);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		qrimage = (ImageView) findViewById(R.id.qr_qr);
		qrimage.setFocusable(true);
		qrimage.setFocusableInTouchMode(true);
		qrimage.requestFocus();
		webView = (WebView) findViewById(R.id.qr_web);
		// image1 = (ImageView) findViewById(R.id.qr_image1);
		// image2 = (ImageView) findViewById(R.id.qr_image2);
		back.setOnClickListener(this);
		qrimage.setOnClickListener(this);

		// webView.setWebViewClient(new WebViewClient() {
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
		// });
		// webView.setWebViewClient(new WebViewClient());
		// webView.setWebChromeClient(new WebChromeClient());
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		// webView.getSettings().setDefaultTextEncodingName("UTF-8");

		// if (MyApplication.getInstance().getMainEntity().event != null) {
		String url = ShareDataTool.getEvent(this);
		if (!ToosUtils.isStringEmpty(ShareDataTool.getKey(this))) {
			url = url + "?key=" + ShareDataTool.getKey(this);
		}
		webView.loadUrl(url);
		webView.addJavascriptInterface(this, "hlife");

		// LogManager.LogShow("-----",
		// MyApplication.getInstance().getMainEntity().event,
		// LogManager.ERROR);

		LayoutParams qrParams = (LayoutParams) qrimage.getLayoutParams();
		qrParams.width = (int) (0.362 * width);
		qrParams.height = (int) (0.362 * width);
		qrimage.setLayoutParams(qrParams);

	}

	@JavascriptInterface
	public void jsToApp(String name) {
		Intent intent = new Intent(this, WebActivity.class);
		intent.putExtra("url", name);
		startActivity(intent);
		// Toast.makeText(this, "朱拉等级玩家的", Toast.LENGTH_LONG).show();
	}

	// public void jsToApp(boolean flag, String name) {
	// Toast.makeText(this, "0ajwlkjdlwk", Toast.LENGTH_LONG).show();
	// }

	// @JavascriptInterface
	// public boolean jsToApp() {
	// Toast.makeText(this, "0ajwlkjdlwk", Toast.LENGTH_LONG).show();
	// return true;
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.qr_qr:
			Intent intent2 = new Intent(QRActivity.this, QRScanActivity.class);
			startActivityForResult(intent2, 0);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String temp = data.getStringExtra("qr");
				if (ToosUtils.isURL(temp)) {
					if (!ToosUtils.isStringEmpty(ShareDataTool
							.getKey(QRActivity.this))) {
						getSub(temp);
					}
					Intent intent = new Intent(QRActivity.this,
							WebActivity.class);
					intent.putExtra("url", temp);
					startActivity(intent);
				} else {
					ToastUtils.displayShortToast(QRActivity.this, "不是个有效的链接！");
				}

			}
		}

	}

	// public class JavaScriptObject {
	// Context mContxt;
	//
	// // sdk17版本以上加上注解
	// public void jsToApp(boolean f, String name) {
	// Toast.makeText(mContxt, "0ajwlkjdlwk", Toast.LENGTH_LONG).show();
	// }
	//
	// // public JavaScriptObject(Context mContxt) {
	// // this.mContxt = mContxt;
	// // // fun1FromAndroid(true, "aaaa");
	// // }
	// //
	// public void callDrawGLFunction2(boolean flag, String name) {
	// Toast.makeText(mContxt, name, Toast.LENGTH_LONG).show();
	// }
	// //
	// }

	/**
	 * 
	 * @param style
	 */
	private void getSub(String url) {
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		// LogManager.LogShow("-----",
		// MyApplication.getInstance().getMainEntity().eventreport
		// + "?key=" + ShareDataTool.getKey(QRActivity.this)
		// + "&url=" + url, LogManager.ERROR);
		utils.send(HttpMethod.GET, ShareDataTool.getEventreport(this) + "?key="
				+ ShareDataTool.getKey(QRActivity.this) + "&url=" + url,
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

					}
				});

	}

}

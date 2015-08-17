package com.iarnold.modernmagazine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.iarnold.modernmagazine.dialog.AltrDialog;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-21
 * @description 网页页面
 */
public class WebActivity extends BaseActivity {

	private ImageView back;

	private WebView webView;

	private String url;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 40:
				if (msg.arg1 == -1) {
					finish();
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		url = getIntent().getStringExtra("url");
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		webView = (WebView) findViewById(R.id.web_webview);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(this, "hlife");
		if (url != null) {
			webView.loadUrl(url);
		}

	}

	@JavascriptInterface
	public void jsToApp(boolean flag, String name) {
		if (flag) {
			AltrDialog altrDialog = new AltrDialog(this, handler, name, -1);
		} else {
			AltrDialog altrDialog = new AltrDialog(this, handler, name, 0);
		}

		// Toast.makeText(this, "朱拉等级玩家的", Toast.LENGTH_LONG).show();
	}

}

package com.iarnold.modernmagazine.dialog;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iarnold.modernmagazine.R;
import com.iarnold.modernmagazine.model.InfoSendEntity;
import com.iarnold.modernmagazine.model.UserInfoEntity;
import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.ShareUtils;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.controller.UMSocialService;

/**
 * @author Mu
 * @description 登录对话框
 */
public class LoginDialog implements android.view.View.OnClickListener {
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	int height;
	private Handler handler;
	private ImageView back;
	private View pro;

	private TextView weixin;

	private TextView qq;

	private TextView xinlang;

	private UMSocialService mController;

	private InfoSendEntity sendEntity;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111:
				sendEntity = (InfoSendEntity) msg.obj;
				getLogin(sendEntity);
				break;
			case 112:
				sendEntity = (InfoSendEntity) msg.obj;
				getLogin(sendEntity);
				break;
			case 113:
				sendEntity = (InfoSendEntity) msg.obj;
				getLogin(sendEntity);
				break;

			default:
				break;
			}
		};
	};

	public LoginDialog(final Context context, Handler handler,
			UMSocialService mController) {
		super();
		this.context = context;
		this.handler = handler;
		this.mController = mController;
		d = new Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.login_dialog, null);
		d.setContentView(view);
		back = (ImageView) d.findViewById(R.id.login_close);
		weixin = (TextView) d.findViewById(R.id.login_weixin);
		qq = (TextView) d.findViewById(R.id.login_qq);
		xinlang = (TextView) d.findViewById(R.id.login_xinlang);
		pro = d.findViewById(R.id.login_pro);
		back.setOnClickListener(this);
		weixin.setOnClickListener(this);
		qq.setOnClickListener(this);
		xinlang.setOnClickListener(this);
		init();
		//

	}

	private void init() {
		Window dialogWindow = d.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		// lp.width = LayoutParams.WRAP_CONTENT;
		lp.width = DensityUtil.dip2px(context, 250);
		// dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		height = lp.height;
		int width = lp.width;

		dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);

		d.show();
		// dialogAnimation(d, view, getWindowHeight(), height, false);
		// dialogAnimation(d, view, -width,
		// 0, false);
	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	// private void dialogAnimation(final Dialog d, View v, int from, int to,
	// boolean needDismiss) {
	//
	// Animation anim = new TranslateAnimation(0, 0, from, to);
	// // Animation anim = new TranslateAnimation(from, to, 0, 0);
	// anim.setFillAfter(true);
	// anim.setDuration(700);
	// if (needDismiss) {
	// anim.setAnimationListener(new AnimationListener() {
	//
	// public void onAnimationStart(Animation animation) {
	// }
	//
	// public void onAnimationRepeat(Animation animation) {
	// }
	//
	// public void onAnimationEnd(Animation animation) {
	// d.dismiss();
	// }
	// });
	//
	// }
	// v.startAnimation(anim);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_close:
			d.dismiss();
			break;
		case R.id.login_weixin:
			ShareUtils.loginWeixin(context, mHandler, mController);
			// d.dismiss();
			break;
		case R.id.login_qq:
			ShareUtils.loginQQ(context, mHandler, mController);
			// d.dismiss();
			break;
		case R.id.login_xinlang:
			ShareUtils.loginSina(context, mHandler, mController);
			// d.dismiss();
			break;

		default:
			break;
		}
	}

	private void getLogin(InfoSendEntity infoSendEntity) {
		RequestParams rp = new RequestParams();
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		// LogManager.LogShow("-----",
		// MyApplication.getInstance().getMainEntity().userlogin
		// + "?username=" + sname + "&password=" + spwd,
		// LogManager.ERROR);
		utils.send(HttpMethod.GET, ShareDataTool.getUserinfo(context) + "?uid="
				+ infoSendEntity.uid + "&type=" + infoSendEntity.type
				+ "&nickname=" + infoSendEntity.nickname + "&face="
				+ infoSendEntity.face, rp, new RequestCallBack<String>() {
			@Override
			public void onStart() {
				pro.setVisibility(View.VISIBLE);
				super.onStart();
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				pro.setVisibility(View.GONE);
				ToastUtils.displayFailureToast(context);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				pro.setVisibility(View.GONE);
				Gson gson = new Gson();
				try {
					LogManager.LogShow("-----", arg0.result, LogManager.ERROR);
					JSONObject jsonObject = new JSONObject(arg0.result);
					int state = jsonObject.getInt("state");
					if (state == 1) {
						UserInfoEntity infoEntity = gson.fromJson(arg0.result,
								UserInfoEntity.class);
						ShareDataTool.SaveInfo(context, infoEntity);
						handler.sendEmptyMessage(111);
						d.dismiss();
					} else if (state == 0) {

						ToastUtils.displayShortToast(context, jsonObject
								.getJSONObject("message").getString("error"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					ToastUtils.displayFailureToast(context);
				}

			}

		});
	}
}

package com.mwl.bluemembers.dialog;

import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mwl.bluemembers.R;
import com.mwl.bluemembers.model.UserInfoEntity;
import com.mwl.bluemembers.utils.DensityUtil;
import com.mwl.bluemembers.utils.LogManager;
import com.mwl.bluemembers.utils.MyApplication;
import com.mwl.bluemembers.utils.ShareDataTool;
import com.mwl.bluemembers.utils.ShareUtils;
import com.mwl.bluemembers.utils.ToastUtils;
import com.mwl.bluemembers.utils.ToosUtils;
import com.umeng.socialize.controller.UMServiceFactory;
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
		d.show();
		dialogAnimation(d, view, getWindowHeight(), height, false);
		// dialogAnimation(d, view, -width,
		// 0, false);
	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		Animation anim = new TranslateAnimation(0, 0, from, to);
		// Animation anim = new TranslateAnimation(from, to, 0, 0);
		anim.setFillAfter(true);
		anim.setDuration(700);
		if (needDismiss) {
			anim.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					d.dismiss();
				}
			});

		}
		v.startAnimation(anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_close:
			d.dismiss();
			break;
		case R.id.login_weixin:
			ShareUtils.loginWeixin(context, handler, mController);
			d.dismiss();
			break;
		case R.id.login_qq:
			ShareUtils.loginQQ(context, handler, mController);
			d.dismiss();
			break;
		case R.id.login_xinlang:
			ShareUtils.loginSina(context, handler, mController);
			d.dismiss();
			break;

		default:
			break;
		}
	}
	// private void getLogin() {
	// final String sname = ToosUtils.getTextContent(name);
	// String spwd = ToosUtils.getTextContent(pwd);
	// RequestParams rp = new RequestParams();
	// HttpUtils utils = new HttpUtils();
	// utils.configTimeout(20000);
	// // LogManager.LogShow("-----",
	// // MyApplication.getInstance().getMainEntity().userlogin
	// // + "?username=" + sname + "&password=" + spwd,
	// // LogManager.ERROR);
	// utils.send(HttpMethod.GET, ShareDataTool.getUserlogin(context)
	// + "?username=" + sname + "&password=" + spwd, rp,
	// new RequestCallBack<String>() {
	// @Override
	// public void onStart() {
	// pro.setVisibility(View.VISIBLE);
	// super.onStart();
	// }
	//
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// pro.setVisibility(View.GONE);
	// ToastUtils.displayFailureToast(context);
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> arg0) {
	// pro.setVisibility(View.GONE);
	// Gson gson = new Gson();
	// try {
	// LogManager.LogShow("-----", arg0.result,
	// LogManager.ERROR);
	// JSONObject jsonObject = new JSONObject(arg0.result);
	// int state = jsonObject.getInt("state");
	// if (state == 1) {
	// UserInfoEntity infoEntity = gson.fromJson(
	// arg0.result, UserInfoEntity.class);
	// ShareDataTool.SaveInfo(context, infoEntity);
	// ShareDataTool.savePhone(context, sname);
	// handler.sendEmptyMessage(111);
	// d.dismiss();
	// } else if (state == 0) {
	//
	// ToastUtils.displayShortToast(context,
	// jsonObject.getJSONObject("message")
	// .getString("error"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// ToastUtils.displayFailureToast(context);
	// }
	//
	// }
	//
	// });
	// }
}

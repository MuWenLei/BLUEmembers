package com.mwl.bluemembers.utils;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * @author Mu
 * @date 2015-8-16上午7:31:51
 * @description 第三方登陆工具类
 */
public class ShareUtils {

	static String appID = "wxe87bcebb7d3818f7";
	static String appSecret = "e9f43079259dfd63c10a41ef61f23bfe";

	/**
	 * 第三方新浪授权
	 * 
	 * @param context
	 */
	public static void loginSina(final Context context, final Handler mhandler,
			final UMSocialService mController) {
		// String uid=null;
		// mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		mController.doOauthVerify(context, SHARE_MEDIA.SINA,
				new UMAuthListener() {
					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						if (value != null
								&& !TextUtils.isEmpty(value.getString("uid"))) {
							// Message aa = new Message();
							// aa.what = 25;
							// aa.obj = "xinlang" + value.getString("uid");
							// mhandler.sendMessage(aa);

							// 新浪获取用户信息
							mController.getPlatformInfo(context,
									SHARE_MEDIA.SINA, new UMDataListener() {
										@Override
										public void onStart() {
											Toast.makeText(context,
													"获取平台数据开始...",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void onComplete(int status,
												Map<String, Object> info) {
											if (status == 200 && info != null) {
												StringBuilder sb = new StringBuilder();
												Set<String> keys = info
														.keySet();
												for (String key : keys) {
													sb.append(key
															+ "="
															+ info.get(key)
																	.toString()
															+ "\r\n");
												}
												Log.d("TestData", sb.toString());
											} else {
												Log.d("TestData", "发生错误："
														+ status);
											}
										}
									});

						} else {
							ToastUtils.displayShortToast(context, "登陆失败");
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
					}

					@Override
					public void onStart(SHARE_MEDIA platform) {
					}
				});

	}

	/**
	 * 第三方QQ登陆
	 * 
	 * @param context
	 * @param handler
	 */
	public static void loginQQ(final Context context, final Handler mhandler,
			final UMSocialService mController) {
		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.login");
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context,
				"1103671901", "LmpibXAz1SXPwi38");
		qqSsoHandler.addToSocialSDK();
		mController.doOauthVerify(context, SHARE_MEDIA.QQ,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(context, "授权开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// if (value != null
						// && !TextUtils.isEmpty(value.getString("uid"))) {
						// // Message aa = new Message();
						// // aa.what = 26;
						// // aa.obj = "qq" + value.getString("uid");
						// // mhandler.sendMessage(aa);
						// } else {
						// ToastUtils.displayShortToast(context, "登陆失败");
						// }
						Toast.makeText(context, "授权完成", Toast.LENGTH_SHORT)
								.show();
						mController.getPlatformInfo(context, SHARE_MEDIA.QQ,
								new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(context, "获取平台数据开始...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key
														+ "="
														+ info.get(key)
																.toString()
														+ "\r\n");
											}
											Log.d("TestData", sb.toString());
										} else {
											Log.d("TestData", "发生错误：" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT)
								.show();
					}
				});

	}

	/**
	 * 第三方微信登录
	 * 
	 * @param context
	 * @param mhandler
	 */
	public static void loginWeixin(final Context context,
			final Handler mhandler, final UMSocialService mController) {
		// final UMSocialService mController = UMServiceFactory
		// .getUMSocialService("com.umeng.login");
		UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
		wxHandler.addToSocialSDK();
		mController.doOauthVerify(context, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(context, "授权开始", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						Toast.makeText(context, "授权完成", Toast.LENGTH_SHORT)
								.show();
						if (value != null
								&& !TextUtils.isEmpty(value.getString("uid"))) {
							Message aa = new Message();
							aa.what = 27;
							aa.obj = "weixin" + value.getString("uid");
							mhandler.sendMessage(aa);
						} else {
							ToastUtils.displayShortToast(context, "登陆失败");
						}
						// 获取相关授权信息
						mController.getPlatformInfo(context,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(context, "获取平台数据开始...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key
														+ "="
														+ info.get(key)
																.toString()
														+ "\r\n");
											}
											Log.d("TestData", sb.toString());
										} else {
											Log.d("TestData", "发生错误：" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	/**
	 * 注销新浪登录
	 * 
	 * @param context
	 * @param mhandler
	 * @param mController
	 */
	public void clearSina(final Context context, final Handler mhandler,
			final UMSocialService mController) {
		mController.deleteOauth(context, SHARE_MEDIA.SINA,
				new SocializeClientListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						if (status == 200) {
							Toast.makeText(context, "删除成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

	/**
	 * 注销QQ登录
	 * 
	 * @param context
	 * @param mhandler
	 * @param mController
	 */
	public void clearQQ(final Context context, final Handler mhandler,
			final UMSocialService mController) {
		mController.deleteOauth(context, SHARE_MEDIA.QQ,
				new SocializeClientListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						if (status == 200) {
							Toast.makeText(context, "删除成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

	/**
	 * 注销微信
	 * 
	 * @param context
	 * @param mhandler
	 * @param mController
	 */
	public void clearWeixin(final Context context, final Handler mhandler,
			final UMSocialService mController) {
		mController.deleteOauth(context, SHARE_MEDIA.WEIXIN,
				new SocializeClientListener() {
					@Override
					public void onStart() {
					}

					@Override
					public void onComplete(int status, SocializeEntity entity) {
						if (status == 200) {
							Toast.makeText(context, "删除成功.", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
	}

}

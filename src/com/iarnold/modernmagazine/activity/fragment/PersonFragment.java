package com.iarnold.modernmagazine.activity.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.google.gson.Gson;
import com.iarnold.modernmagazine.adapter.FMagazineAdapter;
import com.iarnold.modernmagazine.dialog.CustomeDialog;
import com.iarnold.modernmagazine.model.UserInfoEntity;
import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.MyApplication;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mining.app.zxing.decoding.FinishListener;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-17
 * @description 个人中心
 */
public class PersonFragment extends Fragment implements OnClickListener {

	private ImageView icon;

	private TextView nickName;

	private TextView userName;

	private ImageView score;

	private TextView scoretext;

	private RelativeLayout scorelin;

	private ImageView setting;

	private ImageView about;

	private View gv;

	private int width;

	private BitmapUtils bitmapUtils;

	private TextView logout;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 40:
				ShareDataTool.SaveInfo(getActivity(), null);
				ShareDataTool.savePhone(getActivity(), null);
				getActivity().finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_person, container, false);
		width = DensityUtil.getScreenWidth(getActivity());
		icon = (ImageView) view.findViewById(R.id.person_icon);
		nickName = (TextView) view.findViewById(R.id.person_name);
		userName = (TextView) view.findViewById(R.id.person_id);
		nickName = (TextView) view.findViewById(R.id.person_name);
		score = (ImageView) view.findViewById(R.id.person_score);
		scoretext = (TextView) view.findViewById(R.id.person_scoretext);
		scorelin = (RelativeLayout) view.findViewById(R.id.person_scorelin);
		setting = (ImageView) view.findViewById(R.id.person_setting);
		about = (ImageView) view.findViewById(R.id.person_about);
		gv = view.findViewById(R.id.person_gv);
		logout = (TextView) view.findViewById(R.id.person_logout);

		LayoutParams scoreParams = (LayoutParams) score.getLayoutParams();
		scoreParams.width = (int) (0.362 * width);
		scoreParams.height = (int) (0.362 * width);
		score.setLayoutParams(scoreParams);

		LayoutParams scorelinParams = (LayoutParams) scorelin.getLayoutParams();
		scorelinParams.width = (int) (0.362 * width);
		scorelinParams.height = (int) (0.362 * width);
		scorelinParams.leftMargin = (int) (0.116 * width);
		scorelin.setLayoutParams(scorelinParams);

		LayoutParams setParams = (LayoutParams) setting.getLayoutParams();
		setParams.width = (int) (0.362 * width);
		setParams.height = (int) (0.362 * width);
		setParams.rightMargin = (int) (0.116 * width);
		setting.setLayoutParams(setParams);

		LayoutParams aboutParams = (LayoutParams) about.getLayoutParams();
		aboutParams.width = (int) (0.362 * width);
		aboutParams.height = (int) (0.362 * width);
		aboutParams.leftMargin = (int) (0.116 * width);
		about.setLayoutParams(aboutParams);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bitmapUtils = new BitmapUtils(getActivity());
		bitmapUtils.configDefaultLoadingImage(R.drawable.a);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.a);
		score.setOnClickListener(this);
		setting.setOnClickListener(this);
		about.setOnClickListener(this);
		logout.setOnClickListener(this);
		setValues();
		// getInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_logout:
			CustomeDialog customeDialog = new CustomeDialog(getActivity(),
					handler, "确定要退出？", 0);
			break;
		case R.id.person_score:

			break;
		case R.id.person_setting:
			Intent it2 = new Intent(Intent.ACTION_VIEW);
			it2.setData(Uri.parse(ShareDataTool.getUsercenter(getActivity()))); // 这里面是需要调转的rul
			it2 = Intent.createChooser(it2, null);
			startActivity(it2);
			break;
		case R.id.person_about:
			Intent it = new Intent(Intent.ACTION_VIEW);
			it.setData(Uri.parse(ShareDataTool.getAbouthlife(getActivity()))); // 这里面是需要调转的rul
			it = Intent.createChooser(it, null);
			startActivity(it);

			break;

		default:
			break;
		}

	}

	private void getInfo() {
		RequestParams rp = new RequestParams();
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.GET, ShareDataTool.getUserinfo(getActivity())
				+ "?key=" + ShareDataTool.getKey(getActivity()), rp,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						gv.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.displayFailureToast(getActivity());
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Gson gson = new Gson();
						try {
							LogManager.LogShow("-----", arg0.result,
									LogManager.ERROR);
							JSONObject jsonObject = new JSONObject(arg0.result);
							int state = jsonObject.getInt("state");
							if (state == 1) {
								UserInfoEntity infoEntity = gson.fromJson(
										arg0.result, UserInfoEntity.class);
								ShareDataTool.SaveInfo(getActivity(),
										infoEntity);

								gv.setVisibility(View.VISIBLE);

							} else if (state == 0) {
								ToastUtils.displayShortToast(getActivity(),
										"个人信息获取失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
							ToastUtils.displayFailureToast(getActivity());
						}

					}

				});
	}

	private void setValues() {
		bitmapUtils.display(icon, ShareDataTool.getFace(getActivity()));
		nickName.setText(ShareDataTool.getNickname(getActivity()));
		userName.setText("id:" + ShareDataTool.getUsername(getActivity()));
		String fontPath = "fonts/Modern.ttf";
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
				fontPath);
		scoretext.setTypeface(tf);
		scoretext.setText(String.valueOf(ShareDataTool
				.getIntegral(getActivity())));
	}

}

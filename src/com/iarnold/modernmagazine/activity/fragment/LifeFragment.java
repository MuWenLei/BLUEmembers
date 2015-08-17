package com.iarnold.modernmagazine.activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.google.gson.Gson;
import com.iarnold.modernmagazine.activity.Car4sMapActivity;
import com.iarnold.modernmagazine.activity.HomeActivity;
import com.iarnold.modernmagazine.activity.QRActivity;
import com.iarnold.modernmagazine.activity.WebActivity;
import com.iarnold.modernmagazine.model.LifeEntity;
import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.MyApplication;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-17
 * @description 车主生活
 */
public class LifeFragment extends Fragment implements OnClickListener {

	private ImageView car4s;

	private ImageView qr;

	private ImageView park;

	private ImageView sina;

	private ImageView help;

	private ImageView edm;

	private int width;

	private String lifeUrl;

	private LifeEntity entity = null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_life, container, false);
		lifeUrl = ShareDataTool.getOwnerlife(getActivity());
		getInfo();
		width = DensityUtil.getScreenWidth(getActivity());
		car4s = (ImageView) view.findViewById(R.id.life_car4s);
		qr = (ImageView) view.findViewById(R.id.life_qr);
		park = (ImageView) view.findViewById(R.id.life_p);
		sina = (ImageView) view.findViewById(R.id.life_sina);
		help = (ImageView) view.findViewById(R.id.life_help);
		edm = (ImageView) view.findViewById(R.id.life_edm);

		LayoutParams car4sParams = (LayoutParams) car4s.getLayoutParams();
		car4sParams.width = (int) (0.362 * width);
		car4sParams.height = (int) (0.362 * width);
		car4sParams.leftMargin = (int) (0.116 * width);
		car4s.setLayoutParams(car4sParams);

		LayoutParams qrParams = (LayoutParams) qr.getLayoutParams();
		qrParams.width = (int) (0.362 * width);
		qrParams.height = (int) (0.362 * width);
		qrParams.rightMargin = (int) (0.116 * width);
		qr.setLayoutParams(qrParams);

		LayoutParams parkParams = (LayoutParams) park.getLayoutParams();
		parkParams.width = (int) (0.362 * width);
		parkParams.height = (int) (0.362 * width);
		parkParams.leftMargin = (int) (0.116 * width);
		park.setLayoutParams(parkParams);

		LayoutParams sinaParams = (LayoutParams) sina.getLayoutParams();
		sinaParams.width = (int) (0.362 * width);
		sinaParams.height = (int) (0.362 * width);
		sinaParams.rightMargin = (int) (0.116 * width);
		sina.setLayoutParams(sinaParams);

		LayoutParams helpParams = (LayoutParams) help.getLayoutParams();
		helpParams.width = (int) (0.362 * width);
		helpParams.height = (int) (0.362 * width);
		helpParams.leftMargin = (int) (0.116 * width);
		help.setLayoutParams(helpParams);

		LayoutParams edmParams = (LayoutParams) edm.getLayoutParams();
		edmParams.width = (int) (0.362 * width);
		edmParams.height = (int) (0.362 * width);
		edmParams.rightMargin = (int) (0.116 * width);
		edm.setLayoutParams(edmParams);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		car4s.setOnClickListener(this);
		qr.setOnClickListener(this);
		park.setOnClickListener(this);
		sina.setOnClickListener(this);
		help.setOnClickListener(this);
		edm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (entity == null) {
			entity = new LifeEntity();
			entity.s = "4s店";
			entity.p = "停车场";
			entity.sina = "http://weibo.com/bjhyundai";
			entity.help = "4008001100";
			entity.edm = "http://club-beijing-hyundai.com/edm";
		}
		switch (v.getId()) {
		case R.id.life_car4s:
			Intent intent = new Intent(getActivity(), Car4sMapActivity.class);
			intent.putExtra("key", entity.s);
			intent.putExtra("index", 0);
			startActivity(intent);
			break;
		case R.id.life_qr:
			Intent intent2 = new Intent(getActivity(), QRActivity.class);
			startActivity(intent2);
			break;
		case R.id.life_p:
			Intent intent3 = new Intent(getActivity(), Car4sMapActivity.class);
			intent3.putExtra("key", entity.p);
			intent3.putExtra("index", 1);
			startActivity(intent3);
			break;
		case R.id.life_sina:
			Intent intent4 = new Intent(getActivity(), WebActivity.class);
			intent4.putExtra("url", entity.sina);
			startActivity(intent4);
			break;
		case R.id.life_help:
			Intent intent5 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ entity.help));
			startActivity(intent5);
			break;
		case R.id.life_edm:
			Intent intent6 = new Intent(getActivity(), WebActivity.class);
			intent6.putExtra("url", entity.edm);
			startActivity(intent6);
			break;

		default:
			break;
		}
	}

	/**
	 * 获取首页的信息
	 */
	private void getInfo() {
		RequestParams rp = new RequestParams();
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.GET, lifeUrl, new RequestCallBack<String>() {
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
					LogManager.LogShow("----", arg0.result, LogManager.ERROR);
					entity = gson.fromJson(arg0.result, LifeEntity.class);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		});
	}

}

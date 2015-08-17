package com.iarnold.modernmagazine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarnold.modernmagazine.adapter.DetailAdapter;
import com.iarnold.modernmagazine.model.DetailEntity;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.view.CustomListView;
import com.iarnold.modernmagazine.view.PullToRefreshLayout;
import com.iarnold.modernmagazine.view.CustomListView.OnLoadMoreListener;
import com.iarnold.modernmagazine.view.PullToRefreshLayout.OnRefreshListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-21
 * @description 列表页面
 */
public class DetailActivity extends BaseActivity implements OnRefreshListener {

	private ImageView back;

	private ListView listView;

	private PullToRefreshLayout ptrl;

	private View gv;

	private View pro;

	private List<DetailEntity> entities;

	private DetailAdapter adapter;

	private String url;// 网络请求url

	private int pageNo = 0;

	private boolean proShow = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		url = getIntent().getStringExtra("url");
		if (url == null) {
			url = "";
		}
		initView();
		getList(0);
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		listView = (ListView) findViewById(R.id.detail_listview);
		ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_listview);
		gv = findViewById(R.id.detial_gv);
		pro = findViewById(R.id.detail_pro);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		entities = new ArrayList<DetailEntity>();
		adapter = new DetailAdapter(this, entities);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent3 = new Intent(DetailActivity.this,
						WebActivity.class);
				intent3.putExtra("url", entities.get(position).url);
				startActivity(intent3);
				// Intent it = new Intent(Intent.ACTION_VIEW);
				// it.setData(Uri.parse(entities.get(position).url)); //
				// 这里面是需要调转的rul
				// it = Intent.createChooser(it, null);
				// startActivity(it);
			}
		});

		ptrl.setOnRefreshListener(this);

	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		closePro();
		pageNo = 0;
		entities.clear();
		adapter.notifyDataSetChanged();
		getList(1);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pageNo++;
		closePro();
		getList(2);
	}

	public void closePro() {
		proShow = false;
	}

	public void openPro() {
		proShow = true;
	}

	/**
	 * 联网获取列表信息
	 * 
	 * @param style
	 *            0代表首次刷新 1代表上拉刷新 2代表下拉刷新
	 */
	private void getList(final int style) {
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.POST, url + "?page=" + pageNo,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						if (proShow) {
							pro.setVisibility(View.VISIBLE);
							gv.setVisibility(View.GONE);
						} else {
							pro.setVisibility(View.GONE);
							gv.setVisibility(View.VISIBLE);
						}
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(DetailActivity.this);
						if (style == 1) {
							ptrl.refreshFinish(PullToRefreshLayout.FAIL);
						} else if (style == 2) {
							ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						try {
							Gson gson = new Gson();
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							java.lang.reflect.Type type = new TypeToken<List<DetailEntity>>() {
							}.getType();
							List<DetailEntity> detailEntities = gson.fromJson(
									arg0.result, type);
							if (detailEntities != null
									&& detailEntities.size() != 0) {
								for (int i = 0; i < detailEntities.size(); i++) {
									entities.add(detailEntities.get(i));
								}
								adapter.notifyDataSetChanged();
								if (style == 1) {
									ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
								} else if (style == 2) {
									ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
								}
								gv.setVisibility(View.VISIBLE);
							} else {
								if (style == 1) {
									ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
								} else if (style == 2) {
									ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
								}
							}

						} catch (Exception e) {
							ToastUtils
									.displaySendFailureToast(DetailActivity.this);
							if (style == 1) {
								ptrl.refreshFinish(PullToRefreshLayout.FAIL);
							} else if (style == 2) {
								ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
							}
						}

					}
				});

	}

}

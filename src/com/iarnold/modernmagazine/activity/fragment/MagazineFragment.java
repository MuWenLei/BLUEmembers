package com.iarnold.modernmagazine.activity.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iarnold.modernmagazine.adapter.FMagazineAdapter;
import com.iarnold.modernmagazine.download.DownloadInfo;
import com.iarnold.modernmagazine.download.DownloadManager;
import com.iarnold.modernmagazine.download.DownloadService;
import com.iarnold.modernmagazine.utils.DensityUtil;
import com.iarnold.modernmagazine.utils.LogManager;
import com.iarnold.modernmagazine.utils.MyApplication;
import com.iarnold.modernmagazine.utils.ShareDataTool;
import com.iarnold.modernmagazine.utils.ToastUtils;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.iarnold.modernmagazine.view.PullToRefreshLayout;
import com.iarnold.modernmagazine.view.PullToRefreshLayout.OnRefreshListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.iarnold.modernmagazine.R;

/**
 * @author Mu
 * @date 2015-6-17
 * @description 鐢靛瓙鏉傚織fragment
 */
public class MagazineFragment extends Fragment implements OnRefreshListener {

	private GridView gridView;

	private FMagazineAdapter adapter;

	private String magaUrl;

	private List<List<String>> magaList;

	private View pro;

	private DownloadManager downloadManager;

	// private Context mAppContext;

	private int width;

	private int pageNo = 0;

	private boolean proShow = true;

	private PullToRefreshLayout ptrl;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 40:
				int position = msg.arg1;
				List<DownloadInfo> downloadInfos = downloadManager
						.getDownloadInfos();
				DownloadInfo downloadInfo = null;
				if (downloadInfos != null) {
					for (int i = 0; i < downloadInfos.size(); i++) {

						if (downloadInfos.get(i).getDownloadUrl()
								.equals(magaList.get(position).get(2))) {
							downloadInfo = downloadInfos.get(i);
						}

					}

					try {
						if (downloadInfo != null) {
							ToosUtils.deleteFile(new File(downloadInfo.getFileSavePath()));
							downloadManager.removeDownload(downloadInfo);
							
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				;
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_magazine, container,
				false);
		gridView = (GridView) view.findViewById(R.id.fmagazine);
		ptrl = (PullToRefreshLayout) view.findViewById(R.id.refresh_gridview);
		pro = view.findViewById(R.id.fmagazine_pro);
		magaUrl = ShareDataTool.getMagazine(getActivity());

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		downloadManager = DownloadService.getDownloadManager(getActivity()
				.getApplicationContext());
		magaList = new ArrayList<List<String>>();
		width = DensityUtil.getScreenWidth(getActivity());

		// downloadManager.getDownloadInfos();

		adapter = new FMagazineAdapter(getActivity(), magaList,
				downloadManager, width, gridView, handler);
		gridView.setAdapter(adapter);
		getInfo(0);

		ptrl.setOnRefreshListener(this);

		String[] m = ToosUtils.ListFile();
		List<String> paths = null;
		if (m != null && m.length!=0) {
			paths = Arrays.asList(m);
		} else {
			paths = new ArrayList<String>();
		}
		List<DownloadInfo> deldownloadInfos = new ArrayList<DownloadInfo>();
		List<DownloadInfo> downloadInfos = downloadManager.getDownloadInfos();
		if (downloadInfos != null) {
			for (int i = 0; i < downloadInfos.size(); i++) {

				if (!paths.contains(downloadInfos.get(i).getFileSavePath())) {
					// try {
					// LogManager.LogShow("====", downloadInfos.get(i)
					// .getFileSavePath(), LogManager.ERROR);
					deldownloadInfos.add(downloadInfos.get(i));
					// downloadManager.removeDownload(downloadInfos.get(i));
					// } catch (DbException e) {
					// e.printStackTrace();
					// }
				}

			}

			try {
				downloadManager.removeDownloads(deldownloadInfos);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		closePro();
		pageNo = 0;
		magaList.clear();
		adapter.notifyDataSetChanged();
		getInfo(1);

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pageNo++;
		closePro();
		getInfo(2);
	}

	public void closePro() {
		proShow = false;
	}

	public void openPro() {
		proShow = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		try {
			if (adapter != null && downloadManager != null) {
				downloadManager.backupDownloadInfoList();
			}
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
		}
		super.onDestroy();
	}

	/**
	 * 鑾峰彇棣栭〉鐨勪俊鎭�
	 * 
	 * @param style
	 *            0代表首次刷新 1代表上拉刷新 2代表下拉刷新
	 */
	private void getInfo(final int style) {
		HttpUtils utils = new HttpUtils();
		utils.configTimeout(20000);
		utils.send(HttpMethod.GET, magaUrl + "?page=" + pageNo,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						if (proShow) {
							pro.setVisibility(View.VISIBLE);
						} else {
							pro.setVisibility(View.GONE);
						}
						super.onStart();
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pro.setVisibility(View.GONE);
						ToastUtils.displayFailureToast(getActivity());
						if (style == 1) {
							ptrl.refreshFinish(PullToRefreshLayout.FAIL);
						} else if (style == 2) {
							ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						pro.setVisibility(View.GONE);
						Gson gson = new Gson();
						try {
							LogManager.LogShow("----", arg0.result,
									LogManager.ERROR);
							LogManager.LogShow("----", String.valueOf(pageNo),
									LogManager.ERROR);
							java.lang.reflect.Type type = new TypeToken<List<List<String>>>() {
							}.getType();
							if (!ToosUtils.isStringEmpty(arg0.result)) {
								List<List<String>> lists = gson.fromJson(
										arg0.result, type);
								for (int i = 0; i < lists.size(); i++) {
									magaList.add(lists.get(i));
								}
								adapter.notifyDataSetChanged();
								if (style == 1) {
									ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
								} else if (style == 2) {
									ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
								}
							} else {
								if (style == 1) {
									ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
								} else if (style == 2) {
									ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
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

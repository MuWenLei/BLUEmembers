package com.mwl.bluemembers.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.mwl.bluemembers.R;
import com.mwl.bluemembers.dialog.CustomeDialog;
import com.mwl.bluemembers.download.DownloadInfo;
import com.mwl.bluemembers.download.DownloadManager;
import com.mwl.bluemembers.utils.DensityUtil;
import com.mwl.bluemembers.utils.LogManager;
import com.mwl.bluemembers.utils.ToastUtils;
import com.mwl.bluemembers.utils.ToosUtils;
import com.mwl.bluemembers.view.RoundProgressBar;

/**
 * @author Mu
 * @date 2015-6-17
 * @description 电子杂志适配器
 */
public class FMagazineAdapter extends BaseAdapter {

	private Context context;
	private List<List<String>> list;
	private BitmapUtils bitmapUtils;
	private DownloadManager downloadManager;
	private int width;
	private GridView gridView;
	private Handler handler;

	public FMagazineAdapter(Context context, List<List<String>> list,
			DownloadManager downloadManager, int width, GridView gridView,
			Handler handler) {
		super();
		this.context = context;
		this.list = list;
		this.width = width;
		this.gridView = gridView;
		this.handler = handler;
		this.downloadManager = downloadManager;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.zzfm_0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		DownloadInfo downloadInfo = null;
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.fmagazine_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.fragment_magazine_image);
			holder.textView = (TextView) convertView
					.findViewById(R.id.fragment_magazine_text);
			holder.progressBar = (RoundProgressBar) convertView
					.findViewById(R.id.fragment_magazine_roundProgressBar);
			holder.arrows = (ImageView) convertView
					.findViewById(R.id.fragment_magazine_arrows);
			holder.clickLin = convertView
					.findViewById(R.id.fragment_magazine_cilcklin);
			holder.del = (ImageView) convertView
					.findViewById(R.id.fragment_magazine_del);
			convertView.setTag(holder);
			// holder.refresh();
		} else {
			holder = (ViewHolder) convertView.getTag();
			// holder.update(position);
		}

		bitmapUtils.display(holder.imageView, list.get(position).get(0));
		holder.textView.setText(list.get(position).get(1));

		LayoutParams imageParams = (LayoutParams) holder.imageView
				.getLayoutParams();
		imageParams.width = (int) ((width - DensityUtil.dip2px(context, 72)) / 2);
		imageParams.height = (int) ((width - DensityUtil.dip2px(context, 72)) / 2 * 1.3125);
		holder.imageView.setLayoutParams(imageParams);
		//
		List<DownloadInfo> downloadInfos = downloadManager.getDownloadInfos();
		for (int i = 0; i < downloadInfos.size(); i++) {
			if (downloadInfos.get(i).getDownloadUrl()
					.equals(list.get(position).get(2))) {
				downloadInfo = downloadInfos.get(i);
			}
		}

		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.downimage);

		if (downloadInfo != null) {
			LogManager.LogShow("----", "aaaa" + downloadInfo.getFileLength(),
					LogManager.ERROR);
			HttpHandler.State state = downloadInfo.getState();
			switch (state) {
			case WAITING:
			case STARTED:
			case LOADING:
				holder.arrows.startAnimation(animation);
				if (downloadInfo.getFileLength() != 0) {
					holder.progressBar
							.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo
									.getFileLength()));
				} else {
					holder.progressBar.setProgress(0);
				}
				//
				holder.arrows.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.del.setVisibility(View.INVISIBLE);
				break;
			case CANCELLED:
			case FAILURE:
				holder.arrows.clearAnimation();
				if (downloadInfo.getFileLength() != 0) {
					holder.progressBar
							.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo
									.getFileLength()));
				} else {
					holder.progressBar.setProgress(0);
				}
				holder.arrows.setVisibility(View.VISIBLE);
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.del.setVisibility(View.INVISIBLE);
				break;
			case SUCCESS:
				holder.arrows.clearAnimation();
				holder.arrows.setVisibility(View.GONE);
				holder.progressBar.setVisibility(View.GONE);
				holder.del.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}

			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				RequestCallBack callBack = handler.getRequestCallBack();
				if (callBack instanceof DownloadManager.ManagerCallBack) {
					DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
					if (managerCallBack.getBaseCallBack() == null) {
						managerCallBack
								.setBaseCallBack(new DownloadRequestCallBack());
					}
				}
				callBack.setUserTag(new WeakReference<List<String>>(list
						.get(position)));
			}
		} else {
			holder.arrows.clearAnimation();
			holder.arrows.setVisibility(View.VISIBLE);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.del.setVisibility(View.INVISIBLE);
			holder.progressBar.setProgress(0);
		}

		holder.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomeDialog dialog = new CustomeDialog(context, handler,
						"确定要删除此杂志？", position);
			}
		});

		holder.arrows.setTag("arrows" + list.get(position).get(2));
		holder.progressBar.setTag("progressBar" + list.get(position).get(2));
		holder.del.setTag("del" + list.get(position).get(2));

		holder.clickLin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DownloadInfo ondownloadInfo = null;
				List<DownloadInfo> ondownloadInfos = downloadManager
						.getDownloadInfos();
				for (int i = 0; i < ondownloadInfos.size(); i++) {
					if (ondownloadInfos.get(i).getDownloadUrl()
							.equals(list.get(position).get(2))) {
						ondownloadInfo = ondownloadInfos.get(i);
					}
				}
				if (ondownloadInfo != null) {
					HttpHandler.State state = ondownloadInfo.getState();
					switch (state) {
					case WAITING:
					case STARTED:
					case LOADING:
						try {
							downloadManager.stopDownload(ondownloadInfo);
						} catch (DbException e) {
							LogUtils.e(e.getMessage(), e);
						}
						break;
					case CANCELLED:
					case FAILURE:
						try {
							downloadManager.resumeDownload(ondownloadInfo,
									new DownloadRequestCallBack());
						} catch (DbException e) {
							LogUtils.e(e.getMessage(), e);
						}
						notifyDataSetChanged();
						break;
					case SUCCESS:

						SharedPreferences sharedPrefs = PreferenceManager
								.getDefaultSharedPreferences(context);
						sharedPrefs.edit().putString("prefKeyLanguage", "en")
								.commit();

						File fi = new File(ondownloadInfo.getFileSavePath());
						Uri uri = Uri.parse(fi.getAbsolutePath());
						Intent intent = new Intent(context,
								com.artifex.mupdflib.MuPDFActivity.class);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(uri);
						intent.putExtra("password", "encrypted PDF password");
						intent.putExtra("linkhighlight", true);
						intent.putExtra("idleenabled", false);
						intent.putExtra("horizontalscrolling", true);
						intent.putExtra("docname", "PDF document name");

						context.startActivity(intent);
						// 成功调到页面
						break;
					default:
						break;
					}

				} else {
					// 添加下载列表
					String[] strings = list.get(position).get(2).split("/");
					String target = "/sdcard/xUtils/"
							+ strings[strings.length - 1];
					try {
						downloadManager.addNewDownload(list.get(position)
								.get(2), strings[strings.length - 1], target,
								true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
								false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
								new DownloadRequestCallBack());
					} catch (DbException e) {
						LogUtils.e(e.getMessage(), e);
					}
					notifyDataSetChanged();
				}

			}
		});

		return convertView;
	}

	private class DownloadRequestCallBack extends RequestCallBack<File> {

		List<String> viewList = null;

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			// String temp=
			WeakReference<List<String>> tag = (WeakReference<List<String>>) userTag;
			viewList = tag.get();
			String temp = viewList.get(2);
			// ViewHolder holder = tag.get();
			if (!ToosUtils.isStringEmpty(temp)) {
				LogManager.LogShow("----", temp, LogManager.ERROR);
				ImageView imageView = (ImageView) gridView
						.findViewWithTag("arrows" + temp);
				RoundProgressBar progressBar = (RoundProgressBar) gridView
						.findViewWithTag("progressBar" + temp);
				ImageView del = (ImageView) gridView.findViewWithTag("del"
						+ temp);

				if (imageView != null && progressBar != null) {
					refresh(imageView, progressBar, del, temp);
				}

				// holder.refresh();
			}
		}

		@Override
		public void onStart() {
			refreshListItem();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			refreshListItem();
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			if (viewList != null) {
				ToastUtils
						.displayShortToast(context, viewList.get(1) + "下载成功！");
			}

			refreshListItem();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			if (viewList != null) {
				ToastUtils
						.displayShortToast(context, viewList.get(1) + "下载失败！");
			}
			refreshListItem();
		}

		@Override
		public void onCancelled() {
			// ToastUtils.displayShortToast(context, "下载取消！");
			refreshListItem();
		}

		public void refresh(ImageView arrows, RoundProgressBar progressBar,
				ImageView del, String temp) {
			DownloadInfo downloadInfo = null;
			List<DownloadInfo> ondownloadInfos = downloadManager
					.getDownloadInfos();
			for (int i = 0; i < ondownloadInfos.size(); i++) {
				if (ondownloadInfos.get(i).getDownloadUrl().equals(temp)) {
					downloadInfo = ondownloadInfos.get(i);
				}
			}
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.downimage);

			if (downloadInfo != null) {
				LogManager
						.LogShow("----", "aaaa" + downloadInfo.getFileLength(),
								LogManager.ERROR);
				HttpHandler.State state = downloadInfo.getState();
				switch (state) {
				case WAITING:
				case STARTED:
				case LOADING:
					arrows.startAnimation(animation);
					if (downloadInfo.getFileLength() != 0) {
						progressBar.setProgress((int) (downloadInfo
								.getProgress() * 100 / downloadInfo
								.getFileLength()));
					} else {
						progressBar.setProgress(0);
					}
					//
					arrows.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.VISIBLE);
					del.setVisibility(View.INVISIBLE);
					break;
				case CANCELLED:
				case FAILURE:
					arrows.clearAnimation();
					if (downloadInfo.getFileLength() != 0) {
						progressBar.setProgress((int) (downloadInfo
								.getProgress() * 100 / downloadInfo
								.getFileLength()));
					} else {
						progressBar.setProgress(0);
					}
					arrows.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.VISIBLE);
					del.setVisibility(View.INVISIBLE);
					break;
				case SUCCESS:
					arrows.clearAnimation();
					arrows.setVisibility(View.GONE);
					progressBar.setVisibility(View.GONE);
					del.setVisibility(View.VISIBLE);
					notifyDataSetChanged();
					break;
				default:
					break;
				}

			} else {
				arrows.clearAnimation();
				arrows.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				del.setVisibility(View.INVISIBLE);
			}
		}
	}

	class ViewHolder {

		public ImageView imageView;
		public TextView textView;
		public RoundProgressBar progressBar;
		public ImageView arrows;
		public View clickLin;
		public ImageView del;

	}

}

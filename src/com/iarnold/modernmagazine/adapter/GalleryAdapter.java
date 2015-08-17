package com.iarnold.modernmagazine.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.baidu.location.am;
import com.baidu.platform.comapi.map.u;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.BitmapUtils;
import com.iarnold.modernmagazine.R;

public class GalleryAdapter extends BaseAdapter {
	private Context context;
	private Gallery gallery;
	private List<List<String>> urls;
	private int width;
	private BitmapUtils utils;

	public GalleryAdapter(Context context, Gallery gallery,
			List<List<String>> urls, int width) {
		this.context = context;
		this.gallery = gallery;
		this.urls = urls;
		this.width = width;
		utils = new BitmapUtils(context);
		utils.configDefaultLoadingImage(R.drawable.main_image);
		utils.configDefaultLoadFailedImage(R.drawable.main_image);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageResource(R.drawable.main_image);
		imageView.setLayoutParams(new Gallery.LayoutParams(width,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
//		if (!ToosUtils.isStringEmpty(urls.get(position % urls.size()).get(1))) {
//			imageView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					Intent it = new Intent(Intent.ACTION_VIEW);
//
//					it.setData(Uri.parse(urls.get(position % urls.size())
//							.get(1))); // 这里面是需要调转的rul
//
//					it = Intent.createChooser(it, null);
//
//					context.startActivity(it);
//				}
//			});
//		}

		utils.display(imageView, urls.get(position % urls.size()).get(0));
		return imageView;
	}
}

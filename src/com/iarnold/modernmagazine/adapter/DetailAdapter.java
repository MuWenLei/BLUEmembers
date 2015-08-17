package com.iarnold.modernmagazine.adapter;

import java.util.List;

import com.iarnold.modernmagazine.model.DetailEntity;
import com.iarnold.modernmagazine.utils.ToosUtils;
import com.lidroid.xutils.BitmapUtils;
import com.iarnold.modernmagazine.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter {

	private Context context;
	private List<DetailEntity> entities;
	private BitmapUtils bitmapUtils;

	public DetailAdapter(Context context, List<DetailEntity> entities) {
		super();
		this.context = context;
		this.entities = entities;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.activity_detail_item,
					null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.detail_item_image);
			holder.tip = (TextView) convertView
					.findViewById(R.id.detail_item_tip);
			holder.info = (TextView) convertView
					.findViewById(R.id.detail_item_info);
			holder.content = (TextView) convertView
					.findViewById(R.id.detail_item_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		bitmapUtils.display(holder.image, entities.get(position).img);
		if (!ToosUtils.isStringEmpty(entities.get(position).title)) {
			holder.tip.setText(entities.get(position).title);
		}
		if (!ToosUtils.isStringEmpty(entities.get(position).content)) {
			holder.content.setText(entities.get(position).content);
		}
		
		if (!ToosUtils.isStringEmpty(entities.get(position).info)) {
			holder.info.setText(entities.get(position).info);
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView tip;
		public TextView content;
		public TextView info;
	}

}

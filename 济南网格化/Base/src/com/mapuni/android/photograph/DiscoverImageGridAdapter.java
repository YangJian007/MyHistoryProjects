package com.mapuni.android.photograph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuni.android.base.R;
import com.mapuni.android.photograph.BitmapCache.ImageCallback;

public class DiscoverImageGridAdapter extends BaseAdapter {

	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	Activity act;
	List<ImageItem> dataList;
	public Map<String, String> map = new HashMap<String, String>();
	BitmapCache cache;
	private Handler mHandler;
	private int selectTotal = 0;
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public DiscoverImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
		this.act = act;
		dataList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	// private List<String> newSelectPic = new ArrayList<String>();
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.discover_image_grid_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, callback);
		String path = dataList.get(position).imagePath;
		if ((item.isSelected && BimpHelper.drr.contains(path)) || map.containsKey(path)) {
			item.isSelected = true;
			holder.selected.setImageResource(R.drawable.icon_data_select);
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			item.isSelected = false;
			holder.selected.setImageResource(-1);
			holder.text.setBackgroundColor(0x00000000);
		}
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;
				if (BimpHelper.drr.contains(path)) {
					item.isSelected = true;
					holder.selected.setImageResource(R.drawable.icon_data_select);
					holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
				} else {
					if ((BimpHelper.drr.size() + selectTotal) < Cameras.num) {
						item.isSelected = !item.isSelected;
						if (item.isSelected) {
							holder.selected.setImageResource(R.drawable.icon_data_select);
							holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
							selectTotal++;
							if (textcallback != null)
								textcallback.onListen(BimpHelper.drr.size() + selectTotal);
							map.put(path, path);

						} else if (!item.isSelected) {
							holder.selected.setImageResource(-1);
							holder.text.setBackgroundColor(0x00000000);
							selectTotal--;
							if (textcallback != null)
								textcallback.onListen(BimpHelper.drr.size() + selectTotal);
							map.remove(path);
						}
					} else if ((BimpHelper.drr.size() + selectTotal) >= Cameras.num) {
						if (item.isSelected == true) {
							item.isSelected = !item.isSelected;
							holder.selected.setImageResource(-1);
							selectTotal--;
							map.remove(path);
							if (textcallback != null)
								textcallback.onListen(BimpHelper.drr.size() + selectTotal);
						} else {
							Message message = Message.obtain(mHandler, 0);
							message.sendToTarget();
						}
					}
				}

			}

		});

		return convertView;
	}
}

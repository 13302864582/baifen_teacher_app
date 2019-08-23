package com.ucuxin.ucuxin.tec.function.teccourse.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageUtils;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CoursePageModel;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

public class UpLoadClassAdapter extends BaseAdapter {
	public static final String ADD_IMAGE_TAG = "add_image_tag";
	private Context context;
	private List<CoursePageModel> mPageList = new ArrayList<CoursePageModel>();
	private OnImageDeleteClickListener mOnImageDeleteClickListener;
	private int imageSize;

	public UpLoadClassAdapter(Context context, List<CoursePageModel> pageList, OnImageDeleteClickListener listener) {
		this.context = context;
		this.mPageList = pageList;
		this.mOnImageDeleteClickListener = listener;
		imageSize = context.getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
	}

	public void setData(List<CoursePageModel> pageList) {
		this.mPageList = pageList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPageList == null ? 0 : mPageList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mPageList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int pos, View convertView, ViewGroup arg2) {
		// long s = System.currentTimeMillis();
		if (null == convertView) {
			convertView = View.inflate(context, R.layout.upload_class_img_item, null);
		}
		final NetworkImageView imgView = (NetworkImageView) convertView.findViewById(R.id.home_work_image_iv);
		ImageView deleteView = (ImageView) convertView.findViewById(R.id.home_work_delete_iv);

		CoursePageModel pageModel = mPageList.get(pos);
		String path = pageModel.getThumbnail();
		if (null != path) {
//			Bitmap bm;
			if (path.equals(ADD_IMAGE_TAG)) {
				deleteView.setVisibility(View.GONE);
				// holder.imageIV.setImageResource(R.drawable.bg_add_photo_selector);
//				Bitmap bm = BitmapFactory.decodeResource(WApplication.getContext().getResources(),
//						R.drawable.btn_photo);
//				
//				imgView.setCustomBitmap(bm);
//				
				ImageLoader.getInstance().loadImage("", imgView, R.drawable.bg_add_photo_selector);
			} else {
				deleteView.setVisibility(View.VISIBLE);
				imgView.setVisibility(View.VISIBLE);
				if (path.equals("")) {
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
					ToastUtils.show("服务器返回地址为:" + path);
				}
				ImageLoader.getInstance().loadImage(path, imgView, R.drawable.ic_launcher,
						new OnImageLoadListener() {

							@Override
							public void onSuccess(ImageContainer response) {
								if (response != null) {
									Bitmap bitmap = response.getBitmap();
									if (null != bitmap) {
										bitmap = ImageUtils.corner(bitmap, imageSize / 10, imageSize);
										imgView.setCustomBitmap(bitmap);
										imgView.invalidate();
									}
								}
							}

							@Override
							public void onFailed(VolleyError error) {

							}
						});

			}
		} else {
			deleteView.setVisibility(View.GONE);
		}

		deleteView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != mOnImageDeleteClickListener) {
					mOnImageDeleteClickListener.onDeleteClick(pos);
				}
			}
		});
		// LogUtils.d("yh", "time=" + (System.currentTimeMillis() - s));
		return convertView;
	}


	public interface OnImageDeleteClickListener {
		void onDeleteClick(int pos);
	}
}

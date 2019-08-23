package com.ucuxin.ucuxin.tec.function.homework;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;

public class HomeWorkReadOnlyDetailItemFragment extends BaseFragment implements OnClickListener {

	private int window_height, window_width;
	private RelativeLayout divParentLayout;
	private DragImageView mDragImageView;
	private String path;

	public static final String TAG = HomeWorkReadOnlyDetailItemFragment.class.getSimpleName();

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showData();
				break;
			}
		}
	};

	public HomeWorkReadOnlyDetailItemFragment(String path) {
		this.path = path;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = View.inflate(getActivity(), R.layout.view_homework_readonly_item, null);
		divParentLayout = (RelativeLayout) v.findViewById(R.id.div_parent_layout);
		mDragImageView = (DragImageView) v.findViewById(R.id.homework_div);
		divParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = divParentLayout.getHeight();
					window_width = divParentLayout.getWidth();
					mDragImageView.setScreenSize(window_width, window_height);
				}
			}
		});
		mHandler.sendMessageDelayed(mHandler.obtainMessage(1), 10);
		return v;
	}

	public void showData() {
		if (path.startsWith("http://")) {
			ImageLoader.getInstance().loadImage(path, mDragImageView, R.drawable.loading);
		} else if(path.startsWith("https://")) {
			ImageLoader.getInstance().loadImage(path, mDragImageView, R.drawable.loading);
		} else if (path.startsWith("/")) {
			try {
				Bitmap bm = BitmapFactory.decodeFile(path);
				// mDragImageView.setImageBitmap(bm);
				mDragImageView.setCustomBitmap(bm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void goBack() {
		mActivity.finish();
	}

	@Override
	public void onPause() {
		super.onPause();
		MediaUtil.getInstance(false).stopPlay();
	}

	public interface onScaleListener {
		void onScale(boolean isScale);
	}

    @Override
    public void initView(View view) {

        
    }

    @Override
    public void initListener() {

        
    }
}

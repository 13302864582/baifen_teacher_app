package com.ucuxin.ucuxin.tec.function.homework.student;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.MyOrientationEventListener;
import com.ucuxin.ucuxin.tec.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.view.AddPointCommonView;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;

import androidx.fragment.app.Fragment;

public class StuHomeWorkCheckDetailItemFragment extends Fragment implements OnOrientationChangedListener {
	private AddPointCommonView mAddPointView;
	private StuPublishHomeWorkPageModel pageModel;
	private RelativeLayout divParentLayout;
	private DragImageView mPicIv;
	private int window_height, window_width;

	private MyOrientationEventListener moraientation;
	private Handler mHandler = new Handler () {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				String path = msg.obj.toString();
				if (path != null && null != mAddPointView) {
					mAddPointView.setPagePic(path, false);
				}
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.homework_check_detail_pager_item, null);
		Bundle arguments = getArguments();
		pageModel = (StuPublishHomeWorkPageModel) arguments.getSerializable(StuPublishHomeWorkPageModel.TAG);

		mAddPointView = (AddPointCommonView) view.findViewById(R.id.add_point_common_tec_detail);
		divParentLayout = (RelativeLayout) view.findViewById(R.id.div_parent);
		mPicIv = (DragImageView) view.findViewById(R.id.pic_iv_add_point);
		divParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = divParentLayout.getHeight();
					window_width = divParentLayout.getWidth();
					mPicIv.setScreenSize(window_width, window_height);
				}
			}
		});
		if (pageModel != null) {
			String imgpath = pageModel.getImgpath();
			mHandler.sendMessageDelayed(mHandler.obtainMessage(1, imgpath), 10);
//			if (imgpath != null) {
//				mAddPointView.setPagePic(imgpath, false);
//			}
		}
		WeakReference<Activity> mWeakReferenceActivity=new WeakReference<Activity>(getActivity());
		moraientation = new MyOrientationEventListener(mWeakReferenceActivity.get(), this);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		moraientation.enable();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		moraientation.disable();
	}
	
	public void showPageData(ArrayList<HomeWorkCheckPointModel> checkpointlist,int state,int subjectid) {
		
		pageModel.setCheckpointlist(checkpointlist);
		mAddPointView.showRightOrWrongPoint(checkpointlist, state, subjectid,null,0);
	}

	public static StuHomeWorkCheckDetailItemFragment newInstance(StuPublishHomeWorkPageModel pageModel) {
		StuHomeWorkCheckDetailItemFragment fragment = new StuHomeWorkCheckDetailItemFragment();
		Bundle data = new Bundle();
		data.putSerializable(StuPublishHomeWorkPageModel.TAG, pageModel);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointView) {
			mAddPointView.setOrientation(orientation);
		}
	}

}

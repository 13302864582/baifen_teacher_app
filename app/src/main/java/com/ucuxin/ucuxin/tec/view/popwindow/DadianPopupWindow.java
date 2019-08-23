package com.ucuxin.ucuxin.tec.view.popwindow;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerPhotoViewFragment;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class DadianPopupWindow extends PopupWindow {
	DadianResultListener mListener;
	private TextView tv_shenti1, tv_shenti2, tv_guocheng1, tv_guocheng2, tv_zongjie1, tv_zongjie2, tv_tishi;

	public DadianPopupWindow(final PayAnswerPhotoViewFragment mContext, int msubjectid, DadianResultListener Listener) {
		mListener = Listener;
		View view = View.inflate(mContext.getActivity(), R.layout.dadian_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.fade_ins));
		RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.push_popupwindow_in));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(mContext.getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
		update();

		LinearLayout cameraBtn = (LinearLayout) view.findViewById(R.id.item_popupwindows_camera);
		LinearLayout photoBtn = (LinearLayout) view.findViewById(R.id.item_popupwindows_photo);
		LinearLayout cancelBtn = (LinearLayout) view.findViewById(R.id.item_popupwindows_cancel);
		LinearLayout ll1 = (LinearLayout) view.findViewById(R.id.ll1);
		LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.ll2);
		LinearLayout ll3 = (LinearLayout) view.findViewById(R.id.ll3);
		tv_shenti1 = (TextView) view.findViewById(R.id.tv_shenti1);
		tv_shenti2 = (TextView) view.findViewById(R.id.tv_shenti2);
		tv_guocheng1 = (TextView) view.findViewById(R.id.tv_guocheng1);
		tv_guocheng2 = (TextView) view.findViewById(R.id.tv_guocheng2);
		tv_zongjie1 = (TextView) view.findViewById(R.id.tv_zongjie1);
		tv_zongjie2 = (TextView) view.findViewById(R.id.tv_zongjie2);
		tv_tishi = (TextView) view.findViewById(R.id.tv_tishi);
		if (msubjectid != 4) {
			ll1.setVisibility(View.VISIBLE);
			ll2.setVisibility(View.VISIBLE);
			ll3.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("viewreward", ""))) {

				tv_shenti1.setText(SharePerfenceUtil.getInstance().getString("viewreward", ""));
			}
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("viewpunish", ""))) {

				tv_shenti2.setText(SharePerfenceUtil.getInstance().getString("viewpunish", ""));
			}
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("processreward", ""))) {

				tv_guocheng1.setText(SharePerfenceUtil.getInstance().getString("processreward", ""));
			}
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("processpunish", ""))) {

				tv_guocheng2.setText(SharePerfenceUtil.getInstance().getString("processpunish", ""));
			}
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("conclusionreward", ""))) {

				tv_zongjie1.setText(SharePerfenceUtil.getInstance().getString("conclusionreward", ""));
			}
			if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("conclusionpunish", ""))) {

				tv_zongjie2.setText(SharePerfenceUtil.getInstance().getString("conclusionpunish", ""));
			}

		}
		if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("remindtext", ""))) {
			
			tv_tishi.setText(SharePerfenceUtil.getInstance().getString("remindtext", ""));
		}
		
		rl.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				mContext.dismissPoPUP();
				dismiss();
			}
		});
		cameraBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.onReturn(1);
				mContext.showCameraTextSwitchMenu();
				dismiss();
			}
		});
		photoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.onReturn(2);
				mContext.showCameraTextSwitchMenu();
				dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.onReturn(3);
				mContext.showCameraTextSwitchMenu();
				dismiss();
			}
		});

	}

	public interface DadianResultListener {

		void onReturn(int msubtype);

	}
}

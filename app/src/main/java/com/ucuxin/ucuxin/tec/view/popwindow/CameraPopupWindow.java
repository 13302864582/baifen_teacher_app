package com.ucuxin.ucuxin.tec.view.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.manager.IntentManager;

public class CameraPopupWindow extends PopupWindow {
	
	
	public CameraPopupWindow(final Activity mContext, View parent, final int tag,final ArrayList<String> arrayList) {
		View view = View.inflate(mContext, R.layout.item_popupwindows, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_popupwindow_in));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		update();

		TextView blank = (TextView) view.findViewById(R.id.blank_item_popupwindows);
		Button cameraBtn = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button photoBtn = (Button) view.findViewById(R.id.item_popupwindows_photo);
		Button cancelBtn = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		
		blank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
			
		});
		cameraBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IntentManager.startImageCapture(mContext, tag);
				dismiss();
			}
		});
		photoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putInt("tag", tag);
				data.putStringArrayList("viewPagerList", arrayList);
				IntentManager.goToAlbumView(mContext, data);
				dismiss();
			}
		});
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

	}

}

package com.ucuxin.ucuxin.tec.function;

import com.ucuxin.ucuxin.tec.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class SharePopupWindowView extends FrameLayout {

	public SharePopupWindowView(Context context) {
		super(context);
		setupViews();
	}

	public SharePopupWindowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}
	
	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.nav_share_popup_window, null);
		addView(view);
	}
}

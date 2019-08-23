package com.ucuxin.ucuxin.tec.function.homework.view;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class HomeWorkFrameWithDel extends FrameLayout {

	public HomeWorkFrameWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews(context);
	}

	public HomeWorkFrameWithDel(Context context) {
		super(context);
		setupViews(context);
	}

	private void setupViews(Context context) {
		ImageView img = new ImageView(context);
		if (SharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_STUDENT) {
			img.setImageResource(R.drawable.me_v0_11_27);
		}else {
			img.setImageResource(R.drawable.v0_11);
		}
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DisplayUtils.dip2px(context, 29), DisplayUtils.dip2px(context, 29));
		img.setLayoutParams(params);
		img.invalidate();
		addView(img);
	}
	
}

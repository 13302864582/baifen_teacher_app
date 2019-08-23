package com.ucuxin.ucuxin.tec.function.homework.view;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;

public class ChoiceWrongTypePopWindow extends PopupWindow {
	private TecHomeWorkSingleCheckActivity mActivity;
	private View sBtn;
	private View kBtn;

	public ChoiceWrongTypePopWindow(TecHomeWorkSingleCheckActivity activity, View parent ) {
		this.mActivity = activity;
		View view = View.inflate(mActivity, R.layout.choice_wrong_type_pop, null);
		sBtn = view.findViewById(R.id.s_type_wrong_btn_choice);
		sBtn.setOnClickListener(mActivity);
		kBtn = view.findViewById(R.id.k_type_wrong_btn_choice);
		kBtn.setOnClickListener(mActivity);
		view.findViewById(R.id.cancel_btn_wrong_choice).setOnClickListener(mActivity);
		
		view.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_ins));
//		view.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.question_filter_popupwindow_out));
		
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
	}
	
	public void  setWrongType( String wrongType){
		sBtn.setBackgroundResource(R.drawable.choice_wrong_type_btn_selector);
		kBtn.setBackgroundResource(R.drawable.choice_wrong_type_btn_selector);
		if (!TextUtils.isEmpty(wrongType)) {
			switch (wrongType) {
			case "s":
				sBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
				break;
			case "k":
				kBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
				break;

			default:
				break;
			}
		}
	}
}

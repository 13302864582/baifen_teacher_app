package com.ucuxin.ucuxin.tec.function.homework.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.manager.LogicHelper;

public class ReportGrabHomeWorkWindow extends PopupWindow implements OnClickListener {

	private BaseActivity mActivity;

	public ReportGrabHomeWorkWindow(View parent, BaseActivity mActivity) {
		this.mActivity = mActivity;
		View view = View.inflate(mActivity, R.layout.report_grab_homework, null);
		view.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.fade_ins));
		FrameLayout mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
		mTopContainer.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.question_filter_popupwindow_out));

		ImageView mIcDel = (ImageView) view.findViewById(R.id.ic_close_dialog_grab);
		mIcDel.setOnClickListener(this);

		TextView reasonBtn1 = (TextView) view.findViewById(R.id.report_reason_btn1);
		reasonBtn1.setOnClickListener(this);
		reasonBtn1.setText(TecApplication.getContext().getResources().getString(R.string.report_homework_reason_text1));
		reasonBtn1.setVisibility(View.GONE);

		TextView reasonBtn2 = (TextView) view.findViewById(R.id.report_reason_btn2);
		reasonBtn2.setOnClickListener(this);
		reasonBtn2.setText(TecApplication.getContext().getResources().getString(R.string.report_homework_reason_text2));

		TextView reasonBtn3 = (TextView) view.findViewById(R.id.report_reason_btn3);
		reasonBtn3.setOnClickListener(this);
		reasonBtn3.setText(TecApplication.getContext().getResources().getString(R.string.report_homework_reason_text3));

		TextView reasonBtn4 = (TextView) view.findViewById(R.id.report_reason_btn4);
		reasonBtn4.setOnClickListener(this);
		reasonBtn4.setText(TecApplication.getContext().getResources().getString(R.string.report_homework_reason_text4));
		reasonBtn4.setVisibility(View.GONE);

		TextView reasonBtn5 = (TextView) view.findViewById(R.id.report_reason_btn5);
		reasonBtn5.setVisibility(View.VISIBLE);
		reasonBtn5.setOnClickListener(this);
		reasonBtn5.setText(TecApplication.getContext().getResources().getString(R.string.report_homework_reason_text5));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setContentView(view);
		setOutsideTouchable(false);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
	}

	@Override
	public void onClick(View view) {
		dismiss();
		switch (view.getId()) {
		case R.id.ic_close_dialog_grab:
			break;
		case R.id.report_reason_btn1:

			if (view instanceof TextView) {
				String reason=((TextView) view).getText().toString().trim();
				int reasonid= LogicHelper.getReportIdFromTxt(reason);
				String tip=TecApplication.getContext().getResources().getString(R.string.report_eyi_tip).trim();
				mActivity.report(reasonid,reason,tip);
			}
			break;
		case R.id.report_reason_btn2:
			if (view instanceof TextView) {
				String reason=((TextView) view).getText().toString().trim();
				int reasonid= LogicHelper.getReportIdFromTxt(reason);
				mActivity.report(reasonid,reason,"");
			}
			break;
		case R.id.report_reason_btn3:
			if (view instanceof TextView) {
				String reason=((TextView) view).getText().toString().trim();
				int reasonid= LogicHelper.getReportIdFromTxt(reason);
				mActivity.report(reasonid,reason,"");
			}
			break;
		case R.id.report_reason_btn4:
			if (view instanceof TextView) {
				String reason=((TextView) view).getText().toString().trim();
				int reasonid= LogicHelper.getReportIdFromTxt(reason);
				String tip=TecApplication.getContext().getResources().getString(R.string.report_eyi_tip);
				mActivity.report(reasonid,reason,tip);
			}
			break;
		case R.id.report_reason_btn5:
			if (view instanceof TextView) {
				String reason=((TextView) view).getText().toString().trim();
				int reasonid= LogicHelper.getReportIdFromTxt(reason);
				mActivity.report(reasonid,reason,"");
			}
			break;
		}
	}
}

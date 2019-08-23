package com.ucuxin.ucuxin.tec.function.question;

import android.app.Activity;
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
import com.ucuxin.ucuxin.tec.manager.LogicHelper;

/**
 * 问题举报
 * @author Administrator
 *
 */
public class ReportQuestionWindow extends PopupWindow implements OnClickListener {

	private FrameLayout mTopContainer;
	private ImageView mIcDel;
	private TextView reasonBtn1;
	private TextView reasonBtn2;
	private TextView reasonBtn3;
	private TextView reasonBtn4;
	private TextView reasonBtn5;
	private TextView reasonBtn6;
	private PayAnswerFragment mActivity;

	public ReportQuestionWindow(final Activity mContext, View parent, PayAnswerFragment activity) {
		this.mActivity = activity;
		View view = View.inflate(mContext, R.layout.report_change_question, null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
		mTopContainer = (FrameLayout) view.findViewById(R.id.top_container);
		mTopContainer.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.question_filter_popupwindow_out));

		mIcDel = (ImageView) view.findViewById(R.id.ic_close_dialog_grab);
		mIcDel.setOnClickListener(this);

		reasonBtn1 = (TextView) view.findViewById(R.id.report_reason_btn1);
		reasonBtn1.setOnClickListener(this);

		reasonBtn2 = (TextView) view.findViewById(R.id.report_reason_btn2);
		reasonBtn2.setOnClickListener(this);

		reasonBtn3 = (TextView) view.findViewById(R.id.report_reason_btn3);
		reasonBtn3.setOnClickListener(this);

		reasonBtn4 = (TextView) view.findViewById(R.id.report_reason_btn4);
		reasonBtn4.setOnClickListener(this);
		
		 reasonBtn5 = (TextView) view.findViewById(R.id.report_reason_btn5);
	     reasonBtn5.setOnClickListener(this);
	     
	     
	     reasonBtn6 = (TextView) view.findViewById(R.id.report_reason_btn6);
	     reasonBtn6.setOnClickListener(this);

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
		String reason="";
		int reasonid=0;
		switch (view.getId()) {
		case R.id.ic_close_dialog_grab:
			mActivity.isShowRePortPop = false;
			dismiss();
			break;
		case R.id.report_reason_btn1:


			 reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason1).trim();
			 reasonid= LogicHelper.getReportIdFromTxt(reason);
			mActivity.report(reasonid,reason,"");
			dismiss();
			break;
		case R.id.report_reason_btn2:
			 reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason2).trim();
			 reasonid= LogicHelper.getReportIdFromTxt(reason);
			String tip="恶意举报扣5学点，0.5责任指数";
			mActivity.report(reasonid,reason,tip);
			dismiss();
			break;
		case R.id.report_reason_btn3:
			reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason3).trim();
			reasonid= LogicHelper.getReportIdFromTxt(reason);
			mActivity.report(reasonid,reason,"");
			dismiss();
			break;
		case R.id.report_reason_btn4:
			reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason4).trim();
			reasonid= LogicHelper.getReportIdFromTxt(reason);
			mActivity.report(reasonid,reason,"");
			dismiss();
			break;
		case R.id.report_reason_btn5:
			reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason5).trim();
			reasonid= LogicHelper.getReportIdFromTxt(reason);
			mActivity.report(reasonid,reason,"");
			dismiss();
			break;
		case R.id.report_reason_btn6:
			reason=TecApplication.getContext().getResources().getString(R.string.text_report_reason6).trim();
			reasonid= LogicHelper.getReportIdFromTxt(reason);
			mActivity.report(reasonid,reason,"");
			dismiss();
			break;
			
			
			
		}
	}
}

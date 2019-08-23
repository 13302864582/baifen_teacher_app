package com.ucuxin.ucuxin.tec.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.model.SubjectModel;

public class WeLearnUiUtil {

	private ImageView mDialogImg;
	private Dialog mDialog;
	private Context mContext;
	private View mDialogText;
	class MyDialog extends Dialog{

		public MyDialog(Context context, int theme) {
			super(context, theme);
		}
		@Override
		public void onBackPressed() {
		}
	}
	private static WindowManager wm;
	
	private WeLearnUiUtil() {
		this.mContext = TecApplication.getContext();
	}

	private static class WeLearnUiUtilHolder {
		private static final WeLearnUiUtil INSANCE = new WeLearnUiUtil();
	}
	
	public static WeLearnUiUtil getInstance() {
		return WeLearnUiUtilHolder.INSANCE;
	}
	
//	public void showVoiceDialog(Activity context) {
//		if (!context.isFinishing()) {
//			if (mDialog == null) {
//				mDialog = new Dialog(context, R.style.DialogStyle);
//				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//				mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//						WindowManager.LayoutParams.FLAG_FULLSCREEN);
//				mDialog.setContentView(R.layout.voice_dialog);
//				mDialog.setCanceledOnTouchOutside(false);
//				mDialogImg = (ImageView) mDialog.findViewById(R.id.dialog_img);
//			}
//			if (!mDialog.isShowing()) {
//				try {
//					mDialog.show();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	};
//	
	
	public void showCourseVoiceDialog(Activity context, OnClickListener listener) {
		if (!context.isFinishing()) {
			if (mDialog == null) {
				mDialog = new MyDialog(context, R.style.DialogStyle);
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				mDialog.setContentView(R.layout.course_voice_dialog);
				mDialog.setCanceledOnTouchOutside(false);
				
				mDialogImg = (ImageView) mDialog.findViewById(R.id.dialog_img);
				View mDialogBody =  mDialog.findViewById(R.id.dialog_body);
				if (listener != null) {
					mDialogBody.setOnClickListener(listener);
				}
			}
			if (!mDialog.isShowing()) {
				try {
					mDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void showChatVoiceDialog(Activity context) {
		if (!context.isFinishing()) {
			if (mDialog == null) {
				mDialog = new Dialog(context, R.style.DialogStyle);
				mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				mDialog.setContentView(R.layout.chat_voice_dialog);
				mDialog.setCanceledOnTouchOutside(false);
				mDialogImg = (ImageView) mDialog.findViewById(R.id.chat_dialog_img);
				mDialogText = mDialog.findViewById(R.id.chat_dialog_text);
				mDialogText.setVisibility(View.VISIBLE);
			}
			if (!mDialog.isShowing()) {
				try {
					mDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void closeDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}
	
	public void setDialogImage(double voiceValue) {
		if (voiceValue < 200.0) {
			mDialogImg.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			mDialogImg.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			mDialogImg.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			mDialogImg.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			mDialogImg.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			mDialogImg.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			mDialogImg.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			mDialogImg.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			mDialogImg.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			mDialogImg.setImageResource(R.drawable.record_animate_10);
		}
	}

	public void showCancelSendDialog() {
		mDialogText.setVisibility(View.GONE);
		mDialogImg.setImageResource(R.drawable.bg_cancel_send);
	}
	
	public void setMsgDialogImage(double voiceValue) {
		if (voiceValue < 200.0) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			mDialogImg.setImageResource(R.drawable.audio_msg_dialog_10);
		}
	}
	
	public FrameLayout setImageOnImageView(RelativeLayout answerContainern, ExplainPoint point, final String audioPath) {
		
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		FrameLayout.LayoutParams framelayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		
		FrameLayout childConainer = new FrameLayout(mContext);
		childConainer.setBackgroundResource(R.drawable.bg_voice_ok);
		childConainer.setClickable(true);
		final ImageView iconVoiceView = new ImageView(mContext);
		iconVoiceView.setImageResource(R.drawable.ic_play2);
		framelayoutParams.gravity = Gravity.CENTER;
		iconVoiceView.setLayoutParams(framelayoutParams);
		childConainer.addView(iconVoiceView);
		
		float x = point.getX();
		float y = point.getY();
		relativeParams.leftMargin = (int) x;
		relativeParams.topMargin = (int) y;
		childConainer.setLayoutParams(relativeParams);
		
		answerContainern.addView(childConainer);
		
		return childConainer;
	}
	
	public void showWarnDialogWhenRecordVoice() {
		ToastUtils.show("录音时间太短,请重录");
	}
	
	@SuppressLint("InflateParams")
	public static void initSubjects(Context context, ArrayList<SubjectModel> subList, RadioGroup subjectRG, boolean selectFirst) {

		if (null == subList || null == subjectRG) {
			return;
		}

		if (null == wm) {
			wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}

		if (null != subList) {
			RadioButton rb = null;
			@SuppressWarnings("deprecation")
			int screenWidth = wm.getDefaultDisplay().getWidth();
			int padding = screenWidth / 50;
			int margin = screenWidth / 60;
			int btnWidth = screenWidth / subList.size() - margin * 3;
			subjectRG.removeAllViews();
			for (SubjectModel sm : subList) {
				rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.view_radio_button, null);
				rb.setText(sm.getName());
				rb.setId(sm.getId());
				rb.setTag(sm);
				rb.setBackgroundResource(R.drawable.bg_subject_selector);
				RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(btnWidth, LayoutParams.WRAP_CONTENT);
				rb.setPadding(padding, 10, padding, 10);
				lp.setMargins(margin, 0, margin, 0);
				subjectRG.addView(rb, lp);
			}
			
			if (selectFirst) {
				if (null != subList && subList.size() >= 1) {
					try {
						View v = subjectRG.getChildAt(0);
						if (v instanceof RadioButton) {
							RadioButton r = (RadioButton) v;
							r.setChecked(true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

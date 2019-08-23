package com.ucuxin.ucuxin.tec.function.homework.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity.DialogListener;
import com.ucuxin.ucuxin.tec.utils.MD5Util;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.MaxRecordTime;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.RecordCallback;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;

public class RecordPopupViewOfHomeWork extends FrameLayout implements OnClickListener/*, OnTouchListener */{

	private TextView mAudioCancel;
	private FrameLayout mAddAnswerVoiceContainer;
	private ProgressBar mAudioLoadProgressBar;
	private PublishRecordCallback mCallback;
	private String mAudioPath;
	private TextView mRecordBtn;
	private TextView mAddVoiceAnswer;
	private ImageView mPlayAnimation;
	private BaseActivity mActivity;
	private double voiceValue = 0.0;
	private TextView mAudioConfirm;
	
//	private WelearnDialogBuilder mWelearnDialogBuilder;
	
	public static final String VOICE_FILE_NAME = "voice";
	
	public RecordPopupViewOfHomeWork(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof TecHomeWorkSingleCheckActivity) {
			mActivity = (TecHomeWorkSingleCheckActivity) context;
		} 
		
		setupViews();
	}

	public RecordPopupViewOfHomeWork(Context context) {
		super(context);
		if (context instanceof TecHomeWorkSingleCheckActivity) {
			mActivity = (TecHomeWorkSingleCheckActivity) context;
		} 
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.voice_homework_popup_window, null);
		mAudioCancel = (TextView) view.findViewById(R.id.audio_cancel);
		mAddAnswerVoiceContainer = (FrameLayout) view.findViewById(R.id.add_voice_answer_container);
		mRecordBtn = (TextView) view.findViewById(R.id.audio_record);
		mAddVoiceAnswer = (TextView) view.findViewById(R.id.add_voice_answer);
		mPlayAnimation = (ImageView) view.findViewById(R.id.icon_answer_audio);
		mAudioLoadProgressBar = (ProgressBar) view.findViewById(R.id.audio_load_progressbar);
		mAudioConfirm = (TextView) view.findViewById(R.id.audio_confirm);
		addView(view);
		
		mAudioCancel.setOnClickListener(this);
		mRecordBtn.setOnClickListener(this);
		mAudioConfirm.setOnClickListener(this);
//		mAddAnswerVoiceContainer.setOnTouchListener(this);
		mAddAnswerVoiceContainer.setOnClickListener(this);
		
		mCallback = new PublishRecordCallback();
	}

	private DialogListener mListener;
	private HomeWorkSinglePoint mSinglePoint;
	
/**
 * 设置点击监听
 * @param listener
 */
	public void setOnBtnClickListener(HomeWorkSinglePoint singlePoint ,DialogListener listener) {
		this.mSinglePoint = singlePoint;
		this.mListener = listener;
	}
	
	public void reset() {
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_cancel:
			removePoint();
			stopAllPlay();
			mListener.onCancelBtnClick();
			break;
		case R.id.dialog_body://停止录音
			mAddVoiceAnswer.setText(mActivity.getString(R.string.saving_record_text));
			MediaUtil.getInstance(false).stopRecord(voiceValue, mCallback,true);
			break;
		case R.id.add_voice_answer_container:
			int visibility = mAddVoiceAnswer.getVisibility();
			if (visibility == View.VISIBLE) {
				String textStr = mAddVoiceAnswer.getText().toString().trim();
				if (textStr.equals(mActivity.getString(R.string.click_to_record_text))) {//点击开始录音
					mAddVoiceAnswer.setText(mActivity.getString(R.string.recording_text));
					mAudioName = MD5Util.getMD5String(VOICE_FILE_NAME + System.currentTimeMillis());
					isRecording = true;
					MediaUtil.getInstance(false).record(mAudioName, mCallback, mActivity , this , MaxRecordTime.MAX_OF_HOMEWORK);
				}
			}else {
				playVoice();
			}
			break;

		case R.id.audio_confirm:
			if (!isRecording) {
				if (!MyFileUtil.isFileExist(mAudioPath)) {
					ToastUtils.show(R.string.text_input_voice_explain);
				} else {
					stopAllPlay();
					mSinglePoint.setSndpath(mAudioPath);
					mSinglePoint.setAudioTime(mRecordTime);
					mListener.onConfirmBtnClick(mSinglePoint);
				}
			}
			break;
		case R.id.audio_record://重录
			stopAllPlay();
//			mAddAnswerVoiceContainer.setOnClickListener(null);
//			mAddAnswerVoiceContainer.setOnTouchListener(RecordPopupViewOfHomeWork.this);
			mRecordBtn.setVisibility(GONE);
			mAddVoiceAnswer.setVisibility(View.VISIBLE);
			mPlayAnimation.setVisibility(View.GONE);
			removePoint();
			break;
		}
	}
	
	public void removePoint() {
		if (MyFileUtil.deleteFile(mAudioPath)) {
			mAudioPath = null;
		}
	}
	
	/**
	 * 重置播放动画
	 * @param iconVoiceView
	 */
	private void resetAnimationPlay(ImageView iconVoiceView) {
		for (ImageView currentView: TecApplication.anmimationPlayViews) {
			if (currentView != iconVoiceView) {
				currentView.setImageResource(R.drawable.ic_play2);
			}
		}
	}
	
	/**
	 * 停止其他播放
	 */
	private void stopPlay() {
		for (AnimationDrawable animation : TecApplication.animationDrawables) {
			MediaUtil.getInstance(false).stopVoice(animation);
		}
	}
	
	private void stopAllPlay() {
		stopPlay();
		MediaUtil.getInstance(false).stopVoice(mAnimationDrawable);
	}
	
	private AnimationDrawable mAnimationDrawable;
	
	private void playVoice() {
		mPlayAnimation.setImageResource(R.drawable.play_animation);
		mAnimationDrawable = (AnimationDrawable) mPlayAnimation.getDrawable();
		if (TextUtils.isEmpty(mAudioPath)) {
			ToastUtils.show(R.string.text_no_audio_play);
			return;
		}
		MediaUtil.getInstance(false).playVoice(true, mAudioPath, mAnimationDrawable, new ResetImageSourceCallback() {
			
			@Override
			public void reset() {
				mPlayAnimation.setImageResource(R.drawable.ic_play2);
			}
			
			@Override
			public void playAnimation() {
				mPlayAnimation.setVisibility(View.VISIBLE);
				mAudioLoadProgressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void beforePlay() {
				mAudioLoadProgressBar.setVisibility(GONE);
				mPlayAnimation.setVisibility(VISIBLE);
			}
		}, null);
	}

	private float mRecordTime;
	
	private class PublishRecordCallback implements RecordCallback {


		@Override
		public void onAfterRecord(float recordTime) {
//			mAddAnswerVoiceContainer.setOnClickListener(RecordPopupViewOfHomeWork.this);
//			mAddAnswerVoiceContainer.setOnTouchListener(null);
			mRecordBtn.setVisibility(View.VISIBLE);//显示重录按钮
			mAddVoiceAnswer.setVisibility(View.GONE);
			mAddVoiceAnswer.setText(mActivity.getString(R.string.click_to_record_text));
			mPlayAnimation.setVisibility(View.VISIBLE);
			mAudioPath = MyFileUtil.VOICE_PATH + mAudioName + ".amr";
			RecordPopupViewOfHomeWork.this.mRecordTime = recordTime;
//			point.setAudioPath(mAudioPath);
//			WApplication.coordinateAnswerIconSet.add(point);
			isRecording = false;
		}

	}
	private boolean isRecording = false;
	private String mAudioName;
//	private Handler mHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 222:
//				String audioName = (String) msg.obj;
//				isRecording = true;
//				WeLearnMediaUtil.getInstance(false).record(audioName, mCallback, mActivity);
//				break;
//			}
//		};
//	};
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		if (v.getId() == R.id.add_voice_answer_container) {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				//UUID.fromString(VOICE_FILE_NAME + point).toString();
//				mAudioName = MD5Util.getMD5String(VOICE_FILE_NAME + System.currentTimeMillis());
//				isRecording = true;
//				WeLearnMediaUtil.getInstance(false).record(mAudioName, mCallback, mActivity);
//				break;
//			case MotionEvent.ACTION_CANCEL:
//			case MotionEvent.ACTION_UP:
//				WeLearnMediaUtil.getInstance(false).stopRecord(voiceValue, mCallback);
//				break;
//			}
//		}
//		return true;
//	}
}

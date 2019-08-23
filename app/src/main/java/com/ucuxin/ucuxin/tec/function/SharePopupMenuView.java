package com.ucuxin.ucuxin.tec.function;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerAppendAskActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerPhotoViewActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerPhotoViewFragment.ResetView;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.utils.MD5Util;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.MaxRecordTime;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.RecordCallback;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;

public class SharePopupMenuView extends FrameLayout implements OnClickListener/*, OnTouchListener*/ {

	private TextView mAudioCancel;
	private RelativeLayout mAsnwerContainer;
	private CameraChoiceIconWithDel mImg;
	private ResetView mResetCallback;
	private FrameLayout mAddAnswerVoiceContainer;
	private ProgressBar mAudioLoadProgressBar;
	private ExplainPoint point;
	private PublishRecordCallback mCallback;
	private String mAudioPath;
	private TextView mRecordBtn;
	private TextView mAddVoiceAnswer;
	private ImageView mPlayAnimation;
	private BaseActivity mActivity;
	private double voiceValue = 0.0;
	private TextView mAudioConfirm;
	
	private WelearnDialogBuilder mWelearnDialogBuilder;
	
	public static final String VOICE_FILE_NAME = "voice";
	
	public SharePopupMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (context instanceof PayAnswerPhotoViewActivity) {
			mActivity = (PayAnswerPhotoViewActivity) context;
		} else if (context instanceof PayAnswerAppendAskActivity) {
			mActivity = (PayAnswerAppendAskActivity) context;
		}
		
		setupViews();
	}

	public SharePopupMenuView(Context context) {
		super(context);
		mActivity = (PayAnswerPhotoViewActivity) context;
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.nav_share_popup_menu, null);
		mAudioCancel = (TextView) view.findViewById(R.id.audio_cancel);
		mAddAnswerVoiceContainer = (FrameLayout) view.findViewById(R.id.add_voice_answer_container);
		mRecordBtn = (TextView) view.findViewById(R.id.audio_record);
		mAddVoiceAnswer = (TextView) view.findViewById(R.id.add_voice_answer);
		mPlayAnimation = (ImageView) view.findViewById(R.id.icon_answer_audio);
		mAudioLoadProgressBar = (ProgressBar) view.findViewById(R.id.audio_load_progressbar);
		mAudioConfirm = (TextView) view.findViewById(R.id.audio_confirm);
		addView(view);
		
		mRecordBtn.setOnClickListener(this);
		mAudioConfirm.setOnClickListener(this);
//		mAddAnswerVoiceContainer.setOnTouchListener(this);
		mAddAnswerVoiceContainer.setOnClickListener(this);
		
		mCallback = new PublishRecordCallback();
	}

	public void enableCancelBtn(RelativeLayout answerContainer, CameraChoiceIconWithDel img, ResetView resetCallback) {
		this.mAsnwerContainer = answerContainer;
		this.mImg = img;
		this.mResetCallback = resetCallback;
		mAudioCancel.setOnClickListener(this);
	}
	
	private View view;
	/**
	 * 当确定按钮被点击时
	 * @param answerContainern
	 * @param img
	 */
	public void enableConfirmBtn(RelativeLayout answerContainern, CameraChoiceIconWithDel img, ExplainPoint point, View view) {
		this.mAsnwerContainer = answerContainern;
		this.mImg = img;
		this.point = point;
		this.view = view;
		mAudioConfirm.setOnClickListener(this);
	}
	
	private void removeImgOnTheConainer() {
		if (mAsnwerContainer != null && mImg != null) {
			mAsnwerContainer.removeView(mImg);
		}
	}
	
	private void clear() {
		removePoint();
		removeImgOnTheConainer();
		this.mResetCallback.reset(true);
	}
	
	public void reset() {
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_cancel:
			clear();
			removePoint();
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
					String audioName = MD5Util.getMD5String(VOICE_FILE_NAME + point);
					isRecording = true;
					MediaUtil.getInstance(false).record(audioName, mCallback, mActivity , this , MaxRecordTime.MAX_OF_QA);
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
					removeImgOnTheConainer();
					
					RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, 
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
					
					float x = point.getX();
					float y = point.getY();
					
					relativeParams.leftMargin = (int) (x) ;
					relativeParams.topMargin = (int) (y);
					
					final CameraChoiceIconWithDel childConainer = new CameraChoiceIconWithDel(getContext(),point.getSubtype());
					childConainer.setLayoutParams(relativeParams);
					mAsnwerContainer.addView(childConainer);
					
					final ImageView iconVoiceView = childConainer.getIcView();
					childConainer.getBgView().setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							iconVoiceView.setImageResource(R.drawable.play_animation);
							mAnimationDrawable = (AnimationDrawable) iconVoiceView.getDrawable();
							TecApplication.animationDrawables.add(mAnimationDrawable);
							TecApplication.anmimationPlayViews.add(iconVoiceView);
							stopPlay();
							MediaUtil.getInstance(false).playVoice(true, mAudioPath, mAnimationDrawable, new ResetImageSourceCallback() {
								
								@Override
								public void reset() {
									iconVoiceView.setImageResource(R.drawable.ic_play2);
								}
								
								@Override
								public void playAnimation() {}
								
								@Override
								public void beforePlay() {
									resetAnimationPlay(iconVoiceView);
								}
							}, null);
						}
					});
					childConainer.getBgView().setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							if (null == mWelearnDialogBuilder) {
								mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(mActivity);
							}
							mWelearnDialogBuilder.withMessage(R.string.text_dialog_tips_del_carmera_frame)
									.setOkButtonClick(new View.OnClickListener() {
										@Override
										public void onClick(View v) {

											try {
												mWelearnDialogBuilder.dismiss();
											} catch (Exception e) {
												e.printStackTrace();
											}

											removePoint();
											mAsnwerContainer.removeView(childConainer);// 移除该图标
										}
									});
							mWelearnDialogBuilder.show();
							
							return true;
						}
					});
					this.mResetCallback.reset(false);
				}
			}
			break;
		case R.id.audio_record://重录
			stopAllPlay();
//			mAddAnswerVoiceContainer.setOnClickListener(null);
//			mAddAnswerVoiceContainer.setOnTouchListener(SharePopupMenuView.this);
			mRecordBtn.setVisibility(GONE);
			mAddVoiceAnswer.setVisibility(View.VISIBLE);
			mPlayAnimation.setVisibility(View.GONE);
			removePoint();
			break;
		}
	}
	
	public void removePoint() {
		if (MyFileUtil.deleteFile(mAudioPath)) {
			TecApplication.coordinateAnswerIconSet.remove(point);
			if (TecApplication.coordinateAnswerIconSet.size() == 0) {
				if (this.mResetCallback!=null) {
					this.mResetCallback.showRotateBtn();
				}
			}
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

	private class PublishRecordCallback implements RecordCallback {

		@Override
		public void onAfterRecord(float recordTime) {
//			mAddAnswerVoiceContainer.setOnClickListener(SharePopupMenuView.this);
//			mAddAnswerVoiceContainer.setOnTouchListener(null);
			mRecordBtn.setVisibility(View.VISIBLE);//显示重录按钮
			mAddVoiceAnswer.setVisibility(View.GONE);
			mAddVoiceAnswer.setText(mActivity.getString(R.string.click_to_record_text));
			mPlayAnimation.setVisibility(View.VISIBLE);
			mAudioPath = MyFileUtil.VOICE_PATH + MD5Util.getMD5String(VOICE_FILE_NAME + point) + ".amr";
			point.setAudioPath(mAudioPath);
			TecApplication.coordinateAnswerIconSet.add(point);
			isRecording = false;
		}

	}
	private boolean isRecording = false;
	
}

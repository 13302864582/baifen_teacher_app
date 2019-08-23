package com.ucuxin.ucuxin.tec.function.teccourse.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.LearningAdapter;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.utils.BtnUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MD5Util;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.MaxRecordTime;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.RecordCallback;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.viewpager.MyViewPager;

import java.io.File;
import java.util.List;

public class InputExplainView extends FrameLayout implements OnClickListener {
	public static final String TAG = InputExplainView.class.getSimpleName();
	public static final String VOICE_FILE_NAME = "voice";
	private Activity mActivity;
	private TextView recordingTv, tv_tishi;
	private View choiceTextBtn;
	private ImageView iv_recording;
	private RelativeLayout rl_recording, rl_tishi;
	private Button recordBtn;
	private View choiceVoiceBtn;
	private View textInputView;
	private Button inputSureBtn;
	private EditText texeInputEt;
	private ResultListener mListener;
	public boolean isRecording;
	public boolean flag2 = true;
	private double voiceValue = 0.0;
	private String mAudioName;
	private String mAudioPath;
	private MyViewPager mViewPager;
	protected float mRecordTime;
	private Handler mhandler = new Handler() {
		// private String openId;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 13:
				recordBtn.setClickable(true);
				float recordTime = (float) msg.obj;
				rl_tishi.setVisibility(View.VISIBLE);
				rl_recording.setVisibility(View.GONE);

				recordBtn.setText(mActivity.getString(R.string.click_to_record_text));

				blankView.setVisibility(View.GONE);
				mAudioPath = MyFileUtil.VOICE_PATH + mAudioName + ".amr";
				InputExplainView.this.mRecordTime = recordTime;
				isRecording = false;
				if (recordTime != -1) {
					File mAudioPathFile=new File(mAudioPath);
					LogUtils.e("mAudioPath", mAudioPath);
					LogUtils.e("mAudioPathFile.length()--", mAudioPathFile.length()+"");
					if (mAudioPathFile.exists()&&mAudioPathFile.length()>0) {						
						returnResult(GlobalContant.ANSWER_AUDIO, "", mAudioPath);
					}else {
						ToastUtils.show("没有录音权限或录音时间太短,请检查权限重新打点录音");
					}
					
				} else {
					ToastUtils.show("保存失败");
				}
				
				break;
			}

		}
	};
	private RecordCallback mCallback = new RecordCallback() {

		@Override
		public void onAfterRecord(float recordTime) {

			if (flag2) {
				Message obtain = Message.obtain();
				obtain.what = 13;
				obtain.obj = recordTime;
				mhandler.sendMessage(obtain);
			}

		}
	};

	private InputMethodManager imm;
	private View blankView;
	private List<String> viewPagerList;
	private int currentItem;

	public interface ResultListener {
		/**
		 * 
		 * @param type
		 *            GlobalContant.ANSWER_AUDIO 声音 , GlobalContant.ANSWER_TEXT
		 *            文字
		 * @param text
		 *            文字内容
		 * @param audioPath
		 *            语音地址
		 */
		void onReturn(int type, String text, String audioPath);

	}

	public InputExplainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	public InputExplainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	public InputExplainView(Context context) {
		super(context);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	public void setImageList(Activity activity, List<String> viewPagerList, int currentItem) {
		this.mActivity = activity;
		this.viewPagerList = viewPagerList;
		this.currentItem = currentItem;
		if (mViewPager != null) {
			mViewPager.setAdapter(new LearningAdapter(mActivity, viewPagerList));
			mViewPager.setCurrentItem(currentItem);
		}
	}

	private void setUpViews() {
		View view = LayoutInflater.from(mActivity).inflate(R.layout.add_explain_bottom, null);
		imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

		blankView = view.findViewById(R.id.blank_layout_input_view);
		blankView.setOnClickListener(this);
		blankView.setVisibility(View.GONE);
		mViewPager = (MyViewPager) view.findViewById(R.id.mViewPager);
		// GlobalVariable.mViewPager = mViewPager;
		if (mViewPager != null) {
			mViewPager.setAdapter(new LearningAdapter(mActivity, viewPagerList));
			mViewPager.setCurrentItem(currentItem);
		}

		recordingTv = (TextView) view.findViewById(R.id.recording_tv_input_view);
		tv_tishi = (TextView) view.findViewById(R.id.tv_tishi);
		rl_recording = (RelativeLayout) view.findViewById(R.id.rl_recording);
		rl_tishi = (RelativeLayout) view.findViewById(R.id.rl_tishi);
		iv_recording = (ImageView) view.findViewById(R.id.iv_recording);
		choiceTextBtn = view.findViewById(R.id.choice_text_btn_input_view);
		recordBtn = (Button) view.findViewById(R.id.record_btn_input_view);
		textInputView = view.findViewById(R.id.text_input_layout_input_view);
		choiceVoiceBtn = view.findViewById(R.id.choice_voice_btn_input_view);
		inputSureBtn = (Button) view.findViewById(R.id.input_sure_btn_input_view);
		if (!TextUtils.isEmpty(SharePerfenceUtil.getInstance().getString("voiceremind", ""))) {

			tv_tishi.setText(SharePerfenceUtil.getInstance().getString("voiceremind", ""));
		}
		choiceTextBtn.setOnClickListener(this);
		iv_recording.setOnClickListener(this);
		recordBtn.setOnClickListener(this);
		choiceVoiceBtn.setOnClickListener(this);
		inputSureBtn.setOnClickListener(this);

		texeInputEt = (EditText) view.findViewById(R.id.text_input_et_input_view);
		texeInputEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence str, int arg1, int arg2, int arg3) {
				int length = str.length();
				if (length == 0) {
					inputSureBtn.setText(mActivity.getString(R.string.text_nav_cancel));
				} else {
					inputSureBtn.setText(mActivity.getString(R.string.text_nav_submit));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		addView(view);
	}

	public void setResultListener(ResultListener listener) {
		this.mListener = listener;
		setVisibility(View.VISIBLE);
	}

	private void returnResult(int type, String text, String audioPath) {
		setVisibility(View.GONE);
		mListener.onReturn(type, text, audioPath);
		texeInputEt.setText("");
		textInputView.setVisibility(View.GONE);
	}

	/**
	 * 正在录音的话返回false View不隐藏
	 * 
	 * @return
	 */
	public boolean onBackPress() {
		if (isRecording) {
			ToastUtils.show("正在录音中!请先停止");
		} else {
			setVisibility(View.GONE);
		}
		return !isRecording;
	}

	@Override
	public void onClick(View view) {
		if (BtnUtils.isFastClick()) {
			return;
		}
		int id = view.getId();
		switch (id) {
		case R.id.choice_text_btn_input_view:
			if (!isRecording) {

				texeInputEt.requestFocus();
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				textInputView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.record_btn_input_view:// 点击开始录音
			int visibility = textInputView.getVisibility();
			String textStr = recordBtn.getText().toString().trim();
			if (visibility != View.VISIBLE) {
				MediaUtil.getInstance(false).stopVoice(null);
				if (textStr.equals(mActivity.getString(R.string.click_to_record_text))) {// 点击开始录音
					blankView.setVisibility(View.VISIBLE);
					rl_recording.setVisibility(View.VISIBLE);
					rl_tishi.setVisibility(View.GONE);
					recordingTv.setText("正在录音......");
					iv_recording.setBackgroundResource(R.drawable.stop_pre);
					flag2 = true;
					recordBtn.setText(mActivity.getString(R.string.click_to_stop_text));
					mAudioName = MD5Util.getMD5String(VOICE_FILE_NAME + System.currentTimeMillis());
					isRecording = true;
					MediaUtil.getInstance(false).record(mAudioName, mCallback, mActivity, MaxRecordTime.MAX_OF_COURSE);
				} else if (textStr.equals(mActivity.getString(R.string.click_to_stop_text))) {
					stopRecording();
				}
			}
			break;
		case R.id.iv_recording:
			if (flag2) {
				flag2 = false;
				recordingTv.setText("暂停录音......");
				iv_recording.setBackgroundResource(R.drawable.go_on_pre);
				mhandler.removeMessages(13);
				
				MediaUtil.getInstance(false).stopRecord(voiceValue, mCallback, false);
			} else {
				flag2 = true;
				isRecording = true;
				recordingTv.setText("正在录音......");
				iv_recording.setBackgroundResource(R.drawable.stop_pre);
				MediaUtil.getInstance(false).record(System.currentTimeMillis() + "copy", mCallback, mActivity,
						MaxRecordTime.MAX_OF_COURSE);
			}
			break;
		case R.id.choice_voice_btn_input_view:
			textInputView.setVisibility(View.GONE);
			// 隐藏键盘
			imm.hideSoftInputFromWindow(texeInputEt.getWindowToken(), 0);
			break;
		case R.id.input_sure_btn_input_view:
			textStr = inputSureBtn.getText().toString().trim();
			imm.hideSoftInputFromWindow(texeInputEt.getWindowToken(), 0);
			if (textStr.equals(mActivity.getString(R.string.text_nav_cancel))) {// 取消
				mActivity.onBackPressed();
			} else if (textStr.equals(mActivity.getString(R.string.text_nav_submit))) {// 确定
				// 隐藏键盘
				returnResult(GlobalContant.ANSWER_TEXT, texeInputEt.getText().toString().trim(), "");
			}

			break;

		default:
			break;
		}
	}

	private void stopRecording() {
		recordBtn.setText(mActivity.getString(R.string.saving_record_text));
		recordBtn.setClickable(false);
		mhandler.removeMessages(13);
		flag2 = true;
		MediaUtil.getInstance(false).stopRecord(voiceValue, mCallback, true);

	}

}

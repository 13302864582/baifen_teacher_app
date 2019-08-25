package com.ucuxin.ucuxin.tec.function.question;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.CameraChoiceIconWithSer;
import com.ucuxin.ucuxin.tec.function.homework.CropImageActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.model.QuestionModelGson;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.AppUtils;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LOG;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;
import com.ucuxin.ucuxin.tec.view.PayAnswerCommonView;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;
import com.ucuxin.ucuxin.tec.view.popwindow.CameraPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class PayAnswerGrabItemActivity extends BaseActivity implements OnClickListener/*, OnUploadListener*/ {
	public static final String SHOW_ANSWER = "show_answer";
	public static final String IMAGE_RADIO = "image_radio";
	public static final String IMAGE_PATH = "image_path";
	public static final String IMAGE_WIDTH = "image_width";
	public static final String IMAGE_HEIGHT = "image_height";
	public static final String TAG = "PayAnswerGrabItemFragment";
	public static final String FROM_CAMERA = "from_camera";
	private LinearLayout mAddPhotoContainer;
	private RelativeLayout mGrabItemAnswerPicContainer;
	private ImageView mAnswerPic;
	private PayAnswerCommonView mCommonView;
	private int mWidth;
	private int mHeight;
	private String mPath;
	private Bitmap mBitmap;
	int dey = 180000;
	private boolean haveAnswered;
	private boolean isCanClick = false;
	private QuestionModelGson mQuestionModelGson;
	// private View customView;
	private PowerManager pManager;
	private WakeLock mWakeLock;
	private TextView timeslimit_tv;
	// boolean isReAddAswer;
	// private static long answerid;
	private String msubject;
	private TextView timesup_tv;
	private ReportGrabQuestionWindow reportGrabQuestionWindow;

	private WelearnDialogBuilder mWelearnDialogBuilder;

	private RelativeLayout layout_zhuyi;
	private TextView tv_botton;

	MyHandler mHandler = new MyHandler(this);

	class MyHandler extends Handler {
		WeakReference<PayAnswerGrabItemActivity> weakReferenceActivity;

		public MyHandler(PayAnswerGrabItemActivity payAnswerGrabItemActivity) {
			super();
			this.weakReferenceActivity = new WeakReference<PayAnswerGrabItemActivity>(payAnswerGrabItemActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PayAnswerGrabItemActivity activity = weakReferenceActivity.get();
			if (activity != null) {
				if (msg.what == GlobalContant.CLOSEDIALOG) {
				} else if (msg.what == GlobalContant.LOOPMSG) {
					// int time = (Integer) msg.obj;
					timesupMin -= 1;
					if (timesupMin > 0) {
						timesup_tv.setText(timesupMin + "");
						msg = Message.obtain();
						msg.what = GlobalContant.LOOPMSG;
						// msg.obj = timesupMin;
						mHandler.sendMessageDelayed(msg, 60000);
					} else {
						timesup_tv.setText("0");// "超时"+timesupMin+"分钟"
					}

				}
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.fragment_grab_item);
		if (AppConfig.IS_DEBUG) {
			dey = 8000;
		}
		mGrabItemAnswerPicContainer = (RelativeLayout) findViewById(R.id.grabitem_answer_pic_container);
		mAddPhotoContainer = (LinearLayout) findViewById(R.id.add_photo_btn_container);
		mAnswerPic = (ImageView) findViewById(R.id.grabitem_photo_view_image);
		Button addPhoto = (Button) findViewById(R.id.add_photo_btn);
		mCommonView = (PayAnswerCommonView) findViewById(R.id.grab_item_common);

		timeslimit_tv = (TextView) findViewById(R.id.timeslimit_tv_grab_item);
		timesup_tv = (TextView) findViewById(R.id.timesup_tv_grab_item);

		TextView report = (TextView) findViewById(R.id.report_grab_item);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(R.string.text_grab_title);
		findViewById(R.id.back_layout).setOnClickListener(this);
		RelativeLayout nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_grab_item_submit);
		nextStepLayout.setOnClickListener(this);

		report.setOnClickListener(this);
		addPhoto.setOnClickListener(this);

		layout_zhuyi = (RelativeLayout) this.findViewById(R.id.layout_zhuyi);
		tv_botton = (TextView) findViewById(R.id.tv_botton);

		Intent intent = getIntent();
		if (intent != null) {

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showAnswerPicOrNot();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 是否展示答案图片
	 */
	private void showAnswerPicOrNot() {
		Intent intent = getIntent();
		haveAnswered = intent.getBooleanExtra(SHOW_ANSWER, false);
		if (haveAnswered) {
			showAnswerPic(intent);
		} else {
			hideAnswerPic();
		}
		showData(intent);
	}

	int timesupMin = 30;

	public long fromDateStringToLong(String inVal) { // 此方法计算时间毫秒
		Date date = null; // 定义时间类型
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = inputFormat.parse(inVal); // 将字符型转换成日期型
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime(); // 返回毫秒数
	}

	private void showData(Intent intent) {
		Map<String, String> currentJsonDataMap = TecApplication.jsonDataMap;
		String jsonString = intent.getStringExtra(PayAnswerFragment.GRAB_ITEM_DATA);

		if (TextUtils.isEmpty(jsonString)) {
			jsonString = currentJsonDataMap.get(PayAnswerFragment.GRAB_ITEM_DATA);
		} else {
			currentJsonDataMap.put(PayAnswerFragment.GRAB_ITEM_DATA, jsonString);
		}
		if (!TextUtils.isEmpty(jsonString)) {
			JSONObject jsonObj = null;
			try {
				jsonObj = new JSONObject(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mHandler.removeMessages(GlobalContant.LOOPMSG);

			mQuestionModelGson = new Gson().fromJson(jsonObj.toString(), QuestionModelGson.class);

			String grabtimeStr = mQuestionModelGson.getGrabtime();
			int limit_time_min = mQuestionModelGson.getLimit_time();

			if (!TextUtils.isEmpty(mQuestionModelGson.getBottomtip())) {
				layout_zhuyi.setVisibility(View.VISIBLE);
				tv_botton.setText(mQuestionModelGson.getBottomtip());
			} else {
				layout_zhuyi.setVisibility(View.GONE);
			}

			if (null != grabtimeStr) {
				// grabtimeStr = grabtimeStr.split("//.")[0];
				long grabtime_long = fromDateStringToLong(grabtimeStr);
				long failTime = grabtime_long + limit_time_min * 60000;
				long currentTimeMillis = System.currentTimeMillis();

				timesupMin = (int) ((failTime - currentTimeMillis) / 60000);
				if (timesupMin < 0) {
					timesupMin = 0;
				}
				if (timesupMin > limit_time_min) {
					timesupMin = limit_time_min;
				}
				timeslimit_tv.setText(limit_time_min + "");
				timesup_tv.setText(timesupMin + "");
				Message msg = Message.obtain();
				msg.what = GlobalContant.LOOPMSG;
				mHandler.sendMessageDelayed(msg, 60000);
			}

			int newqtn = JsonUtils.getInt(jsonObj, "newqtn", 0);
			if (newqtn == 1) {
				mQuestionModelGson.setNewUser(true);
			}
			// 保存mQuestionModelGson 防止提交的时候报空
			String jsonStr = JSON.toJSONString(mQuestionModelGson);
			SharePerfenceUtil.getInstance().putString("mQuestionModelGson", jsonStr);
			// ToastUtils.show(mActivity, "" +newqtn);
			msubject = mQuestionModelGson.getSubject();
			mCommonView.showData(mQuestionModelGson);
		}
	}

	/**
	 * 添加答案图标到答案图片上
	 */
	private void addAnswerIconInAnswerPic(Intent intent) {
		// 上个界面传过来屏幕显示的宽高
		srcWidth = intent.getIntExtra(IMAGE_WIDTH, 0);
		srcHeight = intent.getIntExtra(IMAGE_HEIGHT, 0);

		float ratio = (float) mWidth / (float) srcWidth;
		int newHeight = Math.round((srcHeight * ratio));

		mAnswerPic.setDrawingCacheEnabled(true);
		mAnswerPic.buildDrawingCache();
		Bitmap bitmap = ((BitmapDrawable) mAnswerPic.getDrawable()).getBitmap();
		mAnswerPic.setDrawingCacheEnabled(false);
		if (null != bitmap) {
			try {
				bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, newHeight, true);
				mAnswerPic.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		mHeight = newHeight;

		LinkedHashSet<ExplainPoint> pointSet = TecApplication.coordinateAnswerIconSet;
		Iterator<ExplainPoint> iter = pointSet.iterator();
		int count = 1;
		while (iter.hasNext()) {
			ExplainPoint point = iter.next();
			final String audioPath = point.getAudioPath();

			final String text = point.getText();

			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

			float x = point.getX();
			float y = point.getY();

			float widthRadio = (float) mWidth / (float) srcWidth;
			float heightRadio = (float) newHeight / (float) srcHeight;

			relativeParams.leftMargin = Math.round(x);
			relativeParams.topMargin = Math.round(y);

			CameraChoiceIconWithSer iconSer = new CameraChoiceIconWithSer(this,
					SharePerfenceUtil.getInstance().getUserRoleId(), point.getSubtype());
			iconSer.setLayoutParams(relativeParams);
			mGrabItemAnswerPicContainer.addView(iconSer);

			if (TextUtils.isEmpty(audioPath)) {
				iconSer.getIcView().setImageResource(R.drawable.ic_text_choic_t);
				iconSer.getmBgView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						// Bundle bundle = new Bundle();
						// bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
						// text);
						// IntentManager.goToAnswertextView(PayAnswerGrabItemActivity.this,
						// bundle);
						GlobalVariable.answertextPopupWindow = new AnswertextPopupWindow(PayAnswerGrabItemActivity.this,
								text);
					}
				});
			} else {
				final ImageView mGrabItemPlay = iconSer.getIcView();
				mGrabItemPlay.setImageResource(R.drawable.ic_play2);
				iconSer.getmBgView().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (TextUtils.isEmpty(audioPath)) {
							return;
						}
						mGrabItemPlay.setImageResource(R.drawable.play_animation);
						AnimationDrawable animationDrawable = (AnimationDrawable) mGrabItemPlay.getDrawable();
						TecApplication.animationDrawables.add(animationDrawable);
						TecApplication.anmimationPlayViews.add(mGrabItemPlay);

						MediaUtil.getInstance(false).stopPlay();
						MediaUtil.getInstance(false).playVoice(false, audioPath, animationDrawable,
								new ResetImageSourceCallback() {

									@Override
									public void reset() {
										if (null != mGrabItemPlay) {
											mGrabItemPlay.setImageResource(R.drawable.ic_play2);
										}
									}

									@Override
									public void playAnimation() {
									}

									@Override
									public void beforePlay() {
										MediaUtil.getInstance(false).resetAnimationPlay(mGrabItemPlay);
									}
								}, null);
					}
				});
			}
			iconSer.getIcSerView().setText(count + "");
			count++;
		}
	}

	private void showAnswerPic(final Intent intent) {
		mAddPhotoContainer.setVisibility(View.GONE);
		mGrabItemAnswerPicContainer.setVisibility(View.VISIBLE);

		mPath = intent.getStringExtra(IMAGE_PATH);
		try {
			mAnswerPic.setImageBitmap(BitmapFactory.decodeFile(mPath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		mGrabItemAnswerPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mWidth = mGrabItemAnswerPicContainer.getWidth();
				mHeight = mGrabItemAnswerPicContainer.getHeight();
				addAnswerIconInAnswerPic(intent);
				mGrabItemAnswerPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	private void hideAnswerPic() {
		mAddPhotoContainer.setVisibility(View.VISIBLE);
		mGrabItemAnswerPicContainer.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			goPrevious();
			break;
		case R.id.next_setp_layout:
			if (isCanClick) {
				ToastUtils.show("正在提交请稍后...");
			} else {

				submitQuestion();
			}
			break;
		case R.id.add_photo_btn:
			SharePerfenceUtil.getInstance().putInt("msubjectid", getSubjectid(msubject));
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(mQuestionModelGson.getImgurl());

			new CameraPopupWindow(this, view, GlobalContant.PAY_ASNWER, arrayList);
			break;
		case R.id.report_grab_item:// 举报
			reportGrabQuestionWindow = new ReportGrabQuestionWindow(this, view, this);
			// new CameraPopupWindow(mActivity, view, GlobalContant.PAY_ASNWER);
			break;
		}
	}

	public int getSubjectid(String str) {
		int mSubjectid = -1;
		if ("英语".equals(str)) {
			mSubjectid = 1;
		}
		if ("数学".equals(str)) {
			mSubjectid = 2;
		}
		if ("物理".equals(str)) {
			mSubjectid = 3;
		}
		if ("化学".equals(str)) {
			mSubjectid = 4;
		}
		if ("生物".equals(str)) {
			mSubjectid = 5;
		}
		if ("语文".equals(str)) {
			mSubjectid = 6;
		}

		return mSubjectid;

	}

	public void report(int reasonid,String reasonTxt,final String tipTxt) {
		LOG.e("----------> 举报");
		if (!NetworkUtils.getInstance().isInternetConnected(PayAnswerGrabItemActivity.this)) {
			ToastUtils.show("请检查网络");
			return;
		}
		if (null != mQuestionModelGson) {
			JSONObject data = new JSONObject();
			try {
				data.put("taskid", mQuestionModelGson.getQuestion_id());
				data.put("tasktype", GlobalContant.TASKTYPE_QUESTION);
				data.put("reason", reasonTxt);
				data.put("reasonid",reasonid);
				data.put("alarmtype",2);//举报方位 1换题 2抢题后


			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(this, "common", "report", data, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (TextUtils.isEmpty(tipTxt)){
						ToastUtils.show("谢谢您的举报，我们将尽快核实");
					}else {
						ToastUtils.show(tipTxt);
					}
					setResultAndFinish();
				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		}
	}

	private void submitQuestion() {
		if (haveAnswered && TecApplication.coordinateAnswerIconSet.size() > 0) {

			if (null == mWelearnDialogBuilder) {
				mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(this);
			}
			mWelearnDialogBuilder.withMessage(R.string.text_dialog_submit_answer)
					.setOkButtonClick(new OnClickListener() {
						@Override
						public void onClick(View v) {

							try {
								mWelearnDialogBuilder.dismiss();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (TextUtils.isEmpty(mPath)) {
								ToastUtils.show("答案图片路径为空");
								return;
							}
							if (mQuestionModelGson == null) {
								String str = SharePerfenceUtil.getInstance().getString("mQuestionModelGson", "");
								if (!TextUtils.isEmpty(str)) {
									mQuestionModelGson = JSON.parseObject(str, QuestionModelGson.class);
									SharePerfenceUtil.getInstance().putString("mQuestionModelGson", "");
								} else {
									ToastUtils.show("mQuestionModelGson 为空");
								}
							}

							if (!NetworkUtils.getInstance().isInternetConnected(PayAnswerGrabItemActivity.this)) {
								ToastUtils.show("网络未连接");
								isCanClick = false;
								return;
							}
							showDialog(getString(R.string.text_toast_send_answer_ing), false);
							JSONObject submitJson = new JSONObject();

							Map<String, List<File>> files = new HashMap<String, List<File>>();
							List<File> picFileList = new ArrayList<File>();
							picFileList.add(new File(mPath));

							Options opts = new Options();
							opts.inJustDecodeBounds = true;
							BitmapFactory.decodeFile(mPath, opts);
							double outWidth = opts.outWidth;
							double outHeight = opts.outHeight;
							double rateW = (outWidth / srcWidth);
							double rateH = (outHeight / srcHeight);
							int mX1 = DisplayUtils.dip2px(PayAnswerGrabItemActivity.this, 8);
							files.put("picfile", picFileList);

							List<File> sndFileList = new ArrayList<File>();
							JSONArray answer = new JSONArray();
							Iterator<ExplainPoint> iter = TecApplication.coordinateAnswerIconSet.iterator();
							try {
								while (iter.hasNext()) {
									ExplainPoint p = iter.next();
									JSONObject pointObj = new JSONObject();
									String audioPath = p.getAudioPath();
									int type = TextUtils.isEmpty(audioPath) ? 1 : 2;
									pointObj.put("type", type);
									pointObj.put("subtype", p.getSubtype());
									pointObj.put("textcontent", p.getText());
									pointObj.put("coordinate", (p.getX() - 8) * rateW + "," + (p.getY() - 8) * rateH);
									answer.put(pointObj);
									if (type == GlobalContant.ANSWER_AUDIO) {
										sndFileList.add(new File(audioPath));
									}
								}
								files.put("sndfile", sndFileList);

								submitJson.put("answer_id", mQuestionModelGson.getAnswerid());
								submitJson.put("question_id", mQuestionModelGson.getQuestion_id());
								submitJson.put("answer", answer);

							} catch (JSONException e1) {
								e1.printStackTrace();
							}

							isCanClick = true;
							// UploadUtil.upload(AppConfig.GO_URL +
							// "teacher/questionanswer",
							// RequestParamUtils.getParam(submitJson), files,
							// PayAnswerGrabItemActivity.this, true,
							// 0);
							// 修改上传方式okhttp
							UploadUtil2.upload(AppConfig.GO_URL + "teacher/questionanswer",
									RequestParamUtils.getMapParam(submitJson), files, new MyStringCallback(), true, 0);

						}
					});
			mWelearnDialogBuilder.show();
		} else {
			ToastUtils.show("请先回答难题");
		}
	}

	/////////////////////////////////////////////////
	public class MyStringCallback extends StringCallback {
		/**
		 * UI Thread
		 */
		@Override
		public void onBefore(Request request) {
			super.onBefore(request);
			// ToastUtils.show("onBefore");
		}

		/**
		 * UI Thread
		 */
		@Override
		public void onAfter() {
			super.onAfter();
			// ToastUtils.show("onAfter");

		}

		@Override
		public void onError(Call call, Exception e) {
			isCanClick = false;
			closeDialog();
			String errorMsg = "";
			if (e != null && !TextUtils.isEmpty(e.getMessage())) {
				errorMsg = e.getMessage();
			} else {
				errorMsg = e.getClass().getSimpleName();
			}
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("onError:" + errorMsg);
			} else {
				ToastUtils.show("请检查的网络连接");
			}
		}

		@Override
		public void onResponse(String response) {
			// ToastUtils.show("onResponse:" + response);
			closeDialog();
			isCanClick = false;
			int code = JsonUtils.getInt(response, "Code", -1);
			String msg = JsonUtils.getString(response, "Msg", "");
			String data = JsonUtils.getString(response, "Data", "");
			if (code == 0) {
				setResultAndFinish();
			} else {
				if (!TextUtils.isEmpty(msg)) {
					if (msg.contains("已经提交")){
						setResultAndFinish();
					}else{
						ToastUtils.show(msg);
					}
				}
			}
		}

		/**
		 * UI Thread
		 */
		@Override
		public void inProgress(float progress) {
			// Log.e(TAG, "inProgress:" + progress);
			// mProgressBar.setProgress((int) (100 * progress));
		}
	}

	/*@Override
	public void onUploadSuccess(UploadResult result, int index) {
		isCanClick = false;
		closeDialog();
		if (result.getCode() == 0) {
			setResultAndFinish();
		} else {
			String msg = result.getMsg();
			if (msg != null) {
				if ("".equals(msg)) {
					msg = "上传失败";
				}
				ToastUtils.show(msg);
			}
		}

	}

	@Override
	public void onUploadError(String msg, int index) {
		isCanClick = false;
		closeDialog();
		if ("".equals(msg)) {
			msg = "上传失败";
		}
		ToastUtils.show(msg);
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		isCanClick = false;
		closeDialog();
		String msg = result.getMsg();
		if (msg != null) {
			if ("".equals(msg)) {
				msg = "上传失败";
			}
			ToastUtils.show(msg);
		}
	}*/

	private void clear() {
		TecApplication.coordinateAnswerIconSet.clear();
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
	}

	public void goPrevious() {
		if (reportGrabQuestionWindow != null && reportGrabQuestionWindow.isShowing()) {
			reportGrabQuestionWindow.dismiss();
		} else {
			if (null == mWelearnDialogBuilder) {
				mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(this);
			}
			mWelearnDialogBuilder.withMessage(R.string.text_dialog_tips_give_up)
					.setOkButtonClick(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								mWelearnDialogBuilder.dismiss();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (null == mQuestionModelGson) {
								return;
							}
							JSONObject data = new JSONObject();
							try {
								data.put("question_id", mQuestionModelGson.getQuestion_id());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							OkHttpHelper.post(PayAnswerGrabItemActivity.this, "teacher", "giveupquestion",
									 data, new HttpListener() {
										
										@Override
										public void onSuccess(int code, String dataJson, String errMsg) {
											clear();
											setResultAndFinish();											
										}
										
										@Override
										public void onFail(int HttpCode,String errMsg) {
											
										}
									});
						}
					});
			mWelearnDialogBuilder.show();
		}
	}

	private int srcWidth;
	private int srcHeight;

	private void setResultAndFinish() {
		LOG.e("----------> 准备关闭抢题界面");
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP) {
			if (resultCode == RESULT_OK) {
				String savePath = data.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
				Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,AppUtils.getUriForFile(this,new File(savePath)));
				sendBroadcast(localIntent);

				Bundle bundle = new Bundle();
				bundle.putString(PayAnswerImageGridActivity.IMAGE_PATH, savePath);
				bundle.putBoolean("ifFirst", true);
				ArrayList<String> arrayList = new ArrayList<String>();
				if (mQuestionModelGson == null) {
					String str = SharePerfenceUtil.getInstance().getString("mQuestionModelGson", "");
					if (!TextUtils.isEmpty(str)) {
						mQuestionModelGson = JSON.parseObject(str, QuestionModelGson.class);
					} else {
						ToastUtils.show("mQuestionModelGson 为空");
					}
				}
				arrayList.add(mQuestionModelGson.getImgurl());
				bundle.putStringArrayList("viewPagerList", arrayList);
				IntentManager.goToPhotoView(this, bundle);
			}
		}else if(requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE){


			String path = MyFileUtil.getAnswerFile().getAbsolutePath() + File.separator + "publish.png";
			Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, AppUtils.getUriForFile(this,new File(path)));
			sendBroadcast(localIntent);

			localIntent.setClass(this, CropImageActivity.class);
			localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
			localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, path);
			localIntent.putExtra("isFromPhotoList", false);
			startActivityForResult(localIntent, GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP);
		}
	}

	@Override
	public void onBackPressed() {
		if (GlobalVariable.answertextPopupWindow != null && GlobalVariable.answertextPopupWindow.isShowing()) {
			GlobalVariable.answertextPopupWindow.dismiss();
			return;
		}
		goPrevious();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCommonView.stopAudio();
		clear();
		mHandler.removeCallbacksAndMessages(null);
	}

}

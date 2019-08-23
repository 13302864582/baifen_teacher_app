package com.ucuxin.ucuxin.tec.function.question;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.CameraChoiceIconWithSer;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.AnswerDetail;
import com.ucuxin.ucuxin.tec.model.AnswerSound;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Request;

public class PayAnswerAppendAskFragment extends PayAnswerPhotoViewFragment implements OnClickListener/*, OnUploadListener*/ {

	public static final String JSON_DATA = "json_data";
	public static final String ANSWER_INDEX = "answer_index";
	public static final String APPEND_ANSWER = "append_answer";
	private static final String TAG = PayAnswerAppendAskFragment.class.getSimpleName();

	// private boolean isAppendAnswer;
	private NetworkImageView mUserAvatar;
	private TextView mUsername;
	private TextView mSchoolName;
	private AnswerDetail mAnswerDetail;
	private AnimationDrawable mAnimationDrawable;
	// private AppendAnswerController mAppendAnswerController;
	private List<CameraChoiceIconWithSer> answerIcsList = new ArrayList<CameraChoiceIconWithSer>();
	private int avatarSize;

	private AnswerDetail getQuestionDetailGson() {
		String jsonStr = mActivity.getIntent().getStringExtra(JSON_DATA);
		int index = mActivity.getIntent().getIntExtra(ANSWER_INDEX, 1);
		JSONArray answerArray = JsonUtils.getJSONArray(jsonStr, "answer", null);
		// Log.i (TAG, answerArray.toString());
		Gson gson = new Gson();
		List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(), new TypeToken<ArrayList<AnswerDetail>>() {
		}.getType());
		return answerList.get(index - 1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mUserAvatar = (NetworkImageView) view.findViewById(R.id.append_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.append_user_avatar_size);
		mUsername = (TextView) view.findViewById(R.id.append_user_name);
		mSchoolName = (TextView) view.findViewById(R.id.append_user_colleage);
		TextView back = (TextView) view.findViewById(R.id.photo_view_nav_btn_back);
		back.setText(R.string.text_nav_cancel);
		mUserAvatar.setOnClickListener(this);
		mUsername.setOnClickListener(this);
		mSchoolName.setOnClickListener(this);

		showData();
		return view;
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.append_user_avatar:
		case R.id.append_user_name:
		case R.id.append_user_colleage:
			IntentManager.gotoPersonalPage(mActivity, mAnswerDetail.getGrabuserid(), mAnswerDetail.getRoleid());
			break;
		}
	}

	@Override
	protected void showBottom() {
		mRotateContainer.setVisibility(View.GONE);
		mUserinfoContainer.setVisibility(View.VISIBLE);
	}

	@Override
	protected void hideBottom() {
		mRotateContainer.setVisibility(View.GONE);
		mUserinfoContainer.setVisibility(View.GONE);
	}

	private void showData() {

		mAnswerDetail = getQuestionDetailGson();

		showAnswerPic(mAnswerDetail);

		showUserInfo(mAnswerDetail);
	}

	private void showAnswerPic(AnswerDetail model) {
		// ImageLoader.getInstance().displayImage(model.getA_imgurl(),
		// mPhotoImage, new SimpleImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String imageUri, View view) {}
		//
		// @Override
		// public void onLoadingComplete(String imageUri, View view,
		// Bitmap loadedImage) {
		// showAnswer(loadedImage);
		// }
		//
		// }, null);

		ImageLoader.getInstance().loadImage(model.getA_imgurl(), mPhotoImage, 0, new OnImageLoadListener() {

			@Override
			public void onSuccess(ImageContainer response) {
				if (response != null) {
					Bitmap bitmap = response.getBitmap();
					if (bitmap != null) {
						showAnswer(bitmap);
					}
				}

			}

			@Override
			public void onFailed(VolleyError error) {

			}
		});
	}

	private void showUserInfo(AnswerDetail model) {
		ImageLoader.getInstance().loadImageWithDefaultAvatar(model.getT_avatar(), mUserAvatar, avatarSize,
				avatarSize / 10);
		mUsername.setText(model.getGrabuser());
	}

	private int currentWidth;
	private int currentHeight;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showAnswer(Bitmap loadedImage) {
		mPhotoImage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				currentWidth = mPhotoImage.getWidth();
				currentHeight = mPhotoImage.getHeight();

				final int srcWidth = mAnswerDetail.getWidth();
				final int srcHeight = mAnswerDetail.getHeight();

				showAnswerInconInPic(currentWidth, currentHeight, srcWidth, srcHeight);
				mPhotoImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}

			private void showAnswerInconInPic(int currentWidth, int currentHeight, int srcWidth, int srcHeight) {
				List<AnswerSound> soundList = mAnswerDetail.getAnswer_snd();
				int count = 1;
				for (final AnswerSound as : soundList) {
					int type = as.getType();
					RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
							android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
					String[] coordinate = as.getCoordinate().split(",");
					float srcX = Float.parseFloat(coordinate[0]);
					float srcY = Float.parseFloat(coordinate[1]);
					float widthRadio = srcX / (float) srcWidth;
					float heightRadio = srcY / (float) srcHeight;

					float currentX = widthRadio * currentWidth;
					float currentY = heightRadio * currentHeight;

					relativeParams.leftMargin = (int) (currentX < 0 ? 0 : currentX);
					relativeParams.topMargin = (int) (currentY < 0 ? 0 : currentY);
					
					int frameWidthOrHeight = DisplayUtils.dip2px(mActivity, 56);
					if (relativeParams.leftMargin > currentWidth) {
						relativeParams.leftMargin = currentWidth -  (frameWidthOrHeight);
					}
					if (relativeParams.topMargin > currentHeight) {
						relativeParams.topMargin = currentHeight -  (frameWidthOrHeight);
					}
					
					//转化后的坐标存入已上传打点坐标集合，用于判断新打点时是否重叠
					ExplainPoint point = new ExplainPoint();
					point.setX(relativeParams.leftMargin);
					point.setY(relativeParams.topMargin);
					point.setRole(as.getRole());
					points2.add(point);
					
					CameraChoiceIconWithSer iconSer = new CameraChoiceIconWithSer(mActivity, as.getRole(),as.getSubtype());
					iconSer.setLayoutParams(relativeParams);
					
					mAnswerPicContainer.addView(iconSer);
					iconSer.getIcSerView().setText(count++ + "");
					answerIcsList.add(iconSer);

					final ImageView mGrabItemIcView = iconSer.getIcView();
					if (type == GlobalContant.ANSWER_AUDIO) {// 声音
						mGrabItemIcView.setImageResource(R.drawable.ic_play2);
						iconSer.getmBgView().setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								if (TextUtils.isEmpty(as.getQ_sndurl())) {
									return;
								}
								mGrabItemIcView.setImageResource(R.drawable.play_animation);
								mAnimationDrawable = (AnimationDrawable) mGrabItemIcView.getDrawable();
								TecApplication.animationDrawables.add(mAnimationDrawable);
								TecApplication.anmimationPlayViews.add(mGrabItemIcView);
								MediaUtil.getInstance(false).stopPlay();
								MediaUtil.getInstance(false).playVoice(false, as.getQ_sndurl(),
										mAnimationDrawable, new ResetImageSourceCallback() {

											@Override
											public void reset() {
												mGrabItemIcView.setImageResource(R.drawable.ic_play2);
											}

											@Override
											public void playAnimation() {
											}

											@Override
											public void beforePlay() {
												MediaUtil.getInstance(false).resetAnimationPlay(mGrabItemIcView);
											}
										}, null);
							}
						});
					} else if (type == GlobalContant.ANSWER_TEXT) {
						mGrabItemIcView.setImageResource(R.drawable.ic_text_choic_t);
						iconSer.getmBgView().setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
//								Bundle bundle = new Bundle();
//								bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT, as.getTextcontent());
//								IntentManager.goToAnswertextView(mActivity, bundle);
								GlobalVariable.answertextPopupWindow=new AnswertextPopupWindow(mActivity, as.getTextcontent());
							}
						});
					}
				}
			}
		});
	}

	@Override
	public void sureBtnClick() {
		final LinkedHashSet<ExplainPoint> points = TecApplication.coordinateAnswerIconSet;
		if (points.size() == 0) {
			ToastUtils.show(getString(R.string.text_toast_append_ask));
			return;
		}
		int orignWidth = mAnswerDetail.getWidth();
		int orignHeight = mAnswerDetail.getHeight();
		int mX1 = DisplayUtils.dip2px(mActivity, 8);
		for (ExplainPoint p : points) {
			float x = p.getX()-mX1;
			float y = p.getY()-mX1;
			// 原始坐标=原有尺寸/现有尺寸*现有坐标
			float srcX = ((float) orignWidth / (float) currentWidth) * x;
			float srcY = ((float) orignHeight / (float) currentHeight) * y;
			p.setX(srcX);
			p.setY(srcY);
		}

		showDialog(getResources().getString(R.string.text_toast_send_answer_ing));
		JSONObject submitJson = new JSONObject();

		Map<String, List<File>> files = new HashMap<String, List<File>>();

		List<File> sndFileList = new ArrayList<File>();
		JSONArray pointlist = new JSONArray();
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
				pointObj.put("coordinate", p.getX() + "," + p.getY());
				pointlist.put(pointObj);
				if (type == 2) {
					sndFileList.add(new File(audioPath));
				}
			}
			files.put("sndfile", sndFileList);

			submitJson.put("answer_id", mAnswerDetail.getAnswer_id());
			submitJson.put("pointlist", pointlist);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

//		UploadUtil.upload(AppConfig.GO_URL + "question/append", RequestParamUtils.getParam(submitJson), files, this,
//				true, 0);
		UploadUtil2.upload(AppConfig.GO_URL + "question/append", RequestParamUtils.getMapParam(submitJson), files, new MyStringCallback(), true, 0);
		LogUtils.i(TAG, points.toString());
	}
	
	class MyStringCallback extends StringCallback{
		
		@Override
		public void onBefore(Request request) {
			super.onBefore(request);
		}
		
		@Override
		public void onAfter() {
			super.onAfter();
		}

		@Override
		public void onError(Call call, Exception e) {
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
				ToastUtils.show("网络异常");
			}

		}

		@Override
		public void onResponse(String response) {
			closeDialog();
			int code = JsonUtils.getInt(response, "Code", -1);
			String msg = JsonUtils.getString(response, "Msg", "");
			String data = JsonUtils.getString(response, "Data", "");
			if (code == 0) {
				mActivity.setResult(Activity.RESULT_OK);
				mActivity.finish();
			} else {
				ToastUtils.show(msg);
			}
			
		}
		
	}

	

	/*@Override
	public void onUploadSuccess(UploadResult result, int index) {
		closeDialog();
		if (result.getCode() == 0) {
			mActivity.setResult(Activity.RESULT_OK);
			mActivity.finish();
		} else {
			ToastUtils.show(result.getMsg());
		}

	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		ToastUtils.show(msg);
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		String msg = result.getMsg();
		if (msg != null) {
			ToastUtils.show(msg);
		}
	}*/
	
	@Override
	protected void goBack() {
		mActivity.finish();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		TecApplication.coordinateAnswerIconSet.clear();
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
	}
}

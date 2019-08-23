package com.ucuxin.ucuxin.tec.function.question;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.StateConstant;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.function.CameraChoiceIconWithSer;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.AnswerDetail;
import com.ucuxin.ucuxin.tec.model.AnswerSound;
import com.ucuxin.ucuxin.tec.model.QuestionDetailGson;
import com.ucuxin.ucuxin.tec.reveiver.HeadsetPlugReceiver;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView.OnScaleListener;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OneQuestionMoreAnswersDetailItemFragment extends BaseFragment implements OnClickListener {

	private int currentPageIndex;
	private String jsonStr;
	private boolean isQpad;
	private TextView answerDesc;
	private NetworkImageView detailUserAvatar;
	private TextView detailUserName;
	private TextView detailUserColleage;
	// private TextView detailUpBtn;
	private DragImageView detailImage;
	private ProgressBar mBar;
	private RelativeLayout mDetailContainer;
	private QuestionDetailGson mQuestionDetailGson;
	private AnswerDetail mAnswerDetail;
	private DragImageView mQuestionImg;
	// private QuestionDetailController mQuestionDetailController;
	private TextView mAnswerBtn1;
	private TextView mAnswerBtn2;
	private TextView mAnswerBtn3;
	private HeadsetPlugReceiver headsetPlugReceiver;
	private Bitmap mBitmap;
	private RelativeLayout imageParentLayout;

	private int window_width, window_height;// 控件宽度
	private ImageView rotate_btn;

	private WelearnDialogBuilder mWelearnDialogBuilder;

	public static final String TAG = OneQuestionMoreAnswersDetailItemFragment.class.getSimpleName();

	private boolean isShowTips = true;

	private int avatarSize;

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		this.getActivity().getApplication().registerReceiver(headsetPlugReceiver, intentFilter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_answer_detail_view, null);
		initView(view,savedInstanceState);
		initListener();
		return view;
	}
	
	@Override
	public void initView(View view) {

		
	}
	
	
	public void initView(View view, Bundle savedInstanceState) {
		// add headset receiver
		registerHeadsetPlugReceiver();

		/*
		 * mActionBar.setDisplayShowHomeEnabled(false);
		 * mActionBar.setLogo(R.drawable.bg_actionbar_back_up_selector);
		 * mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector);
		 */
		
		imageParentLayout = (RelativeLayout) view.findViewById(R.id.image_parent_layout);
		imageParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = imageParentLayout.getHeight();
					window_width = imageParentLayout.getWidth();

					mQuestionImg.setScreenSize(window_width, window_height);
					detailImage.setScreenSize(window_width, window_height);
				}
			}
		});
		answerDesc = (TextView) view.findViewById(R.id.answer_source);
		detailUserAvatar = (NetworkImageView) view.findViewById(R.id.detail_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.one_question_more_answer_detail_item_avatar_size);
		detailUserName = (TextView) view.findViewById(R.id.detail_user_name);
		detailUserColleage = (TextView) view.findViewById(R.id.detail_user_colleage);
		// detailUpBtn = (TextView) view.findViewById(R.id.detail_up_btn);
		// detailUpBtn.setOnClickListener(this);
		detailImage = (DragImageView) view.findViewById(R.id.detail_image);
		// detailImage.setEnableDrag(false);
		detailImage.setOnScaleListener(new OnScaleListener() {
			@Override
			public void onScale(boolean isScale) {
				if (isShowTips != !isScale) {
					isShowTips = !isScale;
					showTips(!isScale);
					if (null != mOnTipsShowListener) {
						mOnTipsShowListener.onTipsShow(!isScale);
					}
				}
			}
		});

		mBar = (ProgressBar) view.findViewById(R.id.img_loading);
		mQuestionImg = (DragImageView) view.findViewById(R.id.detail_question_image);

		mDetailContainer = (RelativeLayout) view.findViewById(R.id.anser_detail_container);
		mAnswerBtn1 = (TextView) view.findViewById(R.id.answer_btn1);
		mAnswerBtn2 = (TextView) view.findViewById(R.id.answer_btn2);
		mAnswerBtn3 = (TextView) view.findViewById(R.id.answer_btn3);

		rotate_btn = (ImageView) view.findViewById(R.id.rotate_btn_answerdetail);

		detailUserName.setOnClickListener(this);
		detailUserColleage.setOnClickListener(this);
		detailUserAvatar.setOnClickListener(this);
		mAnswerBtn1.setOnClickListener(this);
		mAnswerBtn2.setOnClickListener(this);
		mAnswerBtn3.setOnClickListener(this);
		rotate_btn.setOnClickListener(this);
		if (savedInstanceState != null) {
			currentPageIndex = savedInstanceState.getInt("position", -1);
			jsonStr = savedInstanceState.getString("dataStr");
			isQpad = savedInstanceState.getBoolean("isqpad");
		} else {
			currentPageIndex = getArguments().getInt("position");
			jsonStr = getArguments().getString("dataStr");
			isQpad = getArguments().getBoolean("isqpad");
		}
		showData();	
		
	}
	
	@Override
	public void initListener() {

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private List<CameraChoiceIconWithSer> answerIcsList = new ArrayList<CameraChoiceIconWithSer>();

	@SuppressLint("NewApi")
	public void showTips(boolean isShow) {

		if (null == mDetailContainer) {
			return;
		}

		int show = isShow ? View.VISIBLE : View.GONE;

		RelativeLayout.LayoutParams lp = (LayoutParams) mDetailContainer.getLayoutParams();
		if (isShow) {
			lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
			lp.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		} else {
			lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
			lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
		}
		mDetailContainer.setLayoutParams(lp);

		View v = null;
		for (int i = 0; i < mDetailContainer.getChildCount(); i++) {
			v = mDetailContainer.getChildAt(i);
			if (null != v) {
				if (!(v instanceof DragImageView)) {
					v.setVisibility(show);
				}
			}
		}
		if (isShow) {
			try {
				detailImage.callOnClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		isShowTips = isShow;
	}

	private QuestionDetailGson getQuestionDetailGson() {
		JSONObject questionObj = JsonUtils.getJSONObject(jsonStr, "question", null);
		// Log.i(TAG, questionObj.toString());
		// add by milo 2014.09.15 crash bug fixed
		if (questionObj != null) {
			return new Gson().fromJson(questionObj.toString(), QuestionDetailGson.class);
		}
		return new QuestionDetailGson();
	}

	private AnswerDetail getAnswerDetail() {
		JSONArray answerArray = JsonUtils.getJSONArray(jsonStr, "answer", null);
		// Log.i (TAG, answerArray.toString());
		// add by milo 2014.09.15 crash bug fixed
		if (answerArray != null) {
			Gson gson = new Gson();
			List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(),
					new TypeToken<ArrayList<AnswerDetail>>() {
					}.getType());
			return answerList.get(currentPageIndex - 1);
		}
		return null;
	}

	private void hideAppendAdoptAbourBtn() {
		mAnswerBtn1.setVisibility(View.GONE);
		mAnswerBtn3.setVisibility(View.GONE);
		mAnswerBtn2.setVisibility(View.GONE);
	}

	private void showData() {
		if (isShowQuestion()) {
			rotate_btn.setVisibility(View.VISIBLE);
			mQuestionImg.setVisibility(View.VISIBLE);
			mQuestionDetailGson = getQuestionDetailGson();
			answerDesc.setVisibility(View.VISIBLE);
			String answerDescStr = mQuestionDetailGson.getSource();
			if (TextUtils.isEmpty(answerDescStr)) {
				answerDescStr = "";
			}
			answerDesc.setText(answerDescStr);
			ImageLoader.getInstance().loadImageWithDefaultAvatar(mQuestionDetailGson.getAvatar(), detailUserAvatar,
					avatarSize, avatarSize / 10);

			ImageLoader.getInstance().loadImage(mQuestionDetailGson.getQ_imgurl(), mQuestionImg, R.drawable.loading,
					R.drawable.retry, new OnImageLoadListener() {
						@Override
						public void onSuccess(ImageContainer arg0) {
							if (arg0 != null) {
								mBitmap = arg0.getBitmap();
							}
							mBar.setVisibility(View.GONE);
						}

						@Override
						public void onFailed(VolleyError arg0) {

						}
					});

			detailUserName.setText(mQuestionDetailGson.getStudname());
			detailUserColleage.setText(mQuestionDetailGson.getGrade());
		} else {
			rotate_btn.setVisibility(View.GONE);
			mQuestionImg.setVisibility(View.GONE);
			JSONArray answerArray = JsonUtils.getJSONArray(jsonStr, "answer", null);
			LogUtils.i(TAG, answerArray.toString());
			Gson gson = new Gson();
			List<AnswerDetail> answerList = gson.fromJson(answerArray.toString(),
					new TypeToken<ArrayList<AnswerDetail>>() {
					}.getType());
			answerDesc.setVisibility(View.GONE);
			mAnswerDetail = answerList.get(currentPageIndex - 1);
			detailUserColleage.setText(mAnswerDetail.getSchools());
			ImageLoader.getInstance().loadImageWithDefaultAvatar(mAnswerDetail.getT_avatar(), detailUserAvatar,
					avatarSize, avatarSize / 10);

			ImageLoader.getInstance().loadImage(mAnswerDetail.getA_imgurl(), detailImage, R.drawable.loading,
					R.drawable.retry, new OnImageLoadListener() {
						@Override
						public void onSuccess(ImageContainer arg0) {
							mBar.setVisibility(View.GONE);
							Bitmap bm = arg0.getBitmap();
							if (null != bm) {
								detailImage.setImageBitmap(bm);
								showAnswer(arg0.getBitmap());
							}
						}

						@Override
						public void onFailed(VolleyError arg0) {

						}
					});

			detailUserName.setText(mAnswerDetail.getGrabuser() + "   " + mAnswerDetail.getGrabuserid());
		}

		if (isQpad) {
			// detailUpBtn.setVisibility(View.GONE);// 我的q板隐藏点赞
			int currentUserid = SharePerfenceUtil.getInstance().getUserId();
			if (isShowQuestion()) {// 隐藏追问 采纳 拒绝
				hideAppendAdoptAbourBtn();
			} else {// 如果是学生
				if (SharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_STUDENT) {
					// 如果状态是已回答或者追问中,则显示追问 采纳 拒绝
					int a_state = mAnswerDetail.getA_state();
					if (a_state == StateConstant.STATUS_ANSWER_ANSWERED
							|| a_state == StateConstant.STATUS_ANSWER_APPEND) {
						mAnswerBtn1.setText(getString(R.string.text_append_ask));
						mAnswerBtn1.setTag(StateConstant.STATUS_ANSWER_APPEND);

						mAnswerBtn2.setText(getString(R.string.text_adopt));
						mAnswerBtn2.setTag(StateConstant.STATUS_ANSWER_ADOPTED);

						mAnswerBtn3.setText(getString(R.string.text_abour));
						mAnswerBtn3.setTag(StateConstant.STATUS_ANSWER_ABOURED);
						// 增加了已拒绝和仲裁中也可以采纳答案
					} else if (a_state == StateConstant.STATUS_ANSWER_ABOURED
							|| a_state == StateConstant.STATUS_ANSWER_ARBITRATION) {
						mAnswerBtn2.setText(getString(R.string.text_adopt));
						mAnswerBtn2.setTag(StateConstant.STATUS_ANSWER_ADOPTED);
						mAnswerBtn1.setVisibility(View.INVISIBLE);
						mAnswerBtn1.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);
					} else {
						hideAppendAdoptAbourBtn();
					}
				} else if (SharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_COLLEAGE) {
					// 老师 已拒绝 申请仲裁 放弃仲裁
					if (mAnswerDetail.getA_state() == StateConstant.STATUS_ANSWER_ABOURED) {
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 申请仲裁
						mAnswerBtn1.setText(getString(R.string.text_arbitration_apply));
						mAnswerBtn1.setTag(StateConstant.STATUS_ANSWER_ARBITRATION);

						// 放弃仲裁
						mAnswerBtn2.setText(getString(R.string.text_arbitration_abour));
						mAnswerBtn2.setTag(StateConstant.STATUS_ANSWER_ABOUR_ARBITRATION);
					} else if (mAnswerDetail.getA_state() == StateConstant.STATUS_ANSWER_ANSWERED
							|| mAnswerDetail.getA_state() == StateConstant.STATUS_ANSWER_APPEND) {
						// 已回答 追问中 添加回答 就没有申请仲裁 放弃仲裁
						mAnswerBtn1.setVisibility(View.VISIBLE);
						mAnswerBtn2.setVisibility(View.INVISIBLE);
						mAnswerBtn2.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 回答
						mAnswerBtn1.setText(getString(R.string.text_reply));
						mAnswerBtn1.setTag(StateConstant.STATUS_ANSWER_ANSWERED);
					} else if (mAnswerDetail.getA_state() == StateConstant.STATUS_ANSWER_ARBITRATION) {// 仲裁中
						// 仲裁中可以放弃仲裁
						mAnswerBtn1.setVisibility(View.INVISIBLE);
						mAnswerBtn1.setClickable(false);
						mAnswerBtn3.setVisibility(View.INVISIBLE);
						mAnswerBtn3.setClickable(false);

						// 放弃仲裁
						mAnswerBtn2.setText(getString(R.string.text_arbitration_abour));
						mAnswerBtn2.setTag(StateConstant.STATUS_ANSWER_ABOUR_ARBITRATION);
					} else {
						hideAppendAdoptAbourBtn();
					}
				}
				if (currentUserid != getQuestionDetailGson().getStudid()
						&& currentUserid != getAnswerDetail().getGrabuserid()) {

					hideAppendAdoptAbourBtn();
				}
			}
		} else {
			hideAppendAdoptAbourBtn();
		}
	}

	public interface SureBtnClick {
		void ensure();
	}

	public interface AdoptSubmitBtnClick {
		void ensure(int effect, int attitude);
	}

	/**
	 * 图片旋转
	 */
	private void rotate() {

		if (mBitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);

			Bitmap resizedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
					true);
			/*
			 * if (mBitmap != null && !mBitmap.isRecycled()) {
			 * mBitmap.recycle(); mBitmap = null; }
			 */
			mBitmap = resizedBitmap;
			mQuestionImg.setImageBitmap(resizedBitmap);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.answer_btn1:
			int tag = Integer.parseInt(v.getTag().toString());
			if (tag == StateConstant.STATUS_ANSWER_APPEND) {// 追问
			} else if (tag == StateConstant.STATUS_ANSWER_ARBITRATION) {// 申请仲裁
				MobclickAgent.onEvent(mActivity, "Arbitrate");

				if (null == mWelearnDialogBuilder) {
					mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(getActivity());
				}
				mWelearnDialogBuilder.withMessage(R.string.text_dialog_tips_arbitration_apply)
						.setOkButtonClick(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								try {
									mWelearnDialogBuilder.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
								JSONObject data = new JSONObject();
								int answer_id = mAnswerDetail.getAnswer_id();
								try {
									data.put("answer_id", answer_id);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								BaseActivity activity = null;
								if (mActivity instanceof BaseActivity) {
									activity = (BaseActivity) mActivity;
								}
								OkHttpHelper.post(mActivity, "teacher", "applyquestionarbitration",  data, new HttpListener() {
									
									@Override
									public void onSuccess(int code, String dataJson, String errMsg) {
										setResultAndFinish();										
									}
									
									@Override
									public void onFail(int HttpCode,String errMsg) {
										
										
									}
								});
							}
						});
				mWelearnDialogBuilder.show();

			} else if (tag == StateConstant.STATUS_ANSWER_ANSWERED) {// 回答
				// MobclickAgent.onEvent(mActivity, "Accept");
				
				Intent intent = new Intent(mActivity, PayAnswerAppendAskActivity.class);
				intent.putExtra(PayAnswerAppendAskFragment.JSON_DATA, jsonStr);
				intent.putExtra(PayAnswerAppendAskFragment.ANSWER_INDEX, currentPageIndex);
				intent.putExtra(PayAnswerAppendAskFragment.APPEND_ANSWER, true);
				ArrayList<String> arrayList = new ArrayList<String>();
				
				if(mQuestionDetailGson==null){
					mQuestionDetailGson = getQuestionDetailGson();	
				}
				arrayList.add(mQuestionDetailGson.getQ_imgurl());
				intent.putStringArrayListExtra("viewPagerList", arrayList);
				mActivity.startActivityForResult(intent, GlobalContant.APPEND_ANSWER_REQUEST_CODE);
			}
			break;
		case R.id.answer_btn2:
			int tag2 = Integer.parseInt(v.getTag().toString());
			if (tag2 == StateConstant.STATUS_ANSWER_ADOPTED) {// 采纳
			} else if (tag2 == StateConstant.STATUS_ANSWER_ABOUR_ARBITRATION) {// 放弃仲裁
				MobclickAgent.onEvent(mActivity, "CancelArbitrate");
				if (null == mWelearnDialogBuilder) {
					mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(getActivity());
				}
				mWelearnDialogBuilder.withMessage(R.string.text_dialog_tips_arbitration_abour)
						.setOkButtonClick(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								try {
									mWelearnDialogBuilder.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
								JSONObject data = new JSONObject();
								int answer_id = mAnswerDetail.getAnswer_id();
								try {
									data.put("answer_id", answer_id);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								BaseActivity activity = null;
								if (mActivity instanceof BaseActivity) {
									activity = (BaseActivity) mActivity;
								}
								OkHttpHelper.post(mActivity, "teacher", "giveupquestionarbitration",  data, new HttpListener() {
									
									@Override
									public void onSuccess(int code, String dataJson, String errMsg) {
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
			break;
		case R.id.answer_btn3:
			break;
		case R.id.rotate_btn_answerdetail:
			rotate();
			break;
		case R.id.detail_user_avatar:
		case R.id.detail_user_name:
		case R.id.detail_user_colleage:
			if (isShowQuestion()) {
				IntentManager.gotoPersonalPage(mActivity, mQuestionDetailGson.getStudid(),
						mQuestionDetailGson.getRoleid());
			} else {
				IntentManager.gotoPersonalPage(mActivity, mAnswerDetail.getGrabuserid(), mAnswerDetail.getRoleid());
			}
			break;
		}
	}

	private void setResultAndFinish() {
		mActivity.setResult(Activity.RESULT_OK);
		mActivity.finish();
	}

	private AnimationDrawable mAnimationDrawable;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void showAnswer(final Bitmap loadedImage) {
		detailImage.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				final int currentWidth = detailImage.getWidth();
				final int currentHeight = detailImage.getHeight();

				final int srcWidth = mAnswerDetail.getWidth();
				final int srcHeight = mAnswerDetail.getHeight();

				// Bitmap bitmap =
				// ((BitmapDrawable)detailImage.getDrawable()).getBitmap();
				//
				// int newWidth = WeLearnScreenUtils.getScreenWidth();
				// float scaleFactor =
				// (float)newWidth/(float)currentWidth;
				// int newHeight = (int)(currentHeight * scaleFactor);
				// bitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
				// newHeight, true);
				// detailImage.setImageBitmap(bitmap);

				showAnswerInconInPic(currentWidth, currentHeight, srcWidth, srcHeight);
				// showAnswerInconInPic(currentWidth, currentHeight,
				// window_width, window_height - 60);
				detailImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}

			private void showAnswerInconInPic(int currentWidth, int currentHeight, int srcWidth, int srcHeight) {
				List<AnswerSound> soundList = mAnswerDetail.getAnswer_snd();
				int count = 1;
				/*
				 * final boolean [] isSound = new boolean[soundList.size()]; for
				 * (int i = 0; i < isSound.length; i++) { isSound[i] = false; }
				 * for(int i = 0;i<soundList.size();i++){ final AnswerSound as =
				 * soundList.get(i);
				 */
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
						relativeParams.leftMargin = currentWidth - frameWidthOrHeight;
					}
					if (relativeParams.topMargin > currentHeight) {
						relativeParams.topMargin = currentHeight - frameWidthOrHeight;
					}

					CameraChoiceIconWithSer iconSer = new CameraChoiceIconWithSer(mActivity, as.getRole(),as.getSubtype());
					iconSer.setLayoutParams(relativeParams);
					mDetailContainer.addView(iconSer);
					iconSer.getIcSerView().setText(count++ + "");
					answerIcsList.add(iconSer);

					final ImageView mGrabItemIcView = iconSer.getIcView();
					if (type == GlobalContant.ANSWER_AUDIO) {// 声音
						mGrabItemIcView.setImageResource(R.drawable.ic_play2);
						iconSer.getmBgView().setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								MobclickAgent.onEvent(mActivity, UmengEventConstant.CUSTOM_EVENT_PLAY_AUDIO);
								String q_sndurl = as.getQ_sndurl();
								if (TextUtils.isEmpty(q_sndurl)) {
									ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
									return;
								}
								mGrabItemIcView.setImageResource(R.drawable.play_animation);
								mAnimationDrawable = (AnimationDrawable) mGrabItemIcView.getDrawable();
								TecApplication.animationDrawables.add(mAnimationDrawable);
								TecApplication.anmimationPlayViews.add(mGrabItemIcView);
								/*
								 * WeLearnMediaUtil.getInstance(false)
								 * .stopPlay();
								 */
								MediaUtil.getInstance(false).playVoice(false, q_sndurl, mAnimationDrawable,
										new ResetImageSourceCallback() {

									@Override
									public void reset() {
										mGrabItemIcView.setImageResource(R.drawable.ic_play2);
										// isSound[i] =
										// false;
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

	private boolean isShowQuestion() {
		return currentPageIndex == 0;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("position", currentPageIndex);
		outState.putString("dataStr", jsonStr);
		outState.putBoolean("isqpad", isQpad);
	}

	@Override
	protected void goBack() {
		mActivity.finish();
	}

	public static OneQuestionMoreAnswersDetailItemFragment newInstance(int position, String jsonStr, boolean isQpad) {
		OneQuestionMoreAnswersDetailItemFragment fragment = new OneQuestionMoreAnswersDetailItemFragment();
		Bundle data = new Bundle();
		data.putInt("position", position);
		data.putString("dataStr", jsonStr);
		data.putBoolean("isqpad", isQpad);
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onPause() {
		super.onPause();
		MediaUtil.getInstance(false).stopPlay();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
		MediaUtil.getInstance(false).stopPlay();
		MediaUtil.getInstance(false).stopVoice(mAnimationDrawable);

		// remove headset receiver
		try {
			this.getActivity().getApplication().unregisterReceiver(headsetPlugReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception. This is exactly what is desired
			} else {
				// unexpected, re-throw
				throw e;
			}
		}
	}

	private OnTipsShowListener mOnTipsShowListener;

	public void setOnTipsShowListener(OnTipsShowListener listener) {
		mOnTipsShowListener = listener;
	}

	public interface OnTipsShowListener {
		void onTipsShow(boolean isShow);
	}

	




}

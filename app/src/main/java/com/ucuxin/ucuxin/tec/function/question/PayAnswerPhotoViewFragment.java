package com.ucuxin.ucuxin.tec.function.question;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.CameraChoiceIconWithDel;
import com.ucuxin.ucuxin.tec.function.SharePopupMenuView;
import com.ucuxin.ucuxin.tec.function.SharePopupWindowView;
import com.ucuxin.ucuxin.tec.function.homework.model.TishiModel;
import com.ucuxin.ucuxin.tec.function.teccourse.view.InputExplainView;
import com.ucuxin.ucuxin.tec.function.teccourse.view.InputExplainView.ResultListener;
import com.ucuxin.ucuxin.tec.function.teccourse.view.PromoteDialog;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;
import com.ucuxin.ucuxin.tec.view.popwindow.DadianPopupWindow;
import com.ucuxin.ucuxin.tec.view.popwindow.DadianPopupWindow.DadianResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayAnswerPhotoViewFragment extends PayAnswerFragmentPhotoCommon
		implements OnClickListener, OnTouchListener, TextWatcher {

	private Button mTurnLeftBtn;
	private Button mTurnRightBtn;
	protected RelativeLayout mRotateContainer;
	protected LinearLayout mCameraTextChoiceContainer;
	protected ImageButton mPhotoViewCameraChoice;
	protected ImageButton mPhotoViewTextChoice;
	protected RelativeLayout mTextInputContainer;
	protected RelativeLayout mUserinfoContainer;
	protected InputMethodManager mImm;
	protected PopupWindow bgPopupWindow;
	protected PopupWindow popupWindow;
	protected SharePopupMenuView menuView;
	protected SharePopupWindowView mSharePopupWindowView;
	protected Button mSureTextInputBtn;
	protected DadianPopupWindow dadianPopupWindow;
	// protected EditText mAnswerText;
	protected boolean isAnswer = false;
	protected boolean isRotate = false;
	// 是否在打点前弹出提示对话框
	public boolean isshowPopup = false;
	protected int frameWidthOrHeight;
	protected int mWidth;
	protected int mHeight;
	public int subtype = 0;

	private InputExplainView inputLayout;
	private WelearnDialogBuilder mWelearnDialogBuilder;
	public WeakReference<PayAnswerPhotoViewFragment> mFragment;
	public static final String TAG = "PayAnswerPhotoViewFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.hide();
		mFragment=new WeakReference<PayAnswerPhotoViewFragment>(this);
		mImm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		frameWidthOrHeight = DisplayUtils.dip2px(mActivity, 56);
		getrewardandpunish();
	}
	public void getrewardandpunish() {
		if (TecApplication.gradeid != 0 | TecApplication.subjectid !=0) {
			JSONObject data = new JSONObject();
			try {
				data.put("gradeid", TecApplication.gradeid);
				data.put("subjectid", TecApplication.subjectid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(mActivity, "teacher", "rewardandpunish", data, new HttpListener() {

				@Override
				public void onFail(int code,String errMsg) {

				}

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						if (!TextUtils.isEmpty(dataJson)) {
							TishiModel tishiModel = JSON.parseObject(dataJson, TishiModel.class);
							if (tishiModel != null) {
								TecApplication.gradeid=0;
								TecApplication.subjectid=0;
								SharePerfenceUtil.getInstance().putString("viewreward", tishiModel.getViewreward());
								SharePerfenceUtil.getInstance().putString("viewpunish", tishiModel.getViewpunish());
								SharePerfenceUtil.getInstance().putString("processreward", tishiModel.getProcessreward());
								SharePerfenceUtil.getInstance().putString("processpunish", tishiModel.getProcesspunish());
								SharePerfenceUtil.getInstance().putString("conclusionreward", tishiModel.getConclusionreward());
								SharePerfenceUtil.getInstance().putString("conclusionpunish", tishiModel.getConclusionpunish());

								SharePerfenceUtil.getInstance().putString("remindtext", tishiModel.getRemindtext());
								SharePerfenceUtil.getInstance().putString("voiceremind", tishiModel.getVoiceremind());
							}

						}

					}
				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		resetView();
		setimagelist();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mTurnLeftBtn = (Button) view.findViewById(R.id.photo_view_turn_left_btn);
		mTurnRightBtn = (Button) view.findViewById(R.id.photo_view_turn_right_btn);
		mRotateContainer = (RelativeLayout) view.findViewById(R.id.photo_view_rotate_container);
		mUserinfoContainer = (RelativeLayout) view.findViewById(R.id.append_view_userinfo_container);
		mCameraTextChoiceContainer = (LinearLayout) view.findViewById(R.id.photo_view_camera_text_choice_container);
		mTextInputContainer = (RelativeLayout) view.findViewById(R.id.photo_view_text_input_container);
		mPhotoViewCameraChoice = (ImageButton) view.findViewById(R.id.photo_view_voice_choice);
		mPhotoViewTextChoice = (ImageButton) view.findViewById(R.id.photo_view_text_choice);
		mSureTextInputBtn = (Button) view.findViewById(R.id.photo_view_input_sure_btn);
		// mAnswerText = (EditText)
		// view.findViewById(R.id.photo_view_text_input);
		//
		// mAnswerText.addTextChangedListener(this);
		mSureTextInputBtn.setOnClickListener(this);
		mTextInputContainer.setOnClickListener(this);
		mPhotoViewTextChoice.setOnClickListener(this);
		mPhotoViewCameraChoice.setOnClickListener(this);
		// mPhotoImage.setOnTouchListener(this);
		mAnswerPicContainer.setOnTouchListener(this);
		if (!isRotate) {
			mAnswerPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					mWidth = mAnswerPicContainer.getWidth();
					mHeight = mAnswerPicContainer.getHeight();
					mAnswerPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
		}
		mTurnLeftBtn.setOnClickListener(this);
		mTurnRightBtn.setOnClickListener(this);
		mTurnLeftBtn.setVisibility(View.GONE);
		mTurnRightBtn.setVisibility(View.GONE);
		inputLayout = (InputExplainView) view.findViewById(R.id.input_container_tec_single);

		inputLayout.setVisibility(View.GONE);

		if (SharePerfenceUtil.getInstance().isShowFirstSingleTips()) {
			SharePerfenceUtil.getInstance().setFirstSingleFalse();
			new PromoteDialog(mActivity).show();
		}

		return view;
	}

	public void setimagelist() {
		inputLayout.setImageList(mActivity, viewPagerList, 0);
	}

	@Override
	public View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_photo_view, container, false);
	}

	@Override
	public void sureBtnClick() {

		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialogBuilder.getDialog( new WeakReference<Activity>(getActivity()).get());
		}
		mWelearnDialogBuilder.withMessage(R.string.text_dialog_sure_answer)
				.setOkButtonClick(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							mWelearnDialogBuilder.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (TecApplication.coordinateAnswerIconSet.size() == 0) {
							if (isAdded()) {
								ToastUtils.show(getString(R.string.text_toast_not_answer));
							}
							return;
						}
						mFragment.get().clearMenuView();
						mFragment.get().goToGrabItemView();
						// recycleBitmap();
						System.gc();
					}
				});
		mWelearnDialogBuilder.show();
	}

	public void goToPrevious(final boolean isBackKey) {
		if (GlobalVariable.answertextPopupWindow != null && GlobalVariable.answertextPopupWindow.isShowing()) {
			GlobalVariable.answertextPopupWindow.dismiss();
			return;
		}
		if (dadianPopupWindow != null) {
			if (dadianPopupWindow.isShowing()) {
				dadianPopupWindow.dismiss();
				if (frameDelView != null) {
					mAnswerPicContainer.removeView(frameDelView);
				} else {
				}
				return;
			}
		}
		if (isBackKey && inputLayout.getVisibility() == View.VISIBLE) {
			boolean isRemove = inputLayout.onBackPress();
			if (isRemove) {
				if (frameDelView != null) {
					mAnswerPicContainer.removeView(frameDelView);
				}
			}
			return;
		}

		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialogBuilder.getDialog( new WeakReference<Activity>(getActivity()).get());
		}
		mWelearnDialogBuilder.withMessage(R.string.text_toast_abour_answer)
				.setOkButtonClick(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							mWelearnDialogBuilder.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
						mImm.hideSoftInputFromWindow(mTextInputContainer.getWindowToken(), 0);

						if (isBackKey) {
							if (ifFirst) {
								IntentManager.goToGrabItemView(new WeakReference<Activity>(getActivity()).get(), true);
							} else {
								mActivity.finish();
							}
							// recycleBitmap();
						} else {
							if (isFromPhotoList) {
								Bundle data = new Bundle();
								data.putInt("tag", GlobalContant.PAY_ASNWER);
								IntentManager.goToAlbumView(new WeakReference<Activity>(getActivity()).get(), data);
								// recycleBitmap();
							} else {
								IntentManager.startImageCapture(new WeakReference<Activity>(getActivity()).get(), GlobalContant.PAY_ASNWER);
							}
						}
						mFragment.get().clearData();
						System.gc();
					}
				});
		mWelearnDialogBuilder.show();
	}

	public void dismissPoPUP() {
		if (dadianPopupWindow != null) {
			if (dadianPopupWindow.isShowing()) {
				dadianPopupWindow.dismiss();
				if (frameDelView != null) {
					mAnswerPicContainer.removeView(frameDelView);
				} else {
				}
				return;
			}
		}
	}

	private void clearMenuView() {
		if (menuView != null) {
			menuView.reset();
			menuView = null;
		}
	}

	private void goToGrabItemView() {
		Bundle data = new Bundle();
		data.putBoolean(PayAnswerGrabItemActivity.SHOW_ANSWER, true);
		data.putString(PayAnswerGrabItemActivity.IMAGE_PATH, path);
		data.putInt(PayAnswerGrabItemActivity.IMAGE_WIDTH, mWidth);
		data.putInt(PayAnswerGrabItemActivity.IMAGE_HEIGHT, mHeight);
//		if (GlobalVariable.payAnswerActivity != null) {
//			// IntentManager.goToGrabItemView(GlobalVariable.payAnswerActivity,
//			// data, false);
//			IntentManager.goToGrabItemView((BaseActivity) GlobalVariable.payAnswerActivity.getActivity(), data, false);
//			mActivity.finish();
//		} else {
			IntentManager.goToGrabItemView(mActivity, data, true);


	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.photo_view_turn_left_btn:// 左转
			if (TecApplication.coordinateAnswerIconSet.size() == 0) {
				rotate(R.id.photo_view_turn_left_btn);
			}
			break;
		case R.id.photo_view_turn_right_btn:// 右转
			if (TecApplication.coordinateAnswerIconSet.size() == 0) {
				rotate(R.id.photo_view_turn_right_btn);
			}
			break;
		case R.id.photo_view_voice_choice:
			// 显示dialog
			showDialog();
			break;
		// case R.id.photo_view_text_choice:
		// showInput();
		// break;
		case R.id.photo_view_text_input_container:
			mImm.hideSoftInputFromWindow(mTextInputContainer.getWindowToken(), 0);
			break;
		// case R.id.photo_view_input_sure_btn:
		// if (0 == Integer.parseInt(mSureTextInputBtn.getTag().toString())) {
		// mAnswerPicContainer.removeView(frameDelView);
		// } else {
		// addTextAnswer();
		// }
		// resetView();
		// break;
		}
	}

	private Map<CameraChoiceIconWithDel, ExplainPoint> maps = new HashMap<CameraChoiceIconWithDel, ExplainPoint>();

	private void addTextAnswer(String text) {
		// String text = mAnswerText.getText().toString();
		if (TextUtils.isEmpty(text) && isAdded()) {
			ToastUtils.show("");
			return;
		}

		// 添加图片
		point.setSubtype(subtype);

		final CameraChoiceIconWithDel childConainer = addtextIcon();
		int sum = points.size() + points2.size() + 1;
		points.add(point);
		childConainer.getRl().setVisibility(View.VISIBLE);
		childConainer.getIcon_del().setText(sum + "");
		maps.put(childConainer, point);
		childConainer.getBgView().setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (null == mWelearnDialogBuilder) {
					mWelearnDialogBuilder = WelearnDialogBuilder.getDialog( new WeakReference<Activity>(getActivity()).get());
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

						isAnswer = false;
						mAnswerPicContainer.removeView(childConainer);
						TecApplication.coordinateAnswerIconSet.remove(maps.get(childConainer));
						if (TecApplication.coordinateAnswerIconSet.size() == 0) {
							mTurnLeftBtn.setVisibility(View.VISIBLE);
							mTurnRightBtn.setVisibility(View.VISIBLE);
						}
					}
				});
				mWelearnDialogBuilder.show();

				return true;
			}
		});
		childConainer.getBgView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String answerText = maps.get(childConainer).getText();
				// Bundle bundle = new Bundle();
				// bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
				// answerText);
				// IntentManager.goToAnswertextView(getActivity(), bundle);
				GlobalVariable.answertextPopupWindow = new AnswertextPopupWindow(getActivity(), answerText);
			}
		});
		childConainer.getIcView().setImageResource(R.drawable.ic_text_choic_t);
		point.setText(text);
		TecApplication.coordinateAnswerIconSet.add(point);
		mTurnLeftBtn.setVisibility(View.GONE);
		mTurnRightBtn.setVisibility(View.GONE);
		mAnswerPicContainer.removeView(frameDelView);
		// mAnswerText.setText(null);
	}

	/**
	 * 输入语音文字时添加icon
	 * 
	 * @return
	 */
	private CameraChoiceIconWithDel addtextIcon() {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		float x = point.getX();
		float y = point.getY();

		relativeParams.leftMargin = (int) (x);
		relativeParams.topMargin = (int) (y);

		final CameraChoiceIconWithDel childConainer = new CameraChoiceIconWithDel(getActivity(), point.getSubtype());
		childConainer.setLayoutParams(relativeParams);
		mAnswerPicContainer.addView(childConainer);
		return childConainer;
	}

	/**
	 * 重置播放动画
	 * 
	 * @param iconVoiceView
	 */
	private void resetAnimationPlay(ImageView iconVoiceView) {
		for (ImageView currentView : TecApplication.anmimationPlayViews) {
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

	private void addVoiceAnswer(final String audioPath) {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		float x = point.getX();
		float y = point.getY();

		relativeParams.leftMargin = (int) (x);
		relativeParams.topMargin = (int) (y);
		point.setSubtype(subtype);
		point.setAudioPath(audioPath);
		TecApplication.coordinateAnswerIconSet.add(point);

		final CameraChoiceIconWithDel childConainer = new CameraChoiceIconWithDel(getActivity(), point.getSubtype());
		int sum = points.size() + points2.size() + 1;
		points.add(point);
		childConainer.getRl().setVisibility(View.VISIBLE);
		childConainer.getIcon_del().setText(sum + "");
		maps.put(childConainer, point);

		childConainer.setLayoutParams(relativeParams);
		mAnswerPicContainer.addView(childConainer);

		final ImageView iconVoiceView = childConainer.getIcView();
		iconVoiceView.setImageResource(R.drawable.ic_play2);
		childConainer.getBgView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iconVoiceView.setImageResource(R.drawable.play_animation);
				mAnimationDrawable = (AnimationDrawable) iconVoiceView.getDrawable();
				TecApplication.animationDrawables.add(mAnimationDrawable);
				TecApplication.anmimationPlayViews.add(iconVoiceView);
				stopPlay();
				MediaUtil.getInstance(false).playVoice(true, audioPath, mAnimationDrawable,
						new ResetImageSourceCallback() {

					@Override
					public void reset() {
						iconVoiceView.setImageResource(R.drawable.ic_play2);
					}

					@Override
					public void playAnimation() {
					}

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
					mWelearnDialogBuilder = WelearnDialogBuilder.getDialog( new WeakReference<Activity>(getActivity()).get());
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
						isAnswer = false;
						mAnswerPicContainer.removeView(childConainer);
						TecApplication.coordinateAnswerIconSet.remove(maps.get(childConainer));
						if (TecApplication.coordinateAnswerIconSet.size() == 0) {
							mTurnLeftBtn.setVisibility(View.VISIBLE);
							mTurnRightBtn.setVisibility(View.VISIBLE);
						}

					}
				});
				mWelearnDialogBuilder.show();

				return true;
			}
		});
	}

	/**
	 * 显示输入框
	 */
	// private void showInput() {
	// if (!isAnswer) {
	// return;
	// }
	// hideBottom();
	// mCameraTextChoiceContainer.setVisibility(View.GONE);
	// mTextInputContainer.setVisibility(View.VISIBLE);
	// InputMethodManager inputMethodManager = (InputMethodManager) mActivity
	// .getSystemService(Context.INPUT_METHOD_SERVICE);
	// inputMethodManager.toggleSoftInput(0,
	// InputMethodManager.HIDE_NOT_ALWAYS);
	// mAnswerText.requestFocus();
	// }

	public interface ResetView {
		void reset(boolean isCancel);

		void showRotateBtn();
	}

	/**
	 * 显示输入语音dialog
	 */
	public void showDialog() {
		if (!isAnswer) {
			return;
		}
		View parent = mActivity.getWindow().getDecorView();

		View transView = new View(mActivity);
		transView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		transView.setBackgroundResource(R.color.transparent_bg);
		bgPopupWindow = new PopupWindow(transView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bgPopupWindow.setAnimationStyle(R.style.WAlphaAnimation);
		bgPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

		if (bgPopupWindow.isShowing()) {
			mSharePopupWindowView = new SharePopupWindowView(mActivity);
			menuView = (SharePopupMenuView) mSharePopupWindowView.findViewById(R.id.share_popup_menu_view);

			popupWindow = new PopupWindow(mSharePopupWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			popupWindow.setAnimationStyle(R.style.SharePopupAnimation);
			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
			menuView.enableCancelBtn(mAnswerPicContainer, frameDelView, new ResetView() {

				@Override
				public void reset(boolean isCancel) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
					}
					if (bgPopupWindow != null && bgPopupWindow.isShowing()) {
						bgPopupWindow.dismiss();
					}
					resetView();
					if (!isCancel) {
						if (null != mTurnLeftBtn) {
							mTurnLeftBtn.setVisibility(View.GONE);
						}
						if (null != mTurnRightBtn) {
							mTurnRightBtn.setVisibility(View.GONE);
						}
					}
				}

				@Override
				public void showRotateBtn() {
					mTurnLeftBtn.setVisibility(View.VISIBLE);
					mTurnRightBtn.setVisibility(View.VISIBLE);
				}
			});
			menuView.enableConfirmBtn(mAnswerPicContainer, frameDelView, point, this.view);
		}
	}

	protected void showBottom() {
		if (mRotateContainer != null) {
			mRotateContainer.setVisibility(View.VISIBLE);
		}
		if (mUserinfoContainer != null) {
			mUserinfoContainer.setVisibility(View.GONE);
		}
	}

	protected void hideBottom() {
		mRotateContainer.setVisibility(View.GONE);
		mUserinfoContainer.setVisibility(View.GONE);
	}

	protected void resetView() {
		isAnswer = false;
		showBottom();
		if (mCameraTextChoiceContainer != null) {
			mCameraTextChoiceContainer.setVisibility(View.GONE);
		}
		if (mTextInputContainer != null) {
			mTextInputContainer.setVisibility(View.GONE);
			if (mImm != null) {
				mImm.hideSoftInputFromWindow(mTextInputContainer.getWindowToken(), 0);
			}
		}
	}

	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		isRotate = true;
		int degree = 0;
		if (rotate == R.id.photo_view_turn_right_btn) {
			degree = 90;
		} else {
			degree = -90;
		}
		Bitmap orgBitmap = getBitMapFromView(mPhotoImage);
		if (orgBitmap != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);

			Bitmap resizedBitmap = Bitmap.createBitmap(orgBitmap, 0, 0, orgBitmap.getWidth(), orgBitmap.getHeight(),
					matrix, true);
			// if (orgBitmap != null && !orgBitmap.isRecycled()) {
			// orgBitmap.recycle();
			// orgBitmap = null;
			// LogUtils.i(TAG, "---recycle---");
			// }
			// mPhotoImage.setImageBitmap(resizedBitmap);
			mPhotoImage.setCustomBitmap(resizedBitmap);
			mWidth = resizedBitmap.getWidth();
			mHeight = resizedBitmap.getHeight();
			WeLearnImageUtil.saveFile(path, resizedBitmap);
		}
	}

	private View view;

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (frameDelView != null) {
			mAnswerPicContainer.removeView(frameDelView);
		}
		boolean addCameraFrame = addCameraFrame(view, event);
		// 如果没有打点直接返回
		if (!addCameraFrame) {
			return false;
		}
		for (ExplainPoint explainPoint : points2) {
			if (explainPoint.getRole() == 1 | explainPoint.getRole() == 3) {
				isshowPopup = true;
			}
		}
		if (!isshowPopup) {
			subtype = 0;
			int msubjectid = SharePerfenceUtil.getInstance().getInt("msubjectid", -1);
			if (msubjectid == 1 | msubjectid == 5 | msubjectid == 6) {
				subtype = 5;
				showCameraTextSwitchMenu();
			} else {
				dadianPopupWindow = new DadianPopupWindow(PayAnswerPhotoViewFragment.this,msubjectid ,new DadianResultListener() {

					@Override
					public void onReturn(int msubtype) {

						subtype = msubtype;
					}
				});
			}
		} else {
			subtype = 4;
			showCameraTextSwitchMenu();
		}

		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// recycleBitmap();
		maps.clear();
		maps = null;
		System.gc();
	}

	// public void recycleBitmap() {
	// Bitmap orgBitmap = getBitMapFromView(mPhotoImage);
	// if (orgBitmap != null && !orgBitmap.isRecycled()) {
	// orgBitmap.recycle();
	// orgBitmap = null;
	// LogUtils.i(TAG, "---recycle---");
	// }
	// }

	private Bitmap getBitMapFromView(ImageView view) {
		if (null == view) {
			return null;
		}
		BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
		if (null == drawable) {
			return null;
		}
		Bitmap orgBitmap = drawable.getBitmap();
		return orgBitmap;
	}

	public void showCameraTextSwitchMenu() {
		// showBottom();
		// mTextInputContainer.setVisibility(View.GONE);
		// mCameraTextChoiceContainer.setVisibility(View.VISIBLE);
		// mImm.hideSoftInputFromWindow(mTextInputContainer.getWindowToken(),
		// 0);
		inputLayout.setResultListener(new ResultListener() {

			@Override
			public void onReturn(int type, String text, String audioPath) {
				if (frameDelView != null) {
					mAnswerPicContainer.removeView(frameDelView);
				}
				if (type == GlobalContant.ANSWER_TEXT) {
					addTextAnswer(text);
				} else if (type == GlobalContant.ANSWER_AUDIO) {
					addVoiceAnswer(audioPath);
				}
			}
		});
	}

	private CameraChoiceIconWithDel frameDelView;
	private ExplainPoint point;

	private List<ExplainPoint> points = new ArrayList<ExplainPoint>();
	// 已上传打点坐标集合
	public List<ExplainPoint> points2 = new ArrayList<ExplainPoint>();

	/**
	 * 判断打点是否重叠
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean isOverlay(float currentX, float currentY) {
		int mX1 = DisplayUtils.dip2px(mActivity, 56);
		int mX3 = DisplayUtils.dip2px(mActivity, 29);
		int mX2 = DisplayUtils.dip2px(mActivity, 35);
		int mX4 = DisplayUtils.dip2px(mActivity, 10);
		boolean flag1 = false;
		for (ExplainPoint explainPoint : points2) {
			float x = explainPoint.getX();
			float y = explainPoint.getY();
			float absX = Math.abs(currentX - x);
			float absY = Math.abs(currentY - y);

			if (mX1 < absX) {

			} else if (mX2 < absX) {
				if (x > currentX) {
					if ((y - currentY) < mX4) {
						flag1 = true;
					} else {

					}
				} else {
					if ((currentY - y) < mX4) {
						flag1 = true;
					} else {

					}
				}
			} else {
				if (mX2 < absY) {

				} else {
					flag1 = true;
				}
			}

		}
		for (Map.Entry<CameraChoiceIconWithDel, ExplainPoint> entry : maps.entrySet()) {
			float x2 = entry.getValue().getX();
			float y2 = entry.getValue().getY();
			if (mX3 > Math.abs(currentX - x2) && mX3 > Math.abs(currentY - y2)) {
				flag1 = true;
			}
		}

		return flag1;
	}

	/**
	 * 添加相框
	 * 
	 * @param view
	 *
	 *
	 */
	private boolean addCameraFrame(final View view, MotionEvent event) {
		isAnswer = true;
		this.view = view;
		float x = event.getX();// 相对于image的x坐标
		float y = event.getY();// 相对于image的y坐标

		if (x > (view.getMeasuredWidth() - frameWidthOrHeight)) {
			x = view.getMeasuredWidth() - frameWidthOrHeight;
		}
		if (y > (view.getMeasuredHeight() - frameWidthOrHeight)) {
			y = view.getMeasuredHeight() - frameWidthOrHeight;
		}
		// if(x< frameWidthOrHeight / 2 ){
		// x = frameWidthOrHeight / 2 ;
		// }
		// if (y <frameWidthOrHeight / 2 ) {
		// y = frameWidthOrHeight / 2 ;
		// }
		 int currentX = (int) x - DisplayUtils.dip2px(mActivity, 28) / 2;
		 int currentY = (int) y - DisplayUtils.dip2px(mActivity, 40) / 2;
//		int currentX = (int) x;
//		int currentY = (int) y;
		currentX = currentX < 1 ? 1 : currentX;
		currentY = currentY < 1 ? 1 : currentY;

		if (isOverlay(currentX, currentY)) {
			return false;
		} else {

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			frameDelView = new CameraChoiceIconWithDel(mActivity, 0);
			params.leftMargin = currentX;
			params.topMargin = currentY;

			point = new ExplainPoint();
			point.setX(currentX);
			point.setY(currentY);

			frameDelView.setLayoutParams(params);
			frameDelView.invalidate();

			mAnswerPicContainer.addView(frameDelView);
			frameDelView.getBgView().setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (null == mWelearnDialogBuilder) {
						mWelearnDialogBuilder = WelearnDialogBuilder.getDialog( new WeakReference<Activity>(getActivity()).get());
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

							mFragment.get().resetView();
							mAnswerPicContainer.removeView(frameDelView);
						}
					});
					mWelearnDialogBuilder.show();

					return true;
				}
			});
			return true;
		}
	}

	private void clearData() {
		TecApplication.coordinateAnswerIconSet.clear();
		TecApplication.animationDrawables.clear();
		TecApplication.anmimationPlayViews.clear();
	}

	@Override
	protected void goBack() {

		goToPrevious(false);

	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence seq, int arg1, int arg2, int arg3) {
		if (seq.toString().length() > 0) {
			mSureTextInputBtn.setText(getString(R.string.text_nav_submit));
			mSureTextInputBtn.setTag(1);
		} else {
			mSureTextInputBtn.setText(getString(R.string.text_nav_cancel));
			mSureTextInputBtn.setTag(0);
		}
	}

	@Override
	public void initView(View view) {


	}

	@Override
	public void initListener() {


	}
}

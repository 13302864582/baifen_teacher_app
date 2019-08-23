package com.ucuxin.ucuxin.tec.function.teccourse.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.CameraFrameWithDel;
import com.ucuxin.ucuxin.tec.function.homework.view.VoiceOrTextPoint;
import com.ucuxin.ucuxin.tec.function.teccourse.AddHandoutActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.SingleStudentQAActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CoursePoint;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView.OnScaleListener;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;

public class AddPointCommonView extends FrameLayout {

	public static final String TAG = AddPointCommonView.class.getSimpleName();

	interface NextBtnClickLinsener {
		void onNextBtnClick();
	}

	private static final int EXPLAIN_POINT_LIST = 123;

	private static final int CHECK_POINT_LIST = 321;

	public CameraFrameWithDel frameDelView;

	private BaseActivity mActivity;
	private DragImageView mPicIv;
	private RelativeLayout mPicContainer;
//	private int mContainerWidth;
//	private int mContainerHeight;
	// private List<ExplainPoint> mPointList;
	// private ExplainPoint mPoint;
	// private List<VoiceOrTextPoint> mPointViewList;
	// private List<RightWrongPointView> mRightWrongViewList;
	private boolean isAllowAddPoint;
	private HashMap<String, FrameLayout> pointMap;
	private boolean picIsLoaded;
	private boolean isMeasure;
	private int frameWidthOrHeight;
	private int widthPixels;
	private boolean isScaled;

	private boolean canRotateable = true;
	private float oldOri;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EXPLAIN_POINT_LIST:
				@SuppressWarnings("unchecked")
				ArrayList<CoursePoint> pointList = (ArrayList<CoursePoint>) msg.obj;
				addPoints(pointList);
				break;
			}
		}
	};

	public AddPointCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}

	public AddPointCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}

	public AddPointCommonView(Context context) {
		super(context);
		this.mActivity = (BaseActivity) context;
		setUpViews();
	}

	@SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
	private void setUpViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.add_point_common_view, null);
		frameWidthOrHeight = DisplayUtils.dip2px(mActivity, 56);
		DisplayMetrics outMetrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		widthPixels = outMetrics.widthPixels;
		// int heightPixels = (int) (((float) outMetrics.heightPixels) / 1.3f);
		// mContainerWidth = outMetrics.widthPixels;// widthPixels;
		// mContainerHeight = (int) (((float) outMetrics.heightPixels) / 1.3f);// heightPixels;
		pointMap = new HashMap<String, FrameLayout>();
		mPicContainer = (RelativeLayout) view.findViewById(R.id.pic_container_add_point);

		mPicIv = (DragImageView) view.findViewById(R.id.pic_iv_add_point);
		mPicIv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (isAllowAddPoint) {
					int action = event.getAction();
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						if (!isScaled) {
							if (mActivity != null) {
								removeFrameDelView();
								addCameraFrame(event);
							}
						} else {
							removeFrameDelView();
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (isScaled) {
							removeFrameDelView();
						}
						break;
					case MotionEvent.ACTION_UP:

						break;
					case MotionEvent.ACTION_CANCEL:
						break;
					}
				}
				return false;
			}
		});

		mPicIv.setOnScaleListener(new OnScaleListener() {
			@Override
			public void onScale(boolean isScale) {
				showPoints(!isScale);
				isScaled = isScale;
			}
		});

		addView(view);
	}
	/**
	 * 单个检查点
	 * 
	 * @param pointModel
	 */
	public void setImgPath(String imgpath ,boolean isLocal, boolean isAllowAddPoint /*final HomeWorkCheckPointModel pointModel , final int left , final int top*/) {
		this.isAllowAddPoint = isAllowAddPoint;
//		String imgpath = pointModel.getImgpath();
//		boolean isLocal = pointModel.isLocal();
		
		mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int measuredWidth = mPicContainer.getMeasuredWidth();
				int measuredHeight = mPicContainer.getMeasuredHeight();
				if (measuredWidth != 0 && measuredHeight != 0) {
					mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					isMeasure = true;
					mPicIv.setScreenSize(measuredWidth, measuredHeight);
				}
			}
		});

		if (isLocal) {
			Bitmap bm = BitmapFactory.decodeFile(imgpath);
			mPicIv.setCustomBitmap(bm);
			picIsLoaded = true;
		} else  {
			ImageLoader.getInstance().loadImage(imgpath, mPicIv, R.drawable.loading , new OnImageLoadListener() {
				
				@Override
				public void onSuccess(ImageContainer response) {
					picIsLoaded = true;
					
				}
				
				@Override
				public void onFailed(VolleyError error) {

					
				}
			});
		}

	}



	public void addPoints(final ArrayList<CoursePoint> pointList) {
		if (pointList == null) {
			return;
		}
		if (picIsLoaded && isMeasure) {
			for (int i = 0; i < pointList.size(); i++) {
				CoursePoint pointModel = pointList.get(i);
				pointModel.setSeqid(i +1 );
				addVoiceOrTextPoint(pointModel);
			}
		} else {
			Message msg = Message.obtain();
			msg.what = EXPLAIN_POINT_LIST;
			msg.obj = pointList;
			mHandler.sendMessageDelayed(msg, 1000);
		}
	}

	public void removeExPoint(View v, String coordinate) {
		mPicContainer.removeView(v);
		if (pointMap.containsKey(coordinate)) {
			pointMap.remove(coordinate);
		}
	}
	

	public VoiceOrTextPoint addVoiceOrTextPoint(final CoursePoint pointModel) {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		int type = pointModel.getType();
		int roleid = pointModel.getRoleid();
		String coordinate = pointModel.getCoordinate();

		int seqid = pointModel.getSeqid();

		String[] coordinates = coordinate.split(",");
		float pointXPer=0.0f;
        float pointYPer=0.0f;
        try {
            if (coordinates[0].contains(".")&&coordinates[1].contains(".")) {
                pointXPer = Float.parseFloat(coordinates[0]);
                pointYPer = Float.parseFloat(coordinates[1]);
            }
           
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

		int mContainerWidth = mPicIv.getLayoutParams().width;
		int mContainerHeight = mPicIv.getLayoutParams().height;
		
		int currentX = (int) (mContainerWidth * pointXPer);
		int currentY = (int) (mContainerHeight * pointYPer);

//		relativeParams.leftMargin = currentX < 0 ? 0 : currentX;
//		relativeParams.topMargin = currentY < 0 ? 0 : currentY;

		relativeParams.leftMargin = currentX + mPicIv.getLeft();
		relativeParams.topMargin = currentY + mPicIv.getTop()-DisplayUtils.dip2px(mActivity, 15);
		
		if (relativeParams.leftMargin < 0) {
			relativeParams.leftMargin = 0;
		}
		if (relativeParams.topMargin < 0) {
			relativeParams.topMargin = 0;
		}
		if (relativeParams.leftMargin > (widthPixels - frameWidthOrHeight)) {
			relativeParams.leftMargin = widthPixels - frameWidthOrHeight;
		}
		
		VoiceOrTextPoint pointViewContainer = new VoiceOrTextPoint(mActivity, roleid,pointModel.getSubtype());

		pointViewContainer.setLayoutParams(relativeParams);
		if (pointMap.containsKey(coordinate)) {
			return pointViewContainer;
		}

		mPicContainer.addView(pointViewContainer);
		pointMap.put(coordinate, pointViewContainer);
		if (seqid == 0) {
			pointViewContainer.getIcSerView().setVisibility(View.GONE);
		} else {
			pointViewContainer.getIcSerView().setText(seqid + "");
		}
		// mPointViewList.add(pointViewContainer);

		final ImageView pointViewIc = pointViewContainer.getIcView();

		if (type == GlobalContant.ANSWER_AUDIO) {// 声音
			if (roleid == GlobalContant.ROLE_ID_STUDENT) {// 学生红色
				pointViewIc.setImageResource(R.drawable.me_v3);
			} else {// 老师绿色
				pointViewIc.setImageResource(R.drawable.v3);
			}
			pointViewIc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					MobclickAgent.onEvent(mActivity, UmengEventConstant.CUSTOM_EVENT_PLAY_AUDIO);
					
					int roleid = pointModel.getRoleid();
					if (TextUtils.isEmpty(pointModel.getSndurl())) {
						ToastUtils.show(R.string.text_audio_is_playing_please_waiting);
						return;
					}
					

					if (roleid == GlobalContant.ROLE_ID_STUDENT) {// 学生红色
						pointViewIc.setImageResource(R.drawable.play_voice_anim_stu);
						pointViewIc.setTag(R.drawable.me_v3);
					} else {// 老师绿色
						pointViewIc.setTag(R.drawable.v3);
						pointViewIc.setImageResource(R.drawable.play_voice_anim_tec);
					}

					AnimationDrawable mAnimationDrawable = (AnimationDrawable) pointViewIc.getDrawable();
					TecApplication.animationDrawables.add(mAnimationDrawable);
					TecApplication.anmimationPlayViews.add(pointViewIc);
					/*
					 * WeLearnMediaUtil.getInstance(false) .stopPlay();
					 */
					MediaUtil.getInstance(false).playVoice(false, pointModel.getSndurl(), mAnimationDrawable,
							new ResetImageSourceCallback() {

								@Override
								public void reset() {
									int roleid = pointModel.getRoleid();
									if (roleid == GlobalContant.ROLE_ID_STUDENT) {// 学生红色
										pointViewIc.setImageResource(R.drawable.me_v3);
									} else {// 老师绿色
										pointViewIc.setImageResource(R.drawable.v3);
									}
								}

								@Override
								public void playAnimation() {
								}

								@Override
								public void beforePlay() {
									MediaUtil.getInstance(false).resetAnimationPlayAtHomeWork(pointViewIc);
								}
							}, null);
				}
			});
		} else if (type == GlobalContant.ANSWER_TEXT) {
			if (roleid == GlobalContant.ROLE_ID_STUDENT) {// 学生红色
				pointViewIc.setImageResource(R.drawable.text_ic_stu_selector);
			} else {// 老师绿色
				pointViewIc.setImageResource(R.drawable.text_ic_tec_selector);
			}
			pointViewIc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
//					Bundle bundle = new Bundle();
//					bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT, pointModel.getText());
//					IntentManager.goToAnswertextView(mActivity, bundle);
					GlobalVariable.answertextPopupWindow=new AnswertextPopupWindow(mActivity, pointModel.getText());
				}
			});
		}
		return pointViewContainer;
	}

	/**
	 * 去除打点框
	 */
	public void removeFrameDelView() {
		if (frameDelView != null && mPicContainer != null) {
			mPicContainer.removeView(frameDelView);
			frameDelView = null;
		}
		if (mActivity != null) {
			mActivity.hideAddPointBottomContainer();
			 mActivity.hideCamareContainer();
		}
	}

	private void addCameraFrame(MotionEvent event) {
		if ((!picIsLoaded) ||(!isMeasure) ) {
			return;
		}
		float x = event.getX();// 触摸相对于image的x坐标
		float y = event.getY();// 触摸相对于image的y坐标

		int mContainerWidth = mPicIv.getLayoutParams().width;
		int mContainerHeight = mPicIv.getLayoutParams().height;
		
		int currentX = (int) x- DisplayUtils.dip2px(mActivity, 14);// 方框左上角
		int currentY = (int) y- DisplayUtils.dip2px(mActivity, 15);// 方框左上角

		// mPoint = new ExplainPoint();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		frameDelView = new CameraFrameWithDel(mActivity);

		if (currentX < 0) {
			currentX = 0;
		}
		if (currentY < 0) {
			currentY = 0;
		}
		if (currentX > (mContainerWidth -  frameWidthOrHeight)) {
			currentX = mContainerWidth -  frameWidthOrHeight;
		}
		if (currentY > (mContainerHeight -  frameWidthOrHeight)) {
			currentY = mContainerHeight - frameWidthOrHeight;
		}

		int left = mPicIv.getLeft();
		int top = mPicIv.getTop();
		params.leftMargin = currentX + left;
		params.topMargin = currentY + top;
		LogUtils.d(TAG, "yh addCameraFrame() left = " + left + ", top = " + top);
		
		frameDelView.setLayoutParams(params);
		frameDelView.invalidate();

		mPicContainer.addView(frameDelView);
		float coordinateX = (float) currentX / (float) mContainerWidth;
		float coordinateY = (float) (currentY - DisplayUtils.dip2px(mActivity, 15))/ (float) mContainerHeight;
		String coordinate = coordinateX + "," + coordinateY;
		
		if (mActivity instanceof AddHandoutActivity) {
			mActivity.showAddPointBottomContainer(coordinate);
		}else if (mActivity instanceof SingleStudentQAActivity) {
			mActivity.showAddPointBottomContainer(coordinate);
		}
	}

	public void stopVoice() {
		MediaUtil.getInstance(false).stopPlay();
	}

	public void showPoints(boolean isShow) {
		int show = isShow ? View.VISIBLE : View.GONE;
		Set<String> keySet = pointMap.keySet();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			pointMap.get(key).setVisibility(show);
		}
	}

	public boolean isCanRotateable() {
		return canRotateable;
	}

	public void setCanRotateable(boolean canRotateable) {
		this.canRotateable = canRotateable;
	}

	public void setOrientation(int ori) {
		if (canRotateable) {

			final int de = ori;

			ori = Math.abs(360 - ori);

			if (oldOri == 360 && ori == 90) {
				oldOri = 0;
			}

			if (oldOri == 90 && ori == 360) {
				ori = 0;
			}

			if (oldOri == 0 && ori == 270) {
				oldOri = 360;
			}

			if (oldOri == ori || (oldOri == 360 && ori == 0) || (oldOri == 0 && ori == 360)) {
				oldOri = ori;
				return;
			}

			pointMap.keySet();
			for (Iterator<String> iterator = pointMap.keySet().iterator(); iterator.hasNext();) {
				final FrameLayout fl = pointMap.get(iterator.next());
				// ObjectAnimator.ofFloat(fl, "rotation", oldOri, ori).setDuration(270).start();

				AnimatorSet set = new AnimatorSet();
				ObjectAnimator anim3 = ObjectAnimator.ofFloat(fl, "rotation", oldOri, ori);
				anim3.setDuration(270);

				set.play(anim3);
				set.start();
			}
			oldOri = ori;
		}
	}
	
}

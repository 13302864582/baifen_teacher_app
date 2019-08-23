
package com.ucuxin.ucuxin.tec.function.homework.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.NetworkImageView.OnImageLoadListener;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.function.CameraFrameWithDel;
import com.ucuxin.ucuxin.tec.function.homework.MyCheckedHomeworkActivity;
import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.ResetImageSourceCallback;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.goodview.GoodView;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView.OnScaleListener;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;
import com.ucuxin.ucuxin.tec.view.popwindow.DadianPopupWindow2;
import com.ucuxin.ucuxin.tec.view.popwindow.DadianPopupWindow2.DadianResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 答点自定义view
 * 
 * @author: sky
 */
public class AddPointCommonView extends FrameLayout {

	public static final String TAG = AddPointCommonView.class.getSimpleName();

	interface NextBtnClickLinsener {
		void onNextBtnClick();
	}

	public DadianPopupWindow2 dadianPopupWindow;
	// 已上传打点坐标集合
	private List<ExplainPoint> points = new ArrayList<ExplainPoint>();
	public List<ExplainPoint> points2 = new ArrayList<ExplainPoint>();
	private static final int EXPLAIN_POINT_LIST = 123;

	private static final int CHECK_POINT_LIST = 321;

	public CameraFrameWithDel frameDelView;
	boolean flag2 = false;
	private BaseActivity mActivity;

	private DragImageView mPicIv;

	private RelativeLayout mPicContainer;
	// private int mContainerWidth;
	// private int mContainerHeight;
	// private List<ExplainPoint> mPointList;
	// private ExplainPoint mPoint;
	// private List<VoiceOrTextPoint> mPointViewList;
	// private List<RightWrongPointView> mRightWrongViewList;
	private boolean isAllowAddPoint= true;
	private int msubtype = 0;

	private HashMap<String, FrameLayout> pointMap;

	private boolean picIsLoaded;

	private boolean isMeasure;

	private int frameWidthOrHeight;

	private int widthPixels;
	private int mCurrentItem;
	private ArrayList<String> viewPagerList;

	private boolean isScaled;

	private boolean canRotateable = true;

	private float oldOri;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EXPLAIN_POINT_LIST:
				@SuppressWarnings("unchecked")
				ArrayList<HomeWorkSinglePoint> expointlist = (ArrayList<HomeWorkSinglePoint>) msg.obj;
				showCheckPoint(expointlist);
				break;
			case CHECK_POINT_LIST:

				@SuppressWarnings("unchecked")
				ArrayList<HomeWorkCheckPointModel> checkpointlist = (ArrayList<HomeWorkCheckPointModel>) msg.obj;
				showRightOrWrongPoint(checkpointlist, msg.arg1, msg.arg2, null, 0);
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
		// mContainerHeight = (int) (((float) outMetrics.heightPixels) /
		// 1.3f);// heightPixels;
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
	 * 作业单页
	 * homeWorkPageModel
	 * @param
	 */
	public void setPagePic(String imgurl, boolean isAllowAddPoint) {
		this.isAllowAddPoint = isAllowAddPoint;
		// mPicIv.getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// int measuredWidth = mPicIv.getMeasuredWidth();
		// int measuredHeight = mPicIv.getMeasuredHeight();
		// if (measuredWidth != 0 && measuredHeight != 0) {
		// // mContainerWidth = measuredWidth;
		// // mContainerHeight = measuredHeight;
		// picIsLoaded = true;
		// mPicIv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		// // mPicIv.setScreenSize(measuredWidth, measuredHeight);
		// }
		// }
		// });

		mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int measuredWidth = mPicContainer.getMeasuredWidth();
				int measuredHeight = mPicContainer.getMeasuredHeight();
				if (measuredWidth != 0 && measuredHeight != 0) {
					mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					mPicIv.setScreenSize(measuredWidth, measuredHeight);
					LogUtils.d(TAG, "yh setPagePic() w = " + measuredWidth + ", h = " + measuredHeight);
					isMeasure = true;
				}
			}
		});

		ImageLoader.getInstance().loadImage(imgurl, mPicIv, R.drawable.loading, new OnImageLoadListener() {

			@Override
			public void onSuccess(ImageContainer response) {
				picIsLoaded = true;

			}

			@Override
			public void onFailed(VolleyError error) {
//				picIsLoaded = false;
//				Log.e("VolleyError-->", error.getMessage());

			}
		});
	}

	/**
	 * 单个检查点
	 * 
	 * @param pointModel
	 */
	public void setCheckPointImg(final HomeWorkCheckPointModel pointModel, boolean isAllowAddPoint, final int left,
			final int top, final int state, final int subjectid) {
		this.isAllowAddPoint = isAllowAddPoint;
		String imgpath = "";

		imgpath = pointModel.getImgpath();

		boolean isLocal = pointModel.isLocal();

		mPicContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int measuredWidth = mPicContainer.getMeasuredWidth();
				int measuredHeight = mPicContainer.getMeasuredHeight();

				if (measuredWidth != 0 && measuredHeight != 0) {
					mPicContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					isMeasure = true;
					mPicIv.setScreenSize(measuredWidth, measuredHeight);
					LogUtils.d(TAG, "yh setCheckPointImg() w = " + measuredWidth + ", h = " + measuredHeight);

					int isright = pointModel.getIsright();

					if (isright == GlobalContant.RIGHT_HOMEWORK) {

						setRightWrongPoint(pointModel, false, left, top, state, subjectid,false);
					}
				}
			}
		});

		if (isLocal) {
			Bitmap bm = BitmapFactory.decodeFile(imgpath);
			mPicIv.setCustomBitmap(bm);
			picIsLoaded = true;
		} else if (!isLocal) {
			ImageLoader.getInstance().loadImage(imgpath, mPicIv, R.drawable.loading, new OnImageLoadListener() {

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

	public void setCheckPointImg(HomeWorkCheckPointModel pointModel, boolean isAllowAddPoint, int state,
			int subjectid) {
		// setCheckPointImg(pointModel, isAllowAddPoint, 0, 0, state,
		// subjectid);
		setCheckPointImg(pointModel, isAllowAddPoint, state, subjectid, 0, 0);
	}

	/**
	 * 显示对与错
	 * 
	 * @author: sky
	 * @param checkPointModel
	 * @param isShowNext
	 * @param left
	 * @param top
	 * @param homeworkState
	 *            作业状态 void
	 */
	protected void setRightWrongPoint(final HomeWorkCheckPointModel checkPointModel, boolean isShowNext, int left,
			int top, final int homeworkState, final int subjectid, boolean isShowgood) {
		if (!NetworkUtils.getInstance().isInternetConnected(mActivity)) {
			ToastUtils.show("网络异常,不能正常打点");
			return;
		}
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		final int isright = checkPointModel.getIsright();
		if (isright == GlobalContant.RIGHT_HOMEWORK) {
			isShowNext = false;
		}
		final int checkpointid = checkPointModel.getId();
		String coordinateStr = checkPointModel.getCoordinate();

		if (pointMap.containsKey(coordinateStr)) {

			return;

		}

		if (coordinateStr == null) {

			return;
		}
		String[] coordinate = coordinateStr.split(",");
		float pointXPer = 0f;
		float pointYPer = 0f;
		if (coordinate.length < 2) {
			return;
		}
		if (!TextUtils.isEmpty(coordinate[0])) {
			pointXPer = Float.parseFloat(coordinate[0]);
		}
		if (!TextUtils.isEmpty(coordinate[1])) {
			pointYPer = Float.parseFloat(coordinate[1]);
		}

		int mContainerWidth = mPicIv.getLayoutParams().width;
		int mContainerHeight = mPicIv.getLayoutParams().height;

		final int currentX = (int) (mContainerWidth * pointXPer);
		final int currentY = (int) (mContainerHeight * pointYPer);

		// relativeParams.leftMargin = currentX < 0 ? 0 : currentX;
		// relativeParams.topMargin = currentY < 0 ? 0 : currentY;

		if (left == -1 && top == -1) {
			left = mPicIv.getLeft();
			top = mPicIv.getTop();
		}

		relativeParams.leftMargin = currentX + left;
		relativeParams.topMargin = currentY + top;
		LogUtils.e(TAG, "mPicIv.getLeft() = " + left);
		LogUtils.e(TAG, "mPicIv.getTop() = " + top);

		if (relativeParams.leftMargin < 0) {
			relativeParams.leftMargin = 0;
		}
		if (relativeParams.topMargin < 0) {
			relativeParams.topMargin = 0;
		}
		if (relativeParams.leftMargin > (widthPixels - frameWidthOrHeight)) {
			relativeParams.leftMargin = widthPixels - frameWidthOrHeight;
		}
		// 转化后的坐标存入已上传打点坐标集合，用于判断新打点时是否重叠
		ExplainPoint point = new ExplainPoint();
		point.setX(relativeParams.leftMargin);
		point.setY(relativeParams.topMargin);
		points.add(point);

		final RightWrongPointView iconSer = new RightWrongPointView(mActivity, isright,
				checkPointModel.getShowcomplainttype(), checkPointModel.getState());
		iconSer.setLayoutParams(relativeParams);
		final GoodView mGoodView = new GoodView(mActivity);

		mGoodView.setText("+1");
		mPicContainer.addView(iconSer);
		pointMap.put(coordinateStr, iconSer);
		// mRightWrongViewList.add(iconSer);
		if(TecApplication.isShowgoodview==1&&isShowgood) {
			TecApplication.isShowgoodview=0;
			new Handler().postDelayed(new Runnable() {
				public void run() {
					mGoodView.show(iconSer.getImageView());
				}
			}, 400);
		}
		// 长按删除单个对错
		if (checkPointModel.getShowcomplainttype() == 0) {
			iconSer.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (isright == GlobalContant.WRONG_HOMEWORK) {
						if (mActivity instanceof TecHomeWorkCheckDetailActivity) {
							((TecHomeWorkCheckDetailActivity) mActivity).delSingleCheckPoint(mPicContainer, iconSer,
									checkPointModel);
						}

					}

					if (isright == GlobalContant.RIGHT_HOMEWORK) {
						if (mActivity instanceof TecHomeWorkCheckDetailActivity) {
							((TecHomeWorkCheckDetailActivity) mActivity).delSingleCheckPoint(mPicContainer, iconSer,
									checkPointModel);
						}
					}

					return false;
				}
			});
		}
		if (isShowNext) {
			if (isright == GlobalContant.WRONG_HOMEWORK) {

				ImageView nextBtn = iconSer.getmNextBtn();
				nextBtn.setVisibility(View.VISIBLE);

				iconSer.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View nextBtn) {
						if (checkPointModel.getShowcomplainttype() != 1) {

							Bundle data = new Bundle();

							data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
							data.putBoolean("isAllowAddPoint", isAllowAddPoint);
							data.putInt("state", homeworkState);
							data.putInt("subjectid", subjectid);
							data.putInt("mCurrentItem", mCurrentItem);
							data.putStringArrayList("viewPagerList", viewPagerList);
							int clickAction = checkPointModel.getClickAction();
							// Intent intent = null;

							// if (clickAction ==
							// GlobalContant.ROLE_ID_COLLEAGE) {
							IntentManager.goToTecSingleCheckActivity(mActivity, data, false);
							// } else if (clickAction ==
							// GlobalContant.ROLE_ID_STUDENT) {
							// IntentManager.goToStuSingleCheckActivity(mActivity,
							// data, false);
							// }

						} else {
							if (checkPointModel.getRevisiontype() != 0) {
								ToastUtils.show("仲裁中");
								return;
							}
							if (checkPointModel.getComplainttype() == 3) {

								Bundle data = new Bundle();

								data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
								data.putInt("state", homeworkState);
								data.putBoolean("isAllowAddPoint", isAllowAddPoint);
								data.putInt("subjectid", subjectid);
								data.putInt("mCurrentItem", mCurrentItem);
								data.putStringArrayList("viewPagerList", viewPagerList);
								int clickAction = checkPointModel.getClickAction();
								// Intent intent = null;

								// if (clickAction ==
								// GlobalContant.ROLE_ID_COLLEAGE) {
								IntentManager.goToTecSingleCheckActivity(mActivity, data, false);
								// } else if (clickAction ==
								// GlobalContant.ROLE_ID_STUDENT) {
								// IntentManager.goToStuSingleCheckActivity(mActivity,
								// data, false);

								// }

							} else {
								View popuView = View.inflate(mActivity, R.layout.popu_jiucuo_menu, null);
								TextView tv_jiuzheng = (TextView) popuView.findViewById(R.id.tv_jiuzheng);
								TextView tv_duide = (TextView) popuView.findViewById(R.id.tv_duide);
								final PopupWindow pw = new PopupWindow(popuView, LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT, true);
								tv_jiuzheng.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										JSONObject jobj = new JSONObject();

										try {
											jobj.put("complaint_type", 1);
											jobj.put("sub_type", 2);
											jobj.put("checkpointid", checkpointid);
											OkHttpHelper.post(mActivity, "teacher", "revisiohomework", jobj,
													new HttpListener() {
												@Override
												public void onSuccess(int code, String dataJson, String errMsg) {
													if (code == 0) {
														pw.dismiss();
														mActivity.refresh();
													} else {
														ToastUtils.show(errMsg);
													}
												}

												@Override
												public void onFail(int HttpCode,String errMsg) {
													ToastUtils.show("网络异常");
												}
											});
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});

								tv_duide.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										JSONObject jobj = new JSONObject();

										try {
											jobj.put("complaint_type", 1);
											jobj.put("sub_type", 1);
											jobj.put("checkpointid", checkpointid);
											OkHttpHelper.post(mActivity, "teacher", "revisiohomework", jobj,
													new HttpListener() {
												@Override
												public void onSuccess(int code, String dataJson, String errMsg) {
													if (code == 0) {
														pw.dismiss();
														mActivity.refresh();
													} else {
														ToastUtils.show(errMsg);
													}
												}

												@Override
												public void onFail(int HttpCode,String errMsg) {
													ToastUtils.show("网络异常");
												}
											});
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
								});

								// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
								// 我觉得这里是API的一个bug
								pw.setBackgroundDrawable(new ColorDrawable(0));
								// 这里是位置显示方式,在屏幕的左侧
								// pw.showAtLocation(frameDelView, Gravity.LEFT,
								// currentX +
								// left,currentY + top);

								// 设置好参数之后再show
								pw.showAsDropDown(iconSer, 0, 0);
							}

						}
					}

				});

			} else {

			}

		} else {

			iconSer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View nextBtn) {
					if (checkPointModel.getShowcomplainttype() == 1) {
						if (checkPointModel.getRevisiontype() != 0) {
							ToastUtils.show("仲裁中");
							return;
						}
						View popuView = View.inflate(mActivity, R.layout.popu_jiucuo_menu, null);
						TextView tv_jiuzheng = (TextView) popuView.findViewById(R.id.tv_jiuzheng);
						TextView tv_duide = (TextView) popuView.findViewById(R.id.tv_duide);
						final PopupWindow pw = new PopupWindow(popuView, LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT, true);
						tv_jiuzheng.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								mActivity.showCamareContainer(checkPointModel);
								pw.dismiss();
							}
						});

						tv_duide.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								JSONObject jobj = new JSONObject();

								try {
									jobj.put("complaint_type", 2);
									jobj.put("sub_type", 1);
									jobj.put("checkpointid", checkpointid);
									OkHttpHelper.post(mActivity, "teacher", "revisiohomework", jobj, new HttpListener() {
										@Override
										public void onSuccess(int code, String dataJson, String errMsg) {
											if (code == 0) {
												pw.dismiss();
												mActivity.refresh();
											} else {
												ToastUtils.show(errMsg);
											}
										}

										@Override
										public void onFail(int HttpCode,String errMsg) {
											ToastUtils.show("网络异常");
										}
									});
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});

						// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
						// 我觉得这里是API的一个bug
						pw.setBackgroundDrawable(new ColorDrawable(0));
						// 这里是位置显示方式,在屏幕的左侧
						// pw.showAtLocation(frameDelView, Gravity.LEFT,
						// currentX +
						// left,currentY + top);

						// 设置好参数之后再show
						//pw.showAtLocation(iconSer, Gravity.TOP, 0, 0);
						pw.showAsDropDown(iconSer, 0, -20);

					}
				}
			});

		}

	}

	public void showCheckPoint(final ArrayList<HomeWorkSinglePoint> expointlist) {
		if (expointlist == null) {
			return;
		}
		if (picIsLoaded && isMeasure) {
			for (int i = 0; i < expointlist.size(); i++) {
				HomeWorkSinglePoint pointModel = expointlist.get(i);
				LogUtils.w("showCheckPoint", "showCheckPointxxxx");
				addVoiceOrTextPoint(pointModel);
			}
		} else {
			Message msg = Message.obtain();
			msg.what = EXPLAIN_POINT_LIST;
			msg.obj = expointlist;
			mHandler.sendMessageDelayed(msg, 1000);
		}
	}

	public void removeExPoint(View v, String coordinate) {
		mPicContainer.removeView(v);
		if (pointMap.containsKey(coordinate)) {
			pointMap.remove(coordinate);
		}
	}

	/**
	 * 移除所有的view
	 * 
	 * @author: sky void
	 */
	public void removeAllRightWrongPoint() {
		if (null != pointMap) {
			pointMap.clear();
		}
		if (null != mPicContainer) {
			mPicContainer.removeAllViews();
		}
	}

	/**
	 * 移除某个对错点
	 * 
	 * @param checkPointModel
	 */
	public void removeIndexRightWrongPoint(HomeWorkCheckPointModel checkPointModel) {
		if (null != pointMap) {
			String coordinateStr = checkPointModel.getCoordinate();
			pointMap.remove(coordinateStr);
		}

	}

	public VoiceOrTextPoint addVoiceOrTextPoint(final HomeWorkSinglePoint pointModel) {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		int explaintype = pointModel.getExplaintype();
		int roleid = pointModel.getRoleid();
		String coordinate = pointModel.getCoordinate();

		int exseqid = pointModel.getExseqid();

		String[] coordinates = coordinate.split(",");
		float pointXPer = Float.parseFloat(coordinates[0]);
		float pointYPer = Float.parseFloat(coordinates[1]);

		int mContainerWidth = mPicIv.getLayoutParams().width;
		int mContainerHeight = mPicIv.getLayoutParams().height;

		int currentX = (int) (mContainerWidth * pointXPer);
		int currentY = (int) (mContainerHeight * pointYPer);

		// relativeParams.leftMargin = currentX < 0 ? 0 : currentX;
		// relativeParams.topMargin = currentY < 0 ? 0 : currentY;

		relativeParams.leftMargin = currentX + mPicIv.getLeft();
		relativeParams.topMargin = currentY + mPicIv.getTop();
		LogUtils.d(TAG, "yh addVoiceOrTextPoint() left = " + mPicIv.getLeft() + ", top = " + mPicIv.getTop());
		// 少许偏移
		int mX5 = DisplayUtils.dip2px(mActivity, 15);
		relativeParams.leftMargin = relativeParams.leftMargin;
		relativeParams.topMargin = relativeParams.topMargin;
		if (relativeParams.leftMargin < 0) {
			relativeParams.leftMargin = 0;
		}
		if (relativeParams.topMargin < 0) {
			relativeParams.topMargin = 0;
		}
		if (relativeParams.leftMargin > (widthPixels - frameWidthOrHeight)) {
			relativeParams.leftMargin = widthPixels - frameWidthOrHeight;
		}

		// 转化后的坐标存入已上传打点坐标集合，用于判断新打点时是否重叠
		ExplainPoint point = new ExplainPoint();
		point.setX(relativeParams.leftMargin);
		point.setY(relativeParams.topMargin);
		points2.add(point);
		VoiceOrTextPoint pointViewContainer = new VoiceOrTextPoint(mActivity, roleid, pointModel.getSubtype());

		pointViewContainer.setLayoutParams(relativeParams);
		if (pointMap.containsKey(coordinate)) {
			return pointViewContainer;
		}

		mPicContainer.addView(pointViewContainer);
		pointMap.put(coordinate, pointViewContainer);
		if (exseqid == 0) {
			pointViewContainer.getIcSerView().setVisibility(View.GONE);
		} else {
			pointViewContainer.getIcSerView().setText(exseqid + "");
		}
		// mPointViewList.add(pointViewContainer); 
		final ImageView pointViewIc = pointViewContainer.getIcView();

		if (explaintype == GlobalContant.ANSWER_AUDIO) {// 声音
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
					if (TextUtils.isEmpty(pointModel.getSndpath())) {
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
					MediaUtil.getInstance(false).playVoice(false, pointModel.getSndpath(), mAnimationDrawable,
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
		} else if (explaintype == GlobalContant.ANSWER_TEXT) {
			if (roleid == GlobalContant.ROLE_ID_STUDENT) {// 学生红色
				pointViewIc.setImageResource(R.drawable.text_ic_stu_selector);
			} else {// 老师绿色
				pointViewIc.setImageResource(R.drawable.text_ic_tec_selector);
			}
			pointViewIc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					// Bundle bundle = new Bundle();
					// bundle.putString(PayAnswerTextAnswerActivity.ANSWER_TEXT,
					// pointModel.getText());
					// IntentManager.goToAnswertextView(mActivity, bundle);
					GlobalVariable.answertextPopupWindow = new AnswertextPopupWindow(mActivity, pointModel.getText());
				}
			});
		}
		return pointViewContainer;
	}

	public void showRightOrWrongPoint(ArrayList<HomeWorkCheckPointModel> checkpointlist, int state, int subjectid,
			ArrayList<String> list, int mCurrentItem) {
		if (list != null) {
			this.viewPagerList = list;
			this.mCurrentItem = mCurrentItem;
		}
		if (checkpointlist == null) {
			return;
		}
		if (picIsLoaded && isMeasure) {
			for (int i = 0; i < checkpointlist.size(); i++) {
				HomeWorkCheckPointModel pointModel = checkpointlist.get(i);
				if(i==(checkpointlist.size()-1)){

					setRightWrongPoint(pointModel, true, -1, -1, state, subjectid,true);
				}else{

					setRightWrongPoint(pointModel, true, -1, -1, state, subjectid,false);
				}
			}
		} else {
			Message msg = Message.obtain();
			msg.what = CHECK_POINT_LIST;
			msg.obj = checkpointlist;
			msg.arg1 = state;
			msg.arg2 = subjectid;
			mHandler.sendMessageDelayed(msg, 1000);
		}
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

	public void replaceFrameDelView() {
		if (frameDelView != null && mPicContainer != null) {
			// mPicContainer.removeView(frameDelView);
			// frameDelView = null;
			frameDelView.setErrorImage(R.drawable.wrong_answer_ic);

		}
		if (mActivity != null) {
			mActivity.hideAddPointBottomContainer();
		}
	}

	private boolean addCameraFrame(MotionEvent event) {
		if ((!picIsLoaded) || (!isMeasure)) {
			return false;
		}
		float x = event.getX();// 触摸相对于image的x坐标
		float y = event.getY();// 触摸相对于image的y坐标

		int mContainerWidth = mPicIv.getLayoutParams().width;
		int mContainerHeight = mPicIv.getLayoutParams().height;

		int currentX = (int) x - DisplayUtils.dip2px(mActivity, 14);// 方框左上角
		int currentY = (int) y - DisplayUtils.dip2px(mActivity, 15);// 方框左上角

		if (currentX < 0) {
			currentX = 0;
		}
		if (currentY < 0) {
			currentY = 0;
		}
		if (currentX > (mContainerWidth - frameWidthOrHeight)) {
			currentX = mContainerWidth - frameWidthOrHeight;
		}
		if (currentY > (mContainerHeight - frameWidthOrHeight)) {
			currentY = mContainerHeight - frameWidthOrHeight;
		}

		final int left = mPicIv.getLeft();
		final int top = mPicIv.getTop();
		int currentX2 = currentX + left;
		int currentY2 = currentY + top;
		if (isOverlay(currentX2, currentY2)) {
			return false;
		} else {

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			frameDelView = new CameraFrameWithDel(mActivity);
			if (mActivity instanceof TecHomeWorkSingleCheckActivity) {
				frameDelView.getBgView().setImageResource(R.drawable.v0_11);
			}
			params.leftMargin = currentX2;
			params.topMargin = currentY2;
			LogUtils.d(TAG, "yh addCameraFrame() left = " + left + ", top = " + top);

			frameDelView.setLayoutParams(params);
			frameDelView.invalidate();

			mPicContainer.addView(frameDelView);
			float coordinateX = (float) currentX / (float) mContainerWidth;
			float coordinateY = (float) (currentY - DisplayUtils.dip2px(mActivity, 15)) / (float) mContainerHeight;
			final String coordinate = coordinateX + "," + coordinateY;
			final int mcurrentX = currentX;
			final int mcurrentY = currentY;

			if (mActivity instanceof TecHomeWorkSingleCheckActivity) {
				int msubjectid = ((TecHomeWorkSingleCheckActivity) mActivity).subjectid;
				int mstate = ((TecHomeWorkSingleCheckActivity) mActivity).state;
				if (mstate != 3) {
					if (msubjectid == 1 | msubjectid == 5 | msubjectid == 6) {
						msubtype = 5;

						int sum = points2.size() + 1;
						((TecHomeWorkSingleCheckActivity) mActivity).showAddPointBottomContainer(coordinate, msubtype,
								sum);
					} else {

						dadianPopupWindow = new DadianPopupWindow2(mActivity, msubjectid, new DadianResultListener() {

							@Override
							public void onReturn(int subtype) {

								msubtype = subtype;

								int sum = points2.size() + 1;
								((TecHomeWorkSingleCheckActivity) mActivity).showAddPointBottomContainer(coordinate,
										msubtype, sum);
							}
						});
					}
				} else {
					flag2 = false;
					msubtype = 4;
					int sum = points2.size() + 1;
					((TecHomeWorkSingleCheckActivity) mActivity).showAddPointBottomContainer(coordinate, msubtype, sum);
				}
			}
			if (mActivity instanceof TecHomeWorkCheckDetailActivity) {
				((TecHomeWorkCheckDetailActivity) mActivity).showAddPointBottomContainer("hw_check_detail", coordinate,
						left, top, msubtype);

				// mActivity.showAddPointBottomContainer(coordinate);

				((TecHomeWorkCheckDetailActivity) mActivity).showRightOrWrong("hw_check_detail", frameDelView,
						mcurrentX, mcurrentY, left, top);

			}

			if (mActivity instanceof MyCheckedHomeworkActivity) {
				((MyCheckedHomeworkActivity) mActivity).showAddPointBottomContainer("hw_check_detail", coordinate, left,
						top, msubtype);

				// mActivity.showAddPointBottomContainer(coordinate);

				((MyCheckedHomeworkActivity) mActivity).showRightOrWrong("hw_check_detail", frameDelView, mcurrentX,
						mcurrentY, left, top);

			}

			return true;
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

			LogUtils.d(TAG, "yh old=" + oldOri + ", new=" + ori);
			// RotateAnimation animation1 = new RotateAnimation(oldOri, ori,
			// Animation.RELATIVE_TO_SELF, 0.5f,
			// Animation.RELATIVE_TO_SELF, 0.5f);
			// animation1.setDuration(270);
			// animation1.setFillAfter(true);

			pointMap.keySet();
			for (Iterator<String> iterator = pointMap.keySet().iterator(); iterator.hasNext();) {
				final FrameLayout fl = pointMap.get(iterator.next());
				// ObjectAnimator.ofFloat(fl, "rotation", oldOri,
				// ori).setDuration(270).start();

				AnimatorSet set = new AnimatorSet();
				ObjectAnimator anim3 = ObjectAnimator.ofFloat(fl, "rotation", oldOri, ori);
				anim3.setDuration(270);

				set.play(anim3);
				set.start();
			}
			oldOri = ori;
		}
	}

	/**
	 * 判断打点是否重叠
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	private boolean isOverlay(float currentX, float currentY) {
		int mX1 = DisplayUtils.dip2px(mActivity, 10);
		int mX2 = DisplayUtils.dip2px(mActivity, 56);
		int mX3 = DisplayUtils.dip2px(mActivity, 35);
		int mX4 = DisplayUtils.dip2px(mActivity, 29);
		boolean flag1 = false;
		for (ExplainPoint explainPoint : points2) {
			float x = explainPoint.getX();
			float y = explainPoint.getY();
			float absX = Math.abs(currentX - x);
			float absY = Math.abs(currentY - y);
			if (explainPoint.getRole() == GlobalContant.ROLE_ID_STUDENT
					| explainPoint.getRole() == GlobalContant.ROLE_ID_PARENTS) {
				flag2 = true;
			}

			if (mX2 < absX) {

			} else if (mX3 < absX) {
				if (x > currentX) {
					if ((y - currentY) < mX1) {
						flag1 = true;
					} else {

					}
				} else {
					if ((currentY - y) < mX1) {
						flag1 = true;
					} else {

					}
				}
			} else {
				if (mX3 < absY) {

				} else {
					flag1 = true;
				}
			}

		}
		for (ExplainPoint explainPoint : points) {
			float x2 = explainPoint.getX();
			float y2 = explainPoint.getY();
			if (mX4 > Math.abs(currentX - x2) && mX4 > Math.abs(currentY - y2)) {
				flag1 = true;
			}
		}

		return flag1;
	}
}

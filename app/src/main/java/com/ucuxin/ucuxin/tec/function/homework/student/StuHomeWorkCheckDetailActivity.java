package com.ucuxin.ucuxin.tec.function.homework.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.NetworkImageView;
import com.edmodo.cropper.CropImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.homework.CropImageActivity;
import com.ucuxin.ucuxin.tec.function.homework.SelectPicPopupWindow;
import com.ucuxin.ucuxin.tec.function.homework.StateOfHomeWork;
import com.ucuxin.ucuxin.tec.function.homework.adapter.StuHomeWorkDetailAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.viewpager.MyViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;

public class StuHomeWorkCheckDetailActivity extends BaseActivity implements OnClickListener, ViewPager.OnPageChangeListener {
	private int currentPosition;
	private ArrayList<View> dotLists;
	private LinearLayout dots_ll;
	private MyViewPager mViewPager;
	private HomeWorkCheckPointModel checkPointModel;
	private HomeWorkModel mHomeWorkModel;
	private static final int PIC_PUP_REQUSTECODE = 1002;
	private static final int SINGLE_POINT_REQUESTCODE = 1003;
	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
	private NetworkImageView mAvatarStuIv;
	private TextView mNickStuTv;
	private TextView mNumStuTv;
	private LinearLayout mBottomContainer;
	private int avatarSize;
	private int index = 0;
	private LinearLayout mCamareContainer;
	private StuHomeWorkDetailAdapter mAdapter;
	private ArrayList<String> viewPagerList = new ArrayList<String>();
	// private RefuseHomeWorkPopWindow mRefusePopWindow;
	// private AdoptHomeWorkCheckDialog mAdoptDialog;
	private boolean isCurrentUser;
	// private boolean checkingFlag;
	private TextView mGradeStuTv;
	private boolean isTec;
	int taskid = 0;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.homework_check_detail);
		setWelearnTitle(R.string.homework_detail_title_text);

		findViewById(R.id.back_layout).setOnClickListener(this);

		avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		findViewById(R.id.back_layout).setOnClickListener(this);
		dots_ll = (LinearLayout) findViewById(R.id.dots_ll);
		mViewPager = (MyViewPager) findViewById(R.id.detail_pager_homework);
		mViewPager.setOffscreenPageLimit(8);

		mAvatarStuIv = (NetworkImageView) findViewById(R.id.stu_avatar_iv_stu_detail);
		mNickStuTv = (TextView) findViewById(R.id.stu_nick_tv_stu_detail);
		mGradeStuTv = (TextView) findViewById(R.id.stu_grade_tv_stu_detail);
		mNumStuTv = (TextView) findViewById(R.id.stu_num_tv_stu_detail);
		findViewById(R.id.camare_btn_tec_detail).setOnClickListener(this);
		mCamareContainer = (LinearLayout) findViewById(R.id.camare_container_tec);
		mBottomContainer = (LinearLayout) findViewById(R.id.homework_detail_bottom_container_stu);
		findViewById(R.id.homework_detail_bottom_container_stu).setVisibility(View.VISIBLE);

		Intent intent = getIntent();
		if (intent != null) {
			// mHomeWorkModel = (HomeWorkModel)
			// intent.getSerializableExtra(HomeWorkModel.TAG);
			taskid = intent.getIntExtra("taskid", 0);
			currentPosition = intent.getIntExtra("position", 0);
			isTec = SharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_COLLEAGE;
			// if (mHomeWorkModel == null) {
			// }else {
			// taskid = mHomeWorkModel.getTaskid();
			// }
			refreshHomeWorkData(taskid);
		}

	}

	@Override
	public void refresh() {
		refreshHomeWorkData(taskid);
		mViewPager.setCurrentItem(currentPosition, false);
	}

	@Override
	public void showCamareContainer(HomeWorkCheckPointModel checkPointModel) {
		if (mCamareContainer != null) {
			mCamareContainer.setVisibility(View.VISIBLE);

		}
		this.checkPointModel = checkPointModel;

		hideAddPointBottomContainer();

	}

	@Override
	public void hideAddPointBottomContainer() {
		if (mBottomContainer != null) {
			mBottomContainer.setVisibility(View.GONE);
		}
	}

	public void showAddPointBottomContainer() {
		if (mBottomContainer != null) {
			mBottomContainer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideCamareContainer() {
		if (mCamareContainer != null) {
			mCamareContainer.setVisibility(View.GONE);
		}
		showAddPointBottomContainer();
	}

	private void setData() {
		int userId = SharePerfenceUtil.getInstance().getUserId();

		isCurrentUser = mHomeWorkModel.getGrabuserid() == userId;

		mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
		initDot(mHomeWorkPageModelList.size(), currentPosition);

		int state = mHomeWorkModel.getState();
		switch (state) {
		case StateOfHomeWork.ADOPTED:// 已采纳
		case StateOfHomeWork.ARBITRATED:// 仲裁完成
			break;
		case StateOfHomeWork.REPORT:// 已举报
		case StateOfHomeWork.DELETE:// 已删除
		case StateOfHomeWork.ASKING:// 抢题中
		case StateOfHomeWork.ANSWERING:// 答题中
		case StateOfHomeWork.ANSWERED:// 已回答
		case StateOfHomeWork.APPENDASK:// 追问中
			break;
		case StateOfHomeWork.REFUSE:// 已拒绝
			if (isCurrentUser && isTec) {
				findViewById(R.id.apply_arbitration_btn_stu_detail).setOnClickListener(this);
				findViewById(R.id.apply_arbitration_btn_stu_detail).setVisibility(View.VISIBLE);
			}
		case StateOfHomeWork.ARBITRATE:// 仲裁中
			if (isCurrentUser && isTec) {
				findViewById(R.id.giveup_arbitration_btn_stu_detail).setOnClickListener(this);
				findViewById(R.id.giveup_arbitration_btn_stu_detail).setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}

		ImageLoader.getInstance().loadImageWithDefaultAvatar(mHomeWorkModel.getAvatar(), mAvatarStuIv, avatarSize,
				avatarSize / 10);
		mAvatarStuIv.setOnClickListener(this);
		String studname = mHomeWorkModel.getStudname();
		if (!TextUtils.isEmpty(studname)) {
			mNickStuTv.setText(studname);
		}
		String grade = mHomeWorkModel.getGrade();
		if (!TextUtils.isEmpty(grade)) {
			mGradeStuTv.setText(grade);
		}
		int homeworkcnt = mHomeWorkModel.getHomeworkcnt();
		mNumStuTv.setText(getString(R.string.ask_num_text, homeworkcnt + ""));
		mAdapter = new StuHomeWorkDetailAdapter(getSupportFragmentManager(), mHomeWorkPageModelList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(currentPosition, false);// 加上fasle表示切换时不出现平滑效果
	}

	private void refreshHomeWorkData(int taskid) {
		showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", taskid);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		OkHttpHelper.post(this, "homework","getone", data, new HttpListener() {

			@Override
			public void onFail(int code,String errMsg) {
				closeDialog();
			}

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				closeDialog();
				if (code == 0) {
					try {
						mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
						mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (mHomeWorkPageModelList != null) {
						TecApplication.gradeid = mHomeWorkModel.getGradeid();
						TecApplication.subjectid = mHomeWorkModel.getSubjectid();
						setData();
						for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
							viewPagerList.add(pageModel.getImgpath());
							ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
							if (checkpointlist != null) {
								for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
									if (checkPointModel.getShowcomplainttype() ==2) {
										if (checkPointModel.getRevisiontype() != 0) {
											findViewById(R.id.giveup_arbitration_btn_stu_detail).setVisibility(View.GONE);
										}
									}
									checkPointModel.setClickAction(GlobalContant.ROLE_ID_STUDENT);
									checkPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());
									String teacheravatar = mHomeWorkModel.getTeacheravatar();
									if (teacheravatar != null) {
										checkPointModel.setTeacheravatar(teacheravatar);
									}
									checkPointModel.setTeacherhomeworkcnt(mHomeWorkModel.getTeacherhomeworkcnt());
									String teachername = mHomeWorkModel.getTeachername();
									if (teachername != null) {
										checkPointModel.setTeachername(teachername);
									}
									checkPointModel.setAllowAppendAsk(false);
								}
							}
						}

						mAdapter.setAllPageData(mHomeWorkPageModelList, mHomeWorkModel.getState(),
								mHomeWorkModel.getSubjectid());
						showAddPointBottomContainer();
					}
				} else {
					ToastUtils.show(errMsg);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalVariable.mViewPager = mViewPager;
	}
@Override
protected void onDestroy() {
	  GlobalVariable.mViewPager = null;
	super.onDestroy();
}
	// 初始化点
	private void initDot(int size, int defalutPosition) {
		dotLists = new ArrayList<View>();
		dots_ll.removeAllViews();
		for (int i = 0; i < size; i++) {
			// 设置点的宽高
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(this, 6),
					DisplayUtils.dip2px(this, 6));
			// 设置点的间距
			params.setMargins(7, 0, 7, 0);
			// 初始化点的对象
			View m = new View(this);
			// 把点的宽高设置到view里面
			m.setLayoutParams(params);
			if (i == defalutPosition) {
				// 默认情况下，首先会调用第一个点。就必须展示选中的点
				m.setBackgroundResource(R.drawable.dot_focus);
			} else {
				// 其他的点都是默认的。
				m.setBackgroundResource(R.drawable.dot_normal);
			}
			// 把所有的点装载进集合
			dotLists.add(m);
			// 现在的点进入到了布局里面
			dots_ll.addView(m);
		}
	}

	private void selectDot(int postion) {
		for (View dot : dotLists) {
			dot.setBackgroundResource(R.drawable.dot_normal);
		}
		dotLists.get(postion).setBackgroundResource(R.drawable.dot_focus);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.stu_avatar_iv_stu_detail:
			int userid = mHomeWorkModel.getStudid();
			IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_STUDENT);
			break;
		case R.id.apply_arbitration_btn_stu_detail:// 申请仲裁
			clickApplyArbitration();
			break;
		case R.id.giveup_arbitration_btn_stu_detail:// 放弃仲裁
			showDialog("请稍后");
			JSONObject data = new JSONObject();
			try {
				data.put("taskid", mHomeWorkModel.getTaskid());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(this, "teacher","giveuphomeworkarbitration", data, new HttpListener() {

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					closeDialog();
					if (code == 0) {
						uMengEvent("homework_giveuparbitration");
						findViewById(R.id.giveup_arbitration_btn_stu_detail).setVisibility(View.GONE);
						findViewById(R.id.apply_arbitration_btn_stu_detail).setVisibility(View.GONE);
						mHomeWorkModel.setState(StateOfHomeWork.ARBITRATED);
						ToastUtils.show("放弃仲裁成功");
					} else {
						ToastUtils.show(errMsg);
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
					closeDialog();
				}
			});
			break;
		case R.id.camare_btn_tec_detail:
			hideCamareContainer();
			hideAddPointBottomContainer();

			// 弹出选择框
			startActivityForResult(new Intent(this, SelectPicPopupWindow.class), PIC_PUP_REQUSTECODE);
			break;
		default:
			break;
		}

	}

	private void clickApplyArbitration() {
		showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", mHomeWorkModel.getTaskid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "teacher","applyhomeworkarbitration", data, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				closeDialog();
				if (code == 0) {
					uMengEvent("homework_applyarbitration");
					findViewById(R.id.apply_arbitration_btn_stu_detail).setVisibility(View.GONE);
					mHomeWorkModel.setState(StateOfHomeWork.ARBITRATE);
					ToastUtils.show("已提交仲裁申请，请耐心等待结果");
				} else {
					ToastUtils.show(errMsg);
				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				closeDialog();
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int postion) {
		currentPosition = postion;
		hideCamareContainer();
		selectDot(postion);
	}

	@Override
	public void onBackPressed() {
		int camareVisivility = mCamareContainer.getVisibility();
		if (camareVisivility == View.VISIBLE) {
			hideCamareContainer();
		}
		finish();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PIC_PUP_REQUSTECODE:// 答题错误拍照之后的回调
			if (resultCode == RESULT_OK && null != data) {
				String path = data.getStringExtra("path");
				// LogUtils.i(TAG, path);
				if (TextUtils.isEmpty(path)) {
					ToastUtils.show("path is null");
					return;
				}

				boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
				path = autoFixOrientation(path, isFromPhotoList, this, null);
				if (!TextUtils.isEmpty(path)) {
					goIntoSinglePoint(0, path);
				}

			}
			break;
		case SINGLE_POINT_REQUESTCODE:// 单页打点返回或者完成之后的回调
			if (resultCode == RESULT_OK && null != data) {
				boolean isSubmit = data.getBooleanExtra("isSubmit", false);
				if (isSubmit) {

					refresh();
				}
			}
			break;

		default:
			break;
		}

	}

	private void goIntoSinglePoint(int isRight, String imgpath) {
		boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
		ArrayList<HomeWorkCheckPointModel> checkpointlist = null;
		StuPublishHomeWorkPageModel pageModel = null;
		ArrayList<StuPublishHomeWorkPageModel> pagelist = null;
		if (isWrong) {
			uMengEvent("homework_checkwrong");
		} else {
			uMengEvent("homework_checkright");
		}

		if (mHomeWorkModel != null) {
			pagelist = mHomeWorkModel.getPagelist();
			pageModel = pagelist.get(currentPosition);
			checkpointlist = pageModel.getCheckpointlist();
		}
		HomeWorkCheckPointModel homeWorkCheckPointModel = new HomeWorkCheckPointModel();
		Bundle data = new Bundle();
		if (checkPointModel != null) {
			homeWorkCheckPointModel.setCoordinate(checkPointModel.getCoordinate());
			homeWorkCheckPointModel.setCpseqid(checkPointModel.getCpseqid());
			homeWorkCheckPointModel.setId(checkPointModel.getId());
			homeWorkCheckPointModel.setComplainttype(2);
			homeWorkCheckPointModel.setShowcomplainttype(checkPointModel.getShowcomplainttype());
			data.putInt("sub_type", 2);
		}
		homeWorkCheckPointModel.setImgpath(imgpath);
		homeWorkCheckPointModel.setIsright(isRight);
		homeWorkCheckPointModel.setLocal(isWrong);

		if (pagelist != null) {
			homeWorkCheckPointModel.setPicid(pageModel.getId());
		}

		data.putSerializable(HomeWorkCheckPointModel.TAG, homeWorkCheckPointModel);
		data.putBoolean("frist", true);
		data.putInt("left", 0);
		data.putInt("top", 0);
		data.putInt("mCurrentItem", mViewPager.getCurrentItem());
		data.putStringArrayList("viewPagerList", viewPagerList);
		if (mHomeWorkModel != null) {
			data.putInt("gradeid", mHomeWorkModel.getGradeid());
			data.putInt("subjectid", mHomeWorkModel.getSubjectid());
		}
		checkPointModel=null;
		IntentManager.goToTecSingleCheckActivity(this, data, false, SINGLE_POINT_REQUESTCODE);
	}

	public static String autoFixOrientation(String path, boolean isFromPhotoList, Activity activity,
			CropImageView imageView) {// ,
		int deg = 0;
		Matrix m = new Matrix();
		try {
			ExifInterface exif = new ExifInterface(path);

			int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (rotateValue) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				deg = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				deg = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				deg = 270;
				break;
			default:
				deg = 0;
				break;
			}
			m.preRotate(deg);
		} catch (Exception ee) {
			LogUtils.d("catch img error", "return");

		}

		try {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			int outWidth = opts.outWidth;
			int outHeight = opts.outHeight;
			// int ssize = 1;
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int widthPixels = metrics.widthPixels;
			int heightPixels = metrics.heightPixels;
			int ssize = 1;
			if (deg == 90 || deg == 270) {
				int temp = outWidth;
				outWidth = outHeight;
				outHeight = temp;
			}

			ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels
					: outWidth / widthPixels;
			if (ssize <= 0) {
				ssize = 1;

				float scaleWidth = 1.0f, scaleHeight = 1.0f;

				scaleWidth = (float) widthPixels / (float) outWidth;
				scaleHeight = (float) heightPixels / (float) outHeight;
				if (scaleWidth < scaleHeight) {
					scaleHeight = scaleWidth;
				} else {
					scaleWidth = scaleHeight;
				}
				m.postScale(scaleWidth, scaleHeight);

			}
			// Options opts = new Options();
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = ssize;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
			if (null == bitmap) {
				ToastUtils.show(R.string.text_image_not_exists);
				if (activity instanceof CropImageActivity) {
					activity.finish();
				}
				return path;
			}
			bitmap = WeLearnImageUtil.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			if (isFromPhotoList) {
				String absolutePath = activity.getCacheDir().getAbsolutePath();
				path = absolutePath + File.separator + "publish.png";
			}
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
			WeLearnImageUtil.saveFile(path, bitmap);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

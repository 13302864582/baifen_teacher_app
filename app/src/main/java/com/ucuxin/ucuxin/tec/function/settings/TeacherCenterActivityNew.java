package com.ucuxin.ucuxin.tec.function.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.WebViewActivity;
import com.ucuxin.ucuxin.tec.api.ShareAPI;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.MyShareListActivity;
import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.PackageManagerUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Request;

public class TeacherCenterActivityNew extends BaseActivity implements OnClickListener, HttpListener/*, OnUploadListener*/ {

	public static final String TAG = TeacherCenterActivityNew.class.getSimpleName();

	public static int SCALE_LOGO_IMG_SIZE = 120;

	private UserInfoModel uInfo = null;

	private ImageView tecBgIv;
	private ImageView tecAvatarIv;
	private TextView tecNickTv, tecSchroolTv, tecGoodSubjectXiaoTv, tecGoodSubjectChuTv, tecGoodSubjectGaoTv;

	private LinearLayout tecGoodSubjectsLayout;

	private LinearLayout moreLayout;

	private Button logoutBtn;

	private int avatarSize;// , headIvWidth, headIvHeight, headIvTopMargin,
							// headBgWidth, headBgHeight, headBgTopMargin;

	private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 0x1;
	private static final int REQUEST_CODE_GET_BG_IMAGE_FROM_SYS = 0x2;
	public static final int REQUEST_CODE_MODIFY = 0x3;
	public static final int REQUEST_CODE_CROP = 0x4;

	private AlphaAnimation aa = new AlphaAnimation(0f, 1.0f);

	private TextView tecCreditTv;

	private TextView tecIdTv;
	public static int count;
	private ImageView tecSexIv;

	private Bitmap mBitmap;

	private RelativeLayout layout_pingjia;

	private RelativeLayout layout_question,rl_tec_info;
	private RelativeLayout layout_homework, layout_iwantshare, layout_myshare;

	private ShareAPI shareApi = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_teacher_center);
		findViewById(R.id.back_layout).setOnClickListener(this);
		aa.setDuration(300);
		initView();
		initListener();
		updateUiInfo();
		// WeLearnApi.getUserInfoFromServer(this, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		WeLearnApi.getUserInfoFromServer(this, this);
	}

	public void initView() {
		avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);

		tecBgIv = (ImageView) findViewById(R.id.tec_info_bg_iv);
		tecAvatarIv = (ImageView) findViewById(R.id.tec_info_head_iv);
		tecNickTv = (TextView) findViewById(R.id.tec_info_nick_tv);
		tecSchroolTv = (TextView) findViewById(R.id.tec_info_school_tv);

		tecSexIv = (ImageView) findViewById(R.id.tec_info_sex_iv);// 信用/经验
		tecIdTv = (TextView) findViewById(R.id.userid_tv_tec_cen);// 用户ID
		tecCreditTv = (TextView) findViewById(R.id.credit_tv_tec_cen);// 信用/经验

		tecGoodSubjectsLayout = (LinearLayout) findViewById(R.id.tec_good_subject_layout);

		tecGoodSubjectXiaoTv = (TextView) findViewById(R.id.tec_good_subject_xiao_tv);
		tecGoodSubjectChuTv = (TextView) findViewById(R.id.tec_good_subject_chu_tv);
		tecGoodSubjectGaoTv = (TextView) findViewById(R.id.tec_good_subject_gao_tv);

		moreLayout = (LinearLayout) findViewById(R.id.tec_info_more_layout);
		moreLayout.setVisibility(View.GONE);

		logoutBtn = (Button) findViewById(R.id.tec_logout_btn);
		logoutBtn.setVisibility(View.VISIBLE);

		layout_pingjia = (RelativeLayout) this.findViewById(R.id.layout_pingjia);
		rl_tec_info = (RelativeLayout) this.findViewById(R.id.rl_tec_info);

		layout_homework = (RelativeLayout) this.findViewById(R.id.layout_homework);
		layout_question = (RelativeLayout) this.findViewById(R.id.layout_question);

		layout_iwantshare = (RelativeLayout) this.findViewById(R.id.layout_iwantshare);
		layout_myshare = (RelativeLayout) this.findViewById(R.id.layout_myshare);

		shareApi = new ShareAPI();

	}

	@Override
	public void initListener() {
		super.initListener();
		// tecAvatarIv.setOnClickListener(this);
		//tecBgIv.setOnClickListener(this);
		// findViewById(R.id.modifi_info_layout_tec_cen).setOnClickListener(this);//
		// 点击修改资料
		findViewById(R.id.rl_tec_info).setOnClickListener(this);// 点击修改资料
		tecGoodSubjectsLayout.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		layout_pingjia.setOnClickListener(this);
		layout_homework.setOnClickListener(this);
		layout_question.setOnClickListener(this);
		layout_iwantshare.setOnClickListener(this);
		layout_myshare.setOnClickListener(this);
	}

	private void updateUiInfo() {

		uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

		if (null == uInfo) {
			LogUtils.e(TAG, "Contact info gson is null !");
			return;
		}

		moreLayout.removeAllViews();

		// 背景
		String groupphoto = uInfo.getGroupphoto();
		if (groupphoto == null) {
			groupphoto = "";
		}
		// if (mBitmap != null) {
		// tecBgIv.setCustomBitmap(mBitmap);
		// }
		// ImageLoader.getInstance().loadImage(groupphoto, tecBgIv,
		// R.color.welearn_blue_heavy);
		Glide.with(this).load(groupphoto).centerCrop().placeholder(R.drawable.default_teacher_info_bg)
				.into(tecBgIv);

		// 头像
		String avatar_100 = uInfo.getAvatar_100();
		if (avatar_100 == null) {
			avatar_100 = "";
		}
		// ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar_100,
		// tecAvatarIv, avatarSize, avatarSize / 10);

		Glide.with(this).load(avatar_100).centerCrop().placeholder(R.drawable.teacher_default_avatar_circle).into(tecAvatarIv);

		String name = TextUtils.isEmpty(uInfo.getName()) ? getString(R.string.persion_info) : uInfo.getName();
		setWelearnTitle(name);
		tecNickTv.setText(name);

		String school = TextUtils.isEmpty(uInfo.getSchools()) ? "" : uInfo.getSchools();
		tecSchroolTv.setText(school);

		tecSexIv.setVisibility(View.VISIBLE);
		int sex = uInfo.getSex();
		switch (sex) {
		case GlobalContant.SEX_TYPE_MAN:
			tecSexIv.setImageResource(R.drawable.man_icon);
			break;
		case GlobalContant.SEX_TYPE_WOMEN:
			tecSexIv.setImageResource(R.drawable.woman_icon);
			break;

		default:
			tecSexIv.setVisibility(View.GONE);
			break;
		}
		int userid = uInfo.getUserid();
		tecIdTv.setText(getString(R.string.student_id, userid + ""));

		float credit = uInfo.getCredit();
		String creditStr = GoldToStringUtil.GoldToString(credit);
		tecCreditTv.setText(getString(R.string.credit_text, creditStr));

		tecGoodSubjectXiaoTv.setVisibility(View.GONE);
		tecGoodSubjectChuTv.setVisibility(View.GONE);
		tecGoodSubjectGaoTv.setVisibility(View.GONE);
		String tecMajor = uInfo.getTeachmajor();
		if (null != tecMajor) {
			String[] majors = tecMajor.split(";");
			if (null != majors) {
				if (majors.length > 0 && !TextUtils.isEmpty(majors[0])) {
					tecGoodSubjectXiaoTv.setText(majors[0]);
					tecGoodSubjectXiaoTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectXiaoTv.setVisibility(View.GONE);
				}
				if (majors.length > 1 && !TextUtils.isEmpty(majors[1])) {
					tecGoodSubjectChuTv.setText(majors[1]);
					tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectChuTv.setVisibility(View.GONE);
				}
				if (majors.length > 2 && !TextUtils.isEmpty(majors[2])) {
					tecGoodSubjectGaoTv.setText(majors[2]);
					tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectGaoTv.setVisibility(View.GONE);
				}
			}
		}
		// 工作
		String orgname = uInfo.getOrgname();
		orgid = uInfo.getOrgid();
		if (!TextUtils.isEmpty(orgname)) {
			View workView = getItem(R.string.stu_info_item_title_work, null, orgname, true);

			workView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (orgid != 0) {
						IntentManager.gotoCramSchoolDetailsActivity(TeacherCenterActivityNew.this, orgid);
					}
				}
			});
			moreLayout.addView(workView);

		}

		// 学生评价
		// View evaluationView = getItem(R.string.stu_evaluation_text, null,
		// String.valueOf(count), true);
		// evaluationView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// IntentManager.goToStudentAssessmentActivity(TeacherCenterActivityNew.this,
		// uInfo.getUserid());
		// }
		// });
		// moreLayout.addView(evaluationView);

	}

	private View getItem(int titleResId, String leftStr, String rightStr, boolean showArrow) {
		View v = View.inflate(this, R.layout.view_stu_info_item, null);

		TextView titleTV = (TextView) v.findViewById(R.id.item_title_tv);
		TextView titleValueTV = (TextView) v.findViewById(R.id.item_title_value_tv);
		TextView arrowValueTV = (TextView) v.findViewById(R.id.item_arrow_value_tv);
		ImageView arrowIv = (ImageView) v.findViewById(R.id.item_arrow_iv);

		titleTV.setText(titleResId);
		if (TextUtils.isEmpty(leftStr)) {
			leftStr = "";
		}
		titleValueTV.setText(leftStr);

		if (TextUtils.isEmpty(rightStr)) {
			rightStr = "";
		}
		arrowValueTV.setText(rightStr);

		arrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.tec_logout_btn:
			MobclickAgent.onEvent(this, "logout");
			// WeLearnApi.logout(this);
			showDialog(getString(R.string.text_logging_out));
			OkHttpHelper.post(this, "user", "logout", null, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					cleanUseInfo();

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
					cleanUseInfo();
				}
			});

			break;
		case R.id.tec_good_subject_layout:
			IntentManager.goToGradeSubjectChoice(this, false);
			break;
		case R.id.tec_info_bg_iv:
		//	startActivityForResult(new Intent(this, SelectPicPopupWindow.class), REQUEST_CODE_GET_BG_IMAGE_FROM_SYS);
			break;

		case R.id.rl_tec_info:
			IntentManager.goToStuModifiedInfoActivity(this);
			break;
		case R.id.layout_pingjia:// 评价
			IntentManager.goToStudentAssessmentActivity(TeacherCenterActivityNew.this, uInfo.getUserid());
			break;

		case R.id.layout_question:// 问题
			IntentManager.goToMyQpadActivity(this);
			break;
		case R.id.layout_homework:// 作业
			Intent i = new Intent(this, StuHomeWorkHallActivity.class);
			i.putExtra("packtype", 3);
			startActivity(i);
			break;
		case R.id.layout_iwantshare:// 我要分享
			if (NetworkUtils.getInstance().isInternetConnected(TeacherCenterActivityNew.this)) {
				shareApi.wantToShare(requestQueue, TeacherCenterActivityNew.this, RequestConstant.MY_WANT_SHARE_CODE);
			} else {
				ToastUtils.show("请检查网络连接");
			}
			break;
		case R.id.layout_myshare:// 我的分享
			Intent myShareIntent = new Intent(this, MyShareListActivity.class);
			startActivity(myShareIntent);
			break;
		}
	}

	private void cleanUseInfo() {
		IntentManager.stopWService(TeacherCenterActivityNew.this);

		// WApplication.mQQAuth.logout(WApplication.getContext());
		WLDBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
		SharePerfenceUtil.getInstance().setWelearnTokenId("");
		SharePerfenceUtil.getInstance().setTokenId("");
		SharePerfenceUtil.getInstance().setIsChoicGream(false);
		SharePerfenceUtil.getInstance().setGradeId(0);
		SharePerfenceUtil.getInstance().setGoLoginType(-1);
		SharePerfenceUtil.getInstance().setPhoneNum("");
		// WeLearnSpUtil.getInstance().setOpenId("");
		SharePerfenceUtil.getInstance().setAccessToken("");
		// new UserInfoDBHelper(mActivity).update(0, 0);
		// mActivity.finish();
		if (GlobalVariable.mainActivity != null) {
			GlobalVariable.mainActivity.finish();
		}
		closeDialog();
		IntentManager.goToLogInView(TeacherCenterActivityNew.this);

	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			updateUiInfo();
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case REQUEST_CODE_GET_BG_IMAGE_FROM_SYS:
				if (data != null) {

					String path = data.getStringExtra("path");
					boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
					LogUtils.i(TAG, path);
					if (TextUtils.isEmpty(path)) {
						LogUtils.i(TAG, "path is null");
						return;
					}
					path = TecHomeWorkCheckDetailActivity.autoFixOrientation(path, isFromPhotoList, this, null);

					IntentManager.goToCropPhotoActivity(this, path);

				}

				break;
			case REQUEST_CODE_MODIFY:
				break;
			case REQUEST_CODE_CROP:
				if (data != null) {
					String path = data.getStringExtra("path");
					mBitmap = BitmapFactory.decodeFile(path);
					postImageToServer("background", path);
				}
				break;
			default:
				break;
			}

		}
	}

	public static final String UPLOAD_URL = AppConfig.GO_URL + "user/update";

	private int orgid;

	private void postImageToServer(String key, String filePath) {
		try {
			Map<String, List<File>> files = new HashMap<String, List<File>>();
			List<File> fList = new ArrayList<File>();
			fList.add(new File(filePath));
			files.put("picfile", fList);
			JSONObject obj = new JSONObject();
			obj.put(key, "picfile");
			showDialog("上传中,请稍后");
			//UploadUtil.upload(UPLOAD_URL, RequestParamUtils.getParam(obj), files, this, true, 0);
			UploadUtil2.upload(UPLOAD_URL, RequestParamUtils.getMapParam(obj), files, new MyStringCallback(), true, 0);
		} catch (JSONException e) {
			e.printStackTrace();
			ToastUtils.show("error");
		}
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
			if (null == data) {
				ToastUtils.show(R.string.upload_failed);
				return;
			}
			if (code == 0) {
				WeLearnApi.getUserInfoFromServer(TeacherCenterActivityNew.this, TeacherCenterActivityNew.this);
			} else {
				if (!TextUtils.isEmpty(msg)) {
					ToastUtils.show(msg);
				} else {
					ToastUtils.show(R.string.upload_failed);
				}
			}
		}
		
	}
	
	/***************************************************************************/

	/*@Override
	public void onUploadSuccess(UploadResult result, int index) {
		closeDialog();
		if (null == result) {
			ToastUtils.show(R.string.upload_failed);
			return;
		}
		if (result.getCode() == 0) {
			WeLearnApi.getUserInfoFromServer(this, this);
		} else {
			if (!TextUtils.isEmpty(result.getMsg())) {
				ToastUtils.show(result.getMsg());
			} else {
				ToastUtils.show(R.string.upload_failed);
			}
		}
	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		if (!TextUtils.isEmpty(msg)) {
			ToastUtils.show(msg);
		}
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		if (null == result) {
			ToastUtils.show(R.string.upload_failed);
			return;
		}
		if (result.getCode() == 0) {
			WeLearnApi.getUserInfoFromServer(this, this);
		} else {
			if (!TextUtils.isEmpty(result.getMsg())) {
				ToastUtils.show(result.getMsg());
			} else {
				ToastUtils.show(R.string.upload_failed);
			}
		}
	}*/

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.MY_WANT_SHARE_CODE:// 我要分享
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {						
						String promotionurl = JsonUtils.getString(dataJson, "promotionurl", "");
						String codeUrl = "";
						if (promotionurl.contains("ucuxin.com")) {// 如果是有笔作业老师版的网址
							if (promotionurl.contains("?")) {
								codeUrl = promotionurl + "&userid={0}&phoneos={1}&appversion={2}";
								uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
								codeUrl = codeUrl.replace("{0}", uInfo.getUserid() + "").replace("{1}", "android")
										.replace("{2}", PackageManagerUtils.getVersionCode() + "");

							} else {
								codeUrl = promotionurl + "?userid={0}&phoneos={1}&appversion={2}";
								codeUrl = codeUrl.replace("{0}", uInfo.getUserid() + "").replace("{1}", "android")
										.replace("{2}", PackageManagerUtils.getVersionCode() + "");
							}
							Intent intent = new Intent(TeacherCenterActivityNew.this, WebViewActivity.class);
							intent.putExtra("title", "分享");
							intent.putExtra("url", codeUrl);
							startActivity(intent);

						} else {
							Toast.makeText(TeacherCenterActivityNew.this, "该网址不是有笔作业老师版的网址", 4).show();
						}

					}

				} else {
					ToastUtils.show(msg);
				}
			}
			break;

		}

	}

}

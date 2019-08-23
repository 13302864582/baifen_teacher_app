package com.ucuxin.ucuxin.tec.function.settings;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.SelectPicPopupWindow;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.SelectEduDialog;
import com.ucuxin.ucuxin.tec.view.dialog.SelectEduDialog.OnEduUpdatedListener;
import com.ucuxin.ucuxin.tec.view.dialog.SelectSexDialog;
import com.ucuxin.ucuxin.tec.view.dialog.SelectSexDialog.OnSexUpdatedListener;
import com.ucuxin.ucuxin.tec.view.dialog.TextOkCancleDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Request;

public class StuModifiedInfoActivity extends BaseActivity
		implements OnClickListener/* , OnUploadListener */ {
	private static final String TAG = StuModifiedInfoActivity.class.getSimpleName();
	private static final String UPLOAD_URL = AppConfig.GO_URL + "user/update";
	private static final int REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS = 101;
	private static final int REQUEST_CODE_BIND = 102;
	public static final int REQUEST_CODE_GRADE = 104;
	private View changeAvatarBtn;
	private TextView sexTv;
	private View changeSexBtn;
	private UserInfoModel uInfo;
	private EditText nickEt;
	private View saveBtn;
	private int avatarSize;
	private NetworkImageView avatarIv;
	private String mPath;
	private View changeXueliBtn;
	private TextView xueliTv;
	private EditText schoolEt;
	private EditText majorEt;

	private String avatar_100Str = "";

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_modified_data);
		initView();
		initListener();
		initData();

	}

	@Override
	public void initView() {
		super.initView();
		setWelearnTitle(R.string.modify_info_text);

		findViewById(R.id.back_layout).setOnClickListener(this);

		avatarIv = (NetworkImageView) findViewById(R.id.avatar_iv_modified);
		nickEt = (EditText) findViewById(R.id.nick_et_modified);

		changeAvatarBtn = findViewById(R.id.change_avatar_btn_modified);
		changeSexBtn = findViewById(R.id.change_sex_btn_modified);
		changeXueliBtn = findViewById(R.id.change_xueli_btn_modified);
		saveBtn = findViewById(R.id.save_btn_modified);

		sexTv = (TextView) findViewById(R.id.sex_tv_modified);
		xueliTv = (TextView) findViewById(R.id.xueli_tv_modified);
		schoolEt = (EditText) findViewById(R.id.school_et_modified);
		majorEt = (EditText) findViewById(R.id.major_et_modified);

		avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);
	}

	@Override
	public void initListener() {
		super.initListener();
		changeAvatarBtn.setOnClickListener(this);
		changeXueliBtn.setOnClickListener(this);
		changeSexBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);

	}

	private void initData() {
		uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null == uInfo) {
			LogUtils.e(TAG, "User info gson is null !");
			return;
		}
		String avatar_100 = uInfo.getAvatar_100();
		if (avatar_100 == null) {
			avatar_100 = "";
		}
		avatar_100Str = avatar_100;
		ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar_100, avatarIv, avatarSize, avatarSize / 10);

		String name = uInfo.getName();
		nickEt.setText(name);

		int sex = uInfo.getSex();
		// // 性别
		int sexResId = R.string.sextype_unknown;
		switch (sex) {
		case GlobalContant.SEX_TYPE_MAN:
			sexResId = R.string.sextype_man;
			break;
		case GlobalContant.SEX_TYPE_WOMEN:
			sexResId = R.string.sextype_women;
			break;
		}
		String sexStr = getString(sexResId);
		sexTv.setText(sexStr);

		String eduLevelStr = getEduLevelStr(uInfo.getEdulevel());
		xueliTv.setText(eduLevelStr);

		String schools = uInfo.getSchools();
		if (!TextUtils.isEmpty(schools)) {
			schoolEt.setText(schools);
		}

		String major = uInfo.getMajor();
		if (!TextUtils.isEmpty(major)) {
			majorEt.setText(major);
		}

	}

	private String getEduLevelStr(int eduLevel) {
		switch (eduLevel) {
		case 0:
			return getString(R.string.edulevel1);
		case 1:
			return getString(R.string.edulevel2);
		case 2:
			return getString(R.string.edulevel3);
		case 3:
			return getString(R.string.edulevel4);
		default:
			return "";
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			// finish();
			if (NetworkUtils.getInstance().isInternetConnected(this)) {
				boolean isSave = judgeIsSave();
				if (isSave) {
					final TextOkCancleDialog dialog = new TextOkCancleDialog(this);
					dialog.setOnPositiveListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							submitUpdateUserinfo();

						}
					});

					dialog.setOnNegativeListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							finish();
						}
					});
					dialog.show();
				} else {
					finish();
				}
			} else {
				ToastUtils.show("没网络了");
				finish();
			}
			break;
		case R.id.change_avatar_btn_modified:
			startActivityForResult(new Intent(this, SelectPicPopupWindow.class), REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS);
			break;
		case R.id.change_xueli_btn_modified:
			showEduDialog();
			break;
		case R.id.change_sex_btn_modified:
			showSexDialog();
			break;
		case R.id.save_btn_modified:
			submitUpdateUserinfo();
			break;

		default:
			break;
		}
	}

	private void submitUpdateUserinfo() {
		String name = nickEt.getText().toString().trim();
		String schools = schoolEt.getText().toString().trim();
		String major = majorEt.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			ToastUtils.show("昵称不能为空!");
			return;
		}

		showDialog("请稍后");

		WeLearnApi.updateUserInfoFromServer(this, mPath, name, uInfo.getSex(), uInfo.getEdulevel(), schools, major,
				new MyStringCallback());

	}

	class MyStringCallback extends StringCallback {

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
				ToastUtils.show("修改成功!");
				setResult(RESULT_OK);
				finish();
			} else {
				ToastUtils.show(msg);
			}

		}

	}

	/*
	 * @Override public void onUploadSuccess(UploadResult result, int index) {
	 * closeDialog(); ToastUtils.show("修改成功!"); setResult(RESULT_OK); finish();
	 * }
	 * 
	 * @Override public void onUploadError(String msg, int index) {
	 * closeDialog(); if (!TextUtils.isEmpty(msg)) { // ToastUtils.show(msg); }
	 * }
	 * 
	 * @Override public void onUploadFail(UploadResult result, int index) {
	 * closeDialog(); if (result != null) { String msg = result.getMsg(); if
	 * (!TextUtils.isEmpty(msg)) { ToastUtils.show(msg); } } }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_GET_HEAD_IMAGE_FROM_SYS:// 换头像
				String path = data.getStringExtra("path");
				LogUtils.i(TAG, path);
				if (TextUtils.isEmpty(path)) {
					LogUtils.i(TAG, "path is null");
					return;
				}

				boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);

				// String saveImagePath =
				// WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() +
				// File.separator
				// + "publish_" + System.currentTimeMillis() + ".jpg";
				//
				// Intent localIntent = new
				// Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				// Uri.fromFile(new File(path)));
				// sendBroadcast(localIntent);
				//
				// localIntent.setClass(this, CropImageActivity.class);
				// localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH,
				// path);
				// localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG,
				// saveImagePath);
				// localIntent.putExtra("isFromPhotoList", isFromPhotoList);
				// startActivityForResult(localIntent,
				// StuPublishHomeWorkActivity.REQUEST_CODE_GET_IMAGE_FROM_CROP);
				IntentManager.goToCropImageActivity(this, path, isFromPhotoList);
				break;
			case 1202:
				mPath = data.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
				LogUtils.i(TAG, "path=" + mPath);
				if (!TextUtils.isEmpty(mPath)) {
					Bitmap bm = BitmapFactory.decodeFile(mPath);
					avatarIv.setImageBitmap(bm);
				}

				break;
			default:
				break;
			}
		}

	}

	private void showSexDialog() {
		SelectSexDialog ss = new SelectSexDialog(this, uInfo.getSex(), new OnSexUpdatedListener() {

			@Override
			public void onSexUpdated(int selectSex) {
				uInfo.setSex(selectSex);

				int sex = uInfo.getSex();
				// // 性别
				int sexResId = R.string.sextype_unknown;
				switch (sex) {
				case GlobalContant.SEX_TYPE_MAN:
					sexResId = R.string.sextype_man;
					break;
				case GlobalContant.SEX_TYPE_WOMEN:
					sexResId = R.string.sextype_women;
					break;
				}
				String sexStr = getString(sexResId);
				sexTv.setText(sexStr);
			}
		});
		ss.setCanceledOnTouchOutside(true);
		ss.show();
	}

	private void showEduDialog() {
		SelectEduDialog ss = new SelectEduDialog(this, uInfo.getEdulevel(), new OnEduUpdatedListener() {

			@Override
			public void onEduUpdated(int eduLevel) {
				uInfo.setEdulevel(eduLevel);
				String eduLevelStr = getEduLevelStr(uInfo.getEdulevel());
				xueliTv.setText(eduLevelStr);

			}
		});
		ss.setCanceledOnTouchOutside(true);
		ss.show();
	}

	private boolean judgeIsSave() {
		boolean isRn = false;
		String nickStr = nickEt.getText().toString().trim();
		String genderStr = sexTv.getText().toString().trim();

		int sexInt = 0;
		// 性别
		if ("男".equals(genderStr)) {
			sexInt = 1;
		} else if ("女".equals(genderStr)) {
			sexInt = 2;
		} else {
			sexInt = 0;
		}

		String eduLevelStr = xueliTv.getText().toString().trim();
		int eduLevelInt = ReverseGetEduLevelStr(eduLevelStr);

		String schoolStr = schoolEt.getText().toString().trim();
		String majorStr = majorEt.getText().toString().trim();

		UserInfoModel userinfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

		String avatar_100 = userinfo.getAvatar_100();
		if (avatar_100 == null) {
			avatar_100 = "";
		}

		if (!avatar_100.equals(avatar_100Str)) {
			isRn = true;
		} else if (!userinfo.getName().equals(nickStr)) {
			isRn = true;
		} else if (userinfo.getSex() != sexInt) {
			isRn = true;
		} else if (userinfo.getEdulevel() != eduLevelInt) {
			isRn = true;
		} else if (!userinfo.getSchools().equals(schoolStr)) {
			isRn = true;
		} else isRn = !userinfo.getMajor().equals(majorStr);
		return isRn;
	}

	private int ReverseGetEduLevelStr(String eduLevelStr) {
		int eduLevel = 0;
		if (getString(R.string.edulevel1).equals(eduLevelStr)) {
			eduLevel = 0;
		} else if (getString(R.string.edulevel2).equals(eduLevelStr)) {
			eduLevel = 1;
		} else if (getString(R.string.edulevel3).equals(eduLevelStr)) {
			eduLevel = 2;
		} else if (getString(R.string.edulevel4).equals(eduLevelStr)) {
			eduLevel = 3;
		} else {
			eduLevel = 0;
		}
		return eduLevel;
	}

}

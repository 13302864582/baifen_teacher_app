package com.ucuxin.ucuxin.tec;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.manager.UpdateManagerForDialog;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import androidx.fragment.app.Fragment;

/**
 * 侧边栏
 * @author:  sky
 */
public class MenuFragment extends Fragment implements OnClickListener {

	public static final String TAG = MenuFragment.class.getSimpleName();

	/** 账号名字 */
	private TextView mAccNameTv, mAccNumTv, mMoney, mCredit;

	/** 账号头像 */
	private NetworkImageView mAccHeadImv;

	private UserInfoModel userInfo;

	/** 学点充值 */
	private RelativeLayout mAskLayout;
	private RelativeLayout mCheckedHomeworkLayout;
	private RelativeLayout checkUpdateLayout;
	private View updateTips;
	private int latestVersion;
	private UpdateManagerForDialog mUpdateManager;

	// private ImageView qrIV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUpdateManager = new UpdateManagerForDialog(getActivity());
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welearn_menu, null);
		mAccHeadImv = (NetworkImageView) view.findViewById(R.id.menu_acc_head_icon);
		mAccHeadImv.setOnClickListener(this);

		view.findViewById(R.id.menu_recharge).setVisibility(View.GONE);

		mAskLayout = (RelativeLayout) view.findViewById(R.id.menu_my_ask);

		mCheckedHomeworkLayout = (RelativeLayout) view.findViewById(R.id.menu_my_homework);
		mCheckedHomeworkLayout.setOnClickListener(this);
		
		mAskLayout.setVisibility(View.GONE);
		mCheckedHomeworkLayout.setVisibility(View.GONE);

		view.findViewById(R.id.menu_settings).setOnClickListener(this);
		view.findViewById(R.id.menu_persion_info).setOnClickListener(this);
		view.findViewById(R.id.menu_about_us).setOnClickListener(this);
		checkUpdateLayout = (RelativeLayout) view.findViewById(R.id.menu_check_update);
		checkUpdateLayout.setOnClickListener(this);
		mAccNameTv = (TextView) view.findViewById(R.id.menu_acc_name);
		mAccNumTv = (TextView) view.findViewById(R.id.menu_stu_num);
		mMoney = (TextView) view.findViewById(R.id.menu_money);
		mCredit = (TextView) view.findViewById(R.id.menu_credit);
		updateTips = view.findViewById(R.id.tips_update_iv_setting);
		// qrIV = (ImageView) view.findViewById(R.id.qr_imageview);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUIUserInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_recharge:
			break;
		case R.id.menu_my_ask:
			break;
		case R.id.menu_my_homework:
			Intent i = new Intent(getActivity(), StuHomeWorkHallActivity.class);
			// IntentManager.goToStuHomeWorkHallActivity(getActivity());
			i.putExtra("packtype", 2);
			getActivity().startActivity(i);
			break;
		case R.id.menu_settings:
			IntentManager.goToSystemSetting(getActivity());
			break;
		case R.id.menu_check_update:
			WeLearnApi.checkUpdate();
			latestVersion = SharePerfenceUtil.getInstance().getLatestVersion();
			if (latestVersion > TecApplication.versionCode) {
				if (latestVersion > TecApplication.versionCode) {
					updateTips.setVisibility(View.VISIBLE);
				} else {
					updateTips.setVisibility(View.GONE);
				}
				if (mUpdateManager == null) {
					if (!getActivity().isFinishing()) {
						mUpdateManager = new UpdateManagerForDialog(getActivity());
					}
				}
				if (mUpdateManager != null) {
					mUpdateManager.cloesNoticeDialog();
					mUpdateManager.showNoticeDialog(false);
				}
			} else {
				ToastUtils.show("没有发现新版本");
			}
			break;
		case R.id.menu_about_us:
			IntentManager.goToAbout(getActivity());
			break;
		case R.id.menu_acc_head_icon:
		case R.id.menu_persion_info:
			Bundle data = new Bundle();
			data.putInt("userid", userInfo.getUserid());
			data.putInt("roleid", userInfo.getRoleid());
			IntentManager.goToTeacherCenterView(getActivity(),TeacherCenterActivityNew.class, data);
			break;
		}
	}

	public static boolean isDoInDb = false;

	private void updateUIUserInfo() {
		userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null == userInfo) {
			ToastUtils.show(R.string.text_unlogin);
			return;
		}

		if (userInfo.getRoleid() == GlobalContant.ROLE_ID_COLLEAGE) {
			mAskLayout.setVisibility(View.GONE);
			mCheckedHomeworkLayout.setVisibility(View.GONE);
		} else {
		}

		// ImageLoader.getInstance().loadImage(userInfo.getAvatar_100(),
		// mAccHeadImv, R.drawable.default_contact_image);
		int imageSize = getResources().getDimensionPixelSize(R.dimen.menu_persion_icon_size);
		ImageLoader.getInstance().loadImage(userInfo.getAvatar_100(), mAccHeadImv, R.drawable.default_contact_image,
				R.drawable.default_contact_image, imageSize, imageSize / 10);
		mAccNameTv.setText(userInfo.getName() == null ? "" : userInfo.getName());
		mAccNumTv.setText(getString(R.string.student_id, userInfo.getUserid()));

		double gold = userInfo.getGold();
		String goldStr = GoldToStringUtil.GoldToString(gold);
		mMoney.setText(goldStr);
		mCredit.setText(String.valueOf(userInfo.getCredit()));
	}

	public void onMenuFragmentResume() {
		if (null != updateTips) {
			latestVersion = SharePerfenceUtil.getInstance().getLatestVersion();
			if (latestVersion > TecApplication.versionCode) {
				updateTips.setVisibility(View.VISIBLE);
			} else {
				updateTips.setVisibility(View.GONE);
			}
		}
		
		//  更新用户信息
		WeLearnApi.getUserInfoFromServer(getActivity(), new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				updateUIUserInfo();
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
			}
		});
	}
}

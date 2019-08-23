package com.ucuxin.ucuxin.tec.function.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.manager.UpdateManagerForDialog;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.SegmentedControl;
import com.ucuxin.ucuxin.tec.view.SegmentedControl.OnSegmentChangedListener;

public class SystemSettingActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = SystemSettingActivity.class.getSimpleName();

	private SegmentedControl notifySegmentedControl = null;
	private SegmentedControl vibrateSegmentedControl = null;
	// private LogoutController mLogoutController = null;

	private UpdateManagerForDialog mUpdateManager;
	private int latestVersion;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragment_system_setting);

		setWelearnTitle(R.string.system_settings);

		findViewById(R.id.back_layout).setOnClickListener(this);

		notifySegmentedControl = (SegmentedControl) findViewById(R.id.allowMsgNotify);
		notifySegmentedControl.setStyle(SegmentedControl.SEGMENT);
		notifySegmentedControl.newButton(getResources().getString(R.string.system_setting_close), 0);
		notifySegmentedControl.newButton(getResources().getString(R.string.system_setting_open), 1);
		if (SharePerfenceUtil.getInstance().getMsgNotifyFlag()) {
			notifySegmentedControl.setSelectedIndex(1);
		} else {
			notifySegmentedControl.setSelectedIndex(0);
		}

		vibrateSegmentedControl = (SegmentedControl) findViewById(R.id.allowMsgVibrate);
		vibrateSegmentedControl.setStyle(SegmentedControl.SEGMENT);
		vibrateSegmentedControl.newButton(getResources().getString(R.string.system_setting_close), 0);
		vibrateSegmentedControl.newButton(getResources().getString(R.string.system_setting_open), 1);
		if (SharePerfenceUtil.getInstance().getMsgNotifyVibrate()) {
			vibrateSegmentedControl.setSelectedIndex(1);
		} else {
			vibrateSegmentedControl.setSelectedIndex(0);
		}

		installListeners();
	}

	private void installListeners() {
		// find view object with id and add listener on it.
		notifySegmentedControl.setOnSegmentChangedListener(new OnSegmentChangedListener() {
			@Override
			public void onSegmentChanged(int index) {
				notifySegmentedControl.setSelectedIndex(index);
				if (index == 1) {
					SharePerfenceUtil.getInstance().setMsgNotifyFlag(true);
				} else {
					SharePerfenceUtil.getInstance().setMsgNotifyFlag(false);
				}
			}
		});

		vibrateSegmentedControl.setOnSegmentChangedListener(new OnSegmentChangedListener() {
			@Override
			public void onSegmentChanged(int index) {
				vibrateSegmentedControl.setSelectedIndex(index);
				if (index == 1) {
					SharePerfenceUtil.getInstance().setMsgNotifyVibrate(true);
				} else {
					SharePerfenceUtil.getInstance().setMsgNotifyVibrate(false);
				}
			}
		});

		findViewById(R.id.menu_about_us).setOnClickListener(this);

		findViewById(R.id.about_btn).setOnClickListener(this);

		findViewById(R.id.user_respone_btn).setOnClickListener(this);

		// 暂时作为免打扰设置入口
		findViewById(R.id.not_distur_btn).setOnClickListener(this);

		findViewById(R.id.menu_check_update).setOnClickListener(this);
		findViewById(R.id.check_update).setOnClickListener(this);
		// app检查更新
		View updateTips = findViewById(R.id.tips_update_iv_setting);
		latestVersion = SharePerfenceUtil.getInstance().getLatestVersion();
		if (latestVersion > TecApplication.versionCode) {
			updateTips.setVisibility(View.VISIBLE);
		} else {
			updateTips.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// StatService.onEventStart(mActivity, "systemSetting", "");
		MobclickAgent.onEventBegin(this, "systemSetting");
	}

	@Override
	public void onPause() {
		super.onPause();
		// StatService.onEventEnd(mActivity, "systemSetting", "");
		MobclickAgent.onEventEnd(this, "systemSetting");
		// WelearnHandler.getInstance().removeMessage(MsgDef.MSG_DEF_LOGOUT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalVariable.centerActivity = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.menu_about_us:
		case R.id.about_btn:
			MobclickAgent.onEvent(SystemSettingActivity.this, "about");
			IntentManager.goToAbout(SystemSettingActivity.this);
			break;
		case R.id.user_respone_btn:
			MobclickAgent.onEvent(SystemSettingActivity.this, "userRequest");
			IntentManager.goToUserRequest(SystemSettingActivity.this);
			break;
		case R.id.not_distur_btn:
			IntentManager.goToDoNotDisturbActivity(SystemSettingActivity.this, null, false);
			break;
		case R.id.menu_check_update:
		case R.id.check_update:
			if (latestVersion > TecApplication.versionCode) {

				if (mUpdateManager == null) {
					if (!SystemSettingActivity.this.isFinishing()) {
						mUpdateManager = new UpdateManagerForDialog(SystemSettingActivity.this);
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
		}
	}
}

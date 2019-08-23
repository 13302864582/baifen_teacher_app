package com.ucuxin.ucuxin.tec.function.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.view.SegmentedControl;
import com.ucuxin.ucuxin.tec.view.SegmentedControl.OnSegmentChangedListener;

/**
 * 免打扰设置
 * @author:  sky
 */
public class DoNotDisturbActivity extends BaseActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.setTitle("免打扰设置");

		setContentView(R.layout.fragment_donotdistur);
		
		setWelearnTitle(R.string.dnd_settings);
		
		findViewById(R.id.back_layout).setOnClickListener(this);

		final SegmentedControl day_sc = (SegmentedControl) findViewById(R.id.day_time_segmentedcontrol_donotdis);
		day_sc.setStyle(SegmentedControl.SEGMENT);
		day_sc.newButton(getResources().getString(R.string.system_setting_close), 0);
		day_sc.newButton(getResources().getString(R.string.system_setting_open), 1);
		if (SharePerfenceUtil.getInstance().getDayNotDis()) {
			day_sc.setSelectedIndex(1);
		} else {
			day_sc.setSelectedIndex(0);
		}
		day_sc.setOnSegmentChangedListener(new OnSegmentChangedListener() {
			@Override
			public void onSegmentChanged(int index) {
				day_sc.setSelectedIndex(index);
				if (index == 1) {
					SharePerfenceUtil.getInstance().setDayNotDis(true);
				} else {
					SharePerfenceUtil.getInstance().setDayNotDis(false);
				}
			}
		});

		final SegmentedControl night_sc = (SegmentedControl) findViewById(R.id.night_time_segmentedcontrol_donotdis);
		night_sc.setStyle(SegmentedControl.SEGMENT);
		night_sc.newButton(getResources().getString(R.string.system_setting_close), 0);
		night_sc.newButton(getResources().getString(R.string.system_setting_open), 1);
		if (SharePerfenceUtil.getInstance().getNightNotDis()) {
			night_sc.setSelectedIndex(1);
		} else {
			night_sc.setSelectedIndex(0);
		}
		night_sc.setOnSegmentChangedListener(new OnSegmentChangedListener() {
			@Override
			public void onSegmentChanged(int index) {
				night_sc.setSelectedIndex(index);
				if (index == 1) {
					SharePerfenceUtil.getInstance().setNightNotDis(true);
				} else {
					SharePerfenceUtil.getInstance().setNightNotDis(false);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
}

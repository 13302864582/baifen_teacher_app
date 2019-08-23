package com.ucuxin.ucuxin.tec.function.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 关于有笔作业老师版
 * @author:  sky
 */
public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_about1);
		if (AppConfig.IS_DEBUG) {
			ToastUtils.show("该应用安装包来自渠道:" + TecApplication.getChannelValue());
		}
		TextView version = (TextView) findViewById(R.id.version_name_about);
		version.setText(getString(R.string.version_format_str, TecApplication.versionName));
		setWelearnTitle(R.string.about_us);
		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}

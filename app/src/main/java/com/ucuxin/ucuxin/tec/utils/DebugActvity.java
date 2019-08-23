package com.ucuxin.ucuxin.tec.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.account.PhoneLoginActivity;

public class DebugActvity extends BaseActivity implements OnClickListener {
	private Button changTo118Btn;
	private TextView pythonTv;
	private TextView goTv;
	private Button changTo24Btn;
	private Button submitBtn;
	private EditText et_python;
	private EditText et_go;
	
	private Button btn_shang;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragment_debug_changeip);

		pythonTv = (TextView) findViewById(R.id.python_tv_debug);
		goTv = (TextView) findViewById(R.id.go_tv_debug);
		changTo118Btn = (Button) findViewById(R.id.bt_118_debug_changeip);
        btn_shang=(Button)findViewById(R.id.btn_shang_debug_changeip);
		changTo24Btn = (Button) findViewById(R.id.bt_24_debug_changeip);

		
		et_python = (EditText) findViewById(R.id.py_et_debug);
		et_go = (EditText) findViewById(R.id.go_et_debug);
		submitBtn = (Button) findViewById(R.id.submit_btn_debug);
		


		
		findViewById(R.id.back_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		changTo118Btn.setOnClickListener(this);
		changTo24Btn.setOnClickListener(this);
        btn_shang.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

		pythonTv.setText(SpUtil.getInstance().getPYTHONTP());
		goTv.setText(SpUtil.getInstance().getGOTP());
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.submit_btn_debug) {
			String pyStr = et_python.getText().toString().trim();
			String goStr = et_go.getText().toString().trim();
			SpUtil.getInstance().setPYTHONTP(pyStr);
			SpUtil.getInstance().setGOTP(goStr);
		} else {
			TextView tv = (TextView) view;
			String GOIPStr = tv.getText().toString();
			ToastUtils.show("已切换至" + GOIPStr + "环境");
			goTv.setText(GOIPStr);
			SpUtil.getInstance().setGOTP(GOIPStr);
		}
		WLDBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
		AppConfig.loadIP();
		Intent i = new Intent(this, PhoneLoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}
}

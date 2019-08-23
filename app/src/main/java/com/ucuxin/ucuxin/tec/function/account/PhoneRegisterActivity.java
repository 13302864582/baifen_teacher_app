package com.ucuxin.ucuxin.tec.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.MD5Util;
import com.ucuxin.ucuxin.tec.utils.PhoneUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneRegisterActivity extends BaseActivity implements OnClickListener {

	public static final String DO_TAG = "do_tag";
	public static final int DO_REGISTER = 1;
	public static final int DO_BIND = 2;
	public static final int DO_RESET = 3;

	private EditText num_et;
	private EditText psw_et;
	private EditText verCode_et;
	private Button get_ver;
	private Button submit;
	private ImageView deletePhoneIV;
	private int toDo = DO_REGISTER;

	private String num;
	private String psw;

	private ImageView iv_show_pwd;
	private boolean isHidden = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_phone_register);

		findViewById(R.id.back_layout).setOnClickListener(this);

		num_et = (EditText) findViewById(R.id.phone_num_et_phone_register);
		psw_et = (EditText) findViewById(R.id.phone_psw_et_phone_register);
		verCode_et = (EditText) findViewById(R.id.ver_code_et_phone_register);
		get_ver = (Button) findViewById(R.id.get_verificationcode_btn_phone_register);
		submit = (Button) findViewById(R.id.submit_reg_btn_phone_register);
		deletePhoneIV = (ImageView) findViewById(R.id.delete_phone_iv);
		get_ver.setOnClickListener(this);
		submit.setOnClickListener(this);
		deletePhoneIV.setOnClickListener(this);
		iv_show_pwd = (ImageView) this.findViewById(R.id.iv_show_pwd);
		iv_show_pwd.setOnClickListener(this);

		num_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					deletePhoneIV.setVisibility(View.VISIBLE);
				} else {
					deletePhoneIV.setVisibility(View.GONE);
				}
			}
		});

		Intent i = getIntent();
		if (null != i) {
			toDo = i.getIntExtra(DO_TAG, DO_REGISTER);
		}

		switch (toDo) {
		case DO_REGISTER:
			setWelearnTitle(R.string.text_regist_by_phone);
			submit.setText(R.string.text_regist_confirm);
			break;
		case DO_RESET:
			setWelearnTitle(R.string.text_reset_title);
			submit.setText(R.string.text_reset);
			psw_et.setHint(R.string.text_reset_password_hint);
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isShowDialog = false;
		closeDialog();
	}

	@Override
	public void onResume() {
		super.onResume();
		String phoneNum = SharePerfenceUtil.getInstance().getPhoneNum();
		if (!TextUtils.isEmpty(phoneNum) && phoneNum.matches("1[3|5|4|7|8|9|][0-9]{9}")) {
			num_et.setText(phoneNum);
		} else {
			num_et.setText("");
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GlobalContant.LOOPMSG:
				Message msg1 = Message.obtain();
				msg1.what = GlobalContant.LOOPMSG;
				int time = (Integer) msg.obj - 1;
				msg1.obj = time;
				int de = 0;
				if (AppConfig.IS_DEBUG) {
					de = 55;
				}
				if (time >= de) {
					get_ver.setText(time + "");
					mHandler.sendMessageDelayed(msg1, 1000);
				} else {
					get_ver.setText("重新发送");
					get_ver.setClickable(true);
					// get_ver.setBackgroundResource(R.drawable.bg_get_verification_btn_selector);
				}
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.get_verificationcode_btn_phone_register:
			if (System.currentTimeMillis() - clickTime < 500) {
				return;
			}
			clickTime = System.currentTimeMillis();
			num = num_et.getText().toString().trim();
			if (TextUtils.isEmpty(num) || !num.matches("1[3|4|5|7|8|][0-9]{9}")) {
				ToastUtils.show("请输入正确的手机号码");
				return;
			}
			SharePerfenceUtil.getInstance().setPhoneNum(num);

			try {
				JSONObject jobj = new JSONObject();
				jobj.put("tel", num);
				OkHttpHelper.post(this, "guest", "sendsecuritycode", jobj, new HttpListener() {
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						if (code == 0) {
							ToastUtils.show(R.string.text_get_verification_code_success);
						} else {
							if (!TextUtils.isEmpty(errMsg)) {
								ToastUtils.show(errMsg);
							} else {
								ToastUtils.show(R.string.text_get_verification_code_fail);
							}
						}
					}

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

			get_ver.setClickable(false);
			// get_ver.setBackgroundResource(R.drawable.ic_get_verificate_bt_pressed);
			Message msg = Message.obtain();
			msg.what = GlobalContant.LOOPMSG;
			msg.obj = 60;
			get_ver.setText("60s后重新发送");
			mHandler.sendMessageDelayed(msg, 1000);
			break;
		case R.id.submit_reg_btn_phone_register:
			num = num_et.getText().toString().trim();
			psw = psw_et.getText().toString().trim();
			String code = verCode_et.getText().toString().trim();
			if (TextUtils.isEmpty(num) || !num.matches("1[3|5|7|8|][0-9]{9}")) {
				ToastUtils.show("请输入正确的手机号码");
				return;
			}
			SharePerfenceUtil.getInstance().setPhoneNum(num);
			if (TextUtils.isEmpty(psw) || psw.length() < 6) {
				ToastUtils.show("请输入6位以上密码");
				return;
			}
			if (TextUtils.isEmpty(code)) {
				ToastUtils.show("请输入验证码");
				return;
			}
			bindOrRegisterOrReset(num, psw, code);
			break;
		case R.id.delete_phone_iv:
			if (null != num_et) {
				num_et.setText("");
			}
			break;
		case R.id.iv_show_pwd:// 显示密码或者隐藏密码
			if (isHidden) {
				// 设置EditText文本为可见的
				iv_show_pwd.setBackgroundResource(R.drawable.icon_clos_eye);
				psw_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			} else {
				// 设置EditText文本为隐藏的
				iv_show_pwd.setBackgroundResource(R.drawable.icon_open_eye);
				psw_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
			isHidden = !isHidden;
			psw_et.postInvalidate();
			// 切换后将EditText光标置于末尾
			CharSequence charSequence = psw_et.getText();
			if (charSequence instanceof Spannable) {
				Spannable spanText = (Spannable) charSequence;
				Selection.setSelection(spanText, charSequence.length());
			}

			break;
		}
	}

	private void bindOrRegisterOrReset(String tel, String pwd, String code) {
		try {
			String module = "guest";
			String func = "register";
			JSONObject jobj = new JSONObject();
			jobj.put("tel", tel);
			jobj.put("password", MD5Util.getMD5String(pwd));
			jobj.put("code", code);
			if (toDo == DO_REGISTER) {
				jobj.put("roleid", 2);
				jobj.put("solekey", PhoneUtils.getInstance().getDeviceUUID());
			} else if (toDo == DO_RESET) {
				module = "guest";
				func = "resetpassword";
			}
			OkHttpHelper.post(this, module, func, jobj, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						if (toDo == DO_REGISTER) {
							ToastUtils.show(R.string.text_register_success);
						} else if (toDo == DO_RESET) {
							ToastUtils.show(R.string.text_reset_password_success);
						}
						finish();
					} else {
						if (!TextUtils.isEmpty(errMsg)) {
							ToastUtils.show(errMsg);
						} else {
						}
					}
				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

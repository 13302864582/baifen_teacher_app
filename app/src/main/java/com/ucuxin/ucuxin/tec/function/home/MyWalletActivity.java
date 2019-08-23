package com.ucuxin.ucuxin.tec.function.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.home.model.HomepageModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.ControlUtils;
import com.ucuxin.ucuxin.tec.utils.GlideCircleWithBorder;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.EditTextWithDelete;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTixianDialog;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class MyWalletActivity extends BaseActivity {
	private RelativeLayout back_layout;
	private TextView tv_myruzhang;
	private TextView tv_tixianrecord;
	private ImageView iv_avatar;
	private Button btn_apply;
	private HomeApI homeApi = null;
	private EditTextWithDelete et_money;
	private TextView tv_total_xuedian_val;
	private TextView tv_ketixian_val;
	private LinearLayout layout_zerenxin_star;
	private TextView tv_zerenxin_rule_value;
	private HomepageModel homepageModel;
	private float responsibility_index;
	private LinearLayout layout_tixian;

	protected WelearnDialogBuilder mWelearnDialogBuilder;
	private static final int OP_GIVE_UP = 0x1;
	private static final int OP_COMMIT = 0x2;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mywallet_activity);
		initView();
		initListener();
		initData();

	}

	@Override
	public void initView() {
		super.initView();
		back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
		this.iv_avatar = (ImageView) this.findViewById(R.id.iv_avatar);
		this.tv_myruzhang = (TextView) this.findViewById(R.id.tv_myruzhang);
		this.tv_tixianrecord = (TextView) this.findViewById(R.id.tv_tixianrecord);
		this.btn_apply = (Button) this.findViewById(R.id.btn_apply);
		this.et_money = (EditTextWithDelete) this.findViewById(R.id.et_money);
		this.tv_total_xuedian_val = (TextView) this.findViewById(R.id.tv_total_xuedian_val);
		this.tv_ketixian_val = (TextView) this.findViewById(R.id.tv_ketixian_val);
		this.layout_zerenxin_star = (LinearLayout) this.findViewById(R.id.layout_zerenxin_star);
		this.tv_zerenxin_rule_value = (TextView) this.findViewById(R.id.tv_zerenxin_rule_value);
		layout_tixian = (LinearLayout) this.findViewById(R.id.layout_tixian);

		Intent intent_tixian = getIntent();
		homepageModel = (HomepageModel) intent_tixian.getSerializableExtra("homemodel");

		homeApi = new HomeApI();
		String imageUri = homepageModel.getAvatar();
		RequestOptions options = new RequestOptions();
		options.transform(new GlideCircleWithBorder());
		Glide.with(this).load(!TextUtils.isEmpty(imageUri)?imageUri:R.drawable.default_teacher_avatar)
				.centerCrop()
				.apply(options)
				.placeholder(R.drawable.default_teacher_avatar)
				.error(R.drawable.default_teacher_avatar)
				.into(iv_avatar);

		this.tv_total_xuedian_val.setText(homepageModel.getGold() + "");
		// this.tv_ketixian_val.setText(homepageModel.getAvailable());

		layout_zerenxin_star.removeAllViews();
		responsibility_index = (float) homepageModel.getResponsibility_index();
		for (int i = 0; i < responsibility_index; i++) {
			ImageView iv = new ImageView(MyWalletActivity.this);
			iv.setBackgroundResource(R.drawable.home_heart_icon);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 3, 0);
			layout_zerenxin_star.addView(iv, lp);
		}

	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
		tv_myruzhang.setOnClickListener(this);
		tv_tixianrecord.setOnClickListener(this);
		btn_apply.setOnClickListener(this);

		et_money.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String moneyStr = et_money.getText().toString().trim();
				if (!TextUtils.isEmpty(moneyStr)) {
					float money = Float.parseFloat(moneyStr);
					if (money > homepageModel.getGold()) {
						layout_tixian.setVisibility(View.GONE);
						return;
					}

					if (money < 50) {
						layout_tixian.setVisibility(View.GONE);
					} else {
						if (money % 50 == 0) {
							// 判断星的个数
							if (responsibility_index >= 3.0) {
								layout_tixian.setVisibility(View.VISIBLE);
								float shijimoney = money * (responsibility_index / 5f);
								tv_ketixian_val.setText(money+"*"+(int)responsibility_index +"/5"+ "="+shijimoney);

							} else {
								layout_tixian.setVisibility(View.GONE);
							}

						} else {
							layout_tixian.setVisibility(View.GONE);
						}

					}

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {


			}

			@Override
			public void afterTextChanged(Editable s) {


			}
		});
	}

	private void initData() {
		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("正在加载...");
			Map<String, Object> subParams = new HashMap<String, Object>();
			OkHttpHelper.postOKHttpBaseParams("teacher", "getresponsibilityindex", subParams, new MyStringCallback());
		} else {
			ToastUtils.show("网络无法连接，请查看网络");
		}

	}

	public class MyStringCallback extends StringCallback {
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
			dissmissDialog();
			ToastUtils.show("onError:" + e.getMessage());
		}

		@Override
		public void onResponse(String response) {
			if (!TextUtils.isEmpty(response)) {
				int code = JsonUtils.getInt(response, "Code", -1);
				String msg = JsonUtils.getString(response, "Msg", "");
				dissmissDialog();
				if (code == 0) {
					String dataJson = JsonUtils.getString(response, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						String ruleValue = JsonUtils.getString(dataJson, "rule", "");
						// ruleValue=getIntent().getStringExtra("rule");
						if (!TextUtils.isEmpty(ruleValue)) {
							tv_zerenxin_rule_value.setText(Html.fromHtml(ruleValue));
							// tv_zerenxin_rule_value.setText(Html.fromHtml(""));
						}
					}
				} else {
					ToastUtils.show(msg);
				}
			}
		}

		@Override
		public void inProgress(float progress) {
			// Log.e(TAG, "inProgress:" + progress);
			// mProgressBar.setProgress((int) (100 * progress));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.tv_myruzhang:
			et_money.setText("");
			Intent intent = new Intent(this, MyRuzhangActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_tixianrecord:
			et_money.setText("");
			Intent intent2 = new Intent(this, MyTixianRecordActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_apply:
			applyMoney();
			break;

		}
	}

	/**
	 * 申请提现
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2016年5月12日 下午5:16:21 applyMoney void
	 */
	private void applyMoney() {
		if (ControlUtils.isFastClick()){
			//防止重复点击
			return;
		}
		String moneyStr = et_money.getText().toString().trim();
		if (!TextUtils.isEmpty(moneyStr)) {
			float money = Float.parseFloat(moneyStr);
			if (money > homepageModel.getGold()) {
				showTipDialog(getString(R.string.not_dayu_total_money));
				return;
			}
			if (money < 50) {
				showTipDialog(getString(R.string.diyu50_not_apply));
			} else {
				showDialog("确证要申请提现吗？", OP_COMMIT);
				//applyMoneyOK(money);
			}

		} else {
			showTipDialog(getString(R.string.empty_input_money));
		}

	}
	//提现确认框
	private void showDialog(String msg, final int op, final Object... params) {
		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(this);
		}
		mWelearnDialogBuilder.withMessage(msg).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mWelearnDialogBuilder.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

				String moneyStr = et_money.getText().toString().trim();
				float money = Float.parseFloat(moneyStr);

				switch (op) {
					case OP_COMMIT:
						applyMoneyOK(money);
						break;
				}
			}
		});
		mWelearnDialogBuilder.show();
	}

	private void applyMoneyOK(float money) {
		if (money % 50 == 0) {
			// 判断星的个数
			if (responsibility_index >= 3.0) {
				float shijimoney = money * (responsibility_index / 5f);
				this.tv_ketixian_val.setText(shijimoney + "");

				if (NetworkUtils.getInstance().isInternetConnected(MyWalletActivity.this)) {
					showDialog("加载中...");
					homeApi.applyencashment(requestQueue, shijimoney, money, MyWalletActivity.this,
							RequestConstant.APPLY_MONEY_CODE);
				} else {
					ToastUtils.show("网络无连接，请连接网络");
				}

			} else {
				showTipDialog(getString(R.string.leater_zerenxin));
			}

		} else {
			showTipDialog(
					getString(R.string.nessary50_beishu_apply, "<font size=21 color='#ff7200'>50</font>"));

		}
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.APPLY_MONEY_CODE:// 申请提现
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				dissmissDialog();
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						int result_data = JsonUtils.getInt(dataJson, "result", 0);
						String msg_data = JsonUtils.getString(dataJson, "msg", "");
						switch (result_data) {
						case 1:
							// ToastUtils.show("提交成功");
							// break;
						case 2:
							// ToastUtils.show("老师未通过提现审核，请与客服联系");
							// break;
						case 3:
							// ToastUtils.show("申请额度小于规定额度或余额 ");
							// break;
						case 4:
							// ToastUtils.show("申请正在审核，请勿重复提交");
							final CustomTixianDialog dialog = new CustomTixianDialog(MyWalletActivity.this, msg_data);
							dialog.show();
							dialog.setButtonListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();

								}
							});
							break;
						}

					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		case -1:
			closeDialog();
			int flag2 = ((Integer) param[1]).intValue();
			if (flag2 == RequestConstant.APPLY_MONEY_CODE) {

			}
			break;

		}

	}

	public void showTipDialog(String tip) {
		final CustomTixianDialog dialog = new CustomTixianDialog(MyWalletActivity.this, tip);
		dialog.show();
		dialog.setButtonListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

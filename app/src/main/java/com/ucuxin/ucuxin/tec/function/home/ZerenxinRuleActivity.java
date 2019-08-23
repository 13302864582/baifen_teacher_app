package com.ucuxin.ucuxin.tec.function.home;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Request;
/**
 * 责任心指数activity
 * @author sky
 */
public class ZerenxinRuleActivity extends BaseActivity {
	
	private RelativeLayout back_layout;
	private TextView tv_rule_value;
	private String ruleValue;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.zerenxin_rule_activity);
		initView();
		initListener();
		initData();
	}

	@Override
	public void initView() {		
		super.initView();
		back_layout=(RelativeLayout) this.findViewById(R.id.back_layout);
		tv_rule_value=(TextView) this.findViewById(R.id.tv_rule_value);
	}
	
	
	@Override
	public void initListener() {	
		super.initListener();
		back_layout.setOnClickListener(this);
	}
	
	
	private void initData() {
		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("正在加载...");
			Map<String, Object> subParams = new HashMap<String, Object>();			
			OkHttpHelper.postOKHttpBaseParams("teacher","getresponsibilityindex", subParams, new MyStringCallback());
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
						String ruleValue=JsonUtils.getString(dataJson, "rule", "");
//						ruleValue=getIntent().getStringExtra("rule");
						if (!TextUtils.isEmpty(ruleValue)) {
							tv_rule_value.setText(Html.fromHtml(ruleValue));
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
		
		}
	}
	

}

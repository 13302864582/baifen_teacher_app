package com.ucuxin.ucuxin.tec.function.teccourse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CramSchoolDetailsModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

/**
 * 补习班主页详情介绍
 */
public class CramSchoolDetailsActivity extends BaseActivity implements OnClickListener, HttpListener {

	private static final String TAG = CramSchoolDetailsActivity.class.getSimpleName();

	private int orgid;

	private NetworkImageView detail_icon;
	private TextView detail_title;
	private TextView detail_details;
	private TextView detail_switch;
	private TextView detail_address;
	private TextView detail_phonenumber;

	public int avatarSize;
	private boolean switch_state;

	private String orgname;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setWelearnTitle("");
		findViewById(R.id.back_layout).setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			orgid = intent.getIntExtra("orgid", -1);
		}

		requestData();
	}

	public void initView() {
		avatarSize = TecApplication.getContext().getResources().getDimensionPixelSize(R.dimen.cramschool_detail_icon);
		setContentView(R.layout.activity_cramschool_details);
		findViewById(R.id.back_layout).setOnClickListener(this);

		detail_icon = (NetworkImageView) findViewById(R.id.cramschool_detail_icon);
		detail_title = (TextView) findViewById(R.id.cramschool_detail_title);
		detail_details = (TextView) findViewById(R.id.cramschool_detail_details);
		detail_switch = (TextView) findViewById(R.id.cramschool_detail_switch);
		detail_address = (TextView) findViewById(R.id.cramschool_detail_address);
		detail_phonenumber = (TextView) findViewById(R.id.cramschool_detail_phonenumber);

		detail_switch.setOnClickListener(this);
	}

	private void refreshView(CramSchoolDetailsModel data) {
		orgname = data.getOrgname();
		setWelearnTitle(orgname);
		ImageLoader.getInstance().loadImage(data.getLogo(), detail_icon, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		detail_title.setText(data.getOrgname());
		detail_details.setText(data.getInfo());
		detail_details.post(new Runnable() {
			
			@Override
			public void run() {
				int lineCount = detail_details.getLineCount();
				if(lineCount >= 4){
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							detail_switch.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		});			
		detail_address.setText(format(R.string.cramschool_detail_address, data.getAddress()));
		detail_phonenumber.setText(format(R.string.cramschool_detail_phonenumber, data.getTel()));

	}

	public static String format(int res, Object... args) {
		String format = TecApplication.getContext().getResources().getString(res);
		return MessageFormat.format(format, args);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;
		case R.id.cramschool_detail_switch: // 全文、收起
			if (!switch_state) {
				// 展开
				detail_switch.setText(getResources().getString(R.string.cramschool_switch_on));
				detail_details.setMaxLines(Integer.MAX_VALUE);
			} else {
				detail_switch.setText(getResources().getString(R.string.cramschool_switch_off));
				detail_details.setMaxLines(4);
			}
			switch_state = !switch_state;
			break;
		}
	}

	private void requestData() {
		/*
		 * "orgid":获取的辅导机构ID
		 */
		JSONObject json = new JSONObject();
		try {
			json.put("orgid", orgid);
		} catch (JSONException e1) {
			LogUtils.e(TAG, "Json： ", e1);
		}
		OkHttpHelper.post(this, "org","getorginfo",json,this);
	}

	

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			CramSchoolDetailsModel data = null;
			try {
				data = new Gson().fromJson(dataJson, new TypeToken<CramSchoolDetailsModel>() {
				}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			if (data != null) {
				initView();
				refreshView(data);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

		
	}
	
	
}

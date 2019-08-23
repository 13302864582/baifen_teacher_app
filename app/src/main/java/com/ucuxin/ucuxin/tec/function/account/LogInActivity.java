package com.ucuxin.ucuxin.tec.function.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.GuidePageAdapter;
import com.ucuxin.ucuxin.tec.adapter.GuidePageAdapter.OnViewClickListener;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

import com.ucuxin.ucuxin.tec.WebViewActivity;



import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

public class LogInActivity extends BaseActivity implements OnViewClickListener{

	private ViewPager vp;
	private GuidePageAdapter vpAdapter;
	private List<View> views;

	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_guide);

		vp = (ViewPager) findViewById(R.id.guide_viewpager);
		views = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		View page1 = inflater.inflate(R.layout.view_login_guide_layout, null);
		page1.setBackgroundResource(R.drawable.login_guide_1);
		View page2 = inflater.inflate(R.layout.view_login_guide_layout, null);
		page2.setBackgroundResource(R.drawable.login_guide_2);
		View page3 = inflater.inflate(R.layout.view_login_guide_layout, null);
		page3.setBackgroundResource(R.drawable.login_guide_3);
		View page4 = inflater.inflate(R.layout.view_login_guide_layout, null);
		page4.setBackgroundResource(R.drawable.login_guide_4);
		views.add(page1);
		views.add(page2);
		views.add(page3);
		views.add(page4);

		vpAdapter = new GuidePageAdapter(this, views, GuidePageAdapter.GUIDE_TYPE_LOGIN, this);
		vp.setAdapter(vpAdapter);
		vp.setOffscreenPageLimit(2);

		boolean isShow = SharePerfenceUtil.getInstance().isShowLoginGuide();
		if (isShow) {
			SharePerfenceUtil.getInstance().setShowLoginGuideFalse();
		} else {
			vp.setCurrentItem(views.size() - 1, false);
		}
	}
	
	@Override
	public void initView() {

	    super.initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GlobalVariable.loginActivity = null;
	}

	@Override
	public void onSubViewClick(View v) {
		if (System.currentTimeMillis() - clickTime <= 500) {
			return;
		}
		clickTime = System.currentTimeMillis();
		switch (v.getId()) {
		case R.id.phone_loginorreg_bt:
			GlobalVariable.loginActivity = this;
			IntentManager.goToPhoneLoginActivity(this, null, false);
			break;
//		case R.id.phone_userreg_bt:
//			String baseUrl = "http://zy.ucuxin.com/user-reg.html";
//			Intent intent = new Intent(this, WebViewActivity.class);
//			intent.putExtra("title", "有笔作业老师注册");
//			intent.putExtra("url", baseUrl);
//			startActivity(intent);
//			break;
		}
	}
}

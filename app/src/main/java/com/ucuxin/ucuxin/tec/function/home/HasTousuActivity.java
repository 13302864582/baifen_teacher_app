package com.ucuxin.ucuxin.tec.function.home;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.home.adapter.HasTousuAdapter;
import com.ucuxin.ucuxin.tec.function.home.model.HomeworkListModel;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 投诉Activity
 * 
 * @author Administrator
 *
 */
public class HasTousuActivity extends BaseActivity implements IXListViewListener, OnCheckedChangeListener {
	private XListView xListView;
	private HasTousuAdapter hasTousuAdapter;
	private HasTousuAdapter hasZhongcaiAdapter;
	private RadioGroup radioGroup;
	private RadioButton radio_question;
	private RadioButton radio_homework;
	private TextView tv2;
	private LinearLayout ll_kongbai;
	private RelativeLayout rl_tishi;
	private TextView tv_tishi;
	private ImageView iv_tishi;
	private HomeApI homeApi;
	private int pageIndex = 0;
	private int pageSize = 10;
	private List<HomeworkListModel> list = new ArrayList<HomeworkListModel>();
	private String tousutishitime;
	private String zhongcaitishitime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complaints_activity);
		initView();
		initListener();
		initData();
	}

	public void initView() {
		setWelearnTitle("作业投诉");
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radio_question = (RadioButton) findViewById(R.id.radio_question);
		radio_homework = (RadioButton) findViewById(R.id.radio_homework);
		xListView = (XListView) findViewById(R.id.answer_list);
		rl_tishi= (RelativeLayout) findViewById(R.id.rl_tishi);
		tv_tishi = (TextView) findViewById(R.id.tv_tishi);
		iv_tishi = (ImageView) findViewById(R.id.iv_tishi);
		ll_kongbai = (LinearLayout) findViewById(R.id.ll_kongbai);
		tv2 = (TextView) findViewById(R.id.tv2);
		homeApi = new HomeApI();
	}

	@Override
	public void initListener() {
		super.initListener();
		findViewById(R.id.back_layout).setOnClickListener(this);
		iv_tishi.setOnClickListener(this);
		xListView.setXListViewListener(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		pageIndex = 0;
		list.clear();
		initData();
	}

	public void initData() {
		Calendar c = Calendar.getInstance();

		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			if (radio_homework.isChecked()) {				
				if (pageIndex == 0) {
					list.clear();
					hasTousuAdapter = new HasTousuAdapter(this, list,"有投诉");
					xListView.setAdapter(hasTousuAdapter );
				}
				radio_homework.setBackgroundResource(R.drawable.bline);
				radio_question.setBackgroundColor(Color.parseColor("#FFFFFF"));
				radio_homework.setTextColor(Color.parseColor("#FF781E"));
				radio_question.setTextColor(Color.BLACK);
				tousutishitime=c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH)+"tousu";
				if(!tousutishitime.equals(SharePerfenceUtil.getInstance().getString("tousutishitime",""))){
					rl_tishi.setVisibility(View.VISIBLE);
					tv_tishi.setText(R.string.tousu_tishi);
				}else{
					rl_tishi.setVisibility(View.GONE);
				}

				homeApi.getTousuList(requestQueue, pageIndex, pageSize, this, RequestConstant.GET_TOUSU_LIST_CODE);
			} else {				
				if (pageIndex == 0) {
					list.clear();
					hasZhongcaiAdapter = new HasTousuAdapter(this, list,"仲裁中");
					xListView.setAdapter(hasZhongcaiAdapter );
				}
				radio_question.setBackgroundResource(R.drawable.bline);
				radio_homework.setBackgroundColor(Color.parseColor("#FFFFFF"));
				radio_question.setTextColor(Color.parseColor("#FF781E"));
				radio_homework.setTextColor(Color.BLACK);
				zhongcaitishitime=c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH)+"zhongcai";
				if(!zhongcaitishitime.equals(SharePerfenceUtil.getInstance().getString("zhongcaitishitime",""))){
					rl_tishi.setVisibility(View.VISIBLE);
					tv_tishi.setText(R.string.zhongcai_tishi);
				}else{
					rl_tishi.setVisibility(View.GONE);
				}

				homeApi.getZhongcaiList(requestQueue, pageIndex, pageSize, this, RequestConstant.GET_ZHONGCAI_LIST_CODE);
			}
		} else {
			ToastUtils.show("网络无法连接，请检查网络");
		}
		
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			finish();
			break;
			case R.id.iv_tishi:
				rl_tishi.setVisibility(View.GONE);
				if (radio_homework.isChecked()) {
					SharePerfenceUtil.getInstance().putString("tousutishitime",tousutishitime);
				}else{
					SharePerfenceUtil.getInstance().putString("zhongcaitishitime",zhongcaitishitime);
				}
			break;

		}
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		onLoadFinish();
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_TOUSU_LIST_CODE://投诉列表
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<HomeworkListModel> parseArray = JSON.parseArray(dataJson, HomeworkListModel.class);
						if (parseArray.size() < 10) {
							xListView.setPullLoadEnable(false);
						} else {
							xListView.setPullLoadEnable(true);
						}
						list.addAll(parseArray);
						if (list.size() > 0) {
							hasTousuAdapter.notifyDataSetChanged();
							ll_kongbai.setVisibility(View.GONE);
						} else {
							ll_kongbai.setVisibility(View.VISIBLE);
							tv2.setText("暂时还没有投诉的作业");
						}

					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		case RequestConstant.GET_ZHONGCAI_LIST_CODE://仲裁列表
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<HomeworkListModel> parseArray = JSON.parseArray(dataJson, HomeworkListModel.class);
						if (parseArray.size() < 10) {
							xListView.setPullLoadEnable(false);
						} else {
							xListView.setPullLoadEnable(true);
						}
						list.addAll(parseArray);
						if (list.size() > 0) {
							hasZhongcaiAdapter.notifyDataSetChanged();
							ll_kongbai.setVisibility(View.GONE);
						} else {
							ll_kongbai.setVisibility(View.VISIBLE);
							tv2.setText("暂时还没有仲裁的作业");
						}

					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		}

	}

	@Override
	public void onRefresh() {
		pageIndex = 0;
		initData();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		initData();
	}

	public void onLoadFinish() {
		// isRefresh = true;
		xListView.stopRefresh();
		xListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		xListView.setRefreshTime(time);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}

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
import com.ucuxin.ucuxin.tec.function.home.adapter.DaicainaAdapter;
import com.ucuxin.ucuxin.tec.function.home.model.HomeworkListModel;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
 * 待采纳Activity
 * 
 * @author sky
 *
 */
public class DaicainaActivity extends BaseActivity implements IXListViewListener, OnCheckedChangeListener {
	private XListView xListView;
	private RadioGroup radioGroup;
	private DaicainaAdapter daicainaAdapter;
	private RadioButton radio_question;
	private RadioButton radio_homework;
	private TextView tv2;
	private LinearLayout ll_kongbai;
	private HomeApI homeApi;
	private RelativeLayout rl_tishi;
	private TextView tv_tishi;
	private ImageView iv_tishi;
	private int pageIndex = 0;
	private int pageSize = 10;
	private String daicainatishitime;
	private List<HomeworkListModel> list = null;
	
	private static Handler mHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daicaina_activity);
		initView();
		initListener();
		Calendar c = Calendar.getInstance();
		daicainatishitime=c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH)+"daicaina";
		if(!daicainatishitime.equals(SharePerfenceUtil.getInstance().getString("daicainatishitime",""))){
			rl_tishi.setVisibility(View.VISIBLE);
			tv_tishi.setText(R.string.daicaina_tishi);
		}else{
			rl_tishi.setVisibility(View.GONE);
		}
		initData();

	}

	public void initView() {
		setWelearnTitle("待采纳");
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radio_question = (RadioButton) findViewById(R.id.radio_question);
		radio_homework = (RadioButton) findViewById(R.id.radio_homework);
		xListView = (XListView) findViewById(R.id.answer_list);
		ll_kongbai = (LinearLayout) findViewById(R.id.ll_kongbai);
		rl_tishi= (RelativeLayout) findViewById(R.id.rl_tishi);
		tv_tishi = (TextView) findViewById(R.id.tv_tishi);
		iv_tishi = (ImageView) findViewById(R.id.iv_tishi);
		tv2 = (TextView) findViewById(R.id.tv2);
		homeApi = new HomeApI();
		list = new ArrayList<HomeworkListModel>();
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
		if (radio_homework.isChecked()) {
			tv2.setText("没有待采纳的作业");
			if (pageIndex == 0) {
				list.clear();
				daicainaAdapter = new DaicainaAdapter(this, list);
				xListView.setAdapter(daicainaAdapter);
			}
			radio_homework.setBackgroundResource(R.drawable.bline);
			radio_question.setBackgroundColor(Color.parseColor("#FFFFFF"));
			radio_homework.setTextColor(Color.parseColor("#FF781E"));
			radio_question.setTextColor(Color.BLACK);
			homeApi.daicainaHomework(requestQueue, pageIndex, pageSize, this,
					RequestConstant.GET_CAICAINA_HOMEWORK_CODE);
		} else {
			tv2.setText("没有待采纳的难题");
			if (pageIndex == 0) {
				list.clear();
				daicainaAdapter = new DaicainaAdapter(this, list);
				xListView.setAdapter(daicainaAdapter);
			}
			radio_question.setBackgroundResource(R.drawable.bline);
			radio_homework.setBackgroundColor(Color.parseColor("#FFFFFF"));
			radio_question.setTextColor(Color.parseColor("#FF781E"));
			radio_homework.setTextColor(Color.BLACK);
			homeApi.daicainaQuestion(requestQueue, pageIndex, pageSize, this,
					RequestConstant.GET_CAICAINA_QUESTION_CODE);
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

				SharePerfenceUtil.getInstance().putString("daicainatishitime",daicainatishitime);

				break;
		}
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		onLoadFinish();
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_CAICAINA_HOMEWORK_CODE:
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
							updateListUI();
							ll_kongbai.setVisibility(View.GONE);
						} else {
							ll_kongbai.setVisibility(View.VISIBLE);

						}

					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		case RequestConstant.GET_CAICAINA_QUESTION_CODE:
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
							updateListUI();
							ll_kongbai.setVisibility(View.GONE);
						} else {
							ll_kongbai.setVisibility(View.VISIBLE);

						}
					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;
		}

	}

	public void updateListUI() {
		if (radio_homework.isChecked()) {
			daicainaAdapter.notifyDataSetChanged();
		} else {
			daicainaAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				pageIndex = 0;
				initData();
				
			}
		}, 2000);
		
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed( new Runnable() {
			public void run() {
				pageIndex++;
				initData();
			}
		},2000);
		
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
		mHandler.removeCallbacksAndMessages(null);
	}

}

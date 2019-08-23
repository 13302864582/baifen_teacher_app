package com.ucuxin.ucuxin.tec.function.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.home.model.TixianModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyRuzhangActivity extends BaseActivity implements IXListViewListener {
	private RelativeLayout back_layout;
	private XListView mListView;
	private MyRuzhangAdapter mAdapter;

	private List<TixianModel> mData;

	private int pageIndex = 0;

	private int pageSize = 10;

	private HomeApI homeApi;

	private TextView tv_empty_data;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.myruzhang_activity);
		initView();
		initListener();

	}

	@Override
	public void initView() {
		super.initView();
		back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
		back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
		this.mListView = (XListView) this.findViewById(R.id.listview);
		this.tv_empty_data = (TextView) this.findViewById(R.id.tv_empty_data);

		setWelearnTitle("我的入账");
		mData = new ArrayList<TixianModel>();
		homeApi = new HomeApI();

		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("加载中...");
			homeApi.myIncomerecordlist(requestQueue, pageIndex, pageSize, this, RequestConstant.MY_INCOMME_CODE);
		} else {
			ToastUtils.show("没有网络连接，请检查网络连接");
		}

	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setOnItemClickListener(new MyOnItemClickListener());

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

	class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TixianModel item = mData.get(position - 1);
			if (item != null) {
				int tasktype = item.getTasktype();
				switch (tasktype) {
				case 1://难题答疑
					Bundle data = new Bundle();
					data.putInt("position", 0);
					data.putLong("question_id", item.getTaskid());
					data.putBoolean("iaqpad", true);					
					IntentManager.goToAnswerDetail(MyRuzhangActivity.this, data, 1002);
					
					break;
				case 2://作业检查
					clickIntoHomeWork(item.getTaskid());
					break;
				}

			}

		}

	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		onLoadFinish();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		onLoadFinish();
	}

	public void onLoadFinish() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		mListView.setRefreshTime(time);
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.MY_INCOMME_CODE:// 我的入账记录
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				closeDialog();
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<TixianModel> ruzhangList = JSON.parseArray(dataJson, TixianModel.class);
						if (ruzhangList.size() < 10) {
							mListView.setPullLoadEnable(false);
						}
						mData.addAll(ruzhangList);
						mAdapter = new MyRuzhangAdapter();
						mListView.setAdapter(mAdapter);

					}

					if (mData.size() == 0) {
						tv_empty_data.setVisibility(View.VISIBLE);
						mListView.setEmptyView(tv_empty_data);
					} else {
						tv_empty_data.setVisibility(View.GONE);
					}
				} else {
					ToastUtils.show(msg);
				}

			}

			break;

		}

	}
	
	
	private void clickIntoHomeWork(int taskid) {
		showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", taskid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "homework","getone", data, new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				closeDialogHelp();
				try {
					if (code == 0) {
						HomeWorkModel mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
						Bundle data = new Bundle();
						data.putInt("position", 0);
						data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
						// sky add
						SpUtil.getInstance().setCheckTag("checked_hw_tag");
						IntentManager.goToHomeWorkCheckDetailActivity(MyRuzhangActivity.this, data, false);

					}else {
						ToastUtils.show(errMsg);
					}
				} catch (Exception e) {					
					e.printStackTrace();
					ToastUtils.show(e.getMessage());
				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				closeDialogHelp();

			}

		});

	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public class MyRuzhangAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyViewHolder myViewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(MyRuzhangActivity.this, R.layout.ruzhang_item, null);
				myViewHolder = new MyViewHolder(convertView);
				convertView.setTag(myViewHolder);
			} else {
				myViewHolder = (MyViewHolder) convertView.getTag();
			}
			TixianModel tixianModel = (TixianModel) getItem(position);
			myViewHolder.tv_data.setText(tixianModel.getTradetime());
			myViewHolder.tv_laiyuan.setText(tixianModel.getSummary());
			myViewHolder.tv_money.setText(tixianModel.getIncome() + "");
			return convertView;
		}

		public class MyViewHolder {
			private TextView tv_data;
			private TextView tv_laiyuan;
			private TextView tv_money;

			public MyViewHolder(View convertView) {
				super();
				this.tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				this.tv_laiyuan = (TextView) convertView.findViewById(R.id.tv_laiyuan);
				this.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			}

		}

	}

}

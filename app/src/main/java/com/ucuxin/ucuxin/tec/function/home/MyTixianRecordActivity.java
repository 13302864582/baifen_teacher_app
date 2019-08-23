package com.ucuxin.ucuxin.tec.function.home;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.home.model.TixianRecordModel;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyTixianRecordActivity extends BaseActivity implements IXListViewListener {
	private RelativeLayout back_layout;
	private XListView mListView;
	private MyTixianAdapter mAdapter;

	private List<TixianRecordModel> mData;

	private int pageIndex = 0;

	private int pageSize = 10;

	private HomeApI homeApi = null;

	private TextView tv_empty_data;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mytixian_record_activity);
		initView();
		initListener();

	}

	@Override
	public void initView() {
		super.initView();
		back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
		this.mListView = (XListView) this.findViewById(R.id.listview);
		this.tv_empty_data = (TextView) this.findViewById(R.id.tv_empty_data);
		setWelearnTitle("我的提现");
		mData = new ArrayList<TixianRecordModel>();
		homeApi = new HomeApI();
		mAdapter = new MyTixianAdapter();
		mListView.setAdapter(mAdapter);
		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("加载中...");
			homeApi.myCashedrecordlist(requestQueue, pageIndex, pageSize, this, RequestConstant.MY_TIXIAN_CODE);
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

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		pageIndex = 0;
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
		case RequestConstant.MY_TIXIAN_CODE:// 我的提现记录
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				dissmissDialog();
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<TixianRecordModel> sublist = JSON.parseArray(dataJson, TixianRecordModel.class);
						if (sublist.size() < 10) {
							mListView.setPullLoadEnable(false);
						}
						mData.addAll(sublist);
						mAdapter.notifyDataSetChanged();

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public class MyTixianAdapter extends BaseAdapter {

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
				convertView = View.inflate(MyTixianRecordActivity.this, R.layout.tixian_item, null);
				myViewHolder = new MyViewHolder(convertView);
				convertView.setTag(myViewHolder);
			} else {
				myViewHolder = (MyViewHolder) convertView.getTag();
			}
			TixianRecordModel tixianModel = (TixianRecordModel) getItem(position);
			// myViewHolder.tv_data.setText(tixianModel.getTradetime());
			myViewHolder.tv_data.setText(tixianModel.getTradetime().replace(" ", "\n"));

			myViewHolder.tv_money.setText(tixianModel.getSpending() + "");
			switch (tixianModel.getStatus()) {
			case 0:
				myViewHolder.tv_state.setText("待审核");
				break;
			case 1:
				myViewHolder.tv_state.setText("审核中");
				break;
			case 2:
				myViewHolder.tv_state.setText("已完成");
				break;
			}

			return convertView;
		}

		public class MyViewHolder {
			private TextView tv_data;

			private TextView tv_money;
			private TextView tv_state;

			public MyViewHolder(View convertView) {
				super();
				this.tv_data = (TextView) convertView.findViewById(R.id.tv_data);

				this.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				this.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			}

		}

	}

}

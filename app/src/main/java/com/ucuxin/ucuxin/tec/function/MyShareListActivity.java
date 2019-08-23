package com.ucuxin.ucuxin.tec.function;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.ShareAPI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.home.model.MyShareModel;
import com.ucuxin.ucuxin.tec.http.volley.VolleyErrorHelper;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTixianDialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的分享列表
 * 
 * @author sky
 *
 */
public class MyShareListActivity extends BaseActivity {

	private PullToRefreshListView mPullRefreshListView;

	private List<MyShareModel> myShareList;

	private MyShareAdapter mAdapter = null;

	private TextView tv_empty_data = null;

	private ShareAPI shareApi;
	private RelativeLayout back_layout;

	private int pageIndex = 1;
	private int pageSize = 10;

	private final static int REFRESHCOMPLETE = 121;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESHCOMPLETE:
				closeDialog();
				mPullRefreshListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				if (myShareList.size() > 0) {
					tv_empty_data.setVisibility(View.GONE);
					
					if (myShareList.size()<=10) {
						mPullRefreshListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
					}else {
						mPullRefreshListView.setMode(Mode.BOTH);
					}
					
				} else {
					tv_empty_data.setVisibility(View.VISIBLE);
					mPullRefreshListView.setEmptyView(tv_empty_data);
				}

				break;
			
			}

		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mysharelist_activity);
		initView();
		initListener();
		initData();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initView() {
		super.initView();
		 back_layout=(RelativeLayout) this.findViewById(R.id.back_layout);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		tv_empty_data = (TextView) this.findViewById(R.id.tv_empty_data);
		myShareList = new ArrayList<MyShareModel>();
		shareApi = new ShareAPI();
		mAdapter = new MyShareAdapter(this, myShareList);
		// 设置Listview
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setAdapter(mAdapter);

		final String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// 下拉刷新触发的事件
				// 设置刷新标签
				mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
				// 设置下拉标签
				mPullRefreshListView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
				// 设置释放标签
				mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
				// 设置上一次刷新的提示标签
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + label);
				pageIndex = 1;
				myShareList.clear();
				initData();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 上拉加载触发的事件
				mPullRefreshListView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
				mPullRefreshListView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
				mPullRefreshListView.getLoadingLayoutProxy().setReleaseLabel("释放开始加载");
				// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:"
				// + label);
				pageIndex++;
				initData();
			}

		});

	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
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

	public void initData() {
		if (NetworkUtils.getInstance().isInternetConnected(this)) {
			showDialog("加载中...");
			shareApi.myShareList(requestQueue, pageIndex, pageSize, this, RequestConstant.MYSHARE_LIST_CODE);
		} else {
			ToastUtils.show("请检查你的网络连接");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.MYSHARE_LIST_CODE:// 分享列表
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<MyShareModel> subList = JSON.parseArray(dataJson, MyShareModel.class);
						myShareList.addAll(subList);
						mHandler.sendEmptyMessageDelayed(REFRESHCOMPLETE, 2000);

					}
				} else {
					ToastUtils.show(msg);
					mHandler.sendEmptyMessageDelayed(REFRESHCOMPLETE, 2000);
				}

			}

			break;

		case -1:
			closeDialog();
			int flag2 = ((Integer) param[1]).intValue();
			if (flag2 == RequestConstant.GET_HOMEPAGE_INFO_CODE) {
				String errorStr = VolleyErrorHelper.getMessage(param[2], this);
				// ToastUtils.show("超时，请退出重试");
				final CustomTixianDialog dialog = new CustomTixianDialog(MyShareListActivity.this, errorStr);
				dialog.show();
				dialog.setButtonListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
			break;

		}

	}

	/**
	 * 分享adapter
	 * 
	 * @author Sky
	 *
	 */
	class MyShareAdapter extends BaseAdapter {

		private Context context;
		private List<MyShareModel> myShareList;

		public MyShareAdapter(Context context, List<MyShareModel> myShareList) {
			super();
			this.context = context;
			this.myShareList = myShareList;
		}

		@Override
		public int getCount() {
			return myShareList.size();
		}

		@Override
		public Object getItem(int position) {
			return myShareList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(context, R.layout.myshare_item_layout, null);
				viewHolder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
				viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
				viewHolder.tv_award = (TextView) convertView.findViewById(R.id.tv_award);

				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MyShareModel item = myShareList.get(position);
			viewHolder.tv_user.setText(item.getUsertel());
			viewHolder.tv_state.setText(item.getState());
			viewHolder.tv_award.setText(item.getAward() + "");

			return convertView;
		}

		final class ViewHolder {
			TextView tv_user;
			TextView tv_state;
			TextView tv_award;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

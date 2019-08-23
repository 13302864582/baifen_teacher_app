package com.ucuxin.ucuxin.tec.function.home;

import java.util.ArrayList;
import java.util.List;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.home.model.NoticeModel;
import com.ucuxin.ucuxin.tec.utils.DateUtil;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 通知Activity
 * 
 * @author sky
 *
 */
public class NoticeActivity extends BaseActivity implements IXListViewListener {

	private List<NoticeModel> list = null;
	private XListView mXListView;
	private int pageIndex = 0;
	private int pageSize = 10;

	private NoticeAdapter mNoticeAdapter = null;
	private RelativeLayout back_layout;

	private static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.notice_activity);
		initView();
		initListener();
		initData();

	}

	@Override
	public void initView() {
		super.initView();
		this.back_layout = (RelativeLayout) this.findViewById(R.id.back_layout);
		this.mXListView = (XListView) this.findViewById(R.id.xlistview);
		list = new ArrayList<NoticeModel>();

		mNoticeAdapter = new NoticeAdapter(this, list);
		mXListView.setAdapter(mNoticeAdapter);

	}

	@Override
	public void initListener() {
		super.initListener();
		back_layout.setOnClickListener(this);
		mXListView.setXListViewListener(this);
		mXListView.setPullRefreshEnable(true);
		mXListView.setPullLoadEnable(true);
	}

	public void initData() {
		showDialog("正在加载数据...");
		// 从数据库查询通知记录
		List<NoticeModel> sublist = WLDBHelper.getInstance().getWeLearnDB().selectNoticeWithPage(pageIndex, pageSize);
		if (sublist.size() < 10) {
			mXListView.setPullLoadEnable(false);
		}else {
			mXListView.setPullLoadEnable(true);
		}
		list.addAll(sublist);

		closeDialog();
		mNoticeAdapter.notifyDataSetChanged();
		// Collections.sort(list, new Comparator<NoticeModel>() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh：mm：ss");
		//
		// private Date getData(NoticeModel model) {
		// String date = model.getTimestamp() + "";
		// if (TextUtils.isEmpty(date))
		// return null;
		// try {
		// return sdf.parse(date);
		// } catch (ParseException e) {
		// return null;
		// }
		// }
		//
		// @Override
		// public int compare(NoticeModel lhs, NoticeModel rhs) {
		// String date1 = lhs.getTimestamp() + "";
		// String date2 = rhs.getTimestamp() + "";
		// if (date1 == null && date2 == null)
		// return 0;
		// if (date1 == null)
		// return -1;
		// if (date2 == null)
		// return -1;
		// return date1.compareTo(date2);
		// }
		//
		// });

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
	public void resultBack(Object... param) {
		super.resultBack(param);
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			public void run() {
				pageIndex = 0;
				list.clear();
				initData();
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageIndex++;
				initData();
				onLoad();

			}
		}, 2000);

	}

	private void onLoad() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView.setRefreshTime("刚刚");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);

	}

	class NoticeAdapter extends BaseAdapter {
		private Context context;
		private List<NoticeModel> list = null;

		public NoticeAdapter(Context context, List<NoticeModel> list) {
			super();
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyViewHolder myViewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(NoticeActivity.this, R.layout.notice_item, null);
				myViewHolder = new MyViewHolder(convertView);
				convertView.setTag(myViewHolder);

			} else {
				myViewHolder = (MyViewHolder) convertView.getTag();
			}

			NoticeModel item = list.get(position);
			long beforeDate = (position - 1) >= 0 ? list.get(position - 1).getTimestamp() : 0;
			boolean isRn = DateUtil.getDateByTimestamp(item.getTimestamp())
					.equals(DateUtil.getDateByTimestamp(beforeDate));

			if (!isRn) {
				myViewHolder.tv_date.setVisibility(View.VISIBLE);
				myViewHolder.tv_content.setVisibility(View.VISIBLE);
				myViewHolder.tv_date.setText(DateUtil.getDateByTimestamp(item.getTimestamp()));
				myViewHolder.tv_content.setText(item.getContent() + ""+item.getId());
			} else {
				myViewHolder.tv_date.setVisibility(View.GONE);
				myViewHolder.tv_content.setText(list.get(position).getContent());
				
			}

			return convertView;
		}

		class MyViewHolder {
			private TextView tv_date;
			private TextView tv_content;

			public MyViewHolder(View convertView) {
				super();
				tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			}

		}

	}

}

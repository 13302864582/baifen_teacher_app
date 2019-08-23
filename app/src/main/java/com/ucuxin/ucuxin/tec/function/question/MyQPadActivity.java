package com.ucuxin.ucuxin.tec.function.question;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.MyQPadListAdapter;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.question.model.AnswerListItemModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;
import com.ucuxin.ucuxin.tec.view.xlistview.XListViewFooter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyQPadActivity extends BaseActivity
		implements OnClickListener, OnScrollListener, IXListViewListener, HttpListener {

	public static final String KEY_IS_QPAD = "is_qpad";
	private static final int PAGE_COUNT = 5;
	private static final int TYPE_ANSWER = 3;
	private static final int TYPE_OTHER_ASK = 5;
	private static final int TYPE_OTHER_ANSWER = 6;

	private ArrayList<AnswerListItemModel> currentList;
	private XListView mAnswerList;
	private MyQPadListAdapter mAdapter;
	private int pageIndex = 1;
	private boolean isRefresh = true;

	// add by milo 2014.09.01
	private int targetuserid;
	private boolean hasMore = true;

	private int packtype = TYPE_ANSWER;

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_answer_list);

		int roleId = SharePerfenceUtil.getInstance().getUserRoleId();
		if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {
			setWelearnTitle(R.string.text_my_qpad_tea);
		}

		findViewById(R.id.back_layout).setOnClickListener(this);

		Intent intent = getIntent();
		int roleid = intent.getIntExtra("roleid", 0);
		int userid = intent.getIntExtra("userid", 0);
		if (roleid != 0 && userid != 0) {
			switch (roleid) {
			case GlobalContant.ROLE_ID_PARENTS:
			case GlobalContant.ROLE_ID_STUDENT:// 学生 type为别人的问集
				packtype = TYPE_OTHER_ASK;
				break;
			case GlobalContant.ROLE_ID_COLLEAGE:
				packtype = TYPE_OTHER_ANSWER;
				break;
			default:
				break;
			}

			this.targetuserid = userid;
			// mActionBar.setTitle(intent.getStringExtra("title"));
			setWelearnTitle(intent.getStringExtra("title"));
		} else {
			this.targetuserid = SharePerfenceUtil.getInstance().getUserId();
		}

		mAdapter = new MyQPadListAdapter(this);
		if (targetuserid == SharePerfenceUtil.getInstance().getUserId()) {
			mAdapter.setIsadd(true);
		} else {
			mAdapter.setIsadd(false);

		}
		mAnswerList = (XListView) findViewById(R.id.answer_list);

		mAnswerList.setAdapter(mAdapter);
		mAnswerList.setPullLoadEnable(true);
		mAnswerList.setPullRefreshEnable(true);
		mAnswerList.setXListViewListener(this);
		if (currentList == null) {
			currentList = new ArrayList<AnswerListItemModel>();
		}

		loadData();

	}

	private void loadData() {
		showDialog("数据加载中....");
		JSONObject data = new JSONObject();
		try {
			data.put("packtype", packtype);// 0代表广场， 1代表发完悬赏问答之后跳转到广场， 2表示我的问集，
											// 3表示我的答集 , 4代表同级问题
			data.put("pageindex", pageIndex);
			data.put("pagecount", PAGE_COUNT);
			data.put("targetuserid", targetuserid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "question", "getall", data,this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, "MyQPad");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, "MyQPad");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		onLoadFinish();
		if (TextUtils.isEmpty(dataJson)) {
			hasMore = false;
		} else {
			Gson gson = new Gson();
			ArrayList<AnswerListItemModel> answerList = null;
			try {
				answerList = gson.fromJson(dataJson, new TypeToken<ArrayList<AnswerListItemModel>>() {
				}.getType());
			} catch (Exception e) {
			}

			if (isRefresh) {
				currentList.clear();
			}
			if (answerList != null && !answerList.isEmpty()) {
				pageIndex++;
				if (answerList.size() < PAGE_COUNT) {
					hasMore = false;
					mAnswerList.setPullLoadEnable(false);
				}
				currentList.addAll(answerList);
			}
			if (currentList.size() == 0) {
				ToastUtils.show(R.string.text_no_question);
			} else if (currentList.size() < 10) {
				// ToastUtils.show(getString(R.string.text_question_just_have,
				// currentList.size()));
			}
			mAdapter.setData(currentList);
		}

	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

	

	public void onLoadFinish() {
		closeDialog();
		mAnswerList.stopRefresh();
		mAnswerList.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		mAnswerList.setRefreshTime(time);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		mAdapter.setScrolling(true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			mAdapter.setScrolling(false);
		}
	}

	public void scrollToRefresh() {
		if (mAnswerList != null) {
			mAnswerList.showHeaderRefreshing();
			mAnswerList.setSelection(0);
			isRefresh = true;
		}
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		hasMore = true;
		isRefresh = true;
		loadData();
		mAnswerList.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		if (hasMore) {
			loadData();
		} else {
			mAnswerList.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			onLoadFinish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1002) {
				onRefresh();
			}
		}
	}

	
}

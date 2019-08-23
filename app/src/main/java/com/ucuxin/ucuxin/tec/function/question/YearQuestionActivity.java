package com.ucuxin.ucuxin.tec.function.question;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.AnswerListAdapter;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.AnswerListItemGson;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;
import com.ucuxin.ucuxin.tec.view.xlistview.XListViewFooter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class YearQuestionActivity extends BaseActivity implements OnClickListener, IXListViewListener,
		HttpListener, OnScrollListener {

	private static final int PAGE_COUNT = 10;
	private ArrayList<AnswerListItemGson> currentList;
	private XListView mAnswerList;
	private AnswerListAdapter mAdapter;
	private int pageIndex = 1;
	private boolean isRefresh = true;
	// private boolean isQuery = false;
	// private int target_role_id = 0;
	// private int target_user_id = 0;
	private boolean hasMore = true;
	private int subjectGroupId;
	private int chapterGroupId;
	private int knowPointGroupId;
	private int gradeid;
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onLoadFinish();
		}
	};
	private int q_type;

	public static final String KEY_IS_QPAD = "is_qpad";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_answer_list);
		findViewById(R.id.back_layout).setOnClickListener(this);

		// this.target_role_id = WeLearnSpUtil.getInstance().getUserRoleId();
		// this.target_user_id = WeLearnSpUtil.getInstance().getUserId();

		mAdapter = new AnswerListAdapter(this);

		mAnswerList = (XListView) findViewById(R.id.answer_list);

		TextView titleTV = (TextView) findViewById(R.id.title);
		titleTV.setText(R.string.text_excellent_selction);

		RelativeLayout nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_knowledge_point);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepLayout.setOnClickListener(this);

		mAnswerList.setAdapter(mAdapter);
		mAnswerList.setPullLoadEnable(true);
		mAnswerList.setPullRefreshEnable(true);
		mAnswerList.setXListViewListener(this);
		if (currentList == null) {
			currentList = new ArrayList<AnswerListItemGson>();
		}
		loadData();

	}

	private void loadData() {
		JSONObject data = new JSONObject();
		try {
			data.put("q_type", q_type);// #0代表一题多解 1代表收藏
			data.put("page", pageIndex);
			data.put("pagenum", PAGE_COUNT);
			data.put("grade", gradeid);
			data.put("subject", subjectGroupId);
			data.put("chapterid", chapterGroupId);
			data.put("pointid", knowPointGroupId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "question", "library",  data, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, "OneQuestionMoreAnswers");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				if (data != null) {
					int type = data.getIntExtra("type", 0);
					q_type = 0;
					pageIndex = 1;
					isRefresh = true;
					switch (type) {
					case 1:
						String keyword = data.getStringExtra("keyword");
						JSONObject dataJson = new JSONObject();
						try {
							dataJson.put("page", pageIndex);
							dataJson.put("pagenum", PAGE_COUNT);
							dataJson.put("keyword", keyword);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						OkHttpHelper.post(this, "question", "librarysearch", dataJson, this);
						
						break;
					case 2:
						gradeid = data.getIntExtra("gradeid", 0);
						subjectGroupId = data.getIntExtra("subjectGroupId", 0);
						chapterGroupId = data.getIntExtra("chapterGroupId", 0);
						knowPointGroupId = data.getIntExtra("knowPointGroupId", 0);
						
						loadData();
						break;

					default:
						break;
					}
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			MobclickAgent.onEvent(this, UmengEventConstant.CUSTOM_EVENT_FILTER);
			IntentManager.goToTargetFilterActivity(this);
			GlobalVariable.oneQueMoreAnswActivity = this;
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, "OneQuestionMoreAnswers");
	}


	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (TextUtils.isEmpty(dataJson)) {
			hasMore = false;
		} else {
			Gson gson = new Gson();
			ArrayList<AnswerListItemGson> answerList = null;
			try {
				answerList = gson.fromJson(dataJson, new TypeToken<ArrayList<AnswerListItemGson>>() {
				}.getType());
			} catch (Exception e) {
			}
			pageIndex++;
			if (isRefresh) {
				currentList.clear();
			}
			if (answerList != null && !answerList.isEmpty()) {
				if (answerList.size() < PAGE_COUNT) {
					hasMore = false;
					mAnswerList.setPullLoadEnable(false);
				}
				currentList.addAll(answerList);
			}
			if (currentList.size() == 0) {
				ToastUtils.show(R.string.text_no_question);
			} else if (currentList.size() < 10) {
				ToastUtils.show(getString(R.string.text_question_just_have, currentList.size()));
			}
			mAdapter.setData(currentList);
		}
		onLoadFinish();
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		
		
	}

	@SuppressLint("SimpleDateFormat")
	public void onLoadFinish() {
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
		pageIndex = 1;
		loadData();
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



}

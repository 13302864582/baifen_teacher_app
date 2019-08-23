package com.ucuxin.ucuxin.tec.function.homework.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.ResponseCmdDef;
import com.ucuxin.ucuxin.tec.function.homework.adapter.HomeWrokHallAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
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

public class StuHomeWorkHallActivity extends BaseActivity implements OnScrollListener, IXListViewListener,
		OnClickListener {
	public static final String TARGETNAME = "targetname";
	public static final String TARGETID = "targetid";
	public static final String PACKTYPE = "packtype";
	private static final int REQUEST_CODE = 1002;
	private static final String TAG = StuHomeWorkHallActivity.class.getSimpleName();
	private static final int PAGECOUNT = 5;
	private XListView mListView;
	private int pageIndex = 1;
	private boolean isRefresh = true;
	private HomeWrokHallAdapter mHomeWrokHallAdapter;
	private ArrayList<HomeWorkModel> mHomeWorkList;
	private boolean hasMore = true;
	
	private int packtype = 3;
	private int targetid;
	private String targetname;
	
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			onLoadFinish();
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_answer_list);

		mListView = (XListView) findViewById(R.id.answer_list);
		findViewById(R.id.back_layout).setOnClickListener(this);

		mHomeWrokHallAdapter = new HomeWrokHallAdapter(this);
		mListView.setAdapter(mHomeWrokHallAdapter);
		mHomeWorkList = new ArrayList<HomeWorkModel>();
		Intent intent = getIntent();
		if (intent != null) {
			packtype = intent.getIntExtra("packtype", 3);
			if (packtype == 4) {
				targetid = intent.getIntExtra(StuHomeWorkHallActivity.TARGETID, 0);
				targetname = intent.getStringExtra(StuHomeWorkHallActivity.TARGETNAME);
			}
		}
		switch (packtype) {
		case 3:
			setWelearnTitle(R.string.my_checkhomework_title_text);
			uMengEvent("homework_mycheck_tec");
			break;
		case 4:
			uMengEvent("homework_other");
			setWelearnTitle(targetname);
			break;
		default:
			break;
		}
		loadData(packtype);

		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
	}

	/**
	 * 
	 * @param packtype
	 *            # 0, 1, 2, 3 0代表广场， 1代表发完作业之后的跳转广场， 2表示我的作业集， 3表示我的回答集 , 4别人的作业集
	 */
	private void loadData(int packtype) {
		showDialog("数据加载中....");
		JSONObject data = new JSONObject();
		try {
			data.put("packtype", packtype);
			data.put("pageindex", pageIndex);
			data.put("pagecount", PAGECOUNT);
			if (packtype == 4) {
				data.put("userid", targetid);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//handler.sendEmptyMessageDelayed(0, 7000);
		OkHttpHelper.post(this, "homework","getall", data, new HttpListener() {

			@Override
			public void onFail(int code,String errMsg) {
				showNetWorkExceptionToast();
				onLoadFinish();
			}

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				onLoadFinish();
				if (ResponseCmdDef.CODE_RETURN_OK == code) {

					if (null == dataJson) {
						hasMore = false;
					} else {
						ArrayList<HomeWorkModel> loadHomeWorkList = null;
						try {
							loadHomeWorkList = new Gson().fromJson(dataJson, new TypeToken<ArrayList<HomeWorkModel>>() {
							}.getType());
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						if (isRefresh) {
							mHomeWorkList.clear();
						}
						if (loadHomeWorkList != null && !loadHomeWorkList.isEmpty()) {
						    if (loadHomeWorkList.size()<5) {
						        mListView.setPullLoadEnable(false);
                            }
							mHomeWorkList.addAll(loadHomeWorkList);
							pageIndex++;
						}
						if (mHomeWorkList.size() == 0) {
							ToastUtils.show(R.string.text_no_question);
						} else if (mHomeWorkList.size() < 5) {
							//ToastUtils.show(getString(R.string.text_question_just_have, mHomeWorkList.size()));
						}else {
						}
						mHomeWrokHallAdapter.setData(mHomeWorkList, StuHomeWorkHallActivity.this.packtype);
						if (isRefresh) {
							mListView.setSelection(0);
						}
					}
					onLoadFinish();
				} else {
					ToastUtils.show(errMsg);
				}

			}
		});
	}

	// public void setPageIndex(int pageIndex) {
	// this.pageIndex = pageIndex;
	// }
	//
	// public void setIsRefresh(boolean isRefresh) {
	// this.isRefresh = isRefresh;
	// }

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, TAG);
	}

	@SuppressLint("SimpleDateFormat")
	
	public void onLoadFinish() {
		closeDialog();
//		isRefresh = true;
		mListView.stopRefresh();
		mListView.stopLoadMore();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(new Date());
		mListView.setRefreshTime(time);
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		mHomeWrokHallAdapter.setScrolling(true);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			mHomeWrokHallAdapter.setScrolling(false);
		}
	}

	// public void scrollToRefresh() {
	// if (mListView != null) {
	// mListView.showHeaderRefreshing();
	// mListView.setSelection(0);
	// isRefresh = true;
	// }
	// }

	@Override
	public void onRefresh() {
		pageIndex = 1;
		hasMore = true;
		isRefresh = true;
		loadData(packtype);
		mListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
	}

	@Override
	public void onLoadMore() {
		isRefresh = false;
		if (hasMore) {
			loadData(packtype);
		} else {
			mListView.getFooterView().setState(XListViewFooter.STATE_NOMORE, "");
			onLoadFinish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mHomeWrokHallAdapter) {
			mHomeWrokHallAdapter.setData(null, 0);
		}
		if (mHomeWorkList != null) {
			mHomeWorkList.clear();
		}
		mListView.setXListViewListener(null);
		pageIndex = 1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.avatar_iv_hall_item_common:
			Integer userid = (Integer) v.getTag();
			IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_STUDENT);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE) {
				pageIndex = 1;
				hasMore = true;
				isRefresh = true;
				loadData(1);
				mListView.getFooterView().setState(XListViewFooter.STATE_OTHER, "");
			}
		}
	}

	public void showCollectingDialog() {
		showDialog("请稍后");
	}
	
	
}
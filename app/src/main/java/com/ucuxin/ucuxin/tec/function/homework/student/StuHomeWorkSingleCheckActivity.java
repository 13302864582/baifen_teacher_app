package com.ucuxin.ucuxin.tec.function.homework.student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.HomeWorkAPI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.MyOrientationEventListener;
import com.ucuxin.ucuxin.tec.function.MyOrientationEventListener.OnOrientationChangedListener;
import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.model.TishiModel;
import com.ucuxin.ucuxin.tec.function.homework.model.TousuModel;
import com.ucuxin.ucuxin.tec.function.homework.view.AddPointCommonView;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTousuDialog;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StuHomeWorkSingleCheckActivity extends BaseActivity implements OnClickListener,
		OnOrientationChangedListener {

	// private static final String TAG = StuHomeWorkSingleCheckActivity.class.getSimpleName();

	private RelativeLayout divParentLayout;
	private DragImageView mDragImageView;
	private int window_height, window_width;
	
	private AddPointCommonView mAddPointCommonView;

	// private ArrayList<HomeWorkSinglePoint> singlePointList;

	private HomeWorkCheckPointModel mHomeWorkCheckPointModel;

	// private int baseExseqid;
	// private HomeWorkSinglePoint mPointModel;

	protected WelearnDialogBuilder mWelearnDialogBuilder;

	private NetworkImageView mAvatarIv;

	private TextView mNickTv;

	private TextView mNumTv;

	private Button mAskBtn;

	private int checkpointid;
	private int state;
	private int subjectid;
	
	private TextView tv_has_tousu;
	
	private int sub_type = 1;// 纠正
	private HomeWorkAPI homeworkApi;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				loadData();
				break;
			}
		}
	};
	private RelativeLayout nextStepLayout;

	private TextView nextStepTV;

	private int taskid;
	
	private MyOrientationEventListener moraientation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_check_stu_activity);
		setWelearnTitle(R.string.single_homework_title_text);
//		slideClose = false;// 禁止滑动退出
		getrewardandpunish();
		initView();
		
		moraientation = new MyOrientationEventListener(this, this);
	}
	public void getrewardandpunish() {
		if (TecApplication.gradeid != 0 | TecApplication.subjectid != 0) {
			JSONObject data = new JSONObject();
			try {
				data.put("gradeid", TecApplication.gradeid);
				data.put("subjectid", TecApplication.subjectid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			OkHttpHelper.post(this, "teacher", "rewardandpunish", data, new HttpListener() {

				@Override
				public void onFail(int code,String errMsg) {

				}

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						if (!TextUtils.isEmpty(dataJson)) {
							TecApplication.gradeid=0;
							TecApplication.subjectid=0;
							TishiModel tishiModel = JSON.parseObject(dataJson, TishiModel.class);
							if (tishiModel != null) {
								SharePerfenceUtil.getInstance().putString("viewreward", tishiModel.getViewreward());
								SharePerfenceUtil.getInstance().putString("viewpunish", tishiModel.getViewpunish());
								SharePerfenceUtil.getInstance().putString("processreward", tishiModel.getProcessreward());
								SharePerfenceUtil.getInstance().putString("processpunish", tishiModel.getProcesspunish());
								SharePerfenceUtil.getInstance().putString("conclusionreward", tishiModel.getConclusionreward());
								SharePerfenceUtil.getInstance().putString("conclusionpunish", tishiModel.getConclusionpunish());
								SharePerfenceUtil.getInstance().putString("remindtext", tishiModel.getRemindtext());
								SharePerfenceUtil.getInstance().putString("voiceremind", tishiModel.getVoiceremind());
							}

						}
					} else {
						
					}

				}
			});
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		moraientation.enable();
	}
	
	@Override
	public void onPause() {
		mAddPointCommonView.stopVoice();
		super.onPause();
		moraientation.disable();
	}
	
	public void initView() {
		homeworkApi = new HomeWorkAPI();
		divParentLayout = (RelativeLayout) findViewById(R.id.div_parent_layout);
		mAddPointCommonView = (AddPointCommonView) findViewById(R.id.add_point_common_stu_single);
		mDragImageView = (DragImageView) findViewById(R.id.pic_iv_add_point);
		divParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (window_height == 0 || window_width == 0) {
					window_height = divParentLayout.getHeight();
					window_width = divParentLayout.getWidth();
					mDragImageView.setScreenSize(window_width, window_height);
				}
			}
		});

		int avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);
		findViewById(R.id.back_layout).setOnClickListener(this);
		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		
		mAvatarIv = (NetworkImageView) findViewById(R.id.tec_avatar_iv_single);
		mNickTv = (TextView) findViewById(R.id.tec_nick_tv_single);
		mNumTv = (TextView) findViewById(R.id.tec_num_tv_single);
		mAskBtn = (Button) findViewById(R.id.append_btn_single);
		
		tv_has_tousu = (TextView) this.findViewById(R.id.tv_has_tousu);

		mAvatarIv.setOnClickListener(this);
		mAskBtn.setOnClickListener(this);
		// singlePointList = new ArrayList<HomeWorkSinglePoint>();
		Intent intent = getIntent();
		if (intent != null) {
			boolean fromMsg = intent.getBooleanExtra("fromMsg", false);
			if (fromMsg) {
				nextStepTV.setVisibility(View.VISIBLE);
				nextStepTV.setText(R.string.look_homework_text);
				nextStepLayout.setOnClickListener(this);
				
				taskid = intent.getIntExtra("taskid", 0);
				state = intent.getIntExtra("state", 0);
				subjectid = intent.getIntExtra("subjectid", 0);
				
			}
			
			mHomeWorkCheckPointModel = (HomeWorkCheckPointModel) intent
					.getSerializableExtra(HomeWorkCheckPointModel.TAG);
			boolean allowAppendAsk = mHomeWorkCheckPointModel.isAllowAppendAsk();
			if (allowAppendAsk) {
				mAskBtn.setVisibility(View.VISIBLE);
			} else {
				mAskBtn.setVisibility(View.GONE);
			}
			if (mHomeWorkCheckPointModel != null) {
				checkpointid = mHomeWorkCheckPointModel.getId();
				mAddPointCommonView.setCheckPointImg(mHomeWorkCheckPointModel, false,0,0,state,subjectid);
				
				String teacheravatar = mHomeWorkCheckPointModel.getTeacheravatar();
				if (teacheravatar != null) {
					ImageLoader.getInstance().loadImageWithDefaultAvatar(teacheravatar, mAvatarIv, avatarSize,
							avatarSize / 10);
				}
				String teachername = mHomeWorkCheckPointModel.getTeachername();
				if (teachername != null) {
					mNickTv.setText(teachername);
				}
				int homeworkcnt = mHomeWorkCheckPointModel.getTeacherhomeworkcnt();
				mNumTv.setText(getString(R.string.answer_num_text, homeworkcnt+""));
				
				if (mHomeWorkCheckPointModel.getShowcomplainttype() == 1) {
					tv_has_tousu.setVisibility(View.GONE);
					tv_has_tousu.setOnClickListener(this);
				} else {
					tv_has_tousu.setVisibility(View.GONE);
				}
				
				loadData();
				// mHandler.sendMessageDelayed(mHandler.obtainMessage(1), 10);
			}
		}
	}

	private void loadData() {
		JSONObject data = new JSONObject();
		try {
			data.put("checkpointid", checkpointid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "teacher","homeworkexplain", data, new HttpListener() {

			@Override
			public void onFail(int code,String errMsg) {

			}

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (code == 0) {
					ArrayList<HomeWorkSinglePoint> pointList = null;
					try {
						pointList = new Gson().fromJson(dataJson, new TypeToken<ArrayList<HomeWorkSinglePoint>>() {
						}.getType());
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
					if (pointList != null) {
						mHomeWorkCheckPointModel.setExplianlist(pointList);
						mAddPointCommonView.showCheckPoint(pointList);
					}
				}else {
					ToastUtils.show(errMsg);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		Bundle data = new Bundle();
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			data.putInt("taskid", taskid);
			IntentManager.goToStuHomeWorkDetailActivity(this, data, true);
			break;
		case R.id.tec_avatar_iv_single:
			int userid = mHomeWorkCheckPointModel.getGrabuserid();
			IntentManager.gotoPersonalPage(this, userid, GlobalContant.ROLE_ID_COLLEAGE);
			break;
		case R.id.append_btn_single:
	
			data.putSerializable(HomeWorkCheckPointModel.TAG, mHomeWorkCheckPointModel);
			IntentManager.goToTecSingleCheckActivity(StuHomeWorkSingleCheckActivity.this, data, false, 10086);
			break;
		case R.id.tv_has_tousu:// 有投诉
			if (NetworkUtils.getInstance().isInternetConnected(StuHomeWorkSingleCheckActivity.this)) {
				homeworkApi.getcomplaintreasons(requestQueue, checkpointid, this, RequestConstant.GET_TOUSULIYOU_CODE);
			} else {
				ToastUtils.show("网络无连接");
			}

			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 10086) {
				boolean isSubmit = data.getBooleanExtra("isSubmit", false);
				if (isSubmit) {
					ArrayList<HomeWorkSinglePoint> pointList = null;
					try {
						pointList = (ArrayList<HomeWorkSinglePoint>) data.getSerializableExtra(HomeWorkSinglePoint.TAG);
					} catch (Exception e) {
					}
					
					if (pointList != null) {
						mHomeWorkCheckPointModel.getExplianlist().addAll(pointList);
						mAddPointCommonView.showCheckPoint(pointList);
					}
				}
			}
		}
	}

	@Override
	public void onOrientationChanged(int orientation) {
		if (null != mAddPointCommonView) {
			mAddPointCommonView.setOrientation(orientation);
		}
	}
	
	
	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_TOUSULIYOU_CODE:// 获取投诉的理由
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				if (code == 0) {
					String dataJson = JsonUtils.getString(datas, "Data", "");
					if (!TextUtils.isEmpty(dataJson)) {
						List<TousuModel> tousuList = JSON.parseArray(dataJson, TousuModel.class);
						if (tousuList.size() > 0) {
							tv_has_tousu.setVisibility(View.VISIBLE);
						} else {
							tv_has_tousu.setVisibility(View.GONE);
						}
						final CustomTousuDialog tousuDialog = new CustomTousuDialog(StuHomeWorkSingleCheckActivity.this,
								"投诉理由", tousuList, "原题讲解", "重新拍照讲解");
						tousuDialog.show();
						tousuDialog.setonCloseDialog(new OnClickListener() {							
							@Override
							public void onClick(View v) {
								tousuDialog.dismiss();								
							
							}
						});
						tousuDialog.setonLeftButtonListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								//原题讲解
								tousuDialog.dismiss();
								sub_type=1;
							}
						});

						tousuDialog.setonRightButtonListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 重新拍照讲解
								tousuDialog.dismiss();
								Intent intent = new Intent(StuHomeWorkSingleCheckActivity.this,
										TecHomeWorkCheckDetailActivity.class);
								intent.putExtra("chongxinjiangjie", "chongxinjiangjie");
								intent.putExtra("sub_type",2);
								intent.putExtra("checkPointmodel", mHomeWorkCheckPointModel);
								startActivity(intent);
								StuHomeWorkSingleCheckActivity.this.finish();

							}
						});

					}

				} else {
					ToastUtils.show(msg);
				}
			}
			break;

		}

	}
}

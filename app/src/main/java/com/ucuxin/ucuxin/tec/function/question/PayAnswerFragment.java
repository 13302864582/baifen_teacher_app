
package com.ucuxin.ucuxin.tec.function.question;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.controller.QuestionMessageController;
import com.ucuxin.ucuxin.tec.function.home.DaihuidaActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.QuestionModelGson;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.PayAnswerCommonView;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTipDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 问题抢答
 * 
 * @author: sky
 */
public class PayAnswerFragment extends BaseFragment implements OnClickListener,INetWorkListener {

	public static final String GRAB_ITEM_DATA = "grab_item";

	private View view;

	private Button mPayAnswerGrabBtn, mPayAnswerChangeBtn;

	private TextView mPayAnswerReportBtn;

	private PayAnswerCommonView mCommonView;

	private ReportQuestionWindow reportQuestionWindow;

	public boolean isShowRePortPop;

	private long clickChange = 0;

	private int question_id;

	// private static final String TAG =
	// PayAnswerActivity.class.getSimpleName();

	private String grabStr;
	private String msubject;
	private CustomTipDialog tipDialog;

	private QuestionMessageController questionMessageController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_pay_answer, null);
		initView(view);
		initListener();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(getActivity(), "PayAnswer");
		if (questionMessageController == null) {
			questionMessageController = new QuestionMessageController(null, PayAnswerFragment.this);
		}

	}

	@Override
	public void initView(View view) {
		super.initView();
		mPayAnswerGrabBtn = (Button) view.findViewById(R.id.pay_answer_grab_btn);
		mPayAnswerChangeBtn = (Button) view.findViewById(R.id.pay_answer_change_btn);
		mPayAnswerReportBtn = (TextView) view.findViewById(R.id.pay_answer_report_btn);
		mCommonView = (PayAnswerCommonView) view.findViewById(R.id.pay_answer_common);

		// WeLearnApi.getQACount(this);
		// TextView title = (TextView)view.findViewById(R.id.title);
		// title.setText(R.string.text_ask_title);
		checkIsGrab();

	}

	@Override
	public void initListener() {
		view.findViewById(R.id.back_layout).setOnClickListener(this);
		mPayAnswerGrabBtn.setOnClickListener(this);
		mPayAnswerChangeBtn.setOnClickListener(this);
		mPayAnswerReportBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			getActivity().finish();
			break;
		case R.id.pay_answer_change_btn:
			if (System.currentTimeMillis() - clickChange < 500) {
				break;
			}
			MobclickAgent.onEvent(getActivity(), "chengeQuestion");
			clickChange = System.currentTimeMillis();
			// mCommonView.stopAudio();
			changeQuestion();
			break;
		case R.id.pay_answer_grab_btn:
			if(question_id!=0) {
				MobclickAgent.onEvent(getActivity(), "grab");
				grabQuestion();
			}
			break;

		case R.id.pay_answer_report_btn:// 举报
			MobclickAgent.onEvent(getActivity(), "report");
			isShowRePortPop = true;
			reportQuestionWindow = new ReportQuestionWindow(getActivity(), view, this);
			break;
		}
	}

	private void checkIsGrab() {
		JSONObject data = new JSONObject();
		int checktype = 2;
		try {
			data.put("checktype", checktype);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(getActivity(), "common", "check", data, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (!TextUtils.isEmpty(dataJson)) {
					int result = JsonUtils.getInt(dataJson, "result", 0);
					String info = JsonUtils.getString(dataJson, "info", "");
					if (result == 0 || TextUtils.isEmpty(info)) {// 没有在答的题
						changeQuestion();
					} else if (result == 1) {// 有
						TecApplication.gradeid = JsonUtils.getInt(info, "gradeid", 0);
						TecApplication.subjectid = JsonUtils.getInt(info, "subjectid", 0);
						grabTi(info);
					}

				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {

			}
		});
	}

	/**
	 * 换题操作
	 */
	public void changeQuestion() {
		String str = getActivity().getResources().getString(R.string.text_toast_querying_question);
		showDialog(str);
		OkHttpHelper.post(getActivity(), "teacher", "changequestion", null, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				closeDialog();
				QuestionModelGson questionModelGson = null;
				try {
					if (!TextUtils.isEmpty(dataJson)) {
						final int reask_state = JsonUtils.getInt(dataJson, "reask_state", 0);
						if (reask_state == 0) {
						 String question_info = JsonUtils.getString(dataJson, "question_info", "");
						questionModelGson = JSON.parseObject(question_info, QuestionModelGson.class);
						if (questionModelGson != null) {
							TecApplication.gradeid = questionModelGson.getGradeid();
							TecApplication.subjectid = questionModelGson.getSubjectid();
							mPayAnswerGrabBtn.setEnabled(true);
							int newqtn = JsonUtils.getInt(dataJson, "newqtn", 0);
							if (newqtn == 1) {
								questionModelGson.setNewUser(true);
							}
							question_id = questionModelGson.getQuestion_id();
							grabStr = question_info;
							mCommonView.showData(questionModelGson);
						} else {
							mCommonView.showDataNullQuestion();
							question_id = 0;
							mPayAnswerGrabBtn.setEnabled(false);
						}

						} else {

							tipDialog = new CustomTipDialog(getActivity(), "提示", "你有学生追问没有回答，无法继续抢题，请先完成追问", "确定", "取消");
							final Button positiveBtn = tipDialog.getPositiveButton();
							final Button negativeBtn = tipDialog.getNegativeButton();

							tipDialog.setOnNegativeListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
									tipDialog.dismiss();
									Intent intent = new Intent(getActivity(), DaihuidaActivity.class);
									intent.putExtra("reask_state", reask_state);
									startActivity(intent);
								}
							});

							tipDialog.setOnPositiveListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									positiveBtn.setTextColor(Color.parseColor("#28b9b6"));
									tipDialog.dismiss();

								}
							});
							tipDialog.show();

						}
					} else {
						mCommonView.showDataNullQuestion();
						question_id = 0;
						mPayAnswerGrabBtn.setEnabled(false);
					}

				} catch (Exception e) {
					e.printStackTrace();
					mCommonView.showDataNullQuestion();
					question_id = 0;
					mPayAnswerGrabBtn.setEnabled(false);

				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				closeDialog();

			}
		});
	}

	/**
	 * 抢题操作
	 */
	private void grabQuestion() {
		JSONObject data = new JSONObject();
		try {
			data.put("question_id", question_id);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		OkHttpHelper.post(getActivity(), "teacher", "grabquestion", data, new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				final int reask_state = JsonUtils.getInt(dataJson, "reask_state", 0);
				if (reask_state == 0) {
					try {
						JSONObject json = new JSONObject(grabStr);
						String grabtime = JsonUtils.getString(dataJson, "grabtime", "");
						json.put("grabtime", grabtime);
						int limit_time = JsonUtils.getInt(dataJson, "limit_time", 0);
						json.put("limit_time", limit_time);
						long answerid = JsonUtils.getLong(dataJson, "answerid", 0);
						json.put("answerid", answerid);
						String bottomtip = JsonUtils.getString(dataJson, "bottomtip", "");
						json.put("bottomtip", bottomtip);
						TecApplication.gradeid = JsonUtils.getInt(dataJson, "gradeid", 0);
						TecApplication.subjectid = JsonUtils.getInt(dataJson, "subjectid", 0);

						grabStr = json.toString();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					grabTi(grabStr);
				} else {

					tipDialog = new CustomTipDialog(getActivity(), "提示", "你有学生追问没有回答，无法继续抢题，请先完成追问", "确定", "取消");
					final Button positiveBtn = tipDialog.getPositiveButton();
					final Button negativeBtn = tipDialog.getNegativeButton();

					tipDialog.setOnNegativeListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							negativeBtn.setTextColor(Color.parseColor("#28b9b6"));
							tipDialog.dismiss();
							Intent intent = new Intent(getActivity(), DaihuidaActivity.class);
							intent.putExtra("reask_state", reask_state);
							startActivity(intent);
						}
					});

					tipDialog.setOnPositiveListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							positiveBtn.setTextColor(Color.parseColor("#28b9b6"));
							tipDialog.dismiss();

						}
					});
					tipDialog.show();

				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {

			}
		});
	}

	private void grabTi(String jsonStr) {

		Bundle data = new Bundle();
		data.putString(GRAB_ITEM_DATA, jsonStr);
		long answerid = JsonUtils.getLong(jsonStr, "answerid", 0);
		data.putLong("answerid", answerid);
		IntentManager.goToGrabItemView(getActivity(), data, false);
	}

	/**
	 * 举报
	 * 
	 * @param
	 */
	public void report(int reasonid,String reasonTxt,final String tipTxt) {

		if (!NetworkUtils.getInstance().isInternetConnected(getActivity())) {
			ToastUtils.show("请检查网络");
			return;
		}
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", question_id);
			data.put("tasktype", GlobalContant.TASKTYPE_QUESTION);
			data.put("reason", reasonTxt);
			data.put("reasonid",reasonid);
			data.put("alarmtype",1);//举报方位 1换题 2抢题后


		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(getActivity(), "common", "report", data, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (TextUtils.isEmpty(tipTxt)){
					ToastUtils.show("谢谢您的举报，我们将尽快核实");
				}else {
					ToastUtils.show(tipTxt);
				}
				changeQuestion();
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {

			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(getActivity(), "PayAnswer");
	}

	public void executeQuestionBack() {
		if (reportQuestionWindow != null && reportQuestionWindow.isShowing()) {
			reportQuestionWindow.dismiss();
		}

	}

	// @Override
	// public void onBackPressed() {
	// if (reportQuestionWindow != null && reportQuestionWindow.isShowing()) {
	// reportQuestionWindow.dismiss();
	// } else {
	// super.onBackPressed();
	// }
	// }

	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == RESULT_OK) {
	// if (requestCode == 1002) {
	// changeQuestion();
	// }
	// }
	// }

	@Override
	protected void goBack() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (questionMessageController != null) {
			questionMessageController.removeMsgInQueue();
		}
		// if (mCommonView != null) {
		// mCommonView.stopAudio();
		// }
	}

	@Override
	public void onPre() {

		
	}

	@Override
	public void onException() {

		
	}

	@Override
	public void onAfter(String jsonStr, int msgDef) {

		
	}

	@Override
	public void onDisConnect() {

		
	}

}

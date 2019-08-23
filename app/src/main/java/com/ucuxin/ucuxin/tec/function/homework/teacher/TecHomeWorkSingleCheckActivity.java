package com.ucuxin.ucuxin.tec.function.homework.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.HomeWorkAPI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.model.TousuModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadEXPointModel;
import com.ucuxin.ucuxin.tec.function.homework.view.AddPointCommonView;
import com.ucuxin.ucuxin.tec.function.homework.view.ChoiceWrongTypePopWindow;
import com.ucuxin.ucuxin.tec.function.homework.view.VoiceOrTextPoint;
import com.ucuxin.ucuxin.tec.function.teccourse.view.InputExplainView;
import com.ucuxin.ucuxin.tec.function.teccourse.view.InputExplainView.ResultListener;
import com.ucuxin.ucuxin.tec.function.teccourse.view.PromoteDialog;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTousuDialog;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class TecHomeWorkSingleCheckActivity extends BaseActivity
		implements OnClickListener, OnLongClickListener /*,OnUploadListener*/ {

	public static final String KNOWLEDGE_NAME = "knowledgeName";
	private static final int CHOICE_KNOWLEDGE = 10010;
	// private static final String TAG =
	// TecHomeWorkSingleCheckActivity.class.getSimpleName();
	private String func;
	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	private AddPointCommonView mAddPointCommonView;
	// private Button voiceChooseIBtn;
	// private Button textChooseIBtn;
	// private ImageView infoTurnIBtn;
	// private RelativeLayout textInputLayout;
	// private Button inputOKBtn;
	// private EditText textInputET;

	// protected PopupWindow bgPopupWindow;
	// protected PopupWindow popupWindow;
	// protected RecordPopupViewOfHomeWork menuView;
	// protected SharePopupWindowView mSharePopupWindowView;

	private HomeWorkCheckPointModel mHomeWorkCheckPointModel;

	// private LinearLayout helpInfoL;

	// private LinearLayout answerChooseLayout;

	private String mCoordinate;
	private int baseExseqid;
	private HomeWorkSinglePoint mPointModel;
	private UpLoadCheckPointModel submitModel;
	private ArrayList<HomeWorkSinglePoint> singlePointList;
	private static final int OP_GIVE_UP = 0x1;
	private static final int OP_COMMIT = 0x2;
	private static final int OP_REMOVE_POINT = 0x3;

	protected WelearnDialogBuilder mWelearnDialogBuilder;

	// private InputMethodManager imm;
	private int picid;
	private int checkpointid;
	private boolean isFrist;
	private boolean isAllowAddPoint;
	private ChoiceWrongTypePopWindow choiceWrongTypePopWindow;
	private LinearLayout typeBtn;
	private TextView typeTv;
	private ImageView typeIv;
	private String wrongTypeStr = "";
	private LinearLayout knowledgeBtn;
	private TextView knowledgeTv;
	private ImageView knowledgeIv;
	private String kpointStr = "";
	private int gradeid = -1;
	public int subjectid = -1;
	public int state = -1;
	public int mCurrentItem = 0;
	private InputExplainView inputLayout;
	// private int cpseqid;

	private TextView tv_has_tousu;
	private HomeWorkAPI homeworkApi;

	private int complaint_type = 0;// 投诉类型（服务器返回的）必填
	private int showComplainttype = 0;
	private int sub_type = 1;// 纠正
	private String frompaizhao;
	int left = 0;
	int top = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tec_single_check);		
		initView();
		initListener();		
	}



	@Override
	public void initView() {
		super.initView();
		setWelearnTitle(R.string.single_homework_title_text);
		// AndroidBug5497Workaround.assistActivity(this);
		// imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// slideClose = false;// 禁止滑动退出
		
		findViewById(R.id.back_iv).setVisibility(View.VISIBLE);
		TextView backTV = (TextView) findViewById(R.id.back_tv);
		backTV.setVisibility(View.GONE);
		backTV.setText(R.string.go_back_text);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_done);
		nextStepLayout.setOnClickListener(this);

		mAddPointCommonView = (AddPointCommonView) findViewById(R.id.add_point_common_tec_single);
		inputLayout = (InputExplainView) findViewById(R.id.input_container_tec_single);

		inputLayout.setVisibility(View.GONE);
		tv_has_tousu = (TextView) this.findViewById(R.id.tv_has_tousu);		
		initObject();

		getExtraData();
	}

	private void initObject() {
		singlePointList = new ArrayList<HomeWorkSinglePoint>();
		homeworkApi = new HomeWorkAPI();		
	}

	private void getExtraData() {
		Intent intent = getIntent();
		if (intent != null) {
			mHomeWorkCheckPointModel = (HomeWorkCheckPointModel) intent
					.getSerializableExtra(HomeWorkCheckPointModel.TAG);
			complaint_type = mHomeWorkCheckPointModel.getComplainttype();
			showComplainttype = mHomeWorkCheckPointModel.getShowcomplainttype();
			sub_type = intent.getIntExtra("sub_type", 1);
			isFrist = intent.getBooleanExtra("frist", false);
			gradeid = intent.getIntExtra("gradeid", -1);
			subjectid = intent.getIntExtra("subjectid", -1);
			state = intent.getIntExtra("state", -1);
			mCurrentItem = intent.getIntExtra("mCurrentItem", 0);
			frompaizhao = intent.getStringExtra("frompaizhao");
			isAllowAddPoint=intent.getBooleanExtra("isAllowAddPoint", true);
			showView(intent);
		}
	}

	@Override
	public void initListener() {
		super.initListener();
		findViewById(R.id.back_layout).setOnClickListener(this);
		tv_has_tousu.setOnClickListener(this);
	}
	
	


	@Override
	public void onPause() {
		mAddPointCommonView.stopVoice();
		super.onPause();

	}

	/**
	 * 答题奖励的提示信息
	 */


	private void showView(Intent intent) {
		ArrayList<String> viewPagerList = intent.getStringArrayListExtra("viewPagerList");
		inputLayout.setImageList(TecHomeWorkSingleCheckActivity.this, viewPagerList, mCurrentItem);
		if (mHomeWorkCheckPointModel != null) {
			submitModel = new UpLoadCheckPointModel();

			int roleId = SharePerfenceUtil.getInstance().getUserRoleId();

			checkpointid = mHomeWorkCheckPointModel.getId();
			submitModel.setCheckpointid(checkpointid);
			int isright = mHomeWorkCheckPointModel.getIsright();
			submitModel.setIsright(isright);
			submitModel.setCoordinate(mHomeWorkCheckPointModel.getCoordinate());

			if (roleId == GlobalContant.ROLE_ID_STUDENT || roleId == GlobalContant.ROLE_ID_PARENTS) {// 学生追问
			} else if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师端
				boolean allowAdd = false;

				if (isright == GlobalContant.RIGHT_HOMEWORK) {// 对
					allowAdd = false;
					nextStepLayout.setVisibility(View.GONE);
					findViewById(R.id.tips_tec_single).setVisibility(View.GONE);
					left = intent.getIntExtra("left", -1);
					top = intent.getIntExtra("top", -1);

				} else if (isright == GlobalContant.WRONG_HOMEWORK) {
					if (SharePerfenceUtil.getInstance().isShowFirstSingleTips()) {
						SharePerfenceUtil.getInstance().setFirstSingleFalse();
						new PromoteDialog(this).show();
					}
					allowAdd = true;
					findViewById(R.id.choice_container_tec_single).setVisibility(View.GONE);
					typeBtn = (LinearLayout) findViewById(R.id.wrongtype_choice_btn_tec_single);
					typeTv = (TextView) findViewById(R.id.wrongtype_choice_tv_tec_single);
					typeIv = (ImageView) findViewById(R.id.wrongtype_choice_iv_tec_single);
					typeBtn.setOnClickListener(this);

					knowledgeBtn = (LinearLayout) findViewById(R.id.knowledge_choice_btn_tec_single);
					knowledgeTv = (TextView) findViewById(R.id.knowledge_choice_tv_tec_single);
					knowledgeIv = (ImageView) findViewById(R.id.knowledge_choice_iv_tec_single);
					knowledgeBtn.setOnClickListener(this);

					findViewById(R.id.op_layout).setOnLongClickListener(this);
					String wrongtype = mHomeWorkCheckPointModel.getWrongtype();
					if (!TextUtils.isEmpty(wrongtype)) {
						wrongTypeStr = wrongtype;
						switch (wrongTypeStr) {
						case "s":
							typeTv.setText(getString(R.string.s_wrong_type_text));
							typeTv.setTextColor(getResources().getColor(R.color.white));
							typeBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
							typeIv.setImageResource(R.drawable.ic_setting_jump);
							break;
						case "k":
							typeTv.setText(getString(R.string.k_wrong_type_text));
							typeTv.setTextColor(getResources().getColor(R.color.white));
							typeBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
							typeIv.setImageResource(R.drawable.ic_setting_jump);
							kpointStr = mHomeWorkCheckPointModel.getKpoint();
							if (!TextUtils.isEmpty(kpointStr)) {
								setKPointChoiced();
							}
							break;

						default:
							break;
						}
					}

				}
				if (mHomeWorkCheckPointModel.getShowcomplainttype() == 1) {
					if ("frompaizhao".equals(frompaizhao)) {
						tv_has_tousu.setVisibility(View.GONE);
					} else {
						tv_has_tousu.setVisibility(View.VISIBLE);
					}
				} else {
					tv_has_tousu.setVisibility(View.GONE);
				}

				if (mHomeWorkCheckPointModel.getShowcomplainttype() == 1) {
					if ("frompaizhao".equals(frompaizhao)) {
						allowAdd = true;
						nextStepLayout.setVisibility(View.VISIBLE);
					} else {
						allowAdd = false;
						nextStepLayout.setVisibility(View.GONE);
					}

				}
				if(!isAllowAddPoint){
					allowAdd = false;
					nextStepLayout.setVisibility(View.GONE);
				}
				if(mHomeWorkCheckPointModel.getGrabuserid()!=WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo().getUserid()){
					allowAdd = false;
					nextStepLayout.setVisibility(View.GONE);
				}
				mAddPointCommonView.setCheckPointImg(mHomeWorkCheckPointModel, allowAdd, left, top);
				if (isFrist) {// 第一次打点进来
					picid = mHomeWorkCheckPointModel.getPicid();
					int cpseqid = mHomeWorkCheckPointModel.getCpseqid();
					func = "homeworkanswerone";
					submitModel.setPicid(picid);
					submitModel.setCpseqid(cpseqid);
				} else {// 追答
					func = "homeworkreanswer";
					JSONObject data = new JSONObject();
					try {
						data.put("checkpointid", checkpointid);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					OkHttpHelper.post(this, "teacher", "homeworkexplain", data, new HttpListener() {

						@Override
						public void onFail(int code,String errMsg) {

						}

						@Override
						public void onSuccess(int code, String dataJson, String errMsg) {
							if (code == 0) {
								ArrayList<HomeWorkSinglePoint> pointList = null;
								try {
									pointList = new Gson().fromJson(dataJson,
											new TypeToken<ArrayList<HomeWorkSinglePoint>>() {
											}.getType());
								} catch (Exception e) {

								}
								if (null != pointList) {
									baseExseqid = pointList.size();
									mHomeWorkCheckPointModel.setExplianlist(pointList);
									mAddPointCommonView.showCheckPoint(pointList);
								}
							} else {
								ToastUtils.show(errMsg);
							}

						}
					});
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			giveUp();
			break;

		// case R.id.voice_choice_ibtn: // 选择语音
		// if (helpInfoL != null) {
		// helpInfoL.setVisibility(View.GONE);
		// }
		// // answerChooseLayout.setVisibility(View.GONE);
		// // 显示录音Dialog
		// if (mAddPointCommonView.frameDelView != null) {
		// showDialog();
		// } else {
		// ToastUtils.show("请先选择您要打点的位置");
		// }
		// break;
		// case R.id.text_choice_ibtn: // 选择文字
		// if (helpInfoL != null) {
		// helpInfoL.setVisibility(View.GONE);
		// }
		// if (mAddPointCommonView.frameDelView != null) {
		// answerChooseLayout.setVisibility(View.GONE);
		// textInputLayout.setVisibility(View.VISIBLE);
		// textInputET.requestFocus();
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// } else {
		// ToastUtils.show("请先选择您要打点的位置");
		// }
		// break;
		// case R.id.info_turn_ibtn:
		// helpInfoL.setVisibility(helpInfoL.getVisibility() == View.VISIBLE ?
		// View.GONE : View.VISIBLE);
		// break;
		// case R.id.input_sure_btn:
		// String desc = textInputET.getText().toString();
		// if (!TextUtils.isEmpty(desc)) {
		// // 添加一个文字点
		// mPointModel = new HomeWorkSinglePoint();
		// mPointModel.setText(desc);
		// mPointModel.setCoordinate(mCoordinate);
		// mPointModel.setExplaintype(GlobalContant.ANSWER_TEXT);
		// // mPointModel.setExseqid(++mExseqid);
		// mPointModel.setRoleid(WeLearnSpUtil.getInstance().getUserRoleId());
		// singlePointList.add(mPointModel);
		// VoiceOrTextPoint pointView =
		// mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
		// pointView.setTag(mPointModel);
		//
		// pointView.setOnLongClickListener(TecHomeWorkSingleCheckActivity.this);
		//
		// }
		// mAddPointCommonView.removeFrameDelView();
		// textInputLayout.setVisibility(View.GONE);
		// textInputET.setText("");
		// // 隐藏键盘
		// imm.hideSoftInputFromWindow(textInputET.getWindowToken(), 0);
		// break;
		case R.id.next_setp_layout:

			if (submitModel.getIsright() == GlobalContant.RIGHT_HOMEWORK) {
				MediaUtil.getInstance(false).stopVoice(null);
				submitSinglePoint(complaint_type);
			} else {
				showDialog(R.string.teacher_home_work_single_check_confirm, OP_COMMIT);
			}
			break;
		case R.id.wrongtype_choice_btn_tec_single:
			if (choiceWrongTypePopWindow == null) {
				choiceWrongTypePopWindow = new ChoiceWrongTypePopWindow(this, v);
			} else {
				choiceWrongTypePopWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			}
			if (!TextUtils.isEmpty(wrongTypeStr)) {
				choiceWrongTypePopWindow.setWrongType(wrongTypeStr);
			}
			break;
		case R.id.s_type_wrong_btn_choice:
		case R.id.k_type_wrong_btn_choice:
			wrongTypeStr = (String) v.getTag();
			String trim = ((TextView) v).getText().toString().trim();
			typeTv.setTextColor(getResources().getColor(R.color.white));
			typeBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
			typeIv.setImageResource(R.drawable.ic_setting_jump);
			typeTv.setText(trim);
			choiceWrongTypePopWindow.dismiss();
			if (wrongTypeStr.equals("k")) {
				knowledgeBtn.setVisibility(View.VISIBLE);
				gotoChoiceK();
			} else if (wrongTypeStr.equals("s")) {
				knowledgeBtn.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.cancel_btn_wrong_choice:
			choiceWrongTypePopWindow.dismiss();
			break;
		case R.id.knowledge_choice_btn_tec_single:// 知识点
			if (knowledgeBtn.getVisibility() == View.VISIBLE) {
				gotoChoiceK();
			}
			break;
		case R.id.op_layout:
			choiceWrongTypePopWindow.dismiss();
			break;
		case R.id.tv_has_tousu:// 有投诉
			if (NetworkUtils.getInstance().isInternetConnected(TecHomeWorkSingleCheckActivity.this)) {
				homeworkApi.getcomplaintreasons(requestQueue, checkpointid, this, RequestConstant.GET_TOUSULIYOU_CODE);
			} else {
				ToastUtils.show("网络无连接");
			}

			break;
		}
	}

	private void gotoChoiceK() {
		Bundle data = new Bundle();
		if (!TextUtils.isEmpty(kpointStr)) {
			data.putString(KNOWLEDGE_NAME, kpointStr);
		}
		data.putInt("gradeid", gradeid);
		data.putInt("subjectid", subjectid);
		IntentManager.goToChoiceKnowledgeActivity(this, data, false, CHOICE_KNOWLEDGE);
	}

	public interface DialogListener {
		void onConfirmBtnClick(HomeWorkSinglePoint mSinglePoint);

		void onCancelBtnClick();
	}

	// @Override
	public void showAddPointBottomContainer(String coordinate, final int subtype, final int sum) {
		this.mCoordinate = coordinate;
		inputLayout.setResultListener(new ResultListener() {

			@Override
			public void onReturn(int type, String text, String audioPath) {
				mAddPointCommonView.removeFrameDelView();
				mPointModel = new HomeWorkSinglePoint();
				mPointModel.setCoordinate(mCoordinate);
				mPointModel.setExplaintype(type);
				mPointModel.setSubtype(subtype);
				mPointModel.setExseqid(sum);
				mPointModel.setRoleid(SharePerfenceUtil.getInstance().getUserRoleId());
				mPointModel.setSndpath(audioPath);
				mPointModel.setText(text);
				singlePointList.add(mPointModel);
				LogUtils.e("onReturn", "onReturnxxxxxx");
				VoiceOrTextPoint pointView = mAddPointCommonView.addVoiceOrTextPoint(mPointModel);
				pointView.setTag(mPointModel);
				pointView.setOnLongClickListener(TecHomeWorkSingleCheckActivity.this);
			}
		});
		// if (helpInfoL != null) {
		// helpInfoL.setVisibility(View.GONE);
		// }
	}

	@Override
	public void hideAddPointBottomContainer() {
		inputLayout.setVisibility(View.GONE);
	}

	@Override
	public boolean onLongClick(View view) {
		showDialog(R.string.text_dialog_tips_del_carmera_frame, OP_REMOVE_POINT, view);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mAddPointCommonView.dadianPopupWindow != null && mAddPointCommonView.dadianPopupWindow.isShowing()) {
			mAddPointCommonView.dadianPopupWindow.dismiss();
		}
		if (mAddPointCommonView.frameDelView != null || inputLayout.getVisibility() == View.VISIBLE) {
			boolean isRemove = inputLayout.onBackPress();
			if (isRemove) {
				mAddPointCommonView.removeFrameDelView();
			}
		} else if (mWelearnDialogBuilder != null && mWelearnDialogBuilder.isShowing()) {
			mWelearnDialogBuilder.dismiss();
		} else if (choiceWrongTypePopWindow != null && choiceWrongTypePopWindow.isShowing()) {
			choiceWrongTypePopWindow.dismiss();
		} else {
			giveUp();
		}

	}

	private void showDialog(int msgId, final int op, final Object... params) {
		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(this);
		}
		mWelearnDialogBuilder.withMessage(msgId).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mWelearnDialogBuilder.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

				switch (op) {
				case OP_GIVE_UP:
					Intent intent = new Intent();
					intent.putExtra("isSubmit", false);
					setResult(RESULT_OK, intent);
					finish();
					break;
				case OP_COMMIT:
					submitSinglePoint(showComplainttype);
					break;
				case OP_REMOVE_POINT:
					View view = (View) params[0];
					HomeWorkSinglePoint pointModel = (HomeWorkSinglePoint) view.getTag();
					singlePointList.remove(pointModel);
					// mExseqid--;
					// mAsnwerContainer.removeView(childConainer);// 移除该图标
					mAddPointCommonView.removeExPoint(view, pointModel.getCoordinate());
					break;
				}
			}
		});
		mWelearnDialogBuilder.show();
	}

	private void giveUp() {
		if (singlePointList.size() > 0) {
			showDialog(R.string.teacher_home_work_single_check_give_up_info, OP_GIVE_UP);
		} else {
			Intent intent = new Intent();
			intent.putExtra("isSubmit", false);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	// rebegin是否重新开始
	protected void submitSinglePoint(int showComplainttype) {
		if (showComplainttype == 0 || showComplainttype == 2) {// 原来的接口

			// if (System.currentTimeMillis() - clickTime < 500) {
			// return;
			// }
			// clickTime = System.currentTimeMillis();
			int size = singlePointList.size();
			if (size == 0 && submitModel.getIsright() == GlobalContant.WRONG_HOMEWORK) {
				ToastUtils.show("请先添加您的文字/语音描述");
				return;
			}
			Map<String, List<File>> files = new HashMap<String, List<File>>();

			if (isFrist && submitModel.getIsright() == GlobalContant.WRONG_HOMEWORK) {
				// if (TextUtils.isEmpty(wrongTypeStr)) {
				// ToastUtils.show("请先选择错题类型");
				// return;
				// }
				// submitModel.setWrongtype(wrongTypeStr);
				// if (wrongTypeStr.equals("k")) {
				// if (TextUtils.isEmpty(kpointStr)) {
				// ToastUtils.show("请先选择知识点归属");
				// return;
				// }
				// submitModel.setKpoint(kpointStr);
				// }

				submitModel.setWrongtype(wrongTypeStr);
				submitModel.setKpoint(kpointStr);

				List<File> picFileList = new ArrayList<File>();
				picFileList.add(new File(mHomeWorkCheckPointModel.getImgpath()));
				files.put("picfile", picFileList);
			}
			showDialog("请稍后");
			List<File> sndFileList = new ArrayList<File>();
			ArrayList<UpLoadEXPointModel> upLoadExplainList = new ArrayList<UpLoadEXPointModel>();
			submitModel.setExplainlist(upLoadExplainList);
			UpLoadEXPointModel exPointModel;
			for (int i = 0; i < size; i++) {
				HomeWorkSinglePoint singlePoint = singlePointList.get(i);
				String sndpath = singlePoint.getSndpath();
				int explaintype = singlePoint.getExplaintype();
				if (!TextUtils.isEmpty(sndpath) && explaintype == GlobalContant.ANSWER_AUDIO) {
					sndFileList.add(new File(sndpath));
				}

				exPointModel = new UpLoadEXPointModel();
				exPointModel.setCoordinate(singlePoint.getCoordinate());
				exPointModel.setExplaintype(explaintype);
				exPointModel.setSubtype(singlePoint.getSubtype());
				int exseqid = baseExseqid + i + 1;
				exPointModel.setExseqid(exseqid);
				singlePoint.setExseqid(exseqid);
				String text = singlePoint.getText();
				if (text != null) {
					exPointModel.setText(text);
				}
				upLoadExplainList.add(exPointModel);
			}
			files.put("sndfile", sndFileList);

			try {
				JSONObject data = new JSONObject(new Gson().toJson(submitModel));
				// UploadUtil.upload(AppConfig.GO_URL + "homework/" + func,
				// RequestParamUtils.getParam(data), files,
				// TecHomeWorkSingleCheckActivity.this, true, 0);
				UploadUtil2.upload(AppConfig.GO_URL + "teacher/" + func, RequestParamUtils.getMapParam(data), files,
						new MyStringCallback(), true, 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (showComplainttype == 1) {
			// 原题讲解/重新拍照讲解
			int size = singlePointList.size();
			if (size == 0 && submitModel.getIsright() == GlobalContant.WRONG_HOMEWORK) {
				ToastUtils.show("请先添加您的文字/语音描述");
				return;
			}
			Map<String, List<File>> files = new HashMap<String, List<File>>();

			if (isFrist && submitModel.getIsright() == GlobalContant.WRONG_HOMEWORK) {
				// if (TextUtils.isEmpty(wrongTypeStr)) {
				// ToastUtils.show("请先选择错题类型");
				// return;
				// }
				// submitModel.setWrongtype(wrongTypeStr);
				// if (wrongTypeStr.equals("k")) {
				// if (TextUtils.isEmpty(kpointStr)) {
				// ToastUtils.show("请先选择知识点归属");
				// return;
				// }
				// submitModel.setKpoint(kpointStr);
				// }

				submitModel.setWrongtype(wrongTypeStr);
				submitModel.setKpoint(kpointStr);

				List<File> picFileList = new ArrayList<File>();
				picFileList.add(new File(mHomeWorkCheckPointModel.getImgpath()));
				files.put("picfile", picFileList);
			}
			showDialog("请稍后");
			List<File> sndFileList = new ArrayList<File>();
			ArrayList<UpLoadEXPointModel> upLoadExplainList = new ArrayList<UpLoadEXPointModel>();
			submitModel.setExplainlist(upLoadExplainList);
			UpLoadEXPointModel exPointModel;
			for (int i = 0; i < size; i++) {
				HomeWorkSinglePoint singlePoint = singlePointList.get(i);
				String sndpath = singlePoint.getSndpath();
				int explaintype = singlePoint.getExplaintype();
				if (!TextUtils.isEmpty(sndpath) && explaintype == GlobalContant.ANSWER_AUDIO) {
					sndFileList.add(new File(sndpath));
				}

				exPointModel = new UpLoadEXPointModel();
				exPointModel.setCoordinate(singlePoint.getCoordinate());
				exPointModel.setExplaintype(explaintype);
				exPointModel.setSubtype(singlePoint.getSubtype());
				int exseqid = baseExseqid + i + 1;
				exPointModel.setExseqid(exseqid);
				singlePoint.setExseqid(exseqid);
				String text = singlePoint.getText();
				if (text != null) {
					exPointModel.setText(text);
				}
				upLoadExplainList.add(exPointModel);
			}
			files.put("sndfile", sndFileList);
			submitModel.setComplaint_type(complaint_type);
			submitModel.setSub_type(sub_type);

			try {
				JSONObject data = new JSONObject(new Gson().toJson(submitModel));
				func = "revisiohomework";
				// UploadUtil.upload(AppConfig.GO_URL + "teacher/" + func,
				// RequestParamUtils.getParam(data), files,
				// TecHomeWorkSingleCheckActivity.this, true, 0);
				UploadUtil2.upload(AppConfig.GO_URL + "teacher/" + func, RequestParamUtils.getMapParam(data), files,
						new MyStringCallback(), true, 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

	////////////////////upload 回调/////////////////////////////
	public class MyStringCallback extends StringCallback {
		/**
		 * UI Thread
		 */
		@Override
		public void onBefore(Request request) {
			super.onBefore(request);
			// ToastUtils.show("onBefore");
		}

		/**
		 * UI Thread
		 */
		@Override
		public void onAfter() {
			super.onAfter();
			// ToastUtils.show("onAfter");

		}

		@Override
		public void onError(Call call, Exception e) {
			closeDialog();
			String errorMsg = "";
			if (e != null && !TextUtils.isEmpty(e.getMessage())) {
				errorMsg = e.getMessage();
			} else {
				errorMsg = e.getClass().getSimpleName();
			}
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("onError:" + errorMsg);
			} else {
				ToastUtils.show("网络异常");
			}

		}

		@Override
		public void onResponse(String response) {
			// ToastUtils.show("onResponse:" + response);
			closeDialog();
			int code = JsonUtils.getInt(response, "Code", -1);
			String msg = JsonUtils.getString(response, "Msg", "");
			String data = JsonUtils.getString(response, "Data", "");
			if (code == 0) {
				uMengEvent("homework_answer");
				ToastUtils.show("提交成功");
				Intent intent = new Intent();
				intent.putExtra("isSubmit", true);
				intent.putExtra(HomeWorkSinglePoint.TAG, singlePointList);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				ToastUtils.show(msg);
			}

		}

		/**
		 * UI Thread
		 */
		@Override
		public void inProgress(float progress) {
			// Log.e(TAG, "inProgress:" + progress);
			// mProgressBar.setProgress((int) (100 * progress));
		}
	}

	/////////////////////////////////////////////////

//	@Override
//	public void onUploadSuccess(UploadResult result, int index) {
//		closeDialog();
//		if (result.getCode() == 0) {
//			uMengEvent("homework_answer");
//			ToastUtils.show("提交成功");
//			Intent intent = new Intent();
//			intent.putExtra("isSubmit", true);
//			intent.putExtra(HomeWorkSinglePoint.TAG, singlePointList);
//			setResult(RESULT_OK, intent);
//			finish();
//		} else {
//			ToastUtils.show(result.getMsg());
//		}
//
//	}
//
//	@Override
//	public void onUploadError(String msg, int index) {
//		closeDialog();
//		ToastUtils.show(msg);
//	}
//
//	@Override
//	public void onUploadFail(UploadResult result, int index) {
//		closeDialog();
//		String msg = result.getMsg();
//		if (TextUtils.isEmpty(msg)) {
//			ToastUtils.show("提交失败，请稍后重试");
//		} else {
//			ToastUtils.show(msg);
//		}
//
//	}

	private void setKPointChoiced() {
		knowledgeTv.setText(kpointStr);
		knowledgeBtn.setVisibility(View.VISIBLE);
		knowledgeTv.setTextColor(getResources().getColor(R.color.white));
		knowledgeBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
		knowledgeIv.setImageResource(R.drawable.ic_setting_jump);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CHOICE_KNOWLEDGE:
				if (data == null) {
					return;
				}
				kpointStr = data.getStringExtra(KNOWLEDGE_NAME);
				if (!TextUtils.isEmpty(kpointStr)) {
					setKPointChoiced();
				}
				break;

			default:
				break;
			}
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
						final CustomTousuDialog tousuDialog = new CustomTousuDialog(TecHomeWorkSingleCheckActivity.this,
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
								// 原题讲解
								tousuDialog.dismiss();
								sub_type = 1;
								mAddPointCommonView.setCheckPointImg(mHomeWorkCheckPointModel, true, left, top);
								nextStepLayout.setVisibility(View.VISIBLE);
							}
						});

						tousuDialog.setonRightButtonListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 重新拍照讲解
								tousuDialog.dismiss();
								Intent intent = new Intent("com.fireeye.fanhui");
								intent.putExtra("chongxinjiangjie", "chongxinjiangjie");
								intent.putExtra("sub_type", 2);
								intent.putExtra("checkPointmodel", mHomeWorkCheckPointModel);
								sendBroadcast(intent);
								TecHomeWorkSingleCheckActivity.this.finish();

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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (MediaUtil.getInstance(false).audioUtil != null) {
			MediaUtil.getInstance(false).audioUtil = null;
			MediaUtil.getInstance(false).setCurrentMediaPlayer(null);

		}
		GlobalVariable.answertextPopupWindow=null;
		if (TecApplication.anmimationPlayViews != null && TecApplication.anmimationPlayViews.size() > 0) {
			TecApplication.anmimationPlayViews.clear();
		}

	}

}

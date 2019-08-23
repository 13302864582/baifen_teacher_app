package com.ucuxin.ucuxin.tec.function.homework.teacher;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.view.ReportGrabHomeWorkWindow;
import com.ucuxin.ucuxin.tec.function.homework.view.TecHomeWorkCheckCommonView;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 作业检查抢题
 *
 * @author: sky
 */
public class TecHomeWorkCheckGrabItemActivity extends BaseActivity implements OnClickListener {
    private TecHomeWorkCheckCommonView mCommon;
    private HomeWorkModel mHomeWorkModel;
    protected WelearnDialogBuilder mWelearnDialogBuilder;
    private ReportGrabHomeWorkWindow mReportGrabHomeWorkWindow;
    private static final int OP_GIVE_UP = 0x1;
    private static final int OP_COMMIT = 0x2;
    private TextView timesUp_tv;
    private int limittime;
    private long grabtime;
    private int failTime;
    private boolean allowSubmit;//是否允许提交

    private RelativeLayout layout_zhuyi;
    private TextView tv_botton;

    private WeakReference<TecHomeWorkCheckGrabItemActivity> reference;
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TecHomeWorkCheckGrabItemActivity activity = reference.get();
            if (msg.what == GlobalContant.CLOSEDIALOG) {

            } else if (msg.what == GlobalContant.LOOPMSG) {
                activity.failTime -= 1;
                activity.loopCountDown();
            }

        }


    };


//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (msg.what == GlobalContant.CLOSEDIALOG) {
//				
//			} else if (msg.what == GlobalContant.LOOPMSG) {
//				failTime -= 1;
//				loopCountDown();
//			}
//
//		}
//	};

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.tec_homework_check_grab);
        registerFinishActivity();
        setWelearnTitle(R.string.homework_check_title_text);
        reference = new WeakReference<>(this);
        findViewById(R.id.back_layout).setOnClickListener(reference.get());

        RelativeLayout nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
        TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.submit_homework_check_text);
        nextStepLayout.setOnClickListener(reference.get());

        mCommon = (TecHomeWorkCheckCommonView) findViewById(R.id.homework_check_common_grab);
        TextView limitTime_tv = (TextView) findViewById(R.id.time_limit_tv_homework_grab);
        timesUp_tv = (TextView) findViewById(R.id.timesup_tv_homework_grab);
        findViewById(R.id.report_btn_homework_check_grab).setOnClickListener(this);
        findViewById(R.id.goto_this_homework_check_btn).setOnClickListener(this);
        layout_zhuyi = (RelativeLayout) this.findViewById(R.id.layout_zhuyi);
        tv_botton = (TextView) findViewById(R.id.tv_botton);

        mHomeWorkModel = (HomeWorkModel) getIntent().getSerializableExtra(HomeWorkModel.TAG);
        if (mHomeWorkModel != null) {
            mCommon.showData(mHomeWorkModel);
            grabtime = mHomeWorkModel.getGrabtime();
            limittime = mHomeWorkModel.getLimittime();
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show(limittime + "");
            }
            limitTime_tv.setText(limittime + "");
        }

        if ("checked_hw_tag".equals(SpUtil.getInstance().getCheckTag())) {
            SpUtil.getInstance().setCheckTag("none");
        }
        if (!TextUtils.isEmpty(mHomeWorkModel.getBottomtip())) {
            layout_zhuyi.setVisibility(View.VISIBLE);
            tv_botton.setText(mHomeWorkModel.getBottomtip());
        } else {
            layout_zhuyi.setVisibility(View.GONE);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        mHandler.removeMessages(GlobalContant.LOOPMSG);
        long currentTimeMillis = System.currentTimeMillis();
        failTime = limittime - ((int) (currentTimeMillis - grabtime) / 60000);

        loopCountDown();
    }

    private void loopCountDown() {
        if (failTime > 0) {
            if (failTime > limittime) {
                failTime = limittime;
            }
            timesUp_tv.setText(failTime + "");
            Message msg = Message.obtain();
            msg.what = GlobalContant.LOOPMSG;
            mHandler.sendMessageDelayed(msg, 60000);
        } else {
            timesUp_tv.setText("0");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_btn_homework_check_grab:
                mReportGrabHomeWorkWindow = new ReportGrabHomeWorkWindow(v, this);
                break;
            case R.id.goto_this_homework_check_btn://进入作业检查
                Bundle data = new Bundle();
                data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
                IntentManager.goToHomeWorkCheckDetailActivity(this, data, false);
                break;
            case R.id.back_layout:
                showDialog(getString(R.string.teacher_home_work_all_check_give_up_info, 1), OP_GIVE_UP);
                break;
            case R.id.next_setp_layout:
                if (allowSubmit) {
                    showDialog(getString(R.string.teacher_home_work_all_check_confirm), OP_COMMIT);
                } else {
                    ToastUtils.show("请先检查学生的作业");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void report(int reasonid, String reasonTxt,final String tipTxt) {
        if (!NetworkUtils.getInstance().isInternetConnected(TecHomeWorkCheckGrabItemActivity.this)) {
            ToastUtils.show("请检查网络");
            return;
        }

        JSONObject data = new JSONObject();
        try {

            data.put("taskid", mHomeWorkModel.getTaskid());
            data.put("tasktype", GlobalContant.TASKTYPE_HOMEWORK);
            data.put("reason", reasonTxt);
            data.put("reasonid", reasonid);
            data.put("alarmtype", 2);//举报方位 1换题 2抢题后

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(this, "common", "report", data, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                uMengEvent("homework_report");
                if (code == 0) {
                    if (TextUtils.isEmpty(tipTxt)){
                        ToastUtils.show("谢谢您的举报，我们将尽快核实");
                    }else {
                        ToastUtils.show(tipTxt);
                    }
                    finish();
                } else {
                    ToastUtils.show(errMsg);
                }
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 10010) {
                if (data != null) {
                    boolean issubmited = data.getBooleanExtra("issubmited", false);
                    if (issubmited) {
                        finish();
                    } else {
                        allowSubmit = data.getBooleanExtra("allowSubmit", false);
                    }
                }
            }
        }
    }

    private void submitHomeWorkCheck() {
//		showDialog("请稍后");
//		JSONObject data = new JSONObject();
//		try {
//			data.put("taskid", mHomeWorkModel.getTaskid());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		HttpHelper.post(this, "answerfinish", data, new HttpListener() {
//
//			@Override
//			public void onSuccess(int code, String dataJson, String errMsg) {
//				closeDialog();
//				if (code == 0) {
//					uMengEvent("homework_submit");
//					ToastUtils.show("提交成功");
//					finish();
//				}else {
//					ToastUtils.show(errMsg);
//				}
//
//			}
//
//			@Override
//			public void onFail(int HttpCode) {
//				closeDialog();
//			}
//		});


        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("请稍后...");
            Map<String, Object> subParams = new HashMap<String, Object>();
            subParams.put("taskid", mHomeWorkModel.getTaskid());

            OkHttpHelper.postOKHttpBaseParams("teacher", "homeworkanswerfinish", subParams, new StringCallback() {

                @Override
                public void onResponse(String response) {
                    closeDialog();
                    int code = JsonUtils.getInt(response, "Code", -1);
                    String msg = JsonUtils.getString(response, "Msg", "");
                    String data = JsonUtils.getString(response, "Data", "");
                    if (code == 0) {
                        uMengEvent("homework_submit");
                        ToastUtils.show("提交成功");
                        finish();
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                @Override
                public void onError(Call call, Exception e) {
                    closeDialog();
                    if (e != null) {
                        ToastUtils.show("onError:" + e.getMessage());
                    }


                }
            });
        } else {
            ToastUtils.show("网络无法连接，请查看网络");
        }


    }

    protected void giveUpHomeWorkCheck() {
        showDialog("请稍后");
        JSONObject data = new JSONObject();
        try {
            data.put("taskid", mHomeWorkModel.getTaskid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(this, "teacher", "giveuphomework", data, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                if (code == 0) {
                    ToastUtils.show("放弃成功");
                    uMengEvent("homework_giveup");
                    finish();
                } else {
                    ToastUtils.show(errMsg);
                }
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                closeDialog();
            }
        });
    }

    private void showDialog(String msg, final int op, final Object... params) {
        if (null == mWelearnDialogBuilder) {
            mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(reference.get());
        }
        mWelearnDialogBuilder.withMessage(msg).setOkButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mWelearnDialogBuilder.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                switch (op) {
                    case OP_GIVE_UP:
                        reference.get().giveUpHomeWorkCheck();

                        break;
                    case OP_COMMIT:
                        reference.get().submitHomeWorkCheck();
                        break;
                }
            }
        });
        mWelearnDialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        if (mReportGrabHomeWorkWindow != null && mReportGrabHomeWorkWindow.isShowing()) {
            mReportGrabHomeWorkWindow.dismiss();
        } else {
            showDialog(getString(R.string.teacher_home_work_all_check_give_up_info, 1), OP_GIVE_UP);
        }
    }

    /**
     * 注册广播
     *
     * @author: sky void
     */
    public void registerFinishActivity() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ucuxin.ucuxin.opera");
        registerReceiver(mFinishReceiver, filter);
    }


    /**
     * 监听广播
     */
    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        if (mFinishReceiver != null) {
            unregisterReceiver(mFinishReceiver);
            mFinishReceiver = null;

        }
        mHandler.removeCallbacksAndMessages(null);
    }

}

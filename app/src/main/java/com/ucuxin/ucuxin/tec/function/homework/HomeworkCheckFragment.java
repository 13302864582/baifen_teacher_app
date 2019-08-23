
package com.ucuxin.ucuxin.tec.function.homework;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.controller.HomeworkMessageController;
import com.ucuxin.ucuxin.tec.function.home.DaihuidaActivity;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkCheckGrabItemActivity;
import com.ucuxin.ucuxin.tec.function.homework.view.ReportHomeWorkWindow;
import com.ucuxin.ucuxin.tec.function.homework.view.TecHomeWorkCheckCommonView;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LOG;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTipDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作业检查Activity
 *
 * @author: sky
 */
public class HomeworkCheckFragment extends BaseFragment implements OnClickListener, INetWorkListener {
    TecHomeWorkCheckCommonView mCommonView;

    private HomeWorkModel mHomeWorkModel;

    // private String param;
    protected static final String TAG = HomeworkCheckFragment.class.getSimpleName();

    private ReportHomeWorkWindow mReportWindow;

    private Button grabBtn;

    private TextView reportBtn;

    private Button changeBtn;
    private CustomTipDialog tipDialog;

    // private long clickTime = 0L;

    private RelativeLayout back_layout;

    private TextView tv_botton/*, tv_more*/;

    private ImageView iv_bottom_icon;

   /* private TextView[] tvs;*/

    private HomeworkMessageController homeworkMessageController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.homework_check_activity, null);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // changeHomeWork();

        if (homeworkMessageController == null) {
            homeworkMessageController = new HomeworkMessageController(null, HomeworkCheckFragment.this);
        }
    }

    @Override
    public void initView(View view) {
        // setWelearnTitle(R.string.homework_checks_title_text);
        ((BaseActivity) getActivity()).uMengEvent("homework_opencheck");
        this.back_layout = (RelativeLayout) view.findViewById(R.id.back_layout);

        mCommonView = (TecHomeWorkCheckCommonView) view.findViewById(R.id.homework_check_common);

        grabBtn = (Button) view.findViewById(R.id.grab_btn_homework_check);
        reportBtn = (TextView) view.findViewById(R.id.report_btn_homework_check);
        changeBtn = (Button) view.findViewById(R.id.change_btn_homework_check);
        tv_botton = (TextView) view.findViewById(R.id.tv_botton);
        iv_bottom_icon = (ImageView) view.findViewById(R.id.iv_bottom_icon);
        /*tvs = new TextView[4];
        tvs[0] = (TextView) view.findViewById(R.id.tv_0);
        tvs[1] = (TextView) view.findViewById(R.id.tv_1);
        tvs[2] = (TextView) view.findViewById(R.id.tv_2);
        tvs[3] = (TextView) view.findViewById(R.id.tv_3);
        tv_more = (TextView) view.findViewById(R.id.tv_more);*/

        // GlobalVariable.mHomeWorkModel = new HomeWorkModel();
        checkIsGrab();

    }

    @Override
    public void initListener() {
        back_layout.setOnClickListener(this);
        grabBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        //tv_more.setOnClickListener(this);

    }

    private void checkIsGrab() {
        showDialog("请稍后");
        JSONObject data = new JSONObject();
        int checktype = 1;

        try {
            data.put("checktype", checktype);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpHelper.post(getActivity(), "common", "check", data, new HttpListener() {

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                if (code == 0) {
                    int result = JsonUtils.getInt(dataJson, "result", 0);
                    if (result == 0) {// 没有在答的题
                        changeHomeWork();
                    } else if (result == 1) {// 有
                        String info = JsonUtils.getString(dataJson, "info", "");
                        try {
                            mHomeWorkModel = new Gson().fromJson(info, HomeWorkModel.class);
                        } catch (Exception e) {
                        }
                        if (mHomeWorkModel != null) {
                            // mCommonView.showData(mHomeWorkModel);
                            // grabHomeWork();
                            Bundle data = new Bundle();
                            data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
                            IntentManager.goToHomeWorkCheckGrabItemActivity(getActivity(),
                                    TecHomeWorkCheckGrabItemActivity.class, data, false);
                        } else {
                            changeHomeWork();
                        }
                    }
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

    public void changeHomeWork() {
        JSONObject data = new JSONObject();
        int taskid;
        if (mHomeWorkModel == null) {
            taskid = 0;
        } else {
            taskid = mHomeWorkModel.getTaskid();
        }
        try {
            data.put("taskid", taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        grabBtn.setClickable(true);
        reportBtn.setClickable(true);
        changeBtn.setClickable(true);

        showDialog("正在换题", true);

        OkHttpHelper.post(getActivity(), "teacher", "changehomework", data, new HttpListener() {

            @Override
            public void onFail(int code, String errMsg) {
                closeDialog();
                if (!TextUtils.isEmpty(errMsg)) {
                    ToastUtils.show(errMsg);
                }
                // showNetWorkExceptionToast();
            }

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                if (code == 0) {
                    try {
                        if (!TextUtils.isEmpty(dataJson)) {
                            LOG.d("fzljson1:"+dataJson);
                            final int reask_state = JsonUtils.getInt(dataJson, "reask_state", 0);
                            if (reask_state == 0) {
                                String task_info = JsonUtils.getString(dataJson, "task_info", "");
                                mHomeWorkModel = JSON.parseObject(task_info, HomeWorkModel.class);
                                if (mHomeWorkModel != null) {
//								if (TextUtils.isEmpty(mHomeWorkModel.getAvatar())) {
//									grabBtn.setClickable(false);
//									reportBtn.setClickable(false);
//									mCommonView.showDataNullQuestion();
//								} else {}

                                    String bottomTip = mHomeWorkModel.getBottom_tip();
                                    if (!TextUtils.isEmpty(bottomTip)) {
                                        if (bottomTip.contains("{}")) {
                                            bottomTip = bottomTip.replace("{}", "@");
                                            String[] str = bottomTip.split("@");
                                            /*if (str.length > 5) {
                                                //tv_more.setVisibility(View.VISIBLE);
                                                tv_more.setVisibility(View.GONE);
                                            } else {
                                                tv_more.setVisibility(View.GONE);
                                            }*/
                                            if (str.length > 1) {
                                                iv_bottom_icon.setVisibility(View.VISIBLE);
                                                tv_botton.setText(str[0]);
                                               /* for (int i = 1; i < str.length && i < 5; i++) {
                                                    tvs[i - 1].setVisibility(View.VISIBLE);
                                                    tvs[i - 1].setText(str[i]);
                                                }*/

                                            }

                                        } else {
                                            iv_bottom_icon.setVisibility(View.VISIBLE);
                                            tv_botton.setText(bottomTip);
                                        }
                                    } else {
                                        iv_bottom_icon.setVisibility(View.GONE);
                                    }
                                    mCommonView.showData(mHomeWorkModel);

                                } else {
                                    grabBtn.setClickable(false);
                                    reportBtn.setClickable(false);
                                    mCommonView.showDataNullQuestion();
                                    //tv_more.setVisibility(View.GONE);
                                    iv_bottom_icon.setVisibility(View.GONE);
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
                            grabBtn.setClickable(false);
                            reportBtn.setClickable(false);
                            mCommonView.showDataNullQuestion();
                            //tv_more.setVisibility(View.GONE);
                            iv_bottom_icon.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        grabBtn.setClickable(false);
                        reportBtn.setClickable(false);
                        mCommonView.showDataNullQuestion();
                        //tv_more.setVisibility(View.GONE);
                        iv_bottom_icon.setVisibility(View.GONE);

                    }

                } else if (code == 1) {
                    closeDialog();
                    if (!TextUtils.isEmpty(errMsg)) {
                        LOG.e(errMsg);
                        ToastUtils.show(errMsg);
                    }
                } else {
                    grabBtn.setClickable(false);
                    reportBtn.setClickable(false);
                    mCommonView.showDataNullQuestion();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                getActivity().finish();
                break;
            case R.id.grab_btn_homework_check:// 抢题
                ((BaseActivity) getActivity()).uMengEvent("homework_grab");
                grabHomeWork();
                break;
            case R.id.report_btn_homework_check:// 举报

                mReportWindow = new ReportHomeWorkWindow(view, (BaseActivity) getActivity());
                break;
            case R.id.change_btn_homework_check:// 换题
                ((BaseActivity) getActivity()).uMengEvent("homework_change");
                changeHomeWork();
                break;
            case R.id.tv_more:
                break;
        }
    }

    /**
     * 执行抢题操作
     *
     * @author: sky void
     */
    private void grabHomeWork() {
        if (mHomeWorkModel != null) {
            JSONObject dataJson = new JSONObject();
            LOG.d("fzljson2:"+dataJson);
            try {
                dataJson.put("taskid", mHomeWorkModel.getTaskid());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            showDialog("正在抢题");
            OkHttpHelper.post(getActivity(), "teacher", "grabhomework", dataJson, new HttpListener() {

                @Override
                public void onFail(int code, String errMsg) {
                    closeDialog();
                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    closeDialog();
                    if (code == 0) {

                        final int reask_state = JsonUtils.getInt(dataJson, "reask_state", 0);
                        if (reask_state == 0) {
                            long grabtime = JsonUtils.getLong(dataJson, "grabtime", 0);
                            mHomeWorkModel.setGrabtime(grabtime);
                            int limittime = JsonUtils.getInt(dataJson, "limittime", 0);
                            mHomeWorkModel.setLimittime(limittime);
                            String bottomtip = JsonUtils.getString(dataJson, "bottomtip", "");
                            mHomeWorkModel.setBottomtip(bottomtip);
                            mHomeWorkModel.setState(StateOfHomeWork.ANSWERING);
                            Bundle data = new Bundle();
                            data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
                            // 抢完了题目直接进入
                            IntentManager.goToHomeWorkCheckGrabItemActivity(getActivity(),
                                    TecHomeWorkCheckGrabItemActivity.class, data, false);

                        } else {

                            tipDialog = new CustomTipDialog(getActivity(),
                                    "提示",
                                    "你有学生追问没有回答，无法继续抢题，请先完成追问", "确定", "取消");
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
                        ToastUtils.show(errMsg);
                    }

                }

            });
        }
    }

    public void report(int reasonid, String reasonTxt, final String tipTxt) {
        if (!NetworkUtils.getInstance().isInternetConnected(getActivity())) {
            ToastUtils.show("请检查网络");
            return;
        }
        JSONObject data = new JSONObject();
        try {
            data.put("taskid", mHomeWorkModel.getTaskid());
            data.put("tasktype", GlobalContant.TASKTYPE_HOMEWORK);
            data.put("reason", reasonTxt);
            data.put("reasonid",reasonid);
            data.put("alarmtype",1);//举报方位 1换题 2抢题后
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(getActivity(), "common", "report", data, new HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                if (code == 0) {
                    ((BaseActivity) getActivity()).uMengEvent("homework_report");
                    if (TextUtils.isEmpty(tipTxt)){
                        ToastUtils.show("谢谢您的举报，我们将尽快核实");
                    }else {
                        ToastUtils.show(tipTxt);
                    }

                    changeHomeWork();
                } else {
                    ToastUtils.show(errMsg);
                    changeHomeWork();
                }
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                changeHomeWork();
            }
        });
    }

    public void executeHwBack() {
        closeDialog();
        if (mReportWindow != null && mReportWindow.isShowing()) {
            mReportWindow.dismiss();
        }
    }

    // @Override
    // public void onBackPressed() {
    // closeDialog();
    // if (mReportWindow != null && mReportWindow.isShowing()) {
    // mReportWindow.dismiss();
    // } else {
    // super.onBackPressed();
    // }
    // }

    // @Override
    // protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    // changeHomeWork();
    // }

    @Override
    protected void goBack() {


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homeworkMessageController != null) {
            homeworkMessageController.removeMsgInQueue();
        }
    }

}

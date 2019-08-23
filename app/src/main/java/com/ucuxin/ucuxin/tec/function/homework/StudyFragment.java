
package com.ucuxin.ucuxin.tec.function.homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.DebugActvity;

import androidx.fragment.app.Fragment;

/**
 * 学习Fragment
 * 
 * @author: sky
 */
public class StudyFragment extends Fragment implements OnClickListener {

    private static final String TAG = StudyFragment.class.getSimpleName();

    private Button debug_bt;

    private Activity mActivity;

    private RelativeLayout mPayAnswerContainer;

    private RelativeLayout mAnswerListContainer;

    private RelativeLayout mMyQPadContainer;

    private RelativeLayout layout_responder;

    private TextView mPayAnswerText;

    private TextView mQpadText;

    // private WelearnLoginUtil mLoginUtil;
    public static View mRemindViewGas;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View gasStationView = inflater.inflate(R.layout.fragment_gas_station_backup, container,
                false);
        initView(gasStationView);
        initListener();
        return gasStationView;
    }

    public void initView(View gasStationView) {
        debug_bt = (Button)gasStationView.findViewById(R.id.ip_change_debug_bt);

        if (AppConfig.IS_DEBUG) {
            debug_bt.setVisibility(View.VISIBLE);
            debug_bt.setOnClickListener(this);
        } else {
        }

        gasStationView.findViewById(R.id.homework_check_container).setOnClickListener(this);
        gasStationView.findViewById(R.id.my_homework_check_container).setOnClickListener(this);
        gasStationView.findViewById(R.id.tec_course_container_gas).setOnClickListener(this);
        mPayAnswerContainer = (RelativeLayout)gasStationView
                .findViewById(R.id.pay_answer_container);
        mAnswerListContainer = (RelativeLayout)gasStationView
                .findViewById(R.id.answer_list_container);
        mMyQPadContainer = (RelativeLayout)gasStationView.findViewById(R.id.my_qpad_container);

        layout_responder = (RelativeLayout)gasStationView.findViewById(R.id.layout_responder);

        mPayAnswerText = (TextView)gasStationView.findViewById(R.id.pay_answer_text);

        mQpadText = (TextView)gasStationView.findViewById(R.id.my_qpad_text);

        mRemindViewGas = gasStationView.findViewById(R.id.tec_course_remind_iv_gas);

        mQpadText.setText(mActivity.getText(R.string.text_my_qpad_tea));
        mPayAnswerText.setText(mActivity.getText(R.string.text_ask_title));

    }

    public void initListener() {
        mAnswerListContainer.setOnClickListener(this);
        mPayAnswerContainer.setOnClickListener(this);
        mMyQPadContainer.setOnClickListener(this);
        layout_responder.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEventBegin(mActivity, UmengEventConstant.CUSTOM_EVENT_GASSTATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onEventEnd(mActivity, UmengEventConstant.CUSTOM_EVENT_GASSTATION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.pay_answer_container://难题答疑
//                if (GlobalContant.ROLE_ID_COLLEAGE == MySharePerfenceUtil.getInstance()
//                        .getUserRoleId()) {
//                    IntentManager.goToPayAnswerActivity(mActivity, PayAnswerFragment.class, false);
//                }
//                break;
//            case R.id.homework_check_container:// 作业检查
//                startActivity(new Intent(getActivity(), HomeworkCheckFragment.class));
//                break;
            case R.id.my_homework_check_container:// 我检查的作业
                Intent i = new Intent(getActivity(), StuHomeWorkHallActivity.class);
                i.putExtra("packtype", 3);
                startActivity(i);
                break;
            case R.id.answer_list_container://备课工具
                IntentManager.goToAnswersListActivity(mActivity);
                break;
            case R.id.my_qpad_container://我的回答
                IntentManager.goToMyQpadActivity(mActivity);
                break;
            case R.id.tec_course_container_gas://微课辅导
                mRemindViewGas.setVisibility(View.GONE);
                IntentManager.goToTecCourseActivity(mActivity, null, false);
                break;

            case R.id.ip_change_debug_bt:
                mActivity.startActivity(new Intent(mActivity, DebugActvity.class));
                break;
            case R.id.layout_responder:// 抢答
                IntentManager.goToResponderActivity(mActivity, ResponderActivity.class, false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

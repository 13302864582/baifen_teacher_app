
package com.ucuxin.ucuxin.tec.function.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SubjectGradeChoiceActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout mPrimaryAllContainer;

    private RelativeLayout primary_english_container;

    private RelativeLayout primary_math_selector;

    private RelativeLayout primary_chinese_container;

    private RelativeLayout mMiddleEnglishContainer;

    private RelativeLayout mMiddleMathContainer;

    private RelativeLayout mMiddlePhysicalContainer;

    private RelativeLayout mMiddleChemContainer;

    private RelativeLayout mMiddleBiologyContainer;

    private RelativeLayout mHighEnglishContainer;

    private RelativeLayout mHighMathContainer;

    private RelativeLayout mHighPhysicalContainer;

    private RelativeLayout mHighChemContainer;

    private RelativeLayout mHighBiologyContainer;

    private ImageView mPrimaryAllIcon;
    private ImageView mPrimaryEnglishIcon;
    private ImageView mPrimaryMathIcon;
    private ImageView mPrimaryChineseIcon;

    private ImageView mMiddleEnglishIcon;

    private ImageView mMiddleMathIcon;

    private ImageView mMiddlePhysicalIcon;

    private ImageView mMiddleChemIcon;

    private ImageView mMiddleBiologyIcon;

    private ImageView mHighEnglishIcon;

    private ImageView mHighMathIcon;

    private ImageView mHighPhysicalIcon;

    private ImageView mHighChemIcon;

    private ImageView mHighBioIcon;

    private TextView nextStepTV;

    private RelativeLayout nextStepLayout;

    private Set<String> subjects = new HashSet<String>();

    private Set<Integer> groups = new HashSet<Integer>();

    private static final String TAG = SubjectGradeChoiceActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_subject_grade_choice);

        setWelearnTitle(R.string.text_grade_subject_choice);

        findViewById(R.id.back_layout).setOnClickListener(this);

        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.text_nav_submit);
        nextStepLayout.setOnClickListener(this);

        mPrimaryAllContainer = (RelativeLayout)findViewById(R.id.primary_all_container);
        primary_english_container = (RelativeLayout)findViewById(R.id.primary_english_container);
        primary_math_selector = (RelativeLayout)findViewById(R.id.primary_math_selector);
        primary_chinese_container = (RelativeLayout)findViewById(R.id.primary_chinese_container);

        mMiddleEnglishContainer = (RelativeLayout)findViewById(R.id.middle_english_container);
        mMiddleMathContainer = (RelativeLayout)findViewById(R.id.middle_math_selector);
        mMiddlePhysicalContainer = (RelativeLayout)findViewById(R.id.middle_physical_container);
        mMiddleChemContainer = (RelativeLayout)findViewById(R.id.middle_chemistry_container);
        mMiddleBiologyContainer = (RelativeLayout)findViewById(R.id.middle_biology_container);

        mHighEnglishContainer = (RelativeLayout)findViewById(R.id.high_english_container);
        mHighMathContainer = (RelativeLayout)findViewById(R.id.high_math_container);
        mHighPhysicalContainer = (RelativeLayout)findViewById(R.id.high_physical_container);
        mHighChemContainer = (RelativeLayout)findViewById(R.id.high_chemistry_container);
        mHighBiologyContainer = (RelativeLayout)findViewById(R.id.high_biology_container);

        mPrimaryAllIcon = (ImageView)findViewById(R.id.ic_choice_primary_all);
        mPrimaryEnglishIcon = (ImageView)findViewById(R.id.ic_choice_primary_english);
        mPrimaryMathIcon = (ImageView)findViewById(R.id.ic_choice_primary_math);
        mPrimaryChineseIcon = (ImageView)findViewById(R.id.ic_choice_primary_chinese);
        
        
        
        
        mMiddleEnglishIcon = (ImageView)findViewById(R.id.ic_choice_middle_english);
        mMiddleMathIcon = (ImageView)findViewById(R.id.ic_choice_middle_math);
        mMiddlePhysicalIcon = (ImageView)findViewById(R.id.ic_choice_middle_physical);
        mMiddleChemIcon = (ImageView)findViewById(R.id.ic_choice_middle_chemistry);
        mMiddleBiologyIcon = (ImageView)findViewById(R.id.ic_choice_middle_biology);

        mHighEnglishIcon = (ImageView)findViewById(R.id.ic_choice_high_english);
        mHighMathIcon = (ImageView)findViewById(R.id.ic_choice_high_math);
        mHighPhysicalIcon = (ImageView)findViewById(R.id.ic_choice_high_physical);
        mHighChemIcon = (ImageView)findViewById(R.id.ic_choice_high_chemistry);
        mHighBioIcon = (ImageView)findViewById(R.id.ic_choice_high_biology);

        mPrimaryAllContainer.setOnClickListener(this);
        primary_english_container.setOnClickListener(this);
        primary_math_selector.setOnClickListener(this);
        primary_chinese_container.setOnClickListener(this);

        mMiddleEnglishContainer.setOnClickListener(this);
        mMiddleMathContainer.setOnClickListener(this);
        mMiddlePhysicalContainer.setOnClickListener(this);
        mMiddleChemContainer.setOnClickListener(this);
        mMiddleBiologyContainer.setOnClickListener(this);

        mHighEnglishContainer.setOnClickListener(this);
        mHighMathContainer.setOnClickListener(this);
        mHighChemContainer.setOnClickListener(this);
        mHighBiologyContainer.setOnClickListener(this);
        mHighPhysicalContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RelativeLayout container = null;
        if (v instanceof RelativeLayout) {
            container = (RelativeLayout)v;
        }
        switch (v.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.middle_english_container:
                toggle(container.getTag().toString(), mMiddleEnglishIcon);
                groups.add(1);
                break;
            case R.id.middle_math_selector:
                toggle(container.getTag().toString(), mMiddleMathIcon);
                groups.add(1);
                break;
            case R.id.middle_physical_container:
                toggle(container.getTag().toString(), mMiddlePhysicalIcon);
                groups.add(1);
                break;
            case R.id.middle_chemistry_container:
                toggle(container.getTag().toString(), mMiddleChemIcon);
                groups.add(1);
                break;
            case R.id.middle_biology_container:
                toggle(container.getTag().toString(), mMiddleBiologyIcon);
                groups.add(1);
                break;
            case R.id.high_english_container:
                toggle(container.getTag().toString(), mHighEnglishIcon);
                groups.add(2);
                break;
            case R.id.high_math_container:
                toggle(container.getTag().toString(), mHighMathIcon);
                groups.add(2);
                break;
            case R.id.high_physical_container:
                toggle(container.getTag().toString(), mHighPhysicalIcon);
                groups.add(2);
                break;
            case R.id.high_chemistry_container:
                toggle(container.getTag().toString(), mHighChemIcon);
                groups.add(2);
                break;
            case R.id.high_biology_container:
                toggle(container.getTag().toString(), mHighBioIcon);
                groups.add(2);
                break;
            case R.id.primary_all_container:
                toggle(container.getTag().toString(), mPrimaryAllIcon);
                groups.add(3);
                break;
            case R.id.primary_english_container:
                toggle(container.getTag().toString(), mPrimaryEnglishIcon);
                groups.add(3);
                break;
            case R.id.primary_math_selector:
                toggle(container.getTag().toString(), mPrimaryMathIcon);
                groups.add(3);
                break;
            case R.id.primary_chinese_container:
                toggle(container.getTag().toString(), mPrimaryChineseIcon);
                groups.add(3);
                break;
            case R.id.next_setp_layout:
                if ((groups == null || groups.size() <= 0) || subjects == null
                        || subjects.size() <= 0) {
                    ToastUtils.show(R.string.text_toast_grade_subject_choice);
                } else {
                    JSONObject data = new JSONObject();
                    try {
                        // group:1初中，2高中，3小学
                        for (Integer grpup : groups) {
                            JSONArray jsonArr = new JSONArray();
                            for (String subject : subjects) {
                                Integer currentGroup = Integer.parseInt(subject.split(",")[0]);
                                if (currentGroup == grpup) {
                                    jsonArr.put(Integer.parseInt(subject.split(",")[1]));
                                    switch (currentGroup) {
                                        case 1:
                                            data.put("1", jsonArr);
                                            data.put("2", jsonArr);
                                            data.put("3", jsonArr);
                                            break;
                                        case 2:
                                            data.put("4", jsonArr);
                                            data.put("5", jsonArr);
                                            data.put("6", jsonArr);
                                            break;
                                        case 3:
                                            data.put("7", jsonArr);
                                            data.put("8", jsonArr);
                                            data.put("9", jsonArr);
                                            data.put("10", jsonArr);
                                            data.put("11", jsonArr);
                                            data.put("12", jsonArr);
                                            break;
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    LogUtils.i(TAG, data.toString());

                    UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB()
                            .queryCurrentUserInfo();

                    if (null != uInfo) {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("teachmajor", data);
                            WeLearnApi.updateUserInfoFromServer(this, obj, new HttpListener() {
                                @Override
                                public void onSuccess(int code, String dataJson, String errMsg) {
                                    if (code == 0) {
                                        ToastUtils.show(R.string.modifyinfosuccessful);
                                        finish();
                                    } else {
                                        if (!TextUtils.isEmpty(errMsg)) {
                                            ToastUtils.show(errMsg);
                                        } else {
                                            ToastUtils.show(R.string.modifyinfofailed);
                                        }
                                    }
                                }

                                @Override
                                public void onFail(int HttpCode,String errMsg) {
                                    ToastUtils.show(R.string.modifyinfofailed);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(R.string.text_unlogin);
                        finish();
                    }
                }
                break;
        }
    }

    private void toggle(String text, ImageView view) {
        if (view.getVisibility() == View.GONE) {
            subjects.add(text);
            view.setVisibility(View.VISIBLE);
        } else {
            subjects.remove(text);
            view.setVisibility(View.GONE);
        }
    }
}

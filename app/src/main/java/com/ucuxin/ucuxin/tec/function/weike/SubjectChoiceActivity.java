
package com.ucuxin.ucuxin.tec.function.weike;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.account.SubjectGradeChoiceActivity;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 科目选择
 * 
 * @author: sky
 */

public class SubjectChoiceActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = SubjectGradeChoiceActivity.class.getSimpleName();

    private int grideid;

    private int subjectid;

    private ArrayList<ImageView> iconList;

    private RelativeLayout layout_subject_english;

    private RelativeLayout layout_subject_chinese;

    private RelativeLayout layout_subject_math;

    private RelativeLayout layout_subject_physical;

    private RelativeLayout layout_subject_chemistry;

    private RelativeLayout layout_subject_biology;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_choice_activity);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.subject_choice_text2);

        findViewById(R.id.back_layout).setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            subjectid = intent.getIntExtra("subjectid", 0);
            grideid = intent.getIntExtra("grideid", 0);
        }
        findViewById(R.id.next_setp_layout).setOnClickListener(this);
        TextView nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.text_nav_submit);
        findViewById(R.id.next_setp_layout).setVisibility(View.GONE);

        layout_subject_english = (RelativeLayout)findViewById(R.id.english_btn_subjectchoice);
        layout_subject_math = (RelativeLayout)findViewById(R.id.math_btn_subjectchoice);
        layout_subject_physical = (RelativeLayout)findViewById(R.id.physical_btn_subjectchoice);
        layout_subject_chemistry = (RelativeLayout)findViewById(R.id.chemistry_btn_subjectchoice);
        layout_subject_biology = (RelativeLayout)findViewById(R.id.biology_btn_subjectchoice);
        layout_subject_chinese = (RelativeLayout)findViewById(R.id.chinese_btn_subjectchoice);

        ImageView mMiddleEnglishIcon = (ImageView)findViewById(R.id.english_iv_subjectchoice);
        ImageView mMiddleMathIcon = (ImageView)findViewById(R.id.math_iv_subjectchoice);
        ImageView mMiddlePhysicalIcon = (ImageView)findViewById(R.id.physical_iv_subjectchoice);
        ImageView mMiddleChemIcon = (ImageView)findViewById(R.id.chemistry_iv_subjectchoice);
        ImageView mMiddleBiologyIcon = (ImageView)findViewById(R.id.biology_iv_subjectchoice);
        ImageView mMiddleChineseIcon = (ImageView)findViewById(R.id.chinese_iv_subjectchoice);       

        if (grideid > 6) {// 小学            
            layout_subject_english.setVisibility(View.VISIBLE); 
            layout_subject_math .setVisibility(View.VISIBLE);
            layout_subject_physical .setVisibility(View.GONE);
            layout_subject_chemistry.setVisibility(View.GONE); 
            layout_subject_biology .setVisibility(View.GONE);
            layout_subject_chinese.setVisibility(View.VISIBLE);
            findViewById(R.id.line_math).setVisibility(View.GONE);
            findViewById(R.id.line_physical).setVisibility(View.GONE);
            findViewById(R.id.line_chemistry).setVisibility(View.GONE);
            
            iconList = new ArrayList<>();
            iconList.add(mMiddleEnglishIcon);
            iconList.add(mMiddleMathIcon);           
            iconList.add(mMiddleChineseIcon);
            setSubjectSelect(3);
        } else {            
            layout_subject_english.setVisibility(View.VISIBLE); 
            layout_subject_math .setVisibility(View.VISIBLE);
            layout_subject_physical .setVisibility(View.VISIBLE);
            layout_subject_chemistry.setVisibility(View.VISIBLE); 
            layout_subject_biology .setVisibility(View.VISIBLE); 
            layout_subject_chinese.setVisibility(View.GONE);
            iconList = new ArrayList<>();
            iconList.add(mMiddleEnglishIcon);
            iconList.add(mMiddleMathIcon);
            iconList.add(mMiddlePhysicalIcon);
            iconList.add(mMiddleChemIcon);
            iconList.add(mMiddleBiologyIcon);
            iconList.add(mMiddleChineseIcon);
            setSubjectSelect(5);
        }

    }

    @Override
    public void initListener() {
        super.initListener();
        layout_subject_english.setOnClickListener(this);
        layout_subject_math.setOnClickListener(this);
        layout_subject_physical.setOnClickListener(this);
        layout_subject_chemistry.setOnClickListener(this);
        layout_subject_biology.setOnClickListener(this);
        layout_subject_chinese.setOnClickListener(this);
    }

    private void setSubjectSelect(int count) {
        for (int i = 0; i < count; i++) {            
            if (subjectid==6) {
                ImageView view = iconList.get(i);
                if (i ==2) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                
            }else {   
                ImageView view = iconList.get(i);
                if (i == (subjectid - 1)) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
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
                setResultAndFinish();
                break;
            case R.id.english_btn_subjectchoice:
            case R.id.math_btn_subjectchoice:
            case R.id.physical_btn_subjectchoice:
            case R.id.chemistry_btn_subjectchoice:
            case R.id.biology_btn_subjectchoice:
            case R.id.chinese_btn_subjectchoice:
                RelativeLayout container = null;
                if (view instanceof RelativeLayout) {
                    container = (RelativeLayout)view;
                    String tagStr = container.getTag().toString();
                    try {
                        subjectid = Integer.parseInt(tagStr);
                    } catch (Exception e) {
                    }

                    setResultAndFinish();
                    // setSubjectSelect();
                }
                break;
        }

    }

    private void setResultAndFinish() {
        if (subjectid < 1) {
            ToastUtils.show("请先选择科目");
            return;
        }
        Intent data = new Intent();
        data.putExtra("subjectid", subjectid);
        setResult(RESULT_OK, data);
        finish();
    }
}

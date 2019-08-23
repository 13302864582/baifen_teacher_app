package com.ucuxin.ucuxin.tec.function.weike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.GradeUtil;
import com.ucuxin.ucuxin.tec.utils.SubjectUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加课程
 * @author:  sky
 */
public class AddCourseActivity extends BaseActivity implements OnClickListener {
	private EditText nameEt;
	private EditText introdEt;
	private LinearLayout gradeBtn;
	private TextView gradeTv;
	private ImageView gradeIv;
	private LinearLayout subjectBtn;
	private TextView subjectTv;
	private ImageView subjectIv;
	private ImageView minusPriceBtn;
	private ImageView plusPriceBtn;
	private TextView priceTv;
	private ImageView minusNumBtn;
	private TextView numTv;
	private ImageView plusNumBtn;
	private int gradeid = -1;
	private int subjectid = -1;
	private String content;
	private String coursename;
	private int price = DEFULT_PRICE;
	private int charptercount = DEFULT_HOUR;
	public final static int MIN_PRICE = 0;
	public static final int MIN_HOUR = 1;
	public final static int DEFULT_PRICE = 5;
	public static final int DEFULT_HOUR = 2;
	public static final int CELL = 1;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.add_course_activity);
		initView();
		initListener();
		
	}
	
	
	@Override
	public void initView() {
	    super.initView();	    
	    setWelearnTitle(R.string.add_course_title_text);

        TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
        nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
        nextStepTV.setVisibility(View.VISIBLE);
        nextStepTV.setText(R.string.save_text);

        gradeBtn = (LinearLayout) findViewById(R.id.grade_choice_btn_addcourse);
        gradeTv = (TextView) findViewById(R.id.grade_choice_tv_addcourse);
        gradeIv = (ImageView) findViewById(R.id.grade_choice_iv_addcourse);
      

        subjectBtn = (LinearLayout) findViewById(R.id.subject_choice_btn_addcourse);
        subjectTv = (TextView) findViewById(R.id.subject_choice_tv_addcourse);
        subjectIv = (ImageView) findViewById(R.id.subject_choice_iv_addcourse);
        

        nameEt = (EditText) findViewById(R.id.name_of_course_et_addcourse);
        introdEt = (EditText) findViewById(R.id.introd_of_course_et_addcourse);

        minusPriceBtn = (ImageView) findViewById(R.id.minus_price_ibtn_addcourse);
        priceTv = (TextView) findViewById(R.id.price_tv_addcourse);
        priceTv.setText("" + price);
        plusPriceBtn = (ImageView) findViewById(R.id.plus_price_ibtn_addcourse);
    

        minusNumBtn = (ImageView) findViewById(R.id.minus_num_ibtn_addcourse);
        numTv = (TextView) findViewById(R.id.num_tv_addcourse);
        numTv.setText("" + charptercount);
        plusNumBtn = (ImageView) findViewById(R.id.plus_num_ibtn_addcourse);
       
	}
	
	
	@Override
	public void initListener() {
	    super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.next_setp_layout).setOnClickListener(this);
        gradeBtn.setOnClickListener(this);
        subjectBtn.setOnClickListener(this);
        minusPriceBtn.setOnClickListener(this);
        plusPriceBtn.setOnClickListener(this);
        minusNumBtn.setOnClickListener(this);
        plusNumBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.next_setp_layout:// 保存
			addCourse();
			break;
		case R.id.back_layout://返回
			finish();
			break;
		case R.id.grade_choice_btn_addcourse://选择年级
			Bundle data = new Bundle();
			data.putInt("gradeid", gradeid);
			IntentManager.goToGradeChoiceActivity(this, data, false);
			break;
		case R.id.subject_choice_btn_addcourse://选择科目
			if (gradeid == -1) {
				ToastUtils.show("请先选择年级");
				return;
			}
//			if (gradeid > 6) {
//				ToastUtils.show("小学不需要选择科目");
//				return;
//			}
			data = new Bundle();
			data.putInt("grideid", gradeid);
			data.putInt("subjectid", subjectid);
			IntentManager.goToSubjectChoiceActivity(this, data , false);
			break;
		case R.id.minus_price_ibtn_addcourse:
			price = Integer.parseInt(priceTv.getText().toString());
			price -= CELL;
			if (price < MIN_PRICE) {
				ToastUtils.show("价格不能低于" + MIN_PRICE +"个学点");
				return;
			}
			priceTv.setText("" + price);
			break;
		case R.id.plus_price_ibtn_addcourse:
			price = Integer.parseInt(priceTv.getText().toString());
			price += CELL;
			priceTv.setText("" + price);
			break;
		case R.id.minus_num_ibtn_addcourse:
			charptercount = Integer.parseInt(numTv.getText().toString());
			charptercount -- ;
			if (charptercount < MIN_HOUR) {
				ToastUtils.show("不能低于" + MIN_HOUR +"个课时");
				return;
			}
			numTv.setText("" + charptercount);
			
			break;
		case R.id.plus_num_ibtn_addcourse:
			charptercount = Integer.parseInt(numTv.getText().toString());
			charptercount ++ ;
			numTv.setText("" + charptercount);
			break;

		default:
			break;
		}
	}

	private void addCourse() {
		if (gradeid == -1) {
			ToastUtils.show("请先选择年级");
			return;
		}

		if (subjectid == -1) {
			ToastUtils.show("请先选择科目");
			return;
		}

		coursename = nameEt.getText().toString().trim();
		if (TextUtils.isEmpty(coursename)) {
			ToastUtils.show("请先输入课程名称");
			return;
		}
		content = introdEt.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			ToastUtils.show("请先输入课程名称");
			return;
		}

		price =Integer.parseInt(priceTv.getText().toString());
		charptercount = Integer.parseInt(numTv.getText().toString());
		JSONObject data = new JSONObject();
		try {
			data.put("gradeid", gradeid);
			data.put("subjectid", subjectid);
			data.put("coursename", coursename);
			data.put("content", content);
			data.put("price", price);
			data.put("charptercount", charptercount);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "course","addcourse",  data,new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				setResult(RESULT_OK);
				finish();
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case 1002:// 选年级
				if (data != null) {
					int grade = data.getIntExtra("gradeid", -1);

					if (grade > -1) {
						gradeTv.setText(GradeUtil.getGradeString(grade));
						if (grade > 6) {
							subjectTv.setText("选择科目");
							subjectid = -1;
						} else if (gradeid > 6) {
							subjectTv.setText("选择科目");
							subjectid = -1;
						}

						gradeid = grade;
					}
				}
				break;
			case 1003:// 选科目
				subjectid = data.getIntExtra("subjectid", -1);
				if (subjectid > -1) {
					subjectTv.setText(SubjectUtil.getSubjectString(subjectid));
				}
				break;

			default:
				break;
			}
		}
	}

//	private void setGradeChoiced(String grade) {
//		gradeTv.setText(grade);
//		gradeTv.setTextColor(getResources().getColor(R.color.white));
//		gradeBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
//		gradeIv.setImageResource(R.drawable.ic_setting_jump);
//	}
//
//	private void setSubjectChoiced(String subject) {
//		subjectTv.setText(subject);
//		subjectTv.setTextColor(getResources().getColor(R.color.white));
//		subjectBtn.setBackgroundResource(R.drawable.choice_wrong_type_choiced);
//		subjectIv.setImageResource(R.drawable.ic_setting_jump);
//	}
//
//	private void setSubjectUnChoiced() {
//		subjectTv.setText("选择科目");
//		subjectTv.setTextColor(getResources().getColor(R.color.black));
//		subjectBtn.setBackgroundResource(R.drawable.refuse_homework_btn_selector);
//		subjectIv.setImageResource(R.drawable.choice_wrong_type_btn_icon_);
//	}

}

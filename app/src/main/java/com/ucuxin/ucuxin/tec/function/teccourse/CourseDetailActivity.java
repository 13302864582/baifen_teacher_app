package com.ucuxin.ucuxin.tec.function.teccourse;

import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CourseModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.content.ContextCompat;

public class CourseDetailActivity extends BaseActivity implements OnClickListener {
	private EditText nameEt;
	private EditText contentEt;
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
	// private String content;
	private int courseid;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.add_course_activity);
		setWelearnTitle(R.string.add_course_title_text);
		findViewById(R.id.back_layout).setOnClickListener(this);

		findViewById(R.id.next_setp_layout).setOnClickListener(this);

		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.save_text);

		gradeBtn = (LinearLayout) findViewById(R.id.grade_choice_btn_addcourse);
		gradeTv = (TextView) findViewById(R.id.grade_choice_tv_addcourse);
		gradeIv = (ImageView) findViewById(R.id.grade_choice_iv_addcourse);
		gradeBtn.setBackgroundResource(R.drawable.grade_unclick_bg);

		subjectBtn = (LinearLayout) findViewById(R.id.subject_choice_btn_addcourse);
		subjectTv = (TextView) findViewById(R.id.subject_choice_tv_addcourse);
		subjectIv = (ImageView) findViewById(R.id.subject_choice_iv_addcourse);
		subjectBtn.setBackgroundResource(R.drawable.subjects_unclick_bg);

		nameEt = (EditText) findViewById(R.id.name_of_course_et_addcourse);
		nameEt.setEnabled(false);
		nameEt.setBackgroundResource(R.drawable.class_introduce_bg);

		contentEt = (EditText) findViewById(R.id.introd_of_course_et_addcourse);

		minusPriceBtn = (ImageView) findViewById(R.id.minus_price_ibtn_addcourse);
		priceTv = (TextView) findViewById(R.id.price_tv_addcourse);

		plusPriceBtn = (ImageView) findViewById(R.id.plus_price_ibtn_addcourse);
		//getResources().getColor(R.color.unclickable) 已经过时了
//		minusPriceBtn.setBackgroundColor(getResources().getColor(R.color.unclickable));
//		plusPriceBtn.setBackgroundColor(getResources().getColor(R.color.unclickable));

		minusPriceBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.unclickable));
		plusPriceBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.unclickable));

		minusNumBtn = (ImageView) findViewById(R.id.minus_num_ibtn_addcourse);
		numTv = (TextView) findViewById(R.id.num_tv_addcourse);

		plusNumBtn = (ImageView) findViewById(R.id.plus_num_ibtn_addcourse);
		minusNumBtn.setBackgroundColor(getResources().getColor(R.color.unclickable));
		plusNumBtn.setBackgroundColor(getResources().getColor(R.color.unclickable));
		courseid = getIntent().getIntExtra("courseid", 0);

		JSONObject data = new JSONObject();
		try {
			data.put("courseid", courseid);
		} catch (JSONException e) {

			e.printStackTrace();
		}
		OkHttpHelper.post(this, "course","coursedetail",data,new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (!TextUtils.isEmpty(dataJson)) {
					CourseModel courseModel = new Gson().fromJson(dataJson, CourseModel.class);
					if (courseModel != null) {
						String coursename = courseModel.getCoursename();
						String content = courseModel.getContent();
						String grade = courseModel.getGrade();
						String subject = courseModel.getSubject();
						int charptercount = courseModel.getCharptercount();
						int price = (int) (courseModel.getPrice() + 0.005);
						subjectTv.setText("" + subject);
						nameEt.setText("" + coursename);
						contentEt.setText("" + content);
						gradeTv.setText("" + grade);
						numTv.setText("" + charptercount);
						priceTv.setText("" + price);
					}
				}
			
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.next_setp_layout:// 保存
			addCourse();
			break;
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	private void addCourse() {
		String content = contentEt.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			ToastUtils.show("请先输入课程名称");
			return;
		}

		JSONObject data = new JSONObject();
		try {
			data.put("courseid", courseid);
			data.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "course","updatecourse",data, new HttpListener() {
			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				setResult(RESULT_OK);
				finish();	
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		} );
	}

}

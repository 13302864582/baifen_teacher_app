package com.ucuxin.ucuxin.tec.function;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.model.GradeModel;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.GradeView;
import com.ucuxin.ucuxin.tec.view.GradeView.OnGradeChildClickListener;

public class GradeChoiceActivity extends BaseActivity implements OnClickListener, OnGradeChildClickListener {

	private LinearLayout gradeParentLayout;
	private int gradeid;

	public static final String TAG = GradeChoiceActivity.class.getSimpleName();

	private ArrayList<GradeModel> gradeParentList = new ArrayList<GradeModel>();
	private ArrayList<GradeView> gradeViewList = new ArrayList<GradeView>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_grade_choice);

		setWelearnTitle(R.string.text_grade_choice);

		findViewById(R.id.back_layout).setOnClickListener(this);

		gradeParentLayout = (LinearLayout) findViewById(R.id.grade_parent_layout);

		gradeParentList = WLDBHelper.getInstance().getWeLearnDB().queryAllGradeParent();
		if (null != gradeParentList) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 20, 0, 0);
			for (int i = 0; i < gradeParentList.size(); i++) {
				GradeModel gm = gradeParentList.get(i);
				ArrayList<GradeModel> gList = WLDBHelper.getInstance().getWeLearnDB().queryGradeByParentId(gm.getId());
				if (null != gList && gList.size() > 0) {
					GradeView gv = new GradeView(this);
					gv.setIndex(i);
					gv.setGradeTitle(gm.getName());
					for (int j = 0; j < gList.size(); j++) {
						GradeModel subgm = gList.get(j);
						gv.addItem(subgm.getId(), subgm.getName(), j);
					}
					gv.setOnGradeChildClickListener(this);
					gradeViewList.add(gv);
					gradeParentLayout.addView(gv, lp);
				}
			}
		}

		RelativeLayout nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_nav_submit);
		nextStepLayout.setOnClickListener(this);
		nextStepLayout.setVisibility(View.GONE);
		Intent intent = getIntent();
		if (intent != null) {
			gradeid = intent.getIntExtra("gradeid", 0);
		}
		int index = -1;
		switch (gradeid) {
		case 1:
		case 2:
		case 3:
			index = 1;
			break;
		case 4:
		case 5:
		case 6:
			index = 2;
			break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			index = 0;
			break;

		default:
			break;
		}
		if (index > -1) {
			for (int i = 0; i < gradeViewList.size(); i++) {
				GradeView gv = gradeViewList.get(i);
				if (i != index) {
					gv.resetViewsState();
				}
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_setp_layout:
			setResultAndFinish();
			break;
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	private void setResultAndFinish() {
		if (gradeid == 0) {
			ToastUtils.show(R.string.text_toast_select_grade);
		} else {
			Intent data = new Intent();
			data.putExtra("gradeid", gradeid);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	public void onChildClicked(int index, int id) {
		gradeid = id;
		setResultAndFinish();
		

		
		
	}

}

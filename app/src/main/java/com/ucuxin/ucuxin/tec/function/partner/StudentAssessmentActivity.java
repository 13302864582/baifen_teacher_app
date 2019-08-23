package com.ucuxin.ucuxin.tec.function.partner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.TeacherCommentAdapter;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.model.CommentModel;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 
 * 此类的描述： 学生对老师的评价
 * @author:  qhw
 * @最后修改人： qhw 
 * @最后修改日期:2015-7-23 下午3:02:33
 * @version: 2.0
 */
public class StudentAssessmentActivity extends BaseActivity implements OnClickListener, HttpListener{
	RatingBar starLvRb ;
	private int userid;
	private int pageindex = 1;
	private int pagecount = 999;
	private ListView pingjiaLv;
	private TeacherCommentAdapter adapter;
	
	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_student_assessment);
		findViewById(R.id.back_layout).setOnClickListener(this);
		setWelearnTitle(R.string.stu_evaluation_text);
		starLvRb = (RatingBar)findViewById(R.id.star_lv_rb_stupingjia);
		pingjiaLv = (ListView)findViewById(R.id.pingjia_lv_stupingjia);
		Intent intent = getIntent();
		
		if (intent!=null) {
			userid = intent.getIntExtra("userid", 0);
		}
		loadData();
	}

	private void loadData (){
		if (userid != 0) {
			WeLearnApi.getTeacherCommList(this, userid,pageindex, pagecount, this);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	    
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			if (!TextUtils.isEmpty(dataJson)) {
				int star = JsonUtils.getInt(dataJson, "star", 0);
				starLvRb.setProgress(star);
				String contents = JsonUtils.getString(dataJson, "contents", "");
				if (!TextUtils.isEmpty(contents)) {
					ArrayList<CommentModel> comms  = null;
					try {
						comms = new Gson().fromJson(contents, new TypeToken<ArrayList<CommentModel>>() {
						}.getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (adapter == null) {
						adapter = new TeacherCommentAdapter(this, comms);
						pingjiaLv.setAdapter(adapter);
					}else {
						adapter.setData(comms);
					}
					pageindex++;
				}
			}
		}else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		ToastUtils.show("连接失败:"+HttpCode +"");
		
	}

}

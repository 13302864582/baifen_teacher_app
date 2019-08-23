
package com.ucuxin.ucuxin.tec.function.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 知识点
 * 
 * @author sky
 */
public class KnowledgeSummaryActivity extends BaseActivity {

    private ImageView back_iv;

    private TextView next_step_btn;

    private int taskid;

    private EditText et_knowledg1;

    private EditText et_knowledg2;

    private EditText et_knowledg3;

    private EditText et_knowledg4;

    private EditText et_knowledg5;
    
    private HomeWorkModel mHomeWorkModel;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.knowledge_summary_activity);
        GlobalVariable.tempActivityList.add(this);
        getExtra();
        initView();
        initListener();

    }

    private void getExtra() {
        Intent intent=getIntent();
        mHomeWorkModel=(HomeWorkModel)intent.getSerializableExtra("mHomeWorkModel");
        taskid = mHomeWorkModel.getTaskid();

    }

    @Override
    public void initView() {
        super.initView();
        back_iv = (ImageView)findViewById(R.id.back_iv);
        next_step_btn = (TextView)findViewById(R.id.next_step_btn);
        next_step_btn.setVisibility(View.VISIBLE);
        setWelearnTitle("知识点总结");
        next_step_btn.setText("下一步");

        et_knowledg1 = (EditText)this.findViewById(R.id.et_knowledg1);
        et_knowledg2 = (EditText)this.findViewById(R.id.et_knowledg2);
        et_knowledg3 = (EditText)this.findViewById(R.id.et_knowledg3);
        et_knowledg4 = (EditText)this.findViewById(R.id.et_knowledg4);
        et_knowledg5 = (EditText)this.findViewById(R.id.et_knowledg5);

    }

    @Override
    public void initListener() {
        super.initListener();
        back_iv.setOnClickListener(this);
        next_step_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_iv:
                //由于上一个activity已经finish了
                Intent intent=new Intent(this,ShowHomeworkCheckActivity.class);
                Bundle data = new Bundle();
                data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
                intent.putExtras(data);                
                startActivity(intent);
                finish();
                break;
            case R.id.next_step_btn:// 右边的按钮
                executeNext();
                break;
        }
    }

    public void executeNext() {
        String str_knowledg1 = et_knowledg1.getText().toString().trim();
        String str_knowledg2 = et_knowledg2.getText().toString().trim();
        String str_knowledg3 = et_knowledg3.getText().toString().trim();
        String str_knowledg4 = et_knowledg4.getText().toString().trim();
        String str_knowledg5 = et_knowledg5.getText().toString().trim();
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(str_knowledg1)) {
            sb.append(str_knowledg1+";");
        }
        if(!TextUtils.isEmpty(str_knowledg2)){
            sb.append(str_knowledg2+";");
        }
        if(!TextUtils.isEmpty(str_knowledg3)){
            sb.append(str_knowledg3+";");
        }
        if(!TextUtils.isEmpty(str_knowledg4)){
            sb.append(str_knowledg4+";");
        }
        if(!TextUtils.isEmpty(str_knowledg5)){
            sb.append(str_knowledg5);
        }
        if (TextUtils.isEmpty(sb.toString())){
            ToastUtils.show("没有填写知识点");
            return;
        }
        Intent intentn = new Intent(this, HwReviewActivity.class);
        intentn.putExtra("mHomeWorkModel",mHomeWorkModel);
        intentn.putExtra("taskid", taskid);
        intentn.putExtra("knowledge", sb.toString());
        startActivity(intentn);
        
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //由于上一个activity已经finish了
        Intent intent=new Intent(this,ShowHomeworkCheckActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
        intent.putExtras(data);                
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {

    	super.onDestroy();
    }
    
}


package com.ucuxin.ucuxin.tec.function.teccourse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CharpterModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseCatalogActivity extends BaseActivity
        implements OnClickListener, OnItemClickListener {
    private ArrayList<CharpterModel> mClassModels;

    private View haveClassView;

    private ClassAdapter mAdapter;

    private ListView catalogLv;

    private TextView totalTv;

    private TextView doneNumTv;

    private int charptercount;

    private int courseid;

    private int gradeid = -1;

    private int subjectid = -1;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.course_catalog_activity);
        initView();
        loadData();

    }

    @Override
    public void initView() {
        super.initView();

        setWelearnTitle(R.string.class_catalog_title_text);

        findViewById(R.id.back_layout).setOnClickListener(this);

        findViewById(R.id.add_new_class_btn_catalog).setOnClickListener(this);
        haveClassView = findViewById(R.id.have_class_view_catalog);
        catalogLv = (ListView)findViewById(R.id.class_lv_catalog);
        totalTv = (TextView)findViewById(R.id.total_num_tv_catalog);
        doneNumTv = (TextView)findViewById(R.id.done_num_tv_catalog);

        mAdapter = new ClassAdapter();
        catalogLv.setAdapter(mAdapter);
        catalogLv.setOnItemClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            courseid = intent.getIntExtra("courseid", 0);
            charptercount = intent.getIntExtra("charptercount", 0);
            gradeid = intent.getIntExtra("gradeid", -1);
            subjectid = intent.getIntExtra("subjectid", -1);
        }
        totalTv.setText("" + charptercount);

    }

    private void loadData() {
        if (courseid != 0) {
            JSONObject data = new JSONObject();
            try {
                data.put("courseid", courseid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpHelper.post(this, "course","allcharpter",data,new HttpListener() {
				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
                    if (!TextUtils.isEmpty(dataJson)) {
                        try {
                            mClassModels = new Gson().fromJson(dataJson,
                                    new TypeToken<ArrayList<CharpterModel>>() {
                            }.getType());
                        } catch (Exception e) {
                        }
                        if (mClassModels != null && !mClassModels.isEmpty()) {
                            setHavaData();
                        } else {
                            setNullData();
                        }
                    } else {
                        setNullData();
                    }              
					
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {

					
				}
			});
        }
    }

    private void setNullData() {
        haveClassView.setVisibility(View.GONE);
        mClassModels = null;
        mAdapter.notifyDataSetChanged();
    }

    private void setHavaData() {
        haveClassView.setVisibility(View.VISIBLE);
        doneNumTv.setText("" + mClassModels.size());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                goBack();
                break;
            case R.id.add_new_class_btn_catalog:// 添加课程
                IntentManager.goToUpLoadClassActivity(this, courseid, gradeid, subjectid);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1002:
                    loadData();
                    break;

            }
        }
    }

    private class ClassAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mClassModels == null ? 0 : mClassModels.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            CourseViewHodler hodler;
            if (null == view) {
                hodler = new CourseViewHodler();
                view = View.inflate(CourseCatalogActivity.this, R.layout.class_item, null);
                view.setTag(hodler);

                hodler.wclass = (TextView)view.findViewById(R.id.class_tv_class_item);
                hodler.classname = (TextView)view.findViewById(R.id.classname_tv_class_item);
                hodler.uploadtime = (TextView)view.findViewById(R.id.uploadtime_tv_class_item);
                hodler.classwatchcount = (TextView)view.findViewById(R.id.classtype_tv_class_item);

            } else {
                hodler = (CourseViewHodler)view.getTag();
            }

            CharpterModel model = mClassModels.get(position);
            if (model != null) {
                hodler.wclass.setText("课时" + (position + 1));
                hodler.classname.setText(model.getCharptername());
                long datatime = model.getDatatime();
                Date date = new Date(datatime);
                SimpleDateFormat format = new SimpleDateFormat("y-M-d HH:mm");
                String askTime = format.format(date);
                hodler.uploadtime.setText(askTime);
                hodler.classwatchcount.setText(model.getWatchcount() + "已购买");
            }
            // TextView gradeTv = (TextView)
            // view.findViewById(R.id.grade_tv_course_item);
            // TextView subjectTv = (TextView)
            // view.findViewById(R.id.subject_tv_course_item);
            // TextView courserNameTv = (TextView)
            // view.findViewById(R.id.coursename_tv_course_item);
            // ClassHourModel courseModel = mClassModels.get(position);
            // if (courseModel != null) {
            // String coursename = courseModel.getCoursename();
            // String grade = courseModel.getGrade();
            // String subject = courseModel.getSubject();
            //
            // gradeTv.setText(grade);
            // subjectTv.setText(subject);
            // courserNameTv.setText(coursename);
            // } else {
            // gradeTv.setText("");
            // subjectTv.setText("");
            // courserNameTv.setText("");
            // }
            return view;
        }

    }

    private class CourseViewHodler {
        TextView wclass;

        TextView classname;

        TextView uploadtime;

        TextView classwatchcount;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        CharpterModel hourModel = mClassModels.get(position);
        IntentManager.goToUpLoadClassActivity(this, hourModel, gradeid, subjectid);
    }
}

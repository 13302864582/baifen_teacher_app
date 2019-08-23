
package com.ucuxin.ucuxin.tec.function.homework;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.view.tag.FlowTagLayout;
import com.ucuxin.ucuxin.tec.function.homework.view.tag.TagAdapter;
import com.ucuxin.ucuxin.tec.function.homework.view.tag.TagModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.SelectItemPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 作业标签activity
 *
 * @author sky
 */
public class HwReviewActivity extends BaseActivity /*implements  OnUploadListener,*/ {

    private ImageView back_iv;
    private TextView next_step_btn;
    private int taskid;
    private String knowledge;
    private SelectItemPopupWindow menuWindow;


    private FlowTagLayout mMobileFlowTagLayout;
    private TagAdapter mMobileTagAdapter;
    private List<TagModel> alltags;
    private String choiceStr;
    private HomeWorkModel mHomeWorkModel;


    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.homework_review_activity);
        getExtra();
        initView();
        initListener();
        initData();
        // Intent goIntent = new Intent(HwReviewActivity.this,TecHomeWrokCheckActivity.class);
        // goIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // startActivity(goIntent);

    }

    private void getExtra() {
        mHomeWorkModel=(HomeWorkModel) getIntent().getSerializableExtra("mHomeWorkModel");
        taskid = getIntent().getIntExtra("taskid", 0);
        knowledge = getIntent().getStringExtra("knowledge");

    }

    @Override
    public void initView() {
        super.initView();
        back_iv = (ImageView) findViewById(R.id.back_iv);
        next_step_btn = (TextView) findViewById(R.id.next_step_btn);
        next_step_btn.setVisibility(View.VISIBLE);
        setWelearnTitle("作业标签");
        next_step_btn.setText("提交");

        mMobileFlowTagLayout = (FlowTagLayout) findViewById(R.id.tagview);
        alltags=new ArrayList<TagModel>();
        //移动研发标签
        mMobileTagAdapter = new TagAdapter(this);

        mMobileFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mMobileFlowTagLayout.setAdapter(mMobileTagAdapter);
        mMobileFlowTagLayout.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (int i : selectedList) {
                        TagModel item=(TagModel) parent.getAdapter().getItem(i);
                        sb.append(item.getType());
                        sb.append(",");
                        alltags.add(item);
                    }

                     choiceStr=sb.toString();
                 //   ToastUtils.show("你选择了:" + choiceStr);

                } else {
                    ToastUtils.show("没有选择标签");

                }
            }
        });
//        initMobileData();
    }


    public void initData() {
        showDialog("数据加载中");
        OkHttpHelper.post(this, "teacher", "gettasktags", null, new OkHttpHelper.HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                String tagstr=JsonUtils.getString(dataJson,"tags","");
                List<TagModel> subTags = JSON.parseArray(tagstr, TagModel.class);
                mMobileTagAdapter.onlyAddAll(subTags);
            }

            @Override
            public void onFail(int HttpCode, String errMsg) {
                closeDialog();
            }
        });
    }

//    private void initMobileData() {
//        List<String> dataSource = new ArrayList<>();
//        dataSource.add("android开发工程师");
//        dataSource.add("安卓");
//        dataSource.add("SDK源码");
//        dataSource.add("IOS");
//        dataSource.add("iPhone");
//        dataSource.add("游戏");
//        dataSource.add("fragment");
//        dataSource.add("viewcontroller");
//        dataSource.add("cocoachina");
//        dataSource.add("移动研发工程师");
//        dataSource.add("移动互联网");
//        dataSource.add("高薪+期权");
//        mMobileTagAdapter.onlyAddAll(dataSource);
//    }

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
                finish();
                break;
            case R.id.next_step_btn:// 右边的按钮
                if (NetworkUtils.getInstance().isInternetConnected(HwReviewActivity.this)) {
                    submit();
                } else {
                    ToastUtils.show("网络未连接,请检查网络");
                }
                break;


        }
    }


    private void submit() {

//
//		try {
//			JSONObject data = new JSONObject();
//			data.put("taskid", taskid);
//			data.put("kpoint", knowledge);
//			data.put("remark", remarkStr);
//			showDialog("请稍后...");
////			UploadUtil.upload(AppConfig.GO_URL + "teacher/" + "homeworkremark", RequestParamUtils.getParam(data), files,
////					HwReviewActivity.this, true, 0);
//			UploadUtil2.upload(AppConfig.GO_URL + "teacher/" + "homeworkremark", RequestParamUtils.getMapParam(data), files,
//					new MyStringCallback(), true, 0);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}


        if (!TextUtils.isEmpty(choiceStr)){
                  String[] strs= choiceStr.split(",");
                 if (strs!=null&&strs.length<=3){
                     Map<String, Object> json = new HashMap<String, Object>();
                     json.put("stuid",mHomeWorkModel.getStudid());
                     json.put("taskid",taskid);
                     json.put("tasktype",2);
                     choiceStr=choiceStr.substring(0,choiceStr.length()-1);
                     json.put("tagstype",choiceStr);
                     json.put("kpoint", knowledge);
                     showDialog("正在加载...");
                     OkHttpHelper.postOKHttpBaseParams("teacher","settasktags", json, new HwReviewActivity.MyStringCallback());
                 }else{
                     ToastUtils.show("标签最多选3个");
                 }

        }else{
            ToastUtils.show("标签最多选3个");
        }



    }

    class MyStringCallback extends StringCallback {

        @Override
        public void onBefore(Request request) {

            super.onBefore(request);
        }

        @Override
        public void onAfter() {

            super.onAfter();
        }

        @Override
        public void onError(Call call, Exception e) {
            closeDialog();
            String errorMsg = "";
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                errorMsg = e.getMessage();
            } else {
                errorMsg = e.getClass().getSimpleName();
            }
            if (AppConfig.IS_DEBUG) {
                ToastUtils.show("onError:" + errorMsg);
            } else {
                ToastUtils.show("网络异常");
            }

        }

        @Override
        public void onResponse(String response) {
            closeDialog();
            int code = JsonUtils.getInt(response, "Code", -1);
            String msg = JsonUtils.getString(response, "Msg", "");
            String data = JsonUtils.getString(response, "Data", "");
            if (code == 0) {
                uMengEvent("homework_answer");
                ToastUtils.show("提交成功");
                // finish();

                // Intent data = new Intent();
                // data.putExtra("issubmited", true);
                // setResult(TecHomeWorkCheckDetailActivity.RESULT_OK, data);
                // finish();
                if (GlobalVariable.tempActivityList != null) {

                    int size = GlobalVariable.tempActivityList.size();
                    for (int i = 0; i < size; i++) {
                        Activity item = GlobalVariable.tempActivityList.get(i);
                        if (item instanceof KnowledgeSummaryActivity) {
                            item.finish();
                        }
                        if (item instanceof ShowHomeworkCheckActivity) {
                            item.finish();
                        }
                    }
                }
                GlobalVariable.tempActivityList.clear();
                finish();

//				if ("checked_hw_tag".equals(SpUtil.getInstance().getCheckTag())) {
//					Intent intent = new Intent(HwReviewActivity.this, StuHomeWorkHallActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					intent.putExtra("packtype", 3);
//					startActivity(intent);
//					SpUtil.getInstance().setCheckTag("none");
//					finish();
//				} else {
//					Intent intent = new Intent(HwReviewActivity.this, ResponderActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					intent.putExtra("go_tag", "finish_homework");
//					startActivity(intent);
//					finish();
//				}

            } else {
                ToastUtils.show(msg);
            }

        }

    }

	/*@Override
    public void onUploadSuccess(UploadResult result, int index) {
		closeDialog();
		if (result.getCode() == 0) {
			uMengEvent("homework_answer");
			ToastUtils.show("提交成功");
			// finish();

			// Intent data = new Intent();
			// data.putExtra("issubmited", true);
			// setResult(TecHomeWorkCheckDetailActivity.RESULT_OK, data);
			// finish();
			if (GlobalVariable.tempActivityList != null) {

				int size = GlobalVariable.tempActivityList.size();
				for (int i = 0; i < size; i++) {
					Activity item = GlobalVariable.tempActivityList.get(i);
					if (item instanceof KnowledgeSummaryActivity) {
						item.finish();
					}
					if (item instanceof ShowHomeworkCheckActivity) {
						item.finish();
					}
				}
			}
			GlobalVariable.tempActivityList.clear();
			finish();

//			if ("checked_hw_tag".equals(SpUtil.getInstance().getCheckTag())) {
//				Intent intent = new Intent(HwReviewActivity.this, StuHomeWorkHallActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("packtype", 3);
//				startActivity(intent);
//				SpUtil.getInstance().setCheckTag("none");
//				finish();
//			} else {
//				Intent intent = new Intent(HwReviewActivity.this, ResponderActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				intent.putExtra("go_tag", "finish_homework");
//				startActivity(intent);
//				finish();
//			}

		} else {
			ToastUtils.show(result.getMsg());
		}

	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		if (TextUtils.isEmpty(msg)) {
			ToastUtils.show(msg);
		}
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		String msg = result.getMsg();
		if (msg != null) {
			ToastUtils.show(msg);
		}

	}*/


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

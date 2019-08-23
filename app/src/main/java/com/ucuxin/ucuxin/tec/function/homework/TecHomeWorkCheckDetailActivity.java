
package com.ucuxin.ucuxin.tec.function.homework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.edmodo.cropper.CropImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.HomeWorkAPI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.CameraFrameWithDel;
import com.ucuxin.ucuxin.tec.function.homework.adapter.TecHomeWorkDetailAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.model.TishiModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadEXPointModel;
import com.ucuxin.ucuxin.tec.function.homework.view.RightWrongPointView;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.BtnUtils;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.dialog.CustomDelCheckRightAndWrongDialog;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.viewpager.MyViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 作业检查详细页面
 *
 * @author: sky
 */
public class TecHomeWorkCheckDetailActivity extends BaseActivity
        implements OnClickListener, ViewPager.OnPageChangeListener/*, OnUploadListener*/ {

    public static final String TAG = TecHomeWorkCheckDetailActivity.class.getSimpleName();

    private RelativeLayout nextStepLayout;
    private HomeWorkCheckPointModel checkPointModel;
    private HomeWorkCheckPointModel homeWorkCheckPointModel;
    private TextView nextStepTV;

    public static boolean isChecking;

    public static final int SINGLE_POINT_REQUESTCODE = 1003;

    private static final int PIC_PUP_REQUSTECODE = 1002;

    private HomeWorkModel mHomeWorkModel;

    private int currentPosition;

    private ArrayList<View> dotLists;

    private LinearLayout dots_ll;

    private MyViewPager mViewPager;

    private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

    private RelativeLayout mBottomContainer;

    private String mCoordinate;

    private LinearLayout mCamareContainer;

    private TecHomeWorkDetailAdapter mAdapter;

    private int mLeft;// 接受点击的坐标
    private int ischongxin;//是否是重新打点

    private int mTop;// 接受点击的坐标
    // private ArrayList<HomeWorkCheckPointModel> mCheckPointList;

    private WelearnDialogBuilder mDialog;

    private static final int MSG_REFRESH_DATA = 1;
    private ArrayList<String> viewPagerList = new ArrayList<String>();

    // sky
    private boolean isFrist;

    private int baseExseqid;

    private List<HomeWorkSinglePoint> singlePointList;

    private int checkpointid;

    private String mytag = "";

    private LinearLayout layout_check_hw_tip = null;

    // 重新拍照答题
    private static final int CHONGXIN_PIC_PUP_REQUSTECODE = 1102;
    private HomeWorkCheckPointModel chongxinjiangjieHomeworkCheckPointModel;
    private int sub_type = 0;

    private boolean isAllowDadian = true;
    MyHandler mHandler = new MyHandler(this);

    class MyHandler extends Handler {
        WeakReference<TecHomeWorkCheckDetailActivity> mActivityReference;

        public MyHandler(TecHomeWorkCheckDetailActivity mActivityReference) {
            super();
            this.mActivityReference = new WeakReference<TecHomeWorkCheckDetailActivity>(mActivityReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final TecHomeWorkCheckDetailActivity mTecDetailActivity = mActivityReference.get();
            if (mTecDetailActivity != null) {
                switch (msg.what) {
                    case MSG_REFRESH_DATA:
                        mTecDetailActivity.refreshHomeWorkData();
                        break;
                }
            }

        }

    }

    @Override
    public void refresh() {
        refreshHomeWorkData();

    }

    private int msubtype;

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.homework_check_detail);
        registerFanHui();
        getrewardandpunish();
        initView();
        initListener();
        TecApplication.isShowgoodview = 0;
    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.homework_detail_title_text);
        nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView) findViewById(R.id.next_step_btn);
        nextStepTV.setText(R.string.teacher_home_work_single_check_done);

        dots_ll = (LinearLayout) this.findViewById(R.id.dots_ll);
        mViewPager = (MyViewPager) this.findViewById(R.id.detail_pager_homework);
        mBottomContainer = (RelativeLayout) this.findViewById(R.id.homework_detail_bottom_container_tec);
        mCamareContainer = (LinearLayout) this.findViewById(R.id.camare_container_tec);

        findViewById(R.id.homework_detail_text_tec).setVisibility(View.VISIBLE);
        mViewPager.setOffscreenPageLimit(8);

        // 第一次批改作业引导
        layout_check_hw_tip = (LinearLayout) this.findViewById(R.id.layout_check_hw_tip);
        ImageView iv_check_hw_tip = (ImageView) this.findViewById(R.id.iv_check_hw_tip);
        if (SharePerfenceUtil.getInstance().isFirstTip()) {
            layout_check_hw_tip.setVisibility(View.VISIBLE);
            SharePerfenceUtil.getInstance().setFirstTip();
        } else {
            layout_check_hw_tip.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        if (intent != null) {
            mytag = SpUtil.getInstance().getCheckTag();

            currentPosition = intent.getIntExtra("position", 0);
            mHomeWorkModel = (HomeWorkModel) intent.getSerializableExtra(HomeWorkModel.TAG);
            SharePerfenceUtil.getInstance().putInt("mHomeWorkModeltaskid", mHomeWorkModel.getTaskid());
            if (mHomeWorkModel == null) {
                ToastUtils.show("内存不足,请清理系统多余进程后再重新操作");
                return;
            }
            int state = mHomeWorkModel.getState();
            switch (state) {
                case StateOfHomeWork.ADOPTED:// 已采纳
                case StateOfHomeWork.ARBITRATED:// 仲裁完成
                case StateOfHomeWork.ASKING:// 抢题中
                case StateOfHomeWork.REFUSE:// 已拒绝
                case StateOfHomeWork.ARBITRATE:// 仲裁中
                case StateOfHomeWork.REPORT:// 已举报
                case StateOfHomeWork.DELETE:// 已删除
                case StateOfHomeWork.APPENDASK:// 追问中
                case StateOfHomeWork.ANSWERED:// 已回答
                    nextStepTV.setVisibility(View.VISIBLE);
                    nextStepTV.setText(R.string.teacher_home_work_single_check_append_done);
                    nextStepLayout.setOnClickListener(this);
                    isChecking = true;
                    break;
                case StateOfHomeWork.ANSWERING:// 答题中
                    nextStepTV.setVisibility(View.VISIBLE);
                    nextStepLayout.setOnClickListener(this);
                    isChecking = true;
                    break;
                default:
                    break;
            }
            TecApplication.gradeid = mHomeWorkModel.getGradeid();
            TecApplication.subjectid = mHomeWorkModel.getSubjectid();
            mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
            for (StuPublishHomeWorkPageModel sphp : mHomeWorkPageModelList) {
                viewPagerList.add(sphp.getImgpath());
            }
            if (null != mHomeWorkPageModelList) {
                initDot(mHomeWorkPageModelList.size(), currentPosition);
                mAdapter = new TecHomeWorkDetailAdapter(getSupportFragmentManager(), mHomeWorkPageModelList,
                        viewPagerList, isAllowDadian);
                mViewPager.setAdapter(mAdapter);
            }
            mViewPager.setOnPageChangeListener(this);
            mViewPager.setCurrentItem(currentPosition, false);// 加上fasle表示切换时不出现平滑效果
        } else {
            ToastUtils.show("内存不足,请清理系统多余进程后再重新操作");
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_REFRESH_DATA), 50);

        // 弹出提示框start sky
        // if ("showing".equals(TecApplication.isShowDialog)) {
        // final CustomTipDialogWithOneButton tipDialog = new
        // CustomTipDialogWithOneButton(this, "温馨提示",
        // "请在批改过程中记下本次作业设计的最重要的5个知识点,以便下一步填写!", "确定");
        // final Button positiveBtn = tipDialog.getPositiveButton();
        // final Button negativeBtn = tipDialog.getNegativeButton();
        // tipDialog.setOnPositiveListener(new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // positiveBtn.setTextColor(Color.parseColor("#28b9b6"));
        // tipDialog.dismiss();
        //
        // }
        // });
        // tipDialog.show();
        // TecApplication.isShowDialog = "unshowing";
        // }

        // 弹出提示框end

    }
    public void getrewardandpunish() {
        if (TecApplication.gradeid != 0 | TecApplication.subjectid != 0) {
            JSONObject data = new JSONObject();
            try {
                data.put("gradeid", TecApplication.gradeid);
                data.put("subjectid", TecApplication.subjectid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpHelper.post(this, "teacher", "rewardandpunish", data, new HttpListener() {

                @Override
                public void onFail(int code,String errMsg) {

                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        if (!TextUtils.isEmpty(dataJson)) {
                            TecApplication.gradeid = 0;
                            TecApplication.subjectid = 0;
                            TishiModel tishiModel = JSON.parseObject(dataJson, TishiModel.class);
                            if (tishiModel != null) {
                                SharePerfenceUtil.getInstance().putString("viewreward", tishiModel.getViewreward());
                                SharePerfenceUtil.getInstance().putString("viewpunish", tishiModel.getViewpunish());
                                SharePerfenceUtil.getInstance().putString("processreward",
                                        tishiModel.getProcessreward());
                                SharePerfenceUtil.getInstance().putString("processpunish",
                                        tishiModel.getProcesspunish());
                                SharePerfenceUtil.getInstance().putString("conclusionreward",
                                        tishiModel.getConclusionreward());
                                SharePerfenceUtil.getInstance().putString("conclusionpunish",
                                        tishiModel.getConclusionpunish());

                                SharePerfenceUtil.getInstance().putString("remindtext", tishiModel.getRemindtext());
                                SharePerfenceUtil.getInstance().putString("voiceremind", tishiModel.getVoiceremind());
                            }

                        }

                    }
                }
            });
        }
    }
    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.wrong_detail_bottom_btn).setOnClickListener(this);
        findViewById(R.id.right_detail_bottom_btn).setOnClickListener(this);
        findViewById(R.id.camare_btn_tec_detail).setOnClickListener(this);
        findViewById(R.id.cancel_photo_btn_stu_detail).setOnClickListener(this);
        layout_check_hw_tip.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalVariable.mViewPager = mViewPager;
    }

    // 初始化点
    private void initDot(int size, int defalutPosition) {
        dotLists = new ArrayList<View>();
        dots_ll.removeAllViews();
        for (int i = 0; i < size; i++) {
            // 设置点的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(this, 6),
                    DisplayUtils.dip2px(this, 6));
            // 设置点的间距
            params.setMargins(7, 0, 7, 0);
            // 初始化点的对象
            View m = new View(this);
            // 把点的宽高设置到view里面
            m.setLayoutParams(params);
            if (i == defalutPosition) {
                // 默认情况下，首先会调用第一个点。就必须展示选中的点
                m.setBackgroundResource(R.drawable.dot_focus);
            } else {
                // 其他的点都是默认的。
                m.setBackgroundResource(R.drawable.dot_normal);
            }
            // 把所有的点装载进集合
            dotLists.add(m);
            // 现在的点进入到了布局里面
            dots_ll.addView(m);
        }
    }

    private void selectDot(int postion) {
        for (View dot : dotLists) {
            dot.setBackgroundResource(R.drawable.dot_normal);
        }
        dotLists.get(postion).setBackgroundResource(R.drawable.dot_focus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                goPreview();
                break;
            case R.id.next_setp_layout:// 提交
                boolean allowSubmit = false;
                for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                    if (pageModel == null) {
                        continue;
                    }
                    ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
                    if (checkpointlist != null && checkpointlist.size() > 0) {
                        allowSubmit = true;
                    }
                }
                if (allowSubmit) {
                    showCheckHomeWorkFinishDialog();
                } else {
                    ToastUtils.show("请先检查学生的作业");
                }

                break;
            case R.id.wrong_detail_bottom_btn:// 答案错误
                showCamareContainer2();
                break;
            case R.id.right_detail_bottom_btn:// 答案正确
                try {
                    hideAddPointBottomContainer();
                    if (mAdapter != null) {
                        mAdapter.removeFrameView(currentPosition);
                    }
                    ArrayList<StuPublishHomeWorkPageModel> pagelist = mHomeWorkModel.getPagelist();
                    if (pagelist != null && pagelist.size() > currentPosition) {
                        StuPublishHomeWorkPageModel stuPublishHomeWorkPageModel = pagelist.get(currentPosition);
                        if (stuPublishHomeWorkPageModel != null) {
                            String imgpath = stuPublishHomeWorkPageModel.getImgpath();
                            goIntoSinglePoint(1, imgpath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.camare_btn_tec_detail:// 点击弹出选择框
                hideCamareContainer();
                hideAddPointBottomContainer();
                if (mAdapter != null) {
                    mAdapter.removeFrameView(currentPosition);
                }
                startActivityForResult(new Intent(this, SelectPicPopupWindow.class), PIC_PUP_REQUSTECODE);
                break;
            case R.id.cancel_photo_btn_stu_detail:
                if (mCamareContainer != null) {
                    mCamareContainer.setVisibility(View.GONE);
                }
                break;
            case R.id.layout_check_hw_tip:
                layout_check_hw_tip.setVisibility(View.GONE);
                break;
        }

    }

    private void goIntoSinglePoint(int isRight, String imgpath) {
        boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
        ArrayList<HomeWorkCheckPointModel> checkpointlist = null;
        StuPublishHomeWorkPageModel pageModel = null;
        ArrayList<StuPublishHomeWorkPageModel> pagelist = null;
        if (isWrong) {
            uMengEvent("homework_checkwrong");
        } else {
            uMengEvent("homework_checkright");
        }

        if (mHomeWorkModel != null) {
            pagelist = mHomeWorkModel.getPagelist();
            pageModel = pagelist.get(currentPosition);
            checkpointlist = pageModel.getCheckpointlist();
        }
        homeWorkCheckPointModel = new HomeWorkCheckPointModel();

        homeWorkCheckPointModel
                .setGrabuserid(WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo().getUserid());
        homeWorkCheckPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
        Bundle data = new Bundle();
        data.putSerializable(HomeWorkCheckPointModel.TAG, homeWorkCheckPointModel);
        if (checkPointModel != null) {
            homeWorkCheckPointModel.setCoordinate(checkPointModel.getCoordinate());
            homeWorkCheckPointModel.setCpseqid(checkPointModel.getCpseqid());

            homeWorkCheckPointModel.setId(checkPointModel.getId());
            homeWorkCheckPointModel.setComplainttype(2);
            homeWorkCheckPointModel.setShowcomplainttype(checkPointModel.getShowcomplainttype());
            data.putInt("sub_type", 2);

        } else {
            int cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);
            homeWorkCheckPointModel.setCoordinate(mCoordinate);
            homeWorkCheckPointModel.setCpseqid(cpseqid);
        }
        homeWorkCheckPointModel.setImgpath(imgpath);
        homeWorkCheckPointModel.setIsright(isRight);
        homeWorkCheckPointModel.setLocal(isWrong);
        if (pagelist != null) {
            homeWorkCheckPointModel.setPicid(pageModel.getId());
        }

        data.putBoolean("frist", true);
        data.putInt("left", mLeft);
        data.putInt("top", mTop);
        data.putInt("mCurrentItem", mViewPager.getCurrentItem());
        data.putStringArrayList("viewPagerList", viewPagerList);
        data.putString("frompaizhao", "frompaizhao");
        if (mHomeWorkModel != null) {
            data.putInt("gradeid", mHomeWorkModel.getGradeid());
            data.putInt("subjectid", mHomeWorkModel.getSubjectid());
        }
        checkPointModel = null;
        IntentManager.goToTecSingleCheckActivity(this, data, false, SINGLE_POINT_REQUESTCODE);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PIC_PUP_REQUSTECODE:// 答题错误拍照之后的回调
                ischongxin = PIC_PUP_REQUSTECODE;
                if (resultCode == RESULT_OK && null != data) {
                    String path = data.getStringExtra("path");
                    // LogUtils.i(TAG, path);
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.show("path is null");
                        return;
                    }

                    if ("btn_cancle".equals(path)) {
                        nextStepTV.setVisibility(View.VISIBLE);
                    } else {
                        boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
                        path = autoFixOrientation(path, isFromPhotoList, this, null);
                        if (!TextUtils.isEmpty(path)) {
                            goIntoSinglePoint(0, path);
                        }
                    }

                }
                break;
            case SINGLE_POINT_REQUESTCODE:// 单页打点返回或者完成之后的回调
                if (resultCode == RESULT_OK && null != data) {
                    boolean isSubmit = data.getBooleanExtra("isSubmit", false);
                    if (isSubmit) {
                        nextStepTV.setVisibility(View.VISIBLE);
//					if(homeWorkCheckPointModel!=null){
//						ArrayList<HomeWorkCheckPointModel> checkpointlist=	mHomeWorkPageModelList.get(currentPosition).getCheckpointlist();
//						if(checkpointlist!=null){
//							checkpointlist.add(homeWorkCheckPointModel);
//						}else{
//							checkpointlist =new ArrayList<HomeWorkCheckPointModel>();
//							checkpointlist.add(homeWorkCheckPointModel);
//							mHomeWorkPageModelList.get(currentPosition).setCheckpointlist(checkpointlist);
//						}
//
//						mAdapter.setAllPageData(mHomeWorkPageModelList, mHomeWorkModel.getState(), mHomeWorkModel.getSubjectid());
//						mAdapter.notifyDataSetChanged();
//						mViewPager.setCurrentItem(currentPosition, false);
//					}else{
                        if (ischongxin == PIC_PUP_REQUSTECODE) {
                            TecApplication.isShowgoodview = 1;
                        } else {
                            TecApplication.isShowgoodview = 0;

                        }
                        refreshHomeWorkData();
//					}
                        // refreshCurrentPicData();
                        LogUtils.e("refreshCurrentPicData", "refreshCurrentPicData  exec");

                    }
                } else if (resultCode == 1110 && null != data) {

                }
                break;
            case CHONGXIN_PIC_PUP_REQUSTECODE:// 答题错误拍照之后的回调
                ischongxin = CHONGXIN_PIC_PUP_REQUSTECODE;
                if (resultCode == RESULT_OK && null != data) {
                    String path = data.getStringExtra("path");
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.show("path is null");
                        return;
                    }

                    if ("btn_cancle".equals(path)) {
                        nextStepTV.setVisibility(View.VISIBLE);
                    } else {
                        boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
                        path = autoFixOrientation(path, isFromPhotoList, this, null);
                        if (!TextUtils.isEmpty(path)) {
                            gochongxinIntoSinglePoint(0, path);
                        }
                    }

                }
                break;

            default:
                break;
        }

    }

    /**
     * 刷新
     */
    private void refreshHomeWorkData() {
        JSONObject data = new JSONObject();
        int taskid = SharePerfenceUtil.getInstance().getInt("mHomeWorkModeltaskid", 0);
        if (taskid == 0) {
            ToastUtils.show("taskid不存在");
        }
        mHomeWorkModel = null;
        mHomeWorkPageModelList = null;
        try {
            data.put("taskid", taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        showDialog("数据加载中...");

        OkHttpHelper.post(this, "homework", "getone", data, new HttpListener() {
            @Override
            public void onFail(int code, String errMsg) {
                closeDialog();
            }

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                closeDialog();
                if (code == 0) {
                    // LogUtils.e("----->", aa++ +"" );
                    try {
                        // mHomeWorkModel = new Gson().fromJson(dataJson,
                        // HomeWorkModel.class);
                        mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
                        SharePerfenceUtil.getInstance().putInt("mHomeWorkModeltaskid", mHomeWorkModel.getTaskid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mHomeWorkModel != null) {
                        TecApplication.gradeid = mHomeWorkModel.getGradeid();
                        TecApplication.subjectid = mHomeWorkModel.getSubjectid();
                        mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
                        int state = mHomeWorkModel.getState();
                        if (state == StateOfHomeWork.REFUSE || state == StateOfHomeWork.ADOPTED) {
                            // Bundle data = new Bundle();
                            // data.putInt("taskid",
                            // mHomeWorkModel.getTaskid());
                            // data.putInt("position", currentPosition);
                            // IntentManager.goToStuHomeWorkDetailActivity(TecHomeWorkCheckDetailActivity.this,
                            // data,
                            // true);
                            nextStepLayout.setVisibility(View.GONE);
                            findViewById(R.id.homework_detail_text_tec).setVisibility(View.GONE);
                            if (mHomeWorkPageModelList != null) {
                                for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                                    ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
                                    if (checkpointlist != null) {
                                        for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                            checkPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
                                            checkPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());

                                        }
                                    }

                                    if (viewPagerList != null && viewPagerList.size() < mHomeWorkPageModelList.size()) {
                                        viewPagerList.add(pageModel.getImgpath());
                                    }

                                }
                                isAllowDadian = false;
                                nextStepLayout.setVisibility(View.GONE);
                                mAdapter = new TecHomeWorkDetailAdapter(getSupportFragmentManager(),
                                        mHomeWorkPageModelList, viewPagerList, isAllowDadian);
                                mViewPager.setAdapter(mAdapter);
                                mAdapter.setAllPageData(mHomeWorkPageModelList, state, mHomeWorkModel.getSubjectid());
                                mViewPager.setCurrentItem(currentPosition, false);
                            }

                        } else {

                            if (mHomeWorkPageModelList != null) {
                                for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                                    ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
                                    if (checkpointlist != null) {
                                        for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                            checkPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
                                            checkPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());
                                        }
                                    }

                                    if (viewPagerList != null && viewPagerList.size() < mHomeWorkPageModelList.size()) {
                                        viewPagerList.add(pageModel.getImgpath());
                                    }
                                }
                                if (mHomeWorkModel.getGrabuserid() == WLDBHelper.getInstance().getWeLearnDB()
                                        .queryCurrentUserInfo().getUserid()) {
                                    isAllowDadian = true;
                                    nextStepLayout.setVisibility(View.VISIBLE);
                                } else {
                                    nextStepLayout.setVisibility(View.GONE);
                                    isAllowDadian = false;
                                }

                                mAdapter = new TecHomeWorkDetailAdapter(getSupportFragmentManager(),
                                        mHomeWorkPageModelList, viewPagerList, isAllowDadian);
                                mViewPager.setAdapter(mAdapter);
                                // mAdapter.setAllDadian(isAllowDadian);
                                // mAdapter.notifyDataSetChanged();
                                mAdapter.setAllPageData(mHomeWorkPageModelList, state, mHomeWorkModel.getSubjectid());
                                mViewPager.setCurrentItem(currentPosition, false);
                            }
                        }
                    }

                } else {
                    ToastUtils.show(errMsg);
                }
            }
        });
    }

    /**
     * 刷新当前页面
     *
     * @author: sky void
     */
    private void refreshCurrentPicData() {
        JSONObject data = new JSONObject();
        try {
            data.put("picid", mHomeWorkPageModelList.get(currentPosition).getId());

            OkHttpHelper.post(this, "homework", "pageone", data, new HttpListener() {

                @Override
                public void onFail(int code, String errMsg) {

                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        ArrayList<HomeWorkCheckPointModel> checkpointlist = null;
                        try {

                            // checkpointlist = new Gson().fromJson(dataJson,
                            // new
                            // TypeToken<ArrayList<HomeWorkCheckPointModel>>() {
                            // }.getType());
                            checkpointlist = (ArrayList<HomeWorkCheckPointModel>) JSON.parseArray(dataJson,
                                    HomeWorkCheckPointModel.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (checkpointlist != null && null != mHomeWorkPageModelList
                                && currentPosition < mHomeWorkPageModelList.size()) {
                            StuPublishHomeWorkPageModel pageModel = mHomeWorkPageModelList.get(currentPosition);
                            pageModel.setCheckpointlist(checkpointlist);
                            for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                checkPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
                                checkPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());
                            }
                            if (mHomeWorkModel.getGrabuserid() == WLDBHelper.getInstance().getWeLearnDB()
                                    .queryCurrentUserInfo().getUserid()) {
                                isAllowDadian = true;
                                nextStepLayout.setVisibility(View.VISIBLE);
                            } else {
                                nextStepLayout.setVisibility(View.GONE);
                                isAllowDadian = false;
                            }
                            mAdapter = new TecHomeWorkDetailAdapter(getSupportFragmentManager(), mHomeWorkPageModelList,
                                    viewPagerList, isAllowDadian);

                            mViewPager.setAdapter(mAdapter);
                            mAdapter.setPageData(currentPosition, pageModel, mHomeWorkModel.getState(),
                                    mHomeWorkModel.getSubjectid());
                            mViewPager.setCurrentItem(currentPosition, false);
                            nextStepTV.setVisibility(View.VISIBLE);
                            // showAddPointBottomContainer();
                        }
                    } else {
                        ToastUtils.show(errMsg);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String autoFixOrientation(String path, boolean isFromPhotoList, Activity activity,
                                            CropImageView imageView) {// ,
        int deg = 0;
        Matrix m = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(path);

            int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (rotateValue) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    deg = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    deg = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    deg = 270;
                    break;
                default:
                    deg = 0;
                    break;
            }
            m.preRotate(deg);
        } catch (Exception ee) {
            LogUtils.d("catch img error", "return");

        }

        try {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int outWidth = opts.outWidth;
            int outHeight = opts.outHeight;
            // int ssize = 1;
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int widthPixels = metrics.widthPixels;
            int heightPixels = metrics.heightPixels;
            int ssize = 1;
            if (deg == 90 || deg == 270) {
                int temp = outWidth;
                outWidth = outHeight;
                outHeight = temp;
            }

            ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels
                    : outWidth / widthPixels;
            if (ssize <= 0) {
                ssize = 1;

                float scaleWidth = 1.0f, scaleHeight = 1.0f;

                scaleWidth = (float) widthPixels / (float) outWidth;
                scaleHeight = (float) heightPixels / (float) outHeight;
                if (scaleWidth < scaleHeight) {
                    scaleHeight = scaleWidth;
                } else {
                    scaleWidth = scaleHeight;
                }
                m.postScale(scaleWidth, scaleHeight);

            }
            // Options opts = new Options();
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = ssize;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
            if (null == bitmap) {
                ToastUtils.show(R.string.text_image_not_exists);
                if (activity instanceof CropImageActivity) {
                    activity.finish();
                }
                return path;
            }
            bitmap = WeLearnImageUtil.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (isFromPhotoList) {
                String absolutePath = activity.getCacheDir().getAbsolutePath();
                path = absolutePath + File.separator + "publish.png";
            }
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            WeLearnImageUtil.saveFile(path, bitmap);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void goPreview() {
        boolean allowSubmit = false;
        if (mHomeWorkPageModelList != null) {
            for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                if (null == pageModel) {
                    continue;
                }
                ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
                if (checkpointlist != null && checkpointlist.size() > 0) {
                    allowSubmit = true;
                }
            }
        }
        Intent data = new Intent();
        data.putExtra("allowSubmit", allowSubmit);
        setResult(TecHomeWorkCheckDetailActivity.RESULT_OK, data);

        // if (null != mAdapter) {
        // mAdapter.removeAllRightWrongPoint();
        // }

        finish();
    }

    // @Override
    public void showAddPointBottomContainer(String tag, String coordinate, int left, int top, int subtype) {
        mBottomContainer.setVisibility(View.VISIBLE);
        this.mCoordinate = coordinate;
        this.mLeft = left;
        this.mTop = top;
        this.msubtype = subtype;
        // sky
        if ("hw_check_detail".equals(tag)) {
            mBottomContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void hideAddPointBottomContainer() {
        if (mCamareContainer != null) {
            mBottomContainer.setVisibility(View.GONE);
        }
    }

    public void showAddPointBottomContainer() {
        if (mBottomContainer != null) {
            mBottomContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showCamareContainer(HomeWorkCheckPointModel checkPointModel) {
        if (mCamareContainer != null) {
            mCamareContainer.setVisibility(View.VISIBLE);
        }
        this.checkPointModel = checkPointModel;
    }

    public void showCamareContainer2() {
        if (mCamareContainer != null) {
            mCamareContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideCamareContainer() {
        mCamareContainer.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        int camareVisivility = mCamareContainer.getVisibility();
        int bottomVisivility = mBottomContainer.getVisibility();
        if (camareVisivility == View.VISIBLE) {
            hideCamareContainer();
        } else if (bottomVisivility == View.VISIBLE) {
            mAdapter.removeFrameView(currentPosition);
            hideAddPointBottomContainer();
        } else {
            goPreview();
        }
    }

    private void showCheckHomeWorkFinishDialog() {
        if (null == mDialog) {
            mDialog = WelearnDialogBuilder.getDialog(TecHomeWorkCheckDetailActivity.this);
        }
        mDialog.withMessage(R.string.teacher_home_work_all_check_confirm).setOkButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mDialog.dismiss();
                    submitHomeWorkCheck();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mDialog.show();

    }

    private void submitHomeWorkCheck() {
        if ("checked_hw_tag".equals(mytag)) {// 老师已抢答的作业
            if (getString(R.string.teacher_home_work_single_check_append_done)
                    .equals(nextStepTV.getText().toString().trim())) {
                ToastUtils.show("上传完成");
                finish();
                return;
            }
            // 跳转下一个页面
//            Intent intent = new Intent();
//            intent.setClass(TecHomeWorkCheckDetailActivity.this, KnowledgeSummaryActivity.class);
//            intent.putExtra("mHomeWorkModel", mHomeWorkModel);
//            startActivity(intent);
//            finish();

        } else {// 抢答中
            if (BtnUtils.isFastClick()) {
                return;
            }

            showDialog("请稍后");
            JSONObject data = new JSONObject();
            try {
                data.put("taskid", mHomeWorkModel.getTaskid());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OkHttpHelper.post(this, "teacher", "homeworkanswerfinish", data, new HttpListener() {

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    closeDialog();
                    if (code == 0) {
                        uMengEvent("homework_submit");
                        ToastUtils.show("提交成功");
                        // Intent data = new Intent();
                        // data.putExtra("issubmited", true);
                        // TecHomeWorkCheckDetailActivity.this
                        // .setResult(TecHomeWorkCheckDetailActivity.RESULT_OK,
                        // data);
                        // finish();
                        // sky 跳转到知识点总结的页面 原来答题答完之后跳转到抢题界面
                        if (getString(R.string.teacher_home_work_single_check_append_done)
                                .equals(nextStepTV.getText().toString().trim())) {
                            ToastUtils.show("你的作业已经上传完成过");
                            return;
                        }
                        // 发送广播通知前面的页面
                        Intent intent = new Intent();
                        intent.setAction("com.ucuxin.ucuxin.opera");
                        intent.putExtra("issubmited", true);
                        sendBroadcast(intent);
                        // 跳转下一个页面
                        intent.setClass(TecHomeWorkCheckDetailActivity.this, KnowledgeSummaryActivity.class);
                        intent.putExtra("mHomeWorkModel", mHomeWorkModel);
                        startActivity(intent);
                        finish();

                    } else {
                        ToastUtils.show(errMsg);
                    }

                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    closeDialog();

                }
            });

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int postion) {
        if (postion != currentPosition) {
            hideAddPointBottomContainer();
            hideCamareContainer();
            mAdapter.removeFrameView(currentPosition);
        }
        currentPosition = postion;
        selectDot(postion);
        // refreshCurrentPicData();
    }

    public void showRightOrWrong(String string, CameraFrameWithDel frameDelView, int currentX, int currentY, int left,
                                 int top) {

        // 弹出对于错的菜单 sky
        nextStepTV.setVisibility(View.VISIBLE);
        View popuView = View.inflate(this, R.layout.popu_item_menu, null);
        Button btn_right = (Button) popuView.findViewById(R.id.btn_right);
        Button btn_wrong = (Button) popuView.findViewById(R.id.btn_wrong);
        final PopupWindow pw = new PopupWindow(popuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接显示对的标记
                try {
                    hideAddPointBottomContainer();
                    if (mAdapter != null) {
                        mAdapter.removeFrameView(currentPosition);
                    }
                    ArrayList<StuPublishHomeWorkPageModel> pagelist = mHomeWorkModel.getPagelist();
                    if (pagelist != null && pagelist.size() > currentPosition) {
                        StuPublishHomeWorkPageModel stuPublishHomeWorkPageModel = pagelist.get(currentPosition);
                        if (stuPublishHomeWorkPageModel != null) {
                            String imgpath = stuPublishHomeWorkPageModel.getImgpath();
                            // goIntoSinglePoint(1, imgpath);
                            pw.dismiss();
                            if (!NetworkUtils.getInstance().isInternetConnected(TecHomeWorkCheckDetailActivity.this)) {
                                ToastUtils.show("网络异常,不能正常打点");
                                return;
                            }

                            submitSinglePoint(1, imgpath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 显示正确的答案图表
                // setRightWrongPoint

            }
        });

        btn_wrong.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直接显示错误标记
                nextStepTV.setVisibility(View.GONE);
                showCamareContainer2();
                mAdapter.replaceFrameView(currentPosition);
                pw.dismiss();

            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        pw.setBackgroundDrawable(new ColorDrawable(0));
        // 这里是位置显示方式,在屏幕的左侧
        // pw.showAtLocation(frameDelView, Gravity.LEFT, currentX +
        // left,currentY + top);

        // 设置好参数之后再show
        pw.showAsDropDown(frameDelView, currentX + left - 45, currentY + top);

    }

    protected void submitSinglePoint(int isRight, String imgpath) {
        singlePointList = new ArrayList<HomeWorkSinglePoint>();
        int size = singlePointList.size();

        StuPublishHomeWorkPageModel pageModel = mHomeWorkModel.getPagelist().get(currentPosition);
        ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
        // int cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() +
        // 1);
        // 取出cpseqid的值 取出最大的checkpoint 的id
        int cpseqid = 0;
        if (checkpointlist != null && checkpointlist.size() > 0) {
            List<Integer> cpseqidList = new ArrayList<Integer>();
            for (int i = 0; i < checkpointlist.size(); i++) {
                cpseqidList.add(checkpointlist.get(i).getCpseqid());
            }
            if (cpseqidList != null && cpseqidList.size() > 0) {
                cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);
            }

        } else {
            cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);
        }

        homeWorkCheckPointModel = new HomeWorkCheckPointModel();
        boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
        homeWorkCheckPointModel.setCoordinate(mCoordinate);
        homeWorkCheckPointModel.setImgpath(imgpath);
        homeWorkCheckPointModel.setIsright(isRight);
        homeWorkCheckPointModel.setLocal(isWrong);
        homeWorkCheckPointModel.setCpseqid(cpseqid);
        homeWorkCheckPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
        homeWorkCheckPointModel.setGrabuserid(mHomeWorkModel.getGrabuserid());
        homeWorkCheckPointModel.setPicid(pageModel.getId());
        if (checkpointlist == null) {
            checkpointlist = new ArrayList<HomeWorkCheckPointModel>();
        }
        checkpointlist.add(homeWorkCheckPointModel);

        UpLoadCheckPointModel submitModel = new UpLoadCheckPointModel();
        checkpointid = homeWorkCheckPointModel.getId();
        submitModel.setCheckpointid(checkpointid);
        int isright = homeWorkCheckPointModel.getIsright();
        submitModel.setIsright(isright);
        submitModel.setCoordinate(homeWorkCheckPointModel.getCoordinate());
        submitModel.setPicid(homeWorkCheckPointModel.getPicid());
        submitModel.setCpseqid(homeWorkCheckPointModel.getCpseqid());

        Map<String, List<File>> files = new HashMap<String, List<File>>();
        isFrist = true;
        showDialog("请稍后");
        List<File> sndFileList = new ArrayList<File>();
        ArrayList<UpLoadEXPointModel> upLoadExplainList = new ArrayList<UpLoadEXPointModel>();
        submitModel.setExplainlist(upLoadExplainList);
        UpLoadEXPointModel exPointModel;
        for (int i = 0; i < size; i++) {
            HomeWorkSinglePoint singlePoint = singlePointList.get(i);
            String sndpath = singlePoint.getSndpath();
            int explaintype = singlePoint.getExplaintype();
            if (!TextUtils.isEmpty(sndpath) && explaintype == GlobalContant.ANSWER_AUDIO) {
                sndFileList.add(new File(sndpath));
            }

            exPointModel = new UpLoadEXPointModel();
            exPointModel.setCoordinate(singlePoint.getCoordinate());
            exPointModel.setExplaintype(explaintype);
            exPointModel.setExplaintype(msubtype);

            int exseqid = baseExseqid + i + 1;
            exPointModel.setExseqid(exseqid);
            singlePoint.setExseqid(exseqid);
            String text = singlePoint.getText();
            if (text != null) {
                exPointModel.setText(text);
            }
            upLoadExplainList.add(exPointModel);
        }
        files.put("sndfile", sndFileList);

        try {
            JSONObject data = new JSONObject(JSON.toJSONString(submitModel));
            // JSONObject data = new JSONObject(new Gson().toJson(submitModel));
            // UploadUtil.upload(AppConfig.GO_URL + "teacher/homeworkanswerone",
            // RequestParamUtils.getParam(data), files,
            // TecHomeWorkCheckDetailActivity.this, true, 0);

            UploadUtil2.upload(AppConfig.GO_URL + "teacher/homeworkanswerone", RequestParamUtils.getMapParam(data),
                    files, new StringCallback() {

                        /**
                         * UI Thread
                         */
                        @Override
                        public void onBefore(Request request) {
                            super.onBefore(request);
                            // ToastUtils.show("onBefore");
                        }

                        /**
                         * UI Thread
                         */
                        @Override
                        public void onAfter() {
                            super.onAfter();
                            // ToastUtils.show("onAfter");

                        }

                        @Override
                        public void onResponse(String response) {
                            closeDialog();
                            int code = JsonUtils.getInt(response, "Code", -1);
                            String msg = JsonUtils.getString(response, "Msg", "");
                            int data = JsonUtils.getInt(response, "Data", 0);
                            if (code == 0) {
                                uMengEvent("homework_answer");
                                // ToastUtils.show("提交成功");
                                // 刷新界面数据
                                ArrayList<HomeWorkCheckPointModel> checkpointlist = mHomeWorkPageModelList.get(currentPosition).getCheckpointlist();
                                if (checkpointlist != null) {
                                    checkpointlist.get(checkpointlist.size() - 1).setId(data);

                                } else {
                                    checkpointlist = new ArrayList<HomeWorkCheckPointModel>();
                                    homeWorkCheckPointModel.setId(data);
                                    checkpointlist.add(homeWorkCheckPointModel);
                                    mHomeWorkPageModelList.get(currentPosition).setCheckpointlist(checkpointlist);
                                }
                                TecApplication.isShowgoodview = 1;
                                mAdapter.setAllPageData(mHomeWorkPageModelList, mHomeWorkModel.getState(), mHomeWorkModel.getSubjectid());
                                mAdapter.notifyDataSetChanged();
                                mViewPager.setCurrentItem(currentPosition, false);
                                //refreshHomeWorkData();
                            } else {
                                ToastUtils.show(msg);
                            }

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
                    }, true, 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

/*	@Override
    public void onUploadSuccess(UploadResult result, int index) {
		closeDialog();
		LogUtils.e("result-->", result.getData());
		LogUtils.e("result code-->", result.getCode() + "");
		if (result.getCode() == 0) {
			uMengEvent("homework_answer");
			// ToastUtils.show("提交成功");
			// 刷新界面数据
			refreshHomeWorkData();
		} else {
			ToastUtils.show(result.getMsg());
		}

	}

	@Override
	public void onUploadError(String msg, int index) {
		closeDialog();
		ToastUtils.show(msg);
	}

	@Override
	public void onUploadFail(UploadResult result, int index) {
		closeDialog();
		String msg = result.getMsg();
		if (msg != null) {
			ToastUtils.show(msg);
		}

	}*/

    CustomDelCheckRightAndWrongDialog delCheckRightWrongDialog = null;
    private HomeWorkCheckPointModel ss_checkPointModel = null;
    private RightWrongPointView iconSer = null;
    private RelativeLayout mPicContainer;

    /**
     * 删除单个对错点
     */
    public void delSingleCheckPoint(RelativeLayout PicContainer, RightWrongPointView iconSer,
                                    final HomeWorkCheckPointModel checkPointModel) {
        this.ss_checkPointModel = checkPointModel;
        this.iconSer = iconSer;
        this.mPicContainer = PicContainer;
        if (checkPointModel.getState() != 3) {
            delCheckRightWrongDialog = new CustomDelCheckRightAndWrongDialog(this, "提示", "确定要删除吗", "取消", "确定");
            delCheckRightWrongDialog.setOnPositiveListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeWorkAPI homeworkApi = new HomeWorkAPI();
                    homeworkApi.delSingleCheckPoint(requestQueue, checkPointModel.getId(),
                            TecHomeWorkCheckDetailActivity.this, RequestConstant.DEL_CHECK_POINT_CODE);

                }
            });

            delCheckRightWrongDialog.setOnNegativeListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    delCheckRightWrongDialog.dismiss();

                }
            });
            delCheckRightWrongDialog.show();
        }

    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.DEL_CHECK_POINT_CODE:// 删除单个对错点
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtils.getInt(datas, "Code", -1);
                    String msg = JsonUtils.getString(datas, "Msg", "");
                    if (code == 0) {
                        try {
                            delCheckRightWrongDialog.dismiss();
                            String dataJson = JsonUtils.getString(datas, "Data", "");
                            if (iconSer != null) {
                                // mAdapter.removeIndexRightWrongPoint(currentPosition,ss_checkPointModel);
                                // ViewGroup parent = (ViewGroup)
                                // iconSer.getParent();
                                // parent.removeView(iconSer);
                                StuPublishHomeWorkPageModel pageModel = mHomeWorkModel.getPagelist().get(currentPosition);
                                pageModel.getCheckpointlist().remove(ss_checkPointModel);
                                mPicContainer.removeView(iconSer);
                                mPicContainer.invalidate();
                            }
                            // 刷新界面数据
                            //refreshCurrentPicData();
                            refreshHomeWorkData();
                            // mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }
                break;

        }

    }

    // @Override
    // protected void onNewIntent(Intent intent) {
    // super.onNewIntent(intent);
    // Log.e("onnewIntent-->", "onNewIntent");
    // setIntent(intent);
    // String tag = getIntent().getStringExtra("chongxinjiangjie");
    // if ("chongxinjiangjie".equals(tag)) {
    // chongxinjiangjieHomeworkCheckPointModel = (HomeWorkCheckPointModel)
    // getIntent()
    // .getSerializableExtra("checkPointmodel");
    // sub_type = getIntent().getIntExtra("sub_type", 2);
    // // 弹出选择框
    // hideCamareContainer();
    // hideAddPointBottomContainer();
    // if (mAdapter != null) {
    // mAdapter.removeFrameView(currentPosition);
    // }
    // startActivityForResult(new Intent(this, SelectPicPopupWindow.class),
    // CHONGXIN_PIC_PUP_REQUSTECODE);
    // }
    //
    // }

    private void gochongxinIntoSinglePoint(int isRight, String imgpath) {
        boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
        ArrayList<HomeWorkCheckPointModel> checkpointlist = null;
        StuPublishHomeWorkPageModel pageModel = null;
        ArrayList<StuPublishHomeWorkPageModel> pagelist = null;
        if (isWrong) {
            uMengEvent("homework_checkwrong");
        } else {
            uMengEvent("homework_checkright");
        }
        int cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);
        homeWorkCheckPointModel = new HomeWorkCheckPointModel();
        if (mHomeWorkModel != null) {
            pagelist = mHomeWorkModel.getPagelist();
            pageModel = pagelist.get(currentPosition);
            checkpointlist = pageModel.getCheckpointlist();

        }

        homeWorkCheckPointModel
                .setGrabuserid(WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo().getUserid());
        homeWorkCheckPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
        homeWorkCheckPointModel.setCoordinate(chongxinjiangjieHomeworkCheckPointModel.getCoordinate());
        homeWorkCheckPointModel.setImgpath(imgpath);
        homeWorkCheckPointModel.setIsright(isRight);
        homeWorkCheckPointModel.setLocal(isWrong);
        homeWorkCheckPointModel.setCpseqid(chongxinjiangjieHomeworkCheckPointModel.getCpseqid());
        homeWorkCheckPointModel.setId(chongxinjiangjieHomeworkCheckPointModel.getId());
        homeWorkCheckPointModel.setComplainttype(chongxinjiangjieHomeworkCheckPointModel.getComplainttype());
        homeWorkCheckPointModel.setShowcomplainttype(chongxinjiangjieHomeworkCheckPointModel.getShowcomplainttype());
        if (pagelist != null) {
            homeWorkCheckPointModel.setPicid(pageModel.getId());
        }
        Bundle data = new Bundle();
        data.putInt("sub_type", sub_type);
        data.putSerializable(HomeWorkCheckPointModel.TAG, homeWorkCheckPointModel);
        data.putBoolean("frist", true);
        data.putInt("left", mLeft);
        data.putInt("top", mTop);
        data.putInt("mCurrentItem", mViewPager.getCurrentItem());
        data.putStringArrayList("viewPagerList", viewPagerList);
        data.putString("frompaizhao", "frompaizhao");
        if (mHomeWorkModel != null) {
            data.putInt("gradeid", mHomeWorkModel.getGradeid());
            data.putInt("subjectid", mHomeWorkModel.getSubjectid());
        }
        IntentManager.goToTecSingleCheckActivity(this, data, false, SINGLE_POINT_REQUESTCODE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // super.onSaveInstanceState(outState);
        // //将这一行注释掉，阻止activity保存fragment的状态
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void registerFanHui() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.fireeye.fanhui");
        registerReceiver(receiver, filter);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(android.content.Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.fireeye.fanhui".equals(action)) {
                String tag = intent.getStringExtra("chongxinjiangjie");
                if ("chongxinjiangjie".equals(tag)) {
                    chongxinjiangjieHomeworkCheckPointModel = (HomeWorkCheckPointModel) intent
                            .getSerializableExtra("checkPointmodel");
                    sub_type = getIntent().getIntExtra("sub_type", 2);
                    // 弹出选择框
                    hideCamareContainer();
                    hideAddPointBottomContainer();
                    if (mAdapter != null) {
                        mAdapter.removeFrameView(currentPosition);
                    }
                    startActivityForResult(new Intent(TecHomeWorkCheckDetailActivity.this, SelectPicPopupWindow.class),
                            CHONGXIN_PIC_PUP_REQUSTECODE);
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChecking = false;
        mHandler.removeCallbacksAndMessages(null);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        GlobalVariable.mViewPager = null;
    }
}


package com.ucuxin.ucuxin.tec.function.homework;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.CameraFrameWithDel;
import com.ucuxin.ucuxin.tec.function.homework.adapter.ShowHomeworkAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkSinglePoint;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.publishhw.UpLoadEXPointModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.viewpager.MyViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 作业检查的查看打点
 * 
 * @author: sky
 */
public class ShowHomeworkCheckActivity extends BaseActivity
        implements OnClickListener, ViewPager.OnPageChangeListener/*, OnUploadListener*/ {
    public static final String TAG = ShowHomeworkCheckActivity.class.getSimpleName();

    private RelativeLayout nextStepLayout;

    private TextView nextStepTV;

    public static boolean isChecking;

    private static final int SINGLE_POINT_REQUESTCODE = 1003;

    private static final int PIC_PUP_REQUSTECODE = 1002;

    private HomeWorkModel mHomeWorkModel;

    private int currentPosition;

    private List<View> dotList;

    private LinearLayout layout_dots;

    private MyViewPager mViewPager;

    private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

    private RelativeLayout mBottomContainer;

    private String mCoordinate;

    private LinearLayout mCamareContainer;

    private ShowHomeworkAdapter mAdapter;

    private int mLeft;// 接受点击的坐标

    private int mTop;// 接受点击的坐标
    // private ArrayList<HomeWorkCheckPointModel> mCheckPointList;

    private WelearnDialogBuilder mDialog;

    private static final int MSG_REFRESH_DATA = 1;

    // sky
    private boolean isFrist;

    private int baseExseqid;

    private List<HomeWorkSinglePoint> singlePointList;

    private int checkpointid;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_DATA:
                    int camareVisivility = mCamareContainer.getVisibility();
                    int bottomVisivility = mBottomContainer.getVisibility();
                    if (camareVisivility == View.VISIBLE) {
                        hideCamareContainer();
                    } else if (bottomVisivility == View.VISIBLE) {
                        mAdapter.removeFrameView(currentPosition);
                        hideAddPointBottomContainer();
                    } 
                    refreshHomeWorkData();
                    break;
            }
        }
    };

    @Override
    @SuppressLint("InlinedApi")
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.homework_check_detail);
        GlobalVariable.tempActivityList.add(this);
        initView();
        getExtraData();
        initListener();

    }

    @Override
    public void initView() {
        super.initView();
        setWelearnTitle(R.string.homework_detail_title_text);

        nextStepLayout = (RelativeLayout)findViewById(R.id.next_setp_layout);
        nextStepTV = (TextView)findViewById(R.id.next_step_btn);
        nextStepTV.setText(R.string.teacher_home_work_single_check_done);
        nextStepTV.setText("下一步");
        layout_dots = (LinearLayout)this.findViewById(R.id.dots_ll);
        mViewPager = (MyViewPager)this.findViewById(R.id.detail_pager_homework);
        mBottomContainer = (RelativeLayout)this
                .findViewById(R.id.homework_detail_bottom_container_tec);
        mCamareContainer = (LinearLayout)this.findViewById(R.id.camare_container_tec);

        findViewById(R.id.homework_detail_text_tec).setVisibility(View.GONE);
        mViewPager.setOffscreenPageLimit(8);

    }
    //

    public void getExtraData() {
        Intent intent = getIntent();
        if (intent != null) {
            currentPosition = intent.getIntExtra("position", 0);
            mHomeWorkModel = (HomeWorkModel)intent.getSerializableExtra(HomeWorkModel.TAG);
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
                    // 显示知识点啊
                    nextStepTV.setText("下一步");
                    nextStepTV.setVisibility(View.VISIBLE);
                    nextStepLayout.setOnClickListener(this);
                    break;
                case StateOfHomeWork.ANSWERING:// 答题中
                    nextStepTV.setVisibility(View.VISIBLE);
                    nextStepLayout.setOnClickListener(this);
                    isChecking = true;
                    break;
                default:
                    break;
            }

            mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
            if (null != mHomeWorkPageModelList) {
                initDot(mHomeWorkPageModelList.size(), currentPosition);
                mAdapter = new ShowHomeworkAdapter(getSupportFragmentManager(),
                        mHomeWorkPageModelList);
                mViewPager.setAdapter(mAdapter);
            }
            mViewPager.setOnPageChangeListener(this);
            mViewPager.setCurrentItem(currentPosition, false);// 加上fasle表示切换时不出现平滑效果
        }
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_REFRESH_DATA), 50);
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.wrong_detail_bottom_btn).setOnClickListener(this);
        findViewById(R.id.right_detail_bottom_btn).setOnClickListener(this);
        findViewById(R.id.camare_btn_tec_detail).setOnClickListener(this);
        findViewById(R.id.cancel_photo_btn_stu_detail).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalVariable.mViewPager = mViewPager;
    }

    // 初始化点
    private void initDot(int size, int defalutPosition) {
        dotList = new ArrayList<View>();
        layout_dots.removeAllViews();
        for (int i = 0; i < size; i++) {
            // 设置点的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DisplayUtils.dip2px(this, 6), DisplayUtils.dip2px(this, 6));
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
            dotList.add(m);
            // 现在的点进入到了布局里面
            layout_dots.addView(m);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout:// 返回
                goPreview();
                GlobalVariable.tempActivityList.clear();
                break;
            case R.id.next_setp_layout:// 提交
                Intent intent = new Intent(ShowHomeworkCheckActivity.this,
                        KnowledgeSummaryActivity.class);
                intent.putExtra("mHomeWorkModel", mHomeWorkModel);
                startActivity(intent);
                finish();
                break;
            case R.id.wrong_detail_bottom_btn:// 答案错误
                showCamareContainer();
                break;
            case R.id.right_detail_bottom_btn:// 答案正确
                try {
                    hideAddPointBottomContainer();
                    if (mAdapter != null) {
                        mAdapter.removeFrameView(currentPosition);
                    }
                    ArrayList<StuPublishHomeWorkPageModel> pagelist = mHomeWorkModel.getPagelist();
                    if (pagelist != null && pagelist.size() > currentPosition) {
                        StuPublishHomeWorkPageModel stuPublishHomeWorkPageModel = pagelist
                                .get(currentPosition);
                        if (stuPublishHomeWorkPageModel != null) {
                            String imgpath = stuPublishHomeWorkPageModel.getImgpath();
                            goIntoSinglePoint(1, imgpath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.camare_btn_tec_detail:
                hideCamareContainer();
                hideAddPointBottomContainer();
                if (mAdapter != null) {
                    mAdapter.removeFrameView(currentPosition);
                }
                // 弹出选择框
                startActivityForResult(new Intent(this, SelectPicPopupWindow.class),
                        PIC_PUP_REQUSTECODE);
                break;
            case R.id.cancel_photo_btn_stu_detail:
                if (mCamareContainer != null) {
                    mCamareContainer.setVisibility(View.GONE);
                }
                break;
        }

    }

    private void goIntoSinglePoint(int isRight, String imgpath) {
        boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
        if (isWrong) {
            uMengEvent("homework_checkwrong");
        } else {
            uMengEvent("homework_checkright");
        }
        StuPublishHomeWorkPageModel pageModel = mHomeWorkModel.getPagelist().get(currentPosition);
        ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel.getCheckpointlist();
        int cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);
        HomeWorkCheckPointModel homeWorkCheckPointModel = new HomeWorkCheckPointModel();
        homeWorkCheckPointModel.setCoordinate(mCoordinate);
        homeWorkCheckPointModel.setImgpath(imgpath);
        homeWorkCheckPointModel.setIsright(isRight);
        homeWorkCheckPointModel.setLocal(isWrong);
        homeWorkCheckPointModel.setCpseqid(cpseqid);
        homeWorkCheckPointModel.setPicid(pageModel.getId());
        Bundle data = new Bundle();
        data.putSerializable(HomeWorkCheckPointModel.TAG, homeWorkCheckPointModel);
        data.putBoolean("frist", true);
        data.putInt("left", mLeft);
        data.putInt("top", mTop);
        data.putInt("gradeid", mHomeWorkModel.getGradeid());
        data.putInt("subjectid", mHomeWorkModel.getSubjectid());

        IntentManager.goToTecSingleCheckActivity(this, data, false, SINGLE_POINT_REQUESTCODE);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PIC_PUP_REQUSTECODE:// 答题错误拍照之后的回调
                if (resultCode == RESULT_OK && null != data) {
                    String path = data.getStringExtra("path");
                    // LogUtils.i(TAG, path);
                    if (TextUtils.isEmpty(path)) {
                        ToastUtils.show("path is null");
                        return;
                    }

                    boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
                    path = autoFixOrientation(path, isFromPhotoList, this, null);
                    if (!TextUtils.isEmpty(path)) {
                        goIntoSinglePoint(0, path);
                    }
                }
                break;
            case SINGLE_POINT_REQUESTCODE:// 单页打点返回或者完成之后的回调
                if (resultCode == RESULT_OK && null != data) {
                    boolean isSubmit = data.getBooleanExtra("isSubmit", false);
                    if (isSubmit) {
                        refreshCurrentPicData();
                    }
                }
                break;

            default:
                break;
        }

    }

    private void refreshHomeWorkData() {
        JSONObject data = new JSONObject();
        int taskid = mHomeWorkModel.getTaskid();
        mHomeWorkModel = null;
        mHomeWorkPageModelList = null;
        try {
            data.put("taskid", taskid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpHelper.post(this, "homework","getone", data, new HttpListener() {
            @Override
            public void onFail(int code,String errMsg) {
            }

            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                if (code == 0) {
                    // LogUtils.e("----->", aa++ +"" );
                    try {
                        mHomeWorkModel = new Gson().fromJson(dataJson,
                                HomeWorkModel.class);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    if (mHomeWorkModel != null) {
                        mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
                        int state = mHomeWorkModel.getState();
                        if (state == StateOfHomeWork.REFUSE || state == StateOfHomeWork.ADOPTED) {
                            Bundle data = new Bundle();
                            data.putInt("taskid", mHomeWorkModel.getTaskid());
                            data.putInt("position", currentPosition);
                            IntentManager.goToStuHomeWorkDetailActivity(
                                    ShowHomeworkCheckActivity.this, data, true);
                        } else {
                            if (mHomeWorkPageModelList != null) {
                                for (StuPublishHomeWorkPageModel pageModel : mHomeWorkPageModelList) {
                                    ArrayList<HomeWorkCheckPointModel> checkpointlist = pageModel
                                            .getCheckpointlist();
                                    if (checkpointlist != null) {
                                        for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                            checkPointModel
                                                    .setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
                                        }
                                    }
                                }
                                mAdapter.setAllPageData(mHomeWorkPageModelList,state,mHomeWorkModel.getSubjectid());
                                if (mAdapter != null) {
                                    mAdapter.removeFrameView(currentPosition);
                                }
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

            OkHttpHelper.post(this, "teacher","pageone", data, new HttpListener() {

                @Override
                public void onFail(int code,String errMsg) {

                }

                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        ArrayList<HomeWorkCheckPointModel> checkpointlist = null;
                        try {
                            checkpointlist = new Gson().fromJson(dataJson,
                                    new TypeToken<ArrayList<HomeWorkCheckPointModel>>() {
                            }.getType());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (checkpointlist != null && null != mHomeWorkPageModelList
                                && currentPosition < mHomeWorkPageModelList.size()) {
                            StuPublishHomeWorkPageModel pageModel = mHomeWorkPageModelList
                                    .get(currentPosition);
                            pageModel.setCheckpointlist(checkpointlist);
                            for (HomeWorkCheckPointModel checkPointModel : checkpointlist) {
                                checkPointModel.setClickAction(GlobalContant.ROLE_ID_COLLEAGE);
                            }
                            if (null == mAdapter) {
                                mAdapter = new ShowHomeworkAdapter(getSupportFragmentManager(),
                                        mHomeWorkPageModelList);
                            }
                            mAdapter.setPageData(currentPosition, pageModel,mHomeWorkModel.getState(),mHomeWorkModel.getSubjectid());
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

            int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
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

                scaleWidth = (float)widthPixels / (float)outWidth;
                scaleHeight = (float)heightPixels / (float)outHeight;
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
            bitmap = WeLearnImageUtil.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
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

    /**
     * 返回操作
     * 
     * @author: sky void
     */
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
        // Intent data = new Intent();
        // data.putExtra("allowSubmit", allowSubmit);
        // setResult(TecHomeWorkCheckDetailActivity.RESULT_OK, data);

        // 发送广播通知前面的页面
        Intent intent = new Intent();
        intent.setAction("com.ucuxin.ucuxin.opera");
        intent.putExtra("issubmited", true);
        sendBroadcast(intent);

        // if (null != mAdapter) {
        // mAdapter.removeAllRightWrongPoint();
        // }

        finish();
    }

    // @Override
    public void showAddPointBottomContainer(String tag, String coordinate, int left, int top) {
        mBottomContainer.setVisibility(View.VISIBLE);
        this.mCoordinate = coordinate;
        this.mLeft = left;
        this.mTop = top;
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

    public void showCamareContainer() {
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
        GlobalVariable.tempActivityList.clear();
    }

    private void showCheckHomeWorkFinishDialog() {
        if (null == mDialog) {
            mDialog = WelearnDialogBuilder.getDialog(ShowHomeworkCheckActivity.this);
        }
        mDialog.withMessage(R.string.teacher_home_work_all_check_confirm)
                .setOkButtonClick(new View.OnClickListener() {
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
        showDialog("请稍后");
        JSONObject data = new JSONObject();
        try {
            data.put("taskid", mHomeWorkModel.getTaskid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpHelper.post(this, "teacher","answerfinish", data, new HttpListener() {

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
                    Intent intent = new Intent(ShowHomeworkCheckActivity.this,
                            KnowledgeSummaryActivity.class);
                    intent.putExtra("mHomeWorkModel", mHomeWorkModel);
                    startActivity(intent);
                    finish();

                } else {
                    ToastUtils.show(errMsg);
                }

            }

            @Override
            public void onFail(int HttpCode,String errMsg) {
                closeDialog();

            }
        });

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

    private void selectDot(int postion) {
        for (View dot : dotList) {
            dot.setBackgroundResource(R.drawable.dot_normal);
        }
        dotList.get(postion).setBackgroundResource(R.drawable.dot_focus);
    }

    /**
     * 展示错误或者正确的答案
     * 
     * @author: sky
     * @param string
     * @param frameDelView
     * @param currentX
     * @param currentY
     * @param left
     * @param top void
     */
    public void showRightOrWrong(String string, CameraFrameWithDel frameDelView, int currentX,
            int currentY, int left, int top) {

        // 弹出对于错的菜单 sky
        View popuView = View.inflate(this, R.layout.popu_item_menu, null);
        Button btn_right = (Button)popuView.findViewById(R.id.btn_right);
        Button btn_wrong = (Button)popuView.findViewById(R.id.btn_wrong);
        final PopupWindow pw = new PopupWindow(popuView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
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
                        StuPublishHomeWorkPageModel stuPublishHomeWorkPageModel = pagelist
                                .get(currentPosition);
                        if (stuPublishHomeWorkPageModel != null) {
                            String imgpath = stuPublishHomeWorkPageModel.getImgpath();
                            // goIntoSinglePoint(1, imgpath);
                            pw.dismiss();
                            if(!NetworkUtils.getInstance().isInternetConnected(ShowHomeworkCheckActivity.this)){
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
                showCamareContainer();
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
        int cpseqid = checkpointlist == null ? 1 : (checkpointlist.size() + 1);

        HomeWorkCheckPointModel homeWorkCheckPointModel = new HomeWorkCheckPointModel();
        boolean isWrong = isRight == GlobalContant.WRONG_HOMEWORK;
        homeWorkCheckPointModel.setCoordinate(mCoordinate);
        homeWorkCheckPointModel.setImgpath(imgpath);
        homeWorkCheckPointModel.setIsright(isRight);
        homeWorkCheckPointModel.setLocal(isWrong);
        homeWorkCheckPointModel.setCpseqid(cpseqid);
        homeWorkCheckPointModel.setPicid(pageModel.getId());

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
            JSONObject data = new JSONObject(new Gson().toJson(submitModel));
//            UploadUtil.upload(AppConfig.GO_URL + "teacher/homeworkanswerone",
//                    RequestParamUtils.getParam(data), files, ShowHomeworkCheckActivity.this, true,
//                    0);
            UploadUtil2.upload(AppConfig.GO_URL + "teacher/homeworkanswerone",
                    RequestParamUtils.getMapParam(data), files, new MyStringCallback(), true,
                    0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    
    class MyStringCallback extends StringCallback{
    	
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
	            // 刷新界面数据
	            refreshCurrentPicData();
	        } else {
	            ToastUtils.show(msg);
	        }

	    }
    	
    }

/*    @Override
    public void onUploadSuccess(UploadResult result, int index) {
        closeDialog();
        if (result.getCode() == 0) {
            uMengEvent("homework_answer");
            ToastUtils.show("提交成功");
            // 刷新界面数据
            refreshCurrentPicData();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChecking = false;
        GlobalVariable.mViewPager=null;        
    }

}

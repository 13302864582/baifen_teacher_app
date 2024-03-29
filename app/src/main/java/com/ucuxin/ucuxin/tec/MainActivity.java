package com.ucuxin.ucuxin.tec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.controller.MainMessageController;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.communicate.MessageListActivity;
import com.ucuxin.ucuxin.tec.function.home.DaicainaActivity;
import com.ucuxin.ucuxin.tec.function.home.DaihuidaActivity;
import com.ucuxin.ucuxin.tec.function.home.HasTousuActivity;
import com.ucuxin.ucuxin.tec.function.home.MyWalletActivity;
import com.ucuxin.ucuxin.tec.function.home.NoticeActivity;
import com.ucuxin.ucuxin.tec.function.home.ZerenxinRuleActivity;
import com.ucuxin.ucuxin.tec.function.home.model.HomepageModel;
import com.ucuxin.ucuxin.tec.function.home.model.NoticeModel;
import com.ucuxin.ucuxin.tec.function.homework.ResponderActivity;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.VolleyRequestQueue;
import com.ucuxin.ucuxin.tec.http.volley.VolleyErrorHelper;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyAsyncTask;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.glide.GlideImageUtils;
import com.ucuxin.ucuxin.tec.view.dialog.CustomAppCheckUpdateDialog;
import com.ucuxin.ucuxin.tec.view.dialog.CustomTixianDialog;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 主页
 *
 * @author: sky
 */
public class MainActivity extends BaseActivity implements HttpListener,OnClickListener, INetWorkListener, OnRefreshListener<ScrollView>, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static boolean isShowPoint;
    // 按两次退出标志
    private boolean isWaittingExit;
    private static long checkTime;
    private UserInfoModel userInfo;
    private ConstraintLayout layout_tousu, layout_zhuiwen, layout_caina, layout_news_message;
    private ConstraintLayout layout_gonggao;
    private ImageView iv_teacher_avatar, iv_qiang;
    private TextView tv_teacher_name, tv_xuehao, tv_xinyong, tv_total_xuedian_val, tv_ketixian_val, tv_gotixian, tv_gonggao;
    private TextView tv_youtousu, tv_youzhuiwen, tv_youcaina, tv_hasxiaoxi, tv_version;
    //private PullToRefreshScrollView mPullRefreshScrollView;
    //private ScrollView mScrollView;
    private TextView tv_head_rule;
    private LinearLayout layout_zerenxin_star;
    private List<ChatInfo> infos;
    private int currentIndex;
    private static boolean isDoInDB;
    private MainMessageController mainMessageController;
    private HomeApI homeApi = null;
    private List<NoticeModel> noticeList = null;
    private HomepageModel homepageModel;
    private final static int HANDLER_HOME_CODE = 3329;
    private final static int HANDLER_NOTICE_CODE = 332229;
    private final static int HANDLER_TIMER_CODE = 992229;
    private static final int REFRESH_COMPLETE = 0X110;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int SDCARD_NOT_NOUNTED = 3;
    private static final int SHOW_NOTICE = 5;
    private static final int SHOW_NOTICE_NOTFORCE = 6;
    private  CustomAppCheckUpdateDialog noticeDialog;
    private Dialog downloadDialog;
    private Thread downLoadThread;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    protected int progress;
    protected boolean interceptFlag = false;
    /* 下载包安装路径 */
    private static final String savePath = Environment.getExternalStorageDirectory().toString() + "/";
    private static final String saveFileName = savePath + "welearn_tec.apk";


    private String homeResult = "";
    private final Timer timer = new Timer();
    private TimerTask task;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_HOME_CODE:
                    homeApi.getNoticeList(requestQueue, MainActivity.this, RequestConstant.GET_NOTICE_LIST);
                    break;
                case HANDLER_NOTICE_CODE:
                    WeLearnApi.checkUpdate();
                    checkWelcomeImage();
                    break;
                case HANDLER_TIMER_CODE:
                    initData();
                    break;
                case REFRESH_COMPLETE: // 下拉刷新
                    //mPullRefreshScrollView.onRefreshComplete();
                    // ToastUtils.showCustomToast(MainActivity.this, "刷新成功!");
                    break;
                case SHOW_NOTICE:// 强制升级
                    cloesNoticeDialog(noticeDialog);
                    showNoticeDialog(MainActivity.this,  true);
                    break;
                case SHOW_NOTICE_NOTFORCE:// 不强制升级
                    cloesNoticeDialog(noticeDialog);
                    showNoticeDialog(MainActivity.this,  false);
                    break;
                case DOWN_UPDATE:
                    setResult();
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
            }

        }
    };
    private NavigationView nv_main_navigation;
    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView nav_View;
    private ConstraintLayout head_View;
    private CircleImageView drawer_head_img;
    private TextView drawer_user_name;
    private ImageView drawer_sex;
    private TextView drawer_userid;
    private TextView drawer_experience;
    private ImageView header_bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariable.mainActivity = this;
        setContentView(R.layout.drawer_main_activity);
        initView();
        initListener();
        updateUiInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                IntentManager.goToSystemSetting(this);
                break;
        }
        // 返回false允许正常的菜单处理资源，若返回true，则直接在此毁灭它
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
        super.initView();
        doToolbar();
        // 个人信息
        iv_teacher_avatar = (ImageView) this.findViewById(R.id.iv_teacher_avatar);
        tv_teacher_name = (TextView) this.findViewById(R.id.tv_teacher_name);
        tv_xuehao = (TextView) this.findViewById(R.id.tv_xuehao);
        tv_xinyong = (TextView) this.findViewById(R.id.tv_xinyong);

        // 责任心指数
        tv_head_rule = (TextView) this.findViewById(R.id.tv_head_rule);
        layout_zerenxin_star = (LinearLayout) this.findViewById(R.id.layout_zerenxin_star);

        // 学点
        tv_total_xuedian_val = (TextView) this.findViewById(R.id.tv_total_xuedian_val);
        tv_ketixian_val = (TextView) this.findViewById(R.id.tv_ketixian_val);
        tv_gotixian = (TextView) this.findViewById(R.id.tv_gotixian);

        // 有投诉/有追问/待采纳/新消息
        this.layout_tousu = (ConstraintLayout) this.findViewById(R.id.layout_tousu);
        this.layout_zhuiwen = (ConstraintLayout) this.findViewById(R.id.layout_zhuiwen);
        this.layout_caina = (ConstraintLayout) this.findViewById(R.id.layout_caina);
        this.layout_news_message = (ConstraintLayout) this.findViewById(R.id.layout_news_message);

        this.tv_youtousu = (TextView) this.findViewById(R.id.tv_youtousu);
        this.tv_youzhuiwen = (TextView) this.findViewById(R.id.tv_youzhuiwen);
        this.tv_youcaina = (TextView) this.findViewById(R.id.tv_youcaina);
        this.tv_hasxiaoxi = (TextView) this.findViewById(R.id.tv_hasxiaoxi);

        // 公告
        layout_gonggao = (ConstraintLayout) this.findViewById(R.id.layout_gonggao);
        tv_gonggao = (TextView) this.findViewById(R.id.tv_gonggao);
        iv_qiang = (ImageView) this.findViewById(R.id.iv_qiang);
        nv_main_navigation = this.findViewById(R.id.nv_main_navigation);
        tv_version = (TextView) this.findViewById(R.id.tv_version);
        // 显示版本
        String phonemodel = android.os.Build.MODEL;
        String androidosversion = android.os.Build.VERSION.RELEASE;
        tv_version.setText("当前版本  " + getString(R.string.version_format_str, TecApplication.versionName) + "  "
                + phonemodel + " (" + androidosversion + ")");

      /*  // 设置下拉加载
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(this);
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        mScrollView.setFillViewport(true);
        mPullRefreshScrollView.setPullToRefreshEnabled(true);*/

        String str = DateUtils.formatDateTime(MainActivity.this, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

      /*  // 设置刷新标签
        mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
        // 设置下拉标签
        mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
        // 设置释放标签
        mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
        // 设置上一次刷新的提示标签
        mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
        // 加载数据操作
        mPullRefreshScrollView.setMode(Mode.PULL_DOWN_TO_REFRESH);*/

        userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        homeApi = new HomeApI();
        noticeList = new ArrayList<NoticeModel>();
    }

    private void doToolbar() {

        toolbar = findViewById(R.id.toolbar);
        //unReadMsgPointIv = findViewById(R.id.unread_msg_point_iv_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharePerfenceUtil.getInstance().setFirstUseFalse();
        initDrawer();
        initToolbarToogle();
    }

    private void initDrawer() {
        mDrawer = findViewById(R.id.drawer_container);
        nav_View = findViewById(R.id.nv_main_navigation);
        head_View = (ConstraintLayout)nav_View.getHeaderView(0);
        head_View.findViewById(R.id.header).setOnClickListener(this);
        //侧拉抽屉信息
        header_bg = head_View.findViewById(R.id.header_bg);
        drawer_head_img = head_View.findViewById(R.id.drawer_icon);
        drawer_user_name = head_View.findViewById(R.id.drawer_user_name);
        drawer_sex = head_View.findViewById(R.id.drawer_sex);
        drawer_userid = head_View.findViewById(R.id.drawer_userid);
        drawer_experience = head_View.findViewById(R.id.drawer_experience);
    }

    private void initToolbarToogle() {
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,mDrawer,toolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.setToolbarNavigationClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == userInfo) {
                    ToastUtils.show(R.string.text_unlogin);
                    return;
                }
            }
        });
        mDrawerToggle.syncState();
        mDrawer.addDrawerListener(mDrawerToggle);
        nav_View.setNavigationItemSelectedListener(this);
    }


    @Override
    public void initListener() {
        super.initListener();
        layout_tousu.setOnClickListener(this);
        layout_zhuiwen.setOnClickListener(this);
        layout_caina.setOnClickListener(this);
        layout_news_message.setOnClickListener(this);
        iv_qiang.setOnClickListener(this);
        layout_gonggao.setOnClickListener(this);
        tv_gotixian.setOnClickListener(this);
        iv_teacher_avatar.setOnClickListener(this);
        tv_head_rule.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        WeLearnApi.checkUpdate();
        WeLearnApi.getUserInfoFromServer(this, this);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        IntentManager.startWService(this);
        if (mainMessageController == null) {
            mainMessageController = new MainMessageController(null, MainActivity.this);
        }
        showMessageList();
    }

    public void initData() {
        postHomepageInfo();

    }

    public void postHomepageInfo() {

        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            showDialog("正在加载...");
            Map<String, Object> subParams = new HashMap<String, Object>();
            OkHttpHelper.postOKHttpBaseParams("teacher", "homepageinfo", subParams, new MyStringCallback());
        } else {
            ToastUtils.show("网络无法连接，请查看网络");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_homework:
                Intent i = new Intent(this, StuHomeWorkHallActivity.class);
                i.putExtra("packtype", 3);
                startActivity(i);
                break;
            case R.id.nav_login_out:
                MobclickAgent.onEvent(this, "logout");
                // WeLearnApi.logout(this);
                showDialog(getString(R.string.text_logging_out));
                OkHttpHelper.post(this, "user", "logout", null, new HttpListener() {
                    @Override
                    public void onSuccess(int code, String dataJson, String errMsg) {
                        cleanUseInfo();

                    }

                    @Override
                    public void onFail(int HttpCode,String errMsg) {
                        cleanUseInfo();
                    }
                });
                break;
            case R.id.nav_question:
                IntentManager.goToMyQpadActivity(this);
                break;
        }
        return true;
    }

    private void cleanUseInfo() {
        IntentManager.stopWService(MainActivity.this);
        // WApplication.mQQAuth.logout(WApplication.getContext());
        WLDBHelper.getInstance().getWeLearnDB().deleteCurrentUserInfo();
        SharePerfenceUtil.getInstance().setWelearnTokenId("");
        SharePerfenceUtil.getInstance().setTokenId("");
        SharePerfenceUtil.getInstance().setIsChoicGream(false);
        SharePerfenceUtil.getInstance().setGradeId(0);
        SharePerfenceUtil.getInstance().setGoLoginType(-1);
        SharePerfenceUtil.getInstance().setPhoneNum("");
        SharePerfenceUtil.getInstance().setAccessToken("");
        if (GlobalVariable.mainActivity != null) {
            GlobalVariable.mainActivity.finish();
        }
        closeDialog();
        IntentManager.goToLogInView(MainActivity.this);

    }

    public class MyStringCallback extends StringCallback {
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
            dissmissDialog();
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                ToastUtils.show("onError:" + e.getMessage());
            } else {
                ToastUtils.show("网络条件较差，请检查网络");
            }


        }

        @Override
        public void onResponse(String response) {
            if (!TextUtils.isEmpty(response)) {
                int code = JsonUtils.getInt(response, "Code", -1);
                String msg = JsonUtils.getString(response, "Msg", "");
                dissmissDialog();
                if (code == 0) {
                    String dataJson = JsonUtils.getString(response, "Data", "");
                    homeResult = dataJson;
                    if (!TextUtils.isEmpty(dataJson)) {
                        homepageModel = JSON.parseObject(dataJson, HomepageModel.class);
                        GlideImageUtils.getInstance().loadAvatarWithActivity(MainActivity.this,
                                homepageModel.getAvatar(), iv_teacher_avatar);
                        tv_teacher_name.setText(homepageModel.getName());
                        String str_xuehaoname = getResFormatStr(R.string.str_xuehaoname,homepageModel.getUserid() + "");
                        tv_xuehao.setText(str_xuehaoname);
                        String str_xinyu = getResFormatStr(R.string.str_xinyu,homepageModel.getCredit() + "");
                        tv_xinyong.setText(str_xinyu);
                        String str_zongxuedian = getResFormatStr(R.string.str_zongxuedian,homepageModel.getGold() + "");
                        tv_total_xuedian_val.setText(str_zongxuedian);
                        tv_ketixian_val.setText(homepageModel.getAvailable());
                        // 责任心指数
                        layout_zerenxin_star.removeAllViews();
                        int responsibility_index = homepageModel.getResponsibility_index();
                        for (int i = 0; i < responsibility_index; i++) {
                            ImageView iv = new ImageView(MainActivity.this);
                            iv.setBackgroundResource(R.drawable.home_heart_icon);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(0, 0, 3, 0);
                            layout_zerenxin_star.addView(iv, lp);
                        }

                        Message msgg = Message.obtain();
                        msgg.what = HANDLER_HOME_CODE;
                        mHandler.sendMessage(msgg);
                    }
                } else {
                    ToastUtils.show(msg);
                }
            }
        }

        @Override
        public void inProgress(float progress) {
            // Log.e(TAG, "inProgress:" + progress);
            // mProgressBar.setProgress((int) (100 * progress));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_teacher_avatar:
                if (null == userInfo) {
                    ToastUtils.show(R.string.text_unlogin);
                    return;
                }
                if(!mDrawer.isDrawerOpen(GravityCompat.START))
                    mDrawer.openDrawer(GravityCompat.START);
                break;
            /*case R.id.iv_teacher_avatar:
                userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
                if (null == userInfo) {
                    ToastUtils.show(R.string.text_unlogin);
                    return;
                }
                Bundle data = new Bundle();
                data.putInt("userid", userInfo.getUserid());
                data.putInt("roleid", userInfo.getRoleid());
                IntentManager.goToTeacherCenterView(this, TeacherCenterActivityNew.class, data);
                break;*/
            case R.id.tv_ketixian_val:
            case R.id.tv_gotixian:// 提现
                if (homepageModel != null) {
                    Intent intent_tixian = new Intent(this, MyWalletActivity.class);
                    intent_tixian.putExtra("homemodel", homepageModel);
                    startActivity(intent_tixian);
                } else {
                    ToastUtils.show("无法获取提现金额");
                }

                break;
            case R.id.layout_gonggao:// 公告
                Intent intent_gonggao = new Intent(this, NoticeActivity.class);
                intent_gonggao.putExtra("noticeList", (Serializable) noticeList);
                startActivity(intent_gonggao);
                break;
            case R.id.layout_tousu:// 有投诉
                tv_youtousu.setVisibility(View.GONE);
                intent = new Intent(this, HasTousuActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_zhuiwen:// 有追问
                tv_youzhuiwen.setVisibility(View.GONE);
                intent = new Intent(this, DaihuidaActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_caina:// 待采纳
                tv_youcaina.setVisibility(View.GONE);
                intent = new Intent(this, DaicainaActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_news_message:// 新消息
                intent = new Intent(this, MessageListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_qiang:// 抢题
                intent = new Intent(this, ResponderActivity.class);
                startActivity(intent);
                break;
 /*           case R.id.iv_shuaxinjinbi:// 刷新金币
                initData();
                break;*/

            case R.id.tv_head_rule://跳转到责任心指数
                intent = new Intent(this, ZerenxinRuleActivity.class);
                startActivity(intent);
                break;
            case R.id.header:
                IntentManager.goToStuModifiedInfoActivity(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if ((System.currentTimeMillis() - checkTime) > 600000) {
                if (NetworkUtils.getInstance().isInternetConnected(MainActivity.this)) {
                    //检测升级
                    OkHttpHelper.getOKHttpBaseParams(AppConfig.UPDATEURL, "", "", new CheckUpdateCallback());
                    checkTime = System.currentTimeMillis();
                } else {
                    ToastUtils.show("网络无连接");
                }

            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isWaittingExit) {
                isWaittingExit = false;
                finish();
            } else {
                isWaittingExit = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        isWaittingExit = false;
                    }
                }, 3000);
                ToastUtils.show(R.string.text_toast_quit);
            }
        }
        return true;
    }

    private void checkWelcomeImage() {
        final long time = SharePerfenceUtil.getInstance().getWelcomeImageTime();
        VolleyRequestQueue.getQueue().add(new JsonObjectRequest(Method.GET,
                AppConfig.WELCOME_IMAGE_CHECK_URL + "?date=" + time + "&role=2", null, new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        // state: 0--无; 1--新的引导; 2--删除替换
                        int state = response.getInt("state");
                        switch (state) {
                            case 0:
                                break;
                            case 1:
                                String url = response.getString("url");
                                long date = response.getLong("date");
                                new DownloadImageTask().execute(url, String.valueOf(date));
                                break;
                            case 2:
                                MyFileUtil.deleteWelcomeImage();
                                SharePerfenceUtil.getInstance().setWelcomeImageTime(0L);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null));
    }

    class DownloadImageTask extends AsyncTask<String, Void, Integer> {
        private long time = 0L;

        @Override
        protected Integer doInBackground(String... arg0) {
            int result = -1;
            String urlStr = arg0[0];
            time = Long.valueOf(arg0[1]);
            // url =
            // "http://www.iyi8.com/uploadfile/2014/0706/20140706112235583.jpg";
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                InputStream inStream = conn.getInputStream();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String welcomeImgPath = MyFileUtil.getWelcomeImagePath();
                    File wFile = new File(welcomeImgPath);
                    wFile.deleteOnExit();

                    File tFile = new File(MyFileUtil.getWelcomeImagePath() + ".tmp");
                    tFile.deleteOnExit();
                    FileOutputStream fos = null;
                    try {
                        InputStream is = inStream;
                        fos = new FileOutputStream(tFile);
                        byte[] buffer = new byte[1024];
                        int size = 0;
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            size += len;
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        tFile.renameTo(wFile);
                        result = 0;
                        LogUtils.d(TAG, "下载文件size=" + size);
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = -1;
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = -1;
                    } finally {
                        try {
                            if (null != fos) {
                                fos.close();
                            }
                        } catch (Exception e2) {
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Toast.makeText(WApplication.getContext(), "下载结果=" + result,
            // Toast.LENGTH_SHORT).show();
            LogUtils.d(TAG, "下载结果=" + result);
            if (result == 0) {
                SharePerfenceUtil.getInstance().setWelcomeImageTime(time);
            } else {
                SharePerfenceUtil.getInstance().setWelcomeImageTime(0L);
            }
            super.onPostExecute(result);
        }

    }

    @Override
    public void onPre() {


    }

    @Override
    public void onException() {


    }

    @Override
    public void onAfter(String jsonStr, int msgDef) {
        switch (msgDef) {
            case MsgConstant.MSG_DEF_MSGS:
                showMessageList();
                break;
            case MsgConstant.MSG_NOTICE_CODE:// 通知
                String dataString = JsonUtils.getString(jsonStr, "data", "");
                String contentString = JsonUtils.getString(dataString, "content", "");
                NoticeModel noticeModel = JSON.parseObject(contentString, NoticeModel.class);
                noticeList.add(noticeModel);
                getNoticeFromDB(noticeList);

                break;

            case MsgConstant.MSG_TOUSU_CODE:// 投诉
                String tousudataString = JsonUtils.getString(jsonStr, "data", "");
                if (!TextUtils.isEmpty(tousudataString)) {
                    tv_youtousu.setVisibility(View.VISIBLE);
                } else {
                    tv_youtousu.setVisibility(View.GONE);

                }

                break;

            case MsgConstant.MSG_ZHUIWEN_CODE:// 追问
                String zhuiwendataString = JsonUtils.getString(jsonStr, "data", "");
                if (!TextUtils.isEmpty(zhuiwendataString)) {
                    tv_youzhuiwen.setVisibility(View.VISIBLE);
                } else {
                    tv_youzhuiwen.setVisibility(View.GONE);
                }
                break;

            case MsgConstant.MSG_REFRESH_GOLD_CODE:// 刷新金币
                String dataStr = JsonUtils.getString(jsonStr, "data", "");
                float gold = (float) JsonUtils.getDouble(dataStr, "gold", 0);
                float credit = (float) JsonUtils.getDouble(dataStr, "credit", 0);
                tv_xinyong.setText(credit + "");
                tv_total_xuedian_val.setText(gold + "");
                tv_ketixian_val.setText(gold + "");
                break;
        }

    }

    @Override
    public void onDisConnect() {


    }

    private void showMessageList() {
        new MyAsyncTask() {

            @Override
            public void preTask() {
            }

            @Override
            public void postTask() {
               /* if (MainActivity2.unReadMsgPointIv != null) {
                    if (MainActivity2.isShowPoint) {
                        MainActivity2.unReadMsgPointIv.setVisibility(View.VISIBLE);
                    } else {
                        MainActivity2.unReadMsgPointIv.setVisibility(View.INVISIBLE);
                    }
                }*/
                if (infos != null && infos.size() > 0) {
                    for (int i = 0; i < infos.size(); i++) {
                        final ChatInfo chat = infos.get(i);
                        boolean successed = queryAndSetUserData(chat);
                        if (!successed) {
                            currentIndex = i;
                            break;
                        }

                    }
                }

                int count = 0;
                if (infos != null) {
                    for (ChatInfo info : infos) {
                        count += info.getUnReadCount();
                    }
                }
                LogUtils.e("MainActivity messageList-->", count + "");

                if (count > 99) {
                    count = 99;
                }
                tv_hasxiaoxi.setVisibility(View.VISIBLE);
                tv_hasxiaoxi.setText("");
                // tv_youxiaoxi.setText(count+"");

                if (count == 0) {
                    tv_hasxiaoxi.setText("");
                    tv_hasxiaoxi.setVisibility(View.GONE);
                }

            }

            @Override
            public void doInBack() {
                if (!isDoInDB) {
                    isDoInDB = true;
                    MainActivity.isShowPoint = false;
                    infos = WLDBHelper.getInstance().getWeLearnDB().queryMessageList();
                    isDoInDB = false;
                }
            }
        }.excute();
    }

    private boolean queryAndSetUserData(ChatInfo chat) {
        final int fromuser = chat.getFromuser();
        final int fromroleid = chat.getFromroleid();
        UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(fromuser, true);

        boolean flag = true;
        if (user != null) {
            chat.setUser(user);
            // mAdapter.setData(infos);
            flag = true;
        } else {
            flag = false;

            WeLearnApi.getContactInfo(this, fromuser, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        UserInfoModel user = new Gson().fromJson(dataJson, UserInfoModel.class);
                        String name = "";
                        String avatar_100 = "";
                        if (user == null) {
                            user = new UserInfoModel();
                            user.setUserid(fromuser);
                            user.setRoleid(fromroleid);
                            user.setName("未知");
                            user.setAvatar_100("");
                        }
                        name = user.getName();
                        avatar_100 = user.getAvatar_100();
                        WLDBHelper.getInstance().getWeLearnDB().insertorUpdate(fromuser, fromroleid, name, avatar_100);

                        if (currentIndex < infos.size()) {
                            LogUtils.i(TAG, user.toString());
                            infos.get(currentIndex).setUser(user);
                        }
                        setUser();

                    } else {
                        // ToastUtils.show(mActivity, errMsg +"----是我弹的");
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                }
            });
        }
        return flag;
    }

    private void setUser() {
        currentIndex++;
        if (currentIndex < infos.size()) {
            ChatInfo chat = infos.get(currentIndex);
            boolean successed = queryAndSetUserData(chat);
            if (successed) {
                setUser();
            }
        }
    }

    @Override
    public void resultBack(Object... param) {
        super.resultBack(param);
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.GET_HOMEPAGE_INFO_CODE:// 获取首页信息
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtils.getInt(datas, "Code", -1);
                    String msg = JsonUtils.getString(datas, "Msg", "");
                    dissmissDialog();
                    if (code == 0) {
                        String dataJson = JsonUtils.getString(datas, "Data", "");
                        homeResult = dataJson;
                        if (!TextUtils.isEmpty(dataJson)) {
                            homepageModel = JSON.parseObject(dataJson, HomepageModel.class);
                            GlideImageUtils.getInstance().loadAvatarWithActivity(MainActivity.this,
                                    homepageModel.getAvatar(), iv_teacher_avatar);

                            tv_teacher_name.setText(homepageModel.getName());
                            String str_xuehaoname = getResFormatStr(R.string.str_xuehaoname,homepageModel.getUserid() + "");
                            tv_xuehao.setText(str_xuehaoname);
                            String str_xinyu = getResFormatStr(R.string.str_xinyu,homepageModel.getCredit() + "");
                            tv_xinyong.setText(str_xinyu);
                            String str_zongxuedian = getResFormatStr(R.string.str_zongxuedian,homepageModel.getGold() + "");
                            tv_total_xuedian_val.setText(str_zongxuedian);
                            tv_ketixian_val.setText(homepageModel.getAvailable() + "");

                            Message msgg = Message.obtain();
                            msgg.what = HANDLER_HOME_CODE;
                            mHandler.sendMessage(msgg);

                        }
                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case RequestConstant.GET_NOTICE_LIST:// 获取通知
                if (param.length > 0 && param[1] != null && param[1] instanceof String) {
                    String datas = param[1].toString();
                    int code = JsonUtils.getInt(datas, "Code", -1);
                    String msg = JsonUtils.getString(datas, "Msg", "");
                    if (code == 0) {
                        String dataJson = JsonUtils.getString(datas, "Data", "");
                        if (!TextUtils.isEmpty(dataJson)) {
                            List<NoticeModel> list = JSON.parseArray(dataJson, NoticeModel.class);
                            noticeList.addAll(list);
                            getNoticeFromDB(noticeList);
                        }

                        Message msgg = Message.obtain();
                        msgg.what = HANDLER_NOTICE_CODE;
                        mHandler.sendMessage(msgg);

                    } else {
                        ToastUtils.show(msg);
                    }

                }

                break;
            case -1:
                closeDialog();
                int flag2 = ((Integer) param[1]).intValue();
                if (flag2 == RequestConstant.GET_HOMEPAGE_INFO_CODE) {
                    String errorStr = VolleyErrorHelper.getMessage(param[2], this);
                    // ToastUtils.show("超时，请退出重试");
                    final CustomTixianDialog dialog = new CustomTixianDialog(MainActivity.this, errorStr);
                    dialog.show();
                    dialog.setButtonListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                break;

        }

    }

    public void getNoticeFromDB(List<NoticeModel> noticeList) {
        try {
            // 插入通知到数据库
            boolean isFlag = WLDBHelper.getInstance().getWeLearnDB().insertNotice(noticeList);
            noticeList.clear();
            // 从数据库中取出最新数据
            List<NoticeModel> list = WLDBHelper.getInstance().getWeLearnDB().getNewNotice();
            if (list != null && list.size() > 0) {
                tv_gonggao.setText(list.get(0).getContent());
            } else {
                tv_gonggao.setText("请关注我们的通知");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

        if (NetworkUtils.getInstance().isInternetConnected(this)) {
            // showDialog("正在加载...");
            // if (homeApi == null) {
            // homeApi = new HomeApI();
            // }
            // homeApi.HomepageInfo(requestQueue, MainActivity.this,
            // RequestConstant.GET_HOMEPAGE_INFO_CODE);
            initData();
            mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 4000);
        } else {
            ToastUtils.show("网络无法连接，请查看网络");
        }

    }


    /****************
     * 升级start
     ************************/


    public class CheckUpdateCallback extends StringCallback {
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
            dissmissDialog();
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                ToastUtils.show("onError:" + e.getMessage());
            } else {
                ToastUtils.show("网络条件较差，请检查网络");
            }


        }

        @Override
        public void onResponse(String response) {
            if (!TextUtils.isEmpty(response)) {
//                int code = JsonUtils.getInt(response, "Code", -1);
//                String msg = JsonUtils.getString(response, "Msg", "");
                dissmissDialog();
                try {
                    int mCurrentVersionConde = TecApplication.getContext().getPackageManager().getPackageInfo(TecApplication.getContext().getPackageName(),
                            0).versionCode;

                    int versionCode = Integer.parseInt(JsonUtils.getString(response, "versioncode", ""));
                    SharePerfenceUtil.getInstance().setLatestVersion(versionCode);

                    String title = JsonUtils.getString(response, "title", "");
                    SharePerfenceUtil.getInstance().setUpdateTitle(title);

                    String content = JsonUtils.getString(response, "content", "");
                    SharePerfenceUtil.getInstance().setUpdateContent(content);

                    String apkUrl = JsonUtils.getString(response, "url", "");
                    SharePerfenceUtil.getInstance().setUpdateUrl(apkUrl);

                    String compel = JsonUtils.getString(response, "compel", "");
                    if (mCurrentVersionConde < versionCode) {
                        if (compel.equals("yes")) {
                            mHandler.sendEmptyMessageDelayed(SHOW_NOTICE, 2000);
                        } else {
                            mHandler.sendEmptyMessageDelayed(SHOW_NOTICE_NOTFORCE, 2000);
                        }

                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void inProgress(float progress) {
            // Log.e(TAG, "inProgress:" + progress);
            // mProgressBar.setProgress((int) (100 * progress));
        }
    }

    /**
     * 手动检测更新
     */
    // public void maunalCheckUpdateInfo() {
    // isManual = true;
    // WeLearnApi.checkUpdate();
    // }
    public void showNoticeDialog(Context mContext,   boolean isYes) {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            noticeDialog = new CustomAppCheckUpdateDialog(MainActivity.this, isYes);
            noticeDialog.show();
            noticeDialog.setClicklistener(new CustomAppCheckUpdateDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    noticeDialog.dismiss();
                    showDownloadDialog();
                }

                @Override
                public void doCancel() {
                    noticeDialog.dismiss();
                }

                @Override
                public void doGoUpdate() {
                    noticeDialog.dismiss();
                    showDownloadDialog();
                }
            });

        }
    }


    public static void cloesNoticeDialog(Dialog noticeDialog) {
        if (noticeDialog != null && noticeDialog.isShowing()) {
            noticeDialog.dismiss();
        }
    }


    private void showDownloadDialog() {
        showResult();
        downloadApk();
    }


    public void showResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(SharePerfenceUtil.getInstance().getUpdateTitle());
        builder.setMessage("正在下载，请稍后");

        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                finish();
                Process.killProcess(Process.myPid());
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
    }


    protected void setResult() {
        mProgress.setProgress(progress);
    }


    private Runnable mdownApkRunnable = new Runnable() {

        public void run() {
            if (!NetworkUtils.getInstance().isInternetConnected(TecApplication.getContext())) {
                ToastUtils.show("网络呀，你不要抖动的这么厉害呀！好吧，过会儿再试试");
            }
            try {
                URL url = new URL(SharePerfenceUtil.getInstance().getUpdateUrl());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                String state = Environment.getExternalStorageState();
                String apkFile = "";
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    apkFile = saveFileName;
                }
                if (TextUtils.isEmpty(apkFile)) {
                    mHandler.sendEmptyMessage(SDCARD_NOT_NOUNTED);
                }
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    public void click_Personal(View v){

    }


    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        startActivity(i);
    }

    /***************
     * 升级end
     ********************************/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mainMessageController != null) {
            mainMessageController.removeMsgInQueue();
        }
        //unReadMsgPointIv = null;
        mHandler.removeCallbacksAndMessages(null);
        GlobalVariable.mainActivity = null;
    }

    @Override
    public void onSuccess(int code, String dataJson, String errMsg) {
        if (code == 0) {
            updateUiInfo();
        } else {
            if (!TextUtils.isEmpty(errMsg)) {
                ToastUtils.show(errMsg);
            }
        }
    }

    @Override
    public void onFail(int HttpCode,String errMsg) {

    }
    private UserInfoModel uInfo = null;
    private void updateUiInfo() {
        uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();

        if (null == uInfo) {
            LogUtils.e(TAG, "Contact info gson is null !");
            return;
        }
        // 背景
        /*
        String groupphoto = uInfo.getGroupphoto();
        if (groupphoto == null) {
            groupphoto = "";
        }
        Glide.with(this)
                .asBitmap()
                .load(groupphoto)
                .centerCrop()
                .placeholder(R.drawable.default_teacher_info_bg)
                .into(header_bg);*/

        // 头像
        String avatar_100 = uInfo.getAvatar_100();
        if (avatar_100 == null) {
            avatar_100 = "";
        }

        Glide.with(this).load(avatar_100).centerCrop().placeholder(R.drawable.teacher_default_avatar_circle).into(drawer_head_img);

        String name = TextUtils.isEmpty(uInfo.getName()) ? getString(R.string.persion_info) : uInfo.getName();
        setWelearnTitle(name);
        drawer_user_name.setText(name);

        drawer_sex.setVisibility(View.VISIBLE);
        int sex = uInfo.getSex();
        switch (sex) {
            case GlobalContant.SEX_TYPE_MAN:
                drawer_sex.setSelected(true);
                break;
            case GlobalContant.SEX_TYPE_WOMEN:
                drawer_sex.setSelected(false);
                break;

            default:
                drawer_sex.setVisibility(View.GONE);
                break;
        }
        int userid = uInfo.getUserid();
        drawer_userid.setText(getString(R.string.student_id, userid + ""));

        float credit = uInfo.getCredit();
        String creditStr = GoldToStringUtil.GoldToString(credit);
        drawer_experience.setText(getString(R.string.credit_text, creditStr));
    }
}

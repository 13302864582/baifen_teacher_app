
package com.ucuxin.ucuxin.tec;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.ucuxin.ucuxin.tec.constant.WxConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.db.WelearnDBUtil;
import com.ucuxin.ucuxin.tec.http.VolleyRequestQueue;
import com.ucuxin.ucuxin.tec.manager.CrashHandlerException;
import com.ucuxin.ucuxin.tec.model.ExplainPoint;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * 此类的描述： 系统 Application
 *
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015-7-20 下午7:03:57
 */
public class TecApplication extends Application {

    //private static TecApplication mInstance;

    private static Context mContext;
    public static IWXAPI api;
    public static QQAuth mQQAuth;

    public static Tencent mTencent;
    public static NetworkUtils mNetworkUtil;

    public static LinkedHashSet<String> readyReqQueue;

    // public static boolean isChange = false;// 维护抢题页面是否需要答题
    public static boolean isInChatMsgView = false;// 是否在聊天页面

    public static int notifiFromUser = 0;// 是否有消息通知 , 0则没有
    public static int isShowgoodview = 0;// 是否显示goodview

    public static LinkedHashSet<ExplainPoint> coordinateAnswerIconSet;

    public static Map<Double, Integer> time2CmdMap;

    public static List<AnimationDrawable> animationDrawables;

    public static List<ImageView> anmimationPlayViews;

    public static Map<String, String> jsonDataMap;

    public static int currentUserId;
    public static int gradeid = 0;
    public static int subjectid = 0;

    public static float lastmessagetimestamp;// 最后一条消息的时间戳

    public static AudioManager audioManager;

    public static int versionCode;

    public static String versionName;

    public static String umeng_channel;

    public List<Activity> activityList;// 存放所有启动的Activity

    public static String isShowDialog = "showing";

//    private RefWatcher refWatcher;
//
//
//    public static RefWatcher getRefWatcher(Context context) {
//        TecApplication application = (TecApplication) context
//                .getApplicationContext();
//        return application.refWatcher;
//    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mInstance = this;
        //mContext = mInstance.getApplicationContext();
        mContext = getApplicationContext();
//        if (AppConfig.IS_DEBUG) {
//            LeakCanary.install(this);
//        }
        init();
        NetworkListener();
        initWx();
		initException();
        getCurrentVersion();
        initData();
    }

    private void init() {
        activityList = new ArrayList<Activity>();
        umeng_channel = getMetaValue("UMENG_CHANNEL");
        // 初始化数据库 added by yh 2015-01-07 Start
        WLDBHelper.getInstance().getWeLearnDB();
        // 初始化数据库 added by yh 2015-01-07 End

        // 初始化网络请求队列 added by yh 2015-01-09 Start
        VolleyRequestQueue.init(mContext);
        // 初始化网络请求队列 added by yh 2015-01-09 End

        coordinateAnswerIconSet = new LinkedHashSet<ExplainPoint>();
        animationDrawables = new ArrayList<AnimationDrawable>();
        anmimationPlayViews = new ArrayList<ImageView>();
        jsonDataMap = new HashMap<String, String>();
        readyReqQueue = new LinkedHashSet<String>();
        mNetworkUtil = NetworkUtils.getInstance();

        // coordinateAnswerIconSet = new LinkedHashSet<Point>();
        time2CmdMap = new ConcurrentHashMap<Double, Integer>();
        // animationDrawables = new ArrayList<AnimationDrawable>();
        // anmimationPlayViews = new ArrayList<ImageView>();
        // jsonDataMap = new HashMap<String, String>();

        // add by milo 2014.09.23
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMicrophoneMute(false);
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.STREAM_MUSIC);

        // 打开websocket连接
        // IntentManager.startWService(mContext);
    }

    // 初始化腾讯api
    private void initWx() {
        mTencent = Tencent.createInstance(WxConstant.APP_ID_QQ, mContext);
        mQQAuth = QQAuth.createInstance(WxConstant.APP_ID_QQ, mContext);
        api = WXAPIFactory.createWXAPI(mContext, WxConstant.APP_ID_WW, true);
        api.registerApp(WxConstant.APP_ID_WW);

        CrashReport.initCrashReport(getApplicationContext(), WxConstant.Bugly_AppID, false);
        //CrashReport.testJavaCrash();
    }

    /**
     * 得到当前的版本
     */
    public void getCurrentVersion() {
        // mTencent = Tencent.createInstance(APP_ID_QQ, mContext);
        // mQQAuth = QQAuth.createInstance(APP_ID_QQ, mContext);
        //
        // api = WXAPIFactory.createWXAPI(mContext, APP_ID_WW, true);
        // api.registerApp(APP_ID_WW);

        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化异常
     */
    private void initException() {
        // Thread.setDefaultUncaughtExceptionHandler(new
        // ExceptionManager(mContext));
        CrashHandlerException crash = new CrashHandlerException(this);
        Thread.setDefaultUncaughtExceptionHandler(crash);
    }

    /**
     * 监听网络变化广播
     */
    public void NetworkListener() {
        Intent networkIntent = new Intent("com.ucuxin.reveiver.startconn");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(networkIntent);
    }



    public static String getChannelValue() {
        return umeng_channel;
    }

    /**
     * 获取清单文件张Meta-Data的Value值
     */
    public static String getMetaValue(String metaKey) {
        String value = "";
        if (mContext != null && metaKey != null) {
            try {
                String packageName = mContext.getPackageName();
                PackageManager packageManager = mContext.getPackageManager();
                ApplicationInfo aiApplicationInfo = packageManager.getApplicationInfo(packageName,
                        PackageManager.GET_META_DATA);

                if (null != aiApplicationInfo) {
                    Bundle metaData = aiApplicationInfo.metaData;
                    if (null != metaData) {
                        // Set<String> keys = metaData.keySet();
                        // for (String key : keys) {
                        // String string = metaData.getString(key, "");
                        //
                        // if (TextUtils.isEmpty(string)) {
                        // string = metaData.getInt(metaKey , 0) +"";
                        // }
                        //
                        // LogUtils.e("metadata", key + ":" + string);
                        // }
                        value = metaData.getString(metaKey, "");
                        if (TextUtils.isEmpty(value)) {
                            value = metaData.getInt(metaKey, 0) + "";
                        }
                    }
                }

            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    /**
     * 初始化静态数据
     */
    private void initData() {
        // if (WLDBHelper.getInstance().getWeLearnDB().getSubjectCount() <= 0) {
        // WelearnDBUtil.loadDefaultGradeDB();
        // WelearnDBUtil.loadDefaultSubjectDB();
        // }

        // System.out
        // .println("subject-->" +
        // WLDBHelper.getInstance().getWeLearnDB().getSubjectCount());
        if (WLDBHelper.getInstance().getWeLearnDB().getSubjectCount() <= 0) {
            WelearnDBUtil.loadDefaultGradeDB();
            WelearnDBUtil.loadDefaultSubjectDB();
        } else if (WLDBHelper.getInstance().getWeLearnDB().getSubjectCount() > 0) {
            int yuwenCount = WLDBHelper.getInstance().getWeLearnDB().querySubjectWithYuwen();
            if (yuwenCount == 0 || yuwenCount > 1) {
                WLDBHelper.getInstance().getWeLearnDB().delSubjectAndGradeTable();
                WelearnDBUtil.loadDefaultGradeDB();
                WelearnDBUtil.loadDefaultSubjectDB();
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (activityList != null && activityList.size() > 0) {// 程序退出时销毁所有activity
            for (Activity activity : activityList) {
                activity.finish();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    // activity管理：结束所有activity，彻底关闭应用
    public void finishProgram() {
        for (Activity activity : activityList) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}

package com.ucuxin.ucuxin.tec.http;

import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestClientAPI;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.okhttp.OkHttpUtils;
import com.ucuxin.ucuxin.tec.okhttp.builder.GetBuilder;
import com.ucuxin.ucuxin.tec.okhttp.builder.PostFormBuilder;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import android.content.Context;
import android.text.TextUtils;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 此类的描述： http请求封装类
 *
 * @author: Sky
 * @最后修改人： Sky
 * @最后修改日期:2016年5月13日 下午2:45:39
 */
public class OkHttpHelper {
    private static final String TAG=OkHttpHelper.class.getSimpleName();


    /**
     * 封装okhttp基本参数
     * 需要出入回调的StringCallback
     *
     * @param builder
     */
    public static void postOKHttpBaseParams(String module, String func, Map<String, Object> subParams, StringCallback stringcallback) {
        String url = AppConfig.GO_URL + module + "/" + func;
        if (AppConfig.IS_DEBUG){
            LogUtils.e(TAG,url);
        }
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        int ver = TecApplication.versionCode;
        builder.addParams("ver", ver + "");
        builder.addParams("appname", VolleyRequestClientAPI.APP_NAME);
        String sourcechan = TecApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "ucuxin";
        }
        builder.addParams("sourcechan", sourcechan);
        builder.addParams("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId());
        String dataStr = "";
        if (null != subParams) {
//			dataStr = subParams.toString();
            dataStr=JSON.toJSONString(subParams);
        } else {
            dataStr = "{}";
        }

        builder.addParams("data", dataStr);
        builder.build().connTimeOut(30000).writeTimeOut(20000).readTimeOut(20000).execute(stringcallback);

    }

    //get()
    public static void getOKHttpBaseParams(String urls,String module, String func,  StringCallback stringcallback) {
        String url = urls + module + "/" + func;
        GetBuilder builder = OkHttpUtils.get().url(url);
   /*     int ver = TecApplication.versionCode;
        builder.addParams("ver", ver + "");
        builder.addParams("appname", VolleyRequestClientAPI.APP_NAME);
        String sourcechan = TecApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "ucuxin";
        }
        builder.addParams("sourcechan", sourcechan);
        builder.addParams("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId());
        String dataStr = "";
        if (null != subParams) {
//			dataStr = subParams.toString();
            dataStr=JSON.toJSONString(subParams);
        } else {
            dataStr = "{}";
        }

        builder.addParams("data", dataStr);*/
        builder.build().connTimeOut(30000).writeTimeOut(20000).readTimeOut(20000).execute(stringcallback);

    }


    public static void post(Context context, String module, String func, JSONObject data, HttpListener lisener) {
        String url = AppConfig.GO_URL + module + "/" + func;
        if (AppConfig.IS_DEBUG){
            LogUtils.e(TAG,url);
        }
        postWithUrl(context, url, data, lisener);
    }

    public static void postSingle(String url1, String module, String func, JSONObject data, HttpListener lisener) {
        String url = url1 + module + "/" + func;
        if (AppConfig.IS_DEBUG){
            LogUtils.e(TAG,url);
        }
        postWithUrl(TecApplication.getContext(), url, data, lisener);
    }


    public static void postWithUrl(Context context, String url, JSONObject data, HttpListener lisener) {
        // new HttpRequestAsyncTask(context, url, lisener).execute(data);
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        int ver = TecApplication.versionCode;
        builder.addParams("ver", ver + "");
        builder.addParams("appname", VolleyRequestClientAPI.APP_NAME);
        String sourcechan = TecApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "ucuxin";
        }
        builder.addParams("sourcechan", sourcechan);
        builder.addParams("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId());

        String dataStr = null;
        if (null != data) {
            dataStr = data.toString();
//            dataStr=JSON.toJSONString(subParams);
        } else {
            dataStr = "{}";
        }
        builder.addParams("data", dataStr);
        MyStringCallback myStringCallback = new MyStringCallback(context, lisener, url, data);
        builder.build().connTimeOut(30000).writeTimeOut(20000).readTimeOut(20000).execute(myStringCallback);

    }

    ///////////////////////////////////////////

    /**
     * 此类的描述：请求回调
     *
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2016年5月13日 上午10:37:25
     */
    static class MyStringCallback extends StringCallback {
        private Context context;
        private HttpListener listener;
        private String url;
        private JSONObject data;
        // private BaseActivity activity;

        public MyStringCallback(Context context, HttpListener listener, String url, JSONObject data) {
            super();
            this.context = context;
            this.listener = listener;
            this.url = url;
            this.data = data;
            // this.activity=(BaseActivity) context;
        }

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);

            // if (null != activity) {
            // activity.showDialog("请稍后...");
            // }

        }

        @Override
        public void onAfter() {
            super.onAfter();
            // if (null != activity) {
            // activity.closeDialogHelp();
            // }

        }

        @Override
        public void onError(Call call, Exception e) {
            // ToastUtils.show("onError:" + e.getMessage());
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
            listener.onFail(-1, errorMsg);

        }

        @Override
        public void onResponse(String response) {
            if (null != listener) {
                if (!TextUtils.isEmpty(response)) {
                    int code = JsonUtils.getInt(response, "Code", -1);
                    String msg = JsonUtils.getString(response, "Msg", "");
                    final String responseStr = JsonUtils.getString(response, "Data", "");
                    switch (code) {
                        case 0:
                            listener.onSuccess(code, responseStr, msg);
                            break;
                        case 2:
                            doReLogin();
                            break;
                        default:
                            ToastUtils.show(msg);
                            listener.onFail(code, msg);
                            break;
                    }

                } else {
                    //listener.onFail(-1,msg);
                    ToastUtils.show("服务器返回异常");
                }
            }

        }

        @Override
        public void inProgress(float progress) {
            // Log.e(TAG, "inProgress:" + progress);
            // mProgressBar.setProgress((int) (100 * progress));
        }

        /**
         * 此方法描述的是：重登录操作
         *
         * @author: Sky
         * @最后修改人： Sky
         * @最后修改日期:2016年5月13日 下午2:38:39
         * doReLogin void
         */
        private void doReLogin() {
            WeLearnApi.relogin(context, new HttpListener() {
                @Override
                public void onSuccess(int code, String dataJson, String errMsg) {
                    if (code == 0) {
                        UserInfoModel uInfo = null;
                        try {
                            uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                            if (uInfo != null) {
                                WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                            }
                            // 打开websocket连接
                            IntentManager.startWService(TecApplication.getContext());
                            postWithUrl(context, url, data, listener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (null != listener) {
                            listener.onSuccess(code, dataJson, errMsg);
                        }
                        if (code == 1) { // 跳转登录页面
                            TecApplication.mNetworkUtil.disConnect();
                            if (null != context) {
                                IntentManager.goToLogInView(context);
                            }
                        }
                    }
                }

                @Override
                public void onFail(int HttpCode, String errMsg) {
                    if (null != listener) {
                        listener.onFail(HttpCode, errMsg);
                    }
                }
            });
        }
    }
    // 网络回调结束


    public interface HttpListener {
        void onSuccess(int code, String dataJson, String errMsg);

        void onFail(int HttpCode, String errMsg);
    }


}

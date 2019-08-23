package com.ucuxin.ucuxin.tec.api;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.http.RequestParamUtils;
import com.ucuxin.ucuxin.tec.http.VolleyRequestQueue;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.LoginModel;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.UploadUtil2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeLearnApi {

    private static final String TAG = "WeLearnApi";

    public static void sendPingMsg() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "ping");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TecApplication.mNetworkUtil.sendTextmessage(json.toString());
    }

    /**
     * 用户反馈
     *
     * @param msg      反馈内容
     * @param listener 提交回调
     */
    public static void addFeedBack(Context context, String msg, HttpListener listener) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        try {
            JSONObject data = new JSONObject();
            data.put("message", msg);
            OkHttpHelper.post(context, "system", "feedback", data, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getContactsList(Context context, HttpListener listener) {
        OkHttpHelper.post(context, "user", "getcontacts", null, listener);
    }

    /**
     * 获取联系人信息
     *
     * @param userId   用户id
     * @param roleId   角色id
     * @param listener 回调
     */
    public static void getContactInfo(Context context, int userId, HttpListener listener) {
        try {
            JSONObject data = new JSONObject();
            data.put("userid", userId);
            OkHttpHelper.post(context, "user", "getuserinfo", data, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTeacherCommList(Context context, int userId, int pageIndex, int pageCount,
                                          HttpListener listener) {
        try {
            JSONObject data = new JSONObject();
            data.put("userid", userId);
            data.put("pageindex", pageIndex);
            data.put("pagecount", pageCount);
            OkHttpHelper.post(context, "user", "teachercontent", data, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     *
     * @param userId
     * @param listener
     */
    public static void deleteFriend(Context context, int userId, HttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("contactid", userId);
            OkHttpHelper.post(context, "user", "removecontact", obj, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     *
     * @param userId
     * @param listener
     */
    public static void addFriend(Context context, int userId, HttpListener listener) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("contactid", userId);
            OkHttpHelper.post(context, "user", "addcontact", obj, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改用户信息
     *
     * @param userId   用户ID
     * @param data     修改的属性
     * @param listener 回调
     */
    public static void updateContactInfo(Context context, int userId, JSONObject data, HttpListener listener) {
        try {
            OkHttpHelper.post(context, "user", "update", data, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void talkMsgReceivedVerity(String respText) {
        try {
//			LogUtils.e("收到服务器的消息:", respText);
            JSONObject json = new JSONObject();
            json.put("sessionid", SharePerfenceUtil.getInstance().getWelearnTokenId());
            json.put("type", JsonUtils.getInt(respText, "type", 0));
            json.put("subtype", GlobalContant.TALKMSGVERITY);
            json.put("timestamp", JsonUtils.getDouble(respText, "timestamp", 0.0));
            double svrtag = JsonUtils.getDouble(respText, "svrtag", 0.0);
            if (svrtag == 0) {
                return;
            }
            json.put("svrtag", svrtag);
            // LogUtils.e("我发的回包:", json.toString());
            TecApplication.mNetworkUtil.sendTextmessage(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新版本的发送消息
     *
     * @param contentType
     * @param receiveUser
     * @param msgcontent
     * @param recodrTime
     * @param msgDef
     * @param timestamp
     */
    public static void sendMsg(int contentType, int receiveUser, String msgcontent, int recodrTime, int msgDef,
                               double timestamp) {
        try {
            JSONObject json = new JSONObject();
            String session = SharePerfenceUtil.getInstance().getWelearnTokenId();
            json.put("sessionid", session);
            json.put("platform", "android");
            // double timestamp = (double) new Date().getTime() / 1000;
            json.put("timestamp", timestamp);
            if (msgDef > 0) {
                TecApplication.time2CmdMap.put(timestamp, msgDef);
            }
            json.put("version", TecApplication.versionCode);
            json.put("touser", receiveUser);
            json.put("fromuser", SharePerfenceUtil.getInstance().getUserId());
            json.put("type", 2);
            json.put("subtype", 1);
            json.put("fromroleid", SharePerfenceUtil.getInstance().getUserRoleId());
            json.put("contenttype", contentType);
            json.put("msgcontent", msgcontent);
            if (recodrTime > 0) {
                json.put("audiotime", recodrTime);
            }

            LogUtils.i(TAG, "sendMsg send to " + json.toString());
            // LogUtils.e("发送的聊天消息:", json.toString());
            TecApplication.mNetworkUtil.sendTextmessage(json.toString());
        } catch (JSONException e) {
        }
    }

    /**
     * 搜索联系人
     *
     * @param content   搜索关键字
     * @param pageindex 当前页数
     * @param pagecount 总页数
     * @param listener  回调
     */
    public static void searchFriend(Context context, String content, int pageindex, int pagecount, HttpListener listener) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            JSONObject data = new JSONObject();
            data.put("content", content);
            data.put("serachtype", 0);// 查找类型:0 所有, 1 姓名 2 学号
            data.put("usertype", 0);// 用户类型:0表示所有, 1 表示学生, 2 表示老师
            data.put("pageindex", pageindex);
            data.put("pagecount", pagecount);
            BaseActivity activity = null;
            if (context instanceof BaseActivity) {
                activity = (BaseActivity) context;
            }
            OkHttpHelper.post(context, "user", "search", data, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkUpdate() {
        VolleyRequestQueue.getQueue().add(
                new JsonObjectRequest(Method.GET, AppConfig.UPDATEURL, null, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            handleMessage(MsgConstant.MSG_DEF_OBTAIN_UPDATE_SUCCESS, response);
                        }
                    }
                }, null));
    }

    /**
     * 通过handler发送消息.
     *
     * @param msg_what
     * @param obj
     */
    private static void handleMessage(int msg_what, JSONObject obj) {
        Message message = new Message();
        message.what = msg_what;
        message.obj = obj;
        WelearnHandler.getInstance().getHandler().sendMessage(message);
    }

    /**
     * 从服务器获取用户信息
     *
     * @param listener
     */
    public static void getUserInfoFromServer(Context context, final HttpListener listener) {
        OkHttpHelper.post(context, "user", "getuserinfo", null, new HttpListener() {
            @Override
            public void onSuccess(int code, String dataJson, String errMsg) {
                try {
                    UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
                    TeacherCenterActivityNew.count = uInfo.getCount();
                    WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
                    if (null != listener) {
                        listener.onSuccess(code, dataJson, errMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    /**
     * 此方法描述的是：修改用户资料
     *
     * @param context
     * @param path
     * @param name
     * @param sex
     * @param gradeid
     * @param listener void
     * @author: qhw
     * @最后修改人： qhw
     * @最后修改日期:2015-7-22 下午4:33:52
     * @version: 2.0
     * <p>
     * updateUserInfoFromServer
     */
    public static void updateUserInfoFromServer(BaseActivity context, String path, String name, int sex, int edulevel, String schools, String major, StringCallback listener) {
        JSONObject data = new JSONObject();
        Map<String, List<File>> files = null;
        try {
            data.put("name", name);
            data.put("sex", sex);
            data.put("edulevel", edulevel);
            data.put("schools", schools);
            data.put("major", major);
            if (!TextUtils.isEmpty(path)) {//更新头像
                files = new HashMap<String, List<File>>();
                List<File> fList = new ArrayList<File>();
                fList.add(new File(path));
                files.put("picfile", fList);
                data.put("avatar", "picfile");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //UploadUtil.upload(TeacherCenterActivityNew.UPLOAD_URL, RequestParamUtils.getParam(data), files, listener, true, 0);
        UploadUtil2.upload(TeacherCenterActivityNew.UPLOAD_URL, RequestParamUtils.getMapParam(data), files, listener, true, 0);

    }

    /**
     * 更新用户信息
     *
     * @param listener
     */
    public static void updateUserInfoFromServer(Context context, JSONObject data, final HttpListener listener) {
        OkHttpHelper.post(context, "user", "update", data, listener);
    }

    public static void relogin(Context context, HttpListener listener) {
        try {
            String func = "relogin";
            JSONObject obj = null;
            int type = SharePerfenceUtil.getInstance().getGoLoginType();
            if (type == SharePerfenceUtil.LOGIN_TYPE_PHONE) {
                LoginModel model = SharePerfenceUtil.getInstance().getPhoneLoginInfo();
                if (null == model) {
                    if (null != context) {
                        IntentManager.goToLogInView(context);
                    }
                    return;
                }
                func = "telrelogin";
                obj = new JSONObject(new Gson().toJson(model));
            } else if (type == SharePerfenceUtil.LOGIN_TYPE_QQ) {
                String openId = SharePerfenceUtil.getInstance().getQQLoginInfo();
                if (TextUtils.isEmpty(openId)) {
                    if (null != context) {
                        IntentManager.goToLogInView(context);
                    }
                    return;
                }
                func = "relogin";
                obj = new JSONObject();
                obj.put("openid", openId);
            }
            OkHttpHelper.post(context, "user", func, obj, listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每次GO服务器登陆之后，发送GO服务器返回的Session到Python服务器（用于聊天），以保证两边Session一致
     */
    public static void sendSessionToServer() {

        String tokenId = SharePerfenceUtil.getInstance().getWelearnTokenId();
        if (TextUtils.isEmpty(tokenId)) {
            LogUtils.e(TAG, "token id is empty !");
            return;
        }
        UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
        if (null == uInfo || uInfo.getUserid() <= 0) {
            LogUtils.e(TAG, "user id is empty !");
            return;
        }

        int userId = uInfo.getUserid();

        try {
            JSONObject json = new JSONObject();
            json.put("userid", userId);
            json.put("sessionid", tokenId);
            json.put("type", 2);
            json.put("subtype", 8);
            json.put("platform", "android");
            TecApplication.mNetworkUtil.sendTextmessage(json.toString());

            LogUtils.i(TAG, "send session to server ! session = " + tokenId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

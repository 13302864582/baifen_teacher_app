package com.ucuxin.ucuxin.tec.http;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;

import android.text.TextUtils;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class RequestParamUtils {

    public static final String APP_NAME = "android_fdt_teacher_phone";

//	public static String getParamStr(JSONObject obj) {
//		// ver = app版本号
//		// appname = APP端类型
//		// sourcechan = 渠道
//		StringBuffer sb = new StringBuffer();
//
//		int ver = WApplication.versionCode;
//		String appname = "";
//		String sourcechan = WApplication.getContext().getString(R.string.channel_str_res);
//
//		sb.append("ver=\"" + ver);
//		sb.append("\"&");
//		sb.append("appname=\"" + appname);
//		sb.append("\"&");
//		sb.append("sourcechan=\"" + sourcechan);
//		sb.append("\"&");
//
//		String data = null;
//		if (null != obj) {
//			data = obj.toString();
//		} else {
//			data = "[]";
//		}
//		sb.append("data=\"" + data);
//		sb.append("\"");
//
//		return sb.toString();
//	}

	/*public static List<NameValuePair> getParam(JSONObject dataJson) {
		// ver = app版本号
		// appname = APP端类型
		// sourcechan = 渠道
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		int ver = TecApplication.versionCode;
		
		String sourcechan = TecApplication.getChannelValue();
		if (TextUtils.isEmpty(sourcechan)) {
			sourcechan = "ucuxin";
		}
		String data = null;
		if (null != dataJson) {
			data = dataJson.toString();
		} else {
			data = "{}";
		}
		params.add(new BasicNameValuePair("ver","" + ver));
		params.add(new BasicNameValuePair("appname", APP_NAME));
		params.add(new BasicNameValuePair("sourcechan", sourcechan));
		params.add(new BasicNameValuePair("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId()));
		params.add(new BasicNameValuePair("data", data));
		
		return params;
	}*/


    /**
     * 为了改善上传增加的方法
     *
     * @param dataJson
     * @return
     */
    public static Map<String, String> getMapParam(JSONObject dataJson) {
        // ver = app版本号
        // appname = APP端类型
        // sourcechan = 渠道
        Map<String, String> params = new HashMap<String, String>();
        int ver = TecApplication.versionCode;

        String sourcechan = TecApplication.getChannelValue();
        if (TextUtils.isEmpty(sourcechan)) {
            sourcechan = "ucuxin";
        }
        String data = null;
        if (null != dataJson) {
            data = dataJson.toString();
        } else {
            data = "{}";
        }
        params.put("ver", "" + ver);
        params.put("appname", APP_NAME);
        params.put("sourcechan", sourcechan);
        params.put("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId());
        params.put("data", data);
        return params;
    }

}

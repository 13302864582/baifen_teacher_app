package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.config.AppConfig;

public class SpUtil {

	
	private SharedPreferences mSp;
	private Editor mEditor;

	private static final String SP_NAME = "debugSPV" ;

	private SpUtil() {
		mSp = TecApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	private static class MySpUtilHolder {
		private static final SpUtil INSANCE = new SpUtil();
	}

	public static SpUtil getInstance() {
		return MySpUtilHolder.INSANCE;
	}

	public void setUPDATEURL(String UPDATEURL) {
		mEditor = mSp.edit();
		if (!TextUtils.isEmpty(UPDATEURL)) {
			mEditor.putString("UPDATEURL", UPDATEURL);
		}
		mEditor.apply();
	}

	public String getUPDATEURL() {
		return mSp.getString("UPDATEURL", "http://218.244.151.195/app_version.php?os=2&roleid=2");//2是老师
	}

	public void setWSURI(String WSURI) {
		mEditor = mSp.edit();
		if (!TextUtils.isEmpty(WSURI)) {
			mEditor.putString("WSURI", WSURI);
		}
		mEditor.apply();
	}

	public String getWSURI() {
		return mSp.getString("WSURI", "ws://172.16.1.13:9001/ws");
//		return mSp.getString("WSURI", "ws://218.244.151.195:9001/ws");
	}
	
	public void setHTTPURI(String HTTPURI) {
		mEditor = mSp.edit();
		if (!TextUtils.isEmpty(HTTPURI)) {
			mEditor.putString("HTTPURI", HTTPURI);
		}
		mEditor.apply();
	}
	
	public String getHTTPURI() {
		return mSp.getString("HTTPURI", "http://172.16.1.13:9001/http/mail");
//		return mSp.getString("HTTPURI", "http://218.244.151.195:9001/http/mail");
	}
	

//	public void setAnswerData(long answer_id, String answerData) {
//		mEditor = mSp.edit();
//		mEditor.putLong("answer_id", answer_id);
//		mEditor.putString("answerData", answerData);
//		mEditor.apply();
//	}
//
//	public String getAnswerData(long answer_id) {
//		boolean flag = mSp.getLong("answer_id", 0) == answer_id;
//		if (flag) {
//			return mSp.getString("answerData", "");
//		} else {
//			return "";
//		}
//
//	}
	
	
//	public void setWSURL_HTTP(String WSURL_HTTP) {
//		mEditor = mSp.edit();
//		if (!TextUtils.isEmpty(WSURL_HTTP)) {
//			mEditor.putString("WSURL_HTTP", WSURL_HTTP);
//		}
//		mEditor.apply();
//	}
//	
//	public String getWSURL_HTTP() {
//		return mSp.getString("WSURL_HTTP", "http://172.16.1.20:82/api/");
////		return mSp.getString("WSURL_HTTP", "http://218.244.151.195:82/api/");
//	}
	
	
	public void setGOTP(String GOIP) {
		mEditor = mSp.edit();
		if (!TextUtils.isEmpty(GOIP)) {
			mEditor.putString("GOIP", GOIP);
		}
		mEditor.apply();
	}
	
	
	public String getGOTP() {
		if (AppConfig.IS_ONELINE) {
			return mSp.getString("GOIP", TecApplication.getContext().getResources().getString(R.string.goip_118_text));
		} else {
			return mSp.getString("GOIP", TecApplication.getContext().getResources().getString(R.string.goip_172_text));
		}
	}
	
	
	public void setPYTHONTP(String PYTHONTP) {
		mEditor = mSp.edit();
		if (!TextUtils.isEmpty(PYTHONTP)) {
			mEditor.putString("PYTHONTP", PYTHONTP);
		}
		mEditor.apply();
	}
	
	public String getPYTHONTP() {
		if (AppConfig.IS_ONELINE) {
			return mSp.getString("PYTHONTP", TecApplication.getContext().getResources().getString(R.string.pyip_118_text));
		} else {
			return mSp.getString("PYTHONTP", TecApplication.getContext().getResources().getString(R.string.pyip_172_text));
		}
	}
	
	
	
	
	public void setCheckTag(String tag) {
        mEditor = mSp.edit();
        if (!TextUtils.isEmpty(tag)) {
            mEditor.putString("check_hw_tag",tag);
        }
        mEditor.apply();
    }
	
	   public String getCheckTag() {
	        return mSp.getString("check_hw_tag", "");
	    }
	
}

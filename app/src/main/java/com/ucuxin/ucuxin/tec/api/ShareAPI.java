package com.ucuxin.ucuxin.tec.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestClientAPI;

/**
 * 分享api
 * 
 * @author sky
 *
 */
public class ShareAPI extends VolleyRequestClientAPI {

	/**
	 * 我要分享
	 * 
	 * @param queue
	 * @param taskid
	 * @param kpoint
	 * @param remark
	 * @param sndfile
	 * @param listener
	 * @param requestCode
	 */
	public void wantToShare(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/gopromotinos
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/gopromotinos", dataStr, listener,
				requestCode);

	}

	/**
	 * 我的分享
	 * 
	 * @param queue
	 * @param pagenum
	 * @param pagecount
	 * @param listener
	 * @param requestCode
	 */
	public void myShareList(RequestQueue queue, int pagenum, int pagecount, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/mypromotinos
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("pagenum", pagenum);
		subParams.put("pagecount", pagecount);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/mypromotions", dataStr, listener,
				requestCode);

	}

}

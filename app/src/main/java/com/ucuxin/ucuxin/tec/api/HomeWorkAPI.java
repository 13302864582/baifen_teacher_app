package com.ucuxin.ucuxin.tec.api;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestClientAPI;

import java.util.HashMap;
import java.util.Map;

public class HomeWorkAPI extends VolleyRequestClientAPI {

	/**
	 * 删除单个打点
	 * 
	 * @param queue
	 * @param checkpointid
	 * @param listener
	 * @param requestCode
	 */
	public void delSingleCheckPoint(RequestQueue queue, int checkpointid, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/deletecheckpoint
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("checkpointid", checkpointid);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/deletecheckpoint", dataStr, listener,
				requestCode);

	}

	/**
	 * 获取投诉理由
	 * 
	 * @param queue
	 * @param checkpointid
	 * @param listener
	 * @param requestCode
	 */
	public void getcomplaintreasons(RequestQueue queue, int checkpointid, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/complaintreasons
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("checkpointid", checkpointid);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/complaintreasons", dataStr, listener,
				requestCode);

	}

	
	/**
	 * 换题
	 * @param queue
	 * @param taskid
	 * @param listener
	 * @param requestCode
	 */
	public void changeHomework(RequestQueue queue, int taskid, final BaseFragment listener, final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/changehomework
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("taskid", taskid);
		String dataStr = JSON.toJSONString(subParams);	
		requestHttpFragment(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/changehomework", dataStr, listener,
				requestCode);

	}

}

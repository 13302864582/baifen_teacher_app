package com.ucuxin.ucuxin.tec.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestClientAPI;

public class HomeApI extends VolleyRequestClientAPI {
	/**
	 * 点击事件记录
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void clickevent(String event_code, final BaseActivity listener) {
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("event_code", event_code);

		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(listener.requestQueue, HTTP_METHOD_POST, AppConfig.GO_URL + "user/clickevent", dataStr,
				listener, RequestConstant.TONGJI_CODE);

	}

	/**
	 * 获取分享参数
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 * @param type
	 *            1-ios(家长)/2-andorid 家长/3-andorid 老师',
	 */
	public void getshareTip(RequestQueue queue, final BaseActivity listener, final int requestCode) {

		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_GET, AppConfig.WELEARN_URL + "app_share.php?type=3", dataStr, listener,
				requestCode);

	}

	/**
	 * 问题追问列表
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void questionAppendList(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/questionappendlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/questionappendlist", dataStr, listener,
				requestCode);

	}

	/**
	 * 作业追问列表
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void homeworkAppendList(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/homeworkappendlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homeworkappendlist", dataStr, listener,
				requestCode);

	}

	/**
	 * 问题带采纳
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void daicainaQuestion(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/questiontobeadoptedlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/questiontobeadoptedlist", dataStr,
				listener, requestCode);

	}

	/**
	 * 作业带采纳
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void daicainaHomework(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/homeworktobeadoptedlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homeworktobeadoptedlist", dataStr,
				listener, requestCode);

	}

	/**
	 * 有投诉 投诉列表
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void getTousuList(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/homeworkcomplaintlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homeworkcomplaintlist", dataStr,
				listener, requestCode);
	}

	/**
	 * 仲裁列表
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void getZhongcaiList(RequestQueue queue, int page, int count, final BaseActivity listener,
			final int requestCode) {

		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("page", page);
		subParams.put("count", count);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homeworkarbitratelist", dataStr,
				listener, requestCode);

	}

	/**
	 * 获取首页信息
	 * 
	 * @param queue
	 * @param page
	 * @param count
	 * @param listener
	 * @param requestCode
	 */
	public void HomepageInfo(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/homepageinfo
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		customrequestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homepageinfo", dataStr, listener,
				requestCode);

	}

	/**
	 * 获取通知
	 * 
	 * @param queue
	 * @param listener
	 * @param requestCode
	 */
	public void getNoticeList(RequestQueue queue, final BaseActivity listener, final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/importantnoticelist
		Map<String, Object> subParams = new HashMap<String, Object>();
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/importantnoticelist", dataStr,
				listener, requestCode);

	}

	/**
	 * 申请体现
	 * 
	 * @param queue
	 * @param money
	 * @param listener
	 * @param requestCode
	 */
	public void applyencashment(RequestQueue queue, float money, float input_money, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/applyencashment
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("money", money);
		subParams.put("input_money", input_money);

		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/applyencashment", dataStr, listener,
				requestCode);

	}

	/**
	 * 我的入账
	 * 
	 * @param queue
	 * @param pageindex
	 * @param pagecount
	 * @param listener
	 * @param requestCode
	 */
	public void myIncomerecordlist(RequestQueue queue, int pageindex, int pagecount, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/incomerecordlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("pageindex", pageindex);
		subParams.put("pagecount", pagecount);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/incomerecordlist", dataStr, listener,
				requestCode);

	}

	/**
	 * 我的提现
	 * 
	 * @param queue
	 * @param pageindex
	 * @param pagecount
	 * @param listener
	 * @param requestCode
	 */
	public void myCashedrecordlist(RequestQueue queue, int pageindex, int pagecount, final BaseActivity listener,
			final int requestCode) {
		// http://www.welearn.com:8080/api/teacher/cashedrecordlist
		Map<String, Object> subParams = new HashMap<String, Object>();
		subParams.put("pageindex", pageindex);
		subParams.put("pagecount", pagecount);
		String dataStr = JSON.toJSONString(subParams);
		requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/cashedrecordlist", dataStr, listener,
				requestCode);

	}

}

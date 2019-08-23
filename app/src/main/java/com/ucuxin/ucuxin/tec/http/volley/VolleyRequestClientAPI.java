package com.ucuxin.ucuxin.tec.http.volley;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.base.BaseFragmentActivity;
import com.ucuxin.ucuxin.tec.base.IBaseFragment;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

/**
 * 此类的描述： volley 底层通信 *
 * 
 * @author: Sky
 */
public abstract class VolleyRequestClientAPI {
private static final String TAG=VolleyRequestClientAPI.class.getSimpleName();


	// 提交方式
	public static final String HTTP_METHOD_POST = "POST";

	public static final String HTTP_METHOD_GET = "GET";

	public static final String APP_NAME = "android_fdt_teacher";

	public VolleyRequestClientAPI() {
		super();
	}

	/**
	 * 此方法描述的是：封装公共的参数
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015-7-20 下午7:10:08 getBaseParams
	 * @param params
	 *            void
	 */
	public void getBaseParams(Map<String, String> params) {
		int ver = TecApplication.versionCode;
		params.put("ver", ver + "");
		params.put("appname", APP_NAME);
		String sourcechan = TecApplication.getChannelValue();
		if (TextUtils.isEmpty(sourcechan)) {
			sourcechan = "ucuxin";
		}
		params.put("sourcechan", sourcechan);
		params.put("tokenid", SharePerfenceUtil.getInstance().getWelearnTokenId());

	}



	/**
	 * 此方法描述的是：适用于Activity请求
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:26:39
	 * @version: 2.0 requestHttpActivity
	 * @param requestMethod
	 * @param url
	 * @param map
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void requestHttpActivity(RequestQueue queue, String requestMethod, final String url, final String dataStr,
			final BaseActivity listener, final int requestCode) {
		if (AppConfig.IS_DEBUG){
			LogUtils.e(TAG,url);
		}
		StringRequest request = null;
		if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
			request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}) {
				// 重写getParams方法设置参数
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					getBaseParams(params);
					params.put("data", dataStr);
					return params;
				}

				// @Override
				// public Map<String, String> getHeaders() throws
				// AuthFailureError {
				// HashMap<String, String> headers = new HashMap<String,
				// String>();
				// headers.put("Accept", "application/string");
				// headers.put("Content-Type", "application/json;
				// charset=UTF-8");
				// return headers;
				//
				// }

			};

		} else {// 如果是get请求
			request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		queue.add(request);
		queue.start();
	}

	/**
	 * 此方法描述的是：适用于Fragment请求
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:27:30
	 * @version: 2.0 requestHttpFragment
	 * @param requestMethod
	 * @param url
	 * @param map
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void requestHttpFragment(RequestQueue queue, String requestMethod, final String url, final String dataStr,
			final BaseFragment listener, final int requestCode) {
		if (AppConfig.IS_DEBUG){
			LogUtils.e(TAG,url);
		}
		StringRequest request = null;
		if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
			request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("fragment VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}) {
				// 重写getParams方法设置参数
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					getBaseParams(params);
					params.put("data", dataStr);
					return params;
				}

			};

		} else {// 如果是get请求
			request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("fragment VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}

		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		queue.add(request);
		queue.start();
	}

	/**
	 * 此方法描述的是：适用于FragmentActivity请求
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:42:30
	 * @version: 2.0 requestHttpFragment
	 * @param requestMethod
	 * @param url
	 * @param map
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void requestHttpFragmentActivity(RequestQueue queue, String requestMethod, final String url,
			final String dataStr, final BaseFragmentActivity listener, final int requestCode) {
		if (AppConfig.IS_DEBUG){
			LogUtils.e(TAG,url);
		}
		StringRequest request = null;
		if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
			request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("BaseFragmentActivity VolleyClientAPI onErrorResponse",
									url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}) {
				// 重写getParams方法设置参数
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					getBaseParams(params);
					params.put("data", dataStr);
					return params;
				}

			};

		} else {// 如果是get请求
			request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("BaseFragmentActivity VolleyClientAPI onErrorResponse",
									url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		queue.add(request);
		queue.start();

	}

	/**
	 * 此方法描述的是：适用于接口请求
	 * 
	 * @author: Sky @最后修改人： Sky
	 * @最后修改日期:2015年7月14日 上午10:41:34 requestHttpInterface
	 * @param requestMethod
	 * @param url
	 * @param map
	 * @param listener
	 * @param requestCode
	 *            void
	 */
	public void requestHttpInterface(RequestQueue queue, String requestMethod, final String url, final String dataStr,
			final IBaseFragment listener, final int requestCode) {
		if (AppConfig.IS_DEBUG){
			LogUtils.e(TAG,url);
		}
		StringRequest request = null;
		if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
			request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("BaseInterface VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}) {
				// 重写getParams方法设置参数
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					getBaseParams(params);
					params.put("data", dataStr);
					return params;
				}

			};

		} else {// 如果是get请求
			request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("BaseInterface VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		queue.add(request);
		queue.start();
	}

	public void customrequestHttpActivity(RequestQueue queue, String requestMethod, final String url,
			final String dataStr, final BaseActivity listener, final int requestCode) {
		if (AppConfig.IS_DEBUG){
			LogUtils.e(TAG,url);
		}
		CustomRequest customRequest = null;
		if (HTTP_METHOD_POST.equals(requestMethod)) {// 如果是post请求
			customRequest = new CustomRequest(Request.Method.POST, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}) {
				// 重写getParams方法设置参数
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					getBaseParams(params);
					params.put("data", dataStr);
					return params;
				}

				// @Override
				// public Map<String, String> getHeaders() throws
				// AuthFailureError {
				// HashMap<String, String> headers = new HashMap<String,
				// String>();
				// headers.put("Accept", "application/string");
				// headers.put("Content-Type", "application/json;
				// charset=UTF-8");
				// return headers;
				//
				// }

			};

		} else {// 如果是get请求
			customRequest = new CustomRequest(Request.Method.GET, url, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						listener.resultBack(requestCode, response);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					try {
						if (error != null) {
							// listener.resultBack(-1);
							LogUtils.e("activity VolleyClientAPI onErrorResponse", url + " " + error.getMessage());
							listener.resultBack(-1, requestCode, error);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}
		customRequest.setPriority(Priority.HIGH);
		customRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
		queue.add(customRequest);
		queue.start();
	}

}

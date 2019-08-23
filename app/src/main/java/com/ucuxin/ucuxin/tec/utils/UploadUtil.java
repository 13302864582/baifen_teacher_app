//package com.welearn.welearn.tec.utils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.apache.http.HttpMessage;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.ParseException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.CoreConnectionPNames;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.BasicHttpContext;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.protocol.HttpContext;
//import org.apache.http.util.EntityUtils;
//
//import android.graphics.Bitmap.Config;
//import android.os.AsyncTask;
//import android.text.TextUtils;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.ucuxin.ucuxin.tec.config.AppConfig;
//import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
//import com.ucuxin.ucuxin.tec.model.UploadResult;
//
//public class UploadUtil {
//
//	public static final String TAG = UploadUtil.class.getSimpleName();
//
//	public static final String COOKIE_KEY = "Cookie";
//	public static final String SET_COOKIE_KEY = "Set-Cookie";
//	public static final String WELEARN_SESSION_ID_KEY = "WeLearnSessionID";
//
//	public static String UNKNOW_ERROR_MSG = "网络异常";
//
//	public static void upload(String url, List<NameValuePair> params, Map<String, List<File>> files,
//			OnUploadListener listener, boolean isMainThread, int index) {
//		if (isMainThread) {
//			new UploadTask(url, params, files, listener, index).execute();
//		} else {
//			String result = null;
//			try {
//				result = post(url, params, files);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (null != listener) {
//				if (null != result) {
//					UploadResult ur = new Gson().fromJson(result, UploadResult.class);
//					if (ur != null) {
//						int code = ur.getCode();
//						if (code == 0) {
//							listener.onUploadSuccess(ur, index);
//						} else {
//							listener.onUploadFail(ur, index);
//						}
//					} else {
//						listener.onUploadError(result, index);
//					}
//				} else {
//					listener.onUploadError(UNKNOW_ERROR_MSG, index);
//				}
//			}
//		}
//	}
//
//	static class UploadTask extends AsyncTask<Object, Integer, String> {
//		private String url;
//		private List<NameValuePair> params;
//		private Map<String, List<File>> files;
//		private OnUploadListener listener;
//		private int index;
//
//		public UploadTask(String url, List<NameValuePair> params, Map<String, List<File>> files,
//				OnUploadListener listener, int index) {
//			this.url = url;
//			this.params = params;
//			this.files = files;
//			this.listener = listener;
//			this.index = index;
//		}
//
//		@Override
//		protected String doInBackground(Object... arg0) {
//			return post(url, params, files);
//		}
//
//		@Override
//		protected void onPostExecute(String json) {
//			UNKNOW_ERROR_MSG = "网络异常";
//			if (null != listener) {
//				if (!TextUtils.isEmpty(json)) {
//					if (AppConfig.IS_DEBUG) {
//						LogUtils.e("json-->", json);
//					}
//
//					if ("UnsupportedEncodingException".equals(json)) {
//						if (AppConfig.IS_DEBUG) {
//							UNKNOW_ERROR_MSG = "UnsupportedEncodingException";
//						}
//						listener.onUploadError(UNKNOW_ERROR_MSG, index);
//					} else if ("ClientProtocolException".equals(json)) {
//						if (AppConfig.IS_DEBUG) {
//							UNKNOW_ERROR_MSG = "ClientProtocolException";
//						}
//						listener.onUploadError(UNKNOW_ERROR_MSG, index);
//					} else if ("IOException".equals(json)) {
//						if (AppConfig.IS_DEBUG) {
//							UNKNOW_ERROR_MSG = "IOException";
//						}
//						listener.onUploadError(UNKNOW_ERROR_MSG, index);
//					} else if ("Exception".equals(json)) {
//						if (AppConfig.IS_DEBUG) {
//							UNKNOW_ERROR_MSG = "Exception";
//						}
//						listener.onUploadError(UNKNOW_ERROR_MSG, index);
//
//					} else {
//						UploadResult ur = new UploadResult();
//						int code = JsonUtils.getInt(json, "Code", -1);
//						String msg = JsonUtils.getString(json, "Msg", "");
//						String data = JsonUtils.getString(json, "Data", "");
//						ur.setCode(code);
//						ur.setData(data);
//						ur.setMsg(msg);
//						if (code == 0) {
//							listener.onUploadSuccess(ur, index);
//						} else {
//							listener.onUploadFail(ur, index);
//						}
//					}
//
//				} else {
//					listener.onUploadError(UNKNOW_ERROR_MSG, index);
//				}
//			}
//		}
//	}
//
//	private static String post(String url, List<NameValuePair> nameValuePairs, final Map<String, List<File>> files) {
//
//		try {
//			// HttpClient httpClient = new DefaultHttpClient();
//			// httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
//			// 5000);
//			// httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);
//			HttpParams httpParams = new BasicHttpParams();
//			// 与服务器的socket连接，这就是该socket连接的超时时间
//			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
//			// Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
//			HttpConnectionParams.setSoTimeout(httpParams, 30000);
//			HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
//			HttpClient httpClient = new DefaultHttpClient(httpParams);
//
//			HttpContext localContext = new BasicHttpContext();
//			HttpPost httpPost = new HttpPost(url);
//
//			// if (AppConfig.IS_DEBUG) {
//			// ToastUtils.show("sessionId-->" +
//			// MySharePerfenceUtil.getInstance().getWelearnTokenId());
//			// }
//			httpPost.addHeader(COOKIE_KEY,
//					WELEARN_SESSION_ID_KEY + "=" + SharePerfenceUtil.getInstance().getWelearnTokenId());
//			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//			for (int index = 0; index < nameValuePairs.size(); index++) {
//				entity.addPart(nameValuePairs.get(index).getName(),
//						new StringBody(nameValuePairs.get(index).getValue(), Charset.forName(HTTP.UTF_8)));
//			}
//			if (files != null && files.size() > 0) {
//				Set<Entry<String, List<File>>> entries = files.entrySet();
//				for (Entry<String, List<File>> entry : entries) {
//					String key = entry.getKey();
//					List<File> fList = entry.getValue();
//					if (null != fList) {
//						for (File f : fList) {
//							entity.addPart(key, new FileBody(f));
//						}
//					}
//				}
//			}
//
//			httpPost.setEntity(entity);
//			HttpResponse response = httpClient.execute(httpPost, localContext);
//			return EntityUtils.toString(response.getEntity(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return "UnsupportedEncodingException";
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			return "ClientProtocolException";
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "IOException";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Exception";
//		}
//
//	}
//
//	public interface OnUploadListener {
//		void onUploadSuccess(UploadResult result, int index);
//
//		void onUploadError(String msg, int index);
//
//		void onUploadFail(UploadResult result, int index);
//	}
//
//}


package com.ucuxin.ucuxin.tec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.constant.WxConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.BitmapUtils;
import com.ucuxin.ucuxin.tec.utils.BtnUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.PackageManagerUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 此类的描述：公用 WebViewActivity
 */
public class WebViewActivity extends BaseActivity implements OnKeyListener {

	private WebView mWebView;

	private TextView titleTextView;

	private String title;

	private String url;

	private RelativeLayout back_layout;

	private UserInfoModel userInfo;
	private String shareTitle, shareDesc, shareUrl, shareimageUrl;
	private int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_webview);

		getData();
		initView();
		
		initListener();
		initData();
	}

	private void getData() {
		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		isshowcall = getIntent().getStringExtra("isshowcall");

	}

	public void initView() {
		back_layout = (RelativeLayout) findViewById(R.id.back_layout);
		titleTextView = (TextView) findViewById(R.id.title);
		mWebView = (WebView) findViewById(R.id.webview);
		titleTextView.setText(title);
		webviewSetting();

	}

	public void initListener() {
		back_layout.setOnClickListener(this);
	}

	private void webviewSetting() {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("utf-8");
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);// 设定支持缩放
		mWebView.requestFocus();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			Log.e("back-->", "back");
			finish();
			break;

		default:
			break;
		}
	}

	private void initData() {

		showDialog(getString(R.string.load_more));

		mWebView.loadUrl(url);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setDownloadListener(mDownloadListener);
		mWebView.setOnKeyListener(this);
	}

	WebViewClient mWebViewClient = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {
			closeDialog();
			super.onPageFinished(view, url);
		}

		@Override

		public boolean shouldOverrideUrlLoading(WebView view, String url) {		
			 LogUtils.e("url-->",url);
			boolean isFlag = true;
			
			if (url.contains("phonecallsystem")) {// 索券打电话
				int pos = url.indexOf("m:");
				String phoneNum = url.substring(pos + 2);
				callPhone(phoneNum);
				isFlag = true;

			} else if (url.contains("jump:parents_pay")) {// 跳转到充值

				
				isFlag = true;
			} else if (url.contains("share_wechat")) {
				type = 1;
				SharePerfenceUtil.getInstance().putInt("sharekey", type);
				new HomeApI().getshareTip(requestQueue, WebViewActivity.this, RequestConstant.GET_SHARE_CODE);
			} else if (url.contains("share_qq")) {
				type = 2;
				LogUtils.e("share_qq_execute", "ssss");
				new HomeApI().getshareTip(requestQueue, WebViewActivity.this, RequestConstant.GET_SHARE_CODE);

			} else if (url.contains("share_qzone")) {
				type = 3;
				new HomeApI().getshareTip(requestQueue, WebViewActivity.this, RequestConstant.GET_SHARE_CODE);

			} else if (url.contains("share_timeline")) {
				type = 4;
				SharePerfenceUtil.getInstance().putInt("sharekey", type);
				new HomeApI().getshareTip(requestQueue, WebViewActivity.this, RequestConstant.GET_SHARE_CODE);

			} else {
				view.loadUrl(url);
				isFlag = true;
			}

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false

			return isFlag;

		}
	};

	WebChromeClient mWebChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {

			if (newProgress == 55) {
				closeDialog();
			}
			super.onProgressChanged(view, newProgress);
		}

	};

	DownloadListener mDownloadListener = new DownloadListener() {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
				long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	};

	private String isshowcall;

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}

	/**
	 * 直接打电话
	 */
	private void callPhone(String phoneNumber) {
		// 用intent启动拨打电话
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			final ViewGroup viewGroup = (ViewGroup) mWebView.getParent();
			if (viewGroup != null) {
				viewGroup.removeView(mWebView);
			}
			mWebView.destroy();
			mWebView = null;
		}

		super.onDestroy();
	}

	@Override
	public void resultBack(Object... param) {
		super.resultBack(param);
		
		int flag = ((Integer) param[0]).intValue();
		switch (flag) {
		case RequestConstant.GET_SHARE_CODE:
			if (param.length > 0 && param[1] != null && param[1] instanceof String) {
				String datas = param[1].toString();
				int code = JsonUtils.getInt(datas, "Code", -1);
				String msg = JsonUtils.getString(datas, "Msg", "");
				
				if (code == 0) {
					try {
						String dataJson = JsonUtils.getString(datas, "Data", "");
						if (!TextUtils.isEmpty(dataJson)) {
							shareTitle = JsonUtils.getString(dataJson, "title", "");
							shareDesc = JsonUtils.getString(dataJson, "desc", "");
							shareimageUrl= JsonUtils.getString(dataJson, "img", "");
							shareUrl = JsonUtils.getString(dataJson, "link", "");
							String codeUrl = "";
							userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
							if (TextUtils.isEmpty(shareTitle) | TextUtils.isEmpty(shareDesc)
									| TextUtils.isEmpty(shareimageUrl) | TextUtils.isEmpty(shareUrl)) {

							} else{
								
								if (shareUrl.contains("?")) {
									codeUrl = shareUrl + "userid={0}&phoneos={1}&appversion={2}";
									codeUrl = codeUrl.replace("{0}", userInfo.getUserid() + "")
											.replace("{1}", "android")
											.replace("{2}", PackageManagerUtils.getVersionCode() + "");									
								} else {
									codeUrl = shareUrl + "?userid={0}&phoneos={1}&appversion={2}";
									codeUrl = codeUrl.replace("{0}", userInfo.getUserid() + "")
											.replace("{1}", "android")
											.replace("{2}", PackageManagerUtils.getVersionCode() + "");									
									
								}
								shareUrl=codeUrl;								
								if (type == 1) {
									new DownloadShotImageTask().execute(shareimageUrl);
								} else if (type == 2) {
									shareTomQQ();
								} else if (type == 3) {
									shareTomQzone();
								} else if (type == 4) {
									new DownloadShotImageTask().execute(shareimageUrl);
								}	
								
							}
						
							
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				} else {
					ToastUtils.show(msg);
				}

			}
			break;
		}
	}

	private void shareTomWW() {
		boolean registerApp = TecApplication.api.registerApp(WxConstant.APP_ID_WW);
		if (!TecApplication.api.isWXAppInstalled()) {
			Toast.makeText(this, "微信未安装", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!registerApp) {
			Toast.makeText(this, "微信注册失败", Toast.LENGTH_SHORT).show();
			return;
		}
		WXWebpageObject webObj = new WXWebpageObject(shareUrl);

		WXMediaMessage msg = new WXMediaMessage(webObj);
		msg.title = shareTitle;
		msg.description = shareDesc;
		msg.mediaTagName = getString(R.string.app_name);
		Bitmap thumb = BitmapFactory.decodeFile(MyFileUtil.getShotImagePath());
		msg.thumbData = BitmapUtils.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;

		TecApplication.api.sendReq(req);
	}

	private void shareTomQQ() {
		final Bundle bundle = new Bundle();
		bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);
		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareDesc);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareimageUrl);
		Tencent  mTencent=null;
		if (TecApplication.mTencent==null) {
			mTencent = Tencent.createInstance(WxConstant.APP_ID_QQ, getApplicationContext());
		}else {
			mTencent=TecApplication.mTencent;
		}
		mTencent.shareToQQ(this, bundle, new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				ToastUtils.show(R.string.invite_error);
			}

			@Override
			public void onComplete(Object arg0) {
				BtnUtils.clickevent("share_qq", WebViewActivity.this);
			}

			@Override
			public void onCancel() {
				ToastUtils.show(R.string.invite_cancel);
			}

		});
	}

	private void shareTomQuan() {
		boolean registerApp = TecApplication.api.registerApp(WxConstant.APP_ID_WW);
		if (!TecApplication.api.isWXAppInstalled()) {
			Toast.makeText(this, "微信未安装", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!registerApp) {
			Toast.makeText(this, "微信注册失败", Toast.LENGTH_SHORT).show();
			return;
		}
		WXWebpageObject webObj = new WXWebpageObject(shareUrl);

		WXMediaMessage msg = new WXMediaMessage(webObj);
		msg.title = shareTitle;
		msg.description = shareDesc;
		msg.mediaTagName = getString(R.string.app_name);
		Bitmap thumb = BitmapFactory.decodeFile(MyFileUtil.getShotImagePath());
		msg.thumbData = BitmapUtils.bmpToByteArray(thumb, true);

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		boolean flag = TecApplication.api.sendReq(req);
	}

	private void shareTomQzone() {
		final Bundle bundle = new Bundle();
		bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareTitle);
		bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareDesc);
		bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
		bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 1);
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareimageUrl);
		TecApplication.mTencent.shareToQQ(this, bundle, new IUiListener() {
			@Override
			public void onError(UiError arg0) {
				ToastUtils.show(R.string.invite_error);
			}

			@Override
			public void onComplete(Object arg0) {
				BtnUtils.clickevent("share_qzone", WebViewActivity.this);
			}

			@Override
			public void onCancel() {
				ToastUtils.show(R.string.invite_cancel);
			}
		});
	}

	class DownloadShotImageTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 0) {
				if (type == 1) {
					shareTomWW();
				} else if (type == 4) {
					shareTomQuan();
				}
			}
			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(String... urls) {
			int result = -1;
			String urlStr = urls[0];

			try {
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod("GET");
				InputStream inStream = conn.getInputStream();
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String welcomeImgPath = MyFileUtil.getShotImagePath();
					File wFile = new File(welcomeImgPath);
					wFile.deleteOnExit();
					File tFile = new File(MyFileUtil.getShotImagePath() + ".tmp");
					tFile.deleteOnExit();
					FileOutputStream fos = null;
					try {
						InputStream is = inStream;
						fos = new FileOutputStream(tFile);
						byte[] buffer = new byte[1024];
						int size = 0;
						int len = -1;
						while ((len = is.read(buffer)) != -1) {
							size += len;
							fos.write(buffer, 0, len);
						}
						fos.flush();
						tFile.renameTo(wFile);
						result = 0;

					} catch (IOException e) {
						e.printStackTrace();
						result = -1;
					} catch (Exception e) {
						e.printStackTrace();
						result = -1;
					} finally {
						try {
							if (null != fos) {
								fos.close();
							}
						} catch (Exception e2) {
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}
}

package com.ucuxin.ucuxin.tec.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.dispatch.ImMsgDispatch;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public abstract class AbstractUpdateManager {

	protected Context mContext;

	// 提示语
	private String updateMsg = "亲！有您最新的安装包哦，速度升级呀~"; // update by milo ,
														// 2014.09.15

	// 返回的安装包url
	// private String apkUrl;

	// private int mCurrentVersionConde;

	private Dialog noticeDialog;

	protected Dialog downloadDialog;
	/* 下载包安装路径 */
	private static final String savePath = Environment.getExternalStorageDirectory().toString() + "/";

	private static final String saveFileName = savePath + "welearn_tec.apk";

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private static final int SHOW_NOTICE = 5;

	private static final int SDCARD_NOT_NOUNTED = 3;

	private static final int SHOW_NOTICE_NOTFORCE = 6;

	protected int progress;

	private Thread downLoadThread;

	protected boolean interceptFlag = false;

	// private boolean isManual;

	// protected String upMsg;

	private static final String TAG = AbstractUpdateManager.class.getSimpleName();

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				setResult();
				break;
			case DOWN_OVER:
				installApk();
				break;
			case SHOW_NOTICE:// 强制升级
				cloesNoticeDialog();
				showNoticeDialog(true);
				break;
			case SHOW_NOTICE_NOTFORCE:// 不强制升级
				cloesNoticeDialog();
				showNoticeDialog(false);
				break;
			default:
				break;
			}
		}
	};

	protected abstract void showResult();

	protected abstract void setResult();
//	private ImMsgDispatch mDispatch = 
	WeakReference<ImMsgDispatch> mDispatch=new WeakReference<ImMsgDispatch>(new ImMsgDispatch() {

		@Override
		public Bundle handleImMsg(Message msg) {
			switch (msg.what) {
			case MsgConstant.MSG_DEF_OBTAIN_UPDATE_SUCCESS:
				JSONObject obj = (JSONObject) msg.obj;
				if (obj == null) {
					cloesNoticeDialog();
					return null;
				}

				try {
					int mCurrentVersionConde = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),
							0).versionCode;

					int versionCode = Integer.parseInt(JsonUtils.getString(obj, "versioncode", ""));
					SharePerfenceUtil.getInstance().setLatestVersion(versionCode);

					String title = JsonUtils.getString(obj, "title", "");
					SharePerfenceUtil.getInstance().setUpdateTitle(title);

					String content = JsonUtils.getString(obj, "content", "");
					SharePerfenceUtil.getInstance().setUpdateContent(content);

					String apkUrl = JsonUtils.getString(obj, "url", "");
					SharePerfenceUtil.getInstance().setUpdateUrl(apkUrl);

					String compel = JsonUtils.getString(obj, "compel", "");
					if (mCurrentVersionConde < versionCode) {
						if (compel.equals("yes")) {
							mHandler.sendEmptyMessageDelayed(SHOW_NOTICE, 2000);
						} else {
							mHandler.sendEmptyMessageDelayed(SHOW_NOTICE_NOTFORCE, 2000);
						}

					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case SDCARD_NOT_NOUNTED:
				cloesNoticeDialog();
				ToastUtils.show("无法下载安装文件，请确认SD卡是否安装");
				break;
			}
			return null;
		}
	});
	

	public void cloesNoticeDialog() {
		if (noticeDialog != null && noticeDialog.isShowing()) {
			noticeDialog.dismiss();
		}
	}

	public AbstractUpdateManager(Context context) {
		this.mContext = context;
		if (!WelearnHandler.getInstance().getHandler().isRegisterObserver(mDispatch.get(), TAG)) {
			WelearnHandler.getInstance().getHandler().registerObserver(mDispatch.get(), TAG);
		}
	}

	/**
	 * 程序自动检测更新
	 * 
	 * @param obj
	 */
	public void checkUpdateInfo() {
		WeLearnApi.checkUpdate();
	}

	/**
	 * 手动检测更新
	 */
	// public void maunalCheckUpdateInfo() {
	// isManual = true;
	// WeLearnApi.checkUpdate();
	// }

	public void showNoticeDialog(boolean isYes) {
		if (mContext != null && !((Activity) mContext).isFinishing()) {
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(SharePerfenceUtil.getInstance().getUpdateTitle());
			builder.setMessage(SharePerfenceUtil.getInstance().getUpdateContent());
			builder.setCancelable(false);
			builder.setPositiveButton("升级", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					showDownloadDialog();
				}
			});
			if (!isYes) {
				builder.setNegativeButton("以后再说", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}
			noticeDialog = builder.create();
			try {
				noticeDialog.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void showDownloadDialog() {
		showResult();
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		public void run() {
			if (!TecApplication.mNetworkUtil.isInternetConnected(mContext)) {
				ToastUtils.show("网络呀，你不要抖动的这么厉害呀！好吧，过会儿再试试");
			}
			try {
				URL url = new URL(SharePerfenceUtil.getInstance().getUpdateUrl());

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				String state = Environment.getExternalStorageState();
				String apkFile = "";
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					apkFile = saveFileName;
				}
				if (TextUtils.isEmpty(apkFile)) {
					mHandler.sendEmptyMessage(SDCARD_NOT_NOUNTED);
				}
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
package com.ucuxin.ucuxin.tec.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.dispatch.UIMsgHandler;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.manager.IntentManager;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * 网络连接类
 * @author parsonswang
 */
public class NetworkUtils {

	private WebSocketConnection mConnction;
	private Handler mHandler;

	public static final String TAG = NetworkUtils.class.getSimpleName();

	private NetworkUtils() {
		this.mConnction = new WebSocketConnection();
		this.mHandler = new Handler(Looper.getMainLooper());
	}

	private static class NetworkManagerHolder {
		private static final NetworkUtils INSANCE = new NetworkUtils();
	}

	public static NetworkUtils getInstance() {
		return NetworkManagerHolder.INSANCE;
	}

	/**
	 * 检测当前是否有可用网络
	 * 
	 * @return
	 */
	public boolean isInternetConnected(Context context) {
		boolean netConnected = false;
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				netConnected = false;
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					int len = info.length;
					for (int i = 0; i < len; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							netConnected = true;
						}
					}
				}
			}
		} catch (Exception e) {
			netConnected = false;
		}
		return netConnected;
	}

	/**
	 * websocket 发送websocket请求
	 * 
	 * @param url
	 *            websocket url
	 * @param listener
	 *            callback
	 */
	public void connectionServer(String wsUri, WebSocketConnectionHandler listener) {
		try {
//			LogUtils.e("PushService", "connect state = " + (mConnction.isConnected()));
			if (!mConnction.isConnected()) {
				mConnction.connect(wsUri, listener);
			}
		} catch (WebSocketException e) {
			if (mConnction.isConnected()) {
				mConnction.disconnect();
			}
		}
	}

	public synchronized void sendPingMessage(byte[] msg) {
		if (mConnction != null && msg != null) {
			if (msg.length > 0 && mConnction.isConnected()) {
				// Log.i(TAG, "---send ping---");
				mConnction.sendPingMessage(msg);
			}
		}
	}

	public synchronized void sendTextmessage(final String message) {
		if (mConnction != null) {
			// LogUtils.e("PushService", "2 connected = " + (mConnction.isConnected()) + ",conn = " + mConnction);
			if (mConnction.isConnected()) {
				mConnction.sendTextMessage(message);
			} else {
				this.mHandler.post(new Runnable() {

					@Override
					public void run() {
						processNoNetworkConn();
					}
				});
			}
		}
	}



	public boolean isSocketConnected() {
		if (null != mConnction) {
			return mConnction.isConnected();
		}
		return false;
	}
	
	public void disConnect() {
		if (mConnction.isConnected()) {
			mConnction.disconnect();
		}
	}

	public void processNoNetworkConn() {
		Message msg = WelearnHandler.getInstance().getHandler().obtainMessage();
		msg.what = MsgConstant.MSG_DEF_SVR_ERROR;
		WelearnHandler.getInstance().getHandler().sendMessage(msg);		
		IntentManager.startWService(TecApplication.getContext());
	}

	public void sendTimeoutMsg() {
		UIMsgHandler mUiMsgHandler = WelearnHandler.getInstance().getHandler();
		if (mUiMsgHandler == null) {
			return;
		}
		Message msg = mUiMsgHandler.obtainMessage();
		if (msg == null) {
			msg = new Message();
		}
		msg.what = MsgConstant.MSG_DEF_CONN_TIMEOUT;
		mUiMsgHandler.sendMessage(msg);
	}
}

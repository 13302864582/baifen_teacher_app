package com.ucuxin.ucuxin.tec.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.NetworkUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 网络监听类
 * @author sky
 *
 */
public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
		if (intent == null ) {
			return;
		}
		if (context == null) {
			context = TecApplication.getContext();
		}
		
		try {
			String action = intent.getAction();
			if (action.isEmpty()) {
				return;
			}
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = connectivityManager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				State wifiState = wifiInfo.getState();
				State mobileState = connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_MOBILE).getState();
				boolean isConnected = wifiState != null
						&& mobileState != null
						&& (State.CONNECTED == wifiState
								|| wifiState == State.CONNECTING
								|| State.CONNECTED == mobileState || State.CONNECTING == mobileState);
				if (!isConnected) {
					ToastUtils.show("断网重新连接失败");
					if (TecApplication.mNetworkUtil == null) {
						TecApplication.mNetworkUtil = NetworkUtils.getInstance();
					}
					TecApplication.mNetworkUtil.sendTimeoutMsg();
				} else {
					ToastUtils.show("断网重新连接成功");
					WeLearnApi.sendSessionToServer();
					IntentManager.startWService(context);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
package com.ucuxin.ucuxin.tec.utils;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ucuxin.ucuxin.tec.TecApplication;

public class PhoneUtils {
	
	private static PhoneUtils mPhoneUtils;
	private static TelephonyManager tm;
	
	public static PhoneUtils getInstance(){
		if(null == mPhoneUtils){
			mPhoneUtils = new PhoneUtils();
		}
		return mPhoneUtils;
	}
	
	private PhoneUtils() {
		tm = (TelephonyManager) TecApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
	}

	public String getIemi() {
		return tm.getDeviceId();
	}

	public String getDeviceUUID() {
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		if (TextUtils.isEmpty(tmSerial) && TextUtils.isEmpty(tmDevice)) {
			return "";
		}
		androidId = ""
				+ android.provider.Settings.Secure.getString(TecApplication.getContext().getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

}

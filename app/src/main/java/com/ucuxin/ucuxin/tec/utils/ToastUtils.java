package com.ucuxin.ucuxin.tec.utils;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {

	private static Toast globalToast;

	@SuppressLint("ShowToast")
	private static Toast getToast() {
		if (null == globalToast) {
			globalToast = Toast.makeText(TecApplication.getContext(), "", Toast.LENGTH_SHORT);
		}
		return globalToast;
	}

	public static void show(int resId) {
		try {
			show(TecApplication.getContext().getResources().getString(resId), Toast.LENGTH_SHORT);
		} catch (Exception e) {
			e.printStackTrace();
			show(String.valueOf(resId), Toast.LENGTH_SHORT);
		}
	}

	public static void show(CharSequence text) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		show(text, Toast.LENGTH_SHORT);
	}

	public static void show(final CharSequence text, final int duration) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		if (GlobalVariable.mainActivity != null) {
			GlobalVariable.mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast t = getToast();
					t.setText(text);
					t.setDuration(duration);
					t.show();
				}
			});
		} else {
			Toast t = getToast();
			t.setText(text);
			t.setDuration(duration);
			t.show();
		}

	}
	
	
	public static void showCenterToast(Context context, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 80, 80);
		toast.show();
	}
	
	
	public static void showCustomToast(Context context, String msg) {
		Toast toast = null;
		if (context != null && !TextUtils.isEmpty(msg)) {
			toast = new Toast(context);
			View toastView = View.inflate(context, R.layout.custom_toast_layout, null);
			ImageView iv = ((ImageView) toastView.findViewById(R.id.tips_icon));
			TextView tv_msg = ((TextView) toastView.findViewById(R.id.tips_msg));
			iv.setImageResource(R.drawable.ic_launcher);
			tv_msg.setText(msg);
			toast.setView(toastView);
			toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
			toast.show();
		}
	}
	
}

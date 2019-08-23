package com.ucuxin.ucuxin.tec.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class UpdateManagerForDialog extends AbstractUpdateManager {

	private Dialog downloadDialog;
	
	/* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
	
	public UpdateManagerForDialog(Context context) {
		super(context);
	}

	@Override
	protected void showResult() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(SharePerfenceUtil.getInstance().getUpdateTitle());
		builder.setMessage("正在下载，请稍后");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		
		builder.setView(v);
		builder.setCancelable(false);
		builder.setNegativeButton("取消", new OnClickListener() {	
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
				((Activity) mContext).finish();
				Process.killProcess(Process.myPid());
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
	}

	@Override
	protected void setResult() {
		mProgress.setProgress(progress);
	}

}

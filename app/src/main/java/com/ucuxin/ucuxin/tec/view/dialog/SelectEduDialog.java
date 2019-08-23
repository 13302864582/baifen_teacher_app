package com.ucuxin.ucuxin.tec.view.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ucuxin.ucuxin.tec.R;

public class SelectEduDialog extends Dialog implements android.view.View.OnClickListener {
	private Context context;

	private int oldEdu;
	private int selectEdu;
	private RadioGroup eduRadioGroup;

	private OnEduUpdatedListener mOnSexUpdatedListener;

	public void setmOnSexUpdatedListener(OnEduUpdatedListener mOnEduUpdatedListener) {
		this.mOnSexUpdatedListener = mOnEduUpdatedListener;
	}

	public interface AdoptSubmitBtnClick {
		void ensure(int degree, String comment);
	}

	public SelectEduDialog(Context context) {
		super(context);
	}

	public SelectEduDialog(Context context, int oldSex, OnEduUpdatedListener listener) {
		super(context, R.style.adoptHomeWorkRatingBar);
		this.context = context;
		selectEdu = this.oldEdu = oldSex;
		this.mOnSexUpdatedListener = listener;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = View.inflate(context, R.layout.choose_edu_dialog, null);
		setContentView(view);
		ActionBar bar = getActionBar();
		if (bar != null) {
			bar.hide();
		}

		eduRadioGroup = (RadioGroup) findViewById(R.id.edu_group_layout);

		for (int i = 0; i < eduRadioGroup.getChildCount(); i++) {
			View v = eduRadioGroup.getChildAt(i);
			if (v instanceof RadioButton) {
				int tag = Integer.parseInt(v.getTag().toString());
				if (tag == oldEdu) {
					((RadioButton) v).setChecked(true);
				} else {
					((RadioButton) v).setChecked(false);
				}
			}
		}
		eduRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.edu_0_rb:
					selectEdu = 0;
					break;
				case R.id.edu_1_rb:
					selectEdu = 1;
					break;
				case R.id.edu_2_rb:
					selectEdu = 2;
					break;
				case R.id.edu_3_rb:
					selectEdu = 3;
					break;
				}
				if (null != mOnSexUpdatedListener) {
					mOnSexUpdatedListener.onEduUpdated(selectEdu);
				}
				dismiss();
			}
		});
//		findViewById(R.id.submit_adopt_btn).setOnClickListener(this);
//		findViewById(R.id.dialog_layout).setOnClickListener(this);
//		findViewById(R.id.root_adopt_dialog).setOnClickListener(this);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.dialog_layout:
//			break;
//		case R.id.submit_adopt_btn:
//
//			if (selectEdu == oldEdu) {
//				this.cancel();
//				return;
//			}
//
//			JSONObject obj = new JSONObject();
//			try {
//				obj.put("edulevel", selectEdu);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			WeLearnApi.updateUserInfoFromServer(context, obj, new HttpListener() {
//				@Override
//				public void onSuccess(int code, String dataJson, String errMsg) {
//					if (code == 0) {
//						ToastUtils.show(R.string.modifyinfosuccessful);
//						if (null != mOnSexUpdatedListener) {
//							mOnSexUpdatedListener.onEduUpdated();
//						}
//					} else {
//						if (!TextUtils.isEmpty(errMsg)) {
//							ToastUtils.show(errMsg);
//						} else {
//							ToastUtils.show(R.string.modifyinfofailed);
//						}
//					}
//					SelectEduDialog.this.dismiss();
//				}
//
//				@Override
//				public void onFail(int HttpCode) {
//					ToastUtils.show(R.string.modifyinfofailed);
//					SelectEduDialog.this.dismiss();
//				}
//			});
//			break;
//		case R.id.root_adopt_dialog:
//			dismiss();
//			break;
//		}
	}

	public interface OnEduUpdatedListener {
		void onEduUpdated(int eduLevel);
	}
}

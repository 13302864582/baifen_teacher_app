package com.ucuxin.ucuxin.tec.function.partner;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

public class UserRequestActivity extends BaseActivity implements OnClickListener, HttpListener {

	public static final String TAG = UserRequestActivity.class.getSimpleName();
	private EditText textarea = null;

	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.fragment_user_request);
		textarea = (EditText) findViewById(R.id.user_request_text);

		setWelearnTitle(R.string.question_feedback);

		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_commit);
		nextStepLayout.setOnClickListener(this);
		textarea.requestFocus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			if (System.currentTimeMillis() - clickTime < 5000) {
				return;
			}
			clickTime = System.currentTimeMillis();
			String subText = textarea.getText().toString();
			if (subText != null && !subText.isEmpty() && !TextUtils.isEmpty(subText.trim())) {
				// ToastUtils.show(getActivity(),
				// "Thanks for your requisition");

				if (subText.trim().length() < 10 || subText.trim().length() > 200) {
					ToastUtils.show(R.string.content_too_short);
					return;
				}

				showDialog(getString(R.string.text_commiting_please_wait), false);
				WeLearnApi.addFeedBack(this, subText, this);
			} else {
				ToastUtils.show(R.string.please_enter_content);
			}
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		closeDialog();
		if (code == 0) {
			ToastUtils.show(R.string.tks_for_feedback);
			finish();
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
		closeDialog();
		ToastUtils.show(R.string.invite_commit_failed_retry);
	}
}

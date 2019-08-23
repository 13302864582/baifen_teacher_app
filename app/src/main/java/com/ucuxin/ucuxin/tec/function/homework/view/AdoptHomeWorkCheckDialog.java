package com.ucuxin.ucuxin.tec.function.homework.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 采纳答案并且提交满意度评价的dialog
 */
public class AdoptHomeWorkCheckDialog extends Dialog implements
		android.view.View.OnClickListener {
	public interface AdoptSubmitBtnClick{
		void ensure(int degree , String comment);
	}
	private RatingBar mDegreeRatingBar;
	private AdoptSubmitBtnClick mReset;
	private EditText mCommentEditText;
	
	public AdoptHomeWorkCheckDialog(Context context) {
		super(context);
	}

	public AdoptHomeWorkCheckDialog(Context context,
			AdoptSubmitBtnClick reset) {
		super(context, R.style.adoptHomeWorkRatingBar);
		this.mReset = reset;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.adopt_homework_dialog);
		ActionBar bar = getActionBar();
		if (bar!=null) {
			bar.hide();
		}
		
		mDegreeRatingBar = (RatingBar) findViewById(R.id.degree_ratingBar_adopt_dialog);
		mCommentEditText = (EditText) findViewById(R.id.comment_et_adopt_dialog);

		findViewById(R.id.submit_adopt_btn).setOnClickListener(this);

		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉周围黑边
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
		params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.42);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.submit_adopt_btn:
			int degree = mDegreeRatingBar.getProgress();
			if (degree == 0) {
				ToastUtils.show(R.string.text_please_choose_satisfaction);
				return;
			}
			String comment = mCommentEditText.getText().toString().trim();
			if (TextUtils.isEmpty(comment)) {
				comment = "";
			}
			mReset.ensure(degree , comment);
			
			break;

		default:
			break;
		}
	}
}

package com.ucuxin.ucuxin.tec.function.homework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;

public class RightWrongPointView extends FrameLayout {
	private int showcomplainttype;
	private int isRight;
	private ImageView mNextBtn;
	private ImageView mRightView;
	private ImageView mWrongView;
	private int checkPointState;//点

	public int getType() {
		return showcomplainttype;
	}

	public void setType(int type) {
		this.showcomplainttype = type;
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			changWrong();
		} else {
			changRight();
		}

	}

	public ImageView getmNextBtn() {
		return mNextBtn;
	}

	public ImageView getImageView() {
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			return mWrongView;
		} else {
			return mRightView;
		}
	}

	public RightWrongPointView(Context context, int isRight, int showcomplainttype,int checkPointState) {
		super(context);
		this.isRight = isRight;
		this.showcomplainttype = showcomplainttype;
		this.checkPointState = checkPointState;
		setupViews();
	}

	public RightWrongPointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.rightwrong_point_view, null);
		this.mRightView = (ImageView) view.findViewById(R.id.right_point_iv);
		this.mWrongView = (ImageView) view.findViewById(R.id.wrong_point_iv);
		this.mNextBtn = (ImageView) view.findViewById(R.id.gointo_btn_rightwrong_point);
		if (isRight == GlobalContant.WRONG_HOMEWORK) {
			mRightView.setVisibility(View.GONE);
			mWrongView.setVisibility(View.VISIBLE);
			changWrong();
		} else {
			mRightView.setVisibility(View.VISIBLE);
			mWrongView.setVisibility(View.GONE);
			changRight();
		}
		addView(view);
	}

	public void changRight() {
		if (showcomplainttype == 0) {

			mRightView.setImageResource(R.drawable.dui_1);
		} else if (showcomplainttype == 1) {
			mRightView.setImageResource(R.drawable.dui_2);

		} else if (showcomplainttype == 2) {
			mRightView.setImageResource(R.drawable.dui_3);

		}
	}

	public void changWrong() {

		if (showcomplainttype == 0) {

			mNextBtn.setImageResource(R.drawable.gointo_point_btn_ic);
			if (checkPointState == 3) {
				mNextBtn.setImageResource(R.drawable.zhuiwen_icon);
			}
		} else if (showcomplainttype == 1) {
			mNextBtn.setImageResource(R.drawable.su);
			if (checkPointState == 3) {
				mNextBtn.setImageResource(R.drawable.zhuiwen_icon);
			}

		} else if (showcomplainttype == 2) {
			mNextBtn.setImageResource(R.drawable.jiu);
			if (checkPointState == 3) {
				mNextBtn.setImageResource(R.drawable.zhuiwen_icon);
			}

		}
	}
}

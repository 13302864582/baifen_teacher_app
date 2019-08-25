package com.ucuxin.ucuxin.tec.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.view.dialog.effects.BaseEffects;

public class WelearnDialogBuilder extends Dialog implements DialogInterface {

	private final String defTextColor = "#FFFFFFFF";

	private final String defDividerColor = "#11000000";

	private final String defMsgColor = "#FFFFFFFF";

	private final String defDialogColor = "#FFE74C3C";

	private Effectstype type = Effectstype.SlideBottom;

	private LinearLayout mLinearLayoutView;

	private RelativeLayout mRelativeLayoutView;

	private LinearLayout mLinearLayoutMsgView;

	private LinearLayout mLinearLayoutTopView;

	private FrameLayout mFrameLayoutCustomView;

	private View mDialogView;

	private View mDivider;

	private TextView mTitle;

	private TextView mMessage;

	private ImageView mIcon;

	private TextView mOKBtn;

	private TextView mCancelBtn;

	private int mDuration = -1;

	private static int mOrientation = 1;

	private boolean isCancelable = false;

	private volatile static WelearnDialogBuilder instance;

	public WelearnDialogBuilder(Context context) {
		super(context);
		init(context);

	}

	public WelearnDialogBuilder(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(params);
	}

//	public static WelearnDialogBuilder getInstance(Context context) {
//
//		int ort = context.getResources().getConfiguration().orientation;
//		if (mOrientation != ort) {
//			mOrientation = ort;
//			instance = null;
//		}
//
//		if (instance == null) {
//			synchronized (WelearnDialogBuilder.class) {
//				if (instance == null) {
//					instance = new WelearnDialogBuilder(context, R.style.dialog_untran);
//				}
//			}
//		}
//		return instance;
//	}
	
	public static WelearnDialogBuilder getDialog(Context context) {
		return new WelearnDialogBuilder(context, R.style.dialog_untran);
	}

	private void init(Context context) {
		mDialogView = View.inflate(context, R.layout.general_dialog_layout, null);
		mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);
		mRelativeLayoutView = (RelativeLayout) mDialogView.findViewById(R.id.main);
		mLinearLayoutTopView = (LinearLayout) mDialogView.findViewById(R.id.topPanel);
		mLinearLayoutMsgView = (LinearLayout) mDialogView.findViewById(R.id.contentPanel);
		mFrameLayoutCustomView = (FrameLayout) mDialogView.findViewById(R.id.customPanel);

		mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
		mMessage = (TextView) mDialogView.findViewById(R.id.message);
		mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
		mDivider = mDialogView.findViewById(R.id.titleDivider);
		mOKBtn = (Button) mDialogView.findViewById(R.id.dialog_ok_btn);
		mCancelBtn = (Button) mDialogView.findViewById(R.id.dialog_cancel_btn);
		mCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		setContentView(mDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {

				mLinearLayoutView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Slidetop;
				}
				start(type);

			}
		});
		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable)
					dismiss();
			}
		});
	}

	public void toDefault() {
		mTitle.setTextColor(Color.parseColor(defTextColor));
		mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
		mMessage.setTextColor(Color.parseColor(defMsgColor));
		mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
	}

	public WelearnDialogBuilder withDividerColor(String colorString) {
		mDivider.setBackgroundColor(Color.parseColor(colorString));
		return this;
	}

	public WelearnDialogBuilder withTitle(CharSequence title) {
		toggleView(mLinearLayoutTopView, title);
		mTitle.setText(title);
		return this;
	}
	
	public WelearnDialogBuilder withTitle(int resId) {
		toggleView(mLinearLayoutTopView, resId);
		mTitle.setText(resId);
		return this;
	}

	public WelearnDialogBuilder withTitleColor(String colorString) {
		mTitle.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public WelearnDialogBuilder withMessage(int textResId) {
		toggleView(mLinearLayoutMsgView, textResId);
		mMessage.setText(textResId);
		return this;
	}

	public WelearnDialogBuilder withMessage(CharSequence msg) {
		toggleView(mLinearLayoutMsgView, msg);
		mMessage.setText(msg);
		return this;
	}

	public WelearnDialogBuilder withMessageColor(String colorString) {
		mMessage.setTextColor(Color.parseColor(colorString));
		return this;
	}

	public WelearnDialogBuilder withIcon(int drawableResId) {
		mIcon.setImageResource(drawableResId);
		return this;
	}

	public WelearnDialogBuilder withIcon(Drawable icon) {
		mIcon.setImageDrawable(icon);
		return this;
	}

	public WelearnDialogBuilder withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	public WelearnDialogBuilder withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	public WelearnDialogBuilder withButtonDrawable(int resid) {
		mOKBtn.setBackgroundResource(resid);
		mCancelBtn.setBackgroundResource(resid);
		return this;
	}

	public WelearnDialogBuilder withButton1Text(CharSequence text) {
		mOKBtn.setVisibility(View.VISIBLE);
		mOKBtn.setText(text);
		return this;
	}
	
	public WelearnDialogBuilder withButton1Text(int resId) {
		mOKBtn.setVisibility(View.VISIBLE);
		mOKBtn.setText(resId);
		return this;
	}

	public WelearnDialogBuilder withButton2Text(CharSequence text) {
		mCancelBtn.setVisibility(View.VISIBLE);
		mCancelBtn.setText(text);
		return this;
	}
	
	public WelearnDialogBuilder withButton2Text(int resId) {
		mCancelBtn.setVisibility(View.VISIBLE);
		mCancelBtn.setText(resId);
		return this;
	}

	public WelearnDialogBuilder setOkButtonClick(View.OnClickListener click) {
		mOKBtn.setOnClickListener(click);
		return this;
	}

	public WelearnDialogBuilder setCancelButtonClick(View.OnClickListener click) {
		mCancelBtn.setOnClickListener(click);
		return this;
	}

	public WelearnDialogBuilder setCustomView(int resId, Context context) {
		View customView = View.inflate(context, resId, null);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(customView);
		return this;
	}

	public WelearnDialogBuilder setCustomView(View view, Context context) {
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	public WelearnDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCanceledOnTouchOutside(cancelable);
		return this;
	}

	public WelearnDialogBuilder isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		this.setCancelable(cancelable);
		return this;
	}

	private void toggleView(View view, Object obj) {
		if (obj == null) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void show() {

		if (mTitle.getText().equals(""))
			mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);

		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		// mButton1.setVisibility(View.GONE);
		// mButton2.setVisibility(View.GONE);
	}
}
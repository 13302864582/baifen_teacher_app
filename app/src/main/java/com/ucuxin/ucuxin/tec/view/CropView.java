package com.ucuxin.ucuxin.tec.view;

import com.ucuxin.ucuxin.tec.view.imageview.FitImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CropView extends FitImageView {
	private Paint mRectPaint;
	private Paint mResetPaint;

	private int mStartX = 0;
	private int mStartY = 0;
	private int mEndX = 0;
	private int mEndY = 0;
	private boolean mDrawRect = false;
	
	public void setmDrawRect(boolean mDrawRect) {
		this.mDrawRect = mDrawRect;
	}

	private TextPaint mTextPaint = null;

	private OnUpCallback mCallback = null;

	public interface OnUpCallback {
		void onRectFinished(Rect rect);
	}

	public CropView(final Context context) {
		super(context);
		init();
	}

	public CropView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CropView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * Sets callback for up
	 * 
	 * @param callback
	 *            {@link OnUpCallback}
	 */
	public void setOnUpCallback(OnUpCallback callback) {
		mCallback = callback;
	}

	/**
	 * Inits internal data
	 */
	private void init() {
		mRectPaint = new Paint();
		mRectPaint.setColor(Color.GREEN);
		mRectPaint.setStyle(Paint.Style.STROKE);
		mRectPaint.setStrokeWidth(5);

		mResetPaint = new Paint();
		mResetPaint.setColor(Color.TRANSPARENT);
		mResetPaint.setStyle(Paint.Style.STROKE);
		mResetPaint.setStrokeWidth(5);
		
		mTextPaint = new TextPaint();
		mTextPaint.setColor(Color.MAGENTA);
		mTextPaint.setTextSize(20);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDrawRect = false;
			mStartX = (int) event.getX();
			mStartY = (int) event.getY();
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			final int x = (int) event.getX();
			final int y = (int) event.getY();
			if (!mDrawRect || Math.abs(x - mEndX) > 5
					|| Math.abs(y - mEndY) > 5) {
				mEndX = x;
				mEndY = y;
				invalidate();
			}
			mDrawRect = true;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mCallback != null) {
				mCallback.onRectFinished(new Rect(Math.min(mStartX, mEndX),
						Math.min(mStartY, mEndY), Math.max(mEndX, mStartX),
						Math.max(mEndY, mStartY)));
			}
			invalidate();
			break;
		default:
			mDrawRect = false;
			invalidate();
			break;
		}
		return true;
	}

	public void resetRect(Canvas canvas) {
		canvas.drawRect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
				Math.max(mEndX, mStartX), Math.max(mEndY, mStartY),
				mResetPaint);
	}
	
	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);
		if (mDrawRect) {
			canvas.drawRect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
					Math.max(mEndX, mStartX), Math.max(mEndY, mStartY),
					mRectPaint);
		} else {
			resetRect(canvas);
		}
	}
}
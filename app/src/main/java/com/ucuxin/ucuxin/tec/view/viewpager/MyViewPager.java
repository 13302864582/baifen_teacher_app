package com.ucuxin.ucuxin.tec.view.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

	public boolean isMoveInPager;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isMoveInPager) {
			return false;
		}
		try {
			return super.onInterceptTouchEvent(arg0);
		} catch (Exception e) {

		}
		return !isMoveInPager;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		try {
			return super.onTouchEvent(arg0);
		} catch (Exception e) {

		}
		return false;
	}

}

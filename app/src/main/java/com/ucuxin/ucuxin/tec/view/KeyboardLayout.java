
package com.ucuxin.ucuxin.tec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ucuxin.ucuxin.tec.utils.LogUtils;

public class KeyboardLayout extends LinearLayout {
    private static final String TAG = KeyboardLayout.class.getSimpleName();

    public static final byte KEYBOARD_STATE_SHOW = -3;

    public static final byte KEYBOARD_STATE_HIDE = -2;

    public static final byte KEYBOARD_STATE_INIT = -1;

    private boolean mHasInit;

    private boolean mHasKeybord;

    private int mHeight;

    private onKybdsChangeListener mListener;

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLayout(Context context) {
        super(context);
    }

    /**
     * set keyboard state listener
     */
    public void setOnkbdStateListener(onKybdsChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            }
        } else {
            mHeight = mHeight < b ? b : mHeight;
        }
        if (mHasInit && mHeight > b) {
            mHasKeybord = true;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
            }
            LogUtils.w(TAG, "show keyboard.......");
            // 软键盘弹出
        }
        if (mHasInit && mHasKeybord && mHeight == b) {
            mHasKeybord = false;
            if (mListener != null) {
                mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
            }
            LogUtils.w(TAG, "hide keyboard.......");
            // 软键盘隐藏
        }
    }

    public interface onKybdsChangeListener {
        void onKeyBoardStateChange(int state);
    }
}

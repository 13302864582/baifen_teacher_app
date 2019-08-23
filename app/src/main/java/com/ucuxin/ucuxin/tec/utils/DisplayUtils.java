
package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;

import com.ucuxin.ucuxin.tec.TecApplication;

/**
 * 获取屏幕的参数及屏幕分辨率的转换
 * 
 * @author: sky
 */
public class DisplayUtils {

    /**
     * 取得屏幕的宽度 px
     * 
     * @return
     */
    public static int getScreenWidth() {
        int width = TecApplication.getContext().getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 取得屏幕的高度
     * 
     * @return
     */
    public static int getScreenHeight() {
        int height = TecApplication.getContext().getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static float dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5f);
    }

    public static float pxToDpCeilInt(Context context, float px) {
        return (int)(pxToDp(context, px) + 0.5f);
    }

    // /**
    // * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
    // */
    // public static int dip2px(Context context, float dpValue) {
    // final float scale = context.getResources().getDisplayMetrics().density;
    // return (int) (dpValue * scale + 0.5f);
    // }
    //
    // /**
    // * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    // */
    // public static int px2dip(Context context, float pxValue) {
    // final float scale = context.getResources().getDisplayMetrics().density;
    // return (int) (pxValue / scale + 0.5f);
    // }

}

package com.ucuxin.ucuxin.tec.utils;

/**
 * 控件工具类
 * Created by Sky
 * on 2016/5/23 0023.
 */

public class ControlUtils {


    private static long lastClickTime;

    /**
     * 防止按钮重复点击
     *
     * @author: sky
     * @return boolean
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}

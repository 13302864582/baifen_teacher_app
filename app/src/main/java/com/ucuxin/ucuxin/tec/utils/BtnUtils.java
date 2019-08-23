
package com.ucuxin.ucuxin.tec.utils;

import com.ucuxin.ucuxin.tec.api.HomeApI;
import com.ucuxin.ucuxin.tec.base.BaseActivity;


public class BtnUtils {

    private static long lastClickTime;

    /**
     * 防止按钮重复点击
     * 
     * @author: sky
     * @return boolean
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static HomeApI homeApI;
    /**
     * 点击事件记录
     */
    public static void clickevent(String event_code, BaseActivity listener) {
    	if(homeApI==null){
    		homeApI=new HomeApI();
    	}
    	homeApI.clickevent(event_code, listener);
    } 

}

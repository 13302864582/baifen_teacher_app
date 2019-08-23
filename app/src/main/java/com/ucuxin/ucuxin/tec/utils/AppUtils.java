package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;

/**
 * Created by Sky on 2016/6/3 0003.
 */

public class AppUtils {

    private AppUtils() {
    }

    public static AppUtils getInstance(Context context) {
        return AppUtilsHolder.INSANCE;
    }

    private static class AppUtilsHolder {

        private static final AppUtils INSANCE = new AppUtils();
    }


}

package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import androidx.core.content.FileProvider;

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

    /**
     * 将文件转换成uri(支持7.0)
     *
     * @param mContext
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context mContext, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(mContext, "com.lantel.baifen.fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
}

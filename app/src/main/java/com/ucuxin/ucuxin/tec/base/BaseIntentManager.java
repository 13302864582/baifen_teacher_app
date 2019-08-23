
package com.ucuxin.ucuxin.tec.base;

import com.ucuxin.ucuxin.tec.constant.GlobalContant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Intent 基类
 * @author: sky
 */
public abstract class BaseIntentManager {

    public static void openActivity(Context context, Class<? extends Activity> clazz,
            boolean isFinish) {
        try {
            Intent intent = new Intent(context, clazz);
            context.startActivity(intent);
            if (isFinish) {
                ((Activity)context).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openActivity(Activity activity, Class<? extends Activity> activityClazz,
            Bundle bundle, boolean isFinish) {
        try {
            Intent intent = new Intent(activity, activityClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivity(intent);
            // add by milo 2014.09.11
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFinish) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param activity
     * @param activityClazz
     * @param bundle
     * @param isFinish
     * @param requestCode
     */
    public static void openActivityForResult(Activity activity,
            Class<? extends Activity> activityClazz, Bundle bundle, boolean isFinish,
            int requestCode) {
        try {
            Intent intent = new Intent(activity, activityClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivityForResult(intent, requestCode);
            // add by milo 2014.09.11
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFinish) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 过时,不建议使用
     * 
     * @param activity
     * @param activityClazz
     * @param bundle
     * @param isFinish
     */
    @Deprecated
    public static void openActivityForResult(Activity activity,
            Class<? extends Activity> activityClazz, Bundle bundle, boolean isFinish) {
        try {
            Intent intent = new Intent(activity, activityClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            activity.startActivityForResult(intent, GlobalContant.RESULT_OK);
            // add by milo 2014.09.11
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (isFinish) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

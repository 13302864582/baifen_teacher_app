
package com.ucuxin.ucuxin.tec.utils;

import android.os.Handler;
import android.os.Message;

import com.ucuxin.ucuxin.tec.constant.GlobalContant;

/**
 *AsyncTask异步
 * @author:  sky
 */
public abstract class MyAsyncTask {
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GlobalContant.MYASYNC) {
                postTask();
            }
        }
    };

    public abstract void preTask();

    public abstract void doInBack();

    public abstract void postTask();

    public void excute() {
        preTask();
        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                doInBack();
                handler.sendEmptyMessage(GlobalContant.MYASYNC);

            }
        });

    }
}

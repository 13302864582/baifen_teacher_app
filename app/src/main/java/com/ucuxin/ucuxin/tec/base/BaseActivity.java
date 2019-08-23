
package com.ucuxin.ucuxin.tec.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestQueueWrapper;
import com.ucuxin.ucuxin.tec.manager.SystemStatusManager;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.zxing.decoding.CaptureActivityHandler;
import com.ucuxin.ucuxin.tec.utils.zxing.view.ViewfinderView;
import com.ucuxin.ucuxin.tec.view.dialog.WaitingDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 此类的描述： Activity基类
 *
 * @author: Sky
 */
public abstract class BaseActivity extends AppCompatActivity
        implements IBaseActivity, OnClickListener {


    private static final String TAG = BaseActivity.class.getSimpleName();

    protected ProgressDialog mDialog;

    public boolean isShowDialog = false;

    public long clickTime;

    // sky add
    public TecApplication app;

    public RequestQueue requestQueue;

    protected Dialog mProgressDialog;

    private CaptureActivityHandler handler;

    private ViewfinderView viewfinderView;


    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result result, Bitmap barcode) {
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(CaptureActivityHandler handler) {
        this.handler = handler;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//        setTranslucentStatus();
        setContentView(R.layout.activity_main);
        app = (TecApplication) this.getApplication();
        requestQueue = VolleyRequestQueueWrapper.getInstance(this).getRequestQueue();
        // addActivity(this);
        // mDialog = new ProgressDialog(this);
    }


    private void setTranslucentStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            //StatusBarUtil.setColor(BaseActivity.this, getResources().getColor(R.color.colorPrimary));

            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimary));
            tintManager.setStatusBarTintEnabled(true);



        }
    }

    @Override
    public void initView() {


    }

    @Override
    public void initListener() {


    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        // StatService.onResume(this);
        // MobclickAgent.setSessionContinueMillis(60000);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        // StatService.onPause(this);
    }

    public void uMengEvent(String event) {
        MobclickAgent.onEvent(this, event);
    }

    protected void showNetWorkExceptionToast() {
        ToastUtils.show(R.string.network_connect_fail_msg);
    }

    public void showDialog(String text, boolean cancelable) {
        // if (null == mDialog) {
        // mDialog = new ProgressDialog(this);
        // }
        // // mDialog = ProgressDialog.show(this, "", text);
        // mDialog.setMessage(text);
        // mDialog.setCancelable(cancelable);
        // if (mDialog != null && !mDialog.isShowing()) {
        // mDialog.setOnDismissListener(new OnDismissListener() {
        // @Override
        // public void onDismiss(DialogInterface arg0) {
        // isShowDialog = false;
        // }
        // });
        // if (!isFinishing()) {
        // mDialog.show();
        // }
        // isShowDialog = true;
        // }

        // ////// sky add
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog(this, text, cancelable);
            mProgressDialog.show();
        }

    }

    public void showDialog(String text) {
        // if(null == mDialog){
        // mDialog = new ProgressDialog(this);
        // }
        // // mDialog = ProgressDialog.show(this, "", text);
        // mDialog.setMessage(text);
        // mDialog.setCancelable(true);
        // if (mDialog != null && !mDialog.isShowing()) {
        // mDialog.setOnDismissListener(new OnDismissListener() {
        // @Override
        // public void onDismiss(DialogInterface arg0) {
        // isShowDialog = false;
        // }
        // });
        // if (!isFinishing()) {
        // mDialog.show();
        // }
        // isShowDialog = true;
        // }

        // //////////////////////// sky add
        if (mProgressDialog == null) {
            if (!this.isFinishing()) {
                mProgressDialog = WaitingDialog.createLoadingDialog(this, text);
                mProgressDialog.show();
            }

        }

    }

    protected void closeDialog() {
        // if (mDialog != null && mDialog.isShowing()) {
        // try {
        // mDialog.dismiss();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void closeDialogHelp() {
        closeDialog();
        isShowDialog = false;
    }

    /**
     * 此方法描述的是：显示进度条Dialog
     *
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午8:06:20
     * @version: 2.0 showDialog void
     */
    public void loadingDialog(Context context, String resStr) {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog(context, resStr);
            mProgressDialog.show();
        }
    }

    /**
     * 此方法描述的是：关闭进度条Dialog
     *
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午7:28:45 closeDialog void
     */
    public void dissmissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void hideBackLayout() {
        RelativeLayout backLayout = (RelativeLayout) findViewById(R.id.back_layout);
        if (null != backLayout) {
            backLayout.setVisibility(View.GONE);
        }
    }

    protected void setWelearnTitle(int resid) {
        TextView titleTV = (TextView) findViewById(R.id.title);
        if (null != titleTV) {
            try {
                titleTV.setText(resid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setWelearnTitle(String titleStr) {
        TextView titleTV = (TextView) findViewById(R.id.title);
        if (null != titleTV && null != titleStr) {
            titleTV.setText(titleStr);
        }
    }

    public void showAddPointBottomContainer(String coordinate) {
        // mBottomContainer.setVisibility(View.VISIBLE);
    }

    public void hideAddPointBottomContainer() {
        // mBottomContainer.setVisibility(View.GONE);
    }

    public void hideCamareContainer() {

    }

    public void report(int reasonid,String reason,String tip) {

    }

    public void refresh() {

    }

    @Override
    public void resultBack(Object... param) {
        int flag = ((Integer) param[0]).intValue();
        switch (flag) {
            case RequestConstant.ERROR:
                break;
            case RequestConstant.COOKIE_INVILD:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mProgressDialog != null) {
                mProgressDialog = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDialog();
        // 取消所有的请求
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
            requestQueue = null;
        }
//        VolleyRequestQueueWrapper.nullContext();
        // removeActivity(this);
    }


    public void showCamareContainer(HomeWorkCheckPointModel checkPointModel) {


    }

    // private void addActivity(Activity activity) {
    // if(!app.activityList.contains(activity)){
    // synchronized (app) {
    // app.activityList.add(activity);
    // }
    // }
    //
    // }
    //
    // private void removeActivity(Activity activity) {
    // if(app.activityList.contains(activity)){
    // synchronized (app) {
    // app.activityList.remove(activity);
    // }
    // }
    // }
    public String getResStr(int str) {
       return getResources().getString(str);
    }

    public String getResFormatStr(int format_str,String value) {
        String temp = getResStr(format_str);
        return String.format(temp,value);
    }
}

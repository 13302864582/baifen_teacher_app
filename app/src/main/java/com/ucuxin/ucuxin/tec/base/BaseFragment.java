
package com.ucuxin.ucuxin.tec.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.RequestConstant;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestQueueWrapper;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.WaitingDialog;

import androidx.fragment.app.Fragment;

/**
 * 此类的描述： Fragment基类
 * 
 * @author: Sky
 */

public abstract class BaseFragment extends Fragment implements IBaseFragment {

    protected  Activity mActivity;

    public RequestQueue requestQueue;

    protected Dialog mProgressDialog;

    public boolean isShowDialog = false;

    public long clickTime;

    protected float downX;

    protected float downY;

    protected long downTime;

    protected abstract void goBack();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = VolleyRequestQueueWrapper.getInstance(getActivity()).getRequestQueue();

        setHasOptionsMenu(true);// 开启返回按钮

        // mActionBar = getActivity().getActionBar();
        // mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
        // | ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);//
        // 不调用则自定义actionbar失效
        //
        // // mActionBar.setDisplayShowHomeEnabled(true);//显示icon图标
        // mActionBar.setDisplayHomeAsUpEnabled(false);// 添加返回按钮
        // mActionBar.setHomeButtonEnabled(true);
        // mActionBar.setDisplayShowHomeEnabled(true);

        // mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector);
        // mActionBar.setLogo(R.drawable.bg_actionbar_back_up_selector);
        /*
         * mActionBar.setIcon(R.drawable.bg_actionbar_back_up_selector); int
         * upid = Resources.getSystem().getIdentifier("up", "id", "android");
         * ImageView img = (ImageView) mActivity.findViewById(upid);
         * img.setImageResource(R.drawable.bg_actionbar_back_up_selector);
         */
        // mActionBar.setHomeAsUpIndicator(R.drawable.ic_actiobar_homeasup);
        // mActionBar.set

    }

    @Override
    public void onPause() {
        super.onPause();
        // MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        // MobclickAgent.onPageStart(this.getClass().getSimpleName());
        // ToastUtils.show(mActivity, this.getClass().getSimpleName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
    }
    
   
    public abstract  void initView(View view); 
    

    @Override
    public abstract void initListener();

   
    
    /**
     * 此方法描述的是：显示进度条Dialog
     * 
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午8:06:20
     * @version: 2.0 showDialog void
     */
    protected void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog(getActivity(), "正在加载中...");
            mProgressDialog.show();
        }
    }

    public void showDialog(String text) {
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog(getActivity(), text);
            mProgressDialog.show();
        }

    }

    public void showDialog(String text, boolean cancelable) {
        //////// sky add
        if (mProgressDialog == null) {
            mProgressDialog = WaitingDialog.createLoadingDialog(getActivity(), text, cancelable);
            mProgressDialog.show();
        }

    }

    public void closeDialogHelp() {
        closeDialog();
        isShowDialog = false;
    }

    /**
     * 此方法描述的是：关闭进度条Dialog
     * 
     * @author: Sky @最后修改人： Sky
     * @最后修改日期:2015-7-20 下午7:28:45 closeDialog void
     */
    protected void closeDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void showNetWorkExceptionToast() {
        if (isAdded()) {
            ToastUtils.show(getString(R.string.network_connect_fail_msg));
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void resultBack(Object... param) {
        int flag = ((Integer)param[0]).intValue();
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
    public void onDestroy() {
        super.onDestroy();
        // 取消所有的请求
        requestQueue.cancelAll(this);
        if (requestQueue != null) {
            requestQueue = null;
        }
    }

}

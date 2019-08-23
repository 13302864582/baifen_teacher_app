
package com.ucuxin.ucuxin.tec.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

/**
 * 检查更新dialog
 *
 * @author: sky
 */
public class CustomAppCheckUpdateDialog extends Dialog {

    private Activity context;
    TextView tv_title =null;
    TextView tv_content = null;
    public LinearLayout layout_not_force_update = null;
    public Button btn_ok = null;
    public Button btn_cancle = null;
    public LinearLayout layout_force_update = null;
    public Button btn_go_update = null;

    private ClickListenerInterface clickListenerInterface;


    public CustomAppCheckUpdateDialog(Activity context, boolean isForce) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context, isForce);
        setAttributes();
    }

    private void setCustomDialog(Context ctx, boolean isForce) {
        View mCheckUpdateView = View.inflate(ctx, R.layout.app_check_update, null);
         tv_title = (TextView) mCheckUpdateView.findViewById(R.id.tv_title);
         tv_content = (TextView) mCheckUpdateView.findViewById(R.id.tv_content);
        layout_not_force_update = (LinearLayout) mCheckUpdateView.findViewById(R.id.layout_not_force_update);
        btn_ok = (Button) mCheckUpdateView.findViewById(R.id.btn_ok);
        btn_cancle = (Button) mCheckUpdateView.findViewById(R.id.btn_cancle);
        layout_force_update = (LinearLayout) mCheckUpdateView.findViewById(R.id.layout_force_update);
        btn_go_update = (Button) mCheckUpdateView.findViewById(R.id.btn_go_update);

        tv_title.setText(SharePerfenceUtil.getInstance().getUpdateTitle());
        tv_content.setText(SharePerfenceUtil.getInstance().getUpdateContent());
        this.setCancelable(false);
        btn_ok.setOnClickListener(new clickListener());
        btn_cancle.setOnClickListener(new clickListener());
        btn_go_update.setOnClickListener(new clickListener());


        judgeForceUpdate(isForce);
        super.setContentView(mCheckUpdateView);
    }

    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }


    public void judgeForceUpdate(boolean isForce) {
        if (!isForce) {
            ////不强制升级
            layout_not_force_update.setVisibility(View.VISIBLE);
            layout_force_update.setVisibility(View.GONE);
            btn_ok.setText("升级");
            btn_cancle.setText("以后再说");
        } else {
            //强制升级
            layout_not_force_update.setVisibility(View.GONE);
            layout_force_update.setVisibility(View.VISIBLE);
        }

    }

   /* *//**
     * 确定键监听器
     *
     * @param listener
     *//*
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);

    }

    */

    /**
     * 取消键监听器
     *
     * @param
     *//*
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }*/


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_ok:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.btn_cancel:
                    clickListenerInterface.doCancel();
                    break;
                case R.id.btn_go_update:
                    clickListenerInterface.doGoUpdate();
                    break;
            }
        }

    }

    ;


    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();

        public void doGoUpdate();
    }


}

package com.ucuxin.ucuxin.tec.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;

/**
 * 此类的描述：自定义Dialog 
 * @author:  Sky
 * @最后修改人： Sky 
 * @最后修改日期:2015-7-20 下午7:42:17
 */
public class WaitingDialog  {
    
    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.waiting_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialog_view);
        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.img);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tipTextView);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.waiting_animation);
        // 显示动画
        img.startAnimation(animation);
        // 显示文本
        tipText.setText(msg);

        // 创建自定义样式的Dialog
        Dialog waitingDialog = new Dialog(context, R.style.waiting_dialog_style);
        // 设置返回键无效
        waitingDialog.setCancelable(false);
        waitingDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        waitingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return waitingDialog;
    }
    
    
    
    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg,boolean isCancle) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.waiting_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialog_view);
        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.img);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tipTextView);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.waiting_animation);
        // 显示动画
        img.startAnimation(animation);
        // 显示文本
        tipText.setText(msg);

        // 创建自定义样式的Dialog
        Dialog waitingDialog = new Dialog(context, R.style.waiting_dialog_style);
        // 设置返回键无效
        waitingDialog.setCancelable(isCancle);
        waitingDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        waitingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return waitingDialog;
    }
}

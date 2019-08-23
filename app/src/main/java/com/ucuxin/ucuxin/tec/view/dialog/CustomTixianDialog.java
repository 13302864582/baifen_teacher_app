
package com.ucuxin.ucuxin.tec.view.dialog;

import com.ucuxin.ucuxin.tec.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *体现 提示dialog
 * @author: sky
 */
public class CustomTixianDialog extends Dialog {
    
    private TextView tv_title;

    private ImageView iv_content;

    private Button  btn_ok;

    private Activity context;
    

   

    public CustomTixianDialog(Activity context,String title) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context,title);
        setAttributes();
    }

    private void setCustomDialog(Context ctx,String title) {
        View mView = View.inflate(ctx, R.layout.tixian_tip_dialog, null);
        tv_title = (TextView)mView.findViewById(R.id.tv_title);
        iv_content = (ImageView)mView.findViewById(R.id.iv_content);
        btn_ok = (Button)mView.findViewById(R.id.btn_ok);       
        tv_title.setText(Html.fromHtml(title));             
        super.setContentView(mView);
    }
 
    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int)(d.getHeight() * 0.35); // 高度设置为屏幕的0.6
        p.width = (int)(d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
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

    

    /**
     * 取消键监听器
     * 
     * @param listener
     */
    public void setButtonListener(View.OnClickListener listener) {
    	btn_ok.setOnClickListener(listener);
    }
    
    
	
	
}

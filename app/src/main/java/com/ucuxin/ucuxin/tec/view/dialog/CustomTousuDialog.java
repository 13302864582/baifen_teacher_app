
package com.ucuxin.ucuxin.tec.view.dialog;

import java.util.ArrayList;
import java.util.List;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.homework.model.TousuModel;
import com.ucuxin.ucuxin.tec.view.gridview.MyGridView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 投诉dialog
 * @author: sky
 */
public class CustomTousuDialog extends Dialog {
	
	private TextView tv_close;
    
    private TextView tv_title;
    
    private MyGridView gridview;

    private TextView tv_addtion;

    private Button btn_left, btn_right;

    private Activity context;
    
    private TousuAdapter mAdapter;
    
    private String contentType2="";
    
    private List<TousuModel> tousuList=new ArrayList<TousuModel>();

    public Button getLeftButton() {
        return btn_left;
    }

    public Button getRightButton() {
        return btn_right;
    }
  


   

    public CustomTousuDialog(Activity context,String title,List<TousuModel> temp,String leftBtnStr,String rightBtnStr) {
        super(context, R.style.MyDialogStyleBottom);
        this.context = context;
        setCustomDialog(context,title,temp,leftBtnStr,rightBtnStr);
        setAttributes();
    }

    private void setCustomDialog(Context ctx,String title,List<TousuModel> temp,String leftBtnStr,String rightBtnStr) {
        View mView = View.inflate(ctx, R.layout.custom_tousu_dialog_layout, null);
        tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_close = (TextView)mView.findViewById(R.id.tv_close);
        this.gridview = (MyGridView) mView.findViewById(R.id.gridview);
        tv_addtion = (TextView)mView.findViewById(R.id.tv_addtion);
        btn_left = (Button)mView.findViewById(R.id.btn_left);
        btn_right = (Button)mView.findViewById(R.id.btn_right);
        tv_title.setText(title);
        for (int i = 0; i < temp.size(); i++) {
        	TousuModel model=temp.get(i);
			if (model.getType()==2) {
				contentType2=model.getContent();
			}else {
				tousuList.add(model);
			}
		}
        
        if (!TextUtils.isEmpty(contentType2)) {
        	tv_addtion.setVisibility(View.VISIBLE);
        	tv_addtion.setText(contentType2);
		}else {
			tv_addtion.setVisibility(View.GONE);
		}
        mAdapter = new TousuAdapter(ctx, tousuList);
        gridview.setAdapter(mAdapter);
        btn_left.setText(rightBtnStr);
        btn_left.setText(leftBtnStr);
        
        super.setContentView(mView);
    }
    
   

    public void setAttributes() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int)(d.getHeight() * 0.65); // 高度设置为屏幕的0.6
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
     * 确定键监听器
     * 
     * @param listener
     */
    public void setonLeftButtonListener(View.OnClickListener listener) {
    	btn_left.setOnClickListener(listener);

    }

    /**
     * 取消键监听器
     * 
     * @param listener
     */
    public void setonRightButtonListener(View.OnClickListener listener) {
        btn_right.setOnClickListener(listener);
    }
    
    
    public void setonCloseDialog(View.OnClickListener listener) {
        tv_close.setOnClickListener(listener);
    }
    
    
    
    public class TousuAdapter extends BaseAdapter {

    	private Context context;
    	private List<TousuModel> list;
    	private MyGridView gridview;   	
    	

    	public TousuAdapter(Context context, List<TousuModel> list) {
    		super();
    		this.context = context;
    		this.gridview = gridview;
    		this.list = list;
    	}

    	@Override
    	public int getCount() {
    		return list.size();
    	}

    	@Override
    	public Object getItem(int position) {
    		return list.get(position);
    	}

    	@Override
    	public long getItemId(int position) {
    		return position;
    	}
    	


    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		ViewHolder viewholder=null;
    		if (convertView==null) {
    			viewholder=new ViewHolder();
				convertView=View.inflate(context, R.layout.liyou_item, null);
				viewholder.tv_liyou=(TextView) convertView.findViewById(R.id.tv_liyou);
				convertView.setTag(viewholder);
			}else {
				viewholder=(ViewHolder) convertView.getTag();
			}    			
    		TousuModel model = (TousuModel) getItem(position);
    		viewholder.tv_liyou.setText(model.getContent());
    		return convertView;
    	}

    }
    
    class ViewHolder{
    	TextView tv_liyou;
    }
}

package com.ucuxin.ucuxin.tec.function;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class CameraFrameWithDel extends FrameLayout {

	public ImageView getBgView() {
		return bgView;
	}

	public void setBgView(ImageView bgView) {
		this.bgView = bgView;
	}


	private ImageView bgView;


    public CameraFrameWithDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public CameraFrameWithDel(Context context) {
		super(context);
		setupViews();
	}

	private void setupViews() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.camera_frame_with_del, null);
		bgView = (ImageView) view.findViewById(R.id.bg_camera_frame);
		if (SharePerfenceUtil.getInstance().getUserRoleId() == GlobalContant.ROLE_ID_STUDENT) {
			bgView.setImageResource(R.drawable.bg_camera_frame_red);
		}
		addView(view);
	}
	
	
	public void setErrorImage(int resourceId){
	    if (bgView!=null) {	   
	        bgView.setBackgroundResource(0);	        
	        bgView.setImageResource(resourceId); 
        }
	    
	}

   
	
}

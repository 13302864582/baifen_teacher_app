package com.ucuxin.ucuxin.tec.http.volley;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * 设置高优先级Request
 * @author Administrator
 *
 */
public class CustomRequest  extends  StringRequest{

	Priority  mPriority;
	
	public CustomRequest(int method, String url, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);

	}
	
	

	public void setPriority(Priority mPriority) {
		this.mPriority = mPriority;
	}



	@Override
	public com.android.volley.Request.Priority getPriority() {
		//return super.getPriority();		
		return mPriority!=null?mPriority:Priority.NORMAL;
	}
	
	
	

}

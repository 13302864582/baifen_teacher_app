
package com.ucuxin.ucuxin.tec.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.WxConstant;
import com.ucuxin.ucuxin.tec.utils.BtnUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private int errCode;

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_weixinpayok);

		api = WXAPIFactory.createWXAPI(this, WxConstant.APP_ID_WW);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		 errCode = resp.errCode; 
	    	
         if (errCode == 0) {
        	 if (SharePerfenceUtil.getInstance().getInt("sharekey", 0) == 1) {
					
        		 BtnUtils.clickevent("share_wechat",WXEntryActivity.this);
				} else if (SharePerfenceUtil.getInstance().getInt("sharekey", 0) == 4) {
					BtnUtils.clickevent("share_timeline",WXEntryActivity.this);
				}
            
         }
         finish();

	}
}

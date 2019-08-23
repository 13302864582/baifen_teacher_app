package com.ucuxin.ucuxin.tec.function.question;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;*/
import android.view.WindowManager;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PayAnswerAppendAskActivity extends BaseActivity {

	private PayAnswerAppendAskFragment mFragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		Fragment f = fm.findFragmentById(R.id.frameContainer);
		if (f == null) {
			f = createFragment();
			fm.beginTransaction().add(R.id.frameContainer, f).commit();
		}
	}
	
	private PayAnswerAppendAskFragment createFragment() {
		if (null == mFragment) {
			mFragment = new PayAnswerAppendAskFragment();
		}
		return mFragment;
	}
	
	@Override
	public void onBackPressed() {
		if (null != mFragment) {
			mFragment.goToPrevious(true);
		}
	}
}

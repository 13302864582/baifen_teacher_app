package com.ucuxin.ucuxin.tec.function.question;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;*/
import android.view.WindowManager;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.homework.CropImageActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.AppUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;

import java.io.File;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PayAnswerPhotoViewActivity extends BaseActivity {

	public PayAnswerPhotoViewFragment mFragment;

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

	protected Fragment createFragment() {
		mFragment = new PayAnswerPhotoViewFragment();
		return mFragment;
	}

	@Override
	public void onBackPressed() {
		if (null != mFragment) {
			mFragment.goToPrevious(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP) {
			if (resultCode == RESULT_OK && null != mFragment) {
//				mFragment.recycleBitmap();
//				mFragment.showPhoto();
				Bundle bData = new Bundle();
				bData.putBoolean("isFromPhotoList", false);
				bData.putString(PayAnswerImageGridActivity.IMAGE_PATH, mFragment.path);
				IntentManager.goToPhotoView(this, bData );
			}
		}else if(requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE){


			String path = MyFileUtil.getAnswerFile().getAbsolutePath() + File.separator + "publish.png";

			Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, AppUtils.getUriForFile(this,new File(path)));
			sendBroadcast(localIntent);

			localIntent.setClass(this, CropImageActivity.class);
			localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
			localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, path);
			localIntent.putExtra("isFromPhotoList", false);
			startActivityForResult(localIntent, GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP);
		}
	}
}

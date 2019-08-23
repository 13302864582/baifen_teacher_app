package com.ucuxin.ucuxin.tec.function.question;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;

public class PayAnswerPhotoCropperViewActivity extends BaseActivity implements OnClickListener {
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ON_TOUCH = 1;

	private TextView mBackBtn;
	private TextView mSubmitBtn;
	private Button mLeftBtn;
	private Button mRightBtn;

	private String path;
	private CropImageView mPhotoImage;

	public static final String QUESTION_IMG_PATH = "quesiton_img_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		setContentView(R.layout.fragment_question_photo_view);
		findViewById(R.id.header).setVisibility(View.GONE);
		findViewById(R.id.tixing_crop_iv_qphoto).setVisibility(View.GONE);
		mBackBtn = (TextView) findViewById(R.id.question_photo_view_nav_btn_back);
		mSubmitBtn = (TextView) findViewById(R.id.question_photo_view_nav_submit);
		mLeftBtn = (Button) findViewById(R.id.question_photo_view_turn_left_btn);
		mRightBtn = (Button) findViewById(R.id.question_photo_view_turn_right_btn);
		mPhotoImage = (CropImageView) findViewById(R.id.question_img);

		mPhotoImage.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

		mPhotoImage.setGuidelines(ON_TOUCH);

		mBackBtn.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

		showPhoto();
	}

	private Bitmap mBitmap;
	private boolean isFromPhotoList;

	private void showPhoto() {
		Intent intent = getIntent();
		if (intent != null) {
			path = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
			isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
			if (!TextUtils.isEmpty(path)) {
				autoFixOrientation(isFromPhotoList);// , outWidth
													// ,outHeight,widthPixels,heightPixels
				mPhotoImage.setImageBitmap(mBitmap);
			}
		}

	}

	private void autoFixOrientation(boolean isFromPhotoList) {// , int outWidth,
		int deg = 0;

		Matrix m = new Matrix();
		try {
			ExifInterface exif = new ExifInterface(path);
			exif = new ExifInterface(path);

			int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (rotateValue) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				deg = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				deg = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				deg = 270;
				break;
			default:
				deg = 0;
				break;
			}
			m.preRotate(deg);
		} catch (Exception ee) {
			LogUtils.d("catch img error", "return");

		}

		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int outWidth = opts.outWidth;
		int outHeight = opts.outHeight;
		// int ssize = 1;
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		int ssize = 1;
		if (deg == 90 || deg == 270) {
			int temp = outWidth;
			outWidth = outHeight;
			outHeight = temp;
		}

		ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels : outWidth / widthPixels;
		if (ssize <= 1) {
			ssize = 1;

			float scaleWidth = 1, scaleHeight = 1;

			scaleWidth = widthPixels / outWidth;
			scaleHeight = heightPixels / outHeight;
			if (scaleWidth < scaleHeight) {
				scaleHeight = scaleWidth;
			} else {
				scaleWidth = scaleHeight;
			}
			m.postScale(scaleWidth, scaleHeight);
		}
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = ssize;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		mBitmap = BitmapFactory.decodeFile(path, opts);
		mBitmap = WeLearnImageUtil.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
		if (isFromPhotoList) {
			String absolutePath = getCacheDir().getAbsolutePath();
			path = absolutePath + "publish.png";
		}

		WeLearnImageUtil.saveFile(path, mBitmap);
	}

	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		int degree = 0;
		if (rotate == R.id.question_photo_view_turn_right_btn) {
			degree = 90;
		} else {
			degree = -90;
		}
		mPhotoImage.rotateImage(degree, path);

	}

	public void goPrevious() {
		MyFileUtil.deleteFile(path);
//		if (mPhotoImage != null) {
//			ImageLoader.recycleBitmap(mPhotoImage);
//		}
		IntentManager.goToGrabItemView(this, true);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		goPrevious();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.question_photo_view_turn_left_btn:
			rotate(R.id.question_photo_view_turn_left_btn);
			break;
		case R.id.question_photo_view_turn_right_btn:
			rotate(R.id.question_photo_view_turn_right_btn);
			break;
		case R.id.question_photo_view_nav_btn_back://重拍
			if (isFromPhotoList) {
				MyFileUtil.deleteFile(path);
//				if (mPhotoImage != null) {
//					ImageLoader.recycleBitmap(mPhotoImage);
//				}
				Bundle data = new Bundle();
				data.putInt("tag", GlobalContant.PAY_ASNWER);
				IntentManager.goToAlbumView(this, data);
			}else {
				IntentManager.startImageCapture(this, GlobalContant.PAY_ASNWER);
			}
			break;
		case R.id.question_photo_view_nav_submit:
			Bitmap currentBm = mPhotoImage.getCroppedImage();

			if (currentBm != null) {
				WeLearnImageUtil.saveFile(path, currentBm);
			}
			Bundle data = new Bundle();
			data.putString(PayAnswerImageGridActivity.IMAGE_PATH, path);
			IntentManager.goToPayAnswerPhotoView(this, data);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String path = MyFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator + "publish.png";
				if (!TextUtils.isEmpty(path)) {
					autoFixOrientation(false);// , outWidth
					mPhotoImage.setImageBitmap(mBitmap);
				}
			}
		} 
	}
	
}

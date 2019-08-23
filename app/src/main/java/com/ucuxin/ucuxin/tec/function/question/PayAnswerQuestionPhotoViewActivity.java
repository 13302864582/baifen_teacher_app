package com.ucuxin.ucuxin.tec.function.question;
//package com.ucuxin.ucuxin.gasstation.rewardfaq;
//
//import java.io.File;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.BitmapFactory.Options;
//import android.graphics.Matrix;
//import android.media.ExifInterface;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.edmodo.cropper.CropImageView;
//import com.ucuxin.base.ImageLoader;
//import com.ucuxin.base.view.SingleFragmentActivity;
//import com.ucuxin.constant.GlobalContant;
//import com.ucuxin.manager.IntentManager;
//import com.ucuxin.model.FitBitmap;
//import com.ucuxin.util.LogUtils;
//import com.ucuxin.util.ToastUtils;
//import com.ucuxin.util.WeLearnFileUtil;
//import com.ucuxin.util.WeLearnImageUtil;
//import com.ucuxin.ucuxin.tec.R;
//
//public class PayAnswerQuestionPhotoViewActivity extends SingleFragmentActivity implements OnClickListener {
//	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
//	private static final int ON_TOUCH = 1;
//
//	private TextView nextStepTV;
//	private RelativeLayout nextStepLayout;
//	
//	private TextView mBackBtn;
//	private TextView mSubmitBtn;
//	private Button mLeftBtn;
//	private Button mRightBtn;
//
//	private String path;
//	private FitBitmap mPhoto;
//	private CropImageView mPhotoImage;
//	private int left;
//	private int top;
//	private int width;
//	private int height;
//	private ImageView tixing;
//	// private View picContainer;
//
//	private static final String TAG = PayAnswerQuestionPhotoViewActivity.class.getSimpleName();
//	public static final String QUESTION_IMG_PATH = "quesiton_img_path";
//
//	// private Bitmap mBitmap;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		// mActionBar.hide();
//
//		setContentView(R.layout.fragment_question_photo_view);
//
//		setWelearnTitle(R.string.text_image_processing);
//		
//		findViewById(R.id.back_layout).setOnClickListener(this);
//		
//		// picContainer = (View) view.findViewById(R.id.anser_pic_container);
//		// picContainer.setVisibility(View.INVISIBLE);
//
//		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
//		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
//		nextStepTV.setVisibility(View.VISIBLE);
//		nextStepTV.setText(R.string.text_ok);
//		nextStepLayout.setOnClickListener(this);
//		
//		tixing = (ImageView) findViewById(R.id.tixing_crop_iv_qphoto);
//
//		mBackBtn = (TextView) findViewById(R.id.question_photo_view_nav_btn_back);
//		mSubmitBtn = (TextView) findViewById(R.id.question_photo_view_nav_submit);
//		mLeftBtn = (Button) findViewById(R.id.question_photo_view_turn_left_btn);
//		mRightBtn = (Button) findViewById(R.id.question_photo_view_turn_right_btn);
//		mPhotoImage = (CropImageView) findViewById(R.id.question_img);
//
//		mPhotoImage.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
//
//		mPhotoImage.setGuidelines(ON_TOUCH);
//
//		tixing.setOnClickListener(this);
//		mBackBtn.setOnClickListener(this);
//		mSubmitBtn.setOnClickListener(this);
//		mLeftBtn.setOnClickListener(this);
//		mRightBtn.setOnClickListener(this);
//
//		showPhoto();
//
//		// mPhotoImage.setDrawingCacheEnabled(true);
//		/*
//		 * mPhotoImage.setOnUpCallback(new OnUpCallback() {
//		 * 
//		 * @Override public void onRectFinished(Rect rect) { left = rect.left;
//		 * top = rect.top; width = rect.width(); height = rect.height(); } });
//		 */
//	}
//
//	/**
//	 * @param resId
//	 * @return 如果图片太小，那么就拉伸
//	 */
//	public Bitmap getBitmap(String path) {
//		WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
//		int width = wm.getDefaultDisplay().getWidth();
//
//		Bitmap bitmap = BitmapFactory.decodeFile(path);
//		float scaleWidth = 1, scaleHeight = 1;
//		if (bitmap.getWidth() < width) {
//			scaleWidth = width / bitmap.getWidth();
//			scaleHeight = scaleWidth;
//		}
//		Matrix matrix = new Matrix();
//		matrix.postScale(scaleWidth, scaleHeight);
//		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		return bitmap;
//	}
//
//	private Bitmap mBitmap;
//	private boolean isFromPhotoList;
//
//	private void showPhoto() {
//		Intent intent = getIntent();
//		if (intent != null) {
//			path = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
//			isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
//			if (!TextUtils.isEmpty(path)) {
//				autoFixOrientation(isFromPhotoList);// , outWidth
//				mPhotoImage.setImageBitmap(mBitmap);
//			}
//		}
//
//	}
//
//	private void autoFixOrientation(boolean isFromPhotoList) {// , int outWidth,
//		int deg = 0;
//
//		Matrix m = new Matrix();
//		try {
//			ExifInterface exif = new ExifInterface(path);
//			exif = new ExifInterface(path);
//
//			int rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//			switch (rotateValue) {
//			case ExifInterface.ORIENTATION_ROTATE_90:
//				deg = 90;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_180:
//				deg = 180;
//				break;
//			case ExifInterface.ORIENTATION_ROTATE_270:
//				deg = 270;
//				break;
//			default:
//				deg = 0;
//				break;
//			}
//			m.preRotate(deg);
//		} catch (Exception ee) {
//			LogUtils.d("catch img error", "return");
//
//		}
//
//		try {
//			Options opts = new Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, opts);
//			int outWidth = opts.outWidth;
//			int outHeight = opts.outHeight;
//			// int ssize = 1;
//			DisplayMetrics metrics = new DisplayMetrics();
//			getWindowManager().getDefaultDisplay().getMetrics(metrics);
//			int widthPixels = metrics.widthPixels;
//			int heightPixels = metrics.heightPixels;
//			int ssize = 1;
//			if (deg == 90 || deg == 270) {
//				int temp = outWidth;
//				outWidth = outHeight;
//				outHeight = temp;
//				// ssize = outHeight/ widthPixels > outWidth/heightPixels ?
//				// outHeight/ widthPixels:outWidth/heightPixels;
//			}
//
//			ssize = outHeight / heightPixels > outWidth / widthPixels ? outHeight / heightPixels : outWidth
//					/ widthPixels;
//			if (ssize <= 0) {
//				ssize = 1;
//
//				float scaleWidth = 1, scaleHeight = 1;
//
//				scaleWidth = widthPixels / outWidth;
//				scaleHeight = heightPixels / outHeight;
//				if (scaleWidth < scaleHeight) {
//					scaleHeight = scaleWidth;
//				} else {
//					scaleWidth = scaleHeight;
//				}
//				m.postScale(scaleWidth, scaleHeight);
//
//			}
//			// Options opts = new Options();
//			opts.inJustDecodeBounds = false;
//			opts.inSampleSize = ssize;
//			opts.inPreferredConfig = Bitmap.Config.RGB_565;
//			opts.inPurgeable = true;
//			opts.inInputShareable = true;
//			mBitmap = BitmapFactory.decodeFile(path, opts);
//			if (null == mBitmap) {
//				ToastUtils.show(R.string.text_image_not_exists);
//				goPrevious();
//				return;
//			}
//			mBitmap = WeLearnImageUtil.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
//			if (isFromPhotoList) {
//				String absolutePath = getCacheDir().getAbsolutePath();
//				path = absolutePath + "publish.png";
//			}
//
//			WeLearnImageUtil.saveFile(path, mBitmap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * 图片旋转
//	 */
//	private void rotate(int rotate) {
//		if (null != mPhotoImage) {
//			int degree = 0;
//			if (rotate == R.id.question_photo_view_turn_right_btn) {
//				degree = 90;
//			} else {
//				degree = -90;
//			}
//			mPhotoImage.rotateImage(degree, path);
//		}
//	}
//
//	public void goPrevious() {
//		WeLearnFileUtil.deleteFile(path);
////		if (mPhotoImage != null) {
////			ImageLoader.recycleBitmap(mPhotoImage);
////		}
//		IntentManager.goToPayAnswerAskView(this, true);
//	}
//
//	@Override
//	public void onClick(View view) {
//		// picContainer.setVisibility(View.VISIBLE);
//		tixing.setVisibility(View.GONE);
//		switch (view.getId()) {
//		case R.id.back_layout:
//			goPrevious();
//			break;
//		case R.id.question_photo_view_turn_left_btn:
//			rotate(R.id.question_photo_view_turn_left_btn);
//			break;
//		case R.id.question_photo_view_turn_right_btn:
//			rotate(R.id.question_photo_view_turn_right_btn);
//			break;
//		case R.id.question_photo_view_nav_btn_back://重拍
//			//goPrevious();
//			if (isFromPhotoList) {
//				WeLearnFileUtil.deleteFile(path);
////				if (mPhotoImage != null) {
////					ImageLoader.recycleBitmap(mPhotoImage);
////				}
//				Bundle data = new Bundle();
//				data.putInt("tag", GlobalContant.PAY_ANSWER_ASK);
//				IntentManager.goToAlbumView(this, data);
//			}else {
//				IntentManager.startImageCapture(this, GlobalContant.PAY_ANSWER_ASK);
//			}
//			break;
//		case R.id.next_setp_layout:
//		case R.id.question_photo_view_nav_submit:
//			try {
//				Bitmap currentBm = mPhotoImage.getCroppedImage();
//				if (currentBm != null) {
//					WeLearnImageUtil.saveFile(path, currentBm);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			Bundle data = new Bundle();
//			data.putString(QUESTION_IMG_PATH, path);
//			IntentManager.goToPayAnswerAskView(this, data, true);
//			break;
//		}
//	}
//
//	@Override
//	public void onBackPressed() {
//		goPrevious();
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT) {
//			if (resultCode == RESULT_OK) {
//				String path = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator + "publish.png";
//				LogUtils.i(TAG, path);
//				if (!TextUtils.isEmpty(path)) {
//					autoFixOrientation(false);// , outWidth
//					mPhotoImage.setImageBitmap(mBitmap);
//				}
//			}
//		} 
//	}
//}

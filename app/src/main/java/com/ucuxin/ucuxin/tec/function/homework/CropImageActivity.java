package com.ucuxin.ucuxin.tec.function.homework;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edmodo.cropper.CropImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;

public class CropImageActivity extends BaseActivity implements OnClickListener {
	
	public static final String IMAGE_SAVE_PATH_TAG = "image_save_path_tag";

	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ON_TOUCH = 1;

	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;
	
	private TextView mBackBtn;
	private TextView mSubmitBtn;
	private Button mLeftBtn;
	private Button mRightBtn;

	private String path;
//	private String imageSavePath;
//	private FitBitmap mPhoto;
	private CropImageView mPhotoImage;
//	private int left;
//	private int top;
//	private int width;
//	private int height;
//	private ImageView tixing;

	private static final String TAG = CropImageActivity.class.getSimpleName();
	public static final String QUESTION_IMG_PATH = "quesiton_img_path";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mActionBar.hide();

		setContentView(R.layout.fragment_question_photo_view);

		setWelearnTitle(R.string.text_image_processing);
		
		findViewById(R.id.back_layout).setOnClickListener(this);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_nav_submit);
		nextStepLayout.setOnClickListener(this);
		

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

//	private Bitmap mBitmap;
	private boolean isFromPhotoList;

	private void showPhoto() {
		Intent intent = getIntent();
		if (intent != null) {
			path = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
//			imageSavePath = intent.getStringExtra(IMAGE_SAVE_PATH_TAG);
			isFromPhotoList = intent.getBooleanExtra("isFromPhotoList", false);
			if (!TextUtils.isEmpty(path)) {
				path = TecHomeWorkCheckDetailActivity.autoFixOrientation(path,isFromPhotoList,this ,mPhotoImage);// , outWidth
			}
		}

	}


	/**
	 * 图片旋转
	 */
	private void rotate(int rotate) {
		if (null != mPhotoImage) {
			int degree = 0;
			if (rotate == R.id.question_photo_view_turn_right_btn) {
				degree = 90;
			} else {
				degree = -90;
			}
			mPhotoImage.rotateImage(degree, path);
		}
	}

	public void goPrevious() {
		finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			goPrevious();
			break;
		case R.id.question_photo_view_turn_left_btn:
			rotate(R.id.question_photo_view_turn_left_btn);
			break;
		case R.id.question_photo_view_turn_right_btn:
			rotate(R.id.question_photo_view_turn_right_btn);
			break;
//		case R.id.question_photo_view_nav_btn_back://重拍
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
//		case R.id.question_photo_view_nav_submit:
		case R.id.next_setp_layout:
			try {
				Bitmap currentBm = mPhotoImage.getCroppedImage();
				if (currentBm != null) {
					WeLearnImageUtil.saveFile(path, currentBm);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Intent data = new Intent();
			// data.putExtra(QUESTION_IMG_PATH, imageSourcePath);

			data.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, path);
			
			setResult(RESULT_OK, data);
			finish();
			// IntentManager.goToPayAnswerAskView(this, data, true);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		goPrevious();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT) {
//			if (resultCode == RESULT_OK) {
//				String path = WeLearnFileUtil.getQuestionFileFolder().getAbsolutePath() + File.separator + "publish.png";
//				LogUtils.i(TAG, path);
//				if (!TextUtils.isEmpty(path)) {
//					path = TecHomeWorkCheckDetailActivity.autoFixOrientation(path,false,this ,mPhotoImage);// , outWidth
//				}
//			}
//		} 
//	}
}

package com.ucuxin.ucuxin.tec.function.question;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.ImageGridAdapter;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.ActionDef;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.homework.CropImageActivity;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ImageItem;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PayAnswerImageGridActivity extends BaseActivity implements OnClickListener{

	private List<ImageItem> mDataList;
	private ImageGridAdapter mImageGridAdapter;
	private int mTag;
	private int userid;
	private String username;
	private ArrayList<String> arrayList;
	public static final String IMAGE_PATH = "image_path";

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ActionDef.ACTION_PHOTO_SHOW:
				String path = msg.obj.toString();
				if (MyFileUtil.isFileExist(path)) {
					Bundle data = new Bundle();
					data.putString(IMAGE_PATH, msg.obj + "");
					data.putBoolean("isFromPhotoList", true);
					switch (mTag) {
//					case GlobalContant.PAY_ANSWER_ASK:
//						IntentManager.goToQuestionPhotoView(PayAnswerImageGridActivity.this, data);
//						break;
					case GlobalContant.PAY_ASNWER:

						String ccpath = msg.obj + "";

						Intent localIntent = new Intent();


						localIntent.setClass(PayAnswerImageGridActivity.this, CropImageActivity.class);
						localIntent.putExtra(PayAnswerImageGridActivity.IMAGE_PATH, ccpath);
						localIntent.putExtra(CropImageActivity.IMAGE_SAVE_PATH_TAG, ccpath);
						localIntent.putExtra("isFromPhotoList", true);
						startActivityForResult(localIntent, GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP);
						break;
					case GlobalContant.SEND_IMG_MSG:
						data.putInt(ChatMsgViewActivity.USER_ID, userid);
						data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, path);
						data.putString(ChatMsgViewActivity.USER_NAME, username);
						// IntentManager.goToChatListView(mActivity, data,
						// true);
						IntentManager.goToPreSendPicReViewActivity(PayAnswerImageGridActivity.this, data, true);
						break;
					case GlobalContant.CONTACT_SET_USER_IMG:
						//
						data.putInt("userid", SharePerfenceUtil.getInstance().getUserId());
						data.putInt("roleid", SharePerfenceUtil.getInstance().getUserRoleId());
						data.putString("userlogo", path);
						IntentManager.goToTeacherCenterView(PayAnswerImageGridActivity.this,TeacherCenterActivityNew.class, data);
						PayAnswerImageGridActivity.this.finish();
						break;
					case GlobalContant.CONTACT_SET_BG_IMG:
						data.putInt("userid", SharePerfenceUtil.getInstance().getUserId());
						data.putInt("roleid", SharePerfenceUtil.getInstance().getUserRoleId());
						data.putString("bgimg", path);
						IntentManager.goToTeacherCenterView(PayAnswerImageGridActivity.this,TeacherCenterActivityNew.class, data);
						PayAnswerImageGridActivity.this.finish();
						break;
					}
				} else {
					ToastUtils.show(R.string.text_file_not_exists);
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_image_grid);

		setWelearnTitle(R.string.text_album);

		findViewById(R.id.back_layout).setOnClickListener(this);
		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(PayAnswerAlbumActivity.IAMGE_LIST);
		mImageGridAdapter = new ImageGridAdapter(this, mDataList, mHandler);
		gridView.setAdapter(mImageGridAdapter);
		mImageGridAdapter.notifyDataSetChanged();

		Intent i = getIntent();
		if (i != null) {
			mTag = i.getIntExtra("tag", 0);
			arrayList = i.getStringArrayListExtra("viewPagerList");
			switch (mTag) {
			case GlobalContant.SEND_IMG_MSG:
				userid = i.getIntExtra(ChatMsgViewActivity.USER_ID, 0);
				username = i.getStringExtra(ChatMsgViewActivity.USER_NAME);
				
				break;
			// case GlobalContant.CONTACT_SET_USER_IMG:
			// //
			// break;
			// case GlobalContant.CONTACT_SET_BG_IMG:
			// break;
			}
		}
	}

	public void goPrevious() {
		Bundle data = new Bundle();
		data.putInt("tag", mTag);
		IntentManager.goToAlbumView(this, data);
	}

	@Override
	public void onBackPressed() {
		goPrevious();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			goPrevious();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.REQUEST_CODE_GET_IMAGE_FROM_CROP) {
			if (resultCode == RESULT_OK) {
				Bundle bData = new Bundle();
				String savePath = data.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
				bData.putString(IMAGE_PATH, savePath);
				bData.putBoolean("isFromPhotoList", true);
				bData.putBoolean("ifFirst", true);
				bData.putStringArrayList("viewPagerList", arrayList);
						IntentManager.goToPhotoView(PayAnswerImageGridActivity.this, bData);
			}
		}
	}
}

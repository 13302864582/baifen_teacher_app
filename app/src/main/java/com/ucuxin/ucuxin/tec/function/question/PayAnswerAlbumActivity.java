package com.ucuxin.ucuxin.tec.function.question;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.ImageBucketAdapter;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.ActionDef;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.controller.PayAnswerAlbumController;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ImageBucket;
import com.ucuxin.ucuxin.tec.utils.AlbumUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class PayAnswerAlbumActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private AlbumUtil mAlbumUtil;
	private List<ImageBucket> mDataList;
	private ImageBucketAdapter mAdapter;
	private PayAnswerAlbumController mPayAnswerAlbumController;
	private int mTag;
	private int userid;
	private String username;
	private ArrayList<String> arrayList;

	public static final String IAMGE_LIST = "image_list";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_album);

		setWelearnTitle(R.string.text_album);
		
		findViewById(R.id.back_layout).setOnClickListener(this);
		
		mAlbumUtil = AlbumUtil.getInstance();
		mAlbumUtil.init(this);

		mDataList = mAlbumUtil.getImagesBucketList(false);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		Intent i = getIntent();
		if (i != null) {
			mTag = i.getIntExtra("tag", 0);
			userid = getIntent().getIntExtra(ChatMsgViewActivity.USER_ID, 0);
			username = getIntent().getStringExtra(ChatMsgViewActivity.USER_NAME);
			arrayList = getIntent().getStringArrayListExtra("viewPagerList");
			if(arrayList==null){
				arrayList=new ArrayList<String>();	
			}
			arrayList .add(username);
		}

		GridView gridView = (GridView) findViewById(R.id.album_gridview);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		mPayAnswerAlbumController = new PayAnswerAlbumController(null);
		mPayAnswerAlbumController.setActivity(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ImageBucket imageBucket = new ImageBucket();
		imageBucket.setImageList(mDataList.get(position).getImageList());
		mPayAnswerAlbumController.setModel(imageBucket);
		Message msg = mPayAnswerAlbumController.getHandler().obtainMessage();
		msg.what = ActionDef.ACTION_IMAGE_GIRD;
		msg.arg1 = mTag;
		msg.arg2 = userid;
		msg.obj = arrayList;
		
		mPayAnswerAlbumController.getHandler().sendMessage(msg);
	}

	public void goPrevious() {
		switch (mTag) {
		case GlobalContant.PAY_ASNWER:
			IntentManager.goToGrabItemView(this, true);
			break;
		case GlobalContant.SEND_IMG_MSG:
			Bundle data = new Bundle();
			data.putInt(ChatMsgViewActivity.USER_ID, userid);
			data.putString(ChatMsgViewActivity.USER_NAME, username);
			IntentManager.goToChatListView(this, data, true);
			break;
		case GlobalContant.CONTACT_SET_USER_IMG:
			Bundle data1 = new Bundle();
			data1.putInt("userid", SharePerfenceUtil.getInstance().getUserId());
			data1.putInt("roleid", SharePerfenceUtil.getInstance().getUserRoleId());
			IntentManager.goToTeacherCenterView(this,TeacherCenterActivityNew.class, data1);
			finish();
			break;
		case GlobalContant.CONTACT_SET_BG_IMG:
			Bundle data2 = new Bundle();
			data2.putInt("userid", SharePerfenceUtil.getInstance().getUserId());
			data2.putInt("roleid", SharePerfenceUtil.getInstance().getUserRoleId());
			IntentManager.goToTeacherCenterView(this, TeacherCenterActivityNew.class,data2);
			finish();
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPayAnswerAlbumController.removeMsgInQueue();
		if(mAlbumUtil!=null){
			mAlbumUtil.nullContext();
			mAlbumUtil=null;
		}
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
}

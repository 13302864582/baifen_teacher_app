package com.ucuxin.ucuxin.tec.controller;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

import com.ucuxin.ucuxin.tec.constant.ActionDef;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerAlbumActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ImageBucket;
import com.ucuxin.ucuxin.tec.model.Model;

public class PayAnswerAlbumController extends BaseController {

	private Activity mActivity;
	
	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}
	
	public PayAnswerAlbumController(Model model) {
		super(model, null, PayAnswerAlbumController.class.getSimpleName());
	}

	@Override
	protected void handleEventMessage(Message msg) {
		switch (msg.what) {
		case ActionDef.ACTION_IMAGE_GIRD:
			ImageBucket imageBucket = getModel();
			Bundle data = new Bundle();
			ArrayList<String> arrayList=	(ArrayList<String>) msg.obj;
			String remove = arrayList.remove(arrayList.size()-1);
			data.putInt("tag", msg.arg1);
			data.putInt(ChatMsgViewActivity.USER_ID, msg.arg2);
			data.putString(ChatMsgViewActivity.USER_NAME, remove);
			data.putStringArrayList("viewPagerList", arrayList);
			data.putSerializable(PayAnswerAlbumActivity.IAMGE_LIST, (Serializable) imageBucket.getImageList());
			IntentManager.goToImageGridView(mActivity, data);
		}
	}
	
	private ImageBucket getModel() {
		ImageBucket imageBucket = null;
		boolean isImageBucket = this.mModel instanceof ImageBucket;
		if (isImageBucket) {
			imageBucket = (ImageBucket) mModel;
		}
		return imageBucket;
	}
}

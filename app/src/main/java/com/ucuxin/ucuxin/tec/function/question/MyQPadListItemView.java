package com.ucuxin.ucuxin.tec.function.question;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.question.model.AnswerListItemModel;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.AnswerGson;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.view.AbstractAnswerListItemView;

public class MyQPadListItemView extends AbstractAnswerListItemView {

	private long question_id;
	private int msubjectid;
	private Context mContext;
	private boolean isadd;
	private static final String TAG = MyQPadListItemView.class.getSimpleName();

	public static final String EXTRA_POSITION = "position";
	public static final String EXTRA_QUESTION_ID = "question_id";
	public static final String EXTRA_ISQPAD = "iaqpad";

	public MyQPadListItemView(Context context, boolean isadd) {
		super(context);
		this.mContext = context;
		this.isadd = isadd;
	}

	private AnswerListItemModel mItem;

	public void showData(AnswerListItemModel item, boolean isScroll) {

		mItem = item;
		question_id = item.getQid();
		msubjectid = item.getSubjectid();
		List<AnswerGson> answers = item.getAnswerlist();
		int size = answers == null ? 0 : answers.size();

		String answerUrl = "";
		if (size == 0) {
			answerUrl = mItem.getAnswerthumbnail();
		} else if (size == 1) {
			answerUrl = answers.get(0).getA_pic();
		}

		int state = 0;
		String statusStr = "";
		if (TextUtils.isEmpty(answerUrl)) {
			size = 0;
			state = item.getState();
			if (state == 0) {
				statusStr = "抢答中";
			} else if (state == 1) {
				statusStr = "答题中";
			} else if (state == 9) {
				statusStr = "被举报";
			} else if (state == 5) {
				statusStr = "被删除";
			}
		} else {
			size = 1;
			state = item.getA_state();
			statusStr = String.valueOf(status.get(state));
		}
		mStatus.setVisibility(View.VISIBLE);
		mStatus.setText(statusStr);

		// 如果是我的q板,信息为用户信息,状态为答案状态，如果没有答案，则状态是正在回答中
		ImageLoader.getInstance().loadImage(item.getAvatar(), mAnswerAvatar, R.drawable.ic_default_avatar, avatarSize,
				avatarSize / 10);
		mAnswerNick.setText(item.getStudname());

		mAnswerViewCnt.setText(item.getViewcnt() + "看过");
		mGradeSubject.setText(
				mContext.getString(R.string.text_question_id_format, item.getGrade(), item.getSubject(), question_id));
		SimpleDateFormat format = new SimpleDateFormat("y年M月d日");
		String askTime = format.format(new Date(item.getDatatime()));
		mPublishTime.setText(askTime);
		// Log.i(TAG, item.getQ_pic());
		disPlayQuestion(item.getImgpath(), mQuestionPic);
		switch (size) {
		case 0:
			mAnswerPicContainer.setVisibility(View.GONE);

			break;
		case 1:// 只有一张答案
			mAnswerPicContainer.setVisibility(View.VISIBLE);
			mAnswerPic1Container.setVisibility(View.VISIBLE);
			mAnswerPic2Container.setVisibility(View.GONE);
			mAnswerPic3Container.setVisibility(View.GONE);
			disPlayAnswer(answerUrl, mAnswerPic1, 1);
			break;
		}
	}

	private void disPlayAnswer(String url, NetworkImageView imageview, int position) {
		ImageLoader.getInstance().ajaxAnswerPic(url, imageview);
		imageview.setTag(position);
	}

	private void disPlayQuestion(String url, NetworkImageView imageview) {
		ImageLoader.getInstance().ajaxQuestionPic(url, imageview);
		imageview.setTag(0);
	}

	@Override
	protected void onClickCallback(View v) {
		switch (v.getId()) {
		case R.id.answerer_avatar:
			IntentManager.gotoPersonalPage((Activity) mContext, mItem.getStudid(), GlobalContant.ROLE_ID_STUDENT);
			break;
		default:
			if (null != v.getTag() && v.getTag() instanceof Integer) {
				int postion = (Integer) v.getTag();
				Bundle data = new Bundle();
				data.putInt(EXTRA_POSITION, postion);
				data.putLong(EXTRA_QUESTION_ID, question_id);
				data.putBoolean(EXTRA_ISQPAD, isadd);
				MobclickAgent.onEvent(mContext, "MyQaDetail");
				SharePerfenceUtil.getInstance().putInt("msubjectid", msubjectid);
				IntentManager.goToAnswerDetail((Activity) mContext, data, 1002);
			}
			break;
		}

	}
}

package com.ucuxin.ucuxin.tec.function;

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
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.AnswerGson;
import com.ucuxin.ucuxin.tec.model.AnswerListItemGson;
import com.ucuxin.ucuxin.tec.utils.DateUtil;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.view.AbstractAnswerListItemView;

public class AnswerListItemView extends AbstractAnswerListItemView {

	private long question_id;
	private Context mContext;

	private static final String TAG = AnswerListItemView.class.getSimpleName();

	public static final String EXTRA_POSITION = "position";
	public static final String EXTRA_QUESTION_ID = "question_id";
	public static final String EXTRA_ISQPAD = "iaqpad";

	/*
	 * public AnswerListItemView(Context context, AttributeSet attrs) {
	 * super(context, attrs); this.mContext = context; }
	 */
	public AnswerListItemView(Context context) {
		super(context);
		this.mContext = context;
	}

	private AnswerGson mLastAnswerer;
	

	public void showData(AnswerListItemGson item, boolean isScroll) {
		mAnswerListItemGson = item;
//		collectionContainer.setVisibility(View.VISIBLE);
//		praisecnt = item.getPraisecnt();
//		collectionCountTv.setText(praisecnt + "");// 收藏总数
//		int praise = item.getPraise();
//		if (praise == 0) {//0是未收藏,1是已经收藏
//			isCollected = false;
//		}else {
//			isCollected = true;
//		}
//		if (isCollected) {// 已经收藏是实心
//			collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang_pressed);
//		} else {// 未收藏是空心
//			collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang);
//		}
		
		
		question_id = item.getQuestionid();
		List<AnswerGson> answers = item.getAnswerlist();
		int size = answers == null ? 0 : answers.size();
		if (answers.size() > 0) {
			mLastAnswerer = answers.get(answers.size() - 1);
			ImageLoader.getInstance().loadImage(mLastAnswerer.getAvatar(), mAnswerAvatar, R.drawable.ic_default_avatar,
					avatarSize, avatarSize / 10);
			mColleage.setText(mLastAnswerer.getSchools());
			mStatus.setText(String.valueOf(status.get(mLastAnswerer.getA_state())));
			mAnswerNick.setText(mLastAnswerer.getTeach_name());
		} else {
			mStatus.setVisibility(View.GONE);
		}
		mStatus.setVisibility(View.GONE);
		mAnswerViewCnt.setText(mContext.getString(R.string.text_read_count, item.getViewcnt()));
		if (item.getDuration() > 0 && item.getViewcnt() == 0) {
			mAnswerViewCnt.setText(mContext.getString(R.string.text_minutes_to_answer, item.getDuration()));
		}
		mGradeSubject.setText(mContext.getString(R.string.text_question_id_format, item.getGrade(), item.getSubject(),
				question_id));
		String dateStr = item.getDatatime();
		String grabTimeStr = item.getGrabtime();
		if (!TextUtils.isEmpty(dateStr)) {
			String publishTime = DateUtil.getDisplayTime(DateUtil.parseString2Date(dateStr, "yyyy-MM-dd HH:mm:ss"));
			mPublishTime.setText(publishTime);
		} else {
			String grabTime = DateUtil.getDisplayTime(DateUtil.parseString2Date(grabTimeStr, "yyyy-MM-dd HH:mm:ss"));
			mPublishTime.setText(grabTime);
		}

		if (null != item) {
			LogUtils.i(TAG, item.getQ_pic());
		}

		disPlayQuestion(item.getQ_pic(), mQuestionPic);
		switch (size) {
		case 0:
			mAnswerPicContainer.setVisibility(View.GONE);
			break;
		case 1:// 只有一张答案
			mAnswerPicContainer.setVisibility(View.VISIBLE);
			mAnswerPic1Container.setVisibility(View.VISIBLE);
			mAnswerPic2Container.setVisibility(View.GONE);
			mAnswerPic3Container.setVisibility(View.GONE);
			disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
			break;
		case 2:// 只有2张答案
			mAnswerPicContainer.setVisibility(View.VISIBLE);
			mAnswerPic1Container.setVisibility(View.VISIBLE);
			mAnswerPic2Container.setVisibility(View.VISIBLE);
			mAnswerPic3Container.setVisibility(View.GONE);
			disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
			disPlayAnswer(answers.get(1).getA_pic(), mAnswerPic2, 2);
			break;
		case 3:
			mAnswerPicContainer.setVisibility(View.VISIBLE);
			mAnswerPic1Container.setVisibility(View.VISIBLE);
			mAnswerPic2Container.setVisibility(View.VISIBLE);
			mAnswerPic3Container.setVisibility(View.VISIBLE);
			disPlayAnswer(answers.get(0).getA_pic(), mAnswerPic1, 1);
			disPlayAnswer(answers.get(1).getA_pic(), mAnswerPic2, 2);
			disPlayAnswer(answers.get(2).getA_pic(), mAnswerPic3, 3);
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
			IntentManager.gotoPersonalPage((Activity) mContext, mLastAnswerer.getTeach_id(), mLastAnswerer.getRoleid());
			break;
//		case R.id.collection_container_linearlayout_answerlistitem:
//			isCollected = !isCollected;
//			if (isCollected) {// 已经收藏是实心
//				collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang_pressed);
//				praisecnt += 1;
//				mAnswerListItemGson.setPraise(1);//0是未收藏,1是已经收藏
//			} else {// 未收藏是空心
//				collectionIconIv.setImageResource(R.drawable.ic_up_and_shoucang);
//				praisecnt -= 1;
//				mAnswerListItemGson.setPraise(0);//0是未收藏,1是已经收藏
//			}
//			mAnswerListItemGson.setPraisecnt(praisecnt);
//			collectionCountTv.setText("" + praisecnt);
//			WeLearnApi.collectcnt(question_id);
//			break;
		default:
			if (null != v.getTag() && v.getTag() instanceof Integer) {
				int postion = (Integer) v.getTag();
				Bundle data = new Bundle();
				data.putInt(EXTRA_POSITION, postion);
				data.putLong(EXTRA_QUESTION_ID, question_id);
				data.putBoolean(EXTRA_ISQPAD, false);
				MobclickAgent.onEvent(mContext, "OneQuestionMoreAnswersDetail");
				IntentManager.goToAnswerDetail((Activity) mContext, data);
			}

			break;
		}
	}
}

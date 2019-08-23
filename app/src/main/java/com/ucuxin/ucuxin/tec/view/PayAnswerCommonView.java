package com.ucuxin.ucuxin.tec.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerQuestionDetailActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.QuestionModelGson;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;

import androidx.annotation.Nullable;

public class PayAnswerCommonView extends FrameLayout implements OnClickListener {

	private static final String TAG = PayAnswerCommonView.class.getSimpleName();
	private Activity mActivity;

	private NetworkImageView  mPayAnswerUserAvatar;
	private ImageView mPayAnswerQuestionImg;
	private TextView mPayAnswerNick, /*mPayAnswerCredit,*/ mPayAnswerGrade, mPayAnswerSubject, mPayAnswerReword_val,
			mAnswerDesc, tv_xuehao, /*tv_extra_value, */tv_vip_info, pay_answer_reward;

	private AnimationDrawable animationDrawable;
	// private TextView mQuestionIdView;

	private String mAudioPath;
	private String mImgUrl;
	// private boolean isShowQuestionImage = false;
	private static int isShowQuestionImage = 0;
	private int avatarSize;
	private QuestionModelGson mQuestionModelGson;
	
	private CommonRequestListener mCommonRequestListener;

	public PayAnswerCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpViews();
		this.mActivity = (Activity) context;
	}

	public PayAnswerCommonView(Context context) {
		super(context);
		setUpViews();
	}

	private void setUpViews() {
		View payAnswerView = LayoutInflater.from(getContext()).inflate(R.layout.pay_answer_common, null);
		mPayAnswerUserAvatar = (NetworkImageView) payAnswerView.findViewById(R.id.pay_answer_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.pay_answer_user_avatar_size);
		mPayAnswerNick = (TextView) payAnswerView.findViewById(R.id.pay_answer_nick);

		//mPayAnswerCredit = (TextView) payAnswerView.findViewById(R.id.pay_answer_credit);
		mPayAnswerQuestionImg = (ImageView) payAnswerView.findViewById(R.id.pay_answer_question_img);

		mPayAnswerGrade = (TextView) payAnswerView.findViewById(R.id.pay_answer_grade);
		mPayAnswerSubject = (TextView) payAnswerView.findViewById(R.id.pay_answer_subject);
		pay_answer_reward = (TextView) payAnswerView.findViewById(R.id.pay_answer_reward);
		mPayAnswerReword_val = (TextView) payAnswerView.findViewById(R.id.pay_answer_reward_val);
		mAnswerDesc = (TextView) payAnswerView.findViewById(R.id.pay_answer_desc);
		// mQuestionIdView = (TextView)
		// payAnswerView.findViewById(R.id.question_id);
		tv_vip_info = (TextView) payAnswerView.findViewById(R.id.tv_vip_info);
		tv_xuehao = (TextView) payAnswerView.findViewById(R.id.tv_xuehao);
		//tv_extra_value = (TextView) payAnswerView.findViewById(R.id.tv_extra_value);

		mPayAnswerNick.setOnClickListener(this);
		mPayAnswerUserAvatar.setOnClickListener(this);
		mPayAnswerQuestionImg.setOnClickListener(this);
		
		 mCommonRequestListener = new CommonRequestListener();

		addView(payAnswerView);
	}

	public void showData(QuestionModelGson mObj) {
		mPayAnswerNick.setClickable(true);
		mPayAnswerUserAvatar.setClickable(true);
		mPayAnswerQuestionImg.setClickable(true);
		mQuestionModelGson = mObj;
		// mQuestionIdView.setText("问题id: " + mObj.getQuestion_id());
		mImgUrl = mObj.getImgurl();
		loadQImg();
		// ImageLoader.ajaxQuestionPic(mObj.getImgurl(), mPayAnswerQuestionImg);
		// ImageLoader.getInstance().loadImageWithDefaultAvatar(mObj.getAvatar(),
		// mPayAnswerUserAvatar, avatarSize,
		// avatarSize / 10);
		ImageLoader.getInstance().loadImageWithDefaultParentAvatar(mObj.getAvatar(), mPayAnswerUserAvatar, avatarSize,
				avatarSize / 10);

		int orgid = mObj.getOrgid();
		mPayAnswerNick.setText(mObj.getName());
		tv_xuehao.setText("学号: " + mObj.getStudent_id());
		//mPayAnswerCredit.setText("信用 " + mObj.getCredit());
		String sdnUrl = mObj.getSndurl();

		if (!TextUtils.isEmpty(mObj.getDescription())) {
			mAnswerDesc.setVisibility(View.VISIBLE);
			mAnswerDesc.setText(mObj.getDescription());
		} else {
			mAnswerDesc.setVisibility(View.GONE);
		}

		mPayAnswerGrade.setText(mObj.getGrade());
		mPayAnswerSubject.setText(mObj.getSubject());

		pay_answer_reward.setVisibility(View.VISIBLE);
		mPayAnswerReword_val.setText("￥ " + mObj.getBounty());

		if (!TextUtils.isEmpty(mObj.getVip_level_content())) {
			tv_vip_info.setVisibility(View.VISIBLE);
			tv_vip_info.setText(mObj.getVip_level_content());
		} else {
			tv_vip_info.setVisibility(View.GONE);
		}

		/*if (!TextUtils.isEmpty(mObj.getVip_additional_content())) {
			tv_extra_value.setText(mObj.getVip_additional_content());
		} else {
			tv_extra_value.setText("");
		}*/
	}

	private void loadQImg() {
		isShowQuestionImage = 0;// 0是加载中
//		ImageLoader.getInstance().resetUrl(mPayAnswerQuestionImg);
//		mPayAnswerQuestionImg.setImageResource(R.drawable.loading);
//		ImageLoader.getInstance().loadImage(mImgUrl, mPayAnswerQuestionImg, R.drawable.loading, R.drawable.retry,
//				new OnImageLoadListener() {
//					@Override
//					public void onSuccess(ImageContainer arg0) {
//						if (null != arg0.getBitmap()) {
//							// mPayAnswerQuestionImg.setImageBitmap(arg0.getBitmap());
//						}
//						// isShowQuestionImage = true;
//						isShowQuestionImage = 1;// 1是成功
//					}
//
//					@Override
//					public void onFailed(VolleyError arg0) {
//
//						// isShowQuestionImage = false;
//						isShowQuestionImage = 2;// 2是失败
//					}
//				});
		
		
		Glide.with(mActivity).load(mImgUrl).placeholder(R.drawable.loading).listener(mCommonRequestListener).into(mPayAnswerQuestionImg);
	}
	
	
	static class CommonRequestListener implements RequestListener{
		@Override
		public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
			if(e!=null){

				LogUtils.e(TAG, "onException:" + e.toString());
			}
			isShowQuestionImage = 2;// 2是失败
			return false;
		}

		@Override
		public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
			LogUtils.d(TAG, "onResourceReady:" + resource.toString());
			isShowQuestionImage = 1;// 1是成功
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_answer_question_img:
			if (isShowQuestionImage == 1) {
				if (!TextUtils.isEmpty(mImgUrl)) {// 1是成功
					Bundle data = new Bundle();
					data.putString(PayAnswerQuestionDetailActivity.IMG_URL, mImgUrl);
					IntentManager.goToQuestionDetailPicView(mActivity, data);
				}
			} else if (isShowQuestionImage == 2) {// 2是失败
				loadQImg();
			}
			break;
		case R.id.pay_answer_nick:
		case R.id.pay_answer_user_avatar:
			if (mQuestionModelGson == null || mQuestionModelGson.getStudent_id() == 0
					|| mQuestionModelGson.getRoleid() == 2) {
				break;
			}
			IntentManager.gotoPersonalPage(mActivity, mQuestionModelGson.getStudent_id(),
					mQuestionModelGson.getRoleid());
			// mQuestionModelGson = null;
			break;
		}
	}

	/**
	 * 换题发现为空
	 */
	public void showDataNullQuestion() {
		ImageLoader.getInstance().loadImage(null, mPayAnswerUserAvatar, R.drawable.default_contact_image);
		// ImageLoader.getInstance().loadImage(null, mPayAnswerQuestionImg,
		// R.drawable.ic_no_question);
		Glide.with(mActivity).load(R.drawable.ic_no_question).centerCrop().placeholder(R.drawable.ic_no_question).into(mPayAnswerQuestionImg);

		// mPayAnswerUserAvatar.setImageResource(R.drawable.default_contact_image);
		// mPayAnswerQuestionImg.setImageResource(R.drawable.ic_no_question);
		// mPayAnswerQuestionImg

		tv_xuehao.setText("");
		tv_vip_info.setVisibility(View.GONE);

		mPayAnswerNick.setText("");
		//mPayAnswerCredit.setVisibility(View.INVISIBLE);

		mAnswerDesc.setVisibility(View.GONE);
		mPayAnswerGrade.setText("");
		mPayAnswerSubject.setText("");

		pay_answer_reward.setVisibility(View.GONE);
		mPayAnswerReword_val.setText("");

		mPayAnswerNick.setClickable(false);
		mPayAnswerUserAvatar.setClickable(false);
		mPayAnswerQuestionImg.setClickable(false);

	}

	public void stopAudio() {
		MediaUtil.getInstance(false).stopVoice(animationDrawable);
		// mPayAnswerPlay.setImageResource(R.drawable.ic_play2);
	}

}

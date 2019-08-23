package com.ucuxin.ucuxin.tec.function.communicate;

import java.util.Date;
import java.util.List;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerQuestionDetailActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.FitBitmap;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.DateUtil;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.AbstractMsgChatItemView;
import com.ucuxin.ucuxin.tec.view.popwindow.ResendPopupWindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 接收消息的view
 * 
 * @author parsonswang
 * 
 */
public class ChatMsgSendView extends AbstractMsgChatItemView implements OnClickListener {

	private TextView mSendTime;
	private NetworkImageView mUserAvatar;
	private TextView mMsgContent;
	private Context mContext;
	private FrameLayout mVoiceMsgContainer;
	private ImageView mIconPlay;
	private FrameLayout mChatMsgSendImageContainer;
	private ProgressBar waitBar;
	private ImageView mChatMsgSendImage;
	private TextView mAudtioTime;
	private ImageView mSendError;

	private String mAudioPath;
	private ChatInfo mChatInfo;
	// protected boolean isShowPoP;
	private int avatarSize;

	private static final String TAG = ChatMsgSendView.class.getSimpleName();

	public ChatMsgSendView(Context context) {
		super(context);
		mContext = context;
		setupViews(context);
	}

	public ChatMsgSendView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupViews(context);
	}

	@SuppressLint("InflateParams")
	public void setupViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_send_view, null);
		mSendTime = (TextView) view.findViewById(R.id.from_user_send_time);
		mUserAvatar = (NetworkImageView) view.findViewById(R.id.from_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.msg_list_avatar_size);
		mMsgContent = (TextView) view.findViewById(R.id.send_msg_content);
		mVoiceMsgContainer = (FrameLayout) view.findViewById(R.id.voice_send_msg_container);
		mIconPlay = (ImageView) view.findViewById(R.id.ic_voice_send_msg_play);
		mAudtioTime = (TextView) view.findViewById(R.id.msg_audiotime);
		mChatMsgSendImageContainer = (FrameLayout) view.findViewById(R.id.send_msg_img_container);
		mChatMsgSendImage = (ImageView) view.findViewById(R.id.send_msg_img);
		waitBar = (ProgressBar) findViewById(R.id.waiting_bar);
		mSendError = (ImageView) view.findViewById(R.id.ic_send_error);

		mUserAvatar.setOnClickListener(this);
		mVoiceMsgContainer.setOnClickListener(this);
		mChatMsgSendImageContainer.setOnClickListener(this);
		mSendError.setOnClickListener(this);

		addView(view);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public void showData(ChatInfo info, List<ChatInfo> chatinfos, final int position) {
		mChatInfo = info;
		long timestamp = info.getLocalTimestamp();
		long preMsgTimestamp = getPreMsgTimestamp(chatinfos, position - 1);
		LogUtils.i(TAG, timestamp + "");
		boolean isAfter = DateUtil.isAfter(timestamp, preMsgTimestamp);
		if (isAfter) {
			mSendTime.setVisibility(View.VISIBLE);
		} else {
			mSendTime.setVisibility(View.GONE);
		}
		mSendTime.setText(DateUtil.getDisplayChatTime(new Date(info.getLocalTimestamp())));

		if (info.isSendFail()) {
			mSendError.setVisibility(View.VISIBLE);
		} else {
			mSendError.setVisibility(View.GONE);
		}
		UserInfoModel user = info.getUser();
		String avatar = "";
		if (user != null) {
			avatar = user.getAvatar_100();
		}
		if (TextUtils.isEmpty(avatar)) {
			/*
			 * 更改数据调用方式 modified by yh 2015-01-07 Start ----------------------
			 * OLD CODE ---------------------- List<AccountModel> models = new
			 * UserInfoDBHelper(mContext).query(1);
			 */
			UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			if(null != uInfo){
				avatar = uInfo.getAvatar_100();
			}
			// 更改数据调用方式 modified by yh 2015-01-07 End
		}
		LogUtils.i(TAG, avatar);
		ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar, mUserAvatar, avatarSize, avatarSize / 10);
		mMsgContent.setOnClickListener(this);
		if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_TEXT) {// 文字
			mMsgContent.setVisibility(View.VISIBLE);
			mMsgContent.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showPop(mMsgContent, position);
					}

					return true;
				}
			});

			mVoiceMsgContainer.setVisibility(View.GONE);
			mChatMsgSendImageContainer.setVisibility(View.GONE);
			mMsgContent.setText(info.getMsgcontent());
			mAudtioTime.setVisibility(View.GONE);
		} else if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {// 语音
			mAudioPath = info.getPath();
			mMsgContent.setVisibility(View.GONE);
			mVoiceMsgContainer.setVisibility(View.VISIBLE);
			mChatMsgSendImageContainer.setVisibility(View.GONE);
			mAudtioTime.setVisibility(View.VISIBLE);
			mVoiceMsgContainer.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showVodPop(mVoiceMsgContainer, position);
					}
					return true;
				}
			});

			int audiotime = info.getAudiotime();
			if (audiotime != 0) {
				mAudtioTime.setText(audiotime + "s");
				switch (audiotime) {
				case 1:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 60));
					break;
				case 2:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 70));
					break;
				case 3:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 80));
					break;
				case 4:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 90));
					break;
				case 5:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 100));
					break;
				case 6:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 110));
					break;
				case 7:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 120));
					break;
				case 8:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 130));
					break;
				case 9:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 140));
					break;
				case 10:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 150));
					break;
				case 11:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 160));
					break;
				case 12:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 170));
					break;
				case 13:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 180));
					break;
				case 14:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 190));
					break;
				default:
					mVoiceMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mContext, 200));
					break;
				}

			}
		} else if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_IMG) {// 图片
			mMsgContent.setVisibility(View.GONE);
			mVoiceMsgContainer.setVisibility(View.GONE);
			mChatMsgSendImageContainer.setVisibility(View.VISIBLE);

			waitBar.setVisibility(View.GONE);

			mAudtioTime.setVisibility(View.GONE);
			final String path = info.getPath();
			if (!TextUtils.isEmpty(path)) {
				FitBitmap fm = WeLearnImageUtil.getResizedImage(path, null, null, 600, false, 0);
				if (fm != null) {
					final Bitmap getmBitmap = fm.getmBitmap();
					mChatMsgSendImage.setImageBitmap(getmBitmap);
					mChatMsgSendImageContainer.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View arg0) {
							if (GlobalVariable.mChatMsgViewActivity != null) {
								GlobalVariable.mChatMsgViewActivity.showSavePicPop(mChatMsgSendImageContainer,
										getmBitmap, path, position);
							}
							return true;
						}
					});
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (GlobalVariable.mChatMsgViewActivity != null) {
			GlobalVariable.mChatMsgViewActivity.disMissPop();
		}
		switch (view.getId()) {
		case R.id.voice_send_msg_container:
			if (TextUtils.isEmpty(mAudioPath)) {
				return;
			}
			play(mIconPlay, mAudioPath, false);
			break;
		case R.id.send_msg_img_container:
			Bundle data = new Bundle();
			data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, mChatInfo.getPath());
			data.putBoolean("doNotRoate", true);
			IntentManager.goToQuestionDetailPicView((Activity) mContext, data);
			break;
		case R.id.ic_send_error:
			new ResendPopupWindow((Activity) mContext, view, GlobalVariable.mChatMsgViewActivity, mChatInfo);
			break;
		case R.id.from_user_avatar:
			IntentManager.gotoPersonalPage((Activity) mContext, SharePerfenceUtil.getInstance().getUserId(), SharePerfenceUtil
					.getInstance().getUserRoleId());
			break;
		case R.id.send_msg_content:

			break;

		default:
			break;

		}
	}
}

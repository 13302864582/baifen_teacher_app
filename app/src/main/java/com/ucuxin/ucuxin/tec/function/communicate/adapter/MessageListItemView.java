package com.ucuxin.ucuxin.tec.function.communicate.adapter;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.DateUtil;

public class MessageListItemView extends FrameLayout {

	private NetworkImageView mAvatar;
	private TextView mTime;
	private TextView mName;
	private TextView mMsgContent;
	private TextView unread_tv;
	private Context mContext;
	private View mAvatar_container;
	private int avatarSize;
	// private static final String TAG = MessageListItemView.class.getSimpleName();

	public MessageListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupView(context);
	}

	public MessageListItemView(Context context) {
		super(context);
		setupView(context);
	}

	@SuppressLint("InflateParams")
	private void setupView(Context context) {
		mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
		mAvatar_container = view.findViewById(R.id.send_msg_user_avatar_frame);
		mAvatar = (NetworkImageView) view.findViewById(R.id.send_msg_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.communicate_contacts_list_avatar_size);
		unread_tv = (TextView) view.findViewById(R.id.send_msg_user_avatar_unread);
		mTime = (TextView) view.findViewById(R.id.send_msg_time);
		mName = (TextView) view.findViewById(R.id.send_msg_user_name);
		mMsgContent = (TextView) view.findViewById(R.id.send_msg_content);

		addView(view);
	}

	public void showData(ChatInfo info) {
		final int fromUserId = info.getFromuser();
		UserInfoModel ug = info.getUser();
		int roleid = 0;
		if (info.isReaded()) {
			unread_tv.setVisibility(View.GONE);
		} else {
			unread_tv.setVisibility(View.VISIBLE);
			int unReadCount = info.getUnReadCount();
			String str = "" + unReadCount;
			if (unReadCount > 99) {
				str = "99+";
			}
			unread_tv.setText(str);
		}
		// 修改头像圆角问题
		if (ug != null) {
			int defaultOrErrorAvatarId = R.drawable.ic_default_avatar;
			switch (fromUserId) {
			case GlobalContant.USER_ID_SYSTEM:
				defaultOrErrorAvatarId = R.drawable.ic_system_avatar;
				break;
			case GlobalContant.USER_ID_HELPER:
				defaultOrErrorAvatarId = R.drawable.ic_solve_helper_avatar;
				break;
			}
			ImageLoader.getInstance().loadImage(ug.getAvatar_100(), mAvatar, defaultOrErrorAvatarId,
					defaultOrErrorAvatarId, avatarSize, avatarSize / 10);
			mName.setText(ug.getName());
		} else {
			ImageLoader.getInstance().loadImage(null, mAvatar, R.drawable.ic_default_avatar,
					R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
			mName.setText("");
		}
		
		switch (fromUserId) {
		case GlobalContant.USER_ID_SYSTEM:
		case GlobalContant.USER_ID_HELPER:
			roleid = 0;
			mAvatar_container.setEnabled(false);
			break;
		default:
			if (ug!=null) {
				roleid = ug.getRoleid();
				mAvatar_container.setEnabled(true);
			}
			break;
		}
		final int finalRoleid = roleid;
		mAvatar_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putInt("userid", fromUserId);
				data.putInt("roleid", finalRoleid);
				if (finalRoleid == GlobalContant.ROLE_ID_COLLEAGE) {
					IntentManager.goToTeacherInfoView((Activity) mContext, data);
				} else if (finalRoleid == GlobalContant.ROLE_ID_STUDENT) {
					IntentManager.goToStudentInfoView((Activity) mContext, data);
				}
				
			}
		});
		
		

		switch (info.getContenttype()) {
		case GlobalContant.MSG_CONTENT_TYPE_TEXT:
		case GlobalContant.MSG_CONTENT_TYPE_JUMP:
		case GlobalContant.MSG_CONTENT_TYPE_JUMP_URL:
			mMsgContent.setText(info.getMsgcontent());
			break;
		case GlobalContant.MSG_CONTENT_TYPE_AUDIO:
			mMsgContent.setText(getResources().getString(R.string.text_default_audio));
			break;
		case GlobalContant.MSG_CONTENT_TYPE_IMG:
			mMsgContent.setText(getResources().getString(R.string.text_default_image));
			break;
		}
		if (info.getType() == GlobalContant.MSG_TYPE_RECV) {
			mTime.setText(DateUtil.getDisplayChatTimeWithOutSeconds(new Date(DateUtil
					.convertTimestampToLong((float) info.getTimestamp() * 1000))));
		} else {
			mTime.setText(DateUtil.getDisplayChatTimeWithOutSeconds(new Date(info.getLocalTimestamp())));
		}
	}
}

package com.ucuxin.ucuxin.tec.function.communicate;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.constant.ResponseCmdDef;
import com.ucuxin.ucuxin.tec.controller.ChatMsgController;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.function.communicate.adapter.ChatListAdapter;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerQuestionDetailActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.FitBitmap;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.reveiver.HeadsetPlugReceiver;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MediaUtil;
import com.ucuxin.ucuxin.tec.utils.MediaUtil.RecordCallback;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.utils.WeLearnUiUtil;
import com.ucuxin.ucuxin.tec.view.AndroidBug5497Workaround;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView;
import com.ucuxin.ucuxin.tec.view.xlistview.XListView.IXListViewListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ChatMsgViewActivity extends BaseActivity
		implements OnClickListener, OnTouchListener, INetWorkListener, IXListViewListener, HttpListener {
	private XListView mChatList;

	private ChatListAdapter mAdapter;
	private List<ChatInfo> mChatInfoList = new ArrayList<ChatInfo>();
	private ImageButton mTypeChoiceBtn;
	private ImageView mSendMsgBtn;
	private EditText mMsgSendText;
	private ImageView mTextVoiceChoice;
	private Button mSendVoiceMsgBtn;
	private SendVoiceMsgCallback mCallback;
	private InputMethodManager mImm;
	private LinearLayout mChatPhotoContainer;
	private ImageView mChatCameraBtn;
	private ImageView mChatAlbumBtn;
	private RelativeLayout mMsgSendContainer;
	protected boolean isShowPoP;
	private List<ChatInfo> reChats = new ArrayList<ChatInfo>();
	private TextView title;
	private static HashMap<Double, ChatInfo> map;
	private String name;
	private boolean isShowPhotoMenu = false;// 是否显示图片菜单
	private int userid;
	private int pageIndex = 0;
	private boolean mIsVoiceMsg = false;// 是否语音消息
	private LinearLayout ll;
	private TextView mCopyView;

	private HeadsetPlugReceiver headsetPlugReceiver;

	private static final String TAG = ChatMsgViewActivity.class.getSimpleName();

	public static final String USER_NAME = "username";
	public static final String USER_ID = "userid";

	public ChatMsgController mController;

	private PopupWindow copyTextPop;

	private PopupWindow savePicPop;

	private PopupWindow vodPop;

	private static boolean isFromNoti;
	private boolean isCancel;
	private String audioName;
	private long audioTime;
	private int position;
	private Bitmap mSaveBm;
	private String mSavePath;
	private int deletePosition;
	private boolean isQueryAll = false;

	private NotificationManager notificationManager;

	private class SendVoiceMsgCallback implements RecordCallback {

		@Override
		public void onAfterRecord(float recodrTime) {
			if (isCancel) {
				MyFileUtil.deleteFile(audioName);
				isCancel = false;
				MediaUtil.getInstance(true).setIsCancel(isCancel);
			} else {
				sendAudioMsg(recodrTime);
				int roleId = SharePerfenceUtil.getInstance().getUserRoleId();
				if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
					MobclickAgent.onEvent(ChatMsgViewActivity.this, "TecherChat");
				} else {// 学生发送消息
					MobclickAgent.onEvent(ChatMsgViewActivity.this, "StudentChat");
				}
			}
		}

	}

	private TextWatcher mWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (s.toString().length() > 0) {
				mTypeChoiceBtn.setVisibility(View.GONE);
				mSendMsgBtn.setVisibility(View.VISIBLE);
			} else {
				mTypeChoiceBtn.setVisibility(View.VISIBLE);
				mSendMsgBtn.setVisibility(View.GONE);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			if (pageIndex > 1) {
				scrollChatListToBottom();
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x01:
				mChatInfoList = (List<ChatInfo>) msg.obj;
				if (mChatInfoList != null && mChatInfoList.size() > 0) {
					UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(userid, true);
					if (user == null) {
						isQueryAll = true;
						WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid, ChatMsgViewActivity.this);
					} else {
						title.setText(user.getName());

						Iterator<ChatInfo> chk_it = mChatInfoList.iterator();
						while (chk_it.hasNext()) {
							ChatInfo chatInfo = chk_it.next();
							if ((chatInfo.getContenttype() == GlobalContant.MSG_TYPE_RECV
									| chatInfo.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP)
									&& TextUtils.isEmpty(chatInfo.getMsgcontent())) {
								chk_it.remove();
							}
						}
						setChatInfoList(user);
					}
				}
				break;
			case 555:
				double timestamp = (Double) msg.obj;
				if (map.containsKey(timestamp)) {
					ChatInfo chatInfo = map.remove(timestamp);
					if (chatInfo != null) {
						chatInfo.setSendFail(true);
						mAdapter.setData(mChatInfoList);
						ToastUtils.show(R.string.text_send_timeout);
						WLDBHelper.getInstance().getWeLearnDB().update(chatInfo);
					}
				}
				// sendFail();
				break;
			}
		}
	};

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.fragment_chat_view, null);
		setContentView(view);
		AndroidBug5497Workaround.assistActivity(this);

		if (map == null) {
			map = new HashMap<Double, ChatInfo>();
		}
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// add headset receiver
		registerHeadsetPlugReceiver();

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Log.e(TAG, "---onCreateView---");

		mChatList = (XListView) view.findViewById(R.id.chat_list);
		mTypeChoiceBtn = (ImageButton) view.findViewById(R.id.send_img_msg);

		mSendMsgBtn = (ImageView) view.findViewById(R.id.send_msg_btn);

		mMsgSendText = (EditText) view.findViewById(R.id.msg_send_content);

		mTextVoiceChoice = (ImageView) view.findViewById(R.id.chat_text_voice_choice);
		mSendVoiceMsgBtn = (Button) view.findViewById(R.id.send_voice_msg_btn);
		mChatPhotoContainer = (LinearLayout) view.findViewById(R.id.chat_photo_container);
		mChatCameraBtn = (ImageView) view.findViewById(R.id.chat_camera_btn);
		mChatAlbumBtn = (ImageView) view.findViewById(R.id.chat_album_btn);
		mMsgSendContainer = (RelativeLayout) view.findViewById(R.id.msg_send_container);
		ll = (LinearLayout) view.findViewById(R.id.ll);
		name = getIntent().getStringExtra(USER_NAME);
		// mActionBar.setTitle(name);
		title = (TextView) view.findViewById(R.id.title);
		title.setText(name);

		mAdapter = new ChatListAdapter(this);
		mChatList.setAdapter(mAdapter);
		mChatList.setPullLoadEnable(false);
		mChatList.setPullRefreshEnable(true);
		mChatList.setXListViewListener(this);
		mChatList.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				hidePhotoContainer(true);
				disMissPop();
				return false;
			}
		});
		ll.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				int heightDiff = ll.getRootView().getHeight() - ll.getHeight();
				if (heightDiff > 100) {
					// 键盘弹出

					mChatPhotoContainer.setVisibility(View.GONE);
				} else {

				}
			}
		});
		mMsgSendText.addTextChangedListener(mWatcher);
		mMsgSendText.setOnClickListener(this);
		mTextVoiceChoice.setOnClickListener(this);
		mSendVoiceMsgBtn.setOnTouchListener(this);
		mSendMsgBtn.setOnClickListener(this);
		mTypeChoiceBtn.setOnClickListener(this);
		mChatCameraBtn.setOnClickListener(this);
		mChatAlbumBtn.setOnClickListener(this);

		mCallback = new SendVoiceMsgCallback();

		if (!mIsVoiceMsg) {
			showSendMsgTextView();
		}
		if (isShowPhotoMenu) {
			mChatPhotoContainer.setVisibility(View.GONE);
		}
		Intent intent = getIntent();
		if (intent != null) {
			userid = intent.getIntExtra(USER_ID, 0);
			isFromNoti = intent.getBooleanExtra("isFromNoti", false);
			if (GlobalVariable.mChatMsgViewActivity != null) {
				if (isFromNoti && GlobalVariable.mChatMsgViewActivity != this) {
					if (GlobalVariable.mChatMsgViewActivity.mController != null) {
						GlobalVariable.mChatMsgViewActivity.mController.removeMsgInQueue();
					}
					WelearnHandler.getInstance().removeMessage(MsgConstant.MSG_DEF_CHAT_LIST);
					WelearnHandler.getInstance().removeMessage(MsgConstant.MSG_DEF_MSG_RESULT);
				}
			}
			if (userid != 0) {
				TecApplication.currentUserId = userid;
			} else {
				userid = TecApplication.currentUserId;
			}
			String imagePath = intent.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
			if (!TextUtils.isEmpty(imagePath) && MyFileUtil.isFileExist(imagePath)) {
				// Log.i(TAG, "----------------" + imagePath);
				sendImageMsg(imagePath);
			}
		}

		if (TecApplication.notifiFromUser == userid) {// 发现消息栏有通知
			TecApplication.notifiFromUser = 0;
			notificationManager.cancel(0x0a);// 点击一下隐藏通知
		}
		if (userid == GlobalContant.USER_ID_HELPER || userid == GlobalContant.USER_ID_SYSTEM) {
			mMsgSendContainer.setVisibility(View.GONE);
		} else {
			mMsgSendContainer.setVisibility(View.VISIBLE);
		}
		// Log.i(TAG, userid + "");

		showChatInfoList();

		View copyTextPopView = View.inflate(this, R.layout.copy_text_chat_pop, null);
		copyTextPopView.findViewById(R.id.copy_text_btn_chat).setOnClickListener(this);
		copyTextPopView.findViewById(R.id.delete_text_btn_chat).setOnClickListener(this);
		copyTextPop = new PopupWindow(copyTextPopView, // coyp_btn.getWidth(),
														// coyp_btn.getHeight());
				DisplayUtils.dip2px(this, 101), DisplayUtils.dip2px(this, 46));

		View vodPopView = View.inflate(this, R.layout.vod_chat_pop, null);
		vodPopView.findViewById(R.id.delete_vod_btn_chat).setOnClickListener(this);
		vodPop = new PopupWindow(vodPopView, // coyp_btn.getWidth(),
												// coyp_btn.getHeight());
				DisplayUtils.dip2px(this, 60), DisplayUtils.dip2px(this, 46));

		View savePicPopView = View.inflate(this, R.layout.save_pic_chat_pop, null);
		savePicPopView.findViewById(R.id.save_pic_btn_chat).setOnClickListener(this);
		savePicPopView.findViewById(R.id.delete_pic_btn_chat).setOnClickListener(this);
		savePicPop = new PopupWindow(savePicPopView, // coyp_btn.getWidth(),
														// coyp_btn.getHeight());
				DisplayUtils.dip2px(this, 144), DisplayUtils.dip2px(this, 46));

		view.findViewById(R.id.back_layout).setOnClickListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
		GlobalVariable.mChatMsgViewActivity = this;
		MobclickAgent.onEventBegin(this, "OpenChat");
		// Log.i(TAG, "---onResume---");
		TecApplication.isInChatMsgView = true;
		if (mController == null) {
			mController = new ChatMsgController(null, this);
		}

		if (TecApplication.notifiFromUser == userid) {// 发现消息栏有通知
			notificationManager.cancel(0x0a);// 隐藏通知
			TecApplication.notifiFromUser = 0;
			pageIndex = 0;
			showChatInfoList();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		TecApplication.isInChatMsgView = false;
		MobclickAgent.onEventEnd(this, "OpenChat");
	}

	@Override
	public void onBackPressed() {
		leave();
	}

	public void leave() {
		disMissPop();
		mIsVoiceMsg = false;
		isShowPhotoMenu = false;
		TecApplication.isInChatMsgView = false;
		if (GlobalVariable.mainActivity != null) {
			finish();
		} else {
			// WeLearnApi.reLogin();
			IntentManager.goToMainView(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			getApplication().unregisterReceiver(headsetPlugReceiver);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().contains("Receiver not registered")) {
				// Ignore this exception. This is exactly what is desired
			} else {
				// unexpected, re-throw
				throw e;
			}
		}
		if (isFromNoti && (GlobalVariable.mChatMsgViewActivity != this)) {
			TecApplication.isInChatMsgView = true;
		} else {
			TecApplication.isInChatMsgView = false;
			if (mController != null) {
				mController.removeMsgInQueue();
			}
			mHandler.removeMessages(555);
			WelearnHandler.getInstance().removeMessage(MsgConstant.MSG_DEF_CHAT_LIST);
			WelearnHandler.getInstance().removeMessage(MsgConstant.MSG_DEF_MSG_RESULT);
		}
		
		GlobalVariable.mChatMsgViewActivity=null;

	}

	/**
	 * 复制文字
	 * 
	 * @param rootView
	 */
	public void showPop(TextView rootView, int position) {
		disMissPop();
		mCopyView = rootView;
		deletePosition = position;
		isShowPoP = true;
		copyTextPop.showAsDropDown(rootView, DisplayUtils.dip2px(this, -10), DisplayUtils.dip2px(this, -94));
		// pop.isOutsideTouchable();
		copyTextPop.setOutsideTouchable(true);
	}

	/**
	 * 长按语音
	 * 
	 * @param rootView
	 */
	public void showVodPop(View rootView, int position) {
		disMissPop();
		deletePosition = position;
		isShowPoP = true;
		vodPop.showAsDropDown(rootView, DisplayUtils.dip2px(this, -10), DisplayUtils.dip2px(this, -94));
		vodPop.setOutsideTouchable(true);
	}

	public void showSavePicPop(View rootView, Bitmap saveBm, String path, int position) {
		disMissPop();
		deletePosition = position;
		// mSaveView = rootView;
		mSaveBm = saveBm;
		mSavePath = path;
		isShowPoP = true;
		savePicPop.showAsDropDown(rootView, DisplayUtils.dip2px(this, -10), DisplayUtils.dip2px(this, -100));
		// pop.isOutsideTouchable();
		savePicPop.setOutsideTouchable(true);
	}

	/**
	 * 隐藏pop
	 */
	public void disMissPop() {
		if (copyTextPop != null) {
			copyTextPop.dismiss();
		}
		if (vodPop != null) {
			vodPop.dismiss();
		}
		if (savePicPop != null) {
			savePicPop.dismiss();
		}
		isShowPoP = false;
	}

	/**
	 * 发送后更新ui(显示该聊天发送消息)
	 * 
	 * @author qinhaowen
	 * @param recodrTime
	 * @param path
	 * @param msgcontent
	 * @param contentType
	 * @return
	 */
	private double refreshUI(int recodrTime, String path, String msgcontent, int contentType) {
		UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null != uInfo) {
			ChatInfo chatInfo = getChatInfo(uInfo.getAvatar_100(), uInfo.getName(), msgcontent,
					GlobalContant.MSG_TYPE_SEND, userid, contentType);
			double timestamp = (double) chatInfo.getLocalTimestamp() / 1000;
			if (!TextUtils.isEmpty(path)) {
				if (!MyFileUtil.isFileExist(path)) {
					MyFileUtil.decodeFileByBase64(msgcontent, path);
				}
				chatInfo.setPath(path);
				if (recodrTime > 0) {
					chatInfo.setAudiotime(recodrTime);
				}
			}

			LogUtils.i(TAG, mChatInfoList.toString());

			mChatInfoList.add(chatInfo);
			mAdapter.setData(mChatInfoList);
			mChatList.setSelection(mChatList.getLastVisiblePosition());
			chatInfo.setReaded(true);

			WLDBHelper.getInstance().getWeLearnDB().insertMsg(chatInfo);

			Message msg = Message.obtain();
			msg.what = 555;
			msg.obj = timestamp;
			map.put(timestamp, chatInfo);

			int delay = 10000;
			if (contentType == GlobalContant.MSG_CONTENT_TYPE_IMG
					|| contentType == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
				delay = 15000;
			}
			mHandler.sendMessageDelayed(msg, delay);
			// Log.i(TAG, mChatInfoList.toString());
			return timestamp;
		} else {
			IntentManager.goToLogInView(this);

		}
		return 0;
	}

	private void showSendMsgTextView() {
		mTextVoiceChoice.setImageResource(R.drawable.bg_chat_voice_selector);
		mSendVoiceMsgBtn.setVisibility(View.GONE);
		mMsgSendText.setVisibility(View.VISIBLE);
	}

	private void showChatInfoList() {
		mChatInfoList = WLDBHelper.getInstance().getWeLearnDB().queryAllByUserid(userid, pageIndex++);
		Message msg = mHandler.obtainMessage();
		msg.obj = mChatInfoList;
		msg.what = 0x01;
		mHandler.sendMessage(msg);
	}

	// 设置聊天列表数据
	private void setChatInfoList(UserInfoModel user) {
		for (int i = 0; i < mChatInfoList.size(); i++) {
			if (mChatInfoList.get(i).getType() == GlobalContant.MSG_TYPE_RECV) {
				setUserData(mChatInfoList.get(i), user);
			}
		}
		// pageIndex = pageIndex * 20;
		Collections.reverse(mChatInfoList);
		mAdapter.setData(mChatInfoList);
		scrollChatListToBottom();
	}

	private ChatInfo setUserData(ChatInfo chat, UserInfoModel user) {
		chat.setUser(user);
		return chat;
	}

	// 设置单个聊天数据
	private void setChatData(ChatInfo chat, UserInfoModel user) {
		if (chat == null) {
			return;
		}
		chat = setUserData(chat, user);

		boolean insertMsg = WLDBHelper.getInstance().getWeLearnDB().insertMsg(chat);

		if (insertMsg) {
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("接收消息入库");
			}
			mChatInfoList.add(chat);
			mAdapter.setData(mChatInfoList);
		}
		scrollChatListToBottom();
	}

	private void scrollChatListToBottom() {
		mChatList.setSelection(mAdapter.getCount() - 1);
	}

	private void showSendVoiceMsgView() {
		mTextVoiceChoice.setImageResource(R.drawable.bg_chat_text_selector);
		mSendVoiceMsgBtn.setVisibility(View.VISIBLE);
		mMsgSendText.setVisibility(View.GONE);
		mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
	}

	private void hidePhotoContainer(boolean isHideKeyboard) {
		mChatPhotoContainer.setVisibility(View.GONE);
		disMissPop();
		isShowPhotoMenu = false;
		if (isHideKeyboard) {
			mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
		}
	}

	/**
	 * 发送语音聊天
	 * 
	 * @param recodrTime
	 */
	private void sendAudioMsg(float recodrTime) {
		String path = MyFileUtil.getVoiceFile().getAbsolutePath() + File.separator + audioName + ".amr";
		String msgcontent = MyFileUtil.encodeFileByBase64(path);
		if (!TextUtils.isEmpty(msgcontent)) {
			int audtioTime = Math.round(recodrTime);
			double timestamp = refreshUI(audtioTime, path, msgcontent, GlobalContant.MSG_CONTENT_TYPE_AUDIO);
			WeLearnApi.sendMsg(GlobalContant.MSG_CONTENT_TYPE_AUDIO, userid, msgcontent, audtioTime,
					MsgConstant.MSG_DEF_MSG_RESULT, timestamp);
		}
	}

	/**
	 * 发送文字聊天
	 */
	private void sendTextMsg() {
		String msgcontent = mMsgSendText.getText().toString();
		if (TextUtils.isEmpty(msgcontent.trim())) {
			ToastUtils.show(getString(R.string.text_toast_empty_msgcontent));
			return;
		}
		double timestamp = refreshUI(0, null, msgcontent, GlobalContant.MSG_CONTENT_TYPE_TEXT);
		WeLearnApi.sendMsg(GlobalContant.MSG_CONTENT_TYPE_TEXT, userid, msgcontent, 0, MsgConstant.MSG_DEF_MSG_RESULT,
				timestamp);
		mMsgSendText.setText("");
	}

	/**
	 * 压缩图片
	 * 
	 * @param path
	 * @return
	 */
	private String compressImage(String path) {
		FitBitmap fm = WeLearnImageUtil.getResizedImage(path, null, null, 600, false, 0);
		Bitmap bm = WeLearnImageUtil.getAutoOrientation(fm.getmBitmap(), path, null);
		path = MyFileUtil.getImgMsgFile().getAbsolutePath() + File.separator + UUID.randomUUID().toString();
		WeLearnImageUtil.saveFile(path, bm);
		return path;
	}

	/**
	 * 发送图片聊天
	 */
	public void sendImageMsg(String path) {
		path = compressImage(path);
		String msgcontent = MyFileUtil.encodeFileByBase64(path);
		// Log.i(TAG, "====" + path);

		// refreshUI(0, path, msgcontent, GlobalContant.MSG_CONTENT_TYPE_IMG);
		double timestamp = refreshUI(0, path, msgcontent, GlobalContant.MSG_CONTENT_TYPE_IMG);
		WeLearnApi.sendMsg(GlobalContant.MSG_CONTENT_TYPE_IMG, userid, msgcontent, 0, MsgConstant.MSG_DEF_MSG_RESULT,
				timestamp);
	}

	private ChatInfo getChatInfo(String avatar100, String name, String msgcontent, int type, int userid,
			int contentType) {
		ChatInfo chat = new ChatInfo();
		UserInfoModel user = new UserInfoModel();

		user.setAvatar_100(avatar100);
		user.setName(name);
		// chat.setSendFail(true);
		chat.setUser(user);
		chat.setMsgcontent(msgcontent);
		chat.setLocalTimestamp(System.currentTimeMillis());
		chat.setFromuser(userid);
		chat.setType(type);
		chat.setContenttype(contentType);
		return chat;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		disMissPop();
		int roleId = SharePerfenceUtil.getInstance().getUserRoleId();
		switch (v.getId()) {
		case R.id.back_layout:
			leave();
			// getActivity().finish();
			break;
		case R.id.chat_text_voice_choice:
			mIsVoiceMsg = !mIsVoiceMsg;
			if (mIsVoiceMsg) {
				showSendVoiceMsgView();
			} else {
				showSendMsgTextView();
			}
			hidePhotoContainer(true);
			break;
		case R.id.send_msg_btn:
			sendTextMsg();
			if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
				MobclickAgent.onEvent(this, "TecherChat");
			} else {// 学生发送消息
				MobclickAgent.onEvent(this, "StudentChat");
			}
			break;
		case R.id.msg_send_content:
			hidePhotoContainer(false);
			break;
		case R.id.send_img_msg:
			if (roleId == GlobalContant.ROLE_ID_COLLEAGE) {// 老师发送消息
				MobclickAgent.onEvent(this, "TecherChat");
			} else {// 学生发送消息
				MobclickAgent.onEvent(this, "StudentChat");
			}
			isShowPhotoMenu = !isShowPhotoMenu;
			if (isShowPhotoMenu) {
				mChatPhotoContainer.setVisibility(View.VISIBLE);
			} else {
				mChatPhotoContainer.setVisibility(View.GONE);
			}
			mImm.hideSoftInputFromWindow(mMsgSendText.getWindowToken(), 0);
			break;
		case R.id.chat_camera_btn:// 拍照
			IntentManager.startImageCapture(this, GlobalContant.SEND_IMG_MSG);
			break;
		case R.id.chat_album_btn:// 相册;
			Bundle data = new Bundle();
			data.putInt("tag", GlobalContant.SEND_IMG_MSG);
			data.putInt(USER_ID, userid);
			data.putString(USER_NAME, name);
			IntentManager.goToAlbumView(this, data);
			break;
		case R.id.copy_text_btn_chat:// 复制文字
			ClipboardManager c = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
			if (mCopyView != null) {
				CharSequence text = mCopyView.getText();
				c.setText(text);
				if (AppConfig.IS_DEBUG) {
					ToastUtils.show("成功复制:\"" + text + "\"到剪贴板");
				}
			}
			break;
		case R.id.save_pic_btn_chat:
			if (!MyFileUtil.sdCardIsAvailable()) {// 没有SD卡
				ToastUtils.show(this.getString(R.string.text_toast_sdcard_not_available));
				return;
			}
			if (!MyFileUtil.sdCardHasEnoughSpace()) {// SD卡空间不足
				ToastUtils.show(this.getString(R.string.text_toast_have_not_enough));
				return;
			}
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "welearnimg"
					+ File.separator;
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			mSavePath = mSavePath.replaceAll(File.separator + "chat" + File.separator + ".", "") + ".png";
			// new Md5FileNameGenerator().generate(imageUri);
			// path = path +5+".png";
			WeLearnImageUtil.saveFile(mSavePath, mSaveBm);
			if (AppConfig.IS_DEBUG) {
			}
			ToastUtils.show(getString(R.string.text_image_saved_path, path), 1);
			break;

		case R.id.delete_text_btn_chat:
		case R.id.delete_pic_btn_chat:
		case R.id.delete_vod_btn_chat:
			if (mChatInfoList.size() > deletePosition) {
				ChatInfo chat = mChatInfoList.remove(deletePosition);
				mAdapter.setData(mChatInfoList);
				WLDBHelper.getInstance().getWeLearnDB().deleteMsgInChatView(chat);
			}
			break;
		}
	}

	@Override
	public void onAfter(String jsonStr, int msgDef) {
		int code = JsonUtils.getInt(jsonStr, "code", -1);
		// String errMsg = JSONUtils.getString(jsonStr, "errmsg", "");
		if (msgDef == MsgConstant.MSG_DEF_CHAT_LIST) {// 接收聊天消息
			// Log.e("收到聊天消息", jsonStr);
			isQueryAll = false;

			ChatInfo chat = new Gson().fromJson(jsonStr, ChatInfo.class);
			chat.setType(GlobalContant.MSG_TYPE_RECV);
			String noticetype = chat.getNoticetype();
			boolean isReaded = false;
			if (!TextUtils.isEmpty(noticetype) && noticetype.length() == 3) {
				int isRed = Integer.parseInt(noticetype.charAt(0) + "");
				isReaded = isRed == 0;
			} else {
				isReaded = true;
			}
			chat.setReaded(isReaded);

			// 如果是接收的语音消息或者图片消息,则解码文件
			if (chat.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
				String audioPath = MyFileUtil.getVoiceFile().getAbsolutePath() + File.separator
						+ UUID.randomUUID().toString() + ".amr";
				// Log.i(TAG, "====" + audioPath);
				if (!MyFileUtil.isFileExist(audioPath)) {
					MyFileUtil.decodeFileByBase64(chat.getMsgcontent(), audioPath);
				}
				chat.setPath(audioPath);
			} else if (chat.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_IMG) {
				String imgPath = MyFileUtil.getImgMsgFile().getAbsolutePath() + File.separator
						+ UUID.randomUUID().toString() + ".png";
				if (!MyFileUtil.isFileExist(imgPath)) {
					MyFileUtil.decodeFileByBase64(chat.getMsgcontent(), imgPath);
				}
				chat.setPath(imgPath);
			} else if (chat.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP) {

			} else if (chat.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_TEXT) {
			}
			// 新开线程进行db存储
			int userid = chat.getFromuser();
			// 根据id查找次用户信息是否已经保存在本地了
			// 如果没有保存在本地,则调用接口去拉取,然后保存在本地

			UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(userid, true);

			if (user == null) {
				WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid, ChatMsgViewActivity.this);
				reChats.add(chat);
			} else {
				setChatData(chat, user);
			}
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("接收消息成功");
			}
		} else if (msgDef == MsgConstant.MSG_DEF_MSG_RESULT) {
			if (code == ResponseCmdDef.CODE_RETURN_OK) {
				// Log.e("收到聊天消息回包:", jsonStr);
				double timestamp = JsonUtils.getDouble(jsonStr, "timestamp", 0);
				// Log.e("判断两个时间戳:", (sendT == revT) +"");
				if (AppConfig.IS_DEBUG) {
					ToastUtils.show("接收到消息成功回包");
				}
				if (map.containsKey(timestamp)) {

					ChatInfo chatInfo = map.remove(timestamp);
					chatInfo.setSendFail(false);
					if (AppConfig.IS_DEBUG) {
						// chatInfo.setSendFail(true);
						ToastUtils.show("消息入库成功");
					}
					mAdapter.setData(mChatInfoList);
					WLDBHelper.getInstance().getWeLearnDB().update(chatInfo);
				}
			}
		}

	}

	/**
	 * 发送失败后重新发送
	 */
	public void resend(ChatInfo chatInfo) {
		// this.chatInfo = chatInfo;
		String msgContent = chatInfo.getMsgcontent();
		if (TextUtils.isEmpty(msgContent)) {
			if (!TextUtils.isEmpty(chatInfo.getPath()) && MyFileUtil.isFileExist(chatInfo.getPath())) {
				msgContent = MyFileUtil.encodeFileByBase64(chatInfo.getPath());
			}
		}
		if (mController == null) {
			mController = new ChatMsgController(null, this);
		}
		chatInfo.setSendFail(false);
		double timestamp = (double) chatInfo.getLocalTimestamp() / 1000;
		mAdapter.setData(mChatInfoList);
		Message msg = Message.obtain();
		msg.what = 555;
		msg.obj = timestamp;
		if (map.containsKey(timestamp)) {
			map.remove(timestamp);
		}
		map.put(timestamp, chatInfo);
		int delay = 10000;
		int contentType = chatInfo.getContenttype();
		if (contentType == GlobalContant.MSG_CONTENT_TYPE_IMG || contentType == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
			delay = 15000;
		}
		// Log.e("重发的时间戳:", timestamp+"");
		mHandler.sendMessageDelayed(msg, delay);
		WeLearnApi.sendMsg(contentType, userid, msgContent, chatInfo.getAudiotime(), MsgConstant.MSG_DEF_MSG_RESULT,
				timestamp);

	}

	@Override
	public void onPre() {

	}

	@Override
	public void onException() {

	}

	@Override
	public void onDisConnect() {
		Set<Double> keySet = map.keySet();

		Iterator<Double> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Double timestamp = iterator.next();
			ChatInfo chatInfo = map.get(timestamp);
			iterator.remove();
			if (chatInfo != null) {
				chatInfo.setSendFail(true);
				WLDBHelper.getInstance().getWeLearnDB().update(chatInfo);
			}
		}
		mAdapter.setData(mChatInfoList);

		mHandler.removeMessages(555);

	}

	public void onLoadFinish() {
		mChatList.stopRefresh();
	}

	@Override
	public void onRefresh() {
		List<ChatInfo> currentList = WLDBHelper.getInstance().getWeLearnDB().queryAllByUserid(userid, pageIndex++);
		position = currentList.size();
		if (position > 0) {
			// pageIndex = pageIndex * 20;
			LogUtils.i(TAG, "currentList---" + currentList.size());
			Collections.reverse(currentList);

			mChatInfoList.addAll(0, currentList);
			if (mChatInfoList != null && mChatInfoList.size() > 0) {
				UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(userid, true);
				if (user == null) {
					isQueryAll = true;
					WeLearnApi.getContactInfo(ChatMsgViewActivity.this, userid, ChatMsgViewActivity.this);
				} else {
					for (int i = 0; i < mChatInfoList.size(); i++) {
						if (mChatInfoList.get(i).getType() == GlobalContant.MSG_TYPE_RECV) {
							setUserData(mChatInfoList.get(i), user);
						}
					}
				}
			}
			// Log.i(TAG, "mChatInfoList---" + mChatInfoList.size());
			mChatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
			mAdapter.setData(mChatInfoList);
			mChatList.post(new Runnable() {

				@Override
				public void run() {
					mChatList.setSelection(position);
				}
			});
		}
		onLoadFinish();
	}

	@Override
	public void onLoadMore() {
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		float startEventY = 0.0f;
		float currentEventY = 0.0f;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			audioTime = System.currentTimeMillis();
			mSendVoiceMsgBtn.setBackgroundResource(R.drawable.bg_input_voice_btn_pressed);
			mSendVoiceMsgBtn.setText(getString(R.string.text_cancel_end));
			startEventY = event.getY();
			audioName = UUID.randomUUID().toString();
			MediaUtil.getInstance(true).record(audioName, mCallback, this);
			break;
		case MotionEvent.ACTION_MOVE:
			currentEventY = event.getY();
			float distance = currentEventY - startEventY;
			if (Math.abs(distance) > 200) {
				isCancel = true;
				mSendVoiceMsgBtn.setText(getString(R.string.text_cancel_send));
				MediaUtil.getInstance(true).setIsCancel(isCancel);
				WeLearnUiUtil.getInstance().showCancelSendDialog();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			boolean isTooShort = System.currentTimeMillis() - audioTime < 1000;
			if (isTooShort) {
				isCancel = true;
				MediaUtil.getInstance(true).setIsCancel(isCancel);
				// WeLearnUiUtil.getInstance().closeDialog();
				WeLearnUiUtil.getInstance().showWarnDialogWhenRecordVoice();
			} else {
			}
			mSendVoiceMsgBtn.setBackgroundResource(R.drawable.bg_input_voice_btn_normal);
			mSendVoiceMsgBtn.setText(getString(R.string.text_input_voice_btn));
			MediaUtil.getInstance(true).stopRecord(0, mCallback, true);
			break;
		}
		return false;
	}

	private void registerHeadsetPlugReceiver() {
		headsetPlugReceiver = new HeadsetPlugReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.HEADSET_PLUG");
		getApplication().registerReceiver(headsetPlugReceiver, intentFilter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_SEND_IMG) {
			if (resultCode == RESULT_OK) {

				String path = MyFileUtil.getImgMsgFile().getAbsolutePath() + File.separator + "publish.png";

				Bundle bund = new Bundle();
				bund.putString(PayAnswerQuestionDetailActivity.IMG_PATH, path);
				IntentManager.goToPreSendPicReViewActivity(this, bund, false);
			}
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			UserInfoModel uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);

			if (null == uInfo) {
				return;
			}

			WLDBHelper.getInstance().getWeLearnDB().insertorUpdate(uInfo.getUserid(), uInfo.getRoleid(),
					uInfo.getName(), uInfo.getAvatar_100());

			title.setText(uInfo.getName());
			if (isQueryAll) {
				setChatInfoList(uInfo);
			} else {
				if (null != reChats && reChats.size() > 1) {
					ChatInfo ci = null;
					try {
						ci = reChats.remove(0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (null != ci) {
						setChatData(ci, uInfo);
					}
				}
			}
		} else {
			ToastUtils.show(errMsg);
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {
	}

}

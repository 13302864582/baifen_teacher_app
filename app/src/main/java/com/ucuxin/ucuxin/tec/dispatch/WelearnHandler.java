package com.ucuxin.ucuxin.tec.dispatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.homework.StudyFragment;
import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 处理服务器的响应
 * 
 * @author parsonswang
 * 
 */
public class WelearnHandler { 

	private UIMsgHandler mUIMsgHandler;

	private static final String TAG = WelearnHandler.class.getSimpleName();

	private static class WelearnHandlerHolder {
		private static final WelearnHandler INSANCE = new WelearnHandler();
	}

	private WelearnHandler() {
		this.mUIMsgHandler = new UIMsgHandler();
	}

	public UIMsgHandler getHandler() {
		return mUIMsgHandler;
	}

	public static WelearnHandler getInstance() {
		return WelearnHandlerHolder.INSANCE;
	}

	@SuppressWarnings("deprecation")
	private void showNotification(ChatInfo chat) {
		String notifiName = "有笔作业老师";
		String notifiContent = "有您的新消息";
		UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(chat.getFromuser(), true);
		if (user != null) {
			String name = user.getName();
			if (!TextUtils.isEmpty(name)) {
				notifiName = name;
			}
		}

		// 1.弹出通知
		final Context context = TecApplication.getContext();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent();
		Bundle data = new Bundle();
		data.putInt(ChatMsgViewActivity.USER_ID, chat.getFromuser());
		data.putBoolean("isFromNoti", true);
		// Log.i(TAG, "showNotification===" + chat.getFromuser());
		intent.putExtras(data);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(context, ChatMsgViewActivity.class);
		int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
		PendingIntent pendingIntent  = PendingIntent.getActivity(context, iUniqueId, intent, 0);

		Notification.Builder notificationBuilder = new Notification.Builder(context.getApplicationContext());
		//		notification.icon = R.drawable.ic_launcher;
		//设置图标
		notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
		// 设置通知来到的时间
		notificationBuilder.setWhen(System.currentTimeMillis());


		if (chat != null && chat.getMsgcontent() != null) {
			if (chat.getContenttype() == 1) {
//				notification.tickerText = chat.getMsgcontent();
				notificationBuilder.setTicker(chat.getMsgcontent());
				notifiContent = chat.getMsgcontent();




			} else if (chat.getContenttype() == 2) {
				//notification.tickerText = "收到一段语音哦,听不听？";
				notificationBuilder.setTicker("收到一段语音哦,听不听？");
				notifiContent = "[语音]";
			} else if (chat.getContenttype() == 3) {
				//notification.tickerText = "发现一张图正向您扑来……";
				notificationBuilder.setTicker("发现一张图正向您扑来……");
				notifiContent = "[图片]";
			} else {
				//notification.tickerText = chat.getMsgcontent();
				notificationBuilder.setTicker(chat.getMsgcontent());
				notifiContent = chat.getMsgcontent();
			}
		} else {
//			notification.tickerText = "";
			notificationBuilder.setTicker("");
		}

		boolean flag = true;
		boolean dayNotDis = SharePerfenceUtil.getInstance().getDayNotDis();
		boolean nightNotDis = SharePerfenceUtil.getInstance().getNightNotDis();
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (dayNotDis) {
			int week = c.get(Calendar.DAY_OF_WEEK);
			int minute = c.get(Calendar.MINUTE);
			if (week > 1 && week < 7) {
				if ((hour >= 8 && hour < 12) || (hour >= 14 && hour < 17)) {
					flag = false;
				}
				if (hour == 17) {
					if (minute <= 30) {
						flag = false;
					}
				}
			}
		}

		if (nightNotDis) {
			if ((hour >= 0 && hour < 6) || (hour == 23)) {
				flag = false;
			}
		}

		if (TecHomeWorkCheckDetailActivity.isChecking) {
			flag = false;
		}
		if (flag) {
			boolean msgNotifyVibrate = SharePerfenceUtil.getInstance().getMsgNotifyVibrate();
			boolean msgNotifyFlag = SharePerfenceUtil.getInstance().getMsgNotifyFlag();
			int vibrate = 1;
			int sound = 1;
			String noticetype = chat.getNoticetype();
			if (!TextUtils.isEmpty(noticetype) && noticetype.length() == 3) {
				vibrate = Integer.parseInt(noticetype.charAt(1) + "");
				sound = Integer.parseInt(noticetype.charAt(2) + "");
			}
			if (msgNotifyVibrate && msgNotifyFlag) {
				if (vibrate == 1 && sound == 1) {
					//notification.defaults = Notification.DEFAULT_ALL;
					notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
				} else if (vibrate == 1) {
					//notification.defaults = Notification.DEFAULT_VIBRATE;
					notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
				} else if (sound == 1) {
					//notification.defaults = Notification.DEFAULT_SOUND;
					notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
				}
			} else if (msgNotifyVibrate && vibrate == 1) {
				//notification.defaults = Notification.DEFAULT_VIBRATE;
				notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
			} else if (msgNotifyFlag && sound == 1) {
				//notification.defaults = Notification.DEFAULT_SOUND;
				notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
			}
		}
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationBuilder.setAutoCancel(true);
		//notification.setLatestEventInfo(context, notifiName, notifiContent, pIntent);
		//notificationBuilder.setOngoing(true);
		notificationBuilder.setContentTitle(notifiName);
		notificationBuilder.setContentText(notifiContent);
		notificationBuilder.setContentIntent(pendingIntent);
		Notification notification=notificationBuilder.build();
		notificationManager.notify(0x0a, notification);
		TecApplication.notifiFromUser = chat.getFromuser();
	}

	public void onCmdHandle(String respText) {
		int code = JsonUtils.getInt(respText, "code", -1);
		if (code == 5) {
			return;
		}
		// if(code > 0)
		int type = JsonUtils.getInt(respText, "type", 0);
		switch (type) {
		case 3:// 系统
			int subtype3 = JsonUtils.getInt(respText, "subtype", 1);
			switch (subtype3) {
			case 1:// 1分配sessionid 2 升级 3刷新数据
				// String sessionid = JSONUtils.getString(respText, "sessionid",
				// "");
				// WeLearnSpUtil.getInstance().setWelearnTokenId(sessionid);
				// WeLearnApi.reLogin();
				WeLearnApi.sendSessionToServer();
				break;
			}
			break;
		case 2:// 聊天、通知
			int subtype = JsonUtils.getInt(respText, "subtype", 0);
			if (subtype != 5) {
				WeLearnApi.talkMsgReceivedVerity(respText);
			}

			switch (subtype) {
			case 1:
			case 2:
				int fromUserid = JsonUtils.getInt(respText, "fromuser", 0);
				final ChatInfo chat = new Gson().fromJson(respText, ChatInfo.class);
				int mcontenttype4 = chat.getContenttype();
				String mstring = chat.getMsgcontent();
				if (chat.getContenttype() == 0) {
					return;
				}
				if (mcontenttype4 == 4) {// 通知
					if (chat.getData().getAction() == 11) {
						Message msg = mUIMsgHandler.obtainMessage();
						if (msg == null) {
							msg = new Message();
						}
						msg.what = MsgConstant.MSG_NOTICE_CODE;
						msg.obj = respText;
						mUIMsgHandler.sendMessage(msg);
						return;
					} else if (chat.getData().getAction() == 12) {//投诉红点提示
						Message msg = mUIMsgHandler.obtainMessage();
						if (msg == null) {
							msg = new Message();
						}
						msg.what = MsgConstant.MSG_TOUSU_CODE;
						msg.obj = respText;
						mUIMsgHandler.sendMessage(msg);
						return;
					}else if (chat.getData().getAction() == 13) {//追问
						Message msg = mUIMsgHandler.obtainMessage();
						if (msg == null) {
							msg = new Message();
						}
						msg.what = MsgConstant.MSG_ZHUIWEN_CODE;
						msg.obj = respText;
						mUIMsgHandler.sendMessage(msg);
						return;
					}

				}
				if ((mcontenttype4 == 1 | mcontenttype4 == 4) && "".equals(mstring)) {
					return;
				}
				if (TecApplication.isInChatMsgView && fromUserid == TecApplication.currentUserId) {// 在聊天窗口中并且发送人id是和当前聊天id相同并且不是群聊
					Message msg = mUIMsgHandler.obtainMessage();
					if (msg == null) {
						msg = new Message();
					}
					msg.what = MsgConstant.MSG_DEF_CHAT_LIST;
					msg.obj = respText;
					mUIMsgHandler.sendMessage(msg);
				} else {
					// 3.发送消息通知更新ui final ChatInfo
					chat.setType(GlobalContant.MSG_TYPE_RECV);
					setMsgPath(chat);

					boolean insertMsg = WLDBHelper.getInstance().getWeLearnDB().insertMsg(chat);

					if (insertMsg) {
						Message msg = mUIMsgHandler.obtainMessage();
						if (msg == null) {
							msg = new Message();
						}
						msg.what = MsgConstant.MSG_DEF_MSGS;
						msg.obj = respText;
						mUIMsgHandler.sendMessage(msg);
						int userId = SharePerfenceUtil.getInstance().getUserId();
						if (userId != 0) {
							showNotification(chat);
						}

					}

				}
				break;
			case 4:// 离线消息
					// Log.e("离线消息:", respText);
				Map<String, List<String>> resultMap = JsonUtils
						.convetJsonObjToMap(JsonUtils.getJSONObject(respText, "data", null));
				ChatInfo chat4 = null;
				// boolean insertMsg4 = false;
				if (resultMap != null) {
					Set<String> sets = resultMap.keySet();
					for (String key : sets) {
						List<String> jsons = resultMap.get(key);
						for (String jsonStr : jsons) {
							fromUserid = JsonUtils.getInt(jsonStr, "fromuser", 0);
							int contenttype4 = JsonUtils.getInt(jsonStr, "contenttype", 0);
							String string = JsonUtils.getString(jsonStr, "msgcontent", "");
							if (contenttype4 == 0) {
								return;
							}
							if (contenttype4 == 4 && "".equals(string)) {
								return;
							}
							if (TecApplication.isInChatMsgView && fromUserid == TecApplication.currentUserId) {// 在聊天窗口中并且发送人id是和当前聊天id相同并且不是群聊
								Message msg = mUIMsgHandler.obtainMessage();
								if (msg == null) {
									msg = new Message();
								}
								msg.what = MsgConstant.MSG_DEF_CHAT_LIST;
								msg.obj = jsonStr;
								mUIMsgHandler.sendMessage(msg);
							} else {
								chat4 = new Gson().fromJson(jsonStr, ChatInfo.class);
								chat4.setType(GlobalContant.MSG_TYPE_RECV);
								setMsgPath(chat4);

								/* insertMsg4 = */WLDBHelper.getInstance().getWeLearnDB().insertMsg(chat4);
							}

						}
					}
					Message msg = mUIMsgHandler.obtainMessage();
					if (msg == null) {
						msg = new Message();
					}
					msg.what = MsgConstant.MSG_DEF_MSGS;
					msg.obj = respText;
					mUIMsgHandler.sendMessage(msg);
					int userId = SharePerfenceUtil.getInstance().getUserId();
					if (chat4 != null && userId != 0) {
						showNotification(chat4);
					}
				}
				break;
			case 5:
				dispatchMessage(respText);
				break;
			case 7://刷新金币和信用
				String dataStr = JsonUtils.getString(respText, "data", "");
				int action = JsonUtils.getInt(dataStr, "action", -1);
				if (action == 0) {
					float gold = (float) JsonUtils.getDouble(dataStr, "gold", 0);
					float credit = (float) JsonUtils.getDouble(dataStr, "credit", 0);
					SharePerfenceUtil.getInstance().setUserCredit(credit);
					SharePerfenceUtil.getInstance().setUserGold(gold);

					UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
					if (null != uInfo) {
						uInfo.setCredit(credit);
						uInfo.setGold(gold);
						WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetUserInfo(uInfo);
					}
					
					Message msg = mUIMsgHandler.obtainMessage();
					if (msg == null) {
						msg = new Message();
					}
					msg.what = MsgConstant.MSG_REFRESH_GOLD_CODE;
					msg.obj = respText;
					mUIMsgHandler.sendMessage(msg);
				}
				if (action == 4) { //强制老师端退出登录。踢掉老师下线。
					ToastUtils.show("系统检测您答题不认真！");
				}
				break;
			case 9:
				dataStr = JsonUtils.getString(respText, "data", "");
				action = JsonUtils.getInt(dataStr, "action", -1);
				if (action == 0) {
					if (StudyFragment.mRemindViewGas != null) {
						StudyFragment.mRemindViewGas.setVisibility(View.VISIBLE);
					}
				}
				break;
			default:
				// something was wrong , add by milo 2014.11.08
				break;
			}
			break;
		case 1:// 功能
			dispatchMessage(respText);
			break;
		default:

			break;
		}
	}

	private void setMsgPath(ChatInfo chat) {
		if (chat.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
			String audioPath = MyFileUtil.getVoiceFile().getAbsolutePath() + File.separator
					+ UUID.randomUUID().toString() + ".amr";
			LogUtils.i(TAG, "====" + audioPath);
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
		}
	}

	private void dispatchMessage(String respText) {

		double timestamp = JsonUtils.getDouble(respText, "timestamp", 0L);
		// Log.e("", "");
		if (0 != timestamp) {
			Message msg = mUIMsgHandler.obtainMessage();
			if (msg == null) {
				msg = new Message();
			}
			msg.what = TecApplication.time2CmdMap.get(timestamp);
			msg.obj = respText;
			mUIMsgHandler.sendMessage(msg);
		} else {
			if (AppConfig.IS_DEBUG) {
				ToastUtils.show("时间戳为0");
			}
			Message msg = mUIMsgHandler.obtainMessage();
			if (msg == null) {
				msg = new Message();
			}
			msg.what = MsgConstant.MSG_DEF_SVR_ERROR;
			msg.obj = respText;
			mUIMsgHandler.sendMessage(msg);
		}
	}

	public void registDispatcher(ImMsgDispatch dispatcher, String key) {
		if (mUIMsgHandler == null) {
			return;
		}
		if (!mUIMsgHandler.isRegisterObserver(dispatcher, key)) {
			mUIMsgHandler.registerObserver(dispatcher, key);
		}
	}

	public void unRegistDispatcher(ImMsgDispatch dispatcher, String key) {
		if (mUIMsgHandler == null) {
			return;
		}
		if (mUIMsgHandler.isRegisterObserver(dispatcher, key)) {
			mUIMsgHandler.unRegisterObserver(dispatcher, key);
		}
	}

	public void removeMessage(int msgDef) {
		if (mUIMsgHandler == null) {
			return;
		}
		// Log.i(TAG, "---remove msg---");
		mUIMsgHandler.removeMessages(msgDef);
	}
}

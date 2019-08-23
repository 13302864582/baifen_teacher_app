package com.ucuxin.ucuxin.tec.function.communicate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.NetworkImageView;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.AnswerListItemView;
import com.ucuxin.ucuxin.tec.function.AuthActivity;
import com.ucuxin.ucuxin.tec.function.homework.StateOfHomeWork;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.MsgData;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerQuestionDetailActivity;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.FitBitmap;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.DateUtil;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.WeLearnImageUtil;
import com.ucuxin.ucuxin.tec.view.AbstractMsgChatItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * 接收消息的view
 * 
 * @author parsonswang
 * 
 */
public class ChatMsgRecvView extends AbstractMsgChatItemView implements OnClickListener {

	private TextView mSendTime;
	private NetworkImageView mAvatar;
	private TextView mMsgContent;
	private FrameLayout mRecvMsgContainer;
	private ImageView mRecvMsgPlay;
	private TextView mRecvMsgTime;
	private String mAudioPath;
	private ChatInfo mChatInfo;
	private ImageView mChatRecvImg;
	private FrameLayout mChatRecvImgContainer;
//	private Context mContext;
	private ChatMsgViewActivity mActivity;
	private int avatarSize;
	private ImageView mRedPointIv;
	private int fromuser;

	public ChatMsgRecvView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (ChatMsgViewActivity) context;
	}

	public ChatMsgRecvView(Context context) {
		super(context);
		this.mActivity = (ChatMsgViewActivity) context;
	}

	@SuppressLint("InflateParams")
	public void setupViews(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_recv_view, null);
		mSendTime = (TextView) view.findViewById(R.id.to_user_send_time);
		mMsgContent = (TextView) view.findViewById(R.id.recv_msg_content);
		mAvatar = (NetworkImageView) view.findViewById(R.id.to_user_avatar);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.msg_list_avatar_size);
		mRecvMsgContainer = (FrameLayout) view.findViewById(R.id.voice_recv_msg_container);
		mRedPointIv = (ImageView) view.findViewById(R.id.red_point_iv_chat_rev);
		mRecvMsgPlay = (ImageView) view.findViewById(R.id.ic_voice_recv_msg_play);
		mRecvMsgTime = (TextView) view.findViewById(R.id.recv_msg_audiotime);
		mChatRecvImg = (ImageView) view.findViewById(R.id.recv_msg_img);
		mChatRecvImgContainer = (FrameLayout) view.findViewById(R.id.recv_msg_img_container);

		mAvatar.setOnClickListener(this);
		mMsgContent.setOnClickListener(this);
		mRecvMsgContainer.setOnClickListener(this);
		mChatRecvImgContainer.setOnClickListener(this);

		addView(view);
	}

	public void showData(ChatInfo info, List<ChatInfo> chatinfos, final int position) {
		mChatInfo = info;
		mRedPointIv.setVisibility(View.GONE);
		long timestamp = DateUtil.convertTimestampToLong((float) info.getTimestamp() * 1000);
		long preMsgTimestamp = getPreMsgTimestamp(chatinfos, position - 1);
		boolean isAfter = DateUtil.isAfter(timestamp, preMsgTimestamp);
		if (isAfter) {
			mSendTime.setVisibility(View.VISIBLE);
		} else {
			mSendTime.setVisibility(View.GONE);
		}
		mSendTime.setText(DateUtil.getDisplayChatTime(new Date(timestamp)));
		UserInfoModel ug = info.getUser();
		int defaultOrErrorAvatarId = R.drawable.ic_default_avatar;
		fromuser = info.getFromuser();
		if (ug != null) {
			switch (fromuser) {
			case GlobalContant.USER_ID_SYSTEM:
				defaultOrErrorAvatarId = R.drawable.ic_system_avatar;
				break;
			case GlobalContant.USER_ID_HELPER:
				defaultOrErrorAvatarId = R.drawable.ic_solve_helper_avatar;
				break;
			}
			ImageLoader.getInstance().loadImage(ug.getAvatar_100(), mAvatar, defaultOrErrorAvatarId,
					defaultOrErrorAvatarId, avatarSize, avatarSize / 10);
		} else {
			switch (fromuser) {
			case GlobalContant.USER_ID_SYSTEM:
				defaultOrErrorAvatarId = R.drawable.ic_system_avatar;
				break;
			case GlobalContant.USER_ID_HELPER:
				defaultOrErrorAvatarId = R.drawable.ic_solve_helper_avatar;
				break;
			}
			ImageLoader.getInstance().loadImage(null, mAvatar, defaultOrErrorAvatarId, defaultOrErrorAvatarId,
					avatarSize, avatarSize / 10);
		}
		// 如果是文字消息或者系统消息
		if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_TEXT
				|| info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP
				|| info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP_URL) {
			if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP
					|| info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP_URL) {
				//mMsgContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bg_jump_msg, 0);
				mMsgContent.setBackgroundResource(R.drawable.chat_rev_homework_selector);
				boolean isReaded = info.isReaded();
				if (!isReaded) {
					mRedPointIv.setVisibility(View.VISIBLE);
				}
			} else {
				mMsgContent.setBackgroundResource(R.drawable.bg_msg_recv_selector);
				//mMsgContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}

			mMsgContent.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showPop(mMsgContent, position);
					}
					return true;
				}
			});

			mMsgContent.setVisibility(View.VISIBLE);
			mRecvMsgContainer.setVisibility(View.GONE);
			mRecvMsgTime.setVisibility(View.GONE);
			mChatRecvImgContainer.setVisibility(View.GONE);

			String msgcontent = info.getMsgcontent();
			String splitStr = "星级评定是";
			MsgData data = mChatInfo.getData();
			int action = 0 ; 
			if (data != null) {
				action =  data.getAction();
			}
			if (((action == 5) ||(action == 1 ))&&msgcontent.contains(splitStr) ) {
//				String[] split = msgcontent.split(splitStr);msgcontent
//				msgcontent.lastIndexOf(string);
				int index2 = msgcontent.lastIndexOf(splitStr) ;
				int indexStar = index2 + 6;
				SpannableStringBuilder builder = new SpannableStringBuilder(msgcontent);
				ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.star_sum_adopt_msg));
				builder.setSpan(redSpan, indexStar, indexStar + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				if (msgcontent.contains("评价是")) {
					int index1 = msgcontent.indexOf("评价是") ;
					int indexJudge = index1 + 4;
					builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.star_sum_adopt_msg)), indexJudge, index2 - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				}
				mMsgContent.setText(builder);
			} else if ((action == 1) && msgcontent.contains("思维")) {
				int indexStrat = msgcontent.indexOf("思维") ;
				int indexLast = msgcontent.lastIndexOf("星") ;
				SpannableStringBuilder builder = new SpannableStringBuilder(msgcontent);
				ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.star_sum_adopt_msg));
				builder.setSpan(redSpan, indexStrat, indexLast + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mMsgContent.setText(builder);
			} else {
				mMsgContent.setText(msgcontent);
			}
			
		} else if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
			mAudioPath = info.getPath();
			if (TextUtils.isEmpty(mAudioPath)) {
				return;
			}
			boolean isReaded = info.isReaded();
			if (!isReaded) {
				mRedPointIv.setVisibility(View.VISIBLE);
			}
			mMsgContent.setVisibility(View.GONE);
			mRecvMsgContainer.setVisibility(View.VISIBLE);
			mRecvMsgTime.setVisibility(View.VISIBLE);
			mChatRecvImgContainer.setVisibility(View.GONE);
			mRecvMsgContainer.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (GlobalVariable.mChatMsgViewActivity != null) {
						GlobalVariable.mChatMsgViewActivity.showVodPop(mRecvMsgContainer, position);
					}
					return true;
				}
			});
			int audiotime = info.getAudiotime();
			if (audiotime != 0) {
				mRecvMsgTime.setText(audiotime + "s");
				
				switch (audiotime) {
				case 1:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 60));
					break;
				case 2:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 70));
					break;
				case 3:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 80));
					break;
				case 4:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 90));
					break;
				case 5:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 100));
					break;
				case 6:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 110));
					break;
				case 7:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 120));
					break;
				case 8:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 130));
					break;
				case 9:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 140));
					break;
				case 10:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 150));
					break;
				case 11:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 160));
					break;
				case 12:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 170));
					break;
				case 13:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 180));
					break;
				case 14:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 190));
					break;
				default:
					mRecvMsgContainer.setMinimumWidth(DisplayUtils.dip2px(mActivity, 200));
					break;
				}
			}
		} else if (info.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_IMG) {
			final String path = info.getPath();
			mChatRecvImgContainer.setVisibility(View.VISIBLE);
			mMsgContent.setVisibility(View.GONE);
			mRecvMsgContainer.setVisibility(View.GONE);

			// mChatRecvImg.post(new Runnable() {
			// @Override
			// public void run() {
			if (!TextUtils.isEmpty(path)) {
				mMsgContent.setVisibility(View.GONE);
				mRecvMsgTime.setVisibility(View.GONE);
				mChatRecvImg.setVisibility(View.VISIBLE);
				FitBitmap fm = WeLearnImageUtil.getResizedImage(path, null, null, 600, false, 0);
				if (fm != null) {
					final Bitmap bitmap = fm.getmBitmap();
					mChatRecvImg.setImageBitmap(bitmap);
					mChatRecvImgContainer.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View arg0) {
							if (GlobalVariable.mChatMsgViewActivity != null) {
								GlobalVariable.mChatMsgViewActivity.showSavePicPop(mChatRecvImgContainer, bitmap, path,
										position);
							}
							return true;
						}
					});
				}
			}
			// }
			// });
		}
	}

	@Override
	public void onClick(View v) {
		if (GlobalVariable.mChatMsgViewActivity != null) {
			GlobalVariable.mChatMsgViewActivity.disMissPop();
		}
		int id = v.getId();
		switch (id) {
		case R.id.voice_recv_msg_container:
			mRedPointIv.setVisibility(View.GONE);
			mChatInfo.setReaded(true);
			WLDBHelper.getInstance().getWeLearnDB().updateIsReaded(mChatInfo);
			if (!TextUtils.isEmpty(mAudioPath)) {
				play(mRecvMsgPlay, mAudioPath, true);
			}
			break;
		case R.id.recv_msg_content:
			mRedPointIv.setVisibility(View.GONE);
			mChatInfo.setReaded(true);
			WLDBHelper.getInstance().getWeLearnDB().updateIsReaded(mChatInfo);
			if (mChatInfo.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP) {
				MsgData data = mChatInfo.getData();
				if (data == null) {
					return;
				}
				int action = data.getAction();
				Bundle bundleData = new Bundle();
				switch (action) {
				case 1:
					int questionId = data.getQuestion_id();
					bundleData.putLong(AnswerListItemView.EXTRA_QUESTION_ID, questionId);
					bundleData.putInt(AnswerListItemView.EXTRA_POSITION, 0);
					bundleData.putBoolean(AnswerListItemView.EXTRA_ISQPAD, true);
					IntentManager.goToAnswerDetail(mActivity, bundleData);
					break;
				case 2:
					int target_user_id = data.getUserid();
					int target_role_id = data.getRoleid();
					int userId = SharePerfenceUtil.getInstance().getUserId();
					if (target_role_id == 0 || target_user_id == 0) {
						break;
					}
					// Bundle data = new Bundle();
					bundleData.putInt("userid", target_user_id);
					bundleData.putInt("roleid", target_role_id);
					if (target_user_id == userId) {// 进入主页
						IntentManager.goToTeacherCenterView(mActivity,TeacherCenterActivityNew.class, bundleData);
					} else {// 进入他人个人资料
						if (target_role_id == GlobalContant.ROLE_ID_COLLEAGE) {// 老师个人资料
							IntentManager.goToTeacherInfoView(mActivity, bundleData);
						} else {// 学生个人资料
							IntentManager.goToStudentInfoView(mActivity, bundleData);
						}
					}
					break;
				case 3:
					bundleData.putString(AuthActivity.AUTH_URL, data.getUrl());
					IntentManager.gotoAuthView(mActivity, bundleData);
					break;
				case 4:

					break;
				case 5:
					int taskid = data.getTaskid();
					clickIntoHomeWork(taskid);
					break;
				case 6://有人追问了,点击进入单题
					clickIntoSingleHomeWork(data);
					break;
				case 7://课程有人追问了,点击已购学生列表
					clickIntoPurchaseStudent(data.getCourseid());
					break;

				default:
					break;
				}

			} else if (mChatInfo.getContenttype() == GlobalContant.MSG_CONTENT_TYPE_JUMP_URL) {
				MsgData msgdata = mChatInfo.getData();
				Bundle data = new Bundle();
				data.putString(AuthActivity.AUTH_URL, msgdata.getUrl());
				IntentManager.gotoAuthView(mActivity, data);
			}
			break;
		case R.id.recv_msg_img_container:
			Bundle data = new Bundle();
			data.putString(PayAnswerQuestionDetailActivity.IMG_PATH, mChatInfo.getPath());
			data.putBoolean("doNotRoate", true);
			IntentManager.goToQuestionDetailPicView(mActivity, data);
			break;
		case R.id.to_user_avatar:
			if (mChatInfo.getFromuser() == GlobalContant.USER_ID_SYSTEM
					|| mChatInfo.getFromuser() == GlobalContant.USER_ID_HELPER) {
				return;
			}
			int userid = mChatInfo.getFromuser();
			int roleid = mChatInfo.getFromroleid();
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userid);
			bundle.putInt("roleid", roleid);
			bundle.putBoolean("isFromChat", true);
			if (roleid == GlobalContant.ROLE_ID_STUDENT|roleid == GlobalContant.ROLE_ID_PARENTS) {
				MobclickAgent.onEvent(mActivity, "studentInfoView");
				IntentManager.goToStudentInfoView(mActivity, bundle);
			} else if (roleid == GlobalContant.ROLE_ID_COLLEAGE) {
				MobclickAgent.onEvent(mActivity, "teacherInfoView");
				IntentManager.goToTeacherInfoView(mActivity, bundle);
			}
			break;
		}
	}
	
	private void clickIntoPurchaseStudent(int courseid) {
		IntentManager.goToPurchaseStudentActivity(mActivity, courseid);
	}

	private void clickIntoHomeWork(int taskid) {
		if ((fromuser == GlobalContant.USER_ID_HELPER )||(fromuser == GlobalContant.USER_ID_SYSTEM )) {
			mActivity.uMengEvent("homework_detailfromsysmsg_tec");
		}else {
			mActivity.uMengEvent("homework_detailfromchat_tec");
		}
		Bundle data = new Bundle();
		data.putInt("taskid", taskid);
		//IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
		clickIntoHomeWorkFromTaskid(taskid);		
	}

	private  void clickIntoSingleHomeWork(final MsgData msgdata){
		mActivity.showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", msgdata.getTaskid());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(mActivity, "homework","getone", data, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				mActivity.closeDialogHelp();
				if (code == 0) {
					int checkpointid = msgdata.getCheckpointid();
					HomeWorkCheckPointModel checkPointModel = new HomeWorkCheckPointModel();
					checkPointModel.setId(checkpointid);
					int isright = msgdata.getIsright();
					checkPointModel.setIsright(isright);
					String coordinate = msgdata.getCoordinate();
					if (coordinate != null) {
						checkPointModel.setCoordinate(coordinate);
					}
					String imgpath = msgdata.getImgpath();
					checkPointModel.setImgpath(imgpath);
					
					int state = JsonUtils.getInt(dataJson, "state", 0);
					int subjectid = JsonUtils.getInt(dataJson, "subjectid", -1);
					
					String teacheravatar = JsonUtils.getString(dataJson, "teacheravatar", "");
					String teachername = JsonUtils.getString(dataJson, "teachername", "");
					
					String wrongtype = msgdata.getWrongtype();
					String kpoint = msgdata.getKpoint();
					
					int teacherhomeworkcnt = JsonUtils.getInt(dataJson, "teacherhomeworkcnt", 0);
					int grabuserid = JsonUtils.getInt(dataJson, "grabuserid", 0);
					checkPointModel.setGrabuserid(grabuserid);
					checkPointModel.setTeacheravatar(teacheravatar);
					checkPointModel.setTeachername(teachername);
					checkPointModel.setTeacherhomeworkcnt(teacherhomeworkcnt);
					checkPointModel.setWrongtype(wrongtype == null ? "" : wrongtype);
					checkPointModel.setKpoint(kpoint== null ? "" : kpoint);
					
					Bundle data = new Bundle();
					data.putBoolean("fromMsg", true);
					int taskid = msgdata.getTaskid();
					data.putInt("taskid", taskid);
					data.putInt("state", state);
					data.putInt("subjectid", subjectid);
					
					data.putSerializable(HomeWorkCheckPointModel.TAG, checkPointModel);
					
					switch (state) {
					case StateOfHomeWork.ADOPTED:// 已采纳
					case StateOfHomeWork.ARBITRATED:// 仲裁完成
					case StateOfHomeWork.REFUSE:// 已拒绝
					case StateOfHomeWork.ARBITRATE:// 仲裁中
						IntentManager.goToStuSingleCheckActivity(mActivity, data, false);
						break;
					case StateOfHomeWork.ANSWERING:// 答题中
					case StateOfHomeWork.ANSWERED:// 已回答
					case StateOfHomeWork.APPENDASK:// 追问中
						IntentManager.goToTecSingleCheckActivity(mActivity, data, false);
//						data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
//						IntentManager.goToHomeWorkCheckDetailActivity(mActivity, data, false);
						break;
					case StateOfHomeWork.ASKING:
						ToastUtils.show("答案已删除");
						break;
					default:
						break;
					}

				}
				
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				mActivity.closeDialogHelp();
				
			}
			
		});
		
	}
	
	
	private void clickIntoHomeWorkFromTaskid(int taskid) {
		mActivity.showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", taskid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(mActivity, "homework","getone", data, new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				mActivity.closeDialogHelp();
				try {
					if (code == 0) {
						HomeWorkModel mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
						Bundle data = new Bundle();
						data.putInt("position", 0);
						data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
						// sky add
						SpUtil.getInstance().setCheckTag("checked_hw_tag");
						IntentManager.goToHomeWorkCheckDetailActivity(mActivity, data, false);						

					}else {
						ToastUtils.show(errMsg);
					}
				} catch (Exception e) {					
					e.printStackTrace();
					ToastUtils.show(e.getMessage());
				}

			}

			@Override
			public void onFail(int HttpCode,String errMsg) {
				mActivity.closeDialogHelp();

			}

		});

	}
}

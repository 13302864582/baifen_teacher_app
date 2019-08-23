package com.ucuxin.ucuxin.tec.constant;

public class GlobalContant {

	/**
	 * 定义角色
	 */
	public static final int ROLE_ID_STUDENT = 1;// 角色：学生
	
	public static final int ROLE_ID_COLLEAGE = 2;// 角色：老师
	
	public static final int ROLE_ID_PARENTS = 3;// 角色 家长
	
	public static final int ROLE_ID_ORG = 10;// 角色：辅导机构

	/**
	 * 定义性别
	 */
	public static final int SEX_TYPE_MAN = 1;// 性别：男

	public static final int SEX_TYPE_WOMEN = 2;// 性别：女


	/**
	 * 定义对错
	 */
	public static final int RIGHT_HOMEWORK = 1;// 表示正确

	public static final int WRONG_HOMEWORK = 0;// 表示错误


	/**
	 * 定义tasktype  问题还是作业
	 */
	public static final int TASKTYPE_QUESTION=1;//表示问题

	public  static final int TASKTYPE_HOMEWORK=2;//表示作业




	
	
	
	
	

	public static final String HANDLER_THREAD_NAME = "WEI_JIA_JIAO";

	public static final String SP_EDITOR_KEY = "session";

	public static final String SP_EDITOR_KEY_PATH = "path";

	public static final String SP_EDITOR_KEY_GOOD_SUBJECT = "good_subject";

	public static final String SP_EDITOR_USER_ID = "user_id";

	public static final String SP_EDITOR_ROLE_ID = "role_id";

	public static final String SP_EDITOR_GRADE = "grade";

	public static final String SP_EDITOR_NICK = "nick";

	public static final String SP_EDITOR_TOKEN = "token";

	public static final String SP_EDITOR_URL = "url";

	public static final String SP_EDITOR_FIRST = "first";

	public static final String SP_EDITOR_SEND_MSG_REQUEST = "send_msg_request";

	public static final String SP_EDITOR_GOLD = "gold";

	public static final String SP_EDITOR_SUBJECT = "subject";

	public static final String SP_EDITOR_NOTIFY_FLAG = "msg_notify_flag";

	public static final String SP_EDITOR_NOTIFY_VIBRATE = "msg_notify_vibrate";

	public static final int CAPTURE_IMAGE_REQUEST_CODE = 1000;

	public static final int CAPTURE_IMAGE_REQUEST_CODE_STUDENT = 1001;

	public static final int CAPTURE_IMAGE_REQUEST_CODE_SEND_IMG = 1002;

	public static final int APPEND_ASK_REQUEST_CODE = 1003;

	public static final int APPEND_ANSWER_REQUEST_CODE = 1004;

	public static final int CAPTURE_IMAGE_REQUEST_CODE_USER_LOGO = 1005;

	public static final int CAPTURE_IMAGE_REQUEST_CODE_USER_BG = 1006;
	public static final int REQUEST_CODE_GET_IMAGE_FROM_CROP = 1007;

	public static final int SVR_TYPE_FUNCTION = 1;
	/** 后台功能type=1 */

	public static final int SVR_TYPE_CHAT = 2;
	/** 聊天功能type=2 */

	public static final int SVR_SUBTYPE_USER = 2;
	/** 后台用户subtype=2 */

	public static final int SVR_TYPE_AUTH = 4;

	public static final int SVR_SUBTYPE_FEEDBACK = 0;
	/** 提交反馈 subtype=0 */

	public static final int SVR_SUBTYPE_QUESTION = 1;
	/** 后台问题subtype=1 */

	public static final int SVR_SUBTYPE_AUTH = 3;/** 后台问题subtype=1 */

	/** logout type=4 subtype=4 */
	public static final int SVR_SUBTYPE_LOGOUT = 4;

	public static final int QUERY_LIST_PAGE_SIZE = 10;

	public static final int ONE_QUES_MORE_ANS = 1;

	public static final int SAME_GRADE_QUES = 2;

	public static final int QUES_OF_MINE = 3;

	public static final int COLLECT_OF_MINE_QUES = 4;

	public static final int SCROLLING_UN_LOAD_IMG = 1000;

	public static final int UN_SCROLL_LOAD_IMG = 2000;

	public static final int ANSWER_AUDIO = 2;

	public static final int ANSWER_TEXT = 1;

	public static final int MSG_TYPE_SEND = 0;

	public static final int MSG_TYPE_RECV = 1;

	/** 纯文字 */
	public static final int MSG_CONTENT_TYPE_TEXT = 1;

	/** 声音 */
	public static final int MSG_CONTENT_TYPE_AUDIO = 2;

	/** 图片 */
	public static final int MSG_CONTENT_TYPE_IMG = 3;

	/** 文字+链接 */
	public static final int MSG_CONTENT_TYPE_JUMP = 4;

	public static final int MSG_CONTENT_TYPE_JUMP_URL = 5;

	public static final int RESULT_OK = 1;

	/** 提问 */
	public static final int PAY_ANSWER_ASK = 0;

	/** 回答 */
	public static final int PAY_ASNWER = 1;

	/** 发送图片聊天 */
	public static final int SEND_IMG_MSG = 2;

	/** 个人主页设置头像 */
	public static final int CONTACT_SET_USER_IMG = 3;

	/** 个人主页设置背景 */
	public static final int CONTACT_SET_BG_IMG = 4;

	/** 同级问题 */
	public static final int GRADE_SAME_QUESTION = 0;

	/** 我的问题 */
	public static final int QUESTION_OF_MINE = 1;

	/** 一题多解 */
	public static final int ONE_QUS_MORE_ANS = 2;

	/** 我的收藏 */
	public static final int COLLECT_OF_MINE = 3;

	

	

	/** 系统消息用户id */
	public static final int USER_ID_SYSTEM = 10000;

	/** 解题助手用户id */
	public static final int USER_ID_HELPER = 10001;

	/** 陌生人 */
	public static final int UNATTENTION = 0;

	/** 双向好友 */
	public static final int ATTENTION = 1;

	/** 学生看老师是好友 */
	public static final int STUTOTEA = 3;

	/** 老师看学生是好友 */
	public static final int TEATOSTU = 4;

	public static final int REWARD = 2;

	public static final int TALKMSGVERITY = 6;

	public static final int CLOSEDIALOG = 245;
	
	public static final int MYASYNC = 215;
	public static final int TURN_RIGHT = 1;
	public static final int TURN_LEFT = 2;

	public static final String SOUND_ARRIVAL_PATH = "";

	public static final int ERROR_MSG = 247;

	public static final int EXCUTE_ASYNCTASK = 248;// 让主线程开启一个异步任务

	public static final int LOOPMSG = 249;// 循环

	public static final int DIALOG_IN_THREAD = 240;// 在子线程弹dialog
	public static final int ADAPTER_SETINFO_IN_THREAD = 239;// 在子线程刷新Adapter
}

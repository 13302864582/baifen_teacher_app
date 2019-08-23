package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.model.PhoneLoginModel;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;

public class SharePerfenceUtil {

	public static final String TAG = SharePerfenceUtil.class.getSimpleName();

	private SharedPreferences mSp;

	private static final String SP_NAME = "welearn_sp";

	private static final String SP_KEY_SHOW_FIRST_TIPS = "sp_key_show_first_tips";

	public static final String WELEARN_DEFAULT_TOKEN_ID = "fudaotuan";

	public static final String QQ_LOGIN_INFO_KEY = "qq_login_info_key";
	public static final String PHONE_LOGIN_INFO_KEY = "phone_login_info_key";

	public static final String LOGIN_TYPE_KEY = "login_type_key";
	public static final int LOGIN_TYPE_QQ = 1;
	public static final int LOGIN_TYPE_PHONE = 2;

	private static final String SP_KEY_FIRST_TIPS = "sp_key_first_tip";

	private SharePerfenceUtil() {
		mSp = TecApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	private static class WeLearnSpUtilHolder {
		private static final SharePerfenceUtil INSANCE = new SharePerfenceUtil();
	}

	public static SharePerfenceUtil getInstance() {
		return WeLearnSpUtilHolder.INSANCE;
	}

	public void setGradeGroupId(int gradeGroupId) {
		mSp.edit().putInt("gradeGroupId", gradeGroupId).commit();
	}

	public void setSubjectGroupId(int subjectGroupId) {
		mSp.edit().putInt("subjectGroupId", subjectGroupId).commit();
	}

	public void setChapterGroupId(int chapterGroupId) {
		mSp.edit().putInt("chapterGroupId", chapterGroupId).commit();
	}

	public void setKnowPointGroupId(int KnowPointGroupId) {
		mSp.edit().putInt("KnowPointGroupId", KnowPointGroupId).commit();
	}

	public int getKnowPointGroupId() {
		return mSp.getInt("KnowPointGroupId", -1);
	}

	public int getChapterGroupId() {
		return mSp.getInt("chapterGroupId", -1);
	}

	public int getSubjectGroupId() {
		return mSp.getInt("subjectGroupId", -1);
	}

	public int getGradeGroupId() {
		return mSp.getInt("gradeGroupId", -1);
	}

	public int getUserId() {
		UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if (null == uInfo) {
			return 0;
		} else {
			return uInfo.getUserid();
		}
	}

	public void setMaxAskGold(float maxGold) {
		mSp.edit().putFloat("maxAskGold", maxGold).commit();
	}

	public float getMaxAskGold() {
		return mSp.getFloat("maxAskGold", 2.0f);
	}

	public void setMinAskGold(float gold) {
		mSp.edit().putFloat("minAskGold", gold).commit();
	}

	public float getMinAskGold() {
		return mSp.getFloat("minAskGold", 0.5f);
	}

	public void setBaseAskGold(float gold) {
		mSp.edit().putFloat("baseAskGold", gold).commit();
	}

	public float getBaseAskGold() {
		return mSp.getFloat("baseAskGold", 1.2f);
	}

	public void setUserGold(float gold) {
		mSp.edit().putFloat("usergold", gold).commit();
	}

	public float getUserGold() {
		return mSp.getFloat("usergold", 0);
	}

	public void setUserCredit(float credit) {
		mSp.edit().putFloat("UserCredit", credit).commit();
	}

	public float getUserCredit() {
		return mSp.getFloat("UserCredit", 0);
	}

	public void setUserRoleId(int roleId) {
		mSp.edit().putInt(GlobalContant.SP_EDITOR_ROLE_ID, roleId).commit();
	}

	public int getUserRoleId() {
		return mSp.getInt(GlobalContant.SP_EDITOR_ROLE_ID, 0);
	}

	public void setInviteNum(int num) {
		mSp.edit().putInt("invitenum", num).commit();
	}

	public int getInviteNum() {
		return mSp.getInt("invitenum", 0);
	}

	public void setGrades(String grade) {
		if (!TextUtils.isEmpty(grade)) {
			mSp.edit().putString(GlobalContant.SP_EDITOR_GRADE, grade).commit();
		}
	}

	public String getGrades() {
		return mSp.getString(GlobalContant.SP_EDITOR_GRADE, "");
	}

	public void setSubject(String subject) {
		mSp.edit().putString(GlobalContant.SP_EDITOR_SUBJECT, subject).commit();
	}

	public String getSubject() {
		return mSp.getString(GlobalContant.SP_EDITOR_SUBJECT, "");
	}

	public void setNick(String nick) {
		if (!TextUtils.isEmpty(nick)) {
			mSp.edit().putString(GlobalContant.SP_EDITOR_NICK, nick).commit();
		}
	}

	public String getNick() {
		return mSp.getString(GlobalContant.SP_EDITOR_NICK, "");
	}

	public void setTokenId(String tokenid) {
		if (!TextUtils.isEmpty(tokenid)) {
			mSp.edit().putString(GlobalContant.SP_EDITOR_TOKEN, tokenid).commit();
		}
	}

	public String getTokenId() {
		return mSp.getString(GlobalContant.SP_EDITOR_TOKEN, "");
	}

	public void setGold(float goldVal) {
		mSp.edit().putFloat(GlobalContant.SP_EDITOR_GOLD, goldVal).commit();
	}

	public float getGold() {
		return mSp.getFloat(GlobalContant.SP_EDITOR_GOLD, 0.0f);
	}

	public void setRecordGold(float recordGold) {
		mSp.edit().putFloat("recordGold", recordGold).commit();
	}

	public float getRecordGold() {
		return mSp.getFloat("recordGold", 0.0f);
	}

	public void setAuthUrl(String url) {
		if (!TextUtils.isEmpty(url)) {
			mSp.edit().putString(GlobalContant.SP_EDITOR_URL, url).commit();
		}
	}

	public String getAuthUrl() {
		return mSp.getString(GlobalContant.SP_EDITOR_URL, "");
	}

	public void setFirstFlag() {
		mSp.edit().putBoolean(GlobalContant.SP_EDITOR_FIRST, false).commit();
	}

	public boolean getFirstFlag() {
		return mSp.getBoolean(GlobalContant.SP_EDITOR_FIRST, true);
	}

	public void setMsgNotifyFlag(boolean flag) {
		mSp.edit().putBoolean(GlobalContant.SP_EDITOR_NOTIFY_FLAG, flag).commit();
	}

	public boolean getMsgNotifyFlag() {
		return mSp.getBoolean(GlobalContant.SP_EDITOR_NOTIFY_FLAG, true);
	}

	public void setMsgNotifyVibrate(boolean vibrate) {
		mSp.edit().putBoolean(GlobalContant.SP_EDITOR_NOTIFY_VIBRATE, vibrate).commit();
	}

	public boolean getMsgNotifyVibrate() {
		return mSp.getBoolean(GlobalContant.SP_EDITOR_NOTIFY_VIBRATE, true);
	}

	public void setDayNotDis(boolean dayDoNotDis) {
		mSp.edit().putBoolean("dayDoNotDis", dayDoNotDis).commit();
	}

	public boolean getDayNotDis() {
		return mSp.getBoolean("dayDoNotDis", false);
	}

	public void setNightNotDis(boolean nightDoNotDis) {
		mSp.edit().putBoolean("nightDoNotDis", nightDoNotDis).commit();
	}

	public boolean getNightNotDis() {
		return mSp.getBoolean("nightDoNotDis", false);
	}

	public void setContactInfo(String contactInfo) {
		if (!TextUtils.isEmpty(contactInfo)) {
			mSp.edit().putString("contactInfo", contactInfo).commit();
		}
	}

	public String getContactInfo() {
		return mSp.getString("contactInfo", "");
	}

	public void setContactList(String contactList) {
		if (!TextUtils.isEmpty(contactList)) {
			mSp.edit().putString("contactList", contactList).commit();
		}
	}

	public String getContactList() {
		return mSp.getString("contactList", "");
	}

	public void setIsChoicGream(boolean isChoicGream) {
		mSp.edit().putBoolean("isChoicGream", isChoicGream).commit();
	}

	public boolean isChoicGream() {
		return mSp.getBoolean("isChoicGream", false);

	}

	public void setIsDownUnivList(boolean isDownUnivList) {
		mSp.edit().putBoolean("isDownUnivList", isDownUnivList).commit();
	}

	public boolean isDownUnivList() {
		return mSp.getBoolean("isDownUnivList", false);
	}

	public void setGreamSchool1(String greamSchool1) {
		if (!TextUtils.isEmpty(greamSchool1)) {
			mSp.edit().putString("greamSchool1", greamSchool1).commit();
		}
	}

	public String getGreamSchool1() {
		return mSp.getString("greamSchool1", "");
	}

	public void setGreamSchool2(String greamSchool2) {
		if (!TextUtils.isEmpty(greamSchool2)) {
			mSp.edit().putString("greamSchool2", greamSchool2).commit();
		}
	}

	public String getGreamSchool2() {
		return mSp.getString("greamSchool2", "");
	}

	public void setGreamSchool3(String greamSchool3) {
		if (!TextUtils.isEmpty(greamSchool3)) {
			mSp.edit().putString("greamSchool3", greamSchool3).commit();
		}
	}

	public String getGreamSchool3() {
		return mSp.getString("greamSchool3", "");
	}

	public void setGreamSchoolID1(int schoolId) {
		mSp.edit().putInt("greamSchoolID1", schoolId).commit();
	}

	public int getGreamSchoolID1() {
		return mSp.getInt("greamSchoolID1", 0);
	}

	public void setGreamSchoolID2(int schoolId) {
		mSp.edit().putInt("greamSchoolID2", schoolId).commit();
	}

	public int getGreamSchoolID2() {
		return mSp.getInt("greamSchoolID2", 0);
	}

	public void setGreamSchoolID3(int schoolId) {
		mSp.edit().putInt("greamSchoolID3", schoolId).commit();
	}

	public int getGreamSchoolID3() {
		return mSp.getInt("greamSchoolID3", 0);
	}

	public void setUpDateUnivListTime() {
		mSp.edit().putLong("upDateUnivListTime", System.currentTimeMillis()).commit();
	}

	public long getUpDateUnivListTime() {
		return mSp.getLong("upDateUnivListTime", 0);
	}

	public void setUpDatePayAskGoldTime() {
		mSp.edit().putLong("upDatePayAskGoldTime", System.currentTimeMillis()).commit();
	}

	public long getUpDatePayAskGoldTime() {
		return mSp.getLong("upDatePayAskGoldTime", 0);
	}

	public void setGradeId(int gradeId) {
		mSp.edit().putInt("gradeId", gradeId).commit();
	}

	public int getGradeId() {
		return mSp.getInt("gradeId", 0);
	}

	/*
	 * public void setOpenId(String openid) { mEditor = mSp.edit();
	 * mEditor.putString("openid", openid); mEditor.apply(); }
	 * 
	 * public String getOpenId() { return mSp.getString("openid", ""); }
	 */

	public void setQACount(String qacount) {
		mSp.edit().putString("qacount", qacount).commit();
	}

	public String getQACount() {
		return mSp.getString("qacount", "0");
	}

	public void setAccessToken(String access_token) {
		mSp.edit().putString("access_token", access_token).commit();
	}

	public String getAccessToken() {
		return mSp.getString("openid", "");
	}

	public void setIsNewUser(boolean isNewUser) {
		mSp.edit().putBoolean("isNewUser", isNewUser).commit();
	}

	public boolean IsNewUser() {
		return mSp.getBoolean("isNewUser", true);
	}

	public void setLoginType(String login_type) {
		mSp.edit().putString("login_type", login_type).commit();
	}

	public String getLoginType() {
		return mSp.getString("login_type", "");
	}

	public void setPhoneNum(String phone_num) {
		mSp.edit().putString("phone_num", phone_num).commit();
	}

	public String getPhoneNum() {
		return mSp.getString("phone_num", "");
	}

	public void setLatestVersion(int latest_version) {
		LogUtils.d(TAG, "set lastest version = " + latest_version);
		mSp.edit().putInt("latest_version", latest_version).commit();
	}

	public int getLatestVersion() {
		return mSp.getInt("latest_version", 0);
	}

	public void setUpdateTitle(String UpdateTitle) {
		mSp.edit().putString("UpdateTitle", UpdateTitle).commit();
	}

	public String getUpdateTitle() {
		return mSp.getString("UpdateTitle", "");
	}

	public void setUpdateContent(String UpdateContent) {
		mSp.edit().putString("UpdateContent", UpdateContent).commit();
	}

	public String getUpdateContent() {
		return mSp.getString("UpdateContent", "亲！有您最新的安装包哦，速度升级呀~");
	}

	public void setUpdateUrl(String UpdateUrl) {
		mSp.edit().putString("UpdateUrl", UpdateUrl).commit();
	}

	public String getUpdateUrl() {
		return mSp.getString("UpdateUrl", "");
	}

	public void setDescription(String description) {
		if (TextUtils.isEmpty(description)) {
			mSp.edit().putString("description", "").commit();
		} else {
			mSp.edit().putString("description", description).commit();
		}
	}

	public String getDescription() {
		return mSp.getString("description", "");
	}

	public void setIsTodaySignIn(int today_signed) {
		mSp.edit().putInt("today_signed", today_signed).commit();
	}

	public int isTodaySignIn() {// 0是未签
		return mSp.getInt("today_signed", 1);
	}

	public boolean isShowFirstUseTip() {
		return mSp.getBoolean(SP_KEY_SHOW_FIRST_TIPS, true);
	}

	public void setFirstUseFalse() {
		mSp.edit().putBoolean(SP_KEY_SHOW_FIRST_TIPS, false).commit();
	}

	public boolean isShowFirstSingleTips() {
		return mSp.getBoolean("isShowFirstSingleTips", true);
	}

	public void setFirstSingleFalse() {
		mSp.edit().putBoolean("isShowFirstSingleTips", false).commit();
	}

	public boolean isShowLoginGuide() {
		return mSp.getBoolean("isShowLoginGuide", true);
	}

	public void setShowLoginGuideFalse() {
		mSp.edit().putBoolean("isShowLoginGuide", false).commit();
	}

	public boolean isShowAskGuide() {
		return mSp.getBoolean("isShowAskGuide", true);
	}

	public void setShowAskGuideFalse() {
		mSp.edit().putBoolean("isShowAskGuide", false).commit();
	}

	public boolean isShowHomeworkGuide() {
		return mSp.getBoolean("isShowHomeworkGuide", true);
	}

	public void setShowHomeworkGuideFalse() {
		mSp.edit().putBoolean("isShowHomeworkGuide", false).commit();
	}

	public String getWelearnTokenId() {
		String tid = mSp.getString("welearn_token_id", WELEARN_DEFAULT_TOKEN_ID);
		if (TextUtils.isEmpty(tid)) {
			return WELEARN_DEFAULT_TOKEN_ID;
		}
		return tid;
	}

	public void setWelearnTokenId(String sessionId) {
		if (TextUtils.isEmpty(sessionId)) {
			sessionId = WELEARN_DEFAULT_TOKEN_ID;
		}
		mSp.edit().putString("welearn_token_id", sessionId).commit();
	}

	public long getWelcomeImageTime() {
		return mSp.getLong("welcome_time", 0L);
	}

	public void setWelcomeImageTime(long time) {
		mSp.edit().putLong("welcome_time", time).commit();
	}

	public void savePhoneLoginInfo(PhoneLoginModel model) {
		if (null != model) {
			mSp.edit().putString(PHONE_LOGIN_INFO_KEY, model.toString()).commit();
			setGoLoginType(LOGIN_TYPE_PHONE);
		}
	}

	public PhoneLoginModel getPhoneLoginInfo() {
		String phoneLoginString = mSp.getString(PHONE_LOGIN_INFO_KEY, null);
		if (!TextUtils.isEmpty(phoneLoginString)) {
			try {
				return new Gson().fromJson(phoneLoginString, PhoneLoginModel.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void saveQQLoginInfo(String openId) {
		if (null != openId) {
			mSp.edit().putString(QQ_LOGIN_INFO_KEY, openId).commit();
			setGoLoginType(LOGIN_TYPE_QQ);
		}
	}

	public String getQQLoginInfo() {
		return mSp.getString(QQ_LOGIN_INFO_KEY, null);
	}

	public void setGoLoginType(int type) {
		mSp.edit().putInt(LOGIN_TYPE_KEY, type).commit();
	}

	public int getGoLoginType() {
		return mSp.getInt(LOGIN_TYPE_KEY, -1);
	}

	/**
	 * sky add
	 * 
	 * @return
	 */
	public boolean isFirstTip() {
		return mSp.getBoolean(SP_KEY_FIRST_TIPS, true);
	}

	/**
	 * sky add
	 * 
	 * @return
	 */
	public void setFirstTip() {
		mSp.edit().putBoolean(SP_KEY_FIRST_TIPS, false).commit();
	}

	/************************** 合并到此类 *************************************/
	public void putString(String key, String value) {
		if (key == null) {
			return;
		}
		mSp.edit().putString(key, value).commit();

	}

	public String getString(String key, String defValue) {
		return mSp.getString(key, defValue);
	}

	public void putInt(String key, int value) {
		mSp.edit().putInt(key, value).commit();
	}

	public int getInt(String key, int defValue) {
		return mSp.getInt(key, defValue);
	}

	public void putBoolean(String key, Boolean value) {
		mSp.edit().putBoolean(key, value).commit();
	}

	public boolean getBoolean(String key, Boolean defValue) {
		return mSp.getBoolean(key, defValue);
	}

	public Map<String, ?> getAll() {
		return mSp.getAll();
	}

	/**
	 * shareperfence保存数据
	 * 
	 * @author: sky
	 * @param map
	 *            void
	 */
	public void saveData(Map<String, Object> map) {
		for (Map.Entry<String, Object> item : map.entrySet()) {
			String key = item.getKey();
			Object value = item.getValue();

			if (value instanceof String) {
				mSp.edit().putString(key, String.valueOf(value));
			} else if (value instanceof Integer) {
				mSp.edit().putInt(key, (Integer) value);
			} else if (value instanceof Float) {
				mSp.edit().putFloat(key, (Float) value);
			} else if (value instanceof Long) {
				mSp.edit().putLong(key, (Long) value);
			} else if (value instanceof Boolean) {
				mSp.edit().putBoolean(key, (Boolean) value);
			}
			mSp.edit().commit();
		}
	}

	/**
	 * 取出数据
	 * 
	 * @author: sky
	 * @param key
	 * @param clazz
	 * @return Object
	 */
	public Object getData(String key, Class clazz) {
		Object obj = null;
		if (clazz.getName().equals(String.class.getName())) {
			obj = mSp.getString(key, "");
		} else if (clazz.getName().equals(Integer.class.getName())) {
			obj = mSp.getInt(key, 0);
		} else if (clazz.getName().equals(Float.class.getName())) {
			obj = mSp.getFloat(key, 0);
		} else if (clazz.getName().equals(Long.class.getName())) {
			obj = mSp.getLong(key, 0);
		} else if (clazz.getName().equals(Boolean.class.getName())) {
			obj = mSp.getBoolean(key, false);
		}
		return obj;

	}

}

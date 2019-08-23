package com.ucuxin.ucuxin.tec.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.ucuxin.ucuxin.tec.MainActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.GlobalVariable;
import com.ucuxin.ucuxin.tec.function.home.model.NoticeModel;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel;
import com.ucuxin.ucuxin.tec.function.homework.model.MsgData;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Chapter;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Point;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Subject;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.GradeModel;
import com.ucuxin.ucuxin.tec.model.PayAnswerGoldGson;
import com.ucuxin.ucuxin.tec.model.SubjectId;
import com.ucuxin.ucuxin.tec.model.SubjectModel;
import com.ucuxin.ucuxin.tec.model.UnivGson;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

public class WeLearnDB {

	public static final String TAG = WeLearnDB.class.getSimpleName();

	private SQLiteDatabase welearnDataBase;

	public WeLearnDB(SQLiteOpenHelper helper) {
		welearnDataBase = helper.getWritableDatabase();
	}

	// ----- Message Start -----
	public static class TableMessage implements BaseColumns {
		public static final String TABLE_NAME = "t_messagelist";
		/** 如果是接收消息,则是发送人id,如果为发送消息,则是接收人id */
		public static final String USERID = "userid";// "userid integer"
		public static final String CONTENTTYPE = "contenttype";// "contenttype
																// integer"
		public static final String FROMROLEID = "fromroleid";// "fromroleid
																// integer"
		public static final String QUESTIONID = "questionid";// "questionid
																// integer"
		public static final String ISSENDFAIL = "issendfail";// "issendfail
																// integer"
		public static final String ISREADED = "isreaded";// "isreaded integer"
		public static final String MSGCONTENT = "msgcontent";// "msgcontent
																// text"
		public static final String JUMPURL = "jumpurl";// "jumpurl text"
		public static final String AUDIOTIME = "audiotime";// "audiotime
															// integer,"
		public static final String TARGET_USER_ID = "target_user_id";// "target_user_id
																		// integer"
		public static final String TARGET_ROLE_ID = "target_role_id";// "target_role_id
																		// integer"
		public static final String ACTION = "action";// "action integer"
		public static final String CURRENTUSERID = "currentuserid";// "currentuserid
																	// integer"
		public static final String PATH = "path";// "path text"
		public static final String MSGTIME = "msgtime";// "msgtime text"
		public static final String TYPE = "type";// "type integer"

		public static final String TASKID = "taskid";// "type integer"
		public static final String CHECKPOINTID = "checkpointid";// "type
																	// integer"
		public static final String ISRIGHT = "isright";// "type integer"
		public static final String COORDINATE = "coordinate";// "type text"
		public static final String IMGPATH = "imgpath";// "type text"

		public static final String WRONGTYPE = "wrongtype";// "type text"

		public static final String KPOINT = "kpoint";// "type text"
		public static final String NOTICETYPE = "noticetype";// "type text"

		public static final String COURSEID = "courseid";// "type integer"
	}

	public static String getCreateMessageTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableMessage.TABLE_NAME).append(" (");
		sb.append(TableMessage._ID).append(" INTEGER PRIMARY KEY autoincrement,");
		sb.append(TableMessage.USERID).append(" INTEGER,");
		sb.append(TableMessage.CONTENTTYPE).append(" INTEGER,");
		sb.append(TableMessage.FROMROLEID).append(" INTEGER,");
		sb.append(TableMessage.QUESTIONID).append(" INTEGER,");
		sb.append(TableMessage.ISSENDFAIL).append(" INTEGER,");
		sb.append(TableMessage.ISREADED).append(" INTEGER,");
		sb.append(TableMessage.MSGCONTENT).append(" TEXT,");
		sb.append(TableMessage.JUMPURL).append(" TEXT,");
		sb.append(TableMessage.AUDIOTIME).append(" INTEGER,");
		sb.append(TableMessage.TARGET_USER_ID).append(" INTEGER,");
		sb.append(TableMessage.TARGET_ROLE_ID).append(" INTEGER,");
		sb.append(TableMessage.ACTION).append(" INTEGER,");
		sb.append(TableMessage.CURRENTUSERID).append(" INTEGER,");
		sb.append(TableMessage.PATH).append(" TEXT,");
		sb.append(TableMessage.MSGTIME).append(" TEXT,");
		sb.append(TableMessage.TYPE).append(" INTEGER,");

		sb.append(TableMessage.TASKID).append(" INTEGER,");
		sb.append(TableMessage.CHECKPOINTID).append(" INTEGER,");
		sb.append(TableMessage.ISRIGHT).append(" INTEGER,");
		sb.append(TableMessage.COORDINATE).append(" TEXT,");

		sb.append(TableMessage.WRONGTYPE).append(" TEXT,");

		sb.append(TableMessage.KPOINT).append(" TEXT,");
		sb.append(TableMessage.NOTICETYPE).append(" TEXT,");

		sb.append(TableMessage.COURSEID).append(" INTEGER,");

		sb.append(TableMessage.IMGPATH).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteMessageTableSql() {
		return "DROP TABLE IF EXISTS " + TableMessage.TABLE_NAME;
	}

	public static String getAlterMessageTableColumnTASKIDSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.TASKID + " INTEGER";
	}

	public static String getAlterMessageTableColumnCHECKPOINTIDSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.CHECKPOINTID + " INTEGER";
	}

	public static String getAlterMessageTableColumnISRIGHTSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.ISRIGHT + " INTEGER";
	}

	public static String getAlterMessageTableColumnCOORDINATESql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.COORDINATE + " TEXT";
	}

	public static String getAlterMessageTableColumnIMGPATHSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.IMGPATH + " TEXT";
	}

	public static String getAlterMessageTableColumnWRONGTYPESql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.WRONGTYPE + " TEXT";
	}

	public static String getAlterMessageTableColumnKPOINTSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.KPOINT + " TEXT";
	}

	public static String getAlterMessageTableColumnNOTICETYPESql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.NOTICETYPE + " TEXT";
	}

	/**
	 * 增加课程id
	 * 
	 * @return
	 */
	public static String getAlterMessageTableColumnCOURSEIDSql() {
		return "ALTER TABLE " + TableMessage.TABLE_NAME + " ADD COLUMN " + TableMessage.COURSEID + " INTEGER";// IF
																												// EXISTS
																												// "
																												// +
																												// TableMessage.TABLE_NAME;
	}

	public boolean insertMsg(ChatInfo info) {
		// welearnDataBase.beginTransaction();
		int type = info.getType();
		boolean isSend = type == GlobalContant.MSG_TYPE_SEND;
		boolean isSuccess = true;
		String time = "";
		if (!isSend) {
			String sql = "SELECT " + TableMessage.USERID + " FROM " + TableMessage.TABLE_NAME + " WHERE "
					+ TableMessage.MSGTIME + " = ?";
			time = msgTimeChangeString(info.getTimestamp());
			// Log.e("处理后的戳:", time);
			Cursor cursor = welearnDataBase.rawQuery(sql, new String[] { time });
			// execSQL(sql, new Double[]{msgtime});
			if (cursor != null && cursor.moveToFirst()) {
				isSuccess = false;
				cursor.close();
			}
		}
		if (isSuccess) {
			ContentValues values = new ContentValues();
			values.put(TableMessage.USERID, info.getFromuser());
			values.put(TableMessage.CURRENTUSERID, SharePerfenceUtil.getInstance().getUserId());
			int contenttype = info.getContenttype();
			if (contenttype == GlobalContant.MSG_CONTENT_TYPE_TEXT || contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP
					|| contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP_URL) {
				values.put(TableMessage.MSGCONTENT, info.getMsgcontent());
			}
			if (isSend) {
				values.put(TableMessage.MSGTIME, info.getLocalTimestamp());
			} else {
				values.put(TableMessage.MSGTIME, time);
			}
			values.put(TableMessage.ISSENDFAIL, 0);
			values.put(TableMessage.ISREADED, info.isReaded() ? 1 : 0);

			if (contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP) {
				MsgData data = info.getData();
				if (data != null) {
					values.put(TableMessage.ACTION, data.getAction());
					values.put(TableMessage.QUESTIONID, data.getQuestion_id());
					values.put(TableMessage.TARGET_USER_ID, data.getUserid());
					values.put(TableMessage.TARGET_ROLE_ID, data.getRoleid());
					String url = data.getUrl();
					if (url != null) {
						values.put(TableMessage.JUMPURL, url);
					}
					values.put(TableMessage.TASKID, data.getTaskid());
					values.put(TableMessage.CHECKPOINTID, data.getCheckpointid());
					values.put(TableMessage.ISRIGHT, data.getIsright());
					String coordinate = data.getCoordinate();
					if (coordinate != null) {
						values.put(TableMessage.COORDINATE, coordinate);
					}
					String imgpath = data.getImgpath();
					if (imgpath != null) {
						values.put(TableMessage.IMGPATH, imgpath);
					}
					String wrongtype = data.getWrongtype();
					if (wrongtype != null) {
						values.put(TableMessage.WRONGTYPE, wrongtype);
					}
					String kpoint = data.getKpoint();
					if (kpoint != null) {
						values.put(TableMessage.KPOINT, kpoint);
					}

					values.put(TableMessage.COURSEID, data.getCourseid());
				}
			}

			values.put(TableMessage.TYPE, info.getType());
			values.put(TableMessage.CONTENTTYPE, contenttype);
			values.put(TableMessage.FROMROLEID, info.getFromroleid());

			String noticetype = info.getNoticetype();
			if (noticetype != null) {
				values.put(TableMessage.NOTICETYPE, noticetype);
			}

			if (!TextUtils.isEmpty(info.getPath())) {
				values.put(TableMessage.PATH, info.getPath());
				values.put(TableMessage.AUDIOTIME, info.getAudiotime());
			}
			welearnDataBase.insert(TableMessage.TABLE_NAME, null, values);
		}

		// welearnDataBase.setTransactionSuccessful();
		// welearnDataBase.endTransaction();
		return isSuccess;
	}

	/**
	 * 更新发送状态
	 * 
	 * @param info
	 */
	public void update(ChatInfo info) {
		welearnDataBase.beginTransaction();
		String sql = "UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.ISSENDFAIL + " = ? WHERE "
				+ TableMessage.MSGTIME + " = ?";
		Integer sendFlag = info.isSendFail() ? 1 : 0;
		long timeL = info.getLocalTimestamp();
		String timeStr = "" + timeL;
		if (timeL == 0) {
			timeStr = msgTimeChangeString(info.getTimestamp());
		}
		welearnDataBase.execSQL(sql, new String[] { String.valueOf(sendFlag), timeStr });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	/**
	 * 更新是否已读
	 * 
	 * @param info
	 */
	public void updateIsReaded(ChatInfo info) {
		int _id = info.getId();
		welearnDataBase.beginTransaction();
		// 因为从数据库取出的收信,msgtime有误;而没进入数据库的消息则没有id.所以采用两者结合判断
		if (_id == 0) {
			long timeL = info.getLocalTimestamp();
			String timeStr = "" + timeL;
			if (timeL == 0) {
				timeStr = msgTimeChangeString(info.getTimestamp());
			}
			String sql = "UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.ISREADED + " = 1 WHERE "
					+ TableMessage.MSGTIME + " = ?";
			welearnDataBase.execSQL(sql, new String[] { timeStr });

		} else {
			String ID = _id + "";
			String sql = "UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.ISREADED + " = 1 WHERE "
					+ TableMessage._ID + " = ?";
			welearnDataBase.execSQL(sql, new String[] { ID });

		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public List<ChatInfo> queryMessageList() {
		String[] currentUserId = new String[] { String.valueOf(SharePerfenceUtil.getInstance().getUserId()) };
		List<ChatInfo> result = new ArrayList<ChatInfo>();
		String innerSql = "SELECT * FROM " + TableMessage.TABLE_NAME + " WHERE " + TableMessage.CURRENTUSERID
				+ "=? ORDER BY " + TableMessage._ID;

		String sql = "SELECT * , count(*) - sum(" + TableMessage.ISREADED + ") as " + TableMessage.ISREADED + " FROM ("
				+ innerSql + ") a GROUP BY a." + TableMessage.USERID + " ORDER BY a." + TableMessage._ID + " DESC ";
		Cursor cursor = welearnDataBase.rawQuery(sql, currentUserId);
		if (cursor != null) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ChatInfo info = new ChatInfo();
				String msgcontent = cursor.getString(cursor.getColumnIndex(TableMessage.MSGCONTENT));
				int contenttype = cursor.getInt(cursor.getColumnIndex(TableMessage.CONTENTTYPE));
				int userid = cursor.getInt(cursor.getColumnIndex(TableMessage.USERID));
				long msgtime = cursor.getLong(cursor.getColumnIndex(TableMessage.MSGTIME));
				String path = cursor.getString(cursor.getColumnIndex(TableMessage.PATH));
				int audiotime = cursor.getInt(cursor.getColumnIndex(TableMessage.AUDIOTIME));
				int type = cursor.getInt(cursor.getColumnIndex(TableMessage.TYPE));
				int questionid = cursor.getInt(cursor.getColumnIndex(TableMessage.QUESTIONID));
				int action = cursor.getInt(cursor.getColumnIndex(TableMessage.ACTION));
				int target_user_id = cursor.getInt(cursor.getColumnIndex(TableMessage.TARGET_USER_ID));
				int target_role_id = cursor.getInt(cursor.getColumnIndex(TableMessage.TARGET_ROLE_ID));
				int id = cursor.getInt(cursor.getColumnIndex(TableMessage._ID));
				String jumpurl = cursor.getString(cursor.getColumnIndex(TableMessage.JUMPURL));
				int fromroleid = cursor.getInt(cursor.getColumnIndex(TableMessage.FROMROLEID));
				int issendfail = cursor.getInt(cursor.getColumnIndex(TableMessage.ISSENDFAIL));

				int taskid = cursor.getInt(cursor.getColumnIndex(TableMessage.TASKID));
				int checkpointid = cursor.getInt(cursor.getColumnIndex(TableMessage.CHECKPOINTID));
				int isright = cursor.getInt(cursor.getColumnIndex(TableMessage.ISRIGHT));
				String imgpath = cursor.getString(cursor.getColumnIndex(TableMessage.IMGPATH));
				String coordinate = cursor.getString(cursor.getColumnIndex(TableMessage.COORDINATE));
				String wrongtype = cursor.getString(cursor.getColumnIndex(TableMessage.WRONGTYPE));
				String kpoint = cursor.getString(cursor.getColumnIndex(TableMessage.KPOINT));
				String noticetype = cursor.getString(cursor.getColumnIndex(TableMessage.NOTICETYPE));

				int courseid = cursor.getInt(cursor.getColumnIndex(TableMessage.COURSEID));

				boolean sendFlag = issendfail != 0;
				info.setSendFail(sendFlag);

				int isReaded = cursor.getInt(cursor.getColumnIndex(TableMessage.ISREADED));
				boolean unRead = isReaded > 0;// 在这里isReaded表示未读条数
				if (unRead) {
					MainActivity.isShowPoint = true;
				}
				info.setReaded(!unRead);
				info.setUnReadCount(isReaded);// 在这里isReaded表示未读条数

				info.setFromroleid(fromroleid);
				info.setId(id);
				info.setAudiotime(audiotime);
				info.setPath(path);
				info.setContenttype(contenttype);
				info.setFromuser(userid);
				info.setMsgcontent(msgcontent);
				info.setNoticetype(noticetype);
				if (contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP) {
					MsgData data = new MsgData();
					data.setAction(action);
					data.setQuestion_id(questionid);
					data.setUserid(target_user_id);
					data.setRoleid(target_role_id);
					data.setUrl(jumpurl);
					data.setTaskid(taskid);
					data.setCheckpointid(checkpointid);
					data.setIsright(isright);
					data.setCoordinate(coordinate);
					data.setImgpath(imgpath);
					data.setWrongtype(wrongtype);
					data.setKpoint(kpoint);
					data.setCourseid(courseid);
					info.setData(data);
				}

				if (type == GlobalContant.MSG_TYPE_RECV) {
					info.setTimestamp(msgtime);
				} else {
					info.setLocalTimestamp(msgtime);
				}
				info.setType(type);
				result.add(info);
			}
			cursor.close();
		}
		return result;
	}

	public List<ChatInfo> queryAllByUserid(int userid, int pageIndex) {
		ArrayList<ChatInfo> result = new ArrayList<ChatInfo>();
		String sql = "SELECT * FROM " + TableMessage.TABLE_NAME + " WHERE " + TableMessage.USERID + "=? AND "
				+ TableMessage.CURRENTUSERID + "=? ORDER BY " + TableMessage._ID + " DESC LIMIT ?, ? ";
		// LogUtils.i(TAG, sql);
		Cursor cursor = welearnDataBase.rawQuery(sql, new String[] { userid + "",
				SharePerfenceUtil.getInstance().getUserId() + "", pageIndex * 20 + "", 20 + "" });
		// LogUtils.i(TAG, userid + " " + pageIndex + " ");
		if (cursor != null) {
			// welearnDataBase.execSQL("UPDATE " + TableMessage.TABLE_NAME + "
			// SET " + TableMessage.ISREADED
			// + " = 1 WHERE " + TableMessage.USERID + " = " + userid + " AND "
			// + TableMessage.CONTENTTYPE +" <> " +
			// GlobalContant.MSG_CONTENT_TYPE_JUMP);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ChatInfo info = new ChatInfo();
				String msgcontent = cursor.getString(cursor.getColumnIndex(TableMessage.MSGCONTENT));
				int contenttype = cursor.getInt(cursor.getColumnIndex(TableMessage.CONTENTTYPE));
				long msgtime = cursor.getLong(cursor.getColumnIndex(TableMessage.MSGTIME));
				int type = cursor.getInt(cursor.getColumnIndex(TableMessage.TYPE));
				String path = cursor.getString(cursor.getColumnIndex(TableMessage.PATH));
				int audiotime = cursor.getInt(cursor.getColumnIndex(TableMessage.AUDIOTIME));
				int questionid = cursor.getInt(cursor.getColumnIndex(TableMessage.QUESTIONID));
				int action = cursor.getInt(cursor.getColumnIndex(TableMessage.ACTION));
				int target_user_id = cursor.getInt(cursor.getColumnIndex(TableMessage.TARGET_USER_ID));
				int target_role_id = cursor.getInt(cursor.getColumnIndex(TableMessage.TARGET_ROLE_ID));
				int id = cursor.getInt(cursor.getColumnIndex(TableMessage._ID));
				String jumpurl = cursor.getString(cursor.getColumnIndex(TableMessage.JUMPURL));
				int fromroleid = cursor.getInt(cursor.getColumnIndex(TableMessage.FROMROLEID));
				int issendfail = cursor.getInt(cursor.getColumnIndex(TableMessage.ISSENDFAIL));

				int taskid = cursor.getInt(cursor.getColumnIndex(TableMessage.TASKID));
				int checkpointid = cursor.getInt(cursor.getColumnIndex(TableMessage.CHECKPOINTID));
				int isright = cursor.getInt(cursor.getColumnIndex(TableMessage.ISRIGHT));
				String imgpath = cursor.getString(cursor.getColumnIndex(TableMessage.IMGPATH));
				String coordinate = cursor.getString(cursor.getColumnIndex(TableMessage.COORDINATE));
				String wrongtype = cursor.getString(cursor.getColumnIndex(TableMessage.WRONGTYPE));
				String kpoint = cursor.getString(cursor.getColumnIndex(TableMessage.KPOINT));
				int courseid = cursor.getInt(cursor.getColumnIndex(TableMessage.COURSEID));
				boolean sendFlag = issendfail != 0;
				info.setSendFail(sendFlag);

				if (contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP
						|| contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP_URL
						|| contenttype == GlobalContant.MSG_CONTENT_TYPE_AUDIO) {
					int isReaded = cursor.getInt(cursor.getColumnIndex(TableMessage.ISREADED));
					String noticetype = cursor.getString(cursor.getColumnIndex(TableMessage.NOTICETYPE));// 0代表不需要红点,
																											// 1代表要有红点
					int redpoint = 1;
					if (noticetype != null && noticetype.length() == 3) {
						redpoint = Integer.parseInt(noticetype.charAt(0) + "");
					}
					boolean readedFlag = isReaded != 0;
					if (redpoint == 0) {
						info.setReaded(true);
						welearnDataBase.execSQL("UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.ISREADED
								+ " = 1 WHERE " + TableMessage._ID + " = "
								+ cursor.getInt(cursor.getColumnIndex(TableMessage._ID)));
					} else if (redpoint == 1) {
						info.setReaded(readedFlag);
					}
				} else {
					welearnDataBase.execSQL("UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.ISREADED
							+ " = 1 WHERE " + TableMessage._ID + " = "
							+ cursor.getInt(cursor.getColumnIndex(TableMessage._ID)));
					info.setReaded(true);
				}

				info.setFromroleid(fromroleid);
				info.setId(id);
				info.setAudiotime(audiotime);
				info.setPath(path);
				info.setContenttype(contenttype);
				info.setFromuser(userid);
				info.setMsgcontent(msgcontent);

				if (contenttype == GlobalContant.MSG_CONTENT_TYPE_JUMP) {
					MsgData data = new MsgData();
					data.setAction(action);
					data.setQuestion_id(questionid);
					data.setUserid(target_user_id);
					data.setRoleid(target_role_id);
					data.setUrl(jumpurl);
					data.setTaskid(taskid);
					data.setCheckpointid(checkpointid);
					data.setIsright(isright);
					data.setCoordinate(coordinate);
					data.setImgpath(imgpath);
					data.setWrongtype(wrongtype);
					data.setKpoint(kpoint);
					data.setCourseid(courseid);
					info.setData(data);
				}

				if (type == GlobalContant.MSG_TYPE_RECV) {
					info.setTimestamp(msgtime);
				} else {
					info.setLocalTimestamp(msgtime);
				}
				info.setType(type);
				result.add(info);
			}
			cursor.close();
		}
		return result;
	}

	public void deleteMsgInChatView(ChatInfo chat) {
		int _id = chat.getId();
		welearnDataBase.beginTransaction();
		// 因为从数据库取出的收信,msgtime有误;而没进入数据库的消息则没有id.所以采用两者结合判断
		if (_id == 0) {
			long timeL = chat.getLocalTimestamp();
			String timeStr = "" + timeL;
			if (timeL == 0) {
				timeStr = msgTimeChangeString(chat.getTimestamp());
			}
			String sql = "delete from " + TableMessage.TABLE_NAME + " where " + TableMessage.MSGTIME + " =?";
			welearnDataBase.execSQL(sql, new String[] { timeStr });
		} else {
			String sql = "delete from " + TableMessage.TABLE_NAME + " where " + TableMessage._ID + " =?";
			String ID = _id + "";
			welearnDataBase.execSQL(sql, new String[] { ID });
		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public void deleteMsg(int userid) {
		welearnDataBase.beginTransaction();
		String sql = "DELETE FROM " + TableMessage.TABLE_NAME + " WHERE " + TableMessage.USERID + "=? AND "
				+ TableMessage.CURRENTUSERID + "=?";
		welearnDataBase.execSQL(sql, new Object[] { userid, SharePerfenceUtil.getInstance().getUserId() });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public void updateMsg(int userid) {
		welearnDataBase.beginTransaction();
		String sql = "UPDATE " + TableMessage.TABLE_NAME + " SET " + TableMessage.CURRENTUSERID + "=? WHERE "
				+ TableMessage.CURRENTUSERID + "=?";
		welearnDataBase.execSQL(sql, new Object[] { userid, 0 });
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	public String msgTimeChangeString(double msgtime) {
		String time = msgtime + "";
		// LogUtils.e("处理前的戳:", time);
		if (time.contains("e") || time.contains("E")) {
			int index = 0;
			if (time.contains("e")) {
				index = time.indexOf("e");
			} else {
				index = time.indexOf("E");
			}
			String x = time.substring(index + 1, index + 2);
			int xInt = Integer.parseInt(x) + 1;
			time = time.substring(0, index);
			char[] chars = time.toCharArray();
			time = "";
			for (int i = 0; i < chars.length; i++) {
				if (i == 11) {
					time = time + chars[1];
				}
				if (i != 1) {
					time = time + chars[i];
				}
			}
			int length = time.length();
			xInt = xInt - length;
			if (xInt > 0) {
				for (int i = 0; i < xInt; i++) {
					time = time + "0";
				}
			}
		}
		return time;
	}

	// ----- Message End -----

	// ----- Gold Start -----
	public static class TableGold implements BaseColumns {
		public static final String TABLE_NAME = "p_gold";
		public static final String MAXGOLD = "maxGold";// "maxGold float"
		public static final String MINGOLD = "minGold";// "minGold float"
		public static final String BASEGOLD = "baseGold";// "baseGold float"
		// "subject integer"科目ID 1英语 2数学 3物理 4化学 5生物
		public static final String SUBJECT = "subject";
		// "gradeid integer"年级ID 初123 高456
		public static final String GRADEID = "gradeid";
		public static final String GRADE = "grade";// "grade text"
	}

	public static String getCreateGoldTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableGold.TABLE_NAME).append(" (");
		sb.append(TableGold._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableGold.MAXGOLD).append(" FLOAT,");
		sb.append(TableGold.MINGOLD).append(" FLOAT,");
		sb.append(TableGold.BASEGOLD).append(" FLOAT,");
		sb.append(TableGold.SUBJECT).append(" INTEGER,");
		sb.append(TableGold.GRADEID).append(" INTEGER,");
		sb.append(TableGold.GRADE).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteGoldTableSql() {
		return "DROP TABLE IF EXISTS " + TableGold.TABLE_NAME;
	}

	public void insertGold(List<PayAnswerGoldGson> gsonlist) {
		try {
			welearnDataBase.beginTransaction();
			welearnDataBase.delete(TableGold.TABLE_NAME, null, null);
			// welearnDB.execSQL("DROP TABLE IF EXISTS p_gold");
			for (int i = 0; i < gsonlist.size(); i++) {
				PayAnswerGoldGson grades = gsonlist.get(i);
				for (int j = 0; j < grades.getSubjects().size(); j++) {
					SubjectId subject = grades.getSubjects().get(j);
					// Log.e("subject:", subject.toString());
					ContentValues values = new ContentValues();
					values.put(TableGold.GRADEID, i + 1);
					values.put(TableGold.SUBJECT, j + 1);
					values.put(TableGold.BASEGOLD, subject.getOriginal());
					values.put(TableGold.MAXGOLD, subject.getMax());
					values.put(TableGold.MINGOLD, subject.getMin());
					welearnDataBase.insert(TableGold.TABLE_NAME, null, values);
				}
			}
			SharePerfenceUtil.getInstance().setUpDatePayAskGoldTime();
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();
			// WeLearnSpUtil.getInstance().setIsDownUnivList(true);
		} catch (Exception e) {
			// WeLearnSpUtil.getInstance().setIsLogin(false);
			e.printStackTrace();
		} finally {
			GlobalVariable.doingGoldDB = false;
		}
	}

	public SubjectId queryGradeIdAndSubjectId(int gradeid, int subjectid) {

		String querySql = "SELECT * FROM " + TableGold.TABLE_NAME + " WHERE " + TableGold.GRADEID + " = ? AND "
				+ TableGold.SUBJECT + " = ?";
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(querySql, new String[] { gradeid + "", subjectid + "" });
		if (cursor != null && cursor.moveToFirst()) {
			SubjectId subject = new SubjectId();
			subject.setOriginal(cursor.getFloat(cursor.getColumnIndex(TableGold.BASEGOLD)));
			subject.setMax(cursor.getFloat(cursor.getColumnIndex(TableGold.MAXGOLD)));
			subject.setMin(cursor.getFloat(cursor.getColumnIndex(TableGold.MINGOLD)));
			// Log.e("DB subject:", subject.toString());
			cursor.close();
			return subject;
		}
		return null;
	}

	// ----- Gold End -----

	// ----- ReceiveUserInfo Start -----
	public static class TableReceiveUserInfo implements BaseColumns {
		public static final String TABLE_NAME = "t_recv_user";
		public static final String USERID = "userid";// "userid integer"
		public static final String ROLEID = "roleid";// "roleid integer"
		public static final String AVATAR_100 = "avatar_100";// "avatar_100
																// text"
		public static final String NAME = "name";// "name text"
	}

	public static String getCreateReceiveUserInfoTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableReceiveUserInfo.TABLE_NAME).append(" (");
		sb.append(TableReceiveUserInfo._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableReceiveUserInfo.USERID).append(" INTEGER,");
		sb.append(TableReceiveUserInfo.ROLEID).append(" INTEGER,");
		sb.append(TableReceiveUserInfo.AVATAR_100).append(" TEXT,");
		sb.append(TableReceiveUserInfo.NAME).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteReceiveUserInfoTableSql() {
		return "DROP TABLE IF EXISTS " + TableReceiveUserInfo.TABLE_NAME;
	}

	public void insertorUpdate(int userid, int roleid, String name, String logo_url) {
		UserInfoModel ru = queryByUserId(userid, false);
		welearnDataBase.beginTransaction();
		if (ru == null) {// 如果没有，则进行查询
			// LogUtils.i(TAG, "insert recv db");
			String sql = "INSERT INTO " + TableReceiveUserInfo.TABLE_NAME + "('" + TableReceiveUserInfo.USERID + "', '"
					+ TableReceiveUserInfo.ROLEID + "', '" + TableReceiveUserInfo.NAME + "', "
					+ TableReceiveUserInfo.AVATAR_100 + ") values (?, ?, ?, ?)";
			welearnDataBase.execSQL(sql, new Object[] { userid, roleid, name, logo_url });
		} else {// 否则进行更新
			// LogUtils.i(TAG, "update recv db");
			updateRecvUser(userid, roleid, name, logo_url);
		}
		welearnDataBase.setTransactionSuccessful();
		welearnDataBase.endTransaction();
	}

	// add by milo 2014.09.07
	private void updateRecvUser(int userid, int roleid, String name, String logo_url) {
		if (userid > 0) {
			ContentValues values = new ContentValues();
			values.put(TableReceiveUserInfo.AVATAR_100, logo_url);
			values.put(TableReceiveUserInfo.NAME, name);
			values.put(TableReceiveUserInfo.ROLEID, roleid);
			try {
				welearnDataBase.update(TableReceiveUserInfo.TABLE_NAME, values,
						TableReceiveUserInfo.USERID + "=" + userid, null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	public UserInfoModel queryByUserId(int userid, boolean isClose) {
		UserInfoModel result = null;
		String sql = "SELECT * FROM " + TableReceiveUserInfo.TABLE_NAME + " WHERE " + TableReceiveUserInfo.USERID
				+ "=?";
		Cursor cur = welearnDataBase.rawQuery(sql, new String[] { String.valueOf(userid) });
		if (cur != null) {
			if (cur.moveToNext()) {
				result = new UserInfoModel();
				String name = cur.getString(cur.getColumnIndex(TableReceiveUserInfo.NAME));
				String avatar_100 = cur.getString(cur.getColumnIndex(TableReceiveUserInfo.AVATAR_100));
				int roleid = cur.getInt(cur.getColumnIndex(TableReceiveUserInfo.ROLEID));

				result.setRoleid(roleid);
				result.setAvatar_100(avatar_100);
				result.setName(name);
				result.setUserid(userid);
				if (avatar_100 != null) {
					if (avatar_100.startsWith("/")) {
						result = null;
					}
				}
			}
			cur.close();
		}
		return result;
	}

	// ----- ReceiveUserInfo End -----

	// ----- UnivList Start -----
	public static class TableUnivList implements BaseColumns {
		public static final String TABLE_NAME = "univ_list";
		public static final String NAME = "name";// "name text"
	}

	public static String getCreateUnivListTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableUnivList.TABLE_NAME).append(" (");
		sb.append(TableUnivList._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableUnivList.NAME).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteUnivListTableSql() {
		return "DROP TABLE IF EXISTS " + TableUnivList.TABLE_NAME;
	}

	public void insertUniv(List<UnivGson> univList) {
		try {
			welearnDataBase.beginTransaction();
			welearnDataBase.delete(TableUnivList.TABLE_NAME, null, null);
			for (UnivGson univGson : univList) {
				ContentValues values = new ContentValues();
				values.put(TableUnivList._ID, univGson.getId());
				values.put(TableUnivList.NAME, univGson.getName());
				welearnDataBase.insert(TableUnivList.TABLE_NAME, null, values);
			}
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();

			SharePerfenceUtil.getInstance().setIsDownUnivList(true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public List<UnivGson> queryUnivListByName(String name) {
		name = "%" + name + "%";

		String innerSql = "SELECT * FROM " + TableUnivList.TABLE_NAME + " WHERE " + TableUnivList.NAME
				+ " LIKE ? ORDER BY " + TableUnivList._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name });
		if (cursor != null) {
			List<UnivGson> list = new ArrayList<UnivGson>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				UnivGson univ = new UnivGson();
				univ.setId(cursor.getInt(cursor.getColumnIndex(TableUnivList._ID)));
				univ.setName(cursor.getString(cursor.getColumnIndex(TableUnivList.NAME)));
				list.add(univ);
			}
			cursor.close();
			return list;
		}
		return null;
	}

	// ----- UnivList End -----

	// ----- Subjects Start -----
	public static class TableSubjectList implements BaseColumns {
		public static final String TABLE_NAME = "sub_list";
		public static final String SUB_ID = "sub_id";// "科目id int"
		public static final String NAME = "name";// "name text"
	}

	public static String getCreateSubjectsListTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableSubjectList.TABLE_NAME).append(" (");
		sb.append(TableSubjectList._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableSubjectList.SUB_ID).append(" INTEGER,");
		sb.append(TableSubjectList.NAME).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteSubjectsListTableSql() {
		return "DROP TABLE IF EXISTS " + TableSubjectList.TABLE_NAME;
	}

	/**
	 * 获取科目数量
	 * 
	 * @return
	 */
	public int getSubjectCount() {
		try {
			Cursor cur = welearnDataBase.query(TableSubjectList.TABLE_NAME, null, null, null, null, null, null);
			if (null != cur) {
				return cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 判断sub_list表中是否有语文
	 * 
	 * @author: sky
	 * @return int
	 */
	public int querySubjectWithYuwen() {
		try {
			// Cursor cur = welearnDataBase.execSQL("select name from sub_list
			// where name like '%语文%'");
			// Cursor cur = welearnDataBase.query(SubjectListTable.TABLE_NAME,
			// new String[] {
			// SubjectListTable.NAME
			// }, SubjectListTable.NAME + " like ?", new String[] {
			// "语文"
			// }, null, null, null);
			Cursor cur = welearnDataBase.rawQuery("select name from sub_list where name like ?",
					new String[] { "%语文%" });
			if (null != cur) {
				return cur.getCount();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public void insertSubject(SubjectModel subModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(TableSubjectList.SUB_ID, subModel.getId());
			values.put(TableSubjectList.NAME, subModel.getName());
			welearnDataBase.insert(TableSubjectList.TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public ArrayList<SubjectModel> querySubjectByIdList(ArrayList<Integer> subIdList) {

		if (null == subIdList || subIdList.size() == 0) {
			return null;
		}

		ArrayList<SubjectModel> list = new ArrayList<SubjectModel>();
		SubjectModel result = null;

		StringBuffer selection = new StringBuffer();
		for (int i = 0; i < subIdList.size(); i++) {
			if (i > 0) {
				selection.append(" OR ");
			} else {
				selection.append(" WHERE ");
			}
			selection.append(TableSubjectList.SUB_ID + " = " + subIdList.get(i));
		}

		String sql = "SELECT * FROM " + TableSubjectList.TABLE_NAME + selection.toString();
		// sql = "SELECT * FROM " + TableSubjectList.TABLE_NAME;

		Cursor cur = welearnDataBase.rawQuery(sql, null);

		if (cur != null) {
			while (cur.moveToNext()) {
				result = new SubjectModel();

				int sid = cur.getInt(cur.getColumnIndex(TableSubjectList.SUB_ID));
				String name = cur.getString(cur.getColumnIndex(TableSubjectList.NAME));

				result.setId(sid);
				result.setName(name);
				list.add(result);
			}
			cur.close();
		}
		return list;
	}

	/**
	 * 更新科目
	 * 
	 * @param subject
	 */
	public int updateSubject(SubjectModel subject) {
		ContentValues cv = new ContentValues();
		cv.put(TableSubjectList.NAME, subject.getName());
		return welearnDataBase.update(TableSubjectList.TABLE_NAME, cv, TableSubjectList.SUB_ID, new String[] {});
	}

	// ----- Subjects End -----

	// ----- Grade Start -----
	public static class TableGradeList implements BaseColumns {
		public static final String TABLE_NAME = "grade_list";
		/** id int 初一、初二等 */
		public static final String ID = "id";
		/** 年级id int 初中、高中、小学等 */
		public static final String GRADE_ID = "grade_id";
		public static final String NAME = "name";// "名字 text"
		public static final String SUBJECTS = "subjects";// "科目 text"
	}

	public static String getCreateGradeListTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableGradeList.TABLE_NAME).append(" (");
		sb.append(TableGradeList._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableGradeList.ID).append(" INTEGER,");
		sb.append(TableGradeList.GRADE_ID).append(" INTEGER,");
		sb.append(TableGradeList.NAME).append(" TEXT,");
		sb.append(TableGradeList.SUBJECTS).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteGradeTableSql() {
		return "DROP TABLE IF EXISTS " + TableGradeList.TABLE_NAME;
	}

	public void insertGrade(GradeModel gradeModel) {
		try {
			ContentValues values = new ContentValues();
			values.put(TableGradeList.ID, gradeModel.getId());
			values.put(TableGradeList.GRADE_ID, gradeModel.getGradeId());
			values.put(TableGradeList.NAME, gradeModel.getName());
			values.put(TableGradeList.SUBJECTS, gradeModel.getSubjects());
			welearnDataBase.insert(TableGradeList.TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public GradeModel queryGradeByGradeId(int gradeId) {
		GradeModel result = null;

		Cursor cur = welearnDataBase.query(TableGradeList.TABLE_NAME, null, TableGradeList.ID + "=?",
				new String[] { String.valueOf(gradeId) }, null, null, null);

		if (cur != null) {
			if (cur.moveToNext()) {
				result = new GradeModel();

				int sid = cur.getInt(cur.getColumnIndex(TableGradeList.ID));
				int gid = cur.getInt(cur.getColumnIndex(TableGradeList.GRADE_ID));
				String name = cur.getString(cur.getColumnIndex(TableGradeList.NAME));
				String subjects = cur.getString(cur.getColumnIndex(TableGradeList.SUBJECTS));

				result.setId(sid);
				result.setGradeId(gid);
				result.setName(name);
				result.setSubjects(subjects);
				return result;
			}
			cur.close();
		}

		return null;
	}

	/**
	 * 获取所有年级：小学、初中、高中
	 * 
	 * @param gid
	 * @return
	 */
	public ArrayList<GradeModel> queryAllGradeParent() {
		Cursor cur = welearnDataBase.query(TableGradeList.TABLE_NAME, null, TableGradeList.ID + "> 100", null, null,
				null, null);
		return toGradeList(cur);
	}

	/**
	 * 根据年级Id查询所有的子年级
	 * 
	 * @param gradeId
	 * @return
	 */
	public ArrayList<GradeModel> queryGradeByParentId(int gradeId) {
		Cursor cur = welearnDataBase.query(TableGradeList.TABLE_NAME, null, TableGradeList.GRADE_ID + "=?",
				new String[] { String.valueOf(gradeId) }, null, null, null);

		return toGradeList(cur);
	}

	private ArrayList<GradeModel> toGradeList(Cursor cur) {
		ArrayList<GradeModel> list = new ArrayList<GradeModel>();
		if (null == cur) {
			return list;
		}

		GradeModel result = null;
		while (cur.moveToNext()) {
			result = new GradeModel();

			int sid = cur.getInt(cur.getColumnIndex(TableGradeList.ID));
			int gid = cur.getInt(cur.getColumnIndex(TableGradeList.GRADE_ID));
			String name = cur.getString(cur.getColumnIndex(TableGradeList.NAME));
			String subjects = cur.getString(cur.getColumnIndex(TableGradeList.SUBJECTS));

			result.setId(sid);
			result.setGradeId(gid);
			result.setName(name);
			result.setSubjects(subjects);
			list.add(result);
		}
		cur.close();
		return list;
	}

	// ----- Grade End -----

	// ----- TableUserInfo1 Start -----
	public static class TableUserInfo1 implements BaseColumns {
		public static final String TABLE_NAME = "t_user_info";
		public static final String ADOPTCNT = "adoptcnt";// 0,采纳次数
		public static final String AGE = "age";// 0
		public static final String ALLOWGRAB = "allowgrab";// : 1,
		public static final String AMOUNTAMT = "amountamt";// : 0,消费累计金额
		public static final String ARBCNT = "arbcnt"; // : 0,仲裁次数
		public static final String AVATAR_100 = "avatar_100";
		public static final String AVATAR_40 = "avatar_40";
		public static final String CITY = "city";
		public static final String COUNTAMT = "countamt";// : 0,发任务次数
		public static final String CREDIT = "credit";// : 5,
		public static final String DREAMUNIV = "dreamuniv";// "",
		public static final String DREAMUNIVID = "dreamunivid";
		public static final String EARNGOLD = "earngold";// 0,老师收入金币
		public static final String EDULEVEL = "edulevel";// 0,老师教育水平
		public static final String EMAIL = "email";
		public static final String EXPENSESAMT = "expensesamt";// 消费累计金额,
		public static final String FROMCHAN = "fromchan";
		public static final String GOLD = "gold";
		public static final String GRADE = "grade";
		public static final String GRADEID = "gradeid";
		public static final String GROUPPHOTO = "groupphoto";// 背景图像
		public static final String HOMEWORKCNT = "homeworkcnt";// 0,发作业次数
		public static final String INFOSTATE = "infostate";// 0,标识用户信息是否完整
		public static final String NAME = "name";
		public static final String NAMEPINYIN = "namepinyin";
		public static final String PROVINCE = "province";
		public static final String QUICKCNT = "quickcnt";// 0,抢题次数
		public static final String ROLEID = "roleid";// 1,
		public static final String SCHOOLS = "schools";
		public static final String SCHOOLSID = "schoolsid";// 0
		public static final String SEX = "sex";
		public static final String STATE = "state";// 1,是否封号
		public static final String SUPERVIP = "supervip";
		public static final String TEACHLEVEL = "teachlevel";
		public static final String TEL = "tel";
		public static final String THIRDNAME = "thirdname";
		public static final String TOKENID = "tokenid";
		public static final String USERID = "userid";
		public static final String WELEARNID = "welearnid";// 0 所属客服id
		public static final String TEACHMAJOR = "teachmajor";// 老师擅长科目
		public static final String MAJOR = "major";// 专业
		public static final String NEWUSER = "newuser";// :0
		public static final String ORGNAME = "orgname";// :机构名字
		public static final String ORGID = "orgid";// :机构id

	}

	public static final String getCreateUserInfoTableSql1() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableUserInfo1.TABLE_NAME).append(" (");
		sb.append(TableUserInfo1._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableUserInfo1.ADOPTCNT).append(" INTEGER,");
		sb.append(TableUserInfo1.AGE).append(" INTEGER,");
		sb.append(TableUserInfo1.ALLOWGRAB).append(" INTEGER,");
		sb.append(TableUserInfo1.AMOUNTAMT).append(" FLOAT,");
		sb.append(TableUserInfo1.ARBCNT).append(" INTEGER,");
		sb.append(TableUserInfo1.AVATAR_40).append(" TEXT,");
		sb.append(TableUserInfo1.AVATAR_100).append(" TEXT,");
		sb.append(TableUserInfo1.CITY).append(" TEXT,");
		sb.append(TableUserInfo1.COUNTAMT).append(" INTEGER,");
		sb.append(TableUserInfo1.CREDIT).append(" FLOAT,");
		sb.append(TableUserInfo1.DREAMUNIV).append(" TEXT,");
		sb.append(TableUserInfo1.DREAMUNIVID).append(" TEXT,");
		sb.append(TableUserInfo1.EARNGOLD).append(" FLOAT,");
		sb.append(TableUserInfo1.EDULEVEL).append(" INTEGER,");
		sb.append(TableUserInfo1.EMAIL).append(" TEXT,");
		sb.append(TableUserInfo1.EXPENSESAMT).append(" FLOAT,");
		sb.append(TableUserInfo1.FROMCHAN).append(" TEXT,");
		sb.append(TableUserInfo1.GOLD).append(" FLOAT,");
		sb.append(TableUserInfo1.GRADE).append(" TEXT,");
		sb.append(TableUserInfo1.GRADEID).append(" INTEGER,");
		sb.append(TableUserInfo1.GROUPPHOTO).append(" TEXT,");
		sb.append(TableUserInfo1.HOMEWORKCNT).append(" INTEGER,");
		sb.append(TableUserInfo1.INFOSTATE).append(" INTEGER,");
		sb.append(TableUserInfo1.NAME).append(" TEXT,");
		sb.append(TableUserInfo1.NAMEPINYIN).append(" TEXT,");
		sb.append(TableUserInfo1.PROVINCE).append(" TEXT,");
		sb.append(TableUserInfo1.QUICKCNT).append(" INTEGER,");
		sb.append(TableUserInfo1.ROLEID).append(" INTEGER,");
		sb.append(TableUserInfo1.SCHOOLS).append(" TEXT,");
		sb.append(TableUserInfo1.SCHOOLSID).append(" INTEGER,");
		sb.append(TableUserInfo1.SEX).append(" INTEGER,");
		sb.append(TableUserInfo1.STATE).append(" INTEGER,");
		sb.append(TableUserInfo1.SUPERVIP).append(" INTEGER,");
		sb.append(TableUserInfo1.TEACHLEVEL).append(" INTEGER,");
		sb.append(TableUserInfo1.TEL).append(" TEXT,");
		sb.append(TableUserInfo1.THIRDNAME).append(" TEXT,");
		sb.append(TableUserInfo1.TOKENID).append(" TEXT,");
		sb.append(TableUserInfo1.USERID).append(" INTEGER,");
		sb.append(TableUserInfo1.WELEARNID).append(" INTEGER,");
		sb.append(TableUserInfo1.TEACHMAJOR).append(" TEXT,");

		sb.append(TableUserInfo1.ORGID).append(" INTEGER,");
		sb.append(TableUserInfo1.ORGNAME).append(" TEXT,");

		sb.append(TableUserInfo1.MAJOR).append(" TEXT,");
		sb.append(TableUserInfo1.NEWUSER).append(" INTEGER");
		sb.append(");");
		return sb.toString();
	}

	public static String getAlterUserInfoTableColumnORGIDSql() {
		return "ALTER TABLE " + TableUserInfo1.TABLE_NAME + " ADD COLUMN " + TableUserInfo1.ORGID + " INTEGER";
	}

	public static String getAlterUserInfoTableColumnORGNAMESql() {
		return "ALTER TABLE " + TableUserInfo1.TABLE_NAME + " ADD COLUMN " + TableUserInfo1.ORGNAME + " TEXT";
	}

	public UserInfoModel queryCurrentUserInfo() {
		Cursor c = welearnDataBase.query(TableUserInfo1.TABLE_NAME, null, null, null, null, null, null);
		if (null == c) {
			return null;
		} else {
			if (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(TableUserInfo1.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(TableUserInfo1.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(TableUserInfo1.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(TableUserInfo1.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(TableUserInfo1.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(TableUserInfo1.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(TableUserInfo1.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(TableUserInfo1.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(TableUserInfo1.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(TableUserInfo1.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(TableUserInfo1.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(TableUserInfo1.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(TableUserInfo1.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(TableUserInfo1.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(TableUserInfo1.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(TableUserInfo1.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(TableUserInfo1.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(TableUserInfo1.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(TableUserInfo1.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(TableUserInfo1.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(TableUserInfo1.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(TableUserInfo1.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(TableUserInfo1.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(TableUserInfo1.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(TableUserInfo1.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(TableUserInfo1.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(TableUserInfo1.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(TableUserInfo1.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(TableUserInfo1.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(TableUserInfo1.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(TableUserInfo1.SEX)));
				u.setState(c.getInt(c.getColumnIndex(TableUserInfo1.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(TableUserInfo1.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(TableUserInfo1.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(TableUserInfo1.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(TableUserInfo1.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(TableUserInfo1.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(TableUserInfo1.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(TableUserInfo1.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(TableUserInfo1.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(TableUserInfo1.MAJOR)));
				u.setNewuser(c.getInt(c.getColumnIndex(TableUserInfo1.NEWUSER)));
				u.setOrgname(c.getString(c.getColumnIndex(TableUserInfo1.ORGNAME)));
				u.setOrgid(c.getInt(c.getColumnIndex(TableUserInfo1.ORGID)));
				c.close();
				return u;
			} else {
				c.close();
			}
		}
		return null;
	}

	public boolean isUserExists(int uid) {
		Cursor c = welearnDataBase.query(TableUserInfo1.TABLE_NAME, null, TableUserInfo1.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return false;
		} else {
			int count = c.getCount();
			c.close();
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	public long insertOrUpdatetUserInfo(UserInfoModel uInfo) {
		if (null == uInfo) {
			return 0L;
		}

		boolean isUpdate = isUserExists(uInfo.getUserid());

		ContentValues cv = new ContentValues();
		cv.put(TableUserInfo1.ADOPTCNT, uInfo.getAdoptcnt());
		cv.put(TableUserInfo1.AGE, uInfo.getAge());
		cv.put(TableUserInfo1.ALLOWGRAB, uInfo.getAllowgrab());
		cv.put(TableUserInfo1.AMOUNTAMT, uInfo.getAmountamt());
		cv.put(TableUserInfo1.ARBCNT, uInfo.getArbcnt());
		cv.put(TableUserInfo1.AVATAR_100, uInfo.getAvatar_100());
		cv.put(TableUserInfo1.AVATAR_40, uInfo.getAvatar_40());
		cv.put(TableUserInfo1.CITY, uInfo.getCity());
		cv.put(TableUserInfo1.COUNTAMT, uInfo.getCountamt());
		cv.put(TableUserInfo1.CREDIT, uInfo.getCredit());
		cv.put(TableUserInfo1.DREAMUNIV, uInfo.getDreamuniv());
		cv.put(TableUserInfo1.DREAMUNIVID, uInfo.getDreamunivid());
		cv.put(TableUserInfo1.EARNGOLD, uInfo.getEarngold());
		cv.put(TableUserInfo1.EDULEVEL, uInfo.getEdulevel());
		cv.put(TableUserInfo1.EMAIL, uInfo.getEmail());
		cv.put(TableUserInfo1.EXPENSESAMT, uInfo.getExpensesamt());
		cv.put(TableUserInfo1.FROMCHAN, uInfo.getFromchan());
		cv.put(TableUserInfo1.GOLD, uInfo.getGold());
		cv.put(TableUserInfo1.GRADE, uInfo.getGrade());
		cv.put(TableUserInfo1.GRADEID, uInfo.getGradeid());
		cv.put(TableUserInfo1.GROUPPHOTO, uInfo.getGroupphoto());
		cv.put(TableUserInfo1.HOMEWORKCNT, uInfo.getHomeworkcnt());
		cv.put(TableUserInfo1.INFOSTATE, uInfo.getInfostate());
		cv.put(TableUserInfo1.NAME, uInfo.getName());
		cv.put(TableUserInfo1.NAMEPINYIN, uInfo.getNamepinyin());
		cv.put(TableUserInfo1.PROVINCE, uInfo.getProvince());
		cv.put(TableUserInfo1.QUICKCNT, uInfo.getQuickcnt());
		cv.put(TableUserInfo1.ROLEID, uInfo.getRoleid());
		cv.put(TableUserInfo1.SCHOOLS, uInfo.getSchools());
		cv.put(TableUserInfo1.SCHOOLSID, uInfo.getSchoolsid());
		cv.put(TableUserInfo1.SEX, uInfo.getSex());
		cv.put(TableUserInfo1.STATE, uInfo.getState());
		cv.put(TableUserInfo1.SUPERVIP, uInfo.getSupervip());
		cv.put(TableUserInfo1.TEACHLEVEL, uInfo.getTeachlevel());
		cv.put(TableUserInfo1.TEL, uInfo.getTel());
		cv.put(TableUserInfo1.THIRDNAME, uInfo.getThirdname());
		cv.put(TableUserInfo1.TOKENID, uInfo.getTokenid());
		if (!isUpdate) {
			cv.put(TableUserInfo1.USERID, uInfo.getUserid());
		}
		cv.put(TableUserInfo1.WELEARNID, uInfo.getWelearnid());
		cv.put(TableUserInfo1.TEACHMAJOR, uInfo.getTeachmajor());
		cv.put(TableUserInfo1.MAJOR, uInfo.getMajor());
		cv.put(TableUserInfo1.NEWUSER, uInfo.getNewuser());
		cv.put(TableUserInfo1.ORGID, uInfo.getOrgid());
		cv.put(TableUserInfo1.ORGNAME, uInfo.getOrgname());

		if (isUpdate) {
			return updatetUserInfo(uInfo, cv);
		} else {
			return welearnDataBase.insert(TableUserInfo1.TABLE_NAME, null, cv);
		}
	}

	public int updatetUserInfo(UserInfoModel uInfo, ContentValues cv) {
		if (null == uInfo || null == cv) {
			return -1;
		}

		return welearnDataBase.update(TableUserInfo1.TABLE_NAME, cv, TableUserInfo1.USERID + " = ?",
				new String[] { String.valueOf(uInfo.getUserid()) });
	}

	public int deleteCurrentUserInfo() {
		return welearnDataBase.delete(TableUserInfo1.TABLE_NAME, null, null);
	}
	// ----- TableUserInfo1 End -----

	// ----- TableContactInfo Start -----
	public static class TableContactsInfo implements BaseColumns {
		public static final String TABLE_NAME = "t_contacts_info";
		public static final String ADOPTCNT = "adoptcnt";// 0,采纳次数
		public static final String AGE = "age";// 0
		public static final String ALLOWGRAB = "allowgrab";// : 1,
		public static final String AMOUNTAMT = "amountamt";// : 0,消费累计金额
		public static final String ARBCNT = "arbcnt"; // : 0,仲裁次数
		public static final String AVATAR_100 = "avatar_100";
		public static final String AVATAR_40 = "avatar_40";
		public static final String CITY = "city";
		public static final String COUNTAMT = "countamt";// : 0,发任务次数
		public static final String CREDIT = "credit";// : 5,
		public static final String DREAMUNIV = "dreamuniv";// "",
		public static final String DREAMUNIVID = "dreamunivid";
		public static final String EARNGOLD = "earngold";// 0,老师收入金币
		public static final String EDULEVEL = "edulevel";// 0,老师教育水平
		public static final String EMAIL = "email";
		public static final String EXPENSESAMT = "expensesamt";// 消费累计金额,
		public static final String FROMCHAN = "fromchan";
		public static final String GOLD = "gold";
		public static final String GRADE = "grade";
		public static final String GRADEID = "gradeid";
		public static final String GROUPPHOTO = "groupphoto";// 背景图像
		public static final String HOMEWORKCNT = "homeworkcnt";// 0,发作业次数
		public static final String INFOSTATE = "infostate";// 0,标识用户信息是否完整
		public static final String NAME = "name";
		public static final String NAMEPINYIN = "namepinyin";
		public static final String PROVINCE = "province";
		public static final String QUICKCNT = "quickcnt";// 0,抢题次数
		public static final String ROLEID = "roleid";// 1,
		public static final String SCHOOLS = "schools";
		public static final String SCHOOLSID = "schoolsid";// 0
		public static final String SEX = "sex";
		public static final String STATE = "state";// 1,是否封号
		public static final String SUPERVIP = "supervip";
		public static final String TEACHLEVEL = "teachlevel";
		public static final String TEL = "tel";
		public static final String THIRDNAME = "thirdname";
		public static final String TOKENID = "tokenid";
		public static final String USERID = "userid";
		public static final String WELEARNID = "welearnid";// 0 所属客服id
		public static final String TEACHMAJOR = "teachmajor";// 老师擅长科目
		public static final String MAJOR = "major";// 专业
		public static final String RELATION = "relation";// :0/1关系
		public static final String RELATIONTYPE = "relationtype";// 学生与机构的关系，
																	// #0无关 1关注
																	// 2成员
		public static final String NEWUSER = "newuser";// :0
		public static final String CONTACT_SUBJECT = "contact_subject";// 老师简略擅长科目
	}

	public static final String getCreateContactsInfoTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableContactsInfo.TABLE_NAME).append(" (");
		sb.append(TableContactsInfo._ID).append(" INTEGER PRIMARY KEY,");
		sb.append(TableContactsInfo.ADOPTCNT).append(" INTEGER,");
		sb.append(TableContactsInfo.AGE).append(" INTEGER,");
		sb.append(TableContactsInfo.ALLOWGRAB).append(" INTEGER,");
		sb.append(TableContactsInfo.AMOUNTAMT).append(" FLOAT,");
		sb.append(TableContactsInfo.ARBCNT).append(" INTEGER,");
		sb.append(TableContactsInfo.AVATAR_40).append(" TEXT,");
		sb.append(TableContactsInfo.AVATAR_100).append(" TEXT,");
		sb.append(TableContactsInfo.CITY).append(" TEXT,");
		sb.append(TableContactsInfo.COUNTAMT).append(" INTEGER,");
		sb.append(TableContactsInfo.CREDIT).append(" FLOAT,");
		sb.append(TableContactsInfo.DREAMUNIV).append(" TEXT,");
		sb.append(TableContactsInfo.DREAMUNIVID).append(" TEXT,");
		sb.append(TableContactsInfo.EARNGOLD).append(" FLOAT,");
		sb.append(TableContactsInfo.EDULEVEL).append(" INTEGER,");
		sb.append(TableContactsInfo.EMAIL).append(" TEXT,");
		sb.append(TableContactsInfo.EXPENSESAMT).append(" FLOAT,");
		sb.append(TableContactsInfo.FROMCHAN).append(" TEXT,");
		sb.append(TableContactsInfo.GOLD).append(" FLOAT,");
		sb.append(TableContactsInfo.GRADE).append(" TEXT,");
		sb.append(TableContactsInfo.GRADEID).append(" INTEGER,");
		sb.append(TableContactsInfo.GROUPPHOTO).append(" TEXT,");
		sb.append(TableContactsInfo.HOMEWORKCNT).append(" INTEGER,");
		sb.append(TableContactsInfo.INFOSTATE).append(" INTEGER,");
		sb.append(TableContactsInfo.NAME).append(" TEXT,");
		sb.append(TableContactsInfo.NAMEPINYIN).append(" TEXT,");
		sb.append(TableContactsInfo.PROVINCE).append(" TEXT,");
		sb.append(TableContactsInfo.QUICKCNT).append(" INTEGER,");
		sb.append(TableContactsInfo.ROLEID).append(" INTEGER,");
		sb.append(TableContactsInfo.SCHOOLS).append(" TEXT,");
		sb.append(TableContactsInfo.SCHOOLSID).append(" INTEGER,");
		sb.append(TableContactsInfo.SEX).append(" INTEGER,");
		sb.append(TableContactsInfo.STATE).append(" INTEGER,");
		sb.append(TableContactsInfo.SUPERVIP).append(" INTEGER,");
		sb.append(TableContactsInfo.TEACHLEVEL).append(" INTEGER,");
		sb.append(TableContactsInfo.TEL).append(" TEXT,");
		sb.append(TableContactsInfo.THIRDNAME).append(" TEXT,");
		sb.append(TableContactsInfo.TOKENID).append(" TEXT,");
		sb.append(TableContactsInfo.USERID).append(" INTEGER,");
		sb.append(TableContactsInfo.WELEARNID).append(" INTEGER,");
		sb.append(TableContactsInfo.TEACHMAJOR).append(" TEXT,");
		sb.append(TableContactsInfo.MAJOR).append(" TEXT,");
		sb.append(TableContactsInfo.RELATION).append(" INTEGER,");
		sb.append(TableContactsInfo.RELATIONTYPE).append(" INTEGER,");
		sb.append(TableContactsInfo.NEWUSER).append(" INTEGER,");
		sb.append(TableContactsInfo.CONTACT_SUBJECT).append(" TEXT");
		sb.append(");");
		return sb.toString();
	}

	public static String getAlterContactsInfoTableColumnRELATIONTYPESql() {
		return "ALTER TABLE " + TableContactsInfo.TABLE_NAME + " ADD COLUMN " + TableContactsInfo.RELATIONTYPE
				+ " INTEGER";
	}

	public UserInfoModel queryContactInfoById(int uid) {
		Cursor c = welearnDataBase.query(TableContactsInfo.TABLE_NAME, null, TableContactsInfo.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return null;
		} else {
			if (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(TableContactsInfo.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(TableContactsInfo.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(TableContactsInfo.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(TableContactsInfo.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(TableContactsInfo.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(TableContactsInfo.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(TableContactsInfo.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(TableContactsInfo.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(TableContactsInfo.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(TableContactsInfo.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(TableContactsInfo.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(TableContactsInfo.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(TableContactsInfo.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(TableContactsInfo.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(TableContactsInfo.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(TableContactsInfo.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(TableContactsInfo.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(TableContactsInfo.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(TableContactsInfo.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(TableContactsInfo.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(TableContactsInfo.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(TableContactsInfo.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(TableContactsInfo.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(TableContactsInfo.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(TableContactsInfo.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(TableContactsInfo.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(TableContactsInfo.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(TableContactsInfo.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(TableContactsInfo.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(TableContactsInfo.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(TableContactsInfo.SEX)));
				u.setState(c.getInt(c.getColumnIndex(TableContactsInfo.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(TableContactsInfo.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(TableContactsInfo.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(TableContactsInfo.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(TableContactsInfo.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(TableContactsInfo.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(TableContactsInfo.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(TableContactsInfo.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(TableContactsInfo.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(TableContactsInfo.MAJOR)));
				u.setRelation(c.getInt(c.getColumnIndex(TableContactsInfo.RELATION)));
				u.setNewuser(c.getInt(c.getColumnIndex(TableContactsInfo.NEWUSER)));
				u.setContact_subject(c.getString(c.getColumnIndex(TableContactsInfo.CONTACT_SUBJECT)));
				c.close();
				return u;
			} else {
				c.close();
			}
		}
		return null;
	}

	public ArrayList<UserInfoModel> queryAllContactInfo() {
		ArrayList<UserInfoModel> list = new ArrayList<UserInfoModel>();
		Cursor c = welearnDataBase.query(TableContactsInfo.TABLE_NAME, null, null, null, null, null, null);
		if (null == c) {
			return list;
		} else {
			while (c.moveToNext()) {
				UserInfoModel u = new UserInfoModel();
				u.setAdoptcnt(c.getInt(c.getColumnIndex(TableContactsInfo.ADOPTCNT)));
				u.setAge(c.getInt(c.getColumnIndex(TableContactsInfo.AGE)));
				u.setAllowgrab(c.getInt(c.getColumnIndex(TableContactsInfo.ALLOWGRAB)));
				u.setAmountamt(c.getString(c.getColumnIndex(TableContactsInfo.AMOUNTAMT)));
				u.setArbcnt(c.getInt(c.getColumnIndex(TableContactsInfo.ARBCNT)));
				u.setAvatar_100(c.getString(c.getColumnIndex(TableContactsInfo.AVATAR_100)));
				u.setAvatar_40(c.getString(c.getColumnIndex(TableContactsInfo.AVATAR_40)));
				u.setCity(c.getString(c.getColumnIndex(TableContactsInfo.CITY)));
				u.setCountamt(c.getInt(c.getColumnIndex(TableContactsInfo.COUNTAMT)));
				u.setCredit(c.getFloat(c.getColumnIndex(TableContactsInfo.CREDIT)));
				u.setDreamuniv(c.getString(c.getColumnIndex(TableContactsInfo.DREAMUNIV)));
				u.setDreamunivid(c.getString(c.getColumnIndex(TableContactsInfo.DREAMUNIVID)));
				u.setEarngold(c.getFloat(c.getColumnIndex(TableContactsInfo.EARNGOLD)));
				u.setEdulevel(c.getInt(c.getColumnIndex(TableContactsInfo.EDULEVEL)));
				u.setEmail(c.getString(c.getColumnIndex(TableContactsInfo.EMAIL)));
				u.setExpensesamt(c.getFloat(c.getColumnIndex(TableContactsInfo.EXPENSESAMT)));
				u.setFromchan(c.getString(c.getColumnIndex(TableContactsInfo.FROMCHAN)));
				u.setGold(c.getFloat(c.getColumnIndex(TableContactsInfo.GOLD)));
				u.setGrade(c.getString(c.getColumnIndex(TableContactsInfo.GRADE)));
				u.setGradeid(c.getInt(c.getColumnIndex(TableContactsInfo.GRADEID)));
				u.setGroupphoto(c.getString(c.getColumnIndex(TableContactsInfo.GROUPPHOTO)));
				u.setHomeworkcnt(c.getInt(c.getColumnIndex(TableContactsInfo.HOMEWORKCNT)));
				u.setInfostate(c.getInt(c.getColumnIndex(TableContactsInfo.INFOSTATE)));
				u.setName(c.getString(c.getColumnIndex(TableContactsInfo.NAME)));
				u.setNamepinyin(c.getString(c.getColumnIndex(TableContactsInfo.NAMEPINYIN)));
				u.setProvince(c.getString(c.getColumnIndex(TableContactsInfo.PROVINCE)));
				u.setQuickcnt(c.getInt(c.getColumnIndex(TableContactsInfo.QUICKCNT)));
				u.setRoleid(c.getInt(c.getColumnIndex(TableContactsInfo.ROLEID)));
				u.setSchools(c.getString(c.getColumnIndex(TableContactsInfo.SCHOOLS)));
				u.setSchoolsid(c.getInt(c.getColumnIndex(TableContactsInfo.SCHOOLSID)));
				u.setSex(c.getInt(c.getColumnIndex(TableContactsInfo.SEX)));
				u.setState(c.getInt(c.getColumnIndex(TableContactsInfo.STATE)));
				u.setSupervip(c.getInt(c.getColumnIndex(TableContactsInfo.SUPERVIP)));
				u.setTeachlevel(c.getInt(c.getColumnIndex(TableContactsInfo.TEACHLEVEL)));
				u.setTel(c.getString(c.getColumnIndex(TableContactsInfo.TEL)));
				u.setThirdname(c.getString(c.getColumnIndex(TableContactsInfo.THIRDNAME)));
				u.setTokenid(c.getString(c.getColumnIndex(TableContactsInfo.TOKENID)));
				u.setUserid(c.getInt(c.getColumnIndex(TableContactsInfo.USERID)));
				u.setWelearnid(c.getInt(c.getColumnIndex(TableContactsInfo.WELEARNID)));
				u.setTeachmajor(c.getString(c.getColumnIndex(TableContactsInfo.TEACHMAJOR)));
				u.setMajor(c.getString(c.getColumnIndex(TableContactsInfo.MAJOR)));
				u.setRelation(c.getInt(c.getColumnIndex(TableContactsInfo.RELATION)));
				u.setRelationtype(c.getInt(c.getColumnIndex(TableContactsInfo.RELATIONTYPE)));
				u.setNewuser(c.getInt(c.getColumnIndex(TableContactsInfo.NEWUSER)));
				u.setContact_subject(c.getString(c.getColumnIndex(TableContactsInfo.CONTACT_SUBJECT)));
				list.add(u);
			}
			c.close();
		}
		return list;
	}

	public boolean isContactExists(int uid) {
		Cursor c = welearnDataBase.query(TableContactsInfo.TABLE_NAME, null, TableContactsInfo.USERID + " = ?",
				new String[] { String.valueOf(uid) }, null, null, null);
		if (null == c) {
			return false;
		} else {
			int count = c.getCount();
			c.close();
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	public long insertOrUpdatetContactInfo(UserInfoModel uInfo) {
		if (null == uInfo) {
			return 0L;
		}

		boolean isUpdate = isContactExists(uInfo.getUserid());

		ContentValues cv = new ContentValues();
		cv.put(TableContactsInfo.ADOPTCNT, uInfo.getAdoptcnt());
		cv.put(TableContactsInfo.AGE, uInfo.getAge());
		cv.put(TableContactsInfo.ALLOWGRAB, uInfo.getAllowgrab());
		cv.put(TableContactsInfo.AMOUNTAMT, uInfo.getAmountamt());
		cv.put(TableContactsInfo.ARBCNT, uInfo.getArbcnt());
		cv.put(TableContactsInfo.AVATAR_100, uInfo.getAvatar_100());
		cv.put(TableContactsInfo.AVATAR_40, uInfo.getAvatar_40());
		cv.put(TableContactsInfo.CITY, uInfo.getCity());
		cv.put(TableContactsInfo.COUNTAMT, uInfo.getCountamt());
		cv.put(TableContactsInfo.CREDIT, uInfo.getCredit());
		cv.put(TableContactsInfo.DREAMUNIV, uInfo.getDreamuniv());
		cv.put(TableContactsInfo.DREAMUNIVID, uInfo.getDreamunivid());
		cv.put(TableContactsInfo.EARNGOLD, uInfo.getEarngold());
		cv.put(TableContactsInfo.EDULEVEL, uInfo.getEdulevel());
		cv.put(TableContactsInfo.EMAIL, uInfo.getEmail());
		cv.put(TableContactsInfo.EXPENSESAMT, uInfo.getExpensesamt());
		cv.put(TableContactsInfo.FROMCHAN, uInfo.getFromchan());
		cv.put(TableContactsInfo.GOLD, uInfo.getGold());
		cv.put(TableContactsInfo.GRADE, uInfo.getGrade());
		cv.put(TableContactsInfo.GRADEID, uInfo.getGradeid());
		cv.put(TableContactsInfo.GROUPPHOTO, uInfo.getGroupphoto());
		cv.put(TableContactsInfo.HOMEWORKCNT, uInfo.getHomeworkcnt());
		cv.put(TableContactsInfo.INFOSTATE, uInfo.getInfostate());
		cv.put(TableContactsInfo.NAME, uInfo.getName());
		cv.put(TableContactsInfo.NAMEPINYIN, uInfo.getNamepinyin());
		cv.put(TableContactsInfo.PROVINCE, uInfo.getProvince());
		cv.put(TableContactsInfo.QUICKCNT, uInfo.getQuickcnt());
		cv.put(TableContactsInfo.ROLEID, uInfo.getRoleid());
		cv.put(TableContactsInfo.SCHOOLS, uInfo.getSchools());
		cv.put(TableContactsInfo.SCHOOLSID, uInfo.getSchoolsid());
		cv.put(TableContactsInfo.SEX, uInfo.getSex());
		cv.put(TableContactsInfo.STATE, uInfo.getState());
		cv.put(TableContactsInfo.SUPERVIP, uInfo.getSupervip());
		cv.put(TableContactsInfo.TEACHLEVEL, uInfo.getTeachlevel());
		cv.put(TableContactsInfo.TEL, uInfo.getTel());
		cv.put(TableContactsInfo.THIRDNAME, uInfo.getThirdname());
		cv.put(TableContactsInfo.TOKENID, uInfo.getTokenid());
		if (!isUpdate) {
			cv.put(TableContactsInfo.USERID, uInfo.getUserid());
		}
		cv.put(TableContactsInfo.WELEARNID, uInfo.getWelearnid());
		cv.put(TableContactsInfo.TEACHMAJOR, uInfo.getTeachmajor());
		cv.put(TableContactsInfo.MAJOR, uInfo.getMajor());
		cv.put(TableContactsInfo.RELATION, uInfo.getRelation());
		cv.put(TableContactsInfo.RELATIONTYPE, uInfo.getRelationtype());
		cv.put(TableContactsInfo.NEWUSER, uInfo.getNewuser());
		cv.put(TableContactsInfo.CONTACT_SUBJECT, uInfo.getContact_subject());

		if (isUpdate) {
			return updatetContactInfo(uInfo, cv);
		} else {
			return welearnDataBase.insert(TableContactsInfo.TABLE_NAME, null, cv);
		}
	}

	public int updatetContactInfo(UserInfoModel uInfo, ContentValues cv) {
		if (null == uInfo || null == cv) {
			return -1;
		}

		return welearnDataBase.update(TableContactsInfo.TABLE_NAME, cv, TableContactsInfo.USERID + " = ?",
				new String[] { String.valueOf(uInfo.getUserid()) });
	}

	public int deleteContactUserInfoByUserId(int uid) {
		return welearnDataBase.delete(TableContactsInfo.TABLE_NAME, TableContactsInfo.USERID + " = ?",
				new String[] { String.valueOf(uid) });
	}

	public int clearContactUserInfo() {
		return welearnDataBase.delete(TableContactsInfo.TABLE_NAME, null, null);
	}
	// ----- TableContactInfo End -----

	// ----- 知识点 Start -----
	public static class TableKnowledge implements BaseColumns {
		public static final String TABLE_NAME = "t_knowledge";
		public static final String GROUPNAME = "groupname";// "groupname text,"
		public static final String GROUPID = "groupid";// "groupid integer,"
		public static final String SUBJECTNAME = "subjectname";// "subjectname
																// text,"
		public static final String SUBJECTID = "subjectid";// "subjectid
															// integer,"
		public static final String CHAPTERNAME = "chaptername";// "chaptername
																// text,"
		public static final String CHAPTERID = "chapterid";// "chapterid
															// integer,"
		public static final String NAME = "name";// "name text,"
		public static final String ID = "id";// "id integer,"
	}

	public static String getCreateKnowLedgeTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableKnowledge.TABLE_NAME).append(" (");
		sb.append(TableKnowledge._ID).append(" INTEGER PRIMARY KEY autoincrement,");

		sb.append(TableKnowledge.GROUPNAME).append(" TEXT,");
		sb.append(TableKnowledge.GROUPID).append(" INTEGER,");
		sb.append(TableKnowledge.SUBJECTNAME).append(" TEXT,");
		sb.append(TableKnowledge.SUBJECTID).append(" INTEGER,");
		sb.append(TableKnowledge.CHAPTERNAME).append(" TEXT,");
		sb.append(TableKnowledge.CHAPTERID).append(" INTEGER,");
		sb.append(TableKnowledge.NAME).append(" TEXT,");
		sb.append(TableKnowledge.ID).append(" INTEGER");

		sb.append(");");
		return sb.toString();
	}

	public static String getDeleteKnowledgeTableSql() {
		return "DROP TABLE IF EXISTS " + TableKnowledge.TABLE_NAME;
	}

	public void insertKnowledge(List<CatalogModel> catalogModels) {
		ContentValues cv = null;
		for (CatalogModel catalogModel : catalogModels) {
			int groupid = catalogModel.getGroupid();
			String groupname = catalogModel.getGroupname();
			List<Subject> subjects = catalogModel.getSubjects();
			if (subjects != null) {

				for (Subject subject : subjects) {
					int subjectid = subject.getSubjectid();
					String subjectname = subject.getSubjectname();
					List<Chapter> chapters = subject.getChapter();
					if (chapters != null) {
						for (Chapter chapter : chapters) {
							int chapterid = chapter.getChapterid();
							String chaptername = chapter.getChaptername();
							List<Point> points = chapter.getPoint();
							if (points != null) {
								for (Point point : points) {
									int id = point.getId();
									String name = point.getName();
									cv = new ContentValues();
									cv.put(TableKnowledge.GROUPNAME, groupname);
									cv.put(TableKnowledge.GROUPID, groupid);
									cv.put(TableKnowledge.SUBJECTNAME, subjectname);
									cv.put(TableKnowledge.SUBJECTID, subjectid);
									cv.put(TableKnowledge.CHAPTERNAME, chaptername);
									cv.put(TableKnowledge.CHAPTERID, chapterid);
									cv.put(TableKnowledge.NAME, name);
									cv.put(TableKnowledge.ID, id);
									welearnDataBase.insert(TableKnowledge.TABLE_NAME, null, cv);
								}
							}
						}
					}
				}
			}
		}

	}

	public ArrayList<CatalogModel> queryAllKonwledge() {
		ArrayList<CatalogModel> catalogModels = null;
		String sql4 = "SELECT * FROM " + TableKnowledge.TABLE_NAME + " ORDER BY " + TableKnowledge.ID;
		String sql3 = "SELECT * FROM (" + sql4 + ") ORDER BY " + TableKnowledge.CHAPTERID;
		String sql2 = "SELECT * FROM (" + sql3 + ") ORDER BY " + TableKnowledge.SUBJECTID;
		String sql = "SELECT * FROM (" + sql2 + ") ORDER BY " + TableKnowledge.GROUPID;
		int groupid = -1;
		int subjectid = -1;
		int chapterid = -1;
		// int id = -1;
		CatalogModel catalogModel = new CatalogModel();
		Subject subject = catalogModel.new Subject();
		Chapter chapter = catalogModel.new Chapter();
		Point point = catalogModel.new Point();
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<Point> points = new ArrayList<Point>();
		Cursor cursor = welearnDataBase.rawQuery(sql, null);
		if (cursor != null) {
			catalogModels = new ArrayList<CatalogModel>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				int queryGroupId = cursor.getInt(cursor.getColumnIndex(TableKnowledge.GROUPID));
				String queryGroupName = cursor.getString(cursor.getColumnIndex(TableKnowledge.GROUPNAME));

				int querySubjectId = cursor.getInt(cursor.getColumnIndex(TableKnowledge.SUBJECTID));
				String querySubjectName = cursor.getString(cursor.getColumnIndex(TableKnowledge.SUBJECTNAME));

				int queryChapterId = cursor.getInt(cursor.getColumnIndex(TableKnowledge.CHAPTERID));
				String queryChapterName = cursor.getString(cursor.getColumnIndex(TableKnowledge.CHAPTERNAME));

				int queryId = cursor.getInt(cursor.getColumnIndex(TableKnowledge.ID));
				String queryName = cursor.getString(cursor.getColumnIndex(TableKnowledge.NAME));

				if (queryGroupId != groupid) {
					groupid = queryGroupId;
					subjectid = -1;
					catalogModel = new CatalogModel();
					catalogModel.setGroupid(queryGroupId);
					catalogModel.setGroupname(queryGroupName);
					catalogModels.add(catalogModel);
					subjects = new ArrayList<Subject>();
					catalogModel.setSubjects(subjects);
				}
				if (querySubjectId != subjectid) {
					subjectid = querySubjectId;
					chapterid = -1;
					subject = catalogModel.new Subject();
					subject.setSubjectid(querySubjectId);
					subject.setSubjectname(querySubjectName);
					subjects.add(subject);
					chapters = new ArrayList<Chapter>();
					subject.setChapter(chapters);
				}
				if (queryChapterId != chapterid) {
					chapterid = queryChapterId;
					chapter = catalogModel.new Chapter();
					chapter.setChapterid(queryChapterId);
					chapter.setChaptername(queryChapterName);
					chapters.add(chapter);
					points = new ArrayList<Point>();
					chapter.setPoint(points);
				}
				if (queryId != -1) {
					point = catalogModel.new Point();
					point.setId(queryId);
					point.setName(queryName);
					points.add(point);
				}

			}
			cursor.close();
		}
		return catalogModels;

	}

	/**
	 * 
	 * @param name
	 *            模糊查询关键字
	 * @param groupid
	 *            初中是1 高中是2
	 * @param subjectid
	 * @return
	 */
	public List<String> queryKnowledgeByName(String name, String groupid, String subjectid) {
		name = "%" + name + "%";
		String innerSql = "SELECT * FROM " + TableKnowledge.TABLE_NAME + " WHERE " + TableKnowledge.NAME
				+ " LIKE ? AND " + TableKnowledge.GROUPID + " = ? AND " + TableKnowledge.SUBJECTID + " = ? ORDER BY "
				+ TableKnowledge._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name, groupid, subjectid });
		if (cursor != null) {
			List<String> knowledges = new ArrayList<String>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				knowledges.add(cursor.getString(cursor.getColumnIndex(TableKnowledge.NAME)));
			}
			cursor.close();
			return knowledges;
		}
		return null;
	}

	public List<String> queryKnowledgeByName(String name) {
		name = "%" + name + "%";
		String innerSql = "SELECT * FROM " + TableKnowledge.TABLE_NAME + " WHERE " + TableKnowledge.NAME
				+ " LIKE ? ORDER BY " + TableKnowledge._ID;
		// LogUtils.i(TAG, innerSql);
		Cursor cursor = welearnDataBase.rawQuery(innerSql, new String[] { name });
		if (cursor != null) {
			List<String> knowledges = new ArrayList<String>();
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				knowledges.add(cursor.getString(cursor.getColumnIndex(TableKnowledge.NAME)));
			}
			cursor.close();
			return knowledges;
		}
		return null;
	}

	public boolean isKnowledgeExis() {
		String innerSql = "SELECT * FROM " + TableKnowledge.TABLE_NAME;
		// LogUtils.i(TAG, innerSql);
		boolean flag = false;
		Cursor cursor = welearnDataBase.rawQuery(innerSql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				flag = true;
			}
			cursor.close();
		}
		return flag;
	}
	// ----- 知识点 End -----

	// 删除subject表和grade
	public void delSubjectAndGradeTable() {
		try {
			welearnDataBase.execSQL("delete from sub_list");
			welearnDataBase.execSQL("delete from grade_list");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/******************************************** 华丽的分割线 ***********************************************************/
	/**
	 * 通知表
	 * 
	 * @return
	 */
	public static String getCreateNoticeTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(TableNotice.TABLE_NAME).append(" (");
		sb.append(TableNotice._ID).append(" INTEGER PRIMARY KEY autoincrement,");
		sb.append(TableNotice.NOTICEID).append(" INTEGER,");
		sb.append(TableNotice.TIMESTR).append(" TEXT,");
		sb.append(TableNotice.TITLE).append(" TEXT,");
		sb.append(TableNotice.CONTENT).append(" TEXT,");
		sb.append(TableNotice.READSTATE).append(" INTEGER default 0");
		sb.append(");");
		return sb.toString();
		
		
	
	}

	public static class TableNotice implements BaseColumns {
		public static final String TABLE_NAME = "noticeinfo";
		public static final String _ID = "_id";
		public static final String NOTICEID = "notice_id";
		public static final String TIMESTR = "timestr";
		public static final String TITLE = "title";
		public static final String CONTENT = "content";
		public static final String READSTATE = "read_state";

	}

	/**
	 * 分页查询通知
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<NoticeModel> selectNoticeWithPage(int pageIndex, int pageSize) {
		
		String sql = "select notice_id,timestr,title,content,read_state from  noticeinfo order by  notice_id desc" + " Limit " + String.valueOf(pageSize) + " Offset "
				+ String.valueOf(pageIndex * pageSize) ;	
		
//		sql="select * from noticeinfo";

		Cursor cursor = welearnDataBase.rawQuery(sql, null);
		Log.e("noticeinfo size-->", cursor.getCount()+"");

		List<NoticeModel> noticeList = new ArrayList<NoticeModel>();
		if (cursor != null) {
			welearnDataBase.beginTransaction();
			while (cursor.moveToNext()) {
				int noticeId = cursor.getInt(cursor.getColumnIndex("notice_id"));
				String timeStr = cursor.getString(cursor.getColumnIndex("timestr"));
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				int readState = cursor.getInt(cursor.getColumnIndex("read_state"));

				NoticeModel entity = new NoticeModel(noticeId, title, content, Long.parseLong(timeStr), readState);
				noticeList.add(entity);
			}
			cursor.close();
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();
		}

		return noticeList;

	}
	
	
	/**
	 * 取出最新的通知
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<NoticeModel> getNewNotice() {
		
		String sql = "select notice_id,timestr,title,content,read_state from  noticeinfo order by  notice_id desc" ;	
		Cursor cursor = welearnDataBase.rawQuery(sql, null);		

		List<NoticeModel> noticeList = new ArrayList<NoticeModel>();
		if (cursor != null) {
			welearnDataBase.beginTransaction();
			while (cursor.moveToNext()) {
				int noticeId = cursor.getInt(cursor.getColumnIndex("notice_id"));
				String timeStr = cursor.getString(cursor.getColumnIndex("timestr"));
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				int readState = cursor.getInt(cursor.getColumnIndex("read_state"));

				NoticeModel entity = new NoticeModel(noticeId, title, content, Long.parseLong(timeStr), readState);
				noticeList.add(entity);
			}
			cursor.close();
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();
		}

		return noticeList;

	}
	
	
	//查询通知是否存在
	public boolean checkIsExixt(int id ){
		boolean isExist=false;
		String  sql="select notice_id from noticeinfo where notice_id="+id;
		Cursor cursor = welearnDataBase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.getCount()>=1) {
				isExist=true;
			}
		}
		return isExist;
		
	}

	/**
	 * 插入通知到数据库
	 * 
	 * @param list
	 */
	public boolean insertNotice(List<NoticeModel> list) {
		boolean isFlag=false;		
		try {
			welearnDataBase.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				NoticeModel model = list.get(i);				
				if (!checkIsExixt(model.getId())) {
					ContentValues values = new ContentValues();
					values.put(TableNotice.NOTICEID, model.getId());
					values.put(TableNotice.TIMESTR, model.getTimestamp() + "");
					values.put(TableNotice.TITLE, model.getTitle());
					values.put(TableNotice.CONTENT, model.getContent());
					welearnDataBase.insert(TableNotice.TABLE_NAME, null, values);
				}	
			}
			welearnDataBase.setTransactionSuccessful();
			welearnDataBase.endTransaction();
			isFlag=true;
		} catch (Exception e) {
			e.printStackTrace();
			isFlag=false;
		}
		return isFlag;
	}

}

package com.ucuxin.ucuxin.tec.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.db.WeLearnDB.TableUserInfo1;
import com.ucuxin.ucuxin.tec.utils.LogUtils;

public class WLDBHelper extends SQLiteOpenHelper {

	public static final String TAG = WLDBHelper.class.getSimpleName();

	public static final String DATABASE_NAME = "db_welearn.db";

	private static WLDBHelper mWLDBHelper;
	private WeLearnDB weLearnDB;
	private static int version=28;

	private WLDBHelper(Context context) {
		super(context, DATABASE_NAME, null, version);
		// 数据库做升级，版本号+1，需要按照以下格式注明修改点
		// version:16整合数据库 yh
		// version:17 增加联系人信息表qhw
		// version:18 增加年级表、科目表yh
		// version:20 联系人表增加小学yh
		// version:21增加消息模块字段
		// version:22 新增用户信息表，删除原来的用户信息表，完全使用go服务器返回数据yh
		// version:23新增知识点表 增加聊天表字段
		// version:24用户信息表增加“专业”字段
		// version:25统一联系人表
		// version:26 聊天增加课程跳转
		// version:27 联系人表增加机构关系字段 , 个人表增加机构名字,机构id
	}

	public static WLDBHelper getInstance() {
		if (null == mWLDBHelper) {
			mWLDBHelper = new WLDBHelper(TecApplication.getContext());
		}
		return mWLDBHelper;
	}

	public WeLearnDB getWeLearnDB() {
		if (null == weLearnDB) {
			weLearnDB = new WeLearnDB(getInstance());
		}
		return weLearnDB;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtils.i(TAG, "onCreate start");
		db.beginTransaction();

		db.execSQL(WeLearnDB.getCreateMessageTableSql());
//		db.execSQL(WeLearnDB.getCreateContactListNewTableSql());
		db.execSQL(WeLearnDB.getCreateGoldTableSql());
		db.execSQL(WeLearnDB.getCreateReceiveUserInfoTableSql());
		db.execSQL(WeLearnDB.getCreateUnivListTableSql());
//		db.execSQL(WeLearnDB.getCreateUserInfoTableSql());
//		db.execSQL(WeLearnDB.getCreateContactInfoTableSql());
		db.execSQL(WeLearnDB.getCreateGradeListTableSql());
		db.execSQL(WeLearnDB.getCreateSubjectsListTableSql());
		db.execSQL(WeLearnDB.getCreateUserInfoTableSql1());
		db.execSQL(WeLearnDB.getCreateKnowLedgeTableSql());
		db.execSQL(WeLearnDB.getCreateContactsInfoTableSql());
		
		db.execSQL(WeLearnDB.getCreateNoticeTableSql());

		db.setTransactionSuccessful();
		db.endTransaction();
		LogUtils.i(TAG, "onCreate end");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtils.i(TAG, "onUpgrade start");
		// 数据表未做修改不需要清除数据
//		 db.beginTransaction();
//		 db.execSQL(WeLearnDB.getDeleteMessageTableSql());
//		 db.execSQL(WeLearnDB.getDeleteContactListNewTableSql());
//		 db.execSQL(WeLearnDB.getDeleteGoldTableSql());
//		 db.execSQL(WeLearnDB.getDeleteReceiveUserInfoTableSql());
//		 db.execSQL(WeLearnDB.getDeleteUnivListTableSql());
//		 db.execSQL(WeLearnDB.getDeleteUserInfoTableSql());
//		 db.setTransactionSuccessful();
//		 db.endTransaction();
		
//		if (oldVersion < 17) {
//			db.execSQL(WeLearnDB.getCreateContactInfoTableSql());
//		}
		if (oldVersion < 18) {
			db.execSQL(WeLearnDB.getCreateGradeListTableSql());
			db.execSQL(WeLearnDB.getCreateSubjectsListTableSql());
		}
		if (oldVersion < 19) {
			db.execSQL(WeLearnDB.getAlterMessageTableColumnTASKIDSql());
			db.execSQL(WeLearnDB.getAlterMessageTableColumnCHECKPOINTIDSql());
			db.execSQL(WeLearnDB.getAlterMessageTableColumnISRIGHTSql());
			db.execSQL(WeLearnDB.getAlterMessageTableColumnCOORDINATESql());
			db.execSQL(WeLearnDB.getAlterMessageTableColumnIMGPATHSql());
		}
		
//		if (oldVersion < 20) {
//			db.execSQL("ALTER TABLE " + TableContactInfo.TABLE_NAME +" ADD COLUMN " + TableContactInfo.XIAO + " TEXT");
//		}
		
		if (oldVersion < 21) {
			db.execSQL(WeLearnDB.getAlterMessageTableColumnWRONGTYPESql());
		}
		if (oldVersion < 22) {
			db.execSQL(WeLearnDB.getCreateUserInfoTableSql1());
			db.execSQL("DROP TABLE IF EXISTS t_user");//删除旧的用户信息表
		}
		if (oldVersion < 23) {
			db.execSQL(WeLearnDB.getCreateKnowLedgeTableSql());
			db.execSQL(WeLearnDB.getAlterMessageTableColumnKPOINTSql());//增加kpoint字段
			db.execSQL(WeLearnDB.getAlterMessageTableColumnNOTICETYPESql());//增加noticetype字段
		}
		if (oldVersion < 24) {//用户信息表增加“专业”字段
			db.execSQL("ALTER TABLE " + TableUserInfo1.TABLE_NAME + " ADD COLUMN " + TableUserInfo1.MAJOR + " TEXT");
		}
		if (oldVersion < 25) {//统一联系人表
			db.execSQL(WeLearnDB.getCreateContactsInfoTableSql());
		}
		if (oldVersion < 26) {
			db.execSQL(WeLearnDB.getAlterMessageTableColumnCOURSEIDSql());
		}
		if (oldVersion < 27) {
			db.execSQL(WeLearnDB.getAlterContactsInfoTableColumnRELATIONTYPESql());
			db.execSQL(WeLearnDB.getAlterUserInfoTableColumnORGIDSql());
			db.execSQL(WeLearnDB.getAlterUserInfoTableColumnORGNAMESql());
		}
		
		
		if (oldVersion < 28) {
			db.execSQL(WeLearnDB.getCreateNoticeTableSql());			
		}
		
		LogUtils.i(TAG, "onUpgrade end");
	}

}

package com.ucuxin.ucuxin.tec.manager;

import java.io.File;

import com.ucuxin.ucuxin.tec.MainActivity2;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.MainActivity;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseIntentManager;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.AuthActivity;
import com.ucuxin.ucuxin.tec.function.GradeChoiceActivity;
import com.ucuxin.ucuxin.tec.function.SearchSchoolActivity;
import com.ucuxin.ucuxin.tec.function.account.CropPhotoActivity;
import com.ucuxin.ucuxin.tec.function.account.LogInActivity;
import com.ucuxin.ucuxin.tec.function.account.PhoneLoginActivity;
import com.ucuxin.ucuxin.tec.function.account.PhoneRegisterActivity;
import com.ucuxin.ucuxin.tec.function.account.SubjectGradeChoiceActivity;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.communicate.PreSendPicReViewActivity;
import com.ucuxin.ucuxin.tec.function.homework.CropImageActivity;
import com.ucuxin.ucuxin.tec.function.homework.HomeWorkReadOnlyActivity;
import com.ucuxin.ucuxin.tec.function.homework.MyCheckedHomeworkActivity;
import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkCheckDetailActivity;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.function.homework.teacher.ChoiceKnowledgeActivity;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.function.partner.SingleEditTextActivity;
import com.ucuxin.ucuxin.tec.function.partner.StudentAssessmentActivity;
import com.ucuxin.ucuxin.tec.function.partner.UserRequestActivity;
import com.ucuxin.ucuxin.tec.function.question.ChoiceListActivity;
import com.ucuxin.ucuxin.tec.function.question.FiterKnowledgeActivity;
import com.ucuxin.ucuxin.tec.function.question.MyQPadActivity;
import com.ucuxin.ucuxin.tec.function.question.OneQuestionMoreAnswersDetailActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerAlbumActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerGrabItemActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerPhotoViewActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerQuestionDetailActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerTextAnswerActivity;
import com.ucuxin.ucuxin.tec.function.question.YearQuestionActivity;
import com.ucuxin.ucuxin.tec.function.settings.AboutActivity;
import com.ucuxin.ucuxin.tec.function.settings.DoNotDisturbActivity;
import com.ucuxin.ucuxin.tec.function.settings.StuModifiedInfoActivity;
import com.ucuxin.ucuxin.tec.function.settings.StudentInfoActivityNew;
import com.ucuxin.ucuxin.tec.function.settings.SystemSettingActivity;
import com.ucuxin.ucuxin.tec.function.settings.TeacherCenterActivityNew;
import com.ucuxin.ucuxin.tec.function.settings.TeacherInfoActivityNew;
import com.ucuxin.ucuxin.tec.function.teccourse.AddHandoutActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.CourseCatalogActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.CourseDetailActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.CramSchoolDetailsActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.KnowledgeQueryActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.SingleStudentQAActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CharpterModel;
import com.ucuxin.ucuxin.tec.function.weike.AddCourseActivity;
import com.ucuxin.ucuxin.tec.function.weike.PurchaseStudentActivity;
import com.ucuxin.ucuxin.tec.function.weike.SubjectChoiceActivity;
import com.ucuxin.ucuxin.tec.function.weike.TecCourseActivity;
import com.ucuxin.ucuxin.tec.function.weike.UpLoadClassActivity;
import com.ucuxin.ucuxin.tec.service.PushService;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

//import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkDetail_OnlyReadActivity;

/**
 * 管理所有的intent跳转
 * 
 * @author parsonswang
 * 
 */
public class IntentManager extends BaseIntentManager {

	public static void startWService(Context context) {
		Intent intent = new Intent(context, PushService.class);
		context.startService(intent);
	}

	public static void stopWService(Context context) {
		Intent i = new Intent(context, PushService.class);
		i.setAction(PushService.ACTION_EXIT_WEBSOCKET_SERVICE);
		context.startService(i);
	}

	public static void goToPreSendPicReViewActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PreSendPicReViewActivity.class, data, isFinish);
	}

	public static void goToPhoneRegActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PhoneRegisterActivity.class, data, isFinish);
	}

	public static void goToPhoneLoginActivity(Activity activity, Bundle data, boolean isFinish) {
		openActivity(activity, PhoneLoginActivity.class, data, isFinish);
	}

//	public static void goToPayAnswerAskView(Activity activity, Bundle data, boolean isFinish) {
//		openActivity(activity, PayAnswerAskActivity.class, data, isFinish);
//	}
//
//	public static void goToQuestionPhotoView(Activity context, Bundle data) {
//		openActivity(context, PayAnswerQuestionPhotoViewActivity.class, data, true);
//	}

	public static void goToDoNotDisturbActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, DoNotDisturbActivity.class, data, isFinish);
	}

	public static void goToMainView(Activity context) {
		openActivity(context, MainActivity2.class, true);
	}

	public static void goToLogInView(Context context) {
//		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningTaskInfo>  tasksInfo = activityManager.getRunningTasks(1);  
//        if(tasksInfo.size() > 0){  
//            if("com.ucuxin.ucuxin.tec".equals(tasksInfo.get(0).topActivity.getPackageName())){
            	openActivity(context, LogInActivity.class, true);
//            }  
//        }
	}

	public static void goToPayAnswerActivity(Activity context,Class<? extends Activity> clazz, boolean isFinish) {
		openActivity(context, clazz, isFinish);
	}

	public static void goToGrabItemView(Activity context, boolean finish) {
		openActivity(context, PayAnswerGrabItemActivity.class, finish);
	}

	public static void goToAlbumView(Activity context, Bundle data) {
		openActivity(context, PayAnswerAlbumActivity.class, data, true);
	}

	public static void goToGrabItemView(Activity context, Bundle bundle, boolean finish) {
//		if (GlobalVariable.payAnswerActivity !=null && context!=GlobalVariable.payAnswerActivity) {
//			context.finish();
//			context = GlobalVariable.payAnswerActivity;
//			finish = false;
//		}
		openActivityForResult(context, PayAnswerGrabItemActivity.class, bundle, finish, 1002);
	}

	public static void goToImageGridView(Activity context, Bundle bundle) {
		openActivity(context, PayAnswerImageGridActivity.class, bundle, true);
	}

	public static void goToPhotoView(Activity context, Bundle data) {
		openActivity(context, PayAnswerPhotoViewActivity.class, data, true);
		// openActivity(context, PayAnswerPhotoCropperViewActivity.class, data,
		// true);//先去裁剪页面
	}

	/**
	 * 裁剪后进入打点界面
	 * 
	 * @param context
	 * @param data
	 */
	public static void goToPayAnswerPhotoView(Activity context, Bundle data) {
		openActivity(context, PayAnswerPhotoViewActivity.class, data, true);
	}

	public static void goToAnswertextView(Activity context, Bundle data) {
		openActivity(context, PayAnswerTextAnswerActivity.class, data, false);
	}

	public static void goToGradeSubjectChoice(Context context, boolean isFinish) {
		openActivity(context, SubjectGradeChoiceActivity.class, isFinish);
	}


	public static void goToAnswersListActivity(Activity context) {
		openActivity(context, YearQuestionActivity.class, false);
	}

	public static void goToAnswersListActivity(Activity context, Bundle data) {
		openActivity(context, YearQuestionActivity.class, data, false);
	}

	public static void goToMyQpadActivity(Activity context, boolean isFinish) {
		openActivity(context, MyQPadActivity.class, isFinish);
	}

	public static void goToMyQpadActivity(Activity context) {
		openActivity(context, MyQPadActivity.class, false);
	}

	public static void goToTargetQpadActivity(Activity context, Bundle data) {
		openActivity(context, MyQPadActivity.class, data, false);
	}

	public static void goToAnswerDetail(Activity context, Bundle data) {
		openActivity(context, OneQuestionMoreAnswersDetailActivity.class, data, false);
	}
	public static void goToAnswerDetail(Activity context, Bundle data , int requestCode) {
		openActivityForResult(context, OneQuestionMoreAnswersDetailActivity.class, data, false , requestCode);
	}

	public static void goToQuestionDetailPicView(Activity context, Bundle data) {
		openActivity(context, PayAnswerQuestionDetailActivity.class, data, false);
	}

	public static void gotoAuthView(Activity context, Bundle data) {
		openActivity(context, AuthActivity.class, data, false);
	}

	public static void goToChatListView(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, ChatMsgViewActivity.class, data, isFinish);
	}

	public static void goToChatListView(Activity context) {
		openActivity(context, ChatMsgViewActivity.class, false);
	}

	public static void goToTeacherInfoView(Activity context, Bundle data) {
		openActivity(context, TeacherInfoActivityNew.class, data, false);
	}

	public static void goToTeacherCenterView(Activity context, Class<? extends Activity> activityClazz, Bundle data) {
		openActivity(context, activityClazz, data, false);
	}

	public static void goToStudentInfoView(Activity context, Bundle data) {
//		openActivity(context, StudentInfoActivity.class, data, false);
		openActivity(context, StudentInfoActivityNew.class, data, false);
	}

	public static void goToSystemSetting(Activity context) {
		openActivity(context, SystemSettingActivity.class, false);
	}

	public static void goToAbout(Activity context) {
		openActivity(context, AboutActivity.class, false);
	}

	public static void goToUserRequest(Activity context) {
		openActivity(context, UserRequestActivity.class, false);
	}

	public static void goToSearchSchoolActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, SearchSchoolActivity.class, data, isFinish);
	}


	/**
	 * 老师换题时点击图片进入大图浏览
	 *
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToHomeWorkDetail_OnlyReadActivity(Activity context, Bundle data, boolean isFinish) {
		//openActivity(context, TecHomeWorkDetail_OnlyReadActivity.class, data, isFinish);
		openActivity(context, HomeWorkReadOnlyActivity.class, data, isFinish);
	}
	/**
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToHomeWorkCheckGrabItemActivity(Activity context,Class<? extends Activity> activityClazz, Bundle data, boolean isFinish) {
		openActivityForResult(context, activityClazz, data, isFinish , 1002);
	}

	/**
	 * 老师进入作业检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToHomeWorkCheckDetailActivity(Activity context, Bundle data, boolean isFinish) {
		// openActivity(context, TecHomeWorkCheckDetailActivity.class, data,
		// isFinish);
		openActivityForResult(context, TecHomeWorkCheckDetailActivity.class, data, isFinish, 10010);
	}
	
	
	   public static void goToMyCheckedHomeWorkActivity(Activity context, Bundle data, boolean isFinish) {
	        // openActivity(context, TecHomeWorkCheckDetailActivity.class, data,
	        // isFinish);
	        openActivityForResult(context, MyCheckedHomeworkActivity.class, data, isFinish, 10010);
	    }

	/**
	 * 老师第一次进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 * @param requestCode
	 */
	public static void goToTecSingleCheckActivity(Activity context, Bundle data, boolean isFinish, int requestCode) {
		openActivityForResult(context, TecHomeWorkSingleCheckActivity.class, data, isFinish, requestCode);
	}

	/**
	 * 老师再次进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToTecSingleCheckActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, TecHomeWorkSingleCheckActivity.class, data, isFinish,TecHomeWorkCheckDetailActivity.SINGLE_POINT_REQUESTCODE);
	}

	/**
	 * 学生进入单点检查
	 * 
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToStuSingleCheckActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, StuHomeWorkSingleCheckActivity.class, data, isFinish,TecHomeWorkCheckDetailActivity.SINGLE_POINT_REQUESTCODE);
	}

	public static void goToStuHomeWorkDetailActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, StuHomeWorkCheckDetailActivity.class, data, isFinish);
	}
	
	/**
	 * 老师选择K型错题后进入选择知识点归属页面
	 * @param context
	 * @param data
	 * @param isFinish
	 * @param requestCode
	 */
	public static void goToChoiceKnowledgeActivity(Activity context, Bundle data, boolean isFinish, int requestCode) {
		openActivityForResult(context, ChoiceKnowledgeActivity.class, data, isFinish, requestCode);
	}
	
	/**
	 * 新过滤
	 * @param context
	 * @param data
	 */
	public static void goToTargetFilterActivity(Activity context ) {
		openActivityForResult(context, FiterKnowledgeActivity.class, null, false, 1002);
	}
	
	/**
	 * 跳转到选择过滤条件
	 * 
	 * @param context
	 */
	public static void goToChoiceListActivity(Activity context , Bundle data ) {
		openActivityForResult(context, ChoiceListActivity.class, data, false, 1002);
	}

	/**
	 * 名师课程
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToTecCourseActivity(Activity context, Bundle data, boolean isFinish) {
		openActivity(context, TecCourseActivity.class, data, isFinish);
	}
	
	/**
	 * 
	 * @param context
	 * @param courseid
	 */
	public static void goToCourseDetailActivity(Activity context, int courseid ) {
		 Bundle data = new Bundle();
		 data.putInt("courseid", courseid);
		openActivity(context, CourseDetailActivity.class, data, false);
	}
	
	/**
	 * 课时目录  requestCode是1003
	 * @param context
	 * @param courseid
	 * @param charptercount
	 */
	public static void goToCourseCatalogActivity(Activity context, int courseid , int charptercount, int gradeid, int subjectid ) {
		Bundle data = new Bundle();
		data.putInt("courseid",courseid);
		data.putInt("charptercount",charptercount);
		data.putInt("gradeid",gradeid);
		data.putInt("subjectid",subjectid);
		openActivityForResult(context, CourseCatalogActivity.class, data, false, 1003);
	}
	
	/**
	 * 添加课程 requestCode是1002
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToAddCourseActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, AddCourseActivity.class, data, isFinish, 1002);
	}
	
	/**
	 * 选择课程年级
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToGradeChoiceActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, GradeChoiceActivity.class, data, isFinish, 1002);
	}
	
	/**
	 * 选择课程科目
	 * @param context
	 * @param data
	 * @param isFinish
	 */
	public static void goToSubjectChoiceActivity(Activity context, Bundle data, boolean isFinish) {
		openActivityForResult(context, SubjectChoiceActivity.class, data, isFinish, 1003);
	}
	
	/**
	 * 上传新课时
	 * @param context
	 * @param charpterid
	 * @param courseid
	 */
	public static void goToUpLoadClassActivity(Activity context ,  int courseid, int gradeid, int subjectid) {
		Bundle data = new Bundle();
		data.putInt("courseid", courseid);
		data.putInt("gradeid",gradeid);
		data.putInt("subjectid",subjectid);
		openActivityForResult(context, UpLoadClassActivity.class, data, false, 1002);
	}
	
	/**
	 * 编辑课时
	 * @param context
	 * @param hourModel
	 */
	public static void goToUpLoadClassActivity(Activity context, CharpterModel hourModel, int gradeid, int subjectid) {
		Bundle data = new Bundle();
		if (hourModel != null) {
			data.putInt("charpterid", hourModel.getCharpterid());
			String kpoint = hourModel.getKpoint();
			String charptername = hourModel.getCharptername();
			data.putString("charptername", charptername == null ? "" : charptername);
			data.putString("kpoint", kpoint == null ? "" : kpoint);
		}
		data.putInt("gradeid",gradeid);
		data.putInt("subjectid",subjectid);
		openActivityForResult(context, UpLoadClassActivity.class, data, false, 1002);
	}

	/**
	 * 上传讲义
	 * @param context
	 * @param charpterid
	 * @param imgpath
	 */
	public static void goToAddHandoutActivity(Activity context ,  int charpterid , String imgpath) {
		Bundle data = new Bundle();
		data.putInt("charpterid", charpterid);
		data.putString("imgpath", imgpath);
		openActivityForResult(context, AddHandoutActivity.class, data, false, 1002);
	}
	
	public static void goToCropImageActivity(Activity context ,  String imgpath , boolean isFromPhotoList) {
		Bundle data = new Bundle();
		data.putBoolean("isFromPhotoList", isFromPhotoList);
		data.putString(PayAnswerImageGridActivity.IMAGE_PATH, imgpath);
		openActivityForResult(context, CropImageActivity.class, data, false, 1202);
	}
	
	/**
	 * 编辑讲义
	 * @param context
	 * @param imgurl
	 * @param pageid
	 */
	public static void goToAddHandoutActivity(Activity context  , String imgurl,  int pageid) {
		Bundle data = new Bundle();
		data.putInt("pageid", pageid);
		data.putString("imgpath", imgurl);
		openActivityForResult(context, AddHandoutActivity.class, data, false, 1002);
	}
	
	/**
	 * 选择知识点 
	 * @param context
	 * @param kpoint
	 * @param gradeid  小学传6-12中的一个数字
	 * @param subjectid
	 */
	public static void goToKnowledgeQueryActivity(Activity context  , String kpoint,  int gradeid,  int subjectid) {
		Bundle data = new Bundle();
		data.putInt("gradeid", gradeid);
		data.putInt("subjectid", subjectid);
		data.putString(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME, kpoint);
		openActivityForResult(context, KnowledgeQueryActivity.class, data, false, 1003);
	}

	/**
	 * 已购学生列表 requestCode是1004
	 * @param context
	 * @param courseid
	 */
	public static void goToPurchaseStudentActivity(Activity context, int courseid) {
		Bundle data = new Bundle();
		data.putInt("courseid", courseid);
		openActivityForResult(context, PurchaseStudentActivity.class, data, false, 1004);
	}
	
	/**
	 * 进入回答学生追问
	 * @param context
	 * @param pageid
	 * @param imgpath
	 * @param charptername
	 * @param studentid
	 * @param avatar
	 * @param name
	 */
	public static void goToSingleStudentQAActivity(Activity context, int pageid, String imgpath, String charptername,
			int studentid, String avatar, String name) {
		Bundle data = new Bundle();
		data.putInt("pageid", pageid);
		data.putString("imgpath", imgpath);
		data.putString("charptername", charptername);
		data.putInt("studentid", studentid);
		data.putString("avatar", avatar);
		data.putString("name", name);
		openActivityForResult(context, SingleStudentQAActivity.class, data, false, 1002);
	}
	
	
	/**
	 * 
	 * 此方法描述的是：从老师主页进修改资料
	 * @author:  qhw
	 * @最后修改人： qhw 
	 * @最后修改日期:2015-7-22 下午4:28:17
	 * @version: 2.0
	 *
	 * goToStuModifiedInfoActivity
	 * @param context void
	 */
	public static void goToStuModifiedInfoActivity(Activity context) {
		openActivityForResult(context, StuModifiedInfoActivity.class, null, false , TeacherCenterActivityNew.REQUEST_CODE_MODIFY);
	}
	
	/**
	 * 
	 * 此方法描述的是：个人主页修改背景上传前截取
	 * @author:  qhw
	 * @最后修改人： qhw 
	 * @最后修改日期:2015-7-23 下午3:06:03
	 * @version: 2.0
	 *
	 * goToCropPhotoActivity
	 * @param context
	 * @param path void
	 */
	public static void goToCropPhotoActivity(Activity context,String path) {
		Bundle data = new Bundle();
		data.putString("path", path);
		openActivityForResult(context, CropPhotoActivity.class, data, false , TeacherCenterActivityNew.REQUEST_CODE_CROP);
	}
	
	/**
	 * 
	 * 此方法描述的是：查看学生对老师的评价
	 * @author:  qhw
	 * @最后修改人： qhw 
	 * @最后修改日期:2015-7-23 下午3:55:34
	 * @version: 2.0
	 *
	 * goToStudentAssessmentActivity
	 * @param context
	 * @param userid void
	 */
	public static void goToStudentAssessmentActivity(Activity context,int userid) {
		Bundle data = new Bundle();
		data.putInt("userid", userid);
		openActivity(context, StudentAssessmentActivity.class, data, false );
	}
	
	public static void goToSingleEditTextView(Activity context, Bundle data) {
		openActivityForResult(context, SingleEditTextActivity.class, data, false);
	}

	public static void gotoPersonalPage(Activity context, int userid, int roleid) {
		Bundle data = new Bundle();
		data.putInt("userid", userid);
		data.putInt("roleid", roleid);
		if (roleid == GlobalContant.ROLE_ID_STUDENT|roleid == GlobalContant.ROLE_ID_PARENTS) {
			MobclickAgent.onEvent(context, "studentInfoView");
			IntentManager.goToStudentInfoView(context, data);
		} else if (roleid == GlobalContant.ROLE_ID_COLLEAGE) {
			if (userid == SharePerfenceUtil.getInstance().getUserId()) {
				MobclickAgent.onEvent(context, "teacherCenterView");
				// StatService.onEvent(context, "teacherCenterView", "");
				IntentManager.goToTeacherCenterView(context,TeacherCenterActivityNew.class, data);
			} else {
				// StatService.onEvent(context, "teacherInfoView", "");
				MobclickAgent.onEvent(context, "teacherInfoView");
				IntentManager.goToTeacherInfoView(context, data);
			}
		}
	}


	/**
	 * 跳转学生作业大厅
	 * 
	 * @param context
	 */
	public static void goToStuHomeWorkHallActivity(Activity context) {
		openActivity(context, StuHomeWorkHallActivity.class, false);
	}

	
	public static void startImageCapture(Context context, int tag) {
		if (!MyFileUtil.sdCardIsAvailable()) {
			ToastUtils.show(R.string.text_toast_sdcard_not_available);
			return;
		}
		if (!MyFileUtil.sdCardHasEnoughSpace()) {
			ToastUtils.show(R.string.text_toast_have_not_enough);
			return;
		}

		int requestCode = 0;
		File file = null;

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch (tag) {
//		case GlobalContant.PAY_ANSWER_ASK:
//			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_STUDENT;
//			file = new File(WeLearnFileUtil.getQuestionFileFolder(), "publish.png");
//			break;
		case GlobalContant.PAY_ASNWER:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE;
			file = new File(MyFileUtil.getAnswerFile(), "publish.png");
			break;
		case GlobalContant.SEND_IMG_MSG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_SEND_IMG;
			file = new File(MyFileUtil.getImgMsgFile(), "publish.png");
			break;
		case GlobalContant.CONTACT_SET_USER_IMG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_USER_LOGO;
			file = new File(MyFileUtil.getContactImgFile(), "publish.png");
			break;
		case GlobalContant.CONTACT_SET_BG_IMG:
			requestCode = GlobalContant.CAPTURE_IMAGE_REQUEST_CODE_USER_BG;
			file = new File(MyFileUtil.getContactImgFile(), "publish.png");
			break;
		default:
			break;
		}
		Uri fileUri = Uri.fromFile(file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		try {
			((Activity) context).startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static void gotoCramSchoolDetailsActivity(Activity activity, int orgid) {
		try {
			Intent intent = new Intent(activity, CramSchoolDetailsActivity.class);
			intent.putExtra("orgid", orgid);
			activity.startActivity(intent);
			activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   public static void goToResponderActivity(Activity context,Class<? extends Activity> clazz, boolean isFinish) {
	        openActivity(context, clazz, isFinish);
	    }
}

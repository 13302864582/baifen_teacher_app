package com.ucuxin.ucuxin.tec.function.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

/**
 * 学生信息
 * @author:  sky
 */
public class StudentInfoActivityNew extends BaseActivity implements OnClickListener, HttpListener
		 {

	public static final String TAG = StudentInfoActivityNew.class.getSimpleName();

	private UserInfoModel uInfo = null;

//	private RelativeLayout headBgLayout;
	private NetworkImageView stuBgIv;
	private NetworkImageView stuAvatarIv;
	private TextView stuNickTv;
	private TextView stuGradeTv;

	private LinearLayout moreLayout;
	private View communicateLayout;

	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	private int avatarSize;//, headIvWidth, headIvHeight, headIvTopMargin, headBgWidth, headBgHeight, headBgTopMargin;

	private TextView stuIdTv;

	private TextView stuCreditTv;

	private ImageView stuSexIv;

	private int target_user_id;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_student_info);

		findViewById(R.id.back_layout).setOnClickListener(this);

		initView();

		Intent intent = getIntent();
		target_user_id = intent.getIntExtra("userid", 0);

		if (target_user_id == 0) {
			ToastUtils.show(R.string.userid_error);
			finish();
		}

		updateUiInfo();

		WeLearnApi.getContactInfo(this, target_user_id, this);
	}
   
	public void initView() {
		avatarSize = getResources().getDimensionPixelSize(R.dimen.info_persion_icon_size);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setText(R.string.contact_focus);
		nextStepLayout.setOnClickListener(this);
		
		stuBgIv = (NetworkImageView) findViewById(R.id.stu_info_bg_iv);//背景
		
		stuAvatarIv = (NetworkImageView) findViewById(R.id.stu_info_head_iv);//头像
		
		stuNickTv = (TextView) findViewById(R.id.stu_info_nick_tv);//昵称
		stuSexIv = (ImageView) findViewById(R.id.stu_info_sex_iv);//性别
		stuGradeTv = (TextView) findViewById(R.id.stu_info_grade_tv);//年级
		stuIdTv = (TextView) findViewById(R.id.userid_tv_stu_cen);//用户ID
		stuCreditTv = (TextView) findViewById(R.id.credit_tv_stu_cen);//信用/经验

		moreLayout = (LinearLayout) findViewById(R.id.stu_info_more_layout);
		communicateLayout =  findViewById(R.id.communicate_layout);
		communicateLayout.setVisibility(View.VISIBLE);

		findViewById(R.id.communicate_btn).setOnClickListener(this);
	}

	private void updateUiInfo() {

		uInfo = WLDBHelper.getInstance().getWeLearnDB().queryContactInfoById(target_user_id);

		if (null == uInfo) {
			LogUtils.e(TAG, "Contact info gson is null !");
			return;
		}

		moreLayout.removeAllViews();

		int relationtype = uInfo.getRelation();
		if (relationtype == GlobalContant.UNATTENTION || relationtype == GlobalContant.STUTOTEA) {
			nextStepTV.setText(R.string.contact_focus);
		} else if (relationtype == GlobalContant.ATTENTION || relationtype == GlobalContant.TEATOSTU) {
			nextStepTV.setText(R.string.contact_unfocus);
		}

		//背景
		String groupphoto = uInfo.getGroupphoto();
		if (groupphoto == null) {
			groupphoto = "";
		}
		ImageLoader.getInstance().loadImage(groupphoto, stuBgIv, R.color.welearn_blue_heavy);

		//头像
		String avatar_100 = uInfo.getAvatar_100();
		if (avatar_100 == null) {
			avatar_100 = "";
		}
		ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar_100, stuAvatarIv, avatarSize,
				avatarSize / 10);

		String name = TextUtils.isEmpty(uInfo.getName()) ? getString(R.string.persion_info) : uInfo.getName();
		setWelearnTitle(name);
		stuNickTv.setText(name);

		String grade = TextUtils.isEmpty(uInfo.getGrade()) ? "" : uInfo.getGrade();
		stuGradeTv.setText(grade);

		stuSexIv.setVisibility(View.VISIBLE);
		int sex = uInfo.getSex();
		switch (sex) {
		case GlobalContant.SEX_TYPE_MAN:
			stuSexIv.setImageResource(R.drawable.man_icon);
			break;
		case GlobalContant.SEX_TYPE_WOMEN:
			stuSexIv.setImageResource(R.drawable.woman_icon);
			break;

		default:
			stuSexIv.setVisibility(View.GONE);
			break;
		}
		
		int userid = uInfo.getUserid();
		stuIdTv.setText(getString(R.string.student_id, userid +""));
		
		float credit = uInfo.getCredit();
		String creditStr = GoldToStringUtil.GoldToString(credit);
		stuCreditTv.setText(getString(R.string.credit_text, creditStr));
		
		String area = "";
		String province = uInfo.getProvince();
		if (!TextUtils.isEmpty(province)) {
			area = province + " ";
		}
		String city = uInfo.getCity();
		if (!TextUtils.isEmpty(city)) {
			area = area + city;
		}
		if (!TextUtils.isEmpty(area)) {
			View areaView = getItem(R.string.stu_info_item_title_area1,null , area, false);
			moreLayout.addView(areaView);
		}
		
		// 他的问答
		View hesQuestionView = getItem(R.string.question_ask_text, null, String.valueOf(uInfo.getCountamt()), true);
		hesQuestionView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null == uInfo) {
					return;
				}
				Bundle data = new Bundle();
				data.putInt("roleid", uInfo.getRoleid());
				data.putInt("userid", uInfo.getUserid());
				data.putInt("gradeid", uInfo.getGradeid());
				data.putString("title", uInfo.getName() + getResources().getString(R.string.contact_qpane));
				MobclickAgent.onEvent(StudentInfoActivityNew.this, "ViewStudentQ");
				IntentManager.goToTargetQpadActivity(StudentInfoActivityNew.this, data);
			}
		});
		moreLayout.addView(hesQuestionView);

		// 他的作业
		View hesHomeWorkView = getItem(R.string.homework_ask_text, null, String.valueOf(uInfo.getHomeworkcnt()), true);
		hesHomeWorkView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null == uInfo) {
					return;
				}
				Intent i = new Intent(StudentInfoActivityNew.this, StuHomeWorkHallActivity.class);
				i.putExtra(StuHomeWorkHallActivity.TARGETNAME, getString(R.string.stu_homework_title, uInfo.getName()));
				i.putExtra(StuHomeWorkHallActivity.TARGETID, uInfo.getUserid());
				i.putExtra(StuHomeWorkHallActivity.PACKTYPE, 4);
				startActivity(i);
			}
		});
		moreLayout.addView(hesHomeWorkView);


	}

	private View getItem(int titleResId, String leftStr, String rightStr, boolean showArrow) {
		View v = View.inflate(this, R.layout.view_stu_info_item, null);

		TextView titleTV = (TextView) v.findViewById(R.id.item_title_tv);
		TextView titleValueTV = (TextView) v.findViewById(R.id.item_title_value_tv);
		TextView arrowValueTV = (TextView) v.findViewById(R.id.item_arrow_value_tv);
		ImageView arrowIv = (ImageView) v.findViewById(R.id.item_arrow_iv);

		titleTV.setText(titleResId);
		if (TextUtils.isEmpty(leftStr)) {
			leftStr = "";
		} 
		titleValueTV.setText(leftStr);

		if (TextUtils.isEmpty(rightStr)) {
			rightStr = "";
		} 
		arrowValueTV.setText(rightStr);

		arrowIv.setVisibility(showArrow ? View.VISIBLE : View.GONE);

		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			if (null == uInfo) {
				return;
			}
			int relationtype = uInfo.getRelation();
			if (relationtype == GlobalContant.UNATTENTION || relationtype == GlobalContant.STUTOTEA) {
				MobclickAgent.onEvent(this, UmengEventConstant.CUSTOM_EVENT_ATTENTION);
				WeLearnApi.addFriend(this, target_user_id, new HttpListener() {
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						WeLearnApi.getContactInfo(StudentInfoActivityNew.this, target_user_id, StudentInfoActivityNew.this);
					}

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});
			} else if (relationtype == GlobalContant.ATTENTION || relationtype == GlobalContant.TEATOSTU) {
				MobclickAgent.onEvent(this, UmengEventConstant.CUSTOM_EVENT_UNATTENTION);
				WeLearnApi.deleteFriend(this, target_user_id, new HttpListener() {
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						WeLearnApi.getContactInfo(StudentInfoActivityNew.this, target_user_id, StudentInfoActivityNew.this);
					}

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});
			}
			break;
		case R.id.communicate_btn:
			if (null != uInfo && uInfo.getRoleid() != 0 && uInfo.getUserid() != 0) {
				Bundle data = new Bundle();
				data.putInt("userid", uInfo.getUserid());
				data.putInt("roleid", uInfo.getRoleid());
				data.putString("username", uInfo.getName());
				MobclickAgent.onEvent(this, UmengEventConstant.CUSTOM_EVENT_CHAT);
				IntentManager.goToChatListView(this, data, false);
			}
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			if (TextUtils.isEmpty(dataJson)) {
				return;
			}
			UserInfoModel uInfo = null;
			try {
				uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);
			} catch (Exception e) {
			}
			if (null == uInfo) {
				return;
			}
			WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(uInfo);

			WLDBHelper.getInstance().getWeLearnDB()
					.insertorUpdate(uInfo.getUserid(), uInfo.getRoleid(), uInfo.getName(), uInfo.getAvatar_100());

			updateUiInfo();
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}

}

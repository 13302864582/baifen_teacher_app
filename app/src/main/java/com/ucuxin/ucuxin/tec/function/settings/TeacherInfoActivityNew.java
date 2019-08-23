package com.ucuxin.ucuxin.tec.function.settings;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
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
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

public class TeacherInfoActivityNew extends BaseActivity implements OnClickListener, HttpListener
		 {

	public static final String TAG = TeacherInfoActivityNew.class.getSimpleName();

	private UserInfoModel uInfo = null;

	private NetworkImageView tecBgIv;
	private NetworkImageView tecAvatarIv;
	private TextView tecNickTv, tecSchroolTv, tecGoodSubjectXiaoTv, tecGoodSubjectChuTv, tecGoodSubjectGaoTv;

	private TextView nextStepTV;
	private RelativeLayout nextStepLayout;

	private int avatarSize;//, headIvWidth, headIvHeight, headIvTopMargin, headBgWidth, headBgHeight, headBgTopMargin;

	public static final String EXTRA_TAG_FROM_FRIENDS = "extra_tag_from_friends";
	private int target_user_id;
	private int count;
	private boolean isFromFriends;

	private AlphaAnimation aa = new AlphaAnimation(0f, 1.0f);

	private TextView tecIdTv;

	private TextView tecCreditTv;

	private ImageView tecSexIv;

	private LinearLayout moreLayout;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_teacher_info);

		findViewById(R.id.back_layout).setOnClickListener(this);

		aa.setDuration(300);

		initView();


		Intent intent = getIntent();
		if (null == intent) {
			finish();
		}
		target_user_id = intent.getIntExtra("userid", 0);
		isFromFriends = intent.getBooleanExtra(EXTRA_TAG_FROM_FRIENDS, false);

		if (target_user_id == 0) {
			ToastUtils.show(R.string.userid_error);
			finish();
		}

		if (isFromFriends) {
			updateUiInfo();
		}

		WeLearnApi.getContactInfo(this, target_user_id, this);
	}

	public void initView() {
		avatarSize = getResources().getDimensionPixelSize(R.dimen.student_info_avatar_size);

		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setText(R.string.contact_focus);
		nextStepLayout.setOnClickListener(this);

		tecBgIv = (NetworkImageView) findViewById(R.id.tec_info_bg_iv);
		tecAvatarIv = (NetworkImageView) findViewById(R.id.tec_info_head_iv);
		tecNickTv = (TextView) findViewById(R.id.tec_info_nick_tv);
		tecSchroolTv = (TextView) findViewById(R.id.tec_info_school_tv);
		tecSexIv = (ImageView) findViewById(R.id.tec_info_sex_iv);//性别
		tecIdTv = (TextView) findViewById(R.id.userid_tv_tec_cen);//用户ID
		tecCreditTv = (TextView) findViewById(R.id.credit_tv_tec_cen);//信用/经验
		
		tecGoodSubjectXiaoTv = (TextView) findViewById(R.id.tec_good_subject_xiao_tv);
		tecGoodSubjectChuTv = (TextView) findViewById(R.id.tec_good_subject_chu_tv);
		tecGoodSubjectGaoTv = (TextView) findViewById(R.id.tec_good_subject_gao_tv);
		
		moreLayout = (LinearLayout) findViewById(R.id.tec_info_more_layout);
		
		
		findViewById(R.id.communicate_btn).setOnClickListener(this);
	}


	private void updateUiInfo() {
		if (null == uInfo) {
			LogUtils.e(TAG, "Contact info gson is null !");
			return;
		}

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
		ImageLoader.getInstance().loadImage(groupphoto, tecBgIv, R.color.welearn_blue_heavy);

		//头像
		String avatar_100 = uInfo.getAvatar_100();
		if (avatar_100 == null) {
			avatar_100 = "";
		}
		ImageLoader.getInstance().loadImageWithDefaultAvatar(avatar_100, tecAvatarIv, avatarSize,
				avatarSize / 10);
		// 昵称
		String name = TextUtils.isEmpty(uInfo.getName()) ? getString(R.string.persion_info) : uInfo.getName();
		tecNickTv.setText(name);
		setWelearnTitle(name);

		String grade = TextUtils.isEmpty(uInfo.getSchools()) ? "" : uInfo.getSchools();
		tecSchroolTv.setText(grade);

		tecSexIv.setVisibility(View.VISIBLE);
		int sex = uInfo.getSex();
		switch (sex) {
		case GlobalContant.SEX_TYPE_MAN:
			tecSexIv.setImageResource(R.drawable.man_icon);
			break;
		case GlobalContant.SEX_TYPE_WOMEN:
			tecSexIv.setImageResource(R.drawable.woman_icon);
			break;

		default:
			tecSexIv.setVisibility(View.GONE);
			break;
		}
		int userid = uInfo.getUserid();
		tecIdTv.setText(getString(R.string.welearn_id_text, userid +""));
		
		float credit = uInfo.getCredit();
		String creditStr = GoldToStringUtil.GoldToString(credit);
		tecCreditTv.setText(getString(R.string.credit_text, creditStr));
		
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
		
		String orgname = uInfo.getOrgname();
		if (!TextUtils.isEmpty(orgname)) {
			final int orgid = uInfo.getOrgid();
			View orgView = getItem(R.string.stu_info_item_title_hes_question, null, orgname, false);
			moreLayout.addView(orgView);
		}
		
		// 他的问答
		View hesQuestionView = getItem(R.string.text_reply, null, String.valueOf(uInfo.getAdoptcnt()), true);
		hesQuestionView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle data = new Bundle();

				if (null == uInfo) {
					return;
				}
				data = new Bundle();
				data.putInt("roleid", uInfo.getRoleid());
				data.putInt("userid", uInfo.getUserid());
				data.putInt("gradeid", uInfo.getGradeid());
				data.putString("title", uInfo.getName() + getResources().getString(R.string.contact_qpane));
				MobclickAgent.onEvent(TeacherInfoActivityNew.this, "ViewStudentQ");
				IntentManager.goToTargetQpadActivity(TeacherInfoActivityNew.this, data);

			}
		});
		moreLayout.addView(hesQuestionView);
		
		//学生评价
		View evaluationView = getItem(R.string.stu_evaluation_text, null, String.valueOf(count), true);
		evaluationView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				IntentManager.goToStudentAssessmentActivity(TeacherInfoActivityNew.this,  uInfo.getUserid());
			}
		});
		moreLayout.addView(evaluationView);

		tecGoodSubjectXiaoTv.setVisibility(View.GONE);
		tecGoodSubjectChuTv.setVisibility(View.GONE);
		tecGoodSubjectGaoTv.setVisibility(View.GONE);
		String tecMajor = uInfo.getTeachmajor();
		if (null != tecMajor) {
			String[] majors = tecMajor.split(";");
			if (null != majors && majors.length >= 3) {
				if (!TextUtils.isEmpty(majors[0])) {
					tecGoodSubjectXiaoTv.setText(majors[0]);
					tecGoodSubjectXiaoTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectXiaoTv.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(majors[1])) {
					tecGoodSubjectChuTv.setText(majors[1]);
					tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectChuTv.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(majors[2])) {
					tecGoodSubjectGaoTv.setText(majors[2]);
					tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
				} else {
					tecGoodSubjectGaoTv.setVisibility(View.GONE);
				}
			}
			
		    // sky add
            if (null != majors && majors.length == 2) {

                String sb = majors[0] + majors[1];
                if (sb.contains("小学")) {
                    if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("小学")) {
                        tecGoodSubjectXiaoTv.setText(majors[0]);
                        tecGoodSubjectXiaoTv.setVisibility(View.VISIBLE);
                    } else {
                        tecGoodSubjectXiaoTv.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(majors[1]) && majors[1].contains("初中")) {
                        tecGoodSubjectChuTv.setText(majors[1]);
                        tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
                    } else {
                        tecGoodSubjectChuTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(majors[1]) && majors[1].contains("高中")) {
                        tecGoodSubjectGaoTv.setText(majors[1]);
                        tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
                    } else {
                        tecGoodSubjectGaoTv.setVisibility(View.GONE);
                    }
                }

                if (sb.contains("初中")) {
                    if (sb.contains("小学") && sb.contains("初中")) {
                        if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("小学")) {
                            tecGoodSubjectXiaoTv.setText(majors[0]);
                            tecGoodSubjectXiaoTv.setVisibility(View.VISIBLE);
                        } else {
                            tecGoodSubjectXiaoTv.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(majors[1]) && majors[1].contains("初中")) {
                            tecGoodSubjectChuTv.setText(majors[1]);
                            tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
                        } else {
                            tecGoodSubjectChuTv.setVisibility(View.GONE);
                        }
                    } else if (sb.contains("初中") && sb.contains("高中")) {
                        if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("初中")) {
                            tecGoodSubjectChuTv.setText(majors[0]);
                            tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
                        } else {
                            tecGoodSubjectChuTv.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(majors[1]) && majors[1].contains("高中")) {
                            tecGoodSubjectGaoTv.setText(majors[1]);
                            tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
                        } else {
                            tecGoodSubjectGaoTv.setVisibility(View.GONE);
                        }
                    } else {
                        if (!TextUtils.isEmpty(majors[1]) && majors[1].contains("初中")) {
                            tecGoodSubjectChuTv.setText(majors[1]);
                            tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (sb.contains("高中") && "高中".equals(sb)) {
                    if (!TextUtils.isEmpty(majors[2])) {
                        tecGoodSubjectGaoTv.setText(majors[2]);
                        tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
                    } else {
                        tecGoodSubjectGaoTv.setVisibility(View.GONE);
                    }
                }

            }

            if (null != majors && majors.length == 1) {

                if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("小学")) {
                    tecGoodSubjectXiaoTv.setVisibility(View.VISIBLE);
                    tecGoodSubjectXiaoTv.setText(majors[0]);
                } else {
                    tecGoodSubjectXiaoTv.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("初中")) {
                    tecGoodSubjectChuTv.setVisibility(View.VISIBLE);
                    tecGoodSubjectChuTv.setText(majors[0]);
                } else {
                    tecGoodSubjectChuTv.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(majors[0]) && majors[0].contains("高中")) {
                    tecGoodSubjectGaoTv.setVisibility(View.VISIBLE);
                    tecGoodSubjectGaoTv.setText(majors[0]);
                } else {
                    tecGoodSubjectGaoTv.setVisibility(View.GONE);
                }

            }
			
			
		}
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
						WeLearnApi.getContactInfo(TeacherInfoActivityNew.this, target_user_id,
								TeacherInfoActivityNew.this);
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
						WeLearnApi.getContactInfo(TeacherInfoActivityNew.this, target_user_id,
								TeacherInfoActivityNew.this);
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
		case R.id.tec_answer_count_btn:
			if (null == uInfo) {
				return;
			}
			Bundle data = new Bundle();
			data.putInt("roleid", uInfo.getRoleid());
			data.putInt("userid", uInfo.getUserid());
			data.putInt("gradeid", uInfo.getGradeid());
			data.putString("title", uInfo.getName() + getResources().getString(R.string.contact_qpane));
			MobclickAgent.onEvent(this, "ViewStudentQ");
			IntentManager.goToTargetQpadActivity(this, data);
			break;
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {
			uInfo = new Gson().fromJson(dataJson, UserInfoModel.class);

			if (null != uInfo) {
				count = uInfo.getCount();
				if (isFromFriends) {
					WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(uInfo);
				}
				WLDBHelper.getInstance().getWeLearnDB()
						.insertorUpdate(uInfo.getUserid(), uInfo.getRoleid(), uInfo.getName(), uInfo.getAvatar_100());
			}

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

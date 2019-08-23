package com.ucuxin.ucuxin.tec.function.homework.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.AnswerListItemView;
import com.ucuxin.ucuxin.tec.function.homework.adapter.TecHomeWrokCheckCommonGridViewAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkDetail_OnlyReadActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.GoldToStringUtil;

import androidx.constraintlayout.widget.ConstraintLayout;

public class TecHomeWorkCheckCommonView extends FrameLayout {

	private static final String TAG = TecHomeWorkCheckCommonView.class.getSimpleName();
	private Activity mActivity;
	private NetworkImageView mAvatar_iv;
	private TextView mNick_tv, mDesc_tv, mGrade_tv, mSubject_tv, reward_text_tv_check_common, mRewardVal_tv/*, mCredit_tv*/,
			mAskTime_tv;

	private ImageView mNoQuestion_iv;
	private GridView mHomeWorkImgContainer_gv;

	private HomeWorkModel mHomeWorkModel;
	private int avatarSize;
	private TecHomeWrokCheckCommonGridViewAdapter gridViewAdapter;
	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

	private TextView tv_xuehao, tv_vip_info/*, tv_extra_value*/;
	private ConstraintLayout info_card;

	public TecHomeWorkCheckCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	public TecHomeWorkCheckCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	public TecHomeWorkCheckCommonView(Context context) {
		super(context);
		this.mActivity = (Activity) context;
		setUpViews();
	}

	private void setUpViews() {
		View payAnswerView = LayoutInflater.from(getContext()).inflate(R.layout.homeworkcheckcommonview, null);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		mAvatar_iv = (NetworkImageView) payAnswerView.findViewById(R.id.avatar_iv_check_common);
		mNick_tv = (TextView) payAnswerView.findViewById(R.id.nick_tv_check_common);
		tv_xuehao = (TextView) payAnswerView.findViewById(R.id.tv_xuehao);
		tv_vip_info = (TextView) payAnswerView.findViewById(R.id.tv_vip_info);
		//mCredit_tv = (TextView) payAnswerView.findViewById(R.id.credit_tv_check_common);
		mAskTime_tv = (TextView) payAnswerView.findViewById(R.id.ask_time_tv_check_common);

		mHomeWorkImgContainer_gv = (GridView) payAnswerView.findViewById(R.id.homework_img_container_gridview_common);
		mNoQuestion_iv = (ImageView) payAnswerView.findViewById(R.id.no_question_iv_check_common);
		mDesc_tv = (TextView) payAnswerView.findViewById(R.id.desc_tv_check_common);
		info_card = (ConstraintLayout)payAnswerView.findViewById(R.id.info_card);
		mGrade_tv = (TextView) payAnswerView.findViewById(R.id.asker_grade_tv_check_common);
		mSubject_tv = (TextView) payAnswerView.findViewById(R.id.asker_subject_tv_check_common);
		reward_text_tv_check_common = (TextView) payAnswerView.findViewById(R.id.reward_text_tv_check_common);
		mRewardVal_tv = (TextView) payAnswerView.findViewById(R.id.reward_val_tv_check_common);
		//tv_extra_value = (TextView) payAnswerView.findViewById(R.id.tv_extra_value);
		addView(payAnswerView);
	}

	public void showData(HomeWorkModel homeWorkModel) {
		this.mHomeWorkModel = homeWorkModel;
		mNick_tv.setClickable(true);
		mAvatar_iv.setClickable(true);
		mHomeWorkImgContainer_gv.setClickable(true);
		mHomeWorkImgContainer_gv.setVisibility(View.VISIBLE);
		mNoQuestion_iv.setVisibility(View.GONE);

		mHomeWorkPageModelList = mHomeWorkModel.getPagelist();
		// ImageLoader.getInstance().loadImageWithDefaultAvatar(homeWorkModel.getAvatar(),
		// mAvatar_iv, avatarSize,
		// avatarSize / 10);
		Log.e("fzimg",homeWorkModel.getAvatar());
		ImageLoader.getInstance().loadImageWithDefaultParentAvatar(homeWorkModel.getAvatar(), mAvatar_iv, avatarSize,
				avatarSize / 10);
		mAvatar_iv.setTag(homeWorkModel.getStudid());
		mAvatar_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Integer userid = (Integer) view.getTag();
				IntentManager.gotoPersonalPage(mActivity, userid, GlobalContant.ROLE_ID_STUDENT);
			}
		});

		if (mHomeWorkImgContainer_gv.getAdapter() == null) {
			gridViewAdapter = new TecHomeWrokCheckCommonGridViewAdapter(mHomeWorkPageModelList, mActivity);
			mHomeWorkImgContainer_gv.setAdapter(gridViewAdapter);
		} else {
			gridViewAdapter.setList(mHomeWorkPageModelList);
		}
		mHomeWorkImgContainer_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// gridViewAdapter.click(view , position);

				Bundle data = new Bundle();
				data.putInt(AnswerListItemView.EXTRA_POSITION, position);
				data.putSerializable(TecHomeWorkDetail_OnlyReadActivity.HOMEWROKDETAILPAGERLIST,
						mHomeWorkPageModelList);
				IntentManager.goToHomeWorkDetail_OnlyReadActivity(mActivity, data, false);
			}
		});
		int orgid = homeWorkModel.getOrgid();

		mNick_tv.setText(homeWorkModel.getStudname());
		tv_xuehao.setText("学号: " + homeWorkModel.getStudid());
		if (!TextUtils.isEmpty(homeWorkModel.getVip_level_content())) {
			tv_vip_info.setVisibility(View.VISIBLE);
			tv_vip_info.setText(homeWorkModel.getVip_level_content());
		} else {
			tv_vip_info.setVisibility(View.GONE);
		}

		int credit = homeWorkModel.getCredit();
		//mCredit_tv.setText(mActivity.getString(R.string.homework_check_common_credit_text, credit));
		SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm:ss");
		String askTime = format.format(new Date(homeWorkModel.getDatatime()));
		mAskTime_tv.setText(askTime);
		info_card.setVisibility(View.VISIBLE);
		mDesc_tv.setVisibility(View.VISIBLE);
		mDesc_tv.setText(homeWorkModel.getMemo());
		mGrade_tv.setText(homeWorkModel.getGrade());
		mSubject_tv.setText(homeWorkModel.getSubject());
		float gold = homeWorkModel.getBounty() + homeWorkModel.getGivegold();
		String goldToString = GoldToStringUtil.GoldToString(gold);
		reward_text_tv_check_common.setVisibility(View.VISIBLE);
		mRewardVal_tv.setText("￥ " + goldToString);
		/*if (!TextUtils.isEmpty(homeWorkModel.getVip_additional_content())) {
			tv_extra_value.setText(homeWorkModel.getVip_additional_content());
		} else {
			tv_extra_value.setText("");
		}*/

	}

	/**
	 * 换题发现为空UI
	 */
	public void showDataNullQuestion() {
		ImageLoader.getInstance().loadImage(null, mAvatar_iv, R.drawable.default_contact_image);
		mNoQuestion_iv.setVisibility(View.VISIBLE);
		mHomeWorkImgContainer_gv.setVisibility(View.GONE);
		info_card.setVisibility(View.GONE);
		mNick_tv.setText("");
		//mCredit_tv.setVisibility(View.INVISIBLE);
		mDesc_tv.setVisibility(View.GONE);
		mGrade_tv.setText("");
		mSubject_tv.setText("");
		reward_text_tv_check_common.setVisibility(View.GONE);
		mRewardVal_tv.setText("");
		mAskTime_tv.setText("");
		tv_xuehao.setText("");
		tv_vip_info.setVisibility(View.GONE);
		mNick_tv.setClickable(false);
		mAvatar_iv.setClickable(false);

	}

}

package com.ucuxin.ucuxin.tec.function.homework.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.function.homework.StateOfHomeWork;
import com.ucuxin.ucuxin.tec.function.homework.adapter.TecHomeWrokCheckCommonGridViewAdapter;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkHallActivity;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.SpUtil;

public class HomeWorkHallItemCommonView extends FrameLayout implements OnItemClickListener {

	private StuHomeWorkHallActivity mActivity;
	private NetworkImageView mAvatarIv;
	private TextView mNickTv;
	private TextView mAskTime;
	private TextView mGradeSubjectTv;
	private View mNewAskerTag;
	private View mVipAskerTag;
	private GridView mPicGv;
	private TextView mAnswerQTv;
	// private TextView mSowNumTv;
//	private TextView mCollectCountTv;
//	private ImageView mCollectIconIv;
	private TecHomeWrokCheckCommonGridViewAdapter gridViewAdapter;
	private TextView mAnswerTimeTv;
	private HomeWorkModel mHomeWorkModel;
	// private ArrayList<NetworkImageView> mPicList = new
	// ArrayList<NetworkImageView>();
	private int avatarSize;
	private TextView mStateTv;
	private String[] States = { "抢答中", "答题中", "已解答", "追问中", "已采纳", "已拒绝", "仲裁中", "仲裁完成", "被举报", "已删除" };
	private int packtype;

	public HomeWorkHallItemCommonView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mActivity = (StuHomeWorkHallActivity) context;
		setUpViews();
	}

	public HomeWorkHallItemCommonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mActivity = (StuHomeWorkHallActivity) context;
		setUpViews();
	}

	public HomeWorkHallItemCommonView(Context context) {
		super(context);
		this.mActivity = (StuHomeWorkHallActivity) context;
		setUpViews();
	}

	private void setUpViews() {
		avatarSize = getResources().getDimensionPixelSize(R.dimen.avatar_size_homework_check_common);

		View view = LayoutInflater.from(getContext()).inflate(R.layout.homework_hall_item_commonview, null);
		// mPicContainer = (RelativeLayout)
		// view.findViewById(R.id.pic_container_add_point);
		// mPicContainer.setOnTouchListener(this);
		mAvatarIv = (NetworkImageView) view.findViewById(R.id.avatar_iv_hall_item_common);
		mNickTv = (TextView) view.findViewById(R.id.nick_tv_hall_item_common);
		mAskTime = (TextView) view.findViewById(R.id.ask_time_tv_hall_item_common);
		mGradeSubjectTv = (TextView) view.findViewById(R.id.grade_subject_tv_hall_item_common);
		mNewAskerTag = view.findViewById(R.id.new_asker_tv_hall_item_common);
		mVipAskerTag = view.findViewById(R.id.vip_asker_iv_hall_item_common);
		mPicGv = (GridView) view.findViewById(R.id.img_list_container_gridview_item);
		gridViewAdapter = new TecHomeWrokCheckCommonGridViewAdapter(null, mActivity);
		mPicGv.setAdapter(gridViewAdapter);
		mPicGv.setOnItemClickListener(this);

		mAnswerQTv = (TextView) view.findViewById(R.id.answer_qulity_tv_hall_item_common);
		mAnswerTimeTv = (TextView) view.findViewById(R.id.answer_time_tv_hall_item_common);
		// mSowNumTv = (TextView)
		// view.findViewById(R.id.sow_num_tv_hall_item_common);
//		mCollectIconIv = (ImageView) view.findViewById(R.id.collection_icon_iv_hall_item);
//		mCollectCountTv = (TextView) view.findViewById(R.id.collection_count_tv_hall_item);
		mStateTv = (TextView) view.findViewById(R.id.state_homework_tv_hallitem);
//
//		view.findViewById(R.id.collection_container_ll_hall_item).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				mActivity.showCollectingDialog();
//				JSONObject data = new JSONObject();
//				try {
//					data.put("taskid", mHomeWorkModel.getTaskid());
//					data.put("tasktype", 2);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//
//				HttpHelper.post("common", "collect", data, new HttpLisener() {
//
//					@Override
//					public void onSuccess(int code, String dataJson, String errMsg) {
//						mActivity.closeDialogHelp();
//						if (code == 0) {
//							int praise = mHomeWorkModel.getPraise();// 0是空心 1是实心
//							int praisecnt = mHomeWorkModel.getPraisecnt();
//							if (praise == 0) {
//								praisecnt++;
//								mCollectIconIv.setImageResource(R.drawable.collect_ic_shixin);
//								mHomeWorkModel.setPraise(1);
//
//							} else {
//								praisecnt--;
//								mCollectIconIv.setImageResource(R.drawable.collect_ic_kongxin);
//								mHomeWorkModel.setPraise(0);
//							}
//							mCollectCountTv.setText("" + praisecnt);
//							mHomeWorkModel.setPraisecnt(praisecnt);
//						} else {
//							ToastUtils.show(errMsg);
//						}
//					}
//
//					@Override
//					public void onFail(int HttpCode) {
//						mActivity.closeDialogHelp();
//					}
//				});
//			}
//		});

		addView(view);
	}

	/**
	 * 作业详情单页
	 * 
	 * @param homeWorkModel
	 * @param packtype
	 */
	public void showData(HomeWorkModel homeWorkModel, int packtype) {
		if (homeWorkModel == null) {
			return;
		}
		this.mHomeWorkModel = homeWorkModel;
		this.packtype = packtype;
		// for (NetworkImageView picIv : mPicList) {
		// picIv.setClickable(false);
		// picIv.setVisibility(View.INVISIBLE);
		// }

		ImageLoader.getInstance().loadImageWithDefaultAvatar(homeWorkModel.getAvatar(), mAvatarIv, avatarSize,
				avatarSize / 10);
		mAvatarIv.setTag(mHomeWorkModel.getStudid());
		mAvatarIv.setOnClickListener(mActivity);

		mNickTv.setText(homeWorkModel.getStudname());
		SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm:ss");
		String askTime = format.format(new Date(homeWorkModel.getDatatime()));
		mAskTime.setText(askTime);
		mGradeSubjectTv.setText(homeWorkModel.getGrade() + "  " + homeWorkModel.getSubject());
		if (homeWorkModel.getSupervip() > 0) {
			mVipAskerTag.setVisibility(View.VISIBLE);
			mNewAskerTag.setVisibility(View.INVISIBLE);
		} else {
			mVipAskerTag.setVisibility(View.INVISIBLE);
			if (homeWorkModel.getIsnew() == 1) {
				mNewAskerTag.setVisibility(View.VISIBLE);
			} else {
				mNewAskerTag.setVisibility(View.INVISIBLE);
			}
		}
		mStateTv.setText(States[mHomeWorkModel.getState()]);
		ArrayList<StuPublishHomeWorkPageModel> pagelist = homeWorkModel.getPagelist();
		gridViewAdapter.setList(pagelist);
		int satisfaction = homeWorkModel.getSatisfaction();
		if (satisfaction != 0) {
			mAnswerQTv.setVisibility(View.VISIBLE);
			mAnswerQTv.setText(mActivity.getString(R.string.answer_quality_text, satisfaction + "")); // 详细程度几星
		} else {
			mAnswerQTv.setVisibility(View.GONE);
		}
		long answertime = homeWorkModel.getAnswertime();
		if (answertime != 0) {
			long dTime = answertime - homeWorkModel.getGrabtime(); // homeWorkModel.getDatatime();
			int answerTime = (int) (dTime / 60000);
			if (answerTime < 3) {
				answerTime = 3;
			}
			mAnswerTimeTv.setVisibility(View.VISIBLE);
			mAnswerTimeTv.setText(mActivity.getString(R.string.answer_time_text, answerTime + ""));
		} else {
			mAnswerTimeTv.setVisibility(View.GONE);
		}

//		int praise = homeWorkModel.getPraise();// 0是空心 1是实心
//		int praisecnt = homeWorkModel.getPraisecnt();
//		mCollectCountTv.setText("" + praisecnt);
//		if (praise == 0) {
//			mCollectIconIv.setImageResource(R.drawable.collect_ic_kongxin);
//		} else {
//			mCollectIconIv.setImageResource(R.drawable.collect_ic_shixin);
//		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		mActivity.uMengEvent("homework_detailfrommy_tec");

		Bundle data = new Bundle();
		data.putInt("position", position);
		switch (packtype) {
		case 0:// 作业广场
		case 1:// 作业广场(学生刚发布了一个作业)
		case 2:// 我的作业
		case 4:// 他人的作业
			data.putInt("taskid", mHomeWorkModel.getTaskid());
			IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);

			break;
		case 3:// 我检查的作业
			int state = mHomeWorkModel.getState();
			switch (state) {
			case StateOfHomeWork.ADOPTED:// 已采纳
			case StateOfHomeWork.ARBITRATED:// 仲裁完成
			case StateOfHomeWork.ASKING:// 抢题中
			case StateOfHomeWork.REFUSE:// 已拒绝
			case StateOfHomeWork.ARBITRATE:// 仲裁中
			case StateOfHomeWork.REPORT:// 已举报
			case StateOfHomeWork.DELETE:// 已删除
				data.putInt("taskid", mHomeWorkModel.getTaskid());
				IntentManager.goToStuHomeWorkDetailActivity(mActivity, data, false);
				break;
			case StateOfHomeWork.ANSWERING:// 答题中
			case StateOfHomeWork.ANSWERED:// 已回答
			case StateOfHomeWork.APPENDASK:// 追问中
				data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
				//sky add
				SpUtil.getInstance().setCheckTag("checked_hw_tag");
				IntentManager.goToHomeWorkCheckDetailActivity(mActivity, data, false);
//				IntentManager.goToMyCheckedHomeWorkActivity(mActivity, data, false);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
}

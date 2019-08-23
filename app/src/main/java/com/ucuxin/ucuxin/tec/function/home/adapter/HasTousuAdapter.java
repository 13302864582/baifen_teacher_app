package com.ucuxin.ucuxin.tec.function.home.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ucuxin.ucuxin.tec.function.homework.StateOfHomeWork;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.home.model.HomeworkListModel;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.DateUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;
import com.ucuxin.ucuxin.tec.utils.SpUtil;
import com.ucuxin.ucuxin.tec.utils.StirngUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.utils.glide.GlideImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 有投诉adapter
 * 
 * @author sky
 */
public class HasTousuAdapter extends BaseAdapter {
	private BaseActivity context;
	private List<HomeworkListModel> list;
	private ViewHolder viewHolder;
	private String str;

	public HasTousuAdapter(BaseActivity context, List<HomeworkListModel> list2, String str) {
		super();
		this.context = context;
		this.list = list2;
		this.str = str;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.tousu_list_item, parent, false);
			viewHolder.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
			viewHolder.tv_xuehao_value = (TextView) convertView.findViewById(R.id.tv_xuehao_value);
			viewHolder.tv_has_zhuiwen = (TextView) convertView.findViewById(R.id.tv_has_zhuiwen);
			viewHolder.ll_pingjia = (RelativeLayout) convertView.findViewById(R.id.ll_pingjia);
			viewHolder.ll_subject = (TextView) convertView.findViewById(R.id.ll_subject);
			viewHolder.card = (ConstraintLayout) convertView.findViewById(R.id.card);
			viewHolder.layout_userinfo = (ConstraintLayout) convertView.findViewById(R.id.layout_userinfo);
			viewHolder.ll_duicuo = (LinearLayout) convertView.findViewById(R.id.ll_duicuo);
			viewHolder.ll_zhengquelv = (LinearLayout) convertView.findViewById(R.id.ll_zhengquelv);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.tv_pingjia = (TextView) convertView.findViewById(R.id.tv_pingjia);
			viewHolder.tv_dui = (TextView) convertView.findViewById(R.id.tv_dui);
			viewHolder.tv_cuo = (TextView) convertView.findViewById(R.id.tv_cuo);
			viewHolder.tv_zhengquelv = (TextView) convertView.findViewById(R.id.tv_zhengquelv);
			viewHolder.iv_user_avatar = (CircleImageView) convertView.findViewById(R.id.iv_user_avatar);
			viewHolder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 处理头部显示
		final HomeworkListModel HomeworkListModel = list.get(position);
		String create_time = DateUtil.getMonthweek(HomeworkListModel.getCreate_time());
		viewHolder.tv_time.setText(create_time);

		viewHolder.layout_userinfo.setVisibility(View.VISIBLE);
		GlideImageUtils.getInstance().loadAvatarWithActivity(context, HomeworkListModel.getStudent_pic(),
				viewHolder.iv_user_avatar);
		viewHolder.tv_name.setVisibility(View.VISIBLE);
		viewHolder.tv_name.setText(HomeworkListModel.getStudent_name());
		viewHolder.tv_grade.setText(HomeworkListModel.getStudent_grade());
		viewHolder.tv_xuehao_value.setText(HomeworkListModel.getStudent_id() + "");
		viewHolder.tv_has_zhuiwen.setVisibility(View.VISIBLE);
		viewHolder.tv_has_zhuiwen.setText(str);

		viewHolder.iv_user_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentManager.gotoPersonalPage(context,  HomeworkListModel.getStudent_id(), GlobalContant.ROLE_ID_STUDENT);
			}
		});

		// 处理主要信息显示
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		final int Homework_state = HomeworkListModel.getHomework_state();
		int Questino_state = HomeworkListModel.getQuestion_state();
		int Answer_state = HomeworkListModel.getAnswer_state();
		viewHolder.tv_pingjia.setText(StirngUtil.ToSBC(HomeworkListModel.getRemark()));
		viewHolder.tv_pingjia.setTextColor(Color.parseColor("#666666"));

		String subject_name = HomeworkListModel.getSubject_name();
		if ("化学".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.hua);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.hua);
		} else if ("物理".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.li);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.li);
		} else if ("生物".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.sheng);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.sheng);
		} else if ("数学".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.shu);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.shu);
		} else if ("语文".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.yu);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.yu);
		} else if ("英语".equals(subject_name)) {
			viewHolder.ll_subject.setText(R.string.ying);
			//viewHolder.ll_subject.setBackgroundResource(R.drawable.ying);
		} else {
			viewHolder.ll_subject.setText("");
			//viewHolder.ll_subject.setBackgroundDrawable(null);
		}

		if (HomeworkListModel.getTask_type() == 1 | HomeworkListModel.getTask_type() == 2) {
			// viewHolder.iv_type.setImageResource(R.drawable.sanjx_right);

			viewHolder.iv_show.setScaleType(ScaleType.CENTER_CROP);
			viewHolder.iv_show.setBackgroundColor(Color.parseColor("#ECECEC"));

			if (HomeworkListModel.getTask_type() == 1) {// 问题
				Glide.with(context).load(HomeworkListModel.getQuestion_thumbnail()).centerCrop()
						.placeholder(R.drawable.load_img).into(viewHolder.iv_show);
			} else {// 作业
				Glide.with(context).load(HomeworkListModel.getHomewrok_thumbnail()).centerCrop()
						.placeholder(R.drawable.load_img).into(viewHolder.iv_show);
			}

			viewHolder.ll_pingjia.setBackgroundResource(R.drawable.machine_remark_bg);

			long huida_time = HomeworkListModel.getAnswer_time() - HomeworkListModel.getGrab_time();

			viewHolder.card.setClickable(true);
			viewHolder.card.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (HomeworkListModel.getTask_type() == 1) {// 问题
						MobclickAgent.onEvent(context, "MyQaDetail");
						Bundle data = new Bundle();
						data.putInt("position", 0);
						data.putLong("question_id", HomeworkListModel.getQuestion_id());
						data.putBoolean("iaqpad", true);
						SharePerfenceUtil.getInstance().putInt("msubjectid", HomeworkListModel.getSubject_id());
						IntentManager.goToAnswerDetail(context, data, 1002);

					} else {// 作业检查
						context.uMengEvent("homework_detailfrommy");
						clickIntoHomeWork(HomeworkListModel);
					}

				}
			});

			if (Homework_state > 1 && Homework_state != 8) {
				viewHolder.ll_zhengquelv.setVisibility(View.VISIBLE);
				viewHolder.tv_zhengquelv.setText(HomeworkListModel.getPercent() + "%");
			} else {
				viewHolder.ll_zhengquelv.setVisibility(View.GONE);
			}
			if (Homework_state > 0 && Homework_state != 8) {
				viewHolder.ll_duicuo.setVisibility(View.VISIBLE);
				viewHolder.tv_dui.setText("  " + HomeworkListModel.getRight_count());
				viewHolder.tv_cuo.setText("  " + HomeworkListModel.getWrong_count());
			} else {
				viewHolder.ll_duicuo.setVisibility(View.GONE);
			}

		} else {
			// 什么没发

			viewHolder.layout_userinfo.setVisibility(View.GONE);
			viewHolder.ll_duicuo.setVisibility(View.GONE);
			viewHolder.ll_zhengquelv.setVisibility(View.GONE);
			viewHolder.ll_pingjia.setBackgroundResource(R.drawable.encourage_bg);
			viewHolder.tv_pingjia.setTextColor(Color.WHITE);
			viewHolder.iv_show.setBackgroundColor(Color.parseColor("#FFFFFF"));
			viewHolder.iv_show.setScaleType(ScaleType.CENTER_INSIDE);
			viewHolder.card.setClickable(false);
		}
		return convertView;
	}

	private void clickIntoHomeWork(HomeworkListModel homeworkListModel) {
		context.showDialog("请稍后");
		JSONObject data = new JSONObject();
		try {
			data.put("taskid", homeworkListModel.getTask_id());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(context, "homework","getone", data, new HttpListener() {
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				context.closeDialogHelp();
				try {
					if (code == 0) {
						HomeWorkModel mHomeWorkModel = JSON.parseObject(dataJson, HomeWorkModel.class);
						Bundle data = new Bundle();
						if("有投诉".equals(str)){
						data.putInt("position", 0);
						data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
						// sky add
						SpUtil.getInstance().setCheckTag("checked_hw_tag");
						IntentManager.goToHomeWorkCheckDetailActivity(context, data, false);
						}else{
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
								IntentManager.goToStuHomeWorkDetailActivity(context, data, false);
								break;
							case StateOfHomeWork.ANSWERING:// 答题中
							case StateOfHomeWork.ANSWERED:// 已回答
							case StateOfHomeWork.APPENDASK:// 追问中
								data.putSerializable(HomeWorkModel.TAG, mHomeWorkModel);
								SpUtil.getInstance().setCheckTag("checked_hw_tag");
								IntentManager.goToHomeWorkCheckDetailActivity(context, data, false);
								break;
							}
						}
						
						

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
				context.closeDialogHelp();

			}

		});

	}

	private class ViewHolder {
		ConstraintLayout layout_userinfo;
		LinearLayout ll_duicuo, ll_zhengquelv;
		TextView tv_time, tv_name, tv_grade, tv_xuehao_value, tv_has_zhuiwen, tv_pingjia, tv_dui, tv_cuo, tv_zhengquelv,ll_subject;
		ImageView iv_show;
		CircleImageView iv_user_avatar;
		RelativeLayout ll_pingjia;
		ConstraintLayout card;
	}

}

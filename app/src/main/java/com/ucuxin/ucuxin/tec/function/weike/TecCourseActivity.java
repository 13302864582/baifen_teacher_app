package com.ucuxin.ucuxin.tec.function.weike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CourseModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.util.ArrayList;

/**
 * 微课辅导
 * @author:  sky
 */
public class TecCourseActivity extends BaseActivity implements OnClickListener {
	private CourseAdapter mAdapter;
	private ArrayList<CourseModel> mCourseModels;
	// private ArrayList<CheckAsk> checkAsks;
	private View noDataView;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.tec_course_activity);
		setWelearnTitle(R.string.tec_course_title_text);
		findViewById(R.id.back_layout).setOnClickListener(this);

		findViewById(R.id.next_setp_layout).setOnClickListener(this);

		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.add_course_title_text);

		noDataView = findViewById(R.id.nodata_view_course);
		ListView courseLv = (ListView) findViewById(R.id.course_lv_course);
		mAdapter = new CourseAdapter();
		courseLv.setAdapter(mAdapter);

		loadData();
	}

	private void loadData() {
		OkHttpHelper.post(this, "course", "mycourse",null, new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (!TextUtils.isEmpty(dataJson)) {
					try {
						mCourseModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CourseModel>>() {
						}.getType());
					} catch (Exception e) {
					}
					if (mCourseModels != null && !mCourseModels.isEmpty()) {
						setHavaData();
					} else {
						setNullData();
					}
				} else {
					setNullData();
				}		
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {
				
				
			}
		});
	}

	private void setNullData() {
		noDataView.setVisibility(View.VISIBLE);
		mCourseModels = null;
		mAdapter.notifyDataSetChanged();
	}

	private void setHavaData() {
		noDataView.setVisibility(View.GONE);
		// checkAsks = null;

		OkHttpHelper.post(this, "course","checkask",null, new HttpListener() {			
			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				if (!TextUtils.isEmpty(dataJson)) {
					ArrayList<CheckAsk> checkAsks = null;
					try {
						checkAsks = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CheckAsk>>() {
						}.getType());
					} catch (Exception e) {
					}
					int size = mCourseModels.size();
					if (checkAsks != null && !checkAsks.isEmpty()) {
						for (int i = 0; i < checkAsks.size(); i++) {
							CheckAsk checkAsk = checkAsks.get(i);
							int todo = checkAsk.getTodo();
							int courseid = checkAsk.getCourseid();
							if (i < size) {
								CourseModel courseModel = mCourseModels.get(i);
								if (courseModel.getCourseid() == courseid) {
									courseModel.setTodo(todo);
								}
							}
						}
					}
				}
				mAdapter.setData(mCourseModels);
			
				
			}
			
			@Override
			public void onFail(int HttpCode,String errMsg) {

				
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:// 添加课程
			IntentManager.goToAddCourseActivity(this, null, false);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:// 添加课程回来
				loadData();

				break;
			case 1003:// 上传课时回来
				loadData();
				break;
			case 1004:// 学生列表回来
				loadData();
				break;

			default:
				break;
			}
		}
	}

	private class CourseAdapter extends BaseAdapter implements OnClickListener {
		private ArrayList<CourseModel> courseModels;
		private CourseHolder visibleHolder;
		private int visiblePosition = -1;

		public void setData(ArrayList<CourseModel> courseModels) {
			this.courseModels = courseModels;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return courseModels == null ? 0 : courseModels.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup viewGroup) {
			CourseHolder holder = null;
			if (view == null) {
				holder = new CourseHolder();
				view = View.inflate(TecCourseActivity.this, R.layout.course_item, null);
				view.setTag(holder);
				holder.bodyContainer = view.findViewById(R.id.body_container_course_item);
				holder.gradeTv = (TextView) view.findViewById(R.id.grade_tv_course_item);
				holder.subjectTv = (TextView) view.findViewById(R.id.subject_tv_course_item);
				holder.courserNameTv = (TextView) view.findViewById(R.id.coursename_tv_course_item);
				holder.clickIv = (ImageView) view.findViewById(R.id.click_iv_course_item);

				holder.bottomContainer = view.findViewById(R.id.bottom_container_course_item);
				holder.doneNumTv = (TextView) view.findViewById(R.id.num_tv_course_item);
				holder.totalNumTv = (TextView) view.findViewById(R.id.total_tv_course_item);
				holder.saleNumTv = (TextView) view.findViewById(R.id.num_of_sale_tv_course_item);
				holder.remindIv = view.findViewById(R.id.remind_iv_course_item);
				holder.remind1Iv = view.findViewById(R.id.remind1_iv_course_item);

				holder.courseContainer = view.findViewById(R.id.course_info_container_course_item);
				holder.classContainer = view.findViewById(R.id.class_container_course_item);
				holder.studentContainer = view.findViewById(R.id.student_container_course_item);

				holder.bodyContainer.setTag(holder);
				// viewList.add(holder.bottomContainer);

				holder.bodyContainer.setOnClickListener(this);
				holder.courseContainer.setOnClickListener(this);
				holder.classContainer.setOnClickListener(this);
				holder.studentContainer.setOnClickListener(this);

			} else {
				holder = (CourseHolder) view.getTag();
			}
			holder.position = position;
			CourseModel courseModel = courseModels.get(position);
			if (courseModel != null) {
				int todo = courseModel.getTodo();
				if (todo == 1) {
					holder.remindIv.setVisibility(View.VISIBLE);
					holder.remind1Iv.setVisibility(View.VISIBLE);
				} else {
					holder.remindIv.setVisibility(View.GONE);
					holder.remind1Iv.setVisibility(View.GONE);
				}
//				holder.classContainer.setTag(courseModel);
//				holder.studentContainer.setTag(courseModel);

				if (visiblePosition == position) {
					setCatalogVisible(holder);
				}else {
					setCatalogGone(holder);
				}
				String coursename = courseModel.getCoursename();
				String grade = courseModel.getGrade();
				String subject = courseModel.getSubject();
				int charptercount = courseModel.getCharptercount();
				int uploadcount = courseModel.getUploadcount();
				int salecount = courseModel.getSalecount();

				holder.gradeTv.setText(grade);
				holder.subjectTv.setText(subject);
				holder.courserNameTv.setText(coursename);
				holder.totalNumTv.setText(charptercount + "");
				holder.doneNumTv.setText(uploadcount + "");
				holder.saleNumTv.setText(salecount + "");
			} else {
				holder.gradeTv.setText("");
				holder.subjectTv.setText("");
				holder.courserNameTv.setText("");
				holder.totalNumTv.setText("");
				holder.doneNumTv.setText("");
				holder.saleNumTv.setText("");
			}
			return view;
		}

		void setCatalogVisible(CourseHolder holder) {
			holder.clickIv.setImageResource(R.drawable.class_catalog_img);
			holder.bottomContainer.setVisibility(View.VISIBLE);
			visibleHolder = holder;
			visiblePosition = holder.position;
		}

		void setCatalogGone(CourseHolder holder) {
			holder.clickIv.setImageResource(R.drawable.click_icon);
			holder.bottomContainer.setVisibility(View.GONE);
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.body_container_course_item:
				CourseHolder holder = (CourseHolder) view.getTag();
				if (visiblePosition == -1) {
					setCatalogVisible(holder);
				}else  {
					setCatalogGone(visibleHolder);
					if (visiblePosition == holder.position){
						visiblePosition = -1;
						visibleHolder = null;
					}else {
						setCatalogVisible(holder);
					}
				}
			
				
//				if (visibleHolder == null) {
//					setCatalogVisible(holder);
//				} else {
//					setCatalogGone(visibleHolder);
//					if (visibleHolder == holder) {
//						visibleHolder = null;
//					} else {
//						setCatalogVisible(holder);
//					}
//				}
				break;
			case R.id.course_info_container_course_item://点击进入查看课程信息
				if (visiblePosition < courseModels.size()) {
					CourseModel courseModel = courseModels.get(visiblePosition);
					if (courseModel != null) {
						int courseid = courseModel.getCourseid();
						IntentManager.goToCourseDetailActivity(TecCourseActivity.this, courseid);
					}
				}
				break;
			case R.id.class_container_course_item:
				if (visiblePosition < courseModels.size()) {
					CourseModel courseModel = courseModels.get(visiblePosition);
					if (courseModel != null) {
						int courseid = courseModel.getCourseid();
						int charptercount = courseModel.getCharptercount();
						int gradeid = courseModel.getGradeid();
						int subjectid = courseModel.getSubjectid();
						IntentManager.goToCourseCatalogActivity(TecCourseActivity.this, courseid, charptercount, gradeid,
								subjectid);
					}
				}
				break;
			case R.id.student_container_course_item:
				if (visiblePosition < courseModels.size()) {
					CourseModel	courseModel = courseModels.get(visiblePosition);//(CourseModel) view.getTag();
					if (courseModel != null) {
						int courseid = courseModel.getCourseid();
						int salecount = courseModel.getSalecount();
						if (salecount == 0) {
							ToastUtils.show("暂时没有学生购买您的课程噢！");
						} else {
							if (courseid != 0) {
								IntentManager.goToPurchaseStudentActivity(TecCourseActivity.this, courseid);
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}

	static class CourseHolder {
		int position;
		ImageView clickIv;
		TextView doneNumTv;
		TextView totalNumTv;
		TextView saleNumTv;
		TextView gradeTv;
		TextView subjectTv;
		TextView courserNameTv;
		View bodyContainer;
		View bottomContainer;
		View courseContainer;
		View classContainer;
		View studentContainer;
		View remindIv;
		View remind1Iv;
	}

	class CheckAsk {
		int courseid;
		int todo;

		public int getCourseid() {
			return courseid;
		}

		public void setCourseid(int courseid) {
			this.courseid = courseid;
		}

		public int getTodo() {
			return todo;
		}

		public void setTodo(int todo) {
			this.todo = todo;
		}

		@Override
		public String toString() {
			return "CheckAsk [courseid=" + courseid + ", todo=" + todo + "]";
		}

	}

}

package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 作业列表实体
 * @author Administrator
 *
 */
public class HomeworkListModel implements Serializable {

	private static final long serialVersionUID = -4968247882796366734L;

	private long create_time;
	private long answer_time;
	private int percent;
	private int question_id;
	private String question_thumbnail;
	private String homewrok_thumbnail;
	private int question_state;

	private int homework_state;
	private int answer_state;
	private long grab_time;
	private int right_count;
	private int avg_cost_time;
	private int state;
	private int task_id;
	private int task_type;
	private int teacher_id;
	private String teacher_name;
	private String teacher_pic;
	private String remark;
	private String subject_name;
	private int wrong_count;
	private int is_today;
	private int subject_id;
	
	private int student_id;
	private String student_pic;
	private String student_name;
	private String student_grade;
	
	
	
	
	
	
	

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getStudent_pic() {
		return student_pic;
	}

	public void setStudent_pic(String student_pic) {
		this.student_pic = student_pic;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getStudent_grade() {
		return student_grade;
	}

	public void setStudent_grade(String student_grade) {
		this.student_grade = student_grade;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

	public int getIs_today() {
		return is_today;
	}

	public void setIs_today(int is_today) {
		this.is_today = is_today;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public int getQuestion_state() {
		return question_state;
	}

	public void setQuestion_state(int question_state) {
		this.question_state = question_state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public long getAnswer_time() {
		return answer_time;
	}

	public void setAnswer_time(long answer_time) {
		this.answer_time = answer_time;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_thumbnail() {
		return question_thumbnail;
	}

	public void setQuestion_thumbnail(String question_thumbnail) {
		this.question_thumbnail = question_thumbnail;
	}

	public String getHomewrok_thumbnail() {
		return homewrok_thumbnail;
	}

	public void setHomewrok_thumbnail(String homewrok_thumbnail) {
		this.homewrok_thumbnail = homewrok_thumbnail;
	}

	public int getHomework_state() {
		return homework_state;
	}

	public void setHomework_state(int homework_state) {
		this.homework_state = homework_state;
	}

	public int getAnswer_state() {
		return answer_state;
	}

	public void setAnswer_state(int answer_state) {
		this.answer_state = answer_state;
	}

	public long getGrab_time() {
		return grab_time;
	}

	public void setGrab_time(long grab_time) {
		this.grab_time = grab_time;
	}

	public int getRight_count() {
		return right_count;
	}

	public void setRight_count(int right_count) {
		this.right_count = right_count;
	}

	public int getAvg_cost_time() {
		return avg_cost_time;
	}

	public void setAvg_cost_time(int avg_cost_time) {
		this.avg_cost_time = avg_cost_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public int getTask_type() {
		return task_type;
	}

	public void setTask_type(int task_type) {
		this.task_type = task_type;
	}

	public int getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getTeacher_pic() {
		return teacher_pic;
	}

	public void setTeacher_pic(String teacher_pic) {
		this.teacher_pic = teacher_pic;
	}

	public int getWrong_count() {
		return wrong_count;
	}

	public void setWrong_count(int wrong_count) {
		this.wrong_count = wrong_count;
	}

}

package com.ucuxin.ucuxin.tec.function.teccourse.model;

import java.io.Serializable;

public class CourseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914966170169129558L;
	public static final String TAG = CourseModel.class.getSimpleName();
	private int courseid;
	private String coursename;
	private String content;
	private String grade;
	private int gradeid;
	private String subject;
	private int subjectid;
	private long datatime;
	private float price;
	private int charptercount;
	private int salecount;
	private int uploadcount;
	private int todo;

	public int getCourseid() {
		return courseid;
	}

	public void setCourseid(int courseid) {
		this.courseid = courseid;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeid() {
		return gradeid;
	}

	public void setGradeid(int gradeid) {
		this.gradeid = gradeid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCharptercount() {
		return charptercount;
	}

	public void setCharptercount(int charptercount) {
		this.charptercount = charptercount;
	}

	public int getSalecount() {
		return salecount;
	}

	public void setSalecount(int salecount) {
		this.salecount = salecount;
	}

	@Override
	public String toString() {
		return "CourseModel [courseid=" + courseid + ", coursename=" + coursename + ", content=" + content + ", grade="
				+ grade + ", gradeid=" + gradeid + ", subject=" + subject + ", subjectid=" + subjectid + ", datatime="
				+ datatime + ", price=" + price + ", charptercount=" + charptercount + ", salecount=" + salecount
				+ ", uploadcount=" + uploadcount + ", todo=" + todo + "]";
	}

	public int getUploadcount() {
		return uploadcount;
	}

	public void setUploadcount(int uploadcount) {
		this.uploadcount = uploadcount;
	}

	public int getTodo() {
		return todo;
	}

	public void setTodo(int todo) {
		this.todo = todo;
	}

}

package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GradeModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int gradeId;
	private String name;
	private String subjects;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public ArrayList<Integer> getSubjectIds() {
		if (null == subjects) {
			return null;
		} else {
			String[] subStrList = subjects.split(",");
			if (null != subStrList && subStrList.length > 0) {
				ArrayList<Integer> subIds = new ArrayList<Integer>();
				for (String s : subStrList) {
					subIds.add(Integer.parseInt(s));
				}
				return subIds;
			} else {
				return null;
			}
		}
	}

}

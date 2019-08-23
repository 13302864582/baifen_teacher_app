package com.ucuxin.ucuxin.tec.function.teccourse.model;

import java.io.Serializable;

public class EduPartnerModel implements Serializable{
	
   /*
    "userid":学号,
    "grade":年级,
    "name":名字,
    "avatar":头像URL,
    "schools":学校,
    "relationtype":关系,     #1关注  2会员
    "namepinyin":首字母缩写
   */

	private static final long serialVersionUID = -914966170169129558L;
	
	public static final int TYPE_VIP = 2;
	public static final int TYPE_NORMAL = 1;
	
	private int userid;
	private String grade;
	private String name;
	private String avatar;
	private String schools;
	private int relationtype;
	private String namepinyin;
	public EduPartnerModel() {
		super();
	}
	public EduPartnerModel(int userid, String grade, String name, String avatar, String schools, int relationtype,
			String namepinyin) {
		super();
		this.userid = userid;
		this.grade = grade;
		this.name = name;
		this.avatar = avatar;
		this.schools = schools;
		this.relationtype = relationtype;
		this.namepinyin = namepinyin;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getSchools() {
		return schools;
	}
	public void setSchools(String schools) {
		this.schools = schools;
	}
	public int getRelationtype() {
		return relationtype;
	}
	public void setRelationtype(int relationtype) {
		this.relationtype = relationtype;
	}
	public String getNamepinyin() {
		return namepinyin;
	}
	public void setNamepinyin(String namepinyin) {
		this.namepinyin = namepinyin;
	}
}

package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 首页model
 * @author Administrator
 *
 */
public class HomepageModel implements Serializable {

	private static final long serialVersionUID = -4308094207073433944L;
	private String available;// 老师可提现学点(包括公式)
	private float credit;// 老师信用
	private String avatar;// 老师头像URL
	private float gold;// 老师总学点
	private String name;// 老师名字
	private int userid;// 老师学号
	private int responsibility_index;//老师责任心指数	
	
	

	public int getResponsibility_index() {
		return responsibility_index;
	}

	public void setResponsibility_index(int responsibility_index) {
		this.responsibility_index = responsibility_index;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public float getGold() {
		return gold;
	}

	public void setGold(float gold) {
		this.gold = gold;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}

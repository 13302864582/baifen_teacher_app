package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 我的分享model
 * 
 * @author sky
 *
 */
public class MyShareModel implements Serializable {

	private static final long serialVersionUID = 6450204964904662977L;
	private String usertel;
	private String state;
	private float award;

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public float getAward() {
		return award;
	}

	public void setAward(float award) {
		this.award = award;
	}

}

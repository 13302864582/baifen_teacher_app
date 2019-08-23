package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 提现记录model
 * 
 * @author sky
 *
 */
public class TixianRecordModel implements Serializable {

	private static final long serialVersionUID = -3581976463040196994L;
	private String tradetime;// 提现时间
	private String summary;
	private float spending;// 提现金额
	private int status;// 提现状态 0待审核 1审核中 2已完成

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTradetime() {
		return tradetime;
	}

	public void setTradetime(String tradetime) {
		this.tradetime = tradetime;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public float getSpending() {
		return spending;
	}

	public void setSpending(float spending) {
		this.spending = spending;
	}

}

package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 提现金额
 * 
 * @author sky
 *
 */
public class TixianModel implements Serializable {

	private static final long serialVersionUID = -3581976463040196994L;
	private String tradetime;
	private String summary;
	private float income;
	private int taskid;
	private int tasktype;
	
	
	
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getTasktype() {
		return tasktype;
	}
	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
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
	public float getIncome() {
		return income;
	}
	public void setIncome(float income) {
		this.income = income;
	}
	
	

	
}

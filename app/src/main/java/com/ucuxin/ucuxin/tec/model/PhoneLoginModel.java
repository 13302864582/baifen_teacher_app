package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;

public class PhoneLoginModel extends LoginModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 账号, # 学号 或者 手机号码 */
	private String count;
	/** 密码 */
	private String password;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;

public class LoginModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int OS_ANDROID = 1;

	private static final String SOURCE_APP = "APP";
	
	private static final String PHONE_OS_ANDROID = "android";

	/** WEB/APP", # WEB,网站登录 */
	protected String source = SOURCE_APP;
	/** phone_model",#1.3新增 */
	protected String phonemodel = android.os.Build.MODEL;
	/** provice */
	private String province;
	/** city */
	private String city;
	/** 1/2 # android返回1 ios返回2 */
	protected int os = OS_ANDROID;
    
	protected String phoneos =PHONE_OS_ANDROID;	
	
		
	
	
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPhonemodel() {
		return phonemodel;
	}

	public void setPhonemodel(String phonemodel) {
		this.phonemodel = phonemodel;
	}

	public int getOs() {
		return os;
	}

	public void setOs(int os) {
		this.os = os;
	}

	public String getPhoneos() {
		return phoneos;
	}

	public void setPhoneos(String phoneos) {
		this.phoneos = phoneos;
	}

	public static int getOsAndroid() {
		return OS_ANDROID;
	}

	public static String getSourceApp() {
		return SOURCE_APP;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}

package com.ucuxin.ucuxin.tec.function.homework.model;

import java.io.Serializable;

/**
 * 投诉理由model
 * @author Administrator
 *
 */
public class TousuModel implements Serializable {
	
	
	
	private static final long serialVersionUID = 6102138945305993395L;
	private int type;
	private String content;
	
	
	
	
	
	public TousuModel() {
		super();

	}
	public TousuModel(int type, String content) {
		super();
		this.type = type;
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	

}

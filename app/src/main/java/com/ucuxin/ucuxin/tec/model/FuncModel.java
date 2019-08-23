package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;

public class FuncModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int bgResId;
	private String name;
	private String className;

	public FuncModel(int bg, String name, String clsName) {
		this.bgResId = bg;
		this.name = name;
		this.className = clsName;
	}

	public FuncModel() {
	}

	public int getBgResId() {
		return bgResId;
	}

	public void setBgResId(int bgResId) {
		this.bgResId = bgResId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}

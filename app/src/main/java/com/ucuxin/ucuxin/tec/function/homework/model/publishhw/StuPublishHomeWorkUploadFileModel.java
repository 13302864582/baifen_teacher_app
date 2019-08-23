package com.ucuxin.ucuxin.tec.function.homework.model.publishhw;

import java.io.Serializable;

public class StuPublishHomeWorkUploadFileModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int FILE_TYPE_IMAGE = 1;
	public static final int FILE_TYPE_SOUND = 2;

	/** 图片顺序, # 1~n */
	private int picseqid;

	/** 图片说明 */
	private String memo;

	/** 图片宽度 */
	private int width;

	/** 图片高度 */
	private int height;

	public int getPicseqid() {
		return picseqid;
	}

	public void setPicseqid(int picseqid) {
		this.picseqid = picseqid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}

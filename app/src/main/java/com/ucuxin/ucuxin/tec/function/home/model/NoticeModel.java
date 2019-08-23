package com.ucuxin.ucuxin.tec.function.home.model;

import java.io.Serializable;

/**
 * 通知model
 * 
 * @author Administrator
 *
 */
public class NoticeModel implements Serializable {

	private static final long serialVersionUID = 152986194020646683L;
	private int id = 0;
	private String title = "";
	private String content;
	private long timestamp;

	private int readState = 0;

	public NoticeModel() {
		super();

	}

	public NoticeModel(int id, String title, String content, long timestamp, int readState) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.timestamp = timestamp;
		this.readState = readState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

}

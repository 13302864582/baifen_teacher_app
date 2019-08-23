package com.ucuxin.ucuxin.tec.function.homework.model.publishhw;

import java.io.Serializable;
import java.util.ArrayList;

public class UpLoadCheckPointModel implements Serializable {
	public static final String TAG = UpLoadCheckPointModel.class.getSimpleName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 8813912137357284900L;


	/** 检查点唯一ID */
	private int checkpointid;
	
	/** 顺序ID, # 从1开始 */
	private int picid;
	
	/** 顺序ID, # 从1开始 */
	private int cpseqid;
	
	/** 对错 */
	private int isright;

	/** 坐标 */
	private String coordinate;

	
	/** 知识点 */
	private String wrongtype;
	
	/** 知识点 */
	private String kpoint;
	

	
	public String getWrongtype() {
		return wrongtype;
	}

	public void setWrongtype(String wrongtype) {
		this.wrongtype = wrongtype;
	}

	public String getKpoint() {
		return kpoint;
	}

	public void setKpoint(String kpoint) {
		this.kpoint = kpoint;
	}

	/**
	 * 单点列表
	 */
	private ArrayList<UpLoadEXPointModel> explainlist;
	
	private int complaint_type;//投诉类型
	
	private  int sub_type;//投诉子类型
	
	




	public int getComplaint_type() {
		return complaint_type;
	}

	public void setComplaint_type(int complaint_type) {
		this.complaint_type = complaint_type;
	}

	public int getSub_type() {
		return sub_type;
	}

	public void setSub_type(int sub_type) {
		this.sub_type = sub_type;
	}

	public int getCheckpointid() {
		return checkpointid;
	}

	public void setCheckpointid(int checkpointid) {
		this.checkpointid = checkpointid;
	}

	public int getPicid() {
		return picid;
	}

	public void setPicid(int picid) {
		this.picid = picid;
	}

	public int getCpseqid() {
		return cpseqid;
	}

	public void setCpseqid(int cpseqid) {
		this.cpseqid = cpseqid;
	}

	public int getIsright() {
		return isright;
	}

	public void setIsright(int isright) {
		this.isright = isright;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public ArrayList<UpLoadEXPointModel> getExplainlist() {
		return explainlist;
	}

	public void setExplainlist(ArrayList<UpLoadEXPointModel> explainlist) {
		this.explainlist = explainlist;
	}

	@Override
	public String toString() {
		return "UpLoadCheckPointModel [checkpointid=" + checkpointid + ", picid=" + picid + ", cpseqid=" + cpseqid
				+ ", isright=" + isright + ", coordinate=" + coordinate + ", wrongtype=" + wrongtype + ", kpoint="
				+ kpoint + ", explainlist=" + explainlist + "]";
	}

}

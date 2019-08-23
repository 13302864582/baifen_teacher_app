package com.ucuxin.ucuxin.tec.function.teccourse.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CharpterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7193563718023620681L;
	public static final String TAG = CharpterModel.class.getSimpleName();
	private int charpterid;
	private String charptername;
	private int watchcount;
	private String kpoint;
	private long datatime;
	private ArrayList<CoursePageModel> page;

	public int getCharpterid() {
		return charpterid;
	}

	public void setCharpterid(int charpterid) {
		this.charpterid = charpterid;
	}

	public String getCharptername() {
		return charptername;
	}

	public void setCharptername(String charptername) {
		this.charptername = charptername;
	}

	public int getWatchcount() {
		return watchcount;
	}

	public void setWatchcount(int watchcount) {
		this.watchcount = watchcount;
	}

	public String getKpoint() {
		return kpoint;
	}

	public void setKpoint(String kpoint) {
		this.kpoint = kpoint;
	}

	@Override
	public String toString() {
		return "ClassHourModel [charpterid=" + charpterid + ", charptername=" + charptername + ", watchcount="
				+ watchcount + ", kpoint=" + kpoint + ", datatime=" + datatime + ", page=" + page + "]";
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public ArrayList<CoursePageModel> getPage() {
		return page;
	}

	public void setPage(ArrayList<CoursePageModel> page) {
		this.page = page;
	}

	public boolean equals(CharpterModel equalsModel){
		boolean flag = false;
		if (equalsModel != null) {
			if (this == equalsModel) {
				flag = true;
			}else if(charptername.equals(equalsModel.getCharptername()) && kpoint.equals(equalsModel.getKpoint())){
				int size = page == null ? 0 : page.size();
				int equalsSize = equalsModel.getPage()== null ? 0 : equalsModel.getPage().size();
				if (size == equalsSize) {
					flag = true;
				}
			}
		}
		return flag;
	}
}

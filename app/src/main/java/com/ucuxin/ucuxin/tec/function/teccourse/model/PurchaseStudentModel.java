package com.ucuxin.ucuxin.tec.function.teccourse.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PurchaseStudentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3822986236111945360L;
	public static final String TAG = PurchaseStudentModel.class.getSimpleName();
	private int userid;
	private String avatar;
	private String name;
	private int sex;
	private long datatime;
	private float price;
	private int process;
	private long lasttime;
	private int todo;//此学生是否有追问    0 没有 1有
	private ArrayList<Charpter> charpter ;
	

	public ArrayList<Charpter> getCharpter() {
		return charpter;
	}
	public void setCharpter(ArrayList<Charpter> charpter) {
		this.charpter = charpter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getDatatime() {
		return datatime;
	}
	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public long getLasttime() {
		return lasttime;
	}
	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}
	@Override
	public String toString() {
		return "PurchaseStudentModel [userid=" + userid + ", avatar=" + avatar + ", name=" + name + ", sex=" + sex
				+ ", datatime=" + datatime + ", price=" + price + ", process=" + process + ", lasttime=" + lasttime
				+ ", todo=" + todo + ", charpter=" + charpter + "]";
	}
	public int getTodo() {
		return todo;
	}
	public void setTodo(int todo) {
		this.todo = todo;
	}

	public class Charpter {
		private int charpterid;
		private int charpterindex;
		private int pageid;
		private int pageindex;
		/** 追问时间*/
		private long datatime;
		private String charptername;
		private String imgurl;
		public int getCharpterid() {
			return charpterid;
		}
		public void setCharpterid(int charpterid) {
			this.charpterid = charpterid;
		}
		public int getCharpterindex() {
			return charpterindex;
		}
		public void setCharpterindex(int charpterindex) {
			this.charpterindex = charpterindex;
		}
		public int getPageid() {
			return pageid;
		}
		public void setPageid(int pageid) {
			this.pageid = pageid;
		}
		public int getPageindex() {
			return pageindex;
		}
		public void setPageindex(int pageindex) {
			this.pageindex = pageindex;
		}
		public long getDatatime() {
			return datatime;
		}
		public void setDatatime(long datatime) {
			this.datatime = datatime;
		}
		public String getCharptername() {
			return charptername;
		}
		public void setCharptername(String charptername) {
			this.charptername = charptername;
		}
		public String getImgurl() {
			return imgurl;
		}
		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}
		@Override
		public String toString() {
			return "Charpter [charpterid=" + charpterid + ", charpterindex=" + charpterindex + ", pageid=" + pageid
					+ ", pageindex=" + pageindex + ", datatime=" + datatime + ", charptername=" + charptername
					+ ", imgurl=" + imgurl + "]";
		}
		
	}

}

package com.ucuxin.ucuxin.tec.function.homework.model.publishhw;

import java.io.Serializable;

public class StuPublishHomeWorkModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 任务类型, # 1问答 2作业 */
	private int tasktype;
	/** pay方式, # 1悬赏 2包月 3自建 */
	private int paytype;
	/** 文字说明 */
	private String memo;
	/** 发题人ID */
	private int studid;
	/** 发题人姓名 */
	private String studname;
	/** 科目 */
	private int subjectid;
	/** 年级 */
	private int gradeid;
	/** 赏金 */
	private float bounty;
	/** 发布时间, # 后台注意, 实际时间以服务器插入作业记录为准 */
	private String datatime;
	
//	/** 储存图片列表 */
//	private ArrayList<StuPublishHomeWorkPageModel> pagelist;

	public int getTasktype() {
		return tasktype;
	}

	public void setTasktype(int tasktype) {
		this.tasktype = tasktype;
	}

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getStudid() {
		return studid;
	}

	public void setStudid(int studid) {
		this.studid = studid;
	}

	public String getStudname() {
		return studname;
	}

	public void setStudname(String studname) {
		this.studname = studname;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public int getGradeid() {
		return gradeid;
	}

	public void setGradeid(int gradeid) {
		this.gradeid = gradeid;
	}

	public float getBounty() {
		return bounty;
	}

	public void setBounty(float bounty) {
		this.bounty = bounty;
	}

	public String getDatatime() {
		return datatime;
	}

	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}

//	public ArrayList<StuPublishHomeWorkPageModel> getPagelist() {
//		return pagelist;
//	}

//	public void setPagelist(ArrayList<StuPublishHomeWorkPageModel> pagelist) {
//		this.pagelist = pagelist;
//	}

}

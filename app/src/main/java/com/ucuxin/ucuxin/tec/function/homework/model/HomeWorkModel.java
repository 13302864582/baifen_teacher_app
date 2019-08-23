package com.ucuxin.ucuxin.tec.function.homework.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作业实体类
 * @author:  sky
 */
public class HomeWorkModel implements Serializable {
	public static final String TAG = HomeWorkModel.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	
	/** 作业ID */
	private int taskid;
	
	/** 任务类型, # 1问答 2作业 */
	private int tasktype;
	
	/** pay类型, 1悬赏，2包月，3自建，4机构，5外包，6家长 */
	private int paytype;
	
	/** 文字描述 */
	private String memo;
	
	/** 作业状态 */
	private int state;
	
	/** 发布时间 */
	private long datatime;
	
	/** 回答时间 */
	private long answertime;
	
	/** 确认时间 */
	private long confirmtime;
	
	/** 限制时间 分钟 */
	private int limittime;
	
	/** 抢题时间  */
	private long grabtime;
	/** 发题者ID */
	private int studid;
	
	/** 发题者姓名 */
	private String studname;
	
	/** 发题者头像url */
	private String avatar;
	
	/** 发题者年级 */
	private String grade;
	
	/** 发题者年级ID */
	private int gradeid;
	
	/** 科目 */
	private String subject;
	
	/** 科目ID */
	private int subjectid;
	
	/** 悬赏金额 */
	private float bounty;
	
	/** 系统补助, # 注意最后给到老师的金额为 bounty+givegold */
	private float givegold;
	
	/** 老师ID */
	private int grabuserid;
	
	/** 是否为新人作业, # 0 1 */
	private int isnew;
	
	/** 是否vip用户, # 0非vip  大于0为VIP等级 */
	private int supervip;
	
	/** 是否抢过, # 0没有  非0抢过 */
	private int grabed;

	/** 确认人ID, # 用户 或者 系统 */
	private int confirmuserid;
	
	/** 作业是否被赞过 */
	private int praise;
	
	/** 点赞次数 */
	private int praisecnt;
	
	/** 作业星级*/
	private int satisfaction;
	
	/** 提问用户信用 */
	private int credit;
	
	/** 发布作业次数 */
	private int homeworkcnt;
	
	/** 老师姓名 */
	private String teachername;
	
	/** 老师头像url */
	private String teacheravatar;
	
	/** 老师解答次数 */
	private int teacherhomeworkcnt;
	
	/** 作业页列表 */
	private ArrayList<StuPublishHomeWorkPageModel> pagelist;
	
	
	private int orgid;
	
	
	 
	private String vip_level_content;//vip 等级
	private String  vip_additional_content;//额外奖励信息
	private String bottom_tip;//底部提示(以{}分开)(2016/01/21新加)
	
	private String bottomtip;//进入抢题成功后的提示
	
	
	
	
	
	
	
	
	

	public String getBottomtip() {
		return bottomtip;
	}

	public void setBottomtip(String bottomtip) {
		this.bottomtip = bottomtip;
	}

	public String getVip_level_content() {
		return vip_level_content;
	}

	public void setVip_level_content(String vip_level_content) {
		this.vip_level_content = vip_level_content;
	}

	public String getVip_additional_content() {
		return vip_additional_content;
	}

	public void setVip_additional_content(String vip_additional_content) {
		this.vip_additional_content = vip_additional_content;
	}





	public String getBottom_tip() {
		return bottom_tip;
	}

	public void setBottom_tip(String bottom_tip) {
		this.bottom_tip = bottom_tip;
	}

	public int getHomeworkcnt() {
		return homeworkcnt;
	}

	public void setHomeworkcnt(int homeworkcnt) {
		this.homeworkcnt = homeworkcnt;
	}

	public String getTeachername() {
		return teachername;
	}

	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}

	public String getTeacheravatar() {
		return teacheravatar;
	}

	public void setTeacheravatar(String teacheravatar) {
		this.teacheravatar = teacheravatar;
	}

	public int getTeacherhomeworkcnt() {
		return teacherhomeworkcnt;
	}

	public void setTeacherhomeworkcnt(int teacherhomeworkcnt) {
		this.teacherhomeworkcnt = teacherhomeworkcnt;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getDatatime() {
		return datatime;
	}

	public void setDatatime(long datatime) {
		this.datatime = datatime;
	}

	public long getAnswertime() {
		return answertime;
	}

	public void setAnswertime(long answertime) {
		this.answertime = answertime;
	}

	public long getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(long confirmtime) {
		this.confirmtime = confirmtime;
	}

	public int getLimittime() {
		return limittime;
	}

	public void setLimittime(int limittime) {
		this.limittime = limittime;
	}

	public long getGrabtime() {
		return grabtime;
	}

	public void setGrabtime(long grabtime) {
		this.grabtime = grabtime;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getGradeid() {
		return gradeid;
	}

	public void setGradeid(int gradeid) {
		this.gradeid = gradeid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

	public float getBounty() {
		return bounty;
	}

	public void setBounty(float bounty) {
		this.bounty = bounty;
	}

	public float getGivegold() {
		return givegold;
	}

	public void setGivegold(float givegold) {
		this.givegold = givegold;
	}

	public int getGrabuserid() {
		return grabuserid;
	}

	public void setGrabuserid(int grabuserid) {
		this.grabuserid = grabuserid;
	}

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	public int getSupervip() {
		return supervip;
	}

	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}

	public int getGrabed() {
		return grabed;
	}

	public void setGrabed(int grabed) {
		this.grabed = grabed;
	}

	public int getConfirmuserid() {
		return confirmuserid;
	}

	public void setConfirmuserid(int confirmuserid) {
		this.confirmuserid = confirmuserid;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public int getPraisecnt() {
		return praisecnt;
	}

	public void setPraisecnt(int praisecnt) {
		this.praisecnt = praisecnt;
	}

	public ArrayList<StuPublishHomeWorkPageModel> getPagelist() {
		return pagelist;
	}

	public void setPagelist(ArrayList<StuPublishHomeWorkPageModel> pagelist) {
		this.pagelist = pagelist;
	}

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "HomeWorkModel [taskid=" + taskid + ", tasktype=" + tasktype + ", paytype=" + paytype + ", memo=" + memo
				+ ", state=" + state + ", datatime=" + datatime + ", answertime=" + answertime + ", confirmtime="
				+ confirmtime + ", limittime=" + limittime + ", grabtime=" + grabtime + ", studid=" + studid
				+ ", studname=" + studname + ", avatar=" + avatar + ", grade=" + grade + ", gradeid=" + gradeid
				+ ", subject=" + subject + ", subjectid=" + subjectid + ", bounty=" + bounty + ", givegold=" + givegold
				+ ", grabuserid=" + grabuserid + ", isnew=" + isnew + ", supervip=" + supervip + ", grabed=" + grabed
				+ ", confirmuserid=" + confirmuserid + ", praise=" + praise + ", praisecnt=" + praisecnt
				+ ", satisfaction=" + satisfaction + ", credit=" + credit + ", homeworkcnt=" + homeworkcnt
				+ ", teachername=" + teachername + ", teacheravatar=" + teacheravatar + ", teacherhomeworkcnt="
				+ teacherhomeworkcnt + ", pagelist=" + pagelist + ", orgid=" + orgid + "]";
	}

	public int getOrgid() {
		return orgid;
	}

	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}




}

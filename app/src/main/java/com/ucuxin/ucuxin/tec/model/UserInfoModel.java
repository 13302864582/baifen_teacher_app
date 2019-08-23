package com.ucuxin.ucuxin.tec.model;

import java.io.Serializable;

public class UserInfoModel implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 0,采纳次数 */
	private int adoptcnt;
	private int age;
	/** 是否能抢题 */
	private int allowgrab;
	/** 0,消费累计金额 */
	private String amountamt;
	/** 0,仲裁次数 */
	private int arbcnt;
	private String avatar_100;
	private String avatar_40;
	private String city;
	/** 0,发任务次数 */
	private int countamt;
	private float credit;
	private String dreamuniv;
	private String dreamunivid;
	/** 老师收入金币 */
	private float earngold;
	/** 老师教育水平 */
	private int edulevel;
	private String email;
	/** 消费累计金额 */
	private float expensesamt;
	private String fromchan;
	private float gold;
	private String grade;
	private int gradeid;
	private String groupphoto;
	/** 发作业次数 */
	private int homeworkcnt;
	/** 标识用户信息是否完整 */
	private int infostate;
	private String name;
	private String namepinyin;
	private String province;
	/** 抢题次数 */
	private int quickcnt;
	private int roleid;
	private String schools;
	private int schoolsid;
	private int sex;
	/** 是否封号 */
	private int state;
	private int supervip;
	private int teachlevel;
	private String tel;
	private String thirdname;
	private String tokenid;
	private int userid;
	/** 所属客服id */
	private int welearnid;
	private int newuser;
	/** 老师擅长科目 */
	private String teachmajor;
	/** 专业 */
	private String major;
	/** 联系人列表简略擅长科目 */
	private String contact_subject;
	
	/** 所在辅导机构名字 */
	private String orgname;

	/** 所在辅导机构id */
	private int orgid;
	/** 关系 */
	private int relation;
	/** 学生评价数 */
	private int count;
	/** 学生与机构的关系，            #0无关 1关注 2成员 */
	private int relationtype;

	@Override
	public String toString() {
		return "UserInfoModel [adoptcnt=" + adoptcnt + ", age=" + age + ", allowgrab=" + allowgrab + ", amountamt="
				+ amountamt + ", arbcnt=" + arbcnt + ", avatar_100=" + avatar_100 + ", avatar_40=" + avatar_40
				+ ", city=" + city + ", countamt=" + countamt + ", credit=" + credit + ", dreamuniv=" + dreamuniv
				+ ", dreamunivid=" + dreamunivid + ", earngold=" + earngold + ", edulevel=" + edulevel + ", email="
				+ email + ", expensesamt=" + expensesamt + ", fromchan=" + fromchan + ", gold=" + gold + ", grade="
				+ grade + ", gradeid=" + gradeid + ", groupphoto=" + groupphoto + ", homeworkcnt=" + homeworkcnt
				+ ", infostate=" + infostate + ", name=" + name + ", namepinyin=" + namepinyin + ", province="
				+ province + ", quickcnt=" + quickcnt + ", roleid=" + roleid + ", schools=" + schools + ", schoolsid="
				+ schoolsid + ", sex=" + sex + ", state=" + state + ", supervip=" + supervip + ", teachlevel="
				+ teachlevel + ", tel=" + tel + ", thirdname=" + thirdname + ", tokenid=" + tokenid + ", userid="
				+ userid + ", welearnid=" + welearnid + ", newuser=" + newuser + ", teachmajor=" + teachmajor
				+ ", major=" + major + ", contact_subject=" + contact_subject + ", orgname=" + orgname + ", orgid="
				+ orgid + ", relation=" + relation + ", count=" + count + ", relationtype=" + relationtype + "]";
	}

	public int getAdoptcnt() {
		return adoptcnt;
	}

	public void setAdoptcnt(int adoptcnt) {
		this.adoptcnt = adoptcnt;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAllowgrab() {
		return allowgrab;
	}

	public void setAllowgrab(int allowgrab) {
		this.allowgrab = allowgrab;
	}

	public String getAmountamt() {
		return amountamt;
	}

	public void setAmountamt(String amountamt) {
		this.amountamt = amountamt;
	}

	public int getArbcnt() {
		return arbcnt;
	}

	public void setArbcnt(int arbcnt) {
		this.arbcnt = arbcnt;
	}

	public String getAvatar_100() {
		return avatar_100;
	}

	public void setAvatar_100(String avatar_100) {
		this.avatar_100 = avatar_100;
	}

	public String getAvatar_40() {
		return avatar_40;
	}

	public void setAvatar_40(String avatar_40) {
		this.avatar_40 = avatar_40;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCountamt() {
		return countamt;
	}

	public void setCountamt(int countamt) {
		this.countamt = countamt;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public String getDreamuniv() {
		return dreamuniv;
	}

	public void setDreamuniv(String dreamuniv) {
		this.dreamuniv = dreamuniv;
	}

	public String getDreamunivid() {
		return dreamunivid;
	}

	public void setDreamunivid(String dreamunivid) {
		this.dreamunivid = dreamunivid;
	}

	public float getEarngold() {
		return earngold;
	}

	public void setEarngold(float earngold) {
		this.earngold = earngold;
	}

	public int getEdulevel() {
		return edulevel;
	}

	public void setEdulevel(int edulevel) {
		this.edulevel = edulevel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getExpensesamt() {
		return expensesamt;
	}

	public void setExpensesamt(float expensesamt) {
		this.expensesamt = expensesamt;
	}

	public String getFromchan() {
		return fromchan;
	}

	public void setFromchan(String fromchan) {
		this.fromchan = fromchan;
	}

	public float getGold() {
		return gold;
	}

	public void setGold(float gold) {
		this.gold = gold;
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

	public String getGroupphoto() {
		return groupphoto;
	}

	public void setGroupphoto(String groupphoto) {
		this.groupphoto = groupphoto;
	}

	public int getHomeworkcnt() {
		return homeworkcnt;
	}

	public void setHomeworkcnt(int homeworkcnt) {
		this.homeworkcnt = homeworkcnt;
	}

	public int getInfostate() {
		return infostate;
	}

	public void setInfostate(int infostate) {
		this.infostate = infostate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamepinyin() {
		return namepinyin;
	}

	public void setNamepinyin(String namepinyin) {
		this.namepinyin = namepinyin;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getQuickcnt() {
		return quickcnt;
	}

	public void setQuickcnt(int quickcnt) {
		this.quickcnt = quickcnt;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getSchools() {
		return schools;
	}

	public void setSchools(String schools) {
		this.schools = schools;
	}

	public int getSchoolsid() {
		return schoolsid;
	}

	public void setSchoolsid(int schoolsid) {
		this.schoolsid = schoolsid;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSupervip() {
		return supervip;
	}

	public void setSupervip(int supervip) {
		this.supervip = supervip;
	}

	public int getTeachlevel() {
		return teachlevel;
	}

	public void setTeachlevel(int teachlevel) {
		this.teachlevel = teachlevel;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getThirdname() {
		return thirdname;
	}

	public void setThirdname(String thirdname) {
		this.thirdname = thirdname;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getWelearnid() {
		return welearnid;
	}

	public void setWelearnid(int welearnid) {
		this.welearnid = welearnid;
	}

	public int getNewuser() {
		return newuser;
	}

	public void setNewuser(int newuser) {
		this.newuser = newuser;
	}

	public String getTeachmajor() {
		return teachmajor;
	}

	public void setTeachmajor(String teachmajor) {
		this.teachmajor = teachmajor;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public String getContact_subject() {
		return contact_subject;
	}

	public void setContact_subject(String contact_subject) {
		this.contact_subject = contact_subject;
	}

	public int getRelationtype() {
		return relationtype;
	}

	public void setRelationtype(int relationtype) {
		this.relationtype = relationtype;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public int getOrgid() {
		return orgid;
	}

	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

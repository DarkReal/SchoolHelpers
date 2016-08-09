package com.xxw.student.PageModel;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: AppUser
 * 
 * @Description:
 * @author peter Jia
 * @date 2016-7-1-上午10:15:53
 */
public class AppUser implements Serializable {

	private static final long serialVersionUID = -8207727064245094521L;

	private String id;// 标识ID
	private String phone;// 手机号
//	private String password;// 密码
	private String pCode;// 验证码(注:这里的验证码并不是用来存储而是用来传输数据)
	private String nickName;// 昵称
	private String realName;// 真实姓名
	private String birth;// 出生年月
	private String gender;
	private String city;// 城市
	private String education;// 学历
	private String univercity;// 毕业院校
	private String byDate;// 毕业时间
	private String majorIn;// 专业
	private String email;// 邮箱
	private String signature;// 个性签名
	private String headPic;// 头像

	private String adviceCount;// 通知数量

	private String exp1;
	private String exp2;
	private String exp3;
	private String exp4;
	private String exp5;
	private String exp6;
	private String exp7;
	private String exp8;
	private String exp9;
	private Date createTime;// 新增时间
	private String creator;// 新增人员
	private String creatorName;// 新增人员姓名
	private Date updateTime;// 更新时间
	private String updator;// 更新人员
	private String updatorName;// 更新人员姓名

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getUnivercity() {
		return univercity;
	}

	public void setUnivercity(String univercity) {
		this.univercity = univercity;
	}

	public String getByDate() {
		return byDate;
	}

	public void setByDate(String byDate) {
		this.byDate = byDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMajorIn() { return majorIn;}

	public void setMajorIn(String majorIn) {
		this.majorIn = majorIn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getAdviceCount() {
		return adviceCount;
	}

	public void setAdviceCount(String adviceCount) {
		this.adviceCount = adviceCount;
	}

	public String getExp1() {
		return exp1;
	}

	public void setExp1(String exp1) {
		this.exp1 = exp1;
	}

	public String getExp2() {
		return exp2;
	}

	public void setExp2(String exp2) {
		this.exp2 = exp2;
	}

	public String getExp3() {
		return exp3;
	}

	public void setExp3(String exp3) {
		this.exp3 = exp3;
	}

	public String getExp4() {
		return exp4;
	}

	public void setExp4(String exp4) {
		this.exp4 = exp4;
	}

	public String getExp5() {
		return exp5;
	}

	public void setExp5(String exp5) {
		this.exp5 = exp5;
	}

	public String getExp6() {
		return exp6;
	}

	public void setExp6(String exp6) {
		this.exp6 = exp6;
	}

	public String getExp7() {
		return exp7;
	}

	public void setExp7(String exp7) {
		this.exp7 = exp7;
	}

	public String getExp8() {
		return exp8;
	}

	public void setExp8(String exp8) {
		this.exp8 = exp8;
	}

	public String getExp9() {
		return exp9;
	}

	public void setExp9(String exp9) {
		this.exp9 = exp9;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", phone=" + phone + ", pCode=" + pCode + ", nickName=" + nickName
				+ ", realName=" + realName + ", birth=" + birth + ", gender="
				+ gender + ", city=" + city + ", education=" + education
				+ ", univercity=" + univercity + ", byDate=" + byDate
				+ ", majorIn=" + majorIn + ", email=" + email + ", signature="
				+ signature + ", headPic=" + headPic + ", adviceCount="
				+ adviceCount + ", exp1=" + exp1 + ", exp2=" + exp2 + ", exp3="
				+ exp3 + ", exp4=" + exp4 + ", exp5=" + exp5 + ", exp6=" + exp6
				+ ", exp7=" + exp7 + ", exp8=" + exp8 + ", exp9=" + exp9
				+ ", createTime=" + createTime + ", creator=" + creator
				+ ", creatorName=" + creatorName + ", updateTime=" + updateTime
				+ ", updator=" + updator + ", updatorName=" + updatorName + "]";
	}
}

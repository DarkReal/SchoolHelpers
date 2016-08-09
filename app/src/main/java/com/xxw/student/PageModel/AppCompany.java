package com.xxw.student.PageModel;

import java.io.Serializable;
import java.util.Date;


/**
 * ClassName : AppCompany
 * @Description： 公司表
 * @author DarkReal
 */
public class AppCompany implements Serializable {


    private static final long serialVersionUID = -5460659593735103935L;

    private String id;//标识id

    private String companyName;//公司名
    private String companyDesc;//公司简介
    private String city;//城市
    private String companyPic;//公司配图
    private String companyDetail;//公司详情
    private String address;//公司位置
    private String website;//公司网站

    private String exp1;
    private String exp2;
    private String exp3;
    private String exp4;
    private String exp5;
    private String exp6;
    private String exp7;
    private String exp8;
    private String exp9;
    private Date createTime;//新增时间
    private String creator;//新增人员
    private String creatorName;//新增人员姓名
    private Date updateTime;//更新时间
    private String updator;//更新人员
    private String updatorName;//更新人员姓名
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyDesc() {
        return companyDesc;
    }
    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCompanyPic() {
        return companyPic;
    }
    public void setCompanyPic(String companyPic) {
        this.companyPic = companyPic;
    }
    public String getCompanyDetail() {
        return companyDetail;
    }
    public void setCompanyDetail(String companyDetail) {
        this.companyDetail = companyDetail;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
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
        return "AppCompany [id=" + id + ", companyName=" + companyName
                + ", companyDesc=" + companyDesc + ", city=" + city
                + ", companyPic=" + companyPic + ", companyDetail="
                + companyDetail + ", address=" + address + ", website="
                + website + ", exp1=" + exp1 + ", exp2=" + exp2 + ", exp3="
                + exp3 + ", exp4=" + exp4 + ", exp5=" + exp5 + ", exp6=" + exp6
                + ", exp7=" + exp7 + ", exp8=" + exp8 + ", exp9=" + exp9
                + ", createTime=" + createTime + ", creator=" + creator
                + ", creatorName=" + creatorName + ", updateTime=" + updateTime
                + ", updator=" + updator + ", updatorName=" + updatorName + "]";
    }
}

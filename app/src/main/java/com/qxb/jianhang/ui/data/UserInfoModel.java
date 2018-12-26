package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * @author lee
 * @version create time:2015/11/1110:07
 * @Email email
 * @Description $用户信息model
 */

public class UserInfoModel extends BaseModel {

    public int vipType; //0普通用户，1会员

    private String userid,mobile,nickname,photo,
                company, companyid,job,jobid,myfocusunread,myfocuscount
                ,systemmessageunread,feedbackunread;
    private boolean issetpwd;

    public boolean issetpwd() {
        return issetpwd;
    }

    public void setIssetpwd(boolean issetpwd) {
        this.issetpwd = issetpwd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    /*public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }*/

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getMyfocusunread() {
        return myfocusunread;
    }

    public void setMyfocusunread(String myfocusunread) {
        this.myfocusunread = myfocusunread;
    }

    public String getSystemmessageunread() {
        return systemmessageunread;
    }

    public void setSystemmessageunread(String systemmessageunread) {
        this.systemmessageunread = systemmessageunread;
    }

    public String getMyfocuscount() {
        return myfocuscount;
    }

    public void setMyfocuscount(String myfocuscount) {
        this.myfocuscount = myfocuscount;
    }

    public String getFeedbackunread() {
        return feedbackunread;
    }

    public void setFeedbackunread(String feedbackunread) {
        this.feedbackunread = feedbackunread;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "userid='" + userid + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", company='" + company + '\'' +
                ", companyid='" + companyid + '\'' +
                ", job='" + job + '\'' +
                ", jobid='" + jobid + '\'' +
                ", myfocusunread='" + myfocusunread + '\'' +
                ", systemmessageunread='" + systemmessageunread + '\'' +
                ", feedbackunread='" + feedbackunread + '\'' +
                ", issetpwd=" + issetpwd +
                '}';
    }
}

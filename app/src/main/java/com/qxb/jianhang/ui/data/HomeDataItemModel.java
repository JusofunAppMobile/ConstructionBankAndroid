package com.qxb.jianhang.ui.data;

import android.text.TextUtils;

import com.jusfoun.baselibrary.base.BaseModel;

import java.io.Serializable;

/**
 * @author zhaoyapeng
 * @version create time:17/12/2115:44
 * @Email zyp@jusfoun.com
 * @Description ${企业 model}
 */
public class HomeDataItemModel extends BaseModel implements Serializable {

    private String companyId;
    private String companyname;
    private String companystate;
    private String CreateUser;
    private String legal;
    private String CreateTime;
    private String IsDelete;
    private String updatetime;
    private String isupdate;
    private String CompanyJson;
    private String CompanyState;
    private String attentioncount;
    private String ratings;
    private String year;
    private String month;
    private String title;
    private String image;
    private String url;
    private String umeng_analytics;
    private String count;
    private String industry;
    private String location;
    private String funds;
    private String companylightname;
    private String related;
    private String socialcredit;
    public String capital;
    public String establish;
    public String legalPerson;
    public String PublishTime;
    public String relatednofont;
    public boolean isFav;
    public int mSearchType;
    public boolean isSelect;
    public boolean ishasshareholder;
    private String companyName;
    public String address;

    public String distance;
    public String entid;
    public String followState;


    public String nature;
    public String registMoney;
    public String province;
    public String registDate;
    public String name;


    public CompanyDetailModel.LatLonModel topLeft;
    public CompanyDetailModel.LatLonModel bottomRight;
    public int docCount;
    public String type;

    public String longitude;
    public String latitude;
    public String classify;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSocialcredit() {
        return socialcredit;
    }

    public void setSocialcredit(String socialcredit) {
        this.socialcredit = socialcredit;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFunds() {
        return funds;
    }

    public void setFunds(String funds) {
        this.funds = funds;
    }


    public String getCompanyid() {
        return companyId;
    }

    public void setCompanyid(String companyid) {
        this.companyId = companyid;
    }

    public String getCompanyname() {
        if (TextUtils.isEmpty(companyname) && !TextUtils.isEmpty(companyName)) {
            return companyName;

        }
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanystate() {
        return companystate;
    }

    public void setCompanystate(String companystate) {
        this.companystate = companystate;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(String isDelete) {
        IsDelete = isDelete;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCompanyJson() {
        return CompanyJson;
    }

    public void setCompanyJson(String companyJson) {
        CompanyJson = companyJson;
    }

    public String getCompanyState() {
        return CompanyState;
    }

    public void setCompanyState(String companyState) {
        CompanyState = companyState;
    }

    public String getAttentioncount() {
        return attentioncount;
    }

    public void setAttentioncount(String attentioncount) {
        this.attentioncount = attentioncount;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }

    public String getUmeng_analytics() {
        return umeng_analytics;
    }

    public void setUmeng_analytics(String umeng_analytics) {
        this.umeng_analytics = umeng_analytics;
    }

    public String getCompanylightname() {
        return companylightname;
    }

    public void setCompanylightname(String companylightname) {
        this.companylightname = companylightname;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }
}

package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/10.
 * Description
 */
public class CompanyDetailModel extends BaseModel implements Cloneable {


    private String companyName;
    private String companyid;
    private String ratings;
    private String browsecount;//被浏览数
    private String companynature;//公司性质
    private String corporate;//法人
    private String registernumber;
    private String address;
    private String longitude;
    private String latitude;
    private String attentioncount;//关注数
    private String registercapital;//注册资金
    private String cratedate;//成立日期
    private String companysize;//公司规模
    private String isupdate;// 1可以更新 2不能更新
    private String industry;//所处行业
    private String updatestate;
    private String isattention;
    private String shareurl;
    private String states;
    private List<ContactinFormationModel> companyphonelist;
    private List<Url> neturl;
    private List<CompanyDetailMenuModel> subclassMenu;
    private int hasCompanyData;
    private String currentstate;//”当前更新状态 0.正在更新或者未更新 1.已更新成功 ”
    public String taxid;
    public int followState;

    public LatLonModel topLeft;
    public LatLonModel bottomRight;
    public int docCount;
    public String type;

    public String getCurrentstate() {
        return currentstate;
    }

    public void setCurrentstate(String currentstate) {
        this.currentstate = currentstate;
    }

    public class Url implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getCompanyname() {
        return companyName;
    }

    public void setCompanyname(String companyname) {
        this.companyName = companyname;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRegisternumber() {
        return registernumber;
    }

    public void setRegisternumber(String registernumber) {
        this.registernumber = registernumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public List<CompanyDetailMenuModel> getSubclassMenu() {
        return subclassMenu;
    }

    public void setSubclassMenu(List<CompanyDetailMenuModel> subclassMenu) {
        this.subclassMenu = subclassMenu;
    }

    public String getAttentioncount() {
        return attentioncount;
    }

    public void setAttentioncount(String attentioncount) {
        this.attentioncount = attentioncount;
    }

    public String getUpdatestate() {
        return updatestate;
    }

    public void setUpdatestate(String updatestate) {
        this.updatestate = updatestate;
    }

    public String getIsattention() {
        return isattention;
    }

    public void setIsattention(String isattention) {
        this.isattention = isattention;
    }

    public List<ContactinFormationModel> getCompanyphonelist() {
        return companyphonelist;
    }

    public void setCompanyphonelist(List<ContactinFormationModel> companyphonelist) {
        this.companyphonelist = companyphonelist;
    }

    public String getBrowsecount() {
        return browsecount;
    }

    public void setBrowsecount(String browsecount) {
        this.browsecount = browsecount;
    }

    public String getCompanynature() {
        return companynature;
    }

    public void setCompanynature(String companynature) {
        this.companynature = companynature;
    }

    public String getCorporate() {
        return corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getRegistercapital() {
        return registercapital;
    }

    public void setRegistercapital(String registercapital) {
        this.registercapital = registercapital;
    }

    public String getCratedate() {
        return cratedate;
    }

    public void setCratedate(String cratedate) {
        this.cratedate = cratedate;
    }

    public String getCompanysize() {
        return companysize;
    }

    public void setCompanysize(String companysize) {
        this.companysize = companysize;
    }

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }

    public List<Url> getNeturl() {
        return neturl;
    }

    public void setNeturl(List<Url> neturl) {
        this.neturl = neturl;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public int getHasCompanyData() {
        return hasCompanyData;
    }

    public void setHasCompanyData(int hasCompanyData) {
        this.hasCompanyData = hasCompanyData;
    }


    public CompanyDetailModel getCloneModel() {
        CompanyDetailModel model = null;
        try {
            // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            model = (CompanyDetailModel) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return model;
    }


    public class LatLonModel extends BaseModel{

        public double lat;
        public double lon;
        public String geohash;

    }
}

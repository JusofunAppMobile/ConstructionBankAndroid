package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/20.
 * Description
 */
public class CompanyMapDetailModel extends BaseModel {
    private String entId;
    private String shortName;
    private String companyType;
    private String companyName;
    private String createDate;
    private String legal;
    private String stock;
    private String investmentAmount;
    private String shareHolders;
    private String investmentToOut;
    private String industry;
    private String name;
    private String papers;
    private String papernumber;

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getShareHolders() {
        return shareHolders;
    }

    public void setShareHolders(String shareHolders) {
        this.shareHolders = shareHolders;
    }

    public String getInvestmentToOut() {
        return investmentToOut;
    }

    public void setInvestmentToOut(String investmentToOut) {
        this.investmentToOut = investmentToOut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPapers() {
        return papers;
    }

    public void setPapers(String papers) {
        this.papers = papers;
    }

    public String getPapernumber() {
        return papernumber;
    }

    public void setPapernumber(String papernumber) {
        this.papernumber = papernumber;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

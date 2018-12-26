package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * @author lee
 * @version create time:2015/10/2317:38
 * @Email email
 * @Description ${TODO}
 */

public class InvestmentModel extends BaseModel{
    private String entId,shortName,companyName,createDate,legal,stock,investmentAmount,
                    isUnfold,industry;

    private int shareHolders,investmentToOut;

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getInvestmentAmount() {
        return investmentAmount;
    }

    public void setInvestmentAmount(String investmentAmount) {
        this.investmentAmount = investmentAmount;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public int getShareHolders() {
        return shareHolders;
    }

    public void setShareHolders(int shareHolders) {
        this.shareHolders = shareHolders;
    }

    public int getInvestmentToOut() {
        return investmentToOut;
    }

    public void setInvestmentToOut(int investmentToOut) {
        this.investmentToOut = investmentToOut;
    }

    public String getIsUnfold() {
        return isUnfold;
    }

    public void setIsUnfold(String isUnfold) {
        this.isUnfold = isUnfold;
    }


    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "InvestmentModel{" +
                "entId='" + entId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", legal='" + legal + '\'' +
                ", stock='" + stock + '\'' +
                ", shareHolders='" + shareHolders + '\'' +
                ", isUnfold='" + isUnfold + '\'' +
                ", investmentToOut='" + investmentToOut + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }
}

package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

import java.util.List;

/**
 * @author lee
 * @version create time:2015/10/2317:35
 * @Email email
 * @
 */

public class CompanyMapModel extends BaseModel {

    private List<ShareholderModel> shareholders;
    private List<InvestmentModel> investments;

    private String cEntShortName;

    public String getcEntShortName() {
        return cEntShortName;
    }

    public void setcEntShortName(String cEntShortName) {
        this.cEntShortName = cEntShortName;
    }

    public List<ShareholderModel> getShareholders() {
        return shareholders;
    }

    public void setShareholders(List<ShareholderModel> shareholders) {
        this.shareholders = shareholders;
    }

    public List<InvestmentModel> getInvestments() {
        return investments;
    }

    public void setInvestments(List<InvestmentModel> investments) {
        this.investments = investments;
    }

    @Override
    public String toString() {
        return "CompanyMapModel{" +
                "cEntShortName='" + cEntShortName + '\'' +
                ", shareholders=" + shareholders +
                ", investments=" + investments +
                '}';
    }
}

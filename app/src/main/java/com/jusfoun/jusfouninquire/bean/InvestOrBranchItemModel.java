package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/13.
 * Description
 */
public class InvestOrBranchItemModel extends BaseModel {
    private String companyname;
    private String companyid;
    private String legal;
    private String companystate;

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
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
}
package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * @author lee
 * @version create time:2015/11/1010:14
 * @Email email
 * @Description ${TODO}
 */

public class CompanyModel extends BaseModel{

    private String companyid,companyname;

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    @Override
    public String toString() {
        return "CompanyModel{" +
                "companyid='" + companyid + '\'' +
                ", companyname='" + companyname + '\'' +
                '}';
    }
}

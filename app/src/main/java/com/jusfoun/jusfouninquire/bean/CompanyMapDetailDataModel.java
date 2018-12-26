package com.jusfoun.jusfouninquire.bean;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/20.
 * Description
 */
public class CompanyMapDetailDataModel extends BaseModel {

    private CompanyMapDetailModel data;
    private String type;

    public CompanyMapDetailModel getData() {
        return data;
    }

    public void setData(CompanyMapDetailModel data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

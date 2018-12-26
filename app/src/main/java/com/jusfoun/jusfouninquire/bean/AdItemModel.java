package com.jusfoun.jusfouninquire.bean;

import java.io.Serializable;

/**
 * AdItemModel
 *
 * @author : albert
 * @Email : liubinhou007@163.com
 * @date : 16/8/2
 * @Description :首页活动，单个viewpager 的 item 实例
 */
public class AdItemModel implements Serializable {

    private String ImgUrl,ReUrl,Title,Describe,umeng;

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getReUrl() {
        return ReUrl;
    }

    public void setReUrl(String reUrl) {
        ReUrl = reUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getUmeng() {
        return umeng;
    }

    public void setUmeng(String umeng) {
        this.umeng = umeng;
    }

    @Override
    public String toString() {
        return "ImgUrl = " + ImgUrl + " ReUrl = " + ReUrl + " Title = " + Title + " Describe = " + Describe;
    }
}

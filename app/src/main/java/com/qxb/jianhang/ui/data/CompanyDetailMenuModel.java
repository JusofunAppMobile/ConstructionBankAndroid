package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/10.
 * Description
 */
public class CompanyDetailMenuModel extends BaseModel {

    private String menuid;
    private String menuname;
    private String applinkurl;
    private String isshow;
    private String icon;
    private int type;
    private int hasData;
    private String HotImageUrl;
    private boolean animated;//记录是否已经执行过动画
    private String umeng;//统计友盟事件

    private String Items;
    private String ItemUrls;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //    public Object tablist;
    public Object tablist;

    public String getUmeng() {
        return umeng;
    }

    public void setUmeng(String umeng) {
        this.umeng = umeng;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public String getHotImageUrl() {
        return HotImageUrl;
    }

    public void setHotImageUrl(String hotImageUrl) {
        HotImageUrl = hotImageUrl;
    }


    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getApplinkurl() {
        return applinkurl;
    }

    public void setApplinkurl(String applinkurl) {
        this.applinkurl = applinkurl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getHasData() {
        return hasData;
    }

    public void setHasData(int hasData) {
        this.hasData = hasData;
    }

    public String getItemUrls() {
        return ItemUrls;
    }

    public void setItemUrls(String itemUrls) {
        ItemUrls = itemUrls;
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String items) {
        this.Items = items;
    }
}

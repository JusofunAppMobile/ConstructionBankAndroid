package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

import java.util.List;

/**
 * @author liuguangdan
 * @version create at 2018/8/30/030 15:36
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class MessageCompanyModel extends BaseModel {

    public List<MessageCompanyItemModel> list;


    public class MessageCompanyItemModel extends BaseModel{
        public boolean select;
        public String entid;
        public String name;
    }
}

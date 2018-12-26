package com.qxb.jianhang.ui.data;

import java.io.Serializable;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 15:19
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class ClientModel implements Serializable {
    private static final long serialVersionUID = 3255019853237994915L;


    /**
     * companyId : 1234455
     * companyName : 九次方大数据
     * address : 北京市海淀区永泰庄北路1号天地邻枫6号楼
     * distance : 100米
     * followState : 1
     */

    public String companyId;
    public String companyName;
    public String address;
    public String distance;
    public int followState;
    public int type;
}

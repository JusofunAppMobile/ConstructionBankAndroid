package com.qxb.jianhang.ui.data;

import com.jusfoun.baselibrary.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:16/8/1310:29
 * @Email zyp@jusfoun.com
 * @Description ${搜索listmodel}
 */
public class SearchListModel extends BaseModel implements Serializable {
    private int count;
    public List<HomeDataItemModel> list;
    public int havePageIndex;
    public int searchType;

    public String searchKey;

    public int totalCount;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

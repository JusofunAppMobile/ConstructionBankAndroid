package com.jusfoun.baselibrary.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

/**
 * @author zhaoyapeng
 * @date 2017/11/17
 * @describe base viewholder
 */

public abstract class BaseViewHolder<T extends Serializable> extends RecyclerView.ViewHolder {
    protected Context mContext;
    public BaseViewHolder(View itemView,Context mContext) {
        super(itemView);
        this.mContext=mContext;
    }

    public abstract void update(T model);

}

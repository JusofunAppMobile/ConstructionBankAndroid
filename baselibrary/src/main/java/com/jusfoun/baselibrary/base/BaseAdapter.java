package com.jusfoun.baselibrary.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhaoyapeng
 * @date 2017/11/17
 * @describe adapter
 */

public abstract class BaseAdapter<T extends Serializable> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> list = new ArrayList<>();
    protected Context context;
    private LayoutInflater inflater;

    public BaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(getLayoutResId(viewType), parent, false);
        BaseViewHolder viewHolder = getViewHolder(viewType, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public List<T> getList() {
        return list;
    }

    public abstract int getLayoutResId(int viewType);

    protected abstract BaseViewHolder getViewHolder(int viewType, View view);

    /**
     * 添加多项
     *
     * @param list
     */
    public void addList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 刷新列表
     *
     * @param list
     */
    public void refreshList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加一项
     *
     * @param item
     * @param index
     */
    public void add(T item, int index) {
        if (item == null) {
            return;
        }
        if (index >= list.size()) {
            index = list.size();
        }
        if (index == -1) {
            list.add(item);
        }
        list.add(index, item);
        notifyItemInserted(index);
    }

    /**
     * 刷新一项
     *
     * @param item
     * @param index
     */
    public void refreshItem(T item, int index) {
        if (item == null) {
            return;
        }
        if (index < 0 || index >= list.size()) {
            return;
        }
        list.set(index, item);
        notifyItemChanged(index);
    }

    /**
     * 删除一项
     *
     * @param position
     * @return
     */
    public boolean remove(int position) {
        boolean result = false;
        Iterator<T> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            T temp = iterator.next();
            if (position == index) {
                iterator.remove();
                result = true;
                break;
            }
            index++;
        }
        if (result) {
            notifyItemRemoved(index);
        }
        return result;
    }

    public boolean remove(T item) {
        boolean result = false;
        Iterator<T> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            T temp = iterator.next();
            if (temp.equals(item)) {
                iterator.remove();
                result = true;
                break;
            }
            index++;
        }
        if (result) {
            notifyItemRemoved(index);
            notifyDataSetChanged();
        }
        return result;
    }

    /**
     * 删除多项
     *
     * @param items
     */
    public void remove(List<T> items) {
        if (items == null || items.size() == 0) {
            return;
        }
        Iterator<T> iterator = list.iterator();
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);

            while (iterator.hasNext()) {
                T temp = iterator.next();
                if (temp.equals(item)) {
                    iterator.remove();
                    notifyItemRemoved(i);
                    break;
                }
            }
        }
    }
}

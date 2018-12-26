package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.CompanyDetailMenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/9.
 * Description
 */
public class MenuAdapter extends BaseAdapter {

    private int checkCount;
    private List<CompanyDetailMenuModel> list;
    private Context context;
    private MenuAdapter.OnClickListener onClickListener;
    public MenuAdapter(Context context){
        this.context=context;
        this.list =new ArrayList<>();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menu_window, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.updateView(list.get(position),position);
        return convertView;
    }

    public void setCheckCount(int checkCount){
        this.checkCount=checkCount;
        notifyDataSetChanged();
    }

    public void refresh(List<CompanyDetailMenuModel> strings){
        this.list.clear();
        this.list.addAll(strings);
        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView txt;

        public ViewHolder(View view){
            txt= (TextView) view.findViewById(R.id.txt);
        }

        public void updateView(final CompanyDetailMenuModel model,int position){
            if (!TextUtils.isEmpty(model.getMenuname()))
                txt.setText(model.getMenuname());
            if (1==model.getHasData()){
                txt.setTag(new Integer(position));
                if (position==checkCount)
                    txt.setTextColor(context.getResources().getColor(R.color.color_text_menu_select));
                else
                    txt.setTextColor(context.getResources().getColor(R.color.color_text_menu_unselect));
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener!=null){
                            Integer position= (Integer) v.getTag();
                            if (position!=null)
                                onClickListener.onClick(position);
                        }
                    }
                });
            }else {
                txt.setTextColor(context.getResources().getColor(R.color.color_text_menu_unclick));
            }

        }
    }

    public void setOnClickListener(MenuAdapter.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    public static interface OnClickListener{
        public void onClick(int position);
    }
}

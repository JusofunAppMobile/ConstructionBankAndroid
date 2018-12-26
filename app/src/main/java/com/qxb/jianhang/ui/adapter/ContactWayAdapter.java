package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.ContactinFormationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/26.
 * Description
 */
public class ContactWayAdapter extends BaseAdapter {

    private Context mContext;
    private List<ContactinFormationModel> list;

    public ContactWayAdapter(Context mContext){
        this.mContext=mContext;
        list=new ArrayList<>();
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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_contact_way,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.update(list.get(position),position);
        return convertView;
    }

    public void refresh(List<ContactinFormationModel> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView contactWay;

        public ViewHolder(View view){
            contactWay= (TextView) view.findViewById(R.id.contact_way);
        }

        public void update(ContactinFormationModel model,int position){
            if (list.size()<=0)
                return;
            contactWay.setTextColor(mContext.getResources().getColor(R.color.color_text_contact_way));
            if (!TextUtils.isEmpty(model.getNumber()))
                contactWay.setText(model.getNumber());
        }
    }
}

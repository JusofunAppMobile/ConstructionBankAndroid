package com.jusfoun.jusfouninquire.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.InvestOrBranchItemModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Author  JUSFOUN
 * CreateDate 2015/11/13.
 * Description
 */
public class InvestOrBranchAdapter extends BaseAdapter {

    private List<InvestOrBranchItemModel> list;
    private Context mContext;
    private boolean isInvest;

    public InvestOrBranchAdapter(Context mContext){
        this.mContext=mContext;
        list=new ArrayList<>();
    }

    public void refresh(List<InvestOrBranchItemModel> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addMore(List<InvestOrBranchItemModel> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 设置是否是对外投资
     * @param invest true:对外投资  false:分支机构
     */
    public void setInvest(boolean invest) {
        isInvest = invest;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InvestOrBranchViewHolder viewHolder = null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_list_attention,null);
            viewHolder=new InvestOrBranchViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (InvestOrBranchViewHolder) convertView.getTag();
        }
        viewHolder.update(list.get(position));
        return convertView;
    }

    public class InvestOrBranchViewHolder{

        private TextView companyName, legal,company_state;
        public InvestOrBranchItemModel mInvestBranchModel;

        public InvestOrBranchViewHolder(View view){
            companyName = (TextView) view.findViewById(R.id.attention_company_name);
            legal = (TextView) view.findViewById(R.id.attention_legal);
            company_state = (TextView) view.findViewById(R.id.attention_company_state);

            //2016.11.29改版需求,对外投资隐藏公司状态,搜索结果页面不需要改动
            company_state.setVisibility(View.GONE);
        }

        public void update(InvestOrBranchItemModel model){
            mInvestBranchModel = model;
            if(!TextUtils.isEmpty(model.getCompanyname())){
                companyName.setText(model.getCompanyname());
            }else {
                companyName.setText("");
            }

           legal.setVisibility(isInvest ? View.VISIBLE : View.GONE);
            if(!TextUtils.isEmpty(model.getLegal())){
                legal.setText("法人："+model.getLegal());
            }else{
                legal.setText("法人：-");
            }
            if(!TextUtils.isEmpty(model.getCompanystate())){
                company_state.setText(model.getCompanystate());
            }else {
                company_state.setText("-");
            }

        }
    }
}

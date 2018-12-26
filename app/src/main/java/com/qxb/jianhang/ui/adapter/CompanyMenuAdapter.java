package com.qxb.jianhang.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.ImageLoderUtil;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.constant.CompanyDetailTypeConstant;
import com.qxb.jianhang.ui.data.CompanyDetailMenuModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/10.
 * Description
 */
public class CompanyMenuAdapter extends RecyclerView.Adapter<CompanyMenuAdapter.CompanyHolder>implements CompanyDetailTypeConstant {

    private Context context;
    private List<CompanyDetailMenuModel> list;
    private List<Integer> animateList;//统计需要进行动画的宫格的索引
    private int mCurrentAnimIndex;//需要执行动画的item
    private int mAnimatedCount = 0;//记录已经执行过动画的item数量

    private int[] TYPE_ARRAY = {1,2,3,4,5,6,7,8,9};

    public CompanyMenuAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        animateList = new ArrayList<>();
    }

    @Override
    public CompanyMenuAdapter.CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyHolder(LayoutInflater.from(context).inflate(R.layout.item_company_menu, parent,false));
    }

    @Override
    public void onBindViewHolder(CompanyMenuAdapter.CompanyHolder holder, int position) {
        holder.updateView(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<CompanyDetailMenuModel> menuList) {



        if (menuList == null){
            return;
        }
        this.list.clear();
        for(CompanyDetailMenuModel model : menuList ){
            if(types.contains(model.getType())){
                this.list.add(model);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            CompanyDetailMenuModel model = list.get(i);
            if (!TextUtils.isEmpty(model.getHotImageUrl())) {
                animateList.add(i);
            }
        }

        Log.e("tag","list====="+list.size());
        doAnimate(-1);
    }

    /**
     * 选取某个item进行动画展示
     *
     * @param preIndex 之前完成动画的item索引
     */
    public void doAnimate(int preIndex) {
        if (animateList.size() > 0) {
            if (preIndex != -1) {
                mAnimatedCount++;
            }

            //本轮如果已经执行完成，复位执行下一轮
            if (mAnimatedCount == animateList.size()) {
                mAnimatedCount = 0;

                for (int i = 0; i < animateList.size(); i++) {
                    if (list.get(animateList.get(i)) != null) {
                        list.get(animateList.get(i)).setAnimated(false);
                    }
                }
                doAnimate(-1);
                return;
            }


            if (animateList.size() == 1) {
                //TODO 只有一个需要进行动画
                mCurrentAnimIndex = animateList.get(0);
                if (this.list.get(mCurrentAnimIndex) != null) {
                    mAnimatedCount = 0;
                    list.get(animateList.get(0)).setAnimated(false);
                }

                notifyDataSetChanged();
            } else {
                Random random = new Random();
                int index = random.nextInt(animateList.size());
                //TODO 有多个item需要进行动画，挑选需要进行动画的item

                //下次执行和刚刚执行完成的是同一个item,再次触发选择
                if ((animateList.get(index) == preIndex)) {
                    doAnimate(-1);
                } else {
                    if (list.get(animateList.get(index)).isAnimated()) {
                        doAnimate(-1);
                    } else {
                        mCurrentAnimIndex = animateList.get(index);
                        notifyDataSetChanged();
                    }

                }

            }

        } else {
            notifyDataSetChanged();
        }

    }


    class CompanyHolder extends RecyclerView.ViewHolder {

        private TextView txt,countText;
        private ImageView img, hotOrNewImg;
        private View view;

        public CompanyHolder(View itemView) {
            super(itemView);
            view = itemView;
            txt = (TextView) itemView.findViewById(R.id.company_menu_txt);
            img = (ImageView) itemView.findViewById(R.id.company_menu_img);
            countText = (TextView)itemView.findViewById(R.id.text_count);
//            hotOrNewImg = (SimpleDraweeView) itemView.findViewById(R.id.img_hot_new);
        }

        public void updateView(final CompanyDetailMenuModel model, final int position) {
            if (!TextUtils.isEmpty(model.getMenuname()))
                this.txt.setText(model.getMenuname());

//            if (model.getHotImageUrl() != null && !model.getHotImageUrl().equals("")) {
//                hotOrNewImg.setVisibility(View.GONE);
//                hotOrNewImg.setImageURI(Uri.parse(model.getHotImageUrl()));
//                //当前item是需要执行动画的item，且在本轮尚未执行过动画
//                if ((position == mCurrentAnimIndex)) {
//                    Shaker shaker = new Shaker(hotOrNewImg, position);
//                    shaker.shakeAndPause();
//                    model.setAnimated(true);
//                }
//
//            } else {
//                hotOrNewImg.setVisibility(View.GONE);
//            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1!=model.getHasData())
                        return;
//                    if(model.getMenuname().equals("欠税公告")){
//                        model.setType(10);
//                    }
                    //需求只针对有数据的内容进行统计
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position, model.getUmeng());
                }
            });

            if (1==model.getHasData()) {
                ImageLoderUtil.loadNormalImg(context,img,model.getIcon(),R.mipmap.ic_launcher);
                txt.setTextColor(context.getResources().getColor(R.color.color_text_company_detail_menu));
                view.setAlpha(1f);
            } else {
                ImageLoderUtil.loadNormalImg(context,img,model.getIcon(),R.mipmap.ic_launcher);
                    view.setAlpha(0.5f);
                    /*try {
                        String str = model.getItemUrls().substring(0, model.getItemUrls().length() - ".png".length());
                        if (!str.endsWith("h"))
                            str += "h.png";
                        else
                            str += ".png";
//                    img.setImageURI(Uri.parse(model.getIcon()));
                        img.setImageURI(Uri.parse(str));*/
                    /*} catch (Exception e) {

                    }*/
                txt.setTextColor(context.getResources().getColor(R.color.color_text_menu_unclick));
            }
            if(model.getCount()<=99){
                countText.setText(model.getCount()+"");
            }else{
                countText.setText("99+");
            }

        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, String umeng);
    }
}

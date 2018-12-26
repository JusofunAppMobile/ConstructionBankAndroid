package com.jusfoun.baselibrary.Util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.jusfoun.baselibrary.widget.GlideCircleTransform;
import com.jusfoun.baselibrary.widget.GlideRoundTransform;

/**
 * @author zhaoyapeng
 * @version create time:18/1/2509:55
 * @Email zyp@jusfoun.com
 * @Description ${加载图片工具}
 */
public class ImageLoderUtil {

    public static  void loadNormalImg(Context mContext, ImageView imageView, String url,int resId){
        Glide.with(mContext)
                .load(url)
                .placeholder(resId)
                .crossFade()
                .into(imageView);
    }

    public static void loadRoundImage(Context mContext, ImageView imageView, String url,int radius){
        Glide.with(mContext)
                .load(url)
                .transform(new CenterCrop(mContext),new GlideRoundTransform(mContext,radius))
                .crossFade()
                .into(imageView);
    }

    public static void loadRoundImage(Context mContext, ImageView imageView, String url,int radius,int errorResId){
        Glide.with(mContext)
                .load(url)
                .placeholder(errorResId)
                .error(errorResId)
                .transform(new CenterCrop(mContext),new GlideRoundTransform(mContext,radius))
                .crossFade()
                .into(imageView);
    }

    public static void loadRoundSmailImage(Context mContext, ImageView imageView, String url,int errorResId){
        Glide.with(mContext)
                .load(url)
                .placeholder(errorResId)
                .error(errorResId)
                .transform(new CenterCrop(mContext),new GlideRoundTransform(mContext,10))
                .crossFade()
                .into(imageView);
    }

    public static void loadRoundSmailImage(Context mContext, ImageView imageView, String url){
        Glide.with(mContext)
                .load(url)
                .transform(new CenterCrop(mContext),new GlideRoundTransform(mContext,10))
                .crossFade()
                .into(imageView);
    }

    public static void loadCircleImage(Context mContext,ImageView imageView,String url,int errorResId){
        Glide.with(mContext)
                .load(url)
                .transform(new CenterCrop(mContext),new GlideCircleTransform(mContext))
                .placeholder(errorResId)
                .error(errorResId)
                .crossFade()
                .into(imageView);
    }

}

package com.jusfoun.baselibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.ViewConfiguration;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jusfoun.baselibrary.Util.PhoneUtil;

/**
 * Created by wang on 2016/11/18.
 */

public class GlideRoundTransform extends BitmapTransformation {
    private float radius=0f;
    public GlideRoundTransform(Context context,float radius) {
        super(context);
        this.radius= PhoneUtil.dip2px(context,radius);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool,toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private Bitmap roundCrop(BitmapPool pool,Bitmap source)
    {
        if (source==null) return null;

        Bitmap result=pool.get(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result==null){
            result=Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas=new Canvas(result);
        Paint paint=new Paint();
        paint.setShader(new BitmapShader(source,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF=new RectF(0,0,source.getWidth(),source.getHeight());
        canvas.drawRoundRect(rectF,radius,radius,paint);
        return result;
    }
}

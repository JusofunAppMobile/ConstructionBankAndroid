package com.jusfoun.jusfouninquire.route;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.CompanyMapDataModel;
import com.jusfoun.jusfouninquire.bean.CompanyMapDetailDataModel;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.constant.NetConstant;
import com.jusfoun.jusfouninquire.util.GetParamsUtil;
import com.jusfoun.jusfouninquire.util.VolleyUtil;
import com.jusfoun.jusfouninquire.volley.VolleyErrorUtil;
import com.jusfoun.jusfouninquire.volley.VolleyGetRequest;

import java.util.HashMap;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/13.
 * Description
 */
public class GetCompanyMap {

    private static final String getUrl="/api/EntAll/GetEntAtlasData";
    private static final String getDetailUrl="/api/EntAll/GetEntAtlasEntDetail";
    public static void getCompanyMap(Context context, HashMap<String,String> params, String tag, final NetWorkCallBack netWorkCallBack){
        VolleyGetRequest<CompanyMapDataModel> getRequest=new VolleyGetRequest<CompanyMapDataModel>(
                GetParamsUtil.getParmas(context.getString(R.string.req_jusfoun_url)+getUrl, params), CompanyMapDataModel.class, new Response.Listener<CompanyMapDataModel>() {
            @Override
            public void onResponse(CompanyMapDataModel response) {
                Log.e("tag","onResponseonResponse+"+new Gson().toJson(response));
                netWorkCallBack.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        }, context){

        };

        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(NetConstant.TIMEOUT, 1, 1.0f));
        getRequest.setTag(tag);
        VolleyUtil.getQueue(context).add(getRequest);
    }

    public static void getCompanyMapDetail(Context context, HashMap<String,String> params, String tag, final NetWorkCallBack netWorkCallBack){
        VolleyGetRequest<CompanyMapDetailDataModel> getRequest=new VolleyGetRequest<>(
                GetParamsUtil.getParmas(context.getString(R.string.req_jusfoun_url)+getDetailUrl,params)
                ,CompanyMapDetailDataModel.class, new Response.Listener<CompanyMapDetailDataModel>() {
                    @Override
                    public void onResponse(CompanyMapDetailDataModel response) {
                        netWorkCallBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        netWorkCallBack.onFail(VolleyErrorUtil.disposeError(error));
                    }
                },context);
        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(NetConstant.TIMEOUT, 1, 1.0f));
        getRequest.setTag(tag);
        VolleyUtil.getQueue(context).add(getRequest);
    }
}

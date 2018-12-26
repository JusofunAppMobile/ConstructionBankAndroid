package com.jusfoun.jusfouninquire.route;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qxb.jianhang.R;
import com.jusfoun.jusfouninquire.bean.InvestOrBranchModel;
import com.jusfoun.jusfouninquire.callback.NetWorkCallBack;
import com.jusfoun.jusfouninquire.constant.NetConstant;
import com.jusfoun.jusfouninquire.util.GetParamsUtil;
import com.jusfoun.jusfouninquire.util.VolleyUtil;
import com.jusfoun.jusfouninquire.volley.VolleyErrorUtil;
import com.jusfoun.jusfouninquire.volley.VolleyGetRequest;

import java.util.HashMap;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/16.
 * Description 获取对外投资或分支机构
 */
public class GetInvestOrBranch {

    private static final String getUrl="/api/GetEntBranchOrInvesment";

    public static void getInvestOrBranch(Context context, HashMap<String,String> params, String tag, final NetWorkCallBack callBack){
        VolleyGetRequest<InvestOrBranchModel> getRequest=new VolleyGetRequest<>(GetParamsUtil.getParmas(context.getString(R.string.req_jusfoun_url)+getUrl, params)
                , InvestOrBranchModel.class
                , new Response.Listener<InvestOrBranchModel>() {
            @Override
            public void onResponse(InvestOrBranchModel response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onFail(VolleyErrorUtil.disposeError(error));
            }
        },context);

        getRequest.setShouldCache(false);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(NetConstant.TIMEOUT, 1, 1.0f));
        getRequest.setTag(tag);
        VolleyUtil.getQueue(context).add(getRequest);
    }
}

package com.jusfoun.jusfouninquire.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.AppUtil;
import com.jusfoun.baselibrary.Util.LogUtil;
import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.Util.StringUtil;
import com.jusfoun.baselibrary.base.BaseModel;
import com.jusfoun.jusfouninquire.OffsetSharePrerence;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



/**
 * @author henzil
 * @version create time:15/1/21_下午4:21
 * @Description 重写关于volley get请求方法
 */
public class VolleyGetRequest<T extends BaseModel> extends Request<T> {
    private static final int DEFAULT_NET_TYPE = 0;

    private final Response.Listener<T> mListener;
    private Class<T> modelClass;
    private Context mContext;

    /**
     * 重新定义缓存key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        String params = "";
        try {
            params = getParams().toString();
        } catch (Exception e) {

        }
        return getUrl() + params;
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    /**
     * null 正常逻辑
     * 可以重写此方法，返回json数据
     * TODO
     *
     * @return
     */
    public String getDemoRespose() {
        return null;
    }

    public VolleyGetRequest(String url, Class<T> modelClass, Response.Listener<T> listener,
                            Response.ErrorListener errorListener, Context mContext) {
        super(Method.GET, url, errorListener);
        LogUtil.e("TAG", "request_url:========" + url);
        mListener = listener;
        this.mContext = mContext;
        this.modelClass = modelClass;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30*1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        setRetryPolicy(retryPolicy);

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerParams = new HashMap<String, String>();
        headerParams.put("Connection", "keep-alive");
        headerParams.put("Version", "2.1.7");
        headerParams.put("VersionCode", AppUtil.getVersionCode(mContext) + "");
        headerParams.put("AppType", "0");
        headerParams.put("Channel", StringUtil.getChannelName(mContext));
        headerParams.put("DeviceId", PhoneUtil.getIMEI(mContext));
        headerParams.put("Deviceid", PhoneUtil.getIMEI(mContext));
        return headerParams;
    }

    private Response<T> responseWrapper;

    /**
     * 解析 response
     *
     * @param response
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            if(response.headers!=null){
                LogUtil.e("tag","response.headers="+response.headers);
                OffsetSharePrerence.getComputeOffsetTime(mContext, response.headers.get("Date"));
            }
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "UTF-8"));
            LogUtil.e("tag", "网络请求结果=" + parsed);
            responseWrapper = Response
                    .success(new Gson().fromJson(parsed, modelClass), VolleyHttpHeaderParser.volleyParseCacheHeaders(response));

            return responseWrapper;
        } catch (UnsupportedEncodingException e) {
            Log.e("error", e.toString());
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(T response) {
        if(response == null) return;
//        ((BaseModel) response).cacheKey = this.getCacheKey();
//        ((BaseModel) response).isCache = responseWrapper.intermediate;
        mListener.onResponse(response);
    }

}


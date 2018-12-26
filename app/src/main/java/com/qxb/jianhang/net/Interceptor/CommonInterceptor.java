package com.qxb.jianhang.net.Interceptor;


import android.text.TextUtils;
import android.util.Log;

import com.jusfoun.baselibrary.model.UserModel;
import com.qxb.jianhang.ui.util.AppUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        UserModel user =AppUtils.getUserInfo();

        if (user != null && !TextUtils.isEmpty(user.id)) {

            HttpUrl.Builder builder = request.url()
                    .newBuilder().scheme(request.url().scheme())
                    .host(request.url().host());

            String postBodyString = bodyToString(request.body());
            FormBody.Builder formBody = new FormBody.Builder();

            if (TextUtils.isEmpty(postBodyString)) {
                builder.addQueryParameter("userId", user.id);
            } else {
//                formBody.add("userId", user.id);
                Log.e("tag","userId="+AppUtils.getUserInfo().id);
                formBody.add("userId", AppUtils.getUserInfo().id);
                formBody.add("userid", AppUtils.getUserInfo().id);
                if (request.body() instanceof FormBody) {
                    FormBody oldRormBpody =(FormBody) request.body();
                    for(int i=0;i<oldRormBpody.size();i++){
                        formBody.add(oldRormBpody.name(i),oldRormBpody.value(i));
                    }
                }else{
                    return chain.proceed(request);
                }
//                try {
//                    JSONObject obj = new JSONObject(postBodyString);
//                    obj.put("userId", user.id);
//                    postBodyString = obj.toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody.build());
            }
            Request newRequest = request.newBuilder()
                    .method(request.method(), formBody.build())
                    .url(builder.build())
                    .build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(request);
    }

    private String bodyToString(final RequestBody request) {
        try {
            if (request != null) {
                okio.Buffer buffer = new okio.Buffer();
                request.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = request.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                return buffer.readString(charset);
            }
        } catch (final IOException e) {
            return "";
        }
        return "";
    }

//    private UserModel getUserInfo() {
//        String userInfo = PreferenceUtils.getString(BaseApplication.getBaseApplication(), "pre_user_info");
//        if (!TextUtils.isEmpty(userInfo))
//            return new Gson().fromJson(userInfo, UserModel.class);
//        return new UserModel();
//    }
}

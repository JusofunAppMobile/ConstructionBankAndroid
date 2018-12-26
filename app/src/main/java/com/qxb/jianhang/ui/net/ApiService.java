package com.qxb.jianhang.ui.net;

import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import sun.bob.mcalendarview.data.CalendarData;

/**
 * @author liuguangdan
 * @version create at 2018/9/4/004 10:32
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("api/user/sendValidateCode")
    Observable<NetModel> sendValidateCode(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/user/appsearchOrgList")
    Observable<NetModel> searchOrgList(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/user/appreg")
    Observable<NetModel> register(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/user/applogin")
    Observable<NetModel> login(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/MessageList")
    Observable<NetModel> messageList(@FieldMap HashMap<String, Object> map);

    @POST("api/app/pushMessage")
    Observable<NetModel> pushMessage(@Body RequestBody json);

    @FormUrlEncoded
    @POST("api/app/followList")
    Observable<NetModel> getFollowList(@FieldMap HashMap<String, Object> map);

    @POST("api/app/addFollow")
    Observable<TrackRecordDataModel.TrackRecordItemModel> addFollowNet(@Body RequestBody json);

    @FormUrlEncoded
    @POST("api/app/customermark")
    Observable<NetModel> tagCompanyNet(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/searchMessage")
    Observable<NetModel> searchMessage(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/GetStatisticsByTopLeftAndBottomRightList")
    Observable<NetModel> getHomeMapNet(@FieldMap HashMap<String, String> map);

    @FormUrlEncoded
    @POST("api/app/GetGeoBoundingBoxList")
    Observable<NetModel> getCompanyListByPoint(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/GetMapListByType")
    Observable<NetModel> getCompanyList(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/myCustomer")
    Observable<NetModel> myCustomer(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/SearchCompany")
    Observable<NetModel> search(@FieldMap HashMap<String, Object> map);


    @FormUrlEncoded
    @POST("api/app/sendEmailCode")
    Observable<NetModel> sendEmailCode(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/statisticalAnalysis")
    Observable<NetModel> statisticalAnalysis(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/searchfollowListByCalendar")
    Observable<CalendarData> getFollowListData(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/searchfollowListByCalendarDetail")
    Observable<NetModel> getFollowListByDay(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/SendReport")
    Observable<NetModel> sendRoport(@FieldMap HashMap<String, Object> map);

    @FormUrlEncoded
    @POST("api/app/SearchCompanyByAddress")
    Observable<NetModel> searchNew(@FieldMap HashMap<String, Object> map);
}

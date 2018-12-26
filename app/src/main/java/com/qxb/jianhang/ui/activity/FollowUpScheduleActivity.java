package com.qxb.jianhang.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.jusfoun.baselibrary.Util.IOUtil;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;
import com.qxb.jianhang.ui.dialog.SelectTimeDialog;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.view.MyCalendarView;
import com.qxb.jianhang.ui.view.ProcessTableItemView;
import com.qxb.jianhang.ui.view.ReportDateView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import rx.functions.Action1;
import sun.bob.mcalendarview.data.CalendarData;

/**
 * @author zhaoyapeng
 * @version create time:2018/12/1010:59
 * @Email zyp@jusfoun.com
 * @Description ${跟进行程表}
 */
public class FollowUpScheduleActivity extends BaseBackActivity {
    protected MyCalendarView viewCalendar;
    protected RelativeLayout layoutNo;
    private LinearLayout scrollviewLinear;

    private ReportDateView reportDateView;

    Map<String, CalendarData.DayDate> dateMap = new HashMap<>();

//    private SelectTimeDialog startTimeDialog,endTimeDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_follow_up_schedule;
    }

    @Override
    public void initDatas() {
        reportDateView = new ReportDateView(mContext, R.style.my_dialog);
    }

    @Override
    public void initView() {
        scrollviewLinear = (LinearLayout) findViewById(R.id.layout_scrollview);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        tvRight = (TextView) findViewById(R.id.tvRight);
        viewCalendar = (MyCalendarView) findViewById(R.id.view_calendar);
        layoutNo = (RelativeLayout) findViewById(R.id.layout_no);

    }

    @Override
    public void initAction() {
        setTitle("跟进日程表");

        ivRight.setImageResource(R.drawable.img_send_report);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<TreeMap<String, Object>>() {
                        }.getType(),
                        new JsonDeserializer() {
                            @Override
                            public TreeMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

                                TreeMap<String, Object> treeMap = new TreeMap<>();
                                JsonObject jsonObject = json.getAsJsonObject();
                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entrySet) {
                                    treeMap.put(entry.getKey(), entry.getValue());
                                }
                                return treeMap;
                            }
                        }).create();


        final NetModel netModel = gson.fromJson(IOUtil.readTextFileFromRawResourceId(mContext, R.raw.my_recorde), NetModel.class);


//        showLoadDialog();
        viewCalendar.setCallback(new MyCalendarView.CallBack() {
            @Override
            public void selectDate(final String date) {
                if (!dateMap.containsKey(date)) {
                    layoutNo.setVisibility(View.VISIBLE);
                } else {
                    layoutNo.setVisibility(View.GONE);
                    scrollviewLinear.removeAllViews();
                    getDataByDay(date);
                }
            }
        });


//        ivRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reportDateView.show();
//            }
//        });

        reportDateView.setCallBack(new ReportDateView.CallBack() {
            @Override
            public void sendReport(String startTime, String endTime, String email) {
                FollowUpScheduleActivity.this.sendReport(startTime,endTime,email);
            }
        });
        getDateList();
    }

    @Override
    protected void onRightClick() {
        super.onRightClick();
        reportDateView.show();
    }

    private void getDateList() {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        addNetwork(Api.getInstance().getService(ApiService.class).getFollowListData(map), new Action1<CalendarData>() {
            @Override
            public void call(CalendarData net) {

                hideLoadDialog();
                if (net.success()) {
                    for (int i = 0; i < net.datelist.size(); i++) {
                        List<CalendarData.DayDate> dates = net.datelist.get(i).date;
                        for (int j = 0; j < dates.size(); j++) {
                            dateMap.put(dates.get(j).day, dates.get(j));
                        }
                    }
                    viewCalendar.setData(dateMap);
                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }


    private void getDataByDay(String date) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();

        map.put("date", date);
        addNetwork(Api.getInstance().getService(ApiService.class).getFollowListByDay(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    TrackRecordDataModel model = net.dataToObject(TrackRecordDataModel.class);
                    for (int i = 0; i < model.list.size(); i++) {
                        ProcessTableItemView processTableItemView = new ProcessTableItemView(mContext);
                        processTableItemView.setData(model.list.get(i), i);
                        scrollviewLinear.addView(processTableItemView);
                    }
//                    viewCalendar.setData(dateMap);
                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }


    private void sendReport(String startTime,String endTime,String email) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();

        map.put("startDate", startTime);
        map.put("endDate", endTime);
        map.put("email", email);

        addNetwork(Api.getInstance().getService(ApiService.class).sendRoport(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    reportDateView.dismiss();
                    Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();
                } else {
                    showToast(net.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                hideLoadDialog();
                ToastUtils.showHttpError();
            }
        });
    }
}

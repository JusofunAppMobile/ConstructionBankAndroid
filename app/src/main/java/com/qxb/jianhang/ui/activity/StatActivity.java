package com.qxb.jianhang.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.constraint.Group;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.jusfoun.baselibrary.Util.ToastUtils;
import com.jusfoun.baselibrary.model.UserModel;
import com.jusfoun.baselibrary.net.Api;
import com.jusfoun.baselibrary.net.NetModel;
import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.StatAdapter;
import com.qxb.jianhang.ui.base.BaseBackActivity;
import com.qxb.jianhang.ui.data.StatModel;
import com.qxb.jianhang.ui.net.ApiService;
import com.qxb.jianhang.ui.pop.StatDatePop;
import com.qxb.jianhang.ui.util.AppUtils;
import com.qxb.jianhang.ui.view.MyAxisValueFormatter;
import com.qxb.jianhang.ui.view.MyIntFormatter;
import com.qxb.jianhang.ui.view.MyPercentFormatter;
import com.qxb.jianhang.ui.view.MyYFormatter;
import com.qxb.jianhang.ui.view.MyYIntFormatter;
import com.qxb.jianhang.ui.view.ReportDateView;
import com.qxb.jianhang.ui.view.StatPercentView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @author liuguangdan
 * @version create at 2018/12/10/010 10:14
 * @Email lgd@jusfoun.com
 * @Description ${}
 */
public class StatActivity extends BaseBackActivity implements OnChartValueSelectedListener, StatDatePop.OnSelectListener {
    @BindView(R.id.pieChart)
    PieChart mChart;
    @BindView(R.id.barChart1)
    BarChart barChart1;
    @BindView(R.id.barChart2)
    HorizontalBarChart barChart2;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.vPercent1)
    StatPercentView vPercent1;
    @BindView(R.id.vPercent2)
    StatPercentView vPercent2;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.tvCusTotal)
    TextView tvCusTotal;
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvJob)
    TextView tvJob;

    @BindView(R.id.button3)
    TextView sendBtn;
    @BindView(R.id.vCusTotal)
    View vCusTotal;
    @BindView(R.id.group)
    Group group;
    private Typeface tf;

    private ReportDateView reportDateView;

    private StatModel statModel;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_stat;
    }

    Map<Integer, String> dateMap = new HashMap<>();
    private StatAdapter statAdapter;
    private StatDatePop statDatePop;

    private String startDate;
    private String endDate;

    @Override
    public void initDatas() {
        ButterKnife.bind(this);
        initChart();

        initBarChart();

        initHorizontalBar();

        statDatePop = new StatDatePop(this, this);

        statAdapter = new StatAdapter(this);
        recycler.setAdapter(statAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        scrollView.setNestedScrollingEnabled(false);

        vPercent1.setType(1);
        vPercent2.setType(2);

        initStartEndDate();

        reportDateView = new ReportDateView(mContext, R.style.my_dialog);
        tvDate.setText((Calendar.getInstance().get(Calendar.MONTH) + 1) + "月");
        load();

        UserModel user = AppUtils.getUserInfo();
        if (user != null) {
            tvName.setText(user.realName);
            tvJob.setText(user.organName);
        }
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void initStartEndDate() {
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTime());
        startDate = date.substring(0, 8) + "01";
        endDate = date;
    }

    private void load() {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        addNetwork(Api.getInstance().getService(ApiService.class).statisticalAnalysis(map), new Action1<NetModel>() {
            @Override
            public void call(NetModel net) {
                hideLoadDialog();
                if (net.success()) {
                    statModel = net.dataToObject(StatModel.class);
                    buildView();
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

    private void showNoDataText(Chart chart) {
        chart.clear();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void buildView() {
        if (statModel == null)
            return;
        tvTotal.setText(Html.fromHtml("客户总数：<font color='#FD904C'>" + statModel.clientCount + "</font>"));
        tvMonth.setText(Html.fromHtml("本月新增：<font color='#FD904C'>" + statModel.addCount + "</font>"));


        if (statModel.followCount + statModel.formalCount > 0) {
            List<PieEntry> list = new ArrayList<>();
            list.add(new PieEntry(statModel.followCount, "跟进客户"));
            list.add(new PieEntry(statModel.formalCount, "正式客户"));
            setData(list);
        } else {
            showNoDataText(mChart);
        }

        tvCusTotal.setText(String.valueOf(statModel.followCount + statModel.formalCount));

        vCusTotal.setVisibility(statModel.followCount + statModel.formalCount == 0 ? View.GONE : View.VISIBLE);


        if (statModel.addTrendList != null && !statModel.addTrendList.isEmpty()) {
            group.setVisibility(View.VISIBLE);
            List<BarEntry> barList1 = new ArrayList<>();
            List<BarEntry> barList2 = new ArrayList<>();
            dateMap.clear();

            for (int i = 0; i < statModel.addTrendList.size(); i++) {
                StatModel.AddTrendListBean bean = statModel.addTrendList.get(i);
                barList1.add(new BarEntry(i + 1, bean.followCount));
                barList2.add(new BarEntry(i + 1, bean.formalCount));
                dateMap.put(i + 1, bean.date);
            }

            addBarData(barList1, barList2);
        } else {
            group.setVisibility(View.GONE);
            showNoDataText(barChart1);
        }

        vPercent1.setValue((int) statModel.followRate * 100, statModel.followRateState);
        vPercent2.setValue((int) statModel.formaltRate * 100, statModel.formaltRateState);


        if (statModel.followSitList != null && !statModel.followSitList.isEmpty()) {
            List<BarEntry> barList3 = new ArrayList<>();
            Map<Integer, String> map = new HashMap<>();
            for (int i = 0; i < statModel.followSitList.size(); i++) {
                barList3.add(new BarEntry(i + 1, statModel.followSitList.get(i).count));
                map.put(i + 1, statModel.followSitList.get(i).label);
            }
            if (statModel.followSitList.size() < 5)
                for (int i = 0; i < 5 - statModel.followSitList.size(); i++)
                    barList3.add(new BarEntry(barList3.size() + 1, -1));
            setHorizontalBarData(barList3, map);
        } else {
            showNoDataText(barChart2);
        }


        if (statModel.browseList != null && !statModel.browseList.isEmpty()) {
            tvMore.setVisibility(statModel.browseList.size() > 5 ? View.VISIBLE : View.GONE);
            if (statAdapter.getList() != null)
                statAdapter.getList().clear();
            statAdapter.addList(statModel.browseList.subList(0, statModel.browseList.size() > 5 ? 5 : statModel.browseList.size()));
        } else {
            if (statAdapter.getList() != null) {
                statAdapter.getList().clear();
                statAdapter.notifyDataSetChanged();
            }
            tvMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initAction() {
    }

    /**
     * 客户构成 图表
     */
    private void initChart() {
        mChart.setNoDataText("暂无数据");
        mChart.setNoDataTextColor(Color.parseColor("#999999"));
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
//        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationEnabled(true);//拖拽滚动时，手放开是否会持续滚动，默认是true（false是拖到哪是哪，true拖拽之后还会有缓冲）
        mChart.setDragDecelerationFrictionCoef(0.99f);//与上面那个属性配合，持续滚动时的速度快慢，[0,1) 0代表立即停止。


        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setExtraOffsets(45.f, 10.f, 45.f, 10.f);

        mChart.setDrawHoleEnabled(true); // 设置中间区域的背景
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(80);// 中间区域占有比例
        mChart.setTransparentCircleRadius(0);

        mChart.setDrawCenterText(false); // 是否显示中间文本

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true); // 点击突出

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);


        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        mChart.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字（true：下面属性才有效果）

    }

    /**
     * 客户构成 图表
     */
    private void setData(List<PieEntry> entries) {

        final PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f); //设置饼状Item之间的间隙
        dataSet.setSelectionShift(4f); // //设置饼状Item被选中时变化的距离

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.parseColor("#FFBF00"));
        colors.add(Color.parseColor("#00B1BF"));
        colors.add(Color.parseColor("#F66666"));
        colors.add(Color.parseColor("#47C7E8"));
        colors.add(Color.parseColor("#FF8F9D"));
//        colors.add(Color.parseColor("#16CF45"));
        colors.add(Color.parseColor("#A5E7BA"));
        colors.add(Color.parseColor("#109FD9"));
        colors.add(Color.parseColor("#F2CC1D"));
        colors.add(Color.parseColor("#DDA0DD"));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.parseColor("#9A9691")); // // 标记线的颜色
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        final PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyPercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.parseColor("#9A9691"));// 标记线外的文字颜色
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        dataSet.setValueLineColor(Color.TRANSPARENT); // // 标记线的颜色
        data.setValueTextColor(Color.TRANSPARENT);// 标记线外的文字颜色
        mChart.animateY(800, Easing.EasingOption.EaseInQuad);
        mChart.invalidate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dataSet.setValueLineColor(Color.parseColor("#9A9691")); // // 标记线的颜色
                data.setValueTextColor(Color.parseColor("#9A9691"));// 标记线外的文字颜色
                mChart.invalidate();
            }
        }, 1200);
    }

    /**
     * 客户增长走势 图表
     */
    private void initBarChart() {
        barChart1.setExtraOffsets(0, 0, 0, 0);
        barChart1.setFitBars(true);
        barChart1.setNoDataText("暂无数据");
        barChart1.setNoDataTextColor(Color.parseColor("#999999"));
        barChart1.setDrawBarShadow(false);
        barChart1.setDrawValueAboveBar(true); //这里设置为true每一个直方图的值就会显示在直方图的顶部
        barChart1.getDescription().setEnabled(false);
        barChart1.getLegend().setEnabled(false);
        barChart1.getAxisRight().setEnabled(false);
        barChart1.setPinchZoom(false);  // 如果设置为true，挤压缩放被打开。如果设置为false，x和y轴可以被单独挤压缩放。
        barChart1.setScaleXEnabled(false); // 打开或关闭x轴的缩放
        barChart1.setScaleYEnabled(false); // 打开或关闭y轴的缩放
        barChart1.setDrawBorders(false); //启用/禁用绘制图表边框（chart周围的线）。
        barChart1.setBorderColor(Color.YELLOW); //设置 chart 边框线的颜色。
        barChart1.setDrawGridBackground(false); //  如果启用，chart 绘图区后面的背景矩形将绘制。
        barChart1.setGridBackgroundColor(Color.RED); //  设置网格背景应与绘制的颜色。 ,不包括坐标
        barChart1.setHighlightFullBarEnabled(false);
        barChart1.setHighlightPerDragEnabled(false);
        barChart1.setHighlightPerTapEnabled(false);


        XAxis xAxis = barChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置XAxis出现的位置。
        xAxis.setTypeface(tf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
//        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
//        xAxis.setLabelCount(7); //一个界面显示10个Lable。那么这里要设置11个
//        xAxis.setAxisMinimum(-1);
        xAxis.setValueFormatter(new MyAxisValueFormatter(dateMap));
        xAxis.setTextColor(Color.parseColor("#999999"));
//        xAxis.setAvoidFirstLastClipping(true); // 如果设置为true，则在绘制时会避免“剪掉”在x轴上的图表或屏幕边缘的第一个和最后一个坐标轴标签项。
//        if (type == 1)
//            xAxis.setLabelRotationAngle(-0);//设置X轴字体显示角度
//        else
//            xAxis.setLabelRotationAngle(0);//设置X轴字体显示角度
        xAxis.setTextSize(8);
        xAxis.setSpaceMin(1);
        xAxis.setSpaceMax(1);
        xAxis.setCenterAxisLabels(true);

//        IAxisValueFormatter custom = new MyAxisValueFormatter();
        YAxis leftAxis = barChart1.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTypeface(tf);
        leftAxis.setLabelCount(6, true);
        leftAxis.setGranularity(1);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setSpaceMin(1);
        leftAxis.setSpaceMax(1);
        leftAxis.setValueFormatter(new MyYIntFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        leftAxis.setSpaceTop(15f); // 设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）
//        leftAxis.setSpaceBottom(15f); // 设置在图表上最低处的值相比轴上最低处值的底部空间（总轴范围的百分比）
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)  设置一个自定义的最小值为这条轴，如果设置不为0，这个值将不会依赖于提供的数据自动计算。
        leftAxis.setDrawGridLines(true); //不设置Y轴网格
        leftAxis.setEnabled(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGridColor(Color.parseColor("#E8E8E8"));
        leftAxis.setTextColor(Color.parseColor("#999999"));
    }


    /**
     * 客户增长走势 图表
     */
    public void addBarData(List<BarEntry> list1, List<BarEntry> list2) {


        float groupSpace = 0.2f;
        float barSpace = 0.1f; // x4 DataSet
        float barWidth = 0.3f; // x4 DataSet
//        (barWidth + barSpace) * barAmount + groupSpace = (0.3 + 0.05) * 2 + 0.3 = 1.00

        BarDataSet set1, set2;

        if (barChart1.getData() != null &&
                barChart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart1.getData().getDataSetByIndex(0);
            set1.setValues(list1);
            set2 = (BarDataSet) barChart1.getData().getDataSetByIndex(1);
            set2.setValues(list2);

            barChart1.getData().notifyDataChanged();
            barChart1.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(list1, "A");
            set1.setColors(new int[]{ColorTemplate.rgb("#FFBF00")});
            set2 = new BarDataSet(list2, "B");
            set2.setColors(new int[]{ColorTemplate.rgb("#25A7F5")});


            BarData data = new BarData(set1, set2);
//            data.setValueTextSize(10f);
//            data.setValueFormatter(new BarTopDataFormatter());
//            data.setValueTextColor(Color.RED);
            data.setDrawValues(false);
            data.setValueTypeface(tf);


            barChart1.setData(data);
        }

        barChart1.getBarData().setBarWidth(barWidth);

        barChart1.getXAxis().setAxisMinimum(1);

        barChart1.getXAxis().setAxisMaximum(7);

        barChart1.groupBars(1, groupSpace, barSpace);

        barChart1.animateY(1200);

    }

    /**
     * 客户跟进情况 图表
     */
    private void initHorizontalBar() {

        XAxis xl = barChart2.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setAxisLineWidth(1.5f);
        xl.setAxisLineColor(Color.parseColor("#E2E2E2"));
        xl.setSpaceMin(1);
        xl.setSpaceMax(1);
//        xl.setGranularity(10f);
        xl.setTextSize(12);
        xl.setTextColor(Color.parseColor("#666666"));
//        xl.setAxisMinimum(1);
//        xl.setAxisMaximum(5);

        YAxis yl = barChart2.getAxisLeft();
        yl.setEnabled(false);
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = barChart2.getAxisRight();
        yr.setEnabled(false);
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

//        barChart2.getAxisLeft().setEnabled(false);
//        barChart2.getAxisRight().setEnabled(false);
//
//        XAxis yl = barChart2.getXAxis();
//        yl.setTypeface(tf);
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
//        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setValueFormatter(new MyYFormatter());

        barChart2.setNoDataText("暂无数据");
        barChart2.setNoDataTextColor(Color.parseColor("#999999"));
//        barChart2.getAxisRight().setEnabled(false);
        barChart2.getDescription().setEnabled(false);
        barChart2.getLegend().setEnabled(false);
        barChart2.setMaxVisibleValueCount(6);
        barChart2.setFitBars(true);
        barChart2.setPinchZoom(false);
        barChart2.setDrawBarShadow(false);
        barChart2.setHighlightPerTapEnabled(false);
        barChart2.setScaleXEnabled(false); // 打开或关闭x轴的缩放
        barChart2.setScaleYEnabled(false); // 打开或关闭y轴的缩放
        barChart2.setHighlightFullBarEnabled(false);
        barChart2.setHighlightPerDragEnabled(false);
        barChart2.setHighlightPerTapEnabled(false);
    }


    /**
     * 客户跟进情况 图表
     */
    private void setHorizontalBarData(List<BarEntry> values, Map<Integer, String> map) {

        barChart2.getXAxis().setValueFormatter(new MyYFormatter(map));
        float barWidth = 0.4f;
        float spaceForBar = 10f;

        BarDataSet set1;

        if (barChart2.getData() != null &&
                barChart2.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart2.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart2.getData().notifyDataChanged();
            barChart2.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#61D8FA"));
            colors.add(Color.parseColor("#E47CB9"));
            colors.add(Color.parseColor("#FF8E1A"));
            colors.add(Color.parseColor("#74DF70"));
            colors.add(Color.parseColor("#F85C5C"));
            set1.setColors(colors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);
            data.setValueFormatter(new MyIntFormatter());
            data.setBarWidth(barWidth);
            barChart2.setData(data);
        }
        barChart2.invalidate();

    }


    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    @OnClick({R.id.ivBack, R.id.vDate, R.id.tvMore, R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.vDate:
                statDatePop.showAsDropDown(view);
                break;

            case R.id.tvMore:
                if (statModel == null)
                    return;
                Intent intent = new Intent(this, ReadHistoryActivity.class);
                intent.putExtra("model", new Gson().toJson(statModel.browseList));
                startActivity(intent);
                break;
            case R.id.button3:
                reportDateView.show();
                break;
        }
    }

    @Override
    public void select(String start, String end, int month) {
        tvDate.setText(month + "月");
        startDate = start;
        endDate = end;
        load();
    }
}

package com.ed.shuneladmin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.orderStatistics;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

/*----------------------------------------------統計圖表----------------------------------------------*/

public class StatisticsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnSelectDateListener {

    /*************************************************************************
     * 常數數宣告區
     *************************************************************************/
    private static final String TAG = "StatisticsFragment";
    private Activity activity;
    private CommonTask dateTask;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    orderStatistics orderSs;

    /*************************************************************************
     * 變數數宣告區
     *************************************************************************/

    Date myDate;
    Date startDate;
    Date endDate;
    String strStart;
    String strEnd;
    private int dataVar = 1;
    List<PieEntry> ordersEntries=null;
    List<orderStatistics> orderStatisticsList;

    /**************************** View元件變數 *********************************/

    private PieChart pieChart;
    private TextView tvStartDate;

    private TextView tvEndDate;

    private Button btn_SendDate;

    private static int year, month, day;

    /**************************** Adapter元件變數 ******************************/

    /**************************** Array元件變數 ********************************/


    /**************************** 資料集合變數 *********************************/

    /**************************** 物件變數 *************************************/

    /*************************************************************************
     * Override 函式宣告 (覆寫)
     *************************************************************************/


    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setListener();


    }

    private void findViews(View view) {

        pieChart = view.findViewById(R.id.pieChart);
        btn_SendDate = view.findViewById(R.id.btn_SendDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvStartDate = view.findViewById(R.id.tvStartDate);
    }

    private void initData() {

        /* 設定可否旋轉 */
        pieChart.setRotationEnabled(true);
        /* 設定圓心文字 */
        pieChart.setCenterText("商品銷售比例");
        /* 設定圓心文字大小 */
        pieChart.setCenterTextSize(15);

        int colorBlack = Color.parseColor("#000000");
        pieChart.setEntryLabelColor(colorBlack);

        Description description = new Description();
        pieChart.setDescription(description);

    }

    private void setSystemServices() {
    }

    private void setListener() {


        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOneDayPicker();
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndOneDayPicker();
            }
        });


        btn_SendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //為填入日期防呆裝置
                if (startDate == null || endDate == null) {
                    Toast.makeText(activity, "請填入日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                //日期輸入錯誤防呆裝置
                if (startDate.after(endDate)) {
                    Toast.makeText(activity, "日期有誤", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Common.networkConnected(activity)) {
//                    List<orderStatistics> orderStatisticsList = new ArrayList<>();
                    String url = Common.URL_SERVER + "Orders_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getStatistics");
                    jsonObject.addProperty("date1", strStart);
                    jsonObject.addProperty("date2", strEnd);
                    dateTask = new CommonTask(url, jsonObject.toString());
                    try {
                        String jsonIn = dateTask.execute().get();
//                        get as list
                        Type listType = new TypeToken<List<orderStatistics>>() {
                        }.getType();
                        orderStatisticsList = gson.fromJson(jsonIn, listType);
                        Log.e(TAG,"jsoin:"+jsonIn);
                        initData();
                        ordersEntries = getOrdersEntries(orderStatisticsList);

                        PieDataSet pieDataSet = new PieDataSet(ordersEntries, "");
                        pieDataSet.setValueTextColor(Color.BLACK);
                        pieDataSet.setValueTextSize(20);
                        pieDataSet.setSliceSpace(3);

                        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
                        pieDataSet.setColors(Color_test.JOYFUL_COLORS);
                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.invalidate();

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }
            }

        });



    }

    private List<PieEntry> getOrdersEntries(List<orderStatistics> List) {
        List<PieEntry> ordersEntries = new ArrayList<>();
        if (List == null){
            ordersEntries.add(new PieEntry(1, "1"));
            Log.e(TAG, "1");
            return ordersEntries;
        }
        try {
            Log.e(TAG, "2");
            for (orderStatistics os: List
            ) {
                if (os.getCATEGORY_ID() ==1){
                    ordersEntries.add(new PieEntry(os.getSumBUY_PRICE(),os.getPRODUCT_NAME()));
                }else if (os.getCATEGORY_ID() ==2){
                    ordersEntries.add(new PieEntry(os.getSumBUY_PRICE(),os.getPRODUCT_NAME()));
                }else if(os.getCATEGORY_ID() ==3){
                    ordersEntries.add(new PieEntry(os.getSumBUY_PRICE(),os.getPRODUCT_NAME()));
                }

            }


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

//        x: order date, y: sumBuyPrice

//        ordersEntries.add(new Entry(orderSs.getORDER_DATE().getDate(), orderSs.getSumBUY_PRICE()));


        return ordersEntries;
    }



    @Override
    public void onStart() {
        /* 查尋資料庫或是ContentProvider,取得所需資料 */
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /* 覆寫OnDateSetListener.onDateSet()以處理日期挑選完成事件。
               日期挑選完成會呼叫此方法，並傳入選取的年月日 */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StatisticsFragment.year = year;
        StatisticsFragment.month = month;
        StatisticsFragment.day = dayOfMonth;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSelect(List<java.util.Calendar> calendars) {

        for (Calendar c : calendars
        ) {
            //判斷開始日 結束數
            //dataVar 為控制變數 1＝開始日 2=結束日
            // 為開始日
            if (dataVar == 1) {
                startDate = c.getTime();
                strStart = dateToStr(c.getTime());
                tvStartDate.setText(dateToStr(c.getTime()));
                dataVar++;
            } else {
                //為結束日
                endDate = c.getTime();
                strEnd = dateToStr(c.getTime());
                tvEndDate.setText(dateToStr(c.getTime()));
                dataVar = 1;
            }
        }
    }

    private String dateToStr(Date time) {
        //設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //進行轉換
        String dateString = sdf.format(time);
        System.out.println(dateString);

        return dateString;
    }

    private String dateToStr2(Date time) {
        //設定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //進行轉換
        String dateString = sdf.format(time);
        System.out.println(dateString);

        return dateString;
    }


    private void openOneDayPicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 3);


        DatePickerBuilder oneDayBuilder = new DatePickerBuilder(activity, StatisticsFragment.this)
                .setPickerType(CalendarView.ONE_DAY_PICKER)
                .setDate(max)
                .setHeaderColor(R.color.sampleLighter)
                .setHeaderLabelColor(R.color.currentMonthDayColor)
                .setSelectionColor(R.color.daysLabelColor)
                .setTodayLabelColor(R.color.colorAccent)
                .setDialogButtonsColor(R.color.sampleLighter)
                .setDisabledDaysLabelsColor(R.color.sampleLighter)
                .setMinimumDate(min)
                .setMaximumDate(max)
                .setTodayColor(R.color.sampleLighter)
                .setHeaderVisibility(View.VISIBLE)
                .setDisabledDays(getDisabledDays());

        com.applandeo.materialcalendarview.DatePicker oneDayPicker = oneDayBuilder.build();
        oneDayPicker.show();
    }


    private void EndOneDayPicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 3);

        DatePickerBuilder oneDayBuilder = new DatePickerBuilder(activity, StatisticsFragment.this)
                .setPickerType(CalendarView.ONE_DAY_PICKER)
                .setDate(max)
                .setHeaderColor(R.color.colorPrimaryDark)
                .setHeaderLabelColor(R.color.currentMonthDayColor)
                .setSelectionColor(R.color.daysLabelColor)
                .setTodayLabelColor(R.color.colorAccent)
                .setDialogButtonsColor(android.R.color.holo_green_dark)
                .setDisabledDaysLabelsColor(android.R.color.holo_purple)
                .setMinimumDate(min)
                .setMaximumDate(max)
                .setTodayColor(R.color.sampleLighter)
                .setHeaderVisibility(View.VISIBLE)
                .setDisabledDays(getDisabledDays());

        com.applandeo.materialcalendarview.DatePicker oneDayPicker = oneDayBuilder.build();
        oneDayPicker.show();
    }


    private List<java.util.Calendar> getDisabledDays() {
        java.util.Calendar firstDisabled = DateUtils.getCalendar();
        firstDisabled.add(java.util.Calendar.DAY_OF_MONTH, 2);

        java.util.Calendar secondDisabled = DateUtils.getCalendar();
        secondDisabled.add(java.util.Calendar.DAY_OF_MONTH, 1);

        java.util.Calendar thirdDisabled = DateUtils.getCalendar();
        thirdDisabled.add(java.util.Calendar.DAY_OF_MONTH, 18);

        List<java.util.Calendar> calendars = new ArrayList<>();
        calendars.add(firstDisabled);
        calendars.add(secondDisabled);
        calendars.add(thirdDisabled);
        return calendars;
    }

    private static class Color_test {
        /**
         * an "invalid" color that indicates that no color is set
         */
        public static final int COLOR_NONE = 0x00112233;

        /**
         * this "color" is used for the Legend creation and indicates that the next
         * form should be skipped
         */
        public static final int COLOR_SKIP = 0x00112234;

        /**
         * THE COLOR THEMES ARE PREDEFINED (predefined color integer arrays), FEEL
         * FREE TO CREATE YOUR OWN WITH AS MANY DIFFERENT COLORS AS YOU WANT
         */
        public static final int[] LIBERTY_COLORS = {
                Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
                Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
        };
        public static final int[] JOYFUL_COLORS = {
                Color.rgb(147, 236, 76), Color.rgb(203, 254, 255), Color.rgb(254, 247, 120),
                Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)
        };
        public static final int[] PASTEL_COLORS = {
                Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
                Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)
        };
        public static final int[] COLORFUL_COLORS = {
                Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
                Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
        };
        public static final int[] VORDIPLOM_COLORS = {
                Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
        };
//        public static final int[] MATERIAL_COLORS = {
//                rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")
//        };

    }
}

package com.ed.shuneladmin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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

    /*************************************************************************
     * 變數數宣告區
     *************************************************************************/

    Date myDate;
    Date startDate;
    Date endDate;
    String strStart;
    String strEnd;
    private int dataVar = 1;

    /**************************** View元件變數 *********************************/

    LineChart lineChart;

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
        setLinstener();
    }

    private void findViews(View view) {
        lineChart = view.findViewById(R.id.lineChart);
        btn_SendDate = view.findViewById(R.id.btn_SendDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvStartDate = view.findViewById(R.id.tvStartDate);
    }

    private void initData() {

        lineChart.setBackgroundColor(Color.WHITE);
        /* 取得並設定X軸標籤文字 */
        XAxis xAxis = lineChart.getXAxis();
        /* 設定最大值到12(月) */
        xAxis.setAxisMaximum(12);

        /* 取得左側Y軸物件 */
        YAxis yAxisLeft = lineChart.getAxisLeft();
        /* 設定左側Y軸最大值 */
        yAxisLeft.setAxisMaximum(16000);

        /* 取得右側Y軸物件 */
        YAxis yAxisRight = lineChart.getAxisRight();
        /* 是否顯示右側Y軸 */
        yAxisRight.setEnabled(false);

        /* 設定右下角描述文字 */
        Description description = new Description();
        description.setText("Car Sales in Taiwan");
        description.setTextSize(16);
        lineChart.setDescription(description);

    }

    private void setSystemServices() {
    }

    private void setLinstener() {


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
                    String url = Common.URL_SERVER + "Orders_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getStatistics");
                    jsonObject.addProperty("date1", strStart);
                    jsonObject.addProperty("date2", strEnd);
                    dateTask = new CommonTask(url, jsonObject.toString());
                    try {
                        String jsonIn = dateTask.execute().get();
                        Log.e(TAG,"jsoin:"+jsonIn);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }

            }
        });


        /* 監聽是否點選chart內容值 */
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            /* entry: 儲存著被點選項目的值；包含X與Y軸的值
             *  highlight: 儲存著標記相關資訊，也包含X與Y軸的值
             *  有entry物件就不太需要highlight物件 */
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                String text = (int) entry.getX() + "\n" + (int) entry.getY();
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /* 取得各品牌車每月銷售量資料 */
        List<Entry> toyotaEntries = getToyotaEntries();
        List<Entry> nissanEntries = getNissanEntries();

        /* 利用List<Entry>資料建立LineDataSet，line chart需要LineDataSet資料集來繪圖 */
        LineDataSet lineDataSetToyota = new LineDataSet(toyotaEntries, "Toyota");
        /* 設定資料圓點半徑 */
        lineDataSetToyota.setCircleRadius(4);
        /* 設定資料圓點是否中空 */
        lineDataSetToyota.setDrawCircleHole(true);
        /* 設定資料圓點顏色 */
        lineDataSetToyota.setCircleColor(Color.RED);
        /* 設定線的顏色 */
        lineDataSetToyota.setColor(Color.BLUE);
        /* 設定線的粗細 */
        lineDataSetToyota.setLineWidth(4);
        /* 設定highlight線的顏色 */
        lineDataSetToyota.setHighLightColor(Color.CYAN);
        /* 設定資料點上的文字顏色 */
        lineDataSetToyota.setValueTextColor(Color.DKGRAY);
        /* 設定資料點上的文字大小 */
        lineDataSetToyota.setValueTextSize(10);

        LineDataSet lineDataSetNissan = new LineDataSet(nissanEntries, "Nissan");
        lineDataSetNissan.setCircleRadius(4);
        lineDataSetNissan.setDrawCircleHole(false);
        lineDataSetNissan.setCircleColor(Color.MAGENTA);
        lineDataSetNissan.setColor(Color.GREEN);
        lineDataSetNissan.setLineWidth(4);
        lineDataSetNissan.setHighLightColor(Color.CYAN);
        lineDataSetNissan.setValueTextColor(Color.DKGRAY);
        lineDataSetNissan.setValueTextSize(10);

        /* 有幾個LineDataSet，就繪製幾條線 */
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSetToyota);
        dataSets.add(lineDataSetNissan);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();


    }

    private List<Entry> getToyotaEntries() {
        List<Entry> toyotaEntries = new ArrayList<>();
        /* 一個Entry儲存著一筆資訊，在此代表X與Y軸的值 */
        toyotaEntries.add(new Entry(1, 10000));
        toyotaEntries.add(new Entry(2, 12000));
        toyotaEntries.add(new Entry(3, 13500));
        toyotaEntries.add(new Entry(4, 12500));
        toyotaEntries.add(new Entry(5, 13300));
        toyotaEntries.add(new Entry(6, 12400));
        toyotaEntries.add(new Entry(7, 11500));
        toyotaEntries.add(new Entry(8, 12500));
        toyotaEntries.add(new Entry(9, 12300));
        toyotaEntries.add(new Entry(10, 13000));
        toyotaEntries.add(new Entry(11, 13200));
        toyotaEntries.add(new Entry(12, 14000));
        return toyotaEntries;
    }

    private List<Entry> getNissanEntries() {
        List<Entry> nissanEntries = new ArrayList<>();
        nissanEntries.add(new Entry(1, 3000));
        nissanEntries.add(new Entry(2, 3200));
        nissanEntries.add(new Entry(3, 3500));
        nissanEntries.add(new Entry(4, 3150));
        nissanEntries.add(new Entry(5, 3300));
        nissanEntries.add(new Entry(6, 3400));
        nissanEntries.add(new Entry(7, 3120));
        nissanEntries.add(new Entry(8, 3250));
        nissanEntries.add(new Entry(9, 3300));
        nissanEntries.add(new Entry(10, 3230));
        nissanEntries.add(new Entry(11, 3350));
        nissanEntries.add(new Entry(12, 3400));
        return nissanEntries;
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
//                Log.e(TAG,"1 i"+dataVar+"\t"+c.getTime()+"\t"+startDate);
                dataVar++;
//                Log.e(TAG,"2 i"+dataVar);
            } else {
                //為結束日
                endDate = c.getTime();
                strEnd = dateToStr(c.getTime());
                tvEndDate.setText(dateToStr(c.getTime()));
//                Log.e(TAG,"3 i"+dataVar);
                dataVar = 1;
//                Log.e(TAG,"4 i"+dataVar);
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


    private void openOneDayPicker() {
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
}

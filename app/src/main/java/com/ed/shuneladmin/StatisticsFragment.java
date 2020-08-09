package com.ed.shuneladmin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

/*----------------------------------------------統計圖表----------------------------------------------*/

public class StatisticsFragment extends Fragment {


    /*************************************************************************
     * 常數數宣告區
     *************************************************************************/
    private static final String TAG = "StatisticsFragment";
    private Activity activity;
    /*************************************************************************
     * 變數數宣告區
     *************************************************************************/

    /**************************** View元件變數 *********************************/

    LineChart lineChart;


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
        /* 監聽是否點選chart內容值 */
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            /* entry: 儲存著被點選項目的值；包含X與Y軸的值
             *  highlight: 儲存著標記相關資訊，也包含X與Y軸的值
             *  有entry物件就不太需要highlight物件 */
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                String text = (int)entry.getX() + "\n" + (int)entry.getY();
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


}
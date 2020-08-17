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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartFragment extends Fragment {

    private static final String TAG = "TAG_PieChart";
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle(R.string.textPieChart);
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PieChart pieChart = view.findViewById(R.id.pieChart);
//setting chart basic
        /* 設定可否旋轉 */
        pieChart.setRotationEnabled(true);
        /* 設定圓心文字 */
        pieChart.setCenterText("August");
        /* 設定圓心文字大小 */
        pieChart.setCenterTextSize(25);

        Description description = new Description();
        description.setText("Car Sales in Taiwan");
        description.setTextSize(25);
        pieChart.setDescription(description);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.d(TAG, "entry: " + entry.toString() + "; highlight: " + highlight.toString());
                PieEntry pieEntry = (PieEntry) entry;
                String text = pieEntry.getLabel() + "\n" + pieEntry.getValue();
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        /* 取得各品牌車單月銷售量資料 */
        List<PieEntry> pieEntries = getSalesEntries();

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Categories");
        pieDataSet.setValueTextColor(Color.BLUE);
        pieDataSet.setValueTextSize(20);
        pieDataSet.setSliceSpace(2);

        /* 使用官訂顏色範本，顏色不能超過5種，否則官定範本要加顏色 */
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
//get data
    private List<PieEntry> getSalesEntries() {
        List<PieEntry> salesEntries = new ArrayList<>();
        salesEntries.add(new PieEntry(11875, "Toyota"));
        salesEntries.add(new PieEntry(4055, "Mitsubishi"));
        salesEntries.add(new PieEntry(3043, "Nissan"));
        salesEntries.add(new PieEntry(2585, "Honda"));
        salesEntries.add(new PieEntry(2204, "M-Benz"));
        return salesEntries;
    }
}
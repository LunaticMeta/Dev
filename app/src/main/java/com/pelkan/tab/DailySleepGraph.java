package com.pelkan.tab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by jhj0104 on 2016-10-27.
 */

public class DailySleepGraph extends Activity{
    LineChart lineChart;

    int backColor = Color.parseColor("#ffffff"); //#e6f9f0
    int lineColor = Color.parseColor("#32b95b"); //#36d79a //#f9e6ef


    @Override
    public void onCreate(Bundle savedInstanceState){
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "AccValue.db", 1);
        DBHelper dbHelper2 = new DBHelper(getApplicationContext(), "SLEEP_DATA.db", 1);
        DBHelper dbHelper3 = new DBHelper(getApplicationContext(), "DAYofWEEK.db", 1);
        DBHelper dbHelper4 = new DBHelper(getApplicationContext(), "index_DAYofWEEK.db", 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        Intent intent = getIntent();
        Data data = (Data) intent.getSerializableExtra("data");
        TextView txtmDate = (TextView) findViewById(R.id.myDate);
        txtmDate.setText("날짜: "+String.valueOf(data.Date)+"_"+String.valueOf(data.time));

        //TEXT
        TextView text_dailySleepTime = (TextView) findViewById(R.id.dailySleepTime);
        String Str_SleepTime = dbHelper2.getSleepTime(String.valueOf(data.Date), String.valueOf(data.time), 4);
        int SleepTime = Integer.parseInt(Str_SleepTime);
        int hours = SleepTime / 60;
        int min = SleepTime - (hours*60);
        text_dailySleepTime.setText("Sleep time : "+hours+"hr "+min+"min");

        //GRAPH
        lineChart = (LineChart) findViewById(R.id.dailyLineGraph);
        showdailyLineGraph(String.valueOf(data.Date),String.valueOf(data.time));
        //showdailyBarGraph(String.valueOf(data.Date),String.valueOf(data.time));

    }

    public void showdailyLineGraph(String SleepSTTDate, String SleepSTTTime){
        DBHelper dbHelper = new DBHelper(getApplicationContext(), "AccValue.db", 1);
        DBHelper dbHelper2 = new DBHelper(getApplicationContext(), "SLEEP_DATA.db", 1);
        DBHelper dbHelper3 = new DBHelper(getApplicationContext(), "DAYofWEEK.db", 1);
        DBHelper dbHelper4 = new DBHelper(getApplicationContext(), "index_DAYofWEEK.db", 1);

        int totalDay = dbHelper2.getSleepDay(); //Day는 총 수면 일수 받을거
        ArrayList<String> labels = dbHelper.getSleepTime(SleepSTTDate, SleepSTTTime);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Float> sleepYData = dbHelper.getSleepAcc();
        ArrayList<Float> sleepYData_test = dbHelper.select(SleepSTTDate, SleepSTTTime);

        for(int i=0; i<sleepYData_test.size(); i++){
            entries.add(new Entry(sleepYData_test.get(i), i));
        }

        //---------------↓↓ Create new daily LINE Graph ↓↓--------------- //
        LineDataSet lineDataSet = new LineDataSet(entries, "DailySleepLineGraph");
        lineDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawFilled(true); //선아래로 색상표시
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        //lineDataSet.setColors(new int[] { lineColor });
        //lineChart.animateXY(500,500);
        lineChart.setBackgroundColor(backColor);
        lineDataSet.setColors(new int[]{lineColor});
        lineDataSet.setFillColor(lineColor);

        //--------------- ↓↓ Axis styling  ↓↓ ---------------//
        LineData lineData = new LineData(labels, lineDataSet);
        lineChart.setData(lineData);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        YAxis yLeft = lineChart.getAxisLeft();
        yLeft.setTextColor(Color.BLACK);
        yLeft.removeAllLimitLines();
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);


        XAxis x = lineChart.getXAxis();
        x.setTextColor(Color.BLACK);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.BLACK);
        lineChart.setDrawBorders(true);
    }
    public void btn_chkFin(View v){
        finish();
    }

}

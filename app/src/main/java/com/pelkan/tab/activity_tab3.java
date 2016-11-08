package com.pelkan.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by jhj0104 on 2016-10-18.
 */

//---------------------------------------------↓↓ TAB 3 ↓↓--------------------------------------------- //
public class activity_tab3 extends Fragment{
    DBHelper dbHelper;
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    int wakeState= Color.parseColor("#d15650");         //위험상태(R)    :d15650
    int sleepState = Color.parseColor("#5cd660");       //수면상태(G)    :5cd660
    int deepSleepState = Color.parseColor("#25DDDD");   //숙면상태(B)    :25DDDD
    int MightyRose = Color.parseColor("#FFE4E0");

    int barColor[] ={};
    int[] SleepTimeWeekAVG = {10,400,10,250,420,10,40,0};  //criterion(min)
    String[] weekName_en = {"MON","TUE","WED","THU","FRI","SAT","SUN","ALL"};
    String[] weekName_kr = {"일","월","화","수","목","금","토","통합"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page3, container, false);
        super.onCreate(savedInstanceState);
        final DBHelper dbHelper = new DBHelper(getContext(), "AccValue.db", 1);
        final DBHelper dbHelper2 = new DBHelper(getContext(), "SLEEP_DATA.db", 1);
        final DBHelper dbHelper3 = new DBHelper(getContext(), "DAYofWEEK.db", 1);
        final DBHelper dbHelper4 = new DBHelper(getContext(), "index_DAYofWEEK.db", 1);

        barChart = (BarChart) rootView.findViewById(R.id.SleepTimeWeekAVG_barGraph);
        TextView text_dayTotal = (TextView) rootView.findViewById(R.id.txt_DayTotal);
        TextView text_SleepAVG = (TextView) rootView.findViewById(R.id.txt_SleepAVG);
        TextView text_SleepEff = (TextView) rootView.findViewById(R.id.txt_SleepEff);
        int totalDay = dbHelper2.getSleepDay(); //Day는 총 수면 일수 받을거
        int sleepAVG = dbHelper3.getWeekAVG(8);
        int sleepEff = dbHelper3.getWeekAVG(8);
        int hours = sleepAVG/60;
        int min = sleepAVG - (hours*60);
        text_dayTotal.setText(String.valueOf(totalDay));
        text_SleepAVG.setText(hours+"시간 "+min+"분");

        SleepTimeWeekBarGraph();
        return rootView;
    }

    //Save Random value in BarEntry
    public void SleepTimeWeekBarGraph(){

        barEntries = new ArrayList<>();
        barColor = new int[8];
        ArrayList<String> WeekName = new ArrayList<String>();
        for(int i=0; i<8;i++) WeekName.add(weekName_kr[i]); //일 월 화 수 목 금 토

        final DBHelper dbHelper3 = new DBHelper(getContext(), "DAYofWEEK.db", 1);
        List<Integer> list_SleepAVG = dbHelper3.getSleepAVG();
        //int[] SleepAVG = (int[]) list_SleepAVG.toArray(int[list_SleepAVG.size()]);
        //int[] intArray2 = ArrayUtils.toPrimitive(myList.toArray(NO_INTS));
        //int[] SleepAVG = Ints.toArray(list_SleepAVG);
        for(int i=0, len = list_SleepAVG.size(); i < len; i++)
            SleepTimeWeekAVG[i] = list_SleepAVG.get(i);

        //create bar (set YAxis value & color)
        int goodTime[]={840,720,660,600,540,480,420,420,420}; //나이에 따른 수면 충족 시간 0~8 step
        int normalTime[]={660,600,570,510,450,420,360,360,330};

        for(int i=0; i<8; i++){
            barEntries.add(new BarEntry(SleepTimeWeekAVG[i],i));
            if(SleepTimeWeekAVG[i] >=goodTime[6]) barColor[i] = deepSleepState; //(성인 하루 적정 수면 7시간(이상) * 60 = 420분)
            else if(SleepTimeWeekAVG[i]>=normalTime[6]) barColor[i] = sleepState;
            else barColor[i] =wakeState;
        } //같은 연령대와 비교하는 것 필요

        //---------------↓↓ Create new BAR Graph ↓↓--------------- //
        BarDataSet barDataSet = new BarDataSet(barEntries, "Weekend");
        BarData barData = new BarData(WeekName, barDataSet);
        barDataSet.setColors(barColor);
        barChart.setData(barData);

        barDataSet.setBarSpacePercent(30f);
        barDataSet.setHighLightAlpha(50);
        barChart.animateXY(1500,2000);
        barChart.setDrawHighlightArrow(true);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);


        //---------------↓↓ XAxis styling STT ↓↓--------------- //
        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);

        //---------------↓↓ YAxis styling STT ↓↓--------------- //
        YAxis Lyaxis = barChart.getAxisLeft();
        barChart.getAxisLeft().setEnabled(true); //왼쪽 전체 control
        barChart.getAxisRight().setEnabled(false); //오른쪽 전체 control

        barChart.getLegend().setEnabled(false);
        barChart.setDescription("");
        barChart.setDescriptionTextSize(15f);
        barChart.setDescriptionPosition(450f, 80f);
        //barChart.setBackgroundColor(MightyRose);//set background color
        barChart.setDrawBorders(true);
        barChart.getAxisLeft().setAxisMaxValue(700);
        barChart.getAxisLeft().setAxisMinValue(0);
    }
}
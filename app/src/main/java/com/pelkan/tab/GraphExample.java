package com.pelkan.tab;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by jhj0104 on 2016-10-27.
 */

//---------------------------------------------↓↓ TAB 3 ↓↓--------------------------------------------- //
public class GraphExample extends Activity {

    int wakeState= Color.parseColor("#d15650"); // 위험상태(R)          :d15650
    int sleepState = Color.parseColor("#5cd660"); //수면상태(G)         :5cd660
    int deepSleepState = Color.parseColor("#25DDDD"); // 숙면상태(B)    :25DDDD
    int MightyRose = Color.parseColor("#FFE4E0");
    int barColor[] ={};

    com.github.mikephil.charting.charts.BarChart barChart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> dates;
    Random random;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        //기간에 해당하는 그래프 생성
        //createRandomBarGraph("2016/9/16", "2016/12/17");
    }

    //Save Random value in BarEntry
    public void createRandomBarGraph(String Date1, String Date2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);
            barEntries = new ArrayList<>();
            barColor = new int[dates.size()];
            random = new Random();
            float max = 0f;
            float value = 0f;

            //bar 생성
            for(int j=0; j<dates.size(); j++){
                max = 100f;
                value = random.nextFloat()*max;
                if(value<=25) barColor[j] = wakeState;
                else if(value <=80) barColor[j] = sleepState;
                else barColor[j] = deepSleepState;
                barEntries.add(new BarEntry(value,j));
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }


        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        BarData barData = new BarData(dates, barDataSet);
        barDataSet.setColors(barColor);

        barDataSet.setBarSpacePercent(30f);
        barDataSet.setHighLightAlpha(50);
        barChart.setData(barData);
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
        xAxis.setDrawAxisLine(true); // ??
        xAxis.setDrawGridLines(false); // ??



        //---------------↓↓ YAxis styling STT ↓↓--------------- //
        YAxis Lyaxis = barChart.getAxisLeft();
        /*
        Lyaxis.setDrawLabels(false); //세로 값
        Lyaxis.setDrawGridLines(false); //세로 보조 선
        Lyaxis.setDrawAxisLine(false); //세로 선
        Lyaxis.setDrawZeroLine(false); //0 세로 선*/
        barChart.getAxisRight().setEnabled(false); //오른쪽 전체 control
        barChart.getAxisLeft().setEnabled(false); //왼쪽 전체 control


        barChart.getLegend().setTextColor(sleepState); //legend 색상
        barChart.getLegend().setEnabled(false);
        barChart.setDescription("Day sleepwell BarGraph1!!");
        barChart.setDescriptionTextSize(15f);
        barChart.setDescriptionPosition(450f, 80f);
        //barChart.groupBars(1980f, 0.06f, 0.02f); 3.0beta
        barChart.setBackgroundColor(MightyRose);//set background color
        barChart.setDrawBorders(true);
    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<>();
        int i=0;
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }
    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH)+1)+ "/"+ (cld.get(Calendar.DAY_OF_MONTH));
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyyy/MM/dd").format(date);

        } catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }
}
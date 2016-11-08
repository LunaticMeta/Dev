package com.pelkan.tab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jhj0104 on 2016-10-25.
 */


//---------------------------------------------↓↓ DB 선언 ↓↓--------------------------------------------- //
public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE SLEEP_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT, SleepDate TEXT, SleepSTTtime TEXT, SleepFINtime TEXT, SleepTime TEXT, SleepEfficiency TEXT);");
        db.execSQL("CREATE TABLE AccValue (_id INTEGER PRIMARY KEY AUTOINCREMENT, sleepMode TEXT,accX TEXT, accY TEXT, accZ TEXT, AccAVG TEXT, create_date TEXT, create_time TEXT, record_time TEXT);");
        db.execSQL("CREATE TABLE DAYofWEEK (_id INTEGER PRIMARY KEY AUTOINCREMENT, SUN TEXT, MON TEXT, TUE TEXT, WED TEXT, THU TEXT, FRI TEXT, SAT TEXT, TOTAL TEXT);");
        db.execSQL("CREATE TABLE index_DAYofWEEK (_id INTEGER PRIMARY KEY AUTOINCREMENT, SUN TEXT, MON TEXT, TUE TEXT, WED TEXT, THU TEXT, FRI TEXT, SAT TEXT, TOTAL TEXT);");
    }

    public void insert( String sleepMode, String accX, String accY, String accZ,  String AccAVG, String create_date, String create_time, String record_time) {
        // 읽고 쓰기가 가능한 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO AccValue VALUES(null, '" + sleepMode + "','" + accX + "','" + accY + "','" + accZ + "', '" + AccAVG + "', '" + create_date+ "' ,'" + create_time+ "' , '"+record_time+"');");
        db.close();
    }

    public void insert_SleepData(String SleepDate, String SleepSTTtime, String SleepFINtime, String SleepTime, String SleepEfficiency) {
        // 읽고 쓰기가 가능한 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO SLEEP_DATA VALUES(null, '" + SleepDate + "" + "','" + SleepSTTtime+ "','" + SleepFINtime+ "','" + SleepTime+ "', '" + SleepEfficiency+ "');");
        db.close();
    }

    public void insert_DayofWeek(String Sun, String Mon, String Tue, String Wed, String Thu, String Fri, String Sat, String Total){
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO DAYofWEEK VALUES(null, '"+Sun+"' ,'" + Mon + "' ,'" + Tue + "','" + Wed+ "','" + Thu+ "', '" + Fri+ "', '"+Sat+"','"+Total+"' );");
        db.close();
    }

    public void insert_index_DayofWeek(String Sun, String Mon, String Tue, String Wed, String Thu, String Fri, String Sat, String Total){
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO index_DAYofWEEK VALUES(null, '"+Sun+"' ,'" + Mon + "' ,'" + Tue + "','" + Wed+ "','" + Thu+ "', '" + Fri+ "', '"+Sat+"','"+Total+"');");
        db.close();
    }


    //---------------------------------------------↓↓ DB DELETE ↓↓--------------------------------------------- //
    public void delete(String create_at) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM AccValue WHERE create_at='" + create_at + "';");
        db.close();
    }

    //---------------------------------------------↓↓ DB UPADATE ↓↓--------------------------------------------- //
    public void update_SleepData(String SleepDate, int index, String change) {
        SQLiteDatabase db = getWritableDatabase();

        if(index==2) db.execSQL("UPDATE SLEEP_DATA SET SleepSTTtime=" + change+ " WHERE SleepDate='" + SleepDate + "';");
        if(index==3) db.execSQL("UPDATE SLEEP_DATA SET SleepFINtime=" + change+ " WHERE SleepDate='" + SleepDate + "';");
        if(index==4) db.execSQL("UPDATE SLEEP_DATA SET SleepTime=" + change + " WHERE SleepDate='" + SleepDate + "';");
        if(index==5) db.execSQL("UPDATE SLEEP_DATA SET SleepEfficiency=" + change + " WHERE SleepDate='" + SleepDate + "';");

        db.close();
    }

    public void update_DayofWeek(int day, String sleepTime, int newDay) {
        SQLiteDatabase db = getWritableDatabase();
        String[] Day = {"SUN","MON","TUE","WED","THU","FRI","SAT","TOTAL"};

        db.execSQL("UPDATE DAYofWEEK SET "+Day[day]+" = " + sleepTime +";");
        db.execSQL("UPDATE index_DAYofWEEK SET "+Day[day]+" = " + Integer.toString(newDay) +";");
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM AccValue", null);
        while (cursor.moveToNext()) {
            result +=
                    " Day: "
                    + cursor.getString(5)
                    +"  ID: "
                    +cursor.getString(0)
                    + " \n sleepMode: "
                    + cursor.getString(1)
                    + " sleepAcc: "
                    + cursor.getString(4)
                    + "\n";
        }
        db.close();
        return result;
    }

    //---------------------------------------------↓↓ DB get Information ↓↓--------------------------------------------- //
    //수면 accAVG 데이터 받기
    public ArrayList<Float> getSleepAcc(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Float> accData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM AccValue", null);
        cursor.moveToFirst();
        do{
            accData.add((Float.parseFloat(cursor.getString(5))));
        }
        while (cursor.moveToNext());

//        while (cursor.moveToNext()) {
//            accData.add((Float.parseFloat(cursor.getString(5))));
//        }
        db.close();
        return accData;
    }

    //수면 accAVG 데이터 받기
    public ArrayList<Float> getSleepMode(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Float> ModeData = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM AccValue", null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
//            if(cursor.getString(1) == "WAKE") ModeData.add(3f);
//            else if(cursor.getString(1) == "REM") ModeData.add(2f);
//            else if(cursor.getString(1) == "NREM") ModeData.add(1f);
            ModeData.add(Float.parseFloat(cursor.getString(1)));
        }
        cursor.close();
        db.close();
        return ModeData;
    }

    // get XAxis label
    public ArrayList<String> getSleepTime(String StartDate, String StartTime){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> sleepTime = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM AccValue where create_date="+StartDate+" AND create_time = "+StartTime , null);
        cursor.moveToFirst();
        do {
//            정시에만 x축값을 띄운다.
//            if(((Float.valueOf(cursor.getString(7)) %100) !=0)) sleepTime.add(" ");
//            else sleepTime.add((cursor.getString(7)));
            if(cursor!=null) sleepTime.add((cursor.getString(8)));
        }
        while(cursor.moveToNext());
//        while (cursor.moveToNext()) {
//            sleepTime.add((cursor.getString(8)));
//        }
        cursor.close();
        db.close();
        return sleepTime;
    }

    // get YAxis label
    //db.execSQL("CREATE TABLE AccValue (_id INTEGER PRIMARY KEY AUTOINCREMENT, sleepMode TEXT,accX TEXT, accY TEXT, accZ TEXT, AccAVG TEXT, create_date TEXT, create_time TEXT);");
    public ArrayList<Float> select(String StartDate, String StartTime){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Float> accData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM AccValue where create_date = "+StartDate+" AND create_time = "+StartTime,null);
        cursor.moveToFirst();

//        do{
//        //while(cursor.moveToNext()){
////            if(Float.valueOf(cursor.getString(5)) <= 0.1f) accData.add(1.0f);
////            else if(Float.valueOf(cursor.getString(5)) <= 1f) accData.add(2.0f);
////            else accData.add(3.0f);
//            accData.add(Float.valueOf(cursor.getString(5)));
//        }
//        while(cursor.moveToNext());
        while (cursor.moveToNext()) {
            accData.add(Float.valueOf(cursor.getString(5)));
        }

        cursor.close();
        db.close();
        return accData;
    }

    // 수면 AVG data
    //db.execSQL("CREATE TABLE SLEEP_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT, SleepDate TEXT, SleepSTTtime TEXT, SleepFINtime TEXT, SleepTime TEXT, SleepEfficiency TEXT);");
    //db.execSQL("INSERT INTO DAYofWEEK VALUES(null, '"+Sun+"' ,'" + Mon + "' ,'" + Tue + "','" + Wed+ "','" + Thu+ "', '" + Fri+ "', '"+Sat+"','"+Total+"' );");
    public ArrayList<String> getSleepAVG(int index) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> accData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM SLEEP_DATA", null);
        while (cursor.moveToNext()) {
            accData.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return accData;
    }

    public String getSleepTime(String StartDate, String StartTime, int index) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SLEEP_DATA where SleepDate = "+StartDate+" AND SleepSTTtime = "+StartTime, null);
        String sleepTime=null;
        if(cursor!=null && cursor.moveToFirst()) sleepTime = cursor.getString(index);
        cursor.close();
        db.close();
        if(sleepTime==null) return "0";
        return sleepTime;
    }

    public int getSleepDay(){
        int sleepday=0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM SLEEP_DATA", null);
        if(cursor!=null && cursor.moveToLast()) sleepday = cursor.getInt(0);
        cursor.close();
        db.close();
        return sleepday;
    }

    //db.execSQL("INSERT INTO DAYofWEEK VALUES(null, '"+Sun+"' ,'" + Mon + "' ,'" + Tue + "','" + Wed+ "','" + Thu+ "', '" + Fri+ "', '"+Sat+"','"+Total+"' );");
    public int getWeekAVG(int index){
        int weekAVG=0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM DAYofWEEK", null);
        if(cursor!=null && cursor.moveToFirst()) weekAVG = cursor.getInt(index);
        cursor.close();
        db.close();
        return weekAVG;
    }
    public int getWeekNum(int index){
        int i_week=0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM index_DAYofWEEK", null);
        if(cursor!=null && cursor.moveToFirst()) i_week = cursor.getInt(index);
        cursor.close();
        db.close();
        return i_week;
    }

    public ArrayList<Integer> getSleepAVG(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> SleepAVG = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT  * FROM DayofWEEK", null);
        for(int i=1;i<=8;i++){
            if(cursor!=null && cursor.moveToFirst()) SleepAVG.add(cursor.getInt(i));
        }
        cursor.close();
        db.close();
        return SleepAVG;
    }
}

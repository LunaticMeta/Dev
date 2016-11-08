package com.pelkan.tab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

/*
 * Created by jhj0104 on 2016-10-18.
 */

public class Activity_Record extends AppCompatActivity {
    DBHelper dbHelper;
    DBHelper dpHelper2;

    TextClock clk;
    SQLiteDatabase sql;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void onBackPressed() {
    }

    private SensorManager manager;
    private Sensor accSensor;
    boolean use_filter = true;

    private float mX, mY, mZ;
    private float mXYZ[] = {0f, 0f, 0f};

    private Kalman mKalmanAccX;
    private Kalman mKalmanAccY;
    private Kalman mKalmanAccZ;

    int move = 0;
    int Rem = 0;
    int NRem = 0;
    int NRemI = 0;
    float prev_xyz[] = {0f, 0f, 0f};
    float now_xyz[] = {0f, 0f, 0f};
    float prevAcc, nowAcc;
    double MaxAcc = 0;
    int timerI = 0;
    int loop = 0;
    boolean RemFlag = false;
    boolean sFlag = false;

    double modePercent[]={0,0,0};

    // Get 현재 날짜-시간 & 출력될 포맷 설정
    String[] Day = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT","TOTAL"};
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    int day = calendar.get(Calendar.DAY_OF_WEEK) + 1;
    final String create_Date = (new SimpleDateFormat("yyyyMMdd").format(date));
    final String create_Time = (new SimpleDateFormat("HHmm").format(date));
    final Date interestingDate = new Date();
    String nowTime = (new SimpleDateFormat("HHmm").format(date));


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "AccValue.db", 1);
        final DBHelper dbHelper2 = new DBHelper(getApplicationContext(), "SLEEP_DATA.db", 1);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //현재 시간 출력
        clk = (TextClock) findViewById(R.id.textClock1);
        clk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TODO Auto-generated method stub
            Toast.makeText(getBaseContext(), clk.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // 칼만필터 초기화
        mKalmanAccX = new Kalman(0.0f);
        mKalmanAccY = new Kalman(0.0f);
        mKalmanAccZ = new Kalman(0.0f);

        // 기울기 센서 등록
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(sel, accSensor, SensorManager.SENSOR_DELAY_GAME);

        final Handler handler = new Handler();
        TimerTask myTask = new TimerTask() {
            @Override
            public void run() {
                //Log.d("myTask","run()");
                handler.post(new Runnable() {
                    public void run() {
                        prevAcc = 0f;
                        nowAcc = 0f;

                        if (loop <= 10) { //0.2초마다 센서값 받아오기
                            //가속도 센서 합의 평균 구하기
                            for (int i = 0; i < 3; i++) {
                                prev_xyz[i] = now_xyz[i];
                                prevAcc += now_xyz[i];
                            }
                            for (int i = 0; i < 3; i++) {
                                now_xyz[i] = mXYZ[i];
                                nowAcc += now_xyz[i];
                            }
                            double acc = (abs(prevAcc - nowAcc) / 3.0);

                            //소수점 7번째 자리에서 내림
                            double Acc = Math.floor(acc * 10000000d) / 10000000d;
                            //가장 큰 값으로 저장
                            if (loop == 0) MaxAcc = Acc;
                            else if (MaxAcc < Acc) MaxAcc = Acc;
                            loop++;
                        }
                        else {//2초마다 상태 구하기
                            if (RemFlag == true && MaxAcc <= 0.01) {
                                Rem++;
                                NRem++;
                            } else if (MaxAcc < 0.1) NRem++;
                            else {
                                move++;
                                NRemI = 0;
                            }

                            if (timerI >= 4) { //10초마다 db입력
                                String now_mode;
                                if (RemFlag == true && Rem >= 4) now_mode = "Rem";
                                else if (NRem > move) {
                                    now_mode = "NRem";
                                    NRemI++;
                                }
                                else {
                                    now_mode = "Wake";
                                    NRemI = 0;
                                    RemFlag = false;
                                }
                                if (NRemI >= 3) {
                                    RemFlag = true;
                                    NRemI = 0;
                                }
                                if(MaxAcc >= 0.5) MaxAcc = 0.5;

                                String create_date = create_Date.toString();
                                String create_time = create_Time.toString();
                                String record_time = clk.getText().toString();
                                //String record_time = (new SimpleDateFormat("HHmm").format(date));
                                String accAVG = Double.toString(MaxAcc);
                                String accX = Float.toString(mX);
                                String accY = Float.toString(mY);
                                String accZ = Float.toString(mZ);

                                if(now_mode=="Rem") modePercent[2]+=1;
                                else if(now_mode=="NRem") modePercent[1]+=1;
                                else if(now_mode=="Wake") modePercent[0]+=1;

                                dbHelper.insert(now_mode, accX, accY, accZ, accAVG, create_date, create_time, record_time);
                                sFlag = true;
                                timerI = 0;
                                Rem = 0;
                                NRem = 0;
                                move = 0;
                            }
                            loop = 0;
                            timerI++;
                        }
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(myTask, 200, 200); //0.2초마다 loop
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private SensorEventListener sel = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float filteredX = 0.00f;
            float filteredY = 0.00f;
            float filteredZ = 0.00f;

            // 칼만필터를 적용한다
            filteredX = (float) mKalmanAccY.update(x);
            filteredY = (float) mKalmanAccX.update(y);
            filteredZ = (float) mKalmanAccZ.update(z);

            use_filter = false;
            //칼만필터를 사용 여부
            if (use_filter == false) {
                filteredX = x;
                filteredY = y;
            }

            mX = filteredX;
            mY = filteredY;
            mZ = filteredZ;
            mXYZ[0] = filteredX;
            mXYZ[1] = filteredY;
            mXYZ[2] = filteredZ;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    public void onClick_stopRecord(View v) {
        final DBHelper dbHelper2 = new DBHelper(getApplicationContext(), "SLEEP_DATA.db", 1);
        final DBHelper dbHelper3 = new DBHelper(getApplicationContext(), "DAYofWEEK.db", 1);
        final DBHelper dbHelper4 = new DBHelper(getApplicationContext(), "index_DAYofWEEK.db", 1);
        if (sFlag == true) {
            long difference = (new Date()).getTime() - interestingDate.getTime();

            if(day==7)  day=8;
            else if(day>=2) day-=1;
            else day +=6;
            int weekSleepAVG = dbHelper3.getWeekAVG(day);
            int i_week = dbHelper4.getWeekNum(day);
            int totalSleepAVG = dbHelper3.getWeekAVG(8);
            int i_total = dbHelper4.getWeekNum(8);

            int totalDay = dbHelper2.getSleepDay();
            if(totalDay==0){
                dbHelper4.insert_index_DayofWeek("0","0","0","0","0","0","0","0");
                dbHelper3.insert_DayofWeek("0","0","0","0","0","0","0","0");
            }

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            int newAVG = ((weekSleepAVG * i_week) + (hours * 60 + min)) / (i_week + 1);
            int totalAVG = ((totalSleepAVG * i_total) + (hours * 60 + min)) / (i_total + 1);
            dbHelper3.update_DayofWeek(day, Integer.toString(newAVG), i_week + 1);
            dbHelper3.update_DayofWeek(7, Integer.toString(totalAVG), i_total + 1);

            double totalmode = modePercent[0]+modePercent[1]+modePercent[2];
            double effect = (modePercent[2]/totalmode*100)+ ((modePercent[1]/totalmode*100)*0.5);
            String Effect = Integer.toString((int)effect);

            dbHelper2.insert_SleepData(create_Date, create_Time, nowTime,Integer.toString((hours*60+min)),Effect);
            Toast.makeText(getBaseContext(), "새로운 수면 기록이 추가되었습니다 >_<", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "수면시간이 짧아 기록되지 않았습니다 ㅜ_ㅜ", Toast.LENGTH_SHORT).show();
        }
        //moveTaskToBack(true);
        finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onResume() {
        manager.registerListener(sel, accSensor, SensorManager.SENSOR_DELAY_GAME);
//        ArrayAdapter adapter = dailySleepAdapter.getAdapter();
//        adapter.notifyDataSetChanged();
//        dailySleepAdapter.setAdapter(adapter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        manager.unregisterListener(sel);
        super.onPause();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activity_Record Page")
                // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
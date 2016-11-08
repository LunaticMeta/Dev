package com.pelkan.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.pelkan.tab.R.id.timeNum;


//---------------------------------------------↓↓ TAB 1 ↓↓--------------------------------------------- //
public  class activity_tab1 extends Fragment {

    private GoogleApiClient client;
    TextView wakeTime;
    TimePicker simpleTimePicker;
    TimePicker clk;
    Button btn_recordSTT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page1, container, false);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String WakeupHour = new SimpleDateFormat("HH").format(date);
        String WakeupMin = new SimpleDateFormat("MM").format(date);
        clk = (TimePicker) rootView.findViewById(R.id.simpleTimePicker);

        wakeTime = (TextView) rootView.findViewById(timeNum);
        simpleTimePicker = (TimePicker) rootView.findViewById(R.id.simpleTimePicker);
        btn_recordSTT=(Button) rootView.findViewById(R.id.RecordSTT);

        btn_recordSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Record.class);
                startActivity(intent);
            }
        });

        simpleTimePicker.setIs24HourView(false); // used to display AM/PM mode
        wakeTime.setText(WakeupHour + " : " + WakeupMin);
        // perform set on time changed listener event
        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int WakeupHour, int WakeupMin) {
                wakeTime.setText(WakeupHour + " : " + WakeupMin);
            }
        });
        return rootView;
    }

}


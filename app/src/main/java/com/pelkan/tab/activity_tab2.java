package com.pelkan.tab;

/**
 * Created by jhj0104 on 2016-10-18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.widget.Toast.makeText;

//---------------------------------------------↓↓ TAB 2 ↓↓--------------------------------------------- //
public class activity_tab2 extends Fragment {

    DBHelper dbHelper;
    static ListView listView;

    //http://androidhuman.com/205
    //리스트 제목 두줄 출력 참고!!

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_page2, container, false);
        final ArrayList<String> dayList = new ArrayList<String>();

        final DBHelper dbHelper = new DBHelper(getContext(), "AccValue.db", 1);
        final DBHelper dbHelper2 = new DBHelper(getContext(), "SLEEP_DATA.db", 1);

        int totalDay = dbHelper2.getSleepDay(); //Day는 총 수면 일수 받을거
        List<String> SleepSTTDate = dbHelper2.getSleepAVG(1);
        List<String> SleepSTTTime = dbHelper2.getSleepAVG(2);
        final String[] SleepStartDate = SleepSTTDate.toArray(new String[SleepSTTDate.size()]);
        final String[] SleepStartTime = SleepSTTTime.toArray(new String[SleepSTTTime.size()]);

        for(int i=0; i<totalDay; i++){
            dayList.add(i,Integer.toString(i+1)+". "+SleepStartDate[i]+" "+SleepStartTime[i]);
        }

        final ArrayAdapter<String> dailySleepAdapter;
        dailySleepAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, dayList);

        ListView dailyListView = (ListView) rootView.findViewById(R.id.dailyListView);
        dailyListView.setAdapter(dailySleepAdapter);
        dailySleepAdapter.notifyDataSetChanged();

        dailyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view,int position, long id) {
                String str = dayList.get(position);
                String a = str + " 선택";
                makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
                Data data = new Data(SleepStartDate[position], SleepStartTime[position]);
                Intent intent = new Intent(getContext(),DailySleepGraph.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void refresh(){
        final DBHelper dbHelper = new DBHelper(getContext(), "AccValue.db", 1);
        final DBHelper dbHelper2 = new DBHelper(getContext(), "SLEEP_DATA.db", 1);
        final ArrayList<String> dayList = new ArrayList<String>();

        int totalDay = dbHelper2.getSleepDay(); //Day는 총 수면 일수 받을거
        List<String> SleepSTTDate = dbHelper2.getSleepAVG(1);
        List<String> SleepSTTTime = dbHelper2.getSleepAVG(2);
        final String[] SleepStartDate = SleepSTTDate.toArray(new String[SleepSTTDate.size()]);
        final String[] SleepStartTime = SleepSTTTime.toArray(new String[SleepSTTTime.size()]);

        for(int i=0; i<totalDay; i++){
            dayList.add(i,Integer.toString(i+1)+". "+SleepStartDate[i]+" "+SleepStartTime[i]);
        }
    }
}


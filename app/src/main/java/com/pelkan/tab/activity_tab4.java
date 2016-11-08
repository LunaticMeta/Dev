package com.pelkan.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

/**
 * Created by jhj0104 on 2016-10-18.
 */

//---------------------------------------------↓↓ TAB 4 ↓↓--------------------------------------------- //
public class activity_tab4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page4, container, false);
        final ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("내 정보 설정");
        arrayList.add("감도 설정");
        arrayList.add("숙면 TIP");
        arrayList.add("앱 소개");

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, arrayList);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String str = arrayList.get(position);
                String a = str + " 선택";
                makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}

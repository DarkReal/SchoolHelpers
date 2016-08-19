package com.xxw.student.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.shouye_detail.CompanySearch;
import com.xxw.student.shouye_detail.city_select;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.view.search_history.RecordSQLiteOpenHelper2;

/**
 * 首页--头部
 * Created by xxw on 2016/3/26.
 */
public class titleFragment_shouye extends Fragment {


    private LinearLayout select_location;
    private View view;
    private ViewGroup mcontainer;
    private LayoutInflater minflater;

    private PopupWindow popupWindow;

    private LinearLayout search_icon;
    private TextView located;
    private String current_city="";

    /*数据库变量*/
    private RecordSQLiteOpenHelper2 helper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;


    private LinearLayout search_linear;
    private ListView search_list;
    private TextView clear_history;
    private View content;
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState){


        minflater = inflater;
        mcontainer = container;
        view=inflater.inflate(R.layout.main_title_shouye, container, false);


        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);

        MainActivity.city = preferences.getString("currcity","");

        located = (TextView) view.findViewById(R.id.located);
        LogUtils.v("MainActivity.city"+MainActivity.city);
        located.setText(MainActivity.city);
        Intent intent = ((MainActivity)getActivity()).getIntent();

        if(intent.getStringExtra("current_city")!=null){
            current_city = intent.getStringExtra("current_city");
            LogUtils.v("current_city" + "city" + current_city + "is");
            located.setText(current_city);
        }



        select_location = (LinearLayout) view.findViewById(R.id.select_location);
        search_icon = (LinearLayout) view.findViewById(R.id.search_icon);
        //单击转到查询页面
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), CompanySearch.class);
                startActivity(intent);
            }
        });


        select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), city_select.class);
                intent.putExtra("city", located.getText());
                startActivity(intent);
            }
        });

        return view;
    }
}

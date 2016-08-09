package com.xxw.student.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.shouye_detail.city_select;
import com.xxw.student.utils.LogUtils;

/**
 * 首页--头部
 * Created by xxw on 2016/3/26.
 */
public class titleFragment_shouye extends Fragment {


    private LinearLayout select_location;
    private View view;
    private ViewGroup mcontainer;
    private LayoutInflater minflater;
    private mypop pop;

    private LinearLayout search_icon;
    private TextView located;
    private String current_city="";

    public View onCreateView(final LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){


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
        search_icon.setOnClickListener(new View.OnClickListener() {


            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            @Override
            public void onClick(View v) {

                View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.main_title_shouye_search, null);
                TextView locate = (TextView) contentView.findViewById(R.id.location);

                locate.setText(located.getText().toString());

                pop = new mypop(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                View rootview = LayoutInflater.from(view.getContext()).inflate(R.layout.main_title_shouye, null);

                //view.invalidate();
                pop.setBackgroundDrawable(new ColorDrawable(00000000));
                pop.setAnimationStyle(R.style.popup_anim);
                pop.showAtLocation(rootview, Gravity.TOP,0,45);
                lp.alpha=0.6f;
                getActivity().getWindow().setAttributes(lp);
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
    public class mypop extends PopupWindow {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

        public mypop(View contentView, int width, int height, boolean focusable) {
            super(contentView,width,height,focusable);

        }

        @Override
        public void dismiss() {
            super.dismiss();
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
        }
    }
}

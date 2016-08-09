package com.xxw.student.fragment.wode_fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xxw.student.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的-我的回复页面
 * Created by xxw on 2016/4/9.
 */
public class wodehuifuFragment extends BaseFragment {
    private View rootView;
    private List<Map<String,String>> dataList;
    private ListView huifu_list;
    private SimpleAdapter simple_adapter;
    private String[] huifu_yh = new String[]{
            "大虫子",
            "1111",
            "汪泺"
    };
    private String[] huifu_time = new String[]{
            "2016-03-28",
            "2016-07-02",
            "2016-06-08"
    };
    private String[] huifu_content = new String[]{
            "学姐你真棒",
            "学姐你真棒",
            "学姐你真棒"
    };

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodehuifu,container,false);

        setDefault();

        return rootView;
    }

    private ImageView back;
    private GestureDetector mGestureDetector;
    private LinearLayout root_huifu;
    public void setDefault(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_wodehuifu);
        back.setOnClickListener(this);
        huifu_list = (ListView) rootView.findViewById(R.id.huifu_list);
        root_huifu = (LinearLayout)rootView.findViewById(R.id.root_huifu);
        root_huifu.setLongClickable(true);
        root_huifu.setOnTouchListener(this);

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);

        dataList=new ArrayList<Map<String, String>>();
        for(int i=0;i<huifu_yh.length;i++){
            Map<String, String> huifu_list = new HashMap<String, String>();
            huifu_list.put("huifu_yh",huifu_yh[i]);
            huifu_list.put("huifu_time",huifu_time[i]);
            huifu_list.put("huifu_content",huifu_content[i]);
            dataList.add(huifu_list);
        }

        simple_adapter=new SimpleAdapter(this.getActivity(), dataList, R.layout.wodehuifu_list, new String[] {"huifu_yh","huifu_time","huifu_content"},
                new int[] {R.id.huifu_yh, R.id.huifu_time, R.id.huifu_content});
        huifu_list.setAdapter(simple_adapter);
        huifu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
}

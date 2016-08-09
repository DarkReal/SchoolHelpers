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
 * 我的--通知
 * Created by xxw on 2016/4/9.
 */
public class tongzhiFragment extends BaseFragment {
    private View rootView;
    private String[] str = new String[]{
            "恭喜你已被成功录取，点击查看工作信息",
            "很遗憾，你并没有被录取"
    };
    private List<Map<String, String>> dataList;
    private SimpleAdapter simple_adapter;
    private ListView tongzhilist;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodetongzhi,container,false);

        setDefault();

        return rootView;
    }

    private ImageView back;
    private GestureDetector mGestureDetector;
    private LinearLayout root_tongzhi;
    public void setDefault(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_tongzhi);
        back.setOnClickListener(this);
        tongzhilist = (ListView) rootView.findViewById(R.id.tongzhilist);
        root_tongzhi = (LinearLayout)rootView.findViewById(R.id.root_tongzhi);
        root_tongzhi.setOnTouchListener(this);
        root_tongzhi.setLongClickable(true);

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);

        dataList=new ArrayList<Map<String, String>>();
        for(int i=0;i<str.length;i++){
            Map<String, String> tongzhi_list = new HashMap<String, String>();
            tongzhi_list.put("text",str[i]);
            dataList.add(tongzhi_list);
        }

        simple_adapter=new SimpleAdapter(this.getActivity(), dataList, R.layout.tongzhi_list_ever, new String[] {"text"},
                new int[] {R.id.text_id});
        tongzhilist.setAdapter(simple_adapter);
        tongzhilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Intent intent = new Intent();
                intent.setClass(view.getContext(), company_detail.class);
                Bundle bundle=new Bundle();
                //获得单击部分的隐藏起来的company_id的text的值
                TextView tv = (TextView) view.findViewById(R.id.company_id);
                String ids = tv.getText().toString();
                bundle.putString("id", ids);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });

    }

    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
}

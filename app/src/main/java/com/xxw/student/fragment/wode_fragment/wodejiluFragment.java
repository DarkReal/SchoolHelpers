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
 * 我的-我的记录
 * Created by xxw on 2016/4/9.
 */
public class wodejiluFragment extends BaseFragment {
    private View rootView;
    private String[] jl = new String[]{
            "帖子1",
            "帖子2",
            "帖子3"
    };
    private List<Map<String, String>> dataList;
    private SimpleAdapter simple_adapter;
    private ListView jllist;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodejilu,container,false);

        setDefault();

        return rootView;
    }

    private ImageView back;
    private LinearLayout root_jilu;
    private GestureDetector mGestureDetector;
    public void setDefault(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_wodejilu);
        back.setOnClickListener(this);

        root_jilu = (LinearLayout)rootView.findViewById(R.id.root_jilu);
        root_jilu.setOnTouchListener(this);
        root_jilu.setLongClickable(true);


        dataList=new ArrayList<Map<String, String>>();
        for(int i=0;i<jl.length;i++){
            Map<String, String> tongzhi_list = new HashMap<String, String>();
            tongzhi_list.put("jltext",jl[i]);
            dataList.add(tongzhi_list);
        }
        jllist = (ListView) rootView.findViewById(R.id.jilulist);
        simple_adapter=new SimpleAdapter(this.getActivity(), dataList, R.layout.jilulist_ever, new String[] {"jltext"},
                new int[] {R.id.jl_list_txt});
        jllist.setAdapter(simple_adapter);
        jllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Intent intent = new Intent();
                intent.setClass(view.getContext(), company_detail.class);
                Bundle bundle=new Bundle();
                //获得单击部分的隐藏起来的tiezi_id
                TextView tv = (TextView) view.findViewById(R.id.company_id);
                String ids = tv.getText().toString();
                bundle.putString("id", ids);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });


        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);

    }

    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
}

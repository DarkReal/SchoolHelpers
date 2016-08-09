package com.xxw.student.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.R;
import com.xxw.student.fragment.group.add_invitation;
import com.xxw.student.fragment.group.group_detail;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.pullrefresh_view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 圈子
 * Created by xxw on 2016/3/27.
 */
public class contentFragment_quanzi extends Fragment implements pullrefresh_view.OnHeaderRefreshListener{
    private View rootview;
    private SimpleAdapter simple_adapter;
    private ListView group_list_ever;

    private FloatingActionButton fab;
    private pullrefresh_view mPullToRefreshView;
    private List<Map<String,String>> aa;
    private JSONArray ja;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.main_content_quanzi,container,false);

        group_list_ever= (ListView) rootview.findViewById(R.id.group_list_ever);

        mPullToRefreshView = (pullrefresh_view) rootview.findViewById(R.id.quanzi_index);
        mPullToRefreshView.setOnHeaderRefreshListener(this);

        initData();
        getData();//获取帖子初始数据
        return rootview;
    }

    private void getData() {
        String url= Constant.getUrl()+"tiezi/getTiezi.htmls";
        try{
            HttpThread ht = new HttpThread(url){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message;

                        try {
                            message = obj.get("message").toString();
                            ja = obj.getJSONArray("object");
                            //LogUtils.v(ja.toString());
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (obj.get("code").toString().equals("-1"))
                                            Toast.makeText(rootview.getContext(), message, Toast.LENGTH_SHORT).show();
                                        else {
                                            //更新帖子列表显示内容
                                            updateview();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            };
            ht.start();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void updateview(){
        aa = new ArrayList<Map<String, String>>();

        for(int i=0;i<ja.length();i++){
            JSONObject obj = null;
            try {
                obj = (JSONObject) ja.get(i);
                Map<String,String> map = new HashMap<String,String>();
                map.put("group_list_name",obj.get("username").toString());
                map.put("group_list_title",obj.get("tiezi_title").toString());
                map.put("group_list_content",obj.get("content").toString());
                map.put("group_list_like",obj.get("likecount").toString());
                map.put("group_list_comment", obj.get("comment").toString());
                map.put("group_list_id",obj.get("id").toString());
                aa.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        //LogUtils.v(aa.toString());

        simple_adapter=new SimpleAdapter(this.getActivity(), aa, R.layout.group_list_ever, new String[] {"group_list_name","group_list_title","group_list_content","group_list_like","group_list_comment","group_list_id"},
                new int[] {R.id.group_list_name, R.id.group_list_title, R.id.group_list_content, R.id.group_list_like, R.id.group_list_comment, R.id.text_id});
        group_list_ever.setAdapter(simple_adapter);
        group_list_ever.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(rootview.getContext(), group_detail.class);
                Bundle bundle=new Bundle();
                //获得单击部分的隐藏起来的text_id的text的值
                TextView tv = (TextView) view.findViewById(R.id.text_id);
                String ids = tv.getText().toString();
                bundle.putString("id", ids);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    public void initData() {

        //发布帖子
        fab= (FloatingActionButton)rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.v("fab" + "click!!!!");
                Intent intent = new Intent();
                intent.setClass(rootview.getContext(), add_invitation.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onHeaderRefresh(pullrefresh_view view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                Date date = new Date();
                DateFormat format=new SimpleDateFormat("MM-dd HH:mm");
                String time=format.format(date);
                mPullToRefreshView.onHeaderRefreshComplete("上次更新于: "+time);
            }
        }, 1000);
    }
}

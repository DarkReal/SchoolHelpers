package com.xxw.student.fragment.group;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖子查看详情，包含评论列
 * Created by DarkReal on 2016/4/16.
 */
public class group_detail extends Activity implements View.OnClickListener,GestureDetector.OnGestureListener{


    private GestureDetector gestureDetector;
    //元素
    private LinearLayout return_before;
    private SimpleAdapter simple_adapter;
    private ListView group_list;
    private static List<Map<String, String>> dataList;

    private TextView pinglun_send;
    private EditText search_edit;
    private JSONArray ja1;
    private JSONObject ja2;
    private String ids;//帖子的id
    private String username = MainActivity.phone;//用户名--全局变量
    private TextView group_detail_title,user_name,write_time,text_content,like_count,comment_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        //获得帖子id ,username
        Bundle bundle=getIntent().getExtras();
        ids=bundle.getString("id");
//        LogUtils.v("group_detail_id"+ids);
//        LogUtils.v("group_detail_username"+username);
        try {
            //initData
            gestureDetector = new GestureDetector(this,this);
            return_before = (LinearLayout) findViewById(R.id.return_before);
            return_before.setOnClickListener(this);
            search_edit = (EditText)findViewById(R.id.search_edit);
            pinglun_send = (TextView)findViewById(R.id.pinglun_send);
            group_detail_title = (TextView) findViewById(R.id.group_detail_title);
            user_name = (TextView) findViewById(R.id.user_name);
            write_time = (TextView) findViewById(R.id.write_time);
            text_content = (TextView) findViewById(R.id.text_content);
            like_count = (TextView) findViewById(R.id.like_count);
            comment_count = (TextView) findViewById(R.id.comment_count);
            getDefault();//获取帖子标题
            getPinglunList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pinglun_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!search_edit.getText().toString().equals("")) {
                    //获取发帖时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    //组合评论的map
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("username", username);
                    map.put("time", df.format(new Date()));
                    map.put("tiezi_id", ids);
                    map.put("comment", search_edit.getText().toString());
                    LogUtils.v("group_detail_map"+map.values().toString());
                    add_comment(map);//添加评论

                    search_edit.setText("");
                    try {
                        getPinglunList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        group_list = (ListView) findViewById(R.id.comment_listview);
    }

    private void getDefault() {
        String url= Constant.getUrl()+"tiezi/getTieziContent.htmls";
        HashMap<String,String> map1 = new HashMap<String,String>();
        map1.put("id", ids);
        try{
            HttpThread ht = new HttpThread(url,map1){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        ja2 = obj.getJSONObject("object");
                        LogUtils.v("ja2-帖子"+ja2.toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        group_detail_title.setText(ja2.get("tiezi_title").toString());
                                        user_name.setText(ja2.get("username").toString());
                                        write_time.setText("");
                                        text_content.setText(ja2.get("content").toString());
                                        like_count.setText(ja2.get("likecount").toString());
                                        comment_count.setText("评论区("+ja2.length()+")");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
            };
            ht.start();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void add_comment(HashMap<String,String> map) {
        String url= Constant.getUrl()+"pinlun/addpinlun.htmls";
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                        getPinglunList();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
            };
            ht.start();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    //获取评论列表
    private void getPinglunList() throws JSONException {
        String url= Constant.getUrl()+"pinlun/getpinlun.htmls";
        HashMap<String,String> map2 = new HashMap<String,String>();
        map2.put("id", ids);
        try{
            HttpThread ht = new HttpThread(url,map2){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        LogUtils.v("obj"+obj.toString());
                        ja1 = obj.getJSONArray("object");

                        LogUtils.v("ja1-评论列表"+ja1.toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                        initAdapter();
                                        /*commentcount=ja1.length();
                                        comment_count.setText(commentcount);*/
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }
            };
            ht.start();

        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    private void initAdapter(){
        LogUtils.v("initAdapter");
        dataList=new ArrayList<Map<String,String>>();

        for(int i=0;i<ja1.length();i++) {
            Map<String, String> group_map = new HashMap<String, String>();
            try {
                JSONObject obj = (JSONObject) ja1.get(i);
                group_map.put("comment_username",obj.get("username").toString());
                group_map.put("comment_write_time",obj.get("time").toString());
                group_map.put("comment_text", obj.get("comment").toString());
                LogUtils.v("each_map"+group_map.values().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataList.add(group_map);
        }


        simple_adapter=new SimpleAdapter(this, dataList, R.layout.group_comment_list, new String[] {"comment_username","comment_write_time","comment_text"},
                new int[] {R.id.comment_user_name, R.id.comment_write_time, R.id.comment_text});
        group_list.setAdapter(simple_adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.return_before:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtils.v("onDown------------------------------");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtils.v("onShowPress------------------------------");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtils.v("onSingleTapUp------------------------------");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        LogUtils.v("distanceX"+e1+"    distanceY"+e2);
        if(e2.getX()-e1.getX()>30&&e2.getY()-e2.getY()<50)
        {
            LogUtils.v("足够位移");
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        LogUtils.v("onScroll------------------------------");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtils.v("onLongPress------------------------------");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.v("velocityX"+velocityX+"velocityY"+velocityY);

        LogUtils.v("onFling---------------------------------");
        return false;
    }

}

package com.xxw.student.fragment.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.Adapter.CustomAdapter_comcircle;
import com.xxw.student.LoginActivity;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.DateUtils;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.GoodView;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.loading.KProgressHUD;
import com.xxw.student.view.sweetdialog.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static List<HashMap<String, String>> dataList;

    private TextView pinglun_send;
    private EditText search_edit;
    private JSONArray ja1;
    private JSONObject ja2;
    private String ids;//帖子的id
    private String username = MainActivity.phone;//用户名--全局变量
    private TextView group_detail_title,user_name,write_time,text_content,like_count,comment_count;
    private ImageView touxiang,group_like_pic;
    private BitmapUtils bitmapUtils;
    private EditText comment_edit;
    private CustomAdapter_comcircle customAdapter_comcircle;
    private String isAdmire;//这个帖子是否已经点过赞了
    private String currToid;//回复的评论的id
    private KProgressHUD kProgressHUD;//刷新控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        bitmapUtils = new BitmapUtils(group_detail.this);
        //获得帖子id ,username
        Bundle bundle=getIntent().getExtras();
        ids=bundle.getString("id");
        LogUtils.v("group_detail_id" + ids);



        kProgressHUD = new KProgressHUD(group_detail.this);
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);


        //initData
        gestureDetector = new GestureDetector(this,this);
        return_before = (LinearLayout) findViewById(R.id.return_before);
        return_before.setOnClickListener(this);
        pinglun_send = (TextView)findViewById(R.id.comment_send);
        group_detail_title = (TextView) findViewById(R.id.group_detail_title);
        user_name = (TextView) findViewById(R.id.user_name);
        write_time = (TextView) findViewById(R.id.write_time);
        text_content = (TextView) findViewById(R.id.text_content);
        like_count = (TextView) findViewById(R.id.like_count);
        comment_count = (TextView) findViewById(R.id.comment_count);
        touxiang = (ImageView) findViewById(R.id.touxiang);
        group_like_pic = (ImageView) findViewById(R.id.group_like_pic);

        group_like_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isAdmire.equals("0")){
                    //0表示未点赞
                    group_like_pic.setImageResource(R.drawable.like_pressed);
                    like_count.setText((Integer.parseInt(like_count.getText().toString()) + 1) + "");
                    like_count.setVisibility(View.VISIBLE);
                    //点赞部分
                    GoodView goodView = new GoodView(group_detail.this);
                    group_like_pic.setImageResource(R.drawable.like_pressed);
                    goodView.setImage(getResources().getDrawable(R.drawable.like_pressed));
                    goodView.show(group_like_pic);

                    isAdmire ="1";
                    likeEvent();
                }
                else{
                    group_like_pic.setImageResource(R.drawable.like_unclick);
                    String count = like_count.getText().toString();
                    if(like_count.getText().toString()=="1"){
                        like_count.setText((Integer.parseInt(count) - 1) + "");
                        like_count.setVisibility(View.GONE);
                    }else{
                        like_count.setText((Integer.parseInt(count) - 1) + "");
                    }
                    isAdmire ="0";
                    likeEvent();
                }


            }
        });

        comment_edit = (EditText) findViewById(R.id.comment_edit);

        getDefault();//获取帖子内容

        pinglun_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_edit.getText().toString().equals("")) {
                    //组合评论的map

                    HashMap<String,String> commentmap = new HashMap<String,String>();
                    commentmap.put("token",MainActivity.token);
                    commentmap.put("circleId",ids);
                    //如果是艾特他人的内容
                    if(comment_edit.getText().toString().contains("@")){
                        commentmap.put("userTo",currToid);
                        String[] arr =  comment_edit.getText().toString().split(": ");
                        commentmap.put("comment",arr[1]);
                        commentmap.put("replytype","1");
                    }else{
                        commentmap.put("userTo",ids);
                        commentmap.put("comment",comment_edit.getText().toString());
                        commentmap.put("replytype","0");
                    }
                    LogUtils.v("group_detail_map" + commentmap.values().toString());
                    add_comment(commentmap);//添加评论

                    comment_edit.setText("");
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

    private void likeEvent() {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("token", MainActivity.token);
        map.put("circleId",ids);
        LogUtils.v(map.toString());
        String url = Constant.getUrl()+"app/circle/admireCircle.htmls";
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v(obj.get("message").toString());
                        LogUtils.v("obj" + obj.toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        new MaterialDialog(group_detail.this)
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
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

    //初始加载
    private void getDefault() {
        //用户是否有效
        Constant.isActivity();
        String url= Constant.getUrl()+"app/circle/loadCircleInfo.htmls";
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("circleId", ids);
        map.put("token",MainActivity.token);

        try{
            HttpThread ht = new HttpThread(url,map){
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
                                    if (!obj.get("code").toString().equals("10000"))
                                        new MaterialDialog(group_detail.this)
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                    else {
                                        //更新帖子列表显示内容
                                        LogUtils.v(ja2.get("userHeadPic").toString());

                                        group_detail_title.setText(ja2.get("title").toString());
                                        user_name.setText(ja2.get("userName").toString());
                                        write_time.setText(DateUtils.TimeStamp2Date(ja2.get("createTime").toString(), DateUtils.DATE_FORMAT3));
                                        text_content.setText(ja2.get("context").toString());
                                        like_count.setText(ja2.get("admirenum").toString());
                                        comment_count.setText("评论区("+ja2.get("commentnum").toString()+")");
                                        //是否已经点赞
                                        if(!ja2.get("isAdmire").toString().equals("0")){
                                            group_like_pic.setImageResource(R.drawable.like_pressed);
                                        }else{
                                            group_like_pic.setImageResource(R.drawable.like_unclick);
                                        }
                                        isAdmire = ja2.get("isAdmire").toString();
                                        bitmapUtils.display(touxiang, Constant.getUrl() + "upload/media/images/" + ja2.get("userHeadPic").toString());
                                        if(!Constant.isActivitied){
                                            LogUtils.v(Constant.isActivitied+"");
                                            new SweetAlertDialog(group_detail.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("未登录")
                                                    .setContentText("未登录的状态下不能进行评论等相关操作，请重新登录!")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            //转回登录页面
                                                            Intent intent = new Intent();
                                                            intent.setClass(group_detail.this, LoginActivity.class);
                                                            //保存登录状态
                                                            startActivity(intent);
                                                            finish();
                                                            MainActivity.emptyAll();
                                                        }
                                                    })
                                                    .show();
                                        }
                                        getPinglunList();//获取评论列表
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
    //发布评论
    private void add_comment(final HashMap<String,String> map) {
        kProgressHUD.show();

        String url= Constant.getUrl()+"app/circle/sendCircleCom.htmls";
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
                                    if (!obj.get("code").toString().equals("10000"))
                                        new MaterialDialog(group_detail.this)
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                        getPinglunList();
                                        customAdapter_comcircle.notifyDataSetChanged();
                                        currToid = "";//发布完了之后要清空
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
        kProgressHUD.show();
        String url= Constant.getUrl()+"app/circle/loadCircleComs.htmls";
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("circleComId", ids);
        map.put("pageNow", "1");

        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        LogUtils.v("obj"+obj.toString());
                        ja1 = obj.getJSONArray("object");

                        LogUtils.v("评论列表"+ja1.toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        new MaterialDialog(group_detail.this)
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(group_detail.this, message, Toast.LENGTH_SHORT).show();
                                        initAdapter();
                                        kProgressHUD.dismiss();
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
    //初始化评论列表
    private void initAdapter(){
        LogUtils.v("initAdapter");
        dataList=new ArrayList<HashMap<String,String>>();

        for(int i=0;i<ja1.length();i++) {
            HashMap<String, String> group_map = new HashMap<String, String>();
            try {
                JSONObject obj = (JSONObject) ja1.get(i);
                group_map.put("comment_pic",obj.get("userHeadPic").toString());
                group_map.put("comment_username",obj.get("userName").toString());
                group_map.put("comment_time",DateUtils.TimeStamp2Date(obj.get("createTime").toString(), DateUtils.DATE_FORMAT3));
                if(obj.get("comment").toString().contains("@")){
                    String[] arr = obj.get("comment").toString().split("##");
                    group_map.put("comment_text",arr[0]+": "+arr[1]);
                }else{
                    group_map.put("comment_text", obj.get("comment").toString());
                }
                group_map.put("eachid", obj.get("id").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataList.add(group_map);
        }
        customAdapter_comcircle =new CustomAdapter_comcircle(this,group_detail.this,dataList, R.layout.group_comment_list, new String[] {"comment_username","comment_time","comment_text","eachid"},
                new int[] {R.id.comment_username, R.id.comment_time, R.id.comment_context,R.id.eachid});
        group_list.setAdapter(customAdapter_comcircle);
        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView comment_username = (TextView) view.findViewById(R.id.comment_username);
                LogUtils.v(comment_username.getText().toString());
                comment_edit.setText("@" + comment_username.getText().toString() + ": ");
                comment_edit.setSelection(comment_username.getText().length() + 3);
                //如果艾特他人，那么userto就是对应评论的id;
                TextView eachid = (TextView) view.findViewById(R.id.eachid);
                currToid = eachid.getText().toString();
            }
        });
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

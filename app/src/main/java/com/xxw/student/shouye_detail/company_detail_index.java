package com.xxw.student.shouye_detail;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.Adapter.CustomAdapter_comcom;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.DateUtils;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.loading.KProgressHUD;
import com.xxw.student.view.pullrefreshAndLoad.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 公司详情页--公司简介
 * Created by DarkReal on 2016/4/11.
 */
public class company_detail_index extends Fragment implements View.OnClickListener,XListView.IXListViewListener{
    private View view;
    private ViewGroup mcontainer;
    private LayoutInflater minflater;
    private PopupWindow pop;

    private TextView show_more;
    private ImageView detail_cancel;

    private SimpleAdapter simple_adapter;
    private XListView comment_list;
    private ArrayList<HashMap<String, String>> comment_dataList;
    private String id = company_detail.company_id;//获取到公司的id对应获取评论列表
    private JSONArray ja;//盛放评论列表
    private JSONObject jo;//单个评论的时候
    private BitmapUtils bitmapUtils;

    private ImageView company_pic;
    private TextView company_details,company_address,company_website;
    private CustomAdapter_comcom customAdapter_comcom;
    private TextView noneWord;
    private EditText comment_edit;
    private TextView comment_send;
    private HashMap<String,String> map;

    private int currentPage = 1;
    private Handler mHandler;
    private boolean isload = false;//默认false 为true的时候表示是以加载为意图刷新的显示列表

    private KProgressHUD kProgressHUD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        minflater = inflater;
        mcontainer = container;
        view = inflater.inflate(R.layout.company_detail_index,container,false);

        //初始化加载控件
        kProgressHUD = new KProgressHUD(view.getContext());
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);

        bitmapUtils = new BitmapUtils(view.getContext());
        init();
        //initData
        getPinlunlist();
        comment_list= (XListView) view.findViewById(R.id.company_index_comment);
        comment_list.setPullLoadEnable(true, false);//可以加载
        comment_list.setPullRefreshEnable(false);//不能刷新
        comment_list.setXListViewListener(this);
        mHandler = new Handler();

        show_more = (TextView) view.findViewById(R.id.show_more);
        show_more.setOnClickListener(new View.OnClickListener() {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            private View views;
            @Override
            public void onClick(View v) {
                LogUtils.v("pop_click");


                views= minflater.inflate(R.layout.custom_show_onblur,mcontainer,false);
                detail_cancel = (ImageView) views.findViewById(R.id.detail_cancel);

                pop = new PopupWindow(views, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
                pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                        WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                pop.setBackgroundDrawable(new ColorDrawable(0x000000000));
                pop.setAnimationStyle(R.style.mypopwindow_anim_style);
                pop.showAtLocation(views, Gravity.CENTER,0,0);
                detail_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });

            }
        });
        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            company_pic = (ImageView) view.findViewById(R.id.company_pic);
            company_details = (TextView) view.findViewById(R.id.company_detail);
            company_address = (TextView) view.findViewById(R.id.company_address);
            company_website = (TextView) view.findViewById(R.id.company_website);
            JSONObject json = company_detail.company_json;
            bitmapUtils.display(company_pic,Constant.getUrl() + "upload/media/images/" + json.get("companyPic").toString());
            company_details.setText(json.get("companyDetail").toString());
            company_address.setText("公司地址："+json.get("address").toString());
            company_website.setText("公司网站："+json.get("website").toString());

            noneWord = (TextView) view.findViewById(R.id.noneWord);
            comment_edit = (EditText) view.findViewById(R.id.comment_edit);
            comment_send = (TextView) view.findViewById(R.id.comment_send);

            comment_send.setOnClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPinlunlist() {
        kProgressHUD.show();
        String url= Constant.getUrl()+"app/company/getComCom.htmls";
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("companyId", id);
        map.put("pageNow", currentPage+"");
        map.put("token", MainActivity.token);


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
                                        new MaterialDialog(view.getContext())
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                         ja = obj.getJSONArray("object");
                                        LogUtils.v("ja.length"+ja.length());
                                        //填充评论列表
                                        fillCommentList();
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

    private void fillCommentList() {

        if(!isload){
            comment_dataList = new ArrayList<HashMap<String,String>>();
        }
        if(ja.length() == 0){
            noneWord.setVisibility(View.VISIBLE);//显示无内容
        }else {//只要有内容就开始填充
            noneWord.setVisibility(View.GONE);//隐藏空反馈提示

            if(ja.length()<20){
//                如果不满20，那么关掉加载功能
                if(comment_dataList.size()<20)
                    comment_list.setPullLoadEnable(false,true);
                else
                    comment_list.setPullLoadEnable(false,false);
            }else{
                comment_list.setPullLoadEnable(true,false);
            }

            try {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject json = ja.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("touxiang", json.get("headPic").toString());
                    map.put("username", json.get("nickName").toString());



                    map.put("comment_time", json.get("createTime").toString().equals("null")?null:DateUtils.TimeStamp2Date(json.get("createTime").toString(),DateUtils.DATE_FORMAT3));
                    map.put("comment_value", json.get("comment").toString());
                    map.put("comment_like_pic", json.get("isAdmire").toString());//本人是否点赞
                    map.put("dz_count", json.get("admire").toString());//点赞数
                    map.put("eachid",json.get("id").toString());
                    comment_dataList.add(map);
                }
                LogUtils.v(comment_dataList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(!isload){
            customAdapter_comcom = new CustomAdapter_comcom(view.getContext(),getActivity(), comment_dataList, R.layout.company_comment_ever, new String[] {"username","comment_time","comment_value","dz_count"},
                    new int[] {R.id.username, R.id.comment_time, R.id.comment_value,R.id.dz_count});
            comment_list.setAdapter(customAdapter_comcom);
        }else{
            customAdapter_comcom.notifyDataSetChanged();
        }
        kProgressHUD.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_send:
                sendPinlun();
        }
    }
    //发送评论
    private void sendPinlun() {
        //获取EditText中的内容
        LogUtils.v(comment_edit.getText().toString());
        if(comment_edit.getText().toString()!=""){
            map = new HashMap<String,String>();
            map.put("token", MainActivity.token);
            map.put("companyId",id);
            map.put("comment",comment_edit.getText().toString());
            String url = Constant.getUrl()+"app/user/replyCompany.htmls";
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
                                            new MaterialDialog(view.getContext())
                                                    .setTitle("警告")
                                                    .autodismiss(2000)
                                                    .setMessage(message)
                                                    .show();
                                        else {
                                            //更新
                                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                            //评论成功
                                            getPinlunlist();
                                            fillCommentList();
                                            //发完了之后清空内容
                                            comment_edit.setText("");
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
    }
    //初始化数据

    private class my_pop extends PopupWindow {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

        public my_pop(Context context) {
            super();
        }

        @Override
        public void dismiss() {
            super.dismiss();
        }
    }


    private void onLoad() {
        comment_list.stopRefresh();
        comment_list.stopLoadMore();
        comment_list.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        LogUtils.v("onLoadMore -----底部加载");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage++;
                LogUtils.v(currentPage + "");
                isload = true;
                getPinlunlist();
                onLoad();
            }
        }, 1000);
    }


    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        isload = false;
        getPinlunlist();
    }


}

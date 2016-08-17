package com.xxw.student.shouye_detail;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.Adapter.ResumeAdapter_comcom;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.ResponseMessage;
import com.xxw.student.utils.getHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司详情页--公司简介
 * Created by DarkReal on 2016/4/11.
 */
public class company_detail_index extends Fragment implements View.OnClickListener{
    private View view;
    private ViewGroup mcontainer;
    private LayoutInflater minflater;
    private PopupWindow pop;

    private TextView show_more;
    private ImageView detail_cancel;

    private SimpleAdapter simple_adapter;
    private ListView comment_list;
    private ArrayList<HashMap<String, String>> comment_dataList;
    private GestureDetector gestureDetector;
    private  String id = company_detail.company_id;//获取到公司的id对应获取评论列表
    private JSONArray ja;//盛放评论列表
    private JSONObject jo;//单个评论的时候
    private BitmapUtils bitmapUtils;

    private ImageView company_pic;
    private TextView company_details,company_address,company_website;
    private ResumeAdapter_comcom resumeAdapter_comcom;
    private TextView noneWord;
    private EditText comment_edit;
    private TextView comment_send;
    private HashMap<String,String> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        minflater = inflater;
        mcontainer = container;
        view = inflater.inflate(R.layout.company_detail_index,container,false);
        bitmapUtils = new BitmapUtils(view.getContext());
        init();
        //initData
        getPinlunlist();
        comment_list= (ListView) view.findViewById(R.id.company_index_comment);
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
        String url= Constant.getUrl()+"app/company/getComCom.htmls";
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("companyId", id);
        map.put("pageNow", "0");
        map.put("token", MainActivity.token);


        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v(obj.get("message").toString());
                        LogUtils.v("obj" + obj.toString());

                        //数量小于等于1的时候，会报出转型错误的警告
                        ja = obj.getJSONArray("object");
                        LogUtils.v("ja-评论列表" + ja.toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                        try {
                                            ja = obj.getJSONArray("object");
                                        }catch (org.json.JSONException e){
                                            jo = obj.getJSONObject("object");
                                        }
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
        comment_dataList = new ArrayList<HashMap<String,String>>();
        if(ja.length() == 0){
            noneWord.setVisibility(View.VISIBLE);//显示无内容
        }else {//只要有内容就开始填充
            noneWord.setVisibility(View.GONE);//隐藏空反馈提示
            try {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject json = ja.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("touxiang", json.get("headPic").toString());
                    map.put("username", json.get("nickName").toString());
                    map.put("comment_time", json.get("createTime").toString());
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

        resumeAdapter_comcom = new ResumeAdapter_comcom(view.getContext(),getActivity(), comment_dataList, R.layout.company_comment_ever, new String[] {"username","comment_time","comment_value","dz_count"},
                 new int[] {R.id.username, R.id.comment_time, R.id.comment_value,R.id.dz_count});
        comment_list.setAdapter(resumeAdapter_comcom);
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
                                        if (obj.get("code").toString().equals("-1"))
                                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                        else {
                                            //更新
                                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                            //评论成功
                                            HashMap<String,String> comment_map = new HashMap<String, String>();

                                            comment_map.put("touxiang", MainActivity.headpic);
                                            comment_map.put("username", MainActivity.nickname);
                                            comment_map.put("comment_time", Constant.getCurrentTime());
                                            comment_map.put("comment_value", map.get("comment").toString());
                                            comment_map.put("comment_like_pic", "0");//本人是否点赞
                                            comment_map.put("dz_count", "0");//点赞数

                                            comment_dataList.add(comment_map);
                                            resumeAdapter_comcom.notifyDataSetChanged();
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
        //发送完毕之后清空EditText

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
    //发布评论之后动态刷新列表



}

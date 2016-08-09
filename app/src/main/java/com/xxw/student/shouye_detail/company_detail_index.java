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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
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
public class company_detail_index extends Fragment {
    private View view;
    private ViewGroup mcontainer;
    private LayoutInflater minflater;
    private PopupWindow pop;

    private TextView show_more;
    private ImageView detail_cancel;

    private SimpleAdapter simple_adapter;
    private ListView comment_list;
    private List<Map<String, Object>> dataList;
    private GestureDetector gestureDetector;
    private  String id = company_detail.company_id;//获取到公司的id对应获取评论列表
    private JSONArray ja;//盛放评论列表
    private BitmapUtils bitmapUtils;

    private ImageView company_pic;
    private TextView company_details,company_address,company_website;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void getPinlunlist() {
        String url= Constant.getUrl()+"ComCompany/getpinlun.htmls";
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("id", id);
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v(obj.get("message").toString());
                        LogUtils.v("obj" + obj.toString());
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
                                        //Toast.makeText(company_detail.this, message, Toast.LENGTH_SHORT).show();
                                        List<Map<String,String>> aa  = new ArrayList<Map<String,String>>();
                                            for(int i=0; i < ja.length();i++){
                                                Map<String, String> group_map = new HashMap<String, String>();
                                                try {
                                                    JSONObject obj = (JSONObject) ja.get(i);
                                                    group_map.put("username",obj.get("username").toString());
                                                    group_map.put("comment_value",obj.get("comment").toString());
                                                    group_map.put("comment_time",obj.get("time").toString());
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                                aa.add(group_map);
                                            }

                                        simple_adapter = new SimpleAdapter(view.getContext(), aa, R.layout.company_comment_ever, new String[] {"username","comment_value","comment_time"},
                                                new int[] {R.id.username, R.id.comment_value, R.id.comment_time});

                                        comment_list.setAdapter(simple_adapter);
                                        comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            private ImageView company_like_pic;
                                            private TextView dz_count;
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                LogUtils.v("click item!!!!1");
                                                company_like_pic = (ImageView) v.findViewById(R.id.company_like_pic);
                                                dz_count = (TextView) v.findViewById(R.id.dz_count);


                                                company_like_pic.setImageResource(R.drawable.like_pressed);
                                                dz_count.setVisibility(View.VISIBLE);
                                                dz_count.setText("1");
                                            }
                                        });
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

}

package com.xxw.student.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.Adapter.CustomAdapter_group;
import com.xxw.student.R;
import com.xxw.student.fragment.group.add_invitation;
import com.xxw.student.fragment.group.group_detail;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.loading.KProgressHUD;
import com.xxw.student.view.pullrefreshAndLoad.NsRefreshLayout;
import com.xxw.student.view.pullrefreshAndLoad.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 圈子
 * Created by xxw on 2016/3/27.
 */
public class contentFragment_quanzi extends Fragment implements NsRefreshLayout.NsRefreshLayoutController, NsRefreshLayout.NsRefreshLayoutListener,XListView.IXListViewListener {
    private View rootview;
    private CustomAdapter_group customAdapter_group;
    private XListView group_list_ever;

    private FloatingActionButton fab;
    private boolean loadMoreEnable = false;
    private NsRefreshLayout refreshLayout;
    private static ArrayList<HashMap<String,String>> group_datalist;
    private JSONArray ja;
    public static int index = 0;//默认分类是零
    private ImageView hot_circle;
    private int currentPage = 1;
    private Handler mHandler;
    private boolean isload = false;//默认false 为true的时候表示是以加载为意图刷新的显示列表
    private KProgressHUD kProgressHUD;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        //每次都初始化一次


        rootview = inflater.inflate(R.layout.main_content_quanzi,container,false);

        //初始化加载控件
        kProgressHUD = new KProgressHUD(rootview.getContext());
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);



        group_list_ever= (XListView) rootview.findViewById(R.id.group_list_ever);
        group_list_ever.setPullLoadEnable(true,false);//可以加载
        group_list_ever.setPullRefreshEnable(false);//不能刷新
        group_list_ever.setXListViewListener(this);


        refreshLayout = (NsRefreshLayout) rootview.findViewById(R.id.quanzi_index);
        refreshLayout.setRefreshLayoutController(this);
        refreshLayout.setRefreshLayoutListener(this);

        hot_circle = (ImageView) rootview.findViewById(R.id.hot_circle);

        initData();
        getData(index + "");//获取帖子初始数据
        mHandler = new Handler();
        Commonhandler.comHandler = new Handler(){
            public void handleMessage (Message msg) {
                isload = false;
                index = msg.what;
                //更换分类显示的时候也要恢复currentPage到默认
                currentPage = 1;
                getData(index+"");
                customAdapter_group.notifyDataSetChanged();//刷新显示
            }
        };

        return rootview;
    }

    private void getData(String index) {
        kProgressHUD.show();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("index", index);
        map.put("pageNow", currentPage+"");

        String url= Constant.getUrl()+"app/circle/loadCircleList.htmls";
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message;

                        try {
                            message = obj.get("message").toString();
                            LogUtils.v(obj.get("code").toString());
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!obj.get("code").toString().equals("10000"))
                                            Toast.makeText(rootview.getContext(), message, Toast.LENGTH_SHORT).show();
                                        else {
                                            Toast.makeText(rootview.getContext(), message, Toast.LENGTH_SHORT).show();
                                            //更新帖子列表显示内容
                                            ja = obj.getJSONArray("object");
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
    //更新显示
    private void updateview(){

        if(!isload){//如果是刷新就重新加载
            LogUtils.v("刷新操作");
            group_datalist = new ArrayList<HashMap<String, String>>();
        }

//        不是刷新的话不需要初始化只需要在原来的基础上add即可
        for(int i=0;i<ja.length();i++){
            try {
                JSONObject obj = (JSONObject) ja.get(i);
                HashMap<String,String> group_map = new HashMap<String,String>();
                group_map.put("text_id",obj.get("id").toString());
                group_map.put("group_list_pic",obj.get("userHeadPic").toString());
                group_map.put("group_list_name",obj.get("userName").toString());
                group_map.put("group_list_title",obj.get("title").toString());
                group_map.put("group_list_content", obj.get("context").toString());
                group_map.put("group_list_like", obj.get("admirenum").toString());
                group_map.put("group_list_comment", obj.get("commentnum").toString());
                group_datalist.add(group_map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        LogUtils.v(group_datalist.size() + "");
        //数量不够20的时候不需要显示这个
        if(ja.length()<20){
            if(group_datalist.size()<20)
                group_list_ever.setPullLoadEnable(false,true);
            else
                group_list_ever.setPullLoadEnable(false,false);
        }else{
            group_list_ever.setPullLoadEnable(true,false);
        }


        if(!isload){  //刷新的时候的操作
            customAdapter_group =new CustomAdapter_group(rootview.getContext(),getActivity(), group_datalist, R.layout.group_list_ever, new String[] {"text_id","group_list_name","group_list_title","group_list_content","group_list_like","group_list_comment"},
                    new int[] {R.id.text_id, R.id.group_list_name, R.id.group_list_title, R.id.group_list_content,R.id.group_list_like,R.id.group_list_comment});
            group_list_ever.setAdapter(customAdapter_group);
            group_list_ever.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(rootview.getContext(), group_detail.class);
                    Bundle bundle = new Bundle();
                    //获得单击部分的隐藏起来的text_id的text的值
                    TextView tv = (TextView) view.findViewById(R.id.text_id);
                    String ids = tv.getText().toString();
                    bundle.putString("id", ids);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }else{
            customAdapter_group.notifyDataSetChanged();//这样光标不会出问题
        }
        kProgressHUD.dismiss();
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
        //获取热点图片
        getHotCircle();

    }

    private void getHotCircle() {

    }

    @Override
    public boolean isPullRefreshEnable() {
        return true;
    }

    @Override
    public boolean isPullLoadEnable() {
        return loadMoreEnable;
    }

    @Override
    public void onRefresh() {
        LogUtils.v("onRefresh---顶部下拉刷新");
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.finishPullRefresh();
                isload = false;
                currentPage = 1;
                getData(index+"");
            }
        }, 1000);
    }

    private void onLoad() {
        group_list_ever.stopRefresh();
        group_list_ever.stopLoadMore();
        group_list_ever.setRefreshTime("刚刚");
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
                getData(index + "");
                onLoad();
            }
        }, 1000);
    }


    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        isload = false;
        getData(index+"");
    }
}

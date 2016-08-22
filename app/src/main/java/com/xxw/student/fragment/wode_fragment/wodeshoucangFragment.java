
package com.xxw.student.fragment.wode_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.Adapter.CustomAdapter_jobList;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.shouye_detail.job_detail;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.DateUtils;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.loading.KProgressHUD;
import com.xxw.student.view.pullrefreshAndLoad.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的--我的收藏（职位收藏）
 * Created by xxw on 2016/4/9.
 */
public class wodeshoucangFragment extends BaseFragment implements XListView.IXListViewListener{
    private View rootView;
    private HashMap<String,String> map;
    private TextView noneWord;
    private JSONArray ja;

    private ArrayList<HashMap<String,String>> zhiweiLists;
//    private int image[] = {R.drawable.defaulthead, R.drawable.defaulthead};
//    private String[] name = {"多媒体产品经理","储备干部"};
//    private String[] detail = {"网易(杭州)网络有限公司","杭州华润万家便利连锁有限公司"};
//    private String[] position = {"杭州","杭州"};
    private ImageView back;
    private XListView collect_list;
    private GestureDetector mGestureDetector;
    private CustomAdapter_jobList customAdapter_jobList;
    private int currentPage = 1;
    private Handler mHandler;
    private boolean isload = false;//默认false 为true的时候表示是以加载为意图刷新的显示列表
    private KProgressHUD kProgressHUD;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodeshoucang,container,false);
        noneWord = (TextView) rootView.findViewById(R.id.noneWord);
        back = (ImageView)rootView.findViewById(R.id.backTowode_collect);
        back.setOnClickListener(this);
        collect_list = (XListView) rootView.findViewById(R.id.collect_list);
        collect_list.setPullLoadEnable(true, false);//可以加载
        collect_list.setPullRefreshEnable(false);//不能刷新
        collect_list.setXListViewListener(this);
        mHandler = new Handler();

        //初始化加载控件
        kProgressHUD = new KProgressHUD(rootView.getContext());
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);
        //初始化职位列表的数据
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);


        getCollectList();
        return rootView;
    }

    /**
     * 获取职位列表的网络请求
     */
    private void getCollectList() {
        kProgressHUD.show();
        map = new HashMap<String, String>();
        map.put("token", MainActivity.token);
        map.put("pageNow", currentPage+"");
        LogUtils.v(map.values().toString());
        String url = Constant.getUrl() + "app/user/selectRecruitList.htmls";
        try {
            HttpThread ht = new HttpThread(url,map){

                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("---jsonstr---"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        ja = obj.getJSONArray("object");
                                        //空反馈
                                        if (ja.length()==0){
                                            kProgressHUD.dismiss();
                                            Toast.makeText(rootView.getContext(), "暂无收藏内容", Toast.LENGTH_SHORT).show();
                                            noneWord.setVisibility(View.VISIBLE);
                                        }else{
                                            noneWord.setVisibility(View.GONE);
                                            setDefault();
                                        }

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

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //填充
    public void setDefault(){

        try {
            if(!isload){
                zhiweiLists = new ArrayList<HashMap<String, String>>();
            }

            for(int i=0;i<ja.length();i++){
                HashMap<String, String> job_map = new HashMap<String, String>();
                JSONObject obj = (JSONObject) ja.getJSONObject(i);
                job_map.put("recruitName",obj.get("recruitName").toString());
                job_map.put("createTime",DateUtils.TimeStamp2Date(obj.get("createTime").toString(), DateUtils.DATE_FORMAT3));
                job_map.put("workPlace",obj.get("city").toString());
                job_map.put("id",obj.get("id").toString());
                job_map.put("pic",obj.get("recruitPic").toString());
                zhiweiLists.add(job_map);
            }

            if(ja.length()<20){
                if(zhiweiLists.size()<20)
//                如果不满20，那么关掉加载功能
                    collect_list.setPullLoadEnable(false,true);
                else
                    collect_list.setPullLoadEnable(false,false);
            }else{
                collect_list.setPullLoadEnable(true, false);
            }
            if(!isload){
                customAdapter_jobList =new CustomAdapter_jobList(rootView.getContext(),getActivity(), zhiweiLists, R.layout.company_job_ever, new String[] {"recruitName","createTime","workPlace","id"},
                        new int[] {R.id.job_name, R.id.apply_time, R.id.job_location, R.id.job_id});
                collect_list.setAdapter(customAdapter_jobList);
                collect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), job_detail.class);
                        Bundle bundle = new Bundle();
                        //获得单击部分的隐藏起来的company_id的text的值
                        TextView tv = (TextView) view.findViewById(R.id.job_id);
                        String ids = tv.getText().toString();
                        bundle.putString("id", ids);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else{
                customAdapter_jobList.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        kProgressHUD.dismiss();
    }
    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
    @Override
    public void onRefresh() {
        LogUtils.v("onRefresh---顶部下拉刷新");
    }

    private void onLoad() {
        collect_list.stopRefresh();
        collect_list.stopLoadMore();
        collect_list.setRefreshTime("刚刚");
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
                getCollectList();
                onLoad();
            }
        }, 1000);
    }


    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        isload = false;
        getCollectList();
    }
}

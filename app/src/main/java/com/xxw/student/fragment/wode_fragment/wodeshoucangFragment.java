
package com.xxw.student.fragment.wode_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的--我的收藏（职位收藏）
 * Created by xxw on 2016/4/9.
 */
public class wodeshoucangFragment extends BaseFragment {
    private View rootView;
    private HashMap<String,String> map;
    private TextView noneWord;

    private List<Map<String,Object>> zhiweiLists;
    private int image[] = {R.drawable.defaulthead, R.drawable.defaulthead};
    private String[] name = {"多媒体产品经理","储备干部"};
    private String[] detail = {"网易(杭州)网络有限公司","杭州华润万家便利连锁有限公司"};
    private String[] position = {"杭州","杭州"};
    private ImageView back;
    private ListView collect_list;
    private GestureDetector mGestureDetector;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodeshoucang,container,false);

        setDefault();
        getCollectList();
        return rootView;
    }

    /**
     * 获取职位列表的网络请求
     */
    private void getCollectList() {
        map = new HashMap<String, String>();
        map.put("token", MainActivity.token);
        map.put("pageNow", "1");
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
                                        JSONArray json = obj.getJSONArray("object");
                                        //空反馈
                                        if (json.length()==0){
                                            Toast.makeText(rootView.getContext(), "暂无收藏内容", Toast.LENGTH_SHORT).show();
                                            noneWord = (TextView) rootView.findViewById(R.id.noneWord);
                                            noneWord.setVisibility(View.VISIBLE);
                                        }

                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                /*Intent intent = new Intent();
                                                intent.setClass(view.getContext(), MainActivity.class);
                                                //保存登录状态
                                                savestatus();
                                                startActivity(intent);
                                                main_login main_login = (main_login) getActivity();
                                                main_login.finish();*/
                                            }

                                        }, 1000);
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



    public void setDefault(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_collect);
        back.setOnClickListener(this);
        collect_list = (ListView) rootView.findViewById(R.id.collect_list);
        //初始化职位列表的数据
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);
        zhiweiLists = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<image.length;i++){
            Map<String ,Object> zhiweiList = new HashMap<String ,Object>();
            zhiweiList.put("image",image[i]);
            zhiweiList.put("name",name[i]);
            zhiweiList.put("detail",detail[i]);
            zhiweiList.put("position",position[i]);
            zhiweiLists.add(zhiweiList);
        }
        SimpleAdapter zhiweiAdapter = new SimpleAdapter(getActivity(),zhiweiLists, R.layout.zhiwei_gongsi_list,
                new String[]{"image","name","detail","position"},new int[]{R.id.suotu, R.id.mingcheng, R.id.xiangqing, R.id.didian});

        collect_list.setAdapter(zhiweiAdapter);
        collect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //每一项的单击事件
            }
        });
    }
    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }

}

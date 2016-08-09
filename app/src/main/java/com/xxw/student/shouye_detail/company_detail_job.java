package com.xxw.student.shouye_detail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
 * 公司详情页--职位列表
 * Created by DarkReal on 2016/4/11.
 */
public class company_detail_job extends Fragment {
    private View view;
    //公司图片
    private ImageView job_pic;

    private String[] job_name;
    private String[] apply_time;
    private String[] job_offer;
    private String[] job_location;

    private SimpleAdapter simple_adapter;
    private ListView job_list;
    private List<Map<String, String>> dataList;
    private String id = company_detail.company_id;
    private JSONArray ja;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.company_detail_job,container,false);
        initData();
        return view;
    }

    private void initData() {

        job_list= (ListView) view.findViewById(R.id.job_list);
        String url= Constant.getUrl()+"job/getjoblist.htmls";
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("id", id);
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        LogUtils.v("obj"+obj.toString());
                        ja = obj.getJSONArray("object");

                        LogUtils.v("ja-职位列表"+ja.toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        //Toast.makeText(company_detail.this, message, Toast.LENGTH_SHORT).show();
                                        dataList=new ArrayList<Map<String, String>>();
                                        for(int i=0;i<ja.length();i++){
                                            Map<String, String> job_map = new HashMap<String, String>();
                                            JSONObject obj = (JSONObject) ja.get(i);
                                            job_map.put("job_name",obj.get("job_name").toString());
                                            job_map.put("apply_time",obj.get("fabu_time").toString());
                                            job_map.put("job_offer",obj.get("salary").toString());
                                            job_map.put("job_location",obj.get("location").toString());
                                            job_map.put("job_id",obj.get("id").toString());
                                            dataList.add(job_map);
                                        }

                                        simple_adapter=new SimpleAdapter(view.getContext(), dataList, R.layout.company_job_ever, new String[] {"job_name","apply_time","job_offer","job_location","job_id"},
                                                new int[] {R.id.job_name, R.id.apply_time, R.id.job_offer, R.id.job_location, R.id.job_id});

                                        job_list.setAdapter(simple_adapter);
                                        job_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent();
                                                intent.setClass(view.getContext(),job_detail.class);
                                                Bundle bundle=new Bundle();
                                                //获得单击部分的隐藏起来的company_id的text的值
                                                TextView tv = (TextView) view.findViewById(R.id.job_id);
                                                String ids = tv.getText().toString();
                                                bundle.putString("id", ids);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
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
}

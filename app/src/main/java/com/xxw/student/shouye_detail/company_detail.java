package com.xxw.student.shouye_detail;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 公司详情页，包含两个内页fragment:  公司简介/职位列表页
 * Created by DarkReal on 2016/4/11.
 */
public class company_detail extends Activity implements View.OnClickListener,GestureDetector.OnGestureListener{


    private Fragment[] company_detail_frag = new Fragment[2];
    private TextView[] select_btn = new TextView[2];
    private company_detail_index company_detail_index;
    private com.xxw.student.shouye_detail.company_detail_job company_detail_job;
    private TextView select_to_index,select_to_job;
    private View[] below_line = new View[2];
    private View line1,line2;
    private LinearLayout return_before;
    private GestureDetector gestureDetector;
    private String ids;
    private JSONObject ja;
    private TextView company_name,location,count_job;
    public static String company_id;

    private boolean[] page_select={false,false};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_detail_show);
        Bundle bundle=getIntent().getExtras();
        ids=bundle.getString("id");
        //LogUtils.v("group_detail_id"+ids);
        company_id = ids;
        init();
        getDefault();

        setTxtEvent();
        setContentFragment();
    }

    private void getDefault() {
        String url= Constant.getUrl()+"company/getCompanyById.htmls";
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("id", ids);
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v("message: "+obj.get("message").toString());
                        LogUtils.v("obj"+obj.toString());
                        ja = obj.getJSONObject("object");

                        LogUtils.v("ja-公司信息"+ja.toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(company_detail.this, message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        //Toast.makeText(company_detail.this, message, Toast.LENGTH_SHORT).show();
                                        company_name.setText(ja.get("company_name").toString());
                                        location.setText(ja.get("location").toString());
                                        count_job.setText("共有"+ja.get("job_count").toString()+"个在招职位");
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


    private void init() {
        select_to_index = (TextView) findViewById(R.id.company_index);
        select_to_job = (TextView) findViewById(R.id.company_job);
        select_btn[0] = select_to_index;
        select_btn[1] = select_to_job;
        company_detail_job= new company_detail_job();
        company_detail_index = new company_detail_index();
        company_detail_frag[0] = company_detail_index;
        company_detail_frag[1] = company_detail_job;
        line1 = findViewById(R.id.company_index_line);
        line2 = findViewById(R.id.company_job_line);
        below_line[0] = line1;
        below_line[1] = line2;
        return_before= (LinearLayout) findViewById(R.id.return_before);
        return_before.setOnClickListener(this);
        gestureDetector = new GestureDetector(this,this);
        company_name = (TextView) findViewById(R.id.company_name);
        location = (TextView) findViewById(R.id.location);
        count_job = (TextView) findViewById(R.id.count_job);
    }


    public void setTxtEvent() {
        for(TextView tv: select_btn){
            tv.setOnClickListener(this);
        }
    }




    public void setContentFragment(){
        page_select[0] = true ;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.company_detail_frag, company_detail_frag[0]);
        ft.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.company_index :
                if(!page_select[0]){
                    LogUtils.v("select 1");
                    changeFragment(0);
                    changePage(0);
                    changeLine(0);
                }
                break;
            case R.id.company_job :
                if(!page_select[1]){
                    LogUtils.v("select 2");
                    changeFragment(1);
                    changePage(1);
                    changeLine(1);
                }
                break;
            case R.id.return_before :
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    private void changeLine(int index) {
        for(int j=0;j<below_line.length;j++){
            below_line[j].setVisibility(View.INVISIBLE);
            select_btn[j].setTextColor(Color.parseColor("#8A8A8A"));
        }
        below_line[index].setVisibility(View.VISIBLE);
        select_btn[index].setTextColor(Color.parseColor("#36ab60"));
    }

    public void changeFragment(int index){
        page_select[index] = true ;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.company_detail_frag, company_detail_frag[index]);
        ft.commit();
    }

    public void changePage(int index){
        page_select[0]=false;
        page_select[1]=false;
        page_select[index]=true;
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
        //滑动返回
        LogUtils.v("distanceX"+e1+"    distanceY"+e2);
        if(e2.getX()-e1.getX()>50&&e2.getY()-e1.getY()<50)
        {
            LogUtils.v("足够位移");
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        LogUtils.v("onScroll------------------------------");

        //下拉刷新

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtils.v("onLongPress------------------------------");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.v("onFling---------------------------------");
        return false;
    }
}

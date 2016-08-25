package com.xxw.student.fragment.wode_fragment.edit_frag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.Adapter.ResumeAdapter_gzjl;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.wodejianliFragment;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 简历--工作经历
 * Created by DarkReal on 2016/7/5.
 */
public class gzjl_frag extends Activity implements View.OnClickListener{
    private ImageView backTojianli;
    private LinearLayout add_job_experience;
    private List<HashMap<String,String>> gzjl_datalist;
    private ListView gzjl_list;
    private ResumeAdapter_gzjl resumeAdapterGzjl;
    private TextView noneWord;
    private TextView edit_btn;
    private HashMap<String,String> mapforhttp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_gzjl_edit);

        //        根据收到的不同的消息,对应改变edit_btn的点击事件
        Commonhandler.comHandler = new Handler(){
            public void handleMessage (Message msg) {
                LogUtils.v("in hanlder");

                switch(msg.what) {

                    case -1://有修改值
                        LogUtils.v(msg.what + "");
                        edit_btn.setText("保存");
                        edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                save();
                            }
                        });
                        //不能添加
                        add_job_experience.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new MaterialDialog(gzjl_frag.this)
                                        .setTitle("警告")
                                        .autodismiss(2000)
                                        .setMessage("有内容未保存，请保存后进行下一步")
                                        .show();
                            }
                        });
                        break;
                    case 1://删除完毕
                        gzjl_datalist.remove(resumeAdapterGzjl.getCurrentItemInt());
                        resumeAdapterGzjl.notifyDataSetChanged();//更新视图

                        break;
                }
            }
        };
        init();
    }
    private void init() {

        backTojianli = (ImageView) findViewById(R.id.backTojianli);
        add_job_experience = (LinearLayout) findViewById(R.id.add_job_experience);
        gzjl_list = (ListView) findViewById(R.id.gzjl_list);
        noneWord = (TextView) findViewById(R.id.noneWord);
        edit_btn = (TextView) findViewById(R.id.edit_btn);

        backTojianli.setOnClickListener(this);
        add_job_experience.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        //初始化数据
        gzjl_datalist = new ArrayList<HashMap<String, String>>();

        JSONArray listExperience = wodejianliFragment.listExperience;
        if (listExperience.length() == 0) {
            //无值的时候空反馈
            noneWord.setVisibility(View.VISIBLE);
        } else {
            //有值的时候列表显示
            noneWord.setVisibility(View.GONE);
            for (int i = 0; i < listExperience.length(); i++) {
                HashMap<String, String> gzjl_data = new HashMap<String, String>();

                try {
                    JSONObject jsonobj = (JSONObject) listExperience.get(i);
                    gzjl_data.put("id",jsonobj.get("id").toString());
                    gzjl_data.put("position",jsonobj.get("position").toString());
                    gzjl_data.put("workContext",jsonobj.get("workContext").toString());
                    gzjl_data.put("companyName",jsonobj.get("companyName").toString());
                    gzjl_data.put("workDateBegin",jsonobj.get("workDateBegin").toString());
                    gzjl_data.put("workDateEnd",jsonobj.get("workDateEnd").toString());
                    gzjl_datalist.add(gzjl_data);
                    LogUtils.v("gzjl_datalist"+gzjl_datalist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            resumeAdapterGzjl=new ResumeAdapter_gzjl(this,gzjl_frag.this, gzjl_datalist, R.layout.wode_gzjl_edit_ever, new String[] {"workContext","position","companyName","workDateBegin","workDateEnd","id"},
                    new int[] {R.id.job_experience, R.id.job, R.id.company, R.id.gz_timeBegin, R.id.gz_timeEnd, R.id.gzjl_list_id,});
            gzjl_list.setAdapter(resumeAdapterGzjl);
            resumeAdapterGzjl.changeState(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.backTojianli:
                //返回到简历的总页面，
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                break;
            case R.id.add_job_experience:
                addgzjl();
                break;

            case R.id.edit_btn:
                if(edit_btn.getText().toString()=="保存"&&resumeAdapterGzjl.isNeedtoshow()){
                    edit_btn.setText("编辑");
                    resumeAdapterGzjl.changeState(false);
                    resumeAdapterGzjl.notifyDataSetChanged();
                }else{
                    edit_btn.setText("保存");
                    resumeAdapterGzjl.changeState(true);
                    resumeAdapterGzjl.notifyDataSetChanged();
                }
                break;
        }
    }

    private void addgzjl() {
        HashMap<String,String> map = new HashMap<String,String>();

        //检查是否有未保存的内容
        if(!checkIsSaved()){
            //如果内容未保存的话
            Toast.makeText(this, "有内容未保存，请保存后进行下一步", Toast.LENGTH_SHORT).show();
        }else {

            //这些都是默认值
            map.put("id", "0");//未保存的id都是0,保存以后读取的时候就是从数据库读过来的内容
            map.put("workContext","工作经历");
            map.put("position", "实习");
            map.put("companyName", "公司");
            map.put("workDateBegin", "2013-06");
            map.put("workDateEnd","2017-04");
            gzjl_datalist.add(map);
            LogUtils.v(gzjl_datalist.toString());
            //如果没有填写，单击添加之后产生一个新的
            if(resumeAdapterGzjl==null){
                resumeAdapterGzjl=new ResumeAdapter_gzjl(this,gzjl_frag.this, gzjl_datalist, R.layout.wode_gzjl_edit_ever, new String[] {"workContext","position","companyName","workDateBegin","workDateEnd","id"},
                        new int[] {R.id.job_experience, R.id.job, R.id.company, R.id.gz_timeBegin, R.id.gz_timeEnd,R.id.gzjl_list_id});
                gzjl_list.setAdapter(resumeAdapterGzjl);

                noneWord.setVisibility(View.GONE);
            }else{
                //已经填写，就动态刷新
                resumeAdapterGzjl.notifyDataSetChanged();//动态刷新
            }
        }
        resumeAdapterGzjl.setCurrentItemInt(-1);//这时候还没有对新增的内容进行过修改，设置为默认状态
        //单击添加了之后，除非进行了保存，否则弹出警告提示（有未保存的内容）
        edit_btn.setText("保存");
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存数据的http操作
                save();
            }

        });

}

    //检查是否有未保存的添加内容  未保存的内容默认id是空的
    private boolean checkIsSaved() {
        for (int i = 0; i < gzjl_datalist.size(); i++) {
            if (gzjl_datalist.get(i).get("id").toString() == "0") {
                return false;
            }
        }
        return true;
    }
    //收到构造器发出来的修改信息
    //保存成功就改掉保存按钮的点击事件
    private void save(){
        //保存数据的http操作
        mapforhttp = new HashMap<String, String>();

        //没有选中的内容的时候
        if(resumeAdapterGzjl.getCurrentItemInt()==-1){
            Toast.makeText(gzjl_frag.this, "请填写内容然后保存", Toast.LENGTH_SHORT).show();
        }else {
            //获取最后修改的map
            HashMap<String, String> getforMap = resumeAdapterGzjl.getHashMap();
            LogUtils.v("gzjl_获取到的map值"+resumeAdapterGzjl.getHashMap() + "");

            //如果有id证明是修改，就需要补全id,userid,没哟肚饿话表示是新添加的内容
            if ((getforMap.containsKey("id"))) {
                //如果有id这个属性
                mapforhttp.put("id", getforMap.get("id"));
                mapforhttp.put("userId", MainActivity.userid);
            }

            mapforhttp.put("token", MainActivity.token);
            mapforhttp.put("position", getforMap.get("position"));
            mapforhttp.put("companyName", getforMap.get("companyName"));
            mapforhttp.put("workDateBegin", getforMap.get("workDateBegin"));
            mapforhttp.put("workDateEnd", getforMap.get("workDateEnd"));
            mapforhttp.put("workContext", getforMap.get("workContext"));

            LogUtils.v(mapforhttp.values().toString());
            String url = Constant.getUrl() + "app/user/addExperience.htmls";
            try {
                HttpThread ht = new HttpThread(url, mapforhttp) {
                    @Override
                    public void getObj(final JSONObject obj) throws JSONException {
                        if (obj != null) {
                            final String message = obj.get("message").toString();
                            LogUtils.v("-----jsonstr----"+obj.toString());
                            LogUtils.v("message: "+obj.get("message").toString());
                            //只能在handler中更新视图
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new MaterialDialog(gzjl_frag.this)
                                            .setTitle("警告")
                                            .autodismiss(2000)
                                            .setMessage(message)
                                            .show();
                                    //更改文字提示
                                    edit_btn.setText("编辑");
                                    //去除单击保存事件，这个时候单击应该变成删除按钮出现
                                    edit_btn.setOnClickListener(gzjl_frag.this);
//                                    可以添加下一个了
                                    add_job_experience.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addgzjl();
                                        }
                                    });
                                    ResumeAdapter_gzjl.countEdit = -2;//恢复初始状态(因为改变之后还会触发一次edittext的改变，所以必须要把这次抵消掉)抵消掉之后还必须保证不等于0，所以设置为-1
                                    resumeAdapterGzjl.changeState(false);
                                    try {
                                        changeLocalMap(mapforhttp,obj.get("object").toString());//更新视图
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    resumeAdapterGzjl.notifyDataSetChanged();//刷新视图
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
    }

    /**
     * 修改掉之后也更新掉当前页面的map,更新视图
     */
    private void changeLocalMap(HashMap<String,String> mapforhttp,String newid) {

        //遍历教育背景datalist,以id为关键字找到对应的Map然后修改
        for(int i = 0 ;i < gzjl_datalist.size() ; i++){
            if(mapforhttp.containsKey("id")){
                //对应找到id相同的
                if(gzjl_datalist.get(i).get("id").toString()==mapforhttp.get("id").toString()){
                    gzjl_datalist.get(i).put("position", mapforhttp.get("position").toString());
                    gzjl_datalist.get(i).put("companyName", mapforhttp.get("companyName").toString());
                    gzjl_datalist.get(i).put("workDateBegin", mapforhttp.get("workDateBegin").toString());
                    gzjl_datalist.get(i).put("workDateEnd", mapforhttp.get("workDateEnd").toString());
                    gzjl_datalist.get(i).put("workContext", mapforhttp.get("workContext").toString());
                }
            }else{
                if(gzjl_datalist.get(i).get("id").toString()=="0"){
                    gzjl_datalist.get(i).put("id",newid);//10000表示已经修改完毕
                    gzjl_datalist.get(i).put("position", mapforhttp.get("position").toString());
                    gzjl_datalist.get(i).put("companyName", mapforhttp.get("companyName").toString());
                    gzjl_datalist.get(i).put("workDateBegin", mapforhttp.get("workDateBegin").toString());
                    gzjl_datalist.get(i).put("workDateEnd", mapforhttp.get("workDateEnd").toString());
                    gzjl_datalist.get(i).put("workContext", mapforhttp.get("workContext").toString());
                }
            }
        }
        LogUtils.v("after_editor"+gzjl_datalist.toString());
    }
}
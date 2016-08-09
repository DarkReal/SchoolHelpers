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

import com.xxw.student.Adapter.ResumeAdapter_hjjl;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.wodejianliFragment;
import com.xxw.student.utils.Commonhandler;
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

/**
 * 简历--获奖经历
 * Created by DarkReal on 2016/7/5.
 */
public class hjjl_frag extends Activity implements View.OnClickListener{
    private ImageView backTojianli;
    private LinearLayout add_reward;
    private List<HashMap<String,String>> hjjl_datalist;
    private ListView hjjl_list;
    private ResumeAdapter_hjjl resumeAdapterHjjl;
    private TextView noneWord;
    private TextView edit_btn;
    private HashMap<String,String> mapforhttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_hjjl_edit);
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
                        add_reward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(hjjl_frag.this, "有内容未保存，请保存后进行下一步", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 1://删除完毕
                        hjjl_datalist.remove(resumeAdapterHjjl.getCurrentItemInt());
                        resumeAdapterHjjl.notifyDataSetChanged();//更新视图

                        break;
                }
            }
        };
        init();
    }
    //初始化载入数据
    private void init() {

        backTojianli = (ImageView) findViewById(R.id.backTojianli);
        add_reward = (LinearLayout) findViewById(R.id.add_reward);
        hjjl_list = (ListView) findViewById(R.id.hjjl_list);
        noneWord = (TextView) findViewById(R.id.noneWord);
        edit_btn = (TextView) findViewById(R.id.edit_btn);

        backTojianli.setOnClickListener(this);
        add_reward.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        hjjl_datalist=new ArrayList<HashMap<String, String>>();

        JSONArray listRewarded = wodejianliFragment.listRewarded;
        LogUtils.v("listrewarded的值"+listRewarded.toString());
        if (listRewarded.length() == 0) {
            //无值的时候空反馈
            noneWord.setVisibility(View.VISIBLE);
        } else {
            //有值的时候列表显示
            noneWord.setVisibility(View.GONE);
            for (int i = 0; i < listRewarded.length(); i++) {
                HashMap<String, String> hjjl_data = new HashMap<String, String>();

                try {
                    JSONObject jsonobj = (JSONObject) listRewarded.get(i);
                    hjjl_data.put("id", jsonobj.get("id").toString());
                    hjjl_data.put("rewardedRank", jsonobj.get("rewardedRank").toString());
                    hjjl_data.put("rewardedTime", jsonobj.get("rewardedTime").toString());
                    hjjl_data.put("rewardedName", jsonobj.get("rewardedName").toString());

                    hjjl_datalist.add(hjjl_data);
                    LogUtils.v("hjjl_datalist"+hjjl_datalist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            resumeAdapterHjjl = new ResumeAdapter_hjjl(this, hjjl_frag.this, hjjl_datalist, R.layout.wode_hjjl_edit_ever, new String[]{"id","rewardedRank", "rewardedTime", "rewardedName"},
                    new int[]{R.id.hjjl_list_id, R.id.prize_degree, R.id.prize_gettime, R.id.prizeName});
            hjjl_list.setAdapter(resumeAdapterHjjl);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.backTojianli:
                //返回到简历的总页面，
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                resumeAdapterHjjl.changeState(false);
                break;
            case R.id.add_reward:
                addhjjl();
                break;
            case R.id.edit_btn:
                if(edit_btn.getText().toString()=="保存"&&resumeAdapterHjjl.isNeedtoshow()){
                    edit_btn.setText("编辑");
                    resumeAdapterHjjl.changeState(false);
                    resumeAdapterHjjl.notifyDataSetChanged();
                }else{
                    edit_btn.setText("保存");
                    resumeAdapterHjjl.changeState(true);
                    resumeAdapterHjjl.notifyDataSetChanged();
                }
                break;

        }
    }

    private void addhjjl() {
        HashMap<String,String> map = new HashMap<String,String>();
        //这些都是默认值

        if(!checkIsSaved()){
            //如果内容未保存的话
            Toast.makeText(this, "有内容未保存，请保存后进行下一步", Toast.LENGTH_SHORT).show();
        }else {
            //这些都是默认值
        map.put("id", "0");//未保存的id都是0,保存以后读取的时候就是从数据库读过来的内容
        map.put("rewardedRank", "奖项级别");
        map.put("rewardedTime", "获奖时间");
        map.put("rewardedName", "奖项名称");

        hjjl_datalist.add(map);
        LogUtils.v(hjjl_datalist.toString());
        //如果没有填写，单击添加之后产生一个新的
        if(resumeAdapterHjjl==null){
            resumeAdapterHjjl = new ResumeAdapter_hjjl(this, hjjl_frag.this, hjjl_datalist, R.layout.wode_hjjl_edit_ever, new String[]{"id","rewardedRank", "rewardedTime", "rewardedName"},
                    new int[]{R.id.jnjj_list_id, R.id.prize_degree, R.id.prize_gettime, R.id.prizeName});
            hjjl_list.setAdapter(resumeAdapterHjjl);

            noneWord.setVisibility(View.GONE);
        }else{
            //已经填写，就动态刷新
            resumeAdapterHjjl.notifyDataSetChanged();//动态刷新
        }
    }
        resumeAdapterHjjl.setCurrentItemInt(-1);//这时候还没有对新增的内容进行过修改，设置为默认状态
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
        for (int i = 0; i < hjjl_datalist.size(); i++) {
            if (hjjl_datalist.get(i).get("id").toString() == "0") {
                return false;
            }
        }
        return true;
    }
    //收到构造器发出来的修改信息
    //保存成功就改掉保存按钮的点击事件
    //保存获奖经历
    private void save(){
        //保存数据的http操作
        mapforhttp = new HashMap<String, String>();

        //没有选中的内容的时候
        if(resumeAdapterHjjl.getCurrentItemInt()==-1){
            Toast.makeText(hjjl_frag.this, "请填写内容然后保存", Toast.LENGTH_SHORT).show();
        }else {
            //获取最后修改的map
            HashMap<String, String> getforMap = resumeAdapterHjjl.getHashMap();
            LogUtils.v("jybj_获取到的map值"+resumeAdapterHjjl.getHashMap() + "");

            //如果有id证明是修改，就需要补全id,userid,没哟肚饿话表示是新添加的内容
            if ((getforMap.containsKey("id"))) {
                //如果有id这个属性
                mapforhttp.put("id", getforMap.get("id"));
                mapforhttp.put("userId", MainActivity.userid);
            }

            mapforhttp.put("token", MainActivity.token);
//            重构map
            mapforhttp.put("rewardedName", getforMap.get("rewardedName"));
            mapforhttp.put("rewardedRank", getforMap.get("rewardedRank"));
            mapforhttp.put("rewardedTime", getforMap.get("rewardedTime"));

            LogUtils.v("hjjlmap"+mapforhttp.values().toString());
            String url = Constant.getUrl() + "app/user/addRewards.htmls";
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
                                    Toast.makeText(hjjl_frag.this, message, Toast.LENGTH_SHORT).show();
                                    //更改文字提示
                                    edit_btn.setText("编辑");
                                    //去除单击保存事件，这个时候单击应该变成删除按钮出现
                                    edit_btn.setOnClickListener(hjjl_frag.this);
//                                    可以添加下一个了
                                    add_reward.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addhjjl();
                                        }
                                    });

                                    resumeAdapterHjjl.changeState(false);
                                    changeLocalMap(mapforhttp);
                                    resumeAdapterHjjl.notifyDataSetChanged();//刷新视图
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
    private void changeLocalMap(HashMap<String,String> mapforhttp) {

        //遍历教育背景datalist,以id为关键字找到对应的Map然后修改
        for(int i = 0 ;i < hjjl_datalist.size() ; i++){
            if(mapforhttp.containsKey("id")){
                //对应找到id相同的
                if(hjjl_datalist.get(i).get("id").toString()==mapforhttp.get("id").toString()){
                    hjjl_datalist.get(i).put("rewardedName", mapforhttp.get("rewardedName").toString());
                    hjjl_datalist.get(i).put("rewardedRank", mapforhttp.get("rewardedRank").toString());
                    hjjl_datalist.get(i).put("rewardedRank", mapforhttp.get("rewardedRank").toString());
                }
            }else{
                if(hjjl_datalist.get(i).get("id").toString()=="0"){
                    hjjl_datalist.get(i).put("id", "10000");//10000表示已经修改完毕
                    hjjl_datalist.get(i).put("rewardedName", mapforhttp.get("rewardedName").toString());
                    hjjl_datalist.get(i).put("rewardedRank", mapforhttp.get("rewardedRank").toString());
                    hjjl_datalist.get(i).put("rewardedRank", mapforhttp.get("rewardedRank").toString());
                }
            }
        }
        LogUtils.v("after_editor"+hjjl_datalist.toString());
    }
}
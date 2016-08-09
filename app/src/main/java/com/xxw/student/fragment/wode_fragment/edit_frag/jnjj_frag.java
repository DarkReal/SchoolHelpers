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

import com.xxw.student.Adapter.ResumeAdapter_jnjj;
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
 * 简历--技能简介
 * Created by DarkReal on 2016/7/5.
 */
public class jnjj_frag extends Activity implements View.OnClickListener{
    private ImageView backTojianli;
    private LinearLayout add_jnjj;
    private List<HashMap<String,String>> jnjj_datalist;
    private ListView jnjj_list;
    private ResumeAdapter_jnjj resumeAdapterJnjj;
    private TextView noneWord;
    private TextView edit_btn;
    private HashMap<String,String> mapforhttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_jnjj_edit);
        Constant.setSkill_list();//初始化
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
                        add_jnjj.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(jnjj_frag.this, "有内容未保存，请保存后进行下一步", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 1://删除完毕
                        jnjj_datalist.remove(resumeAdapterJnjj.getCurrentItemInt());
                        resumeAdapterJnjj.notifyDataSetChanged();//更新视图
                        break;
                }
            }
        };
        init();
    }
    private void init() {

        backTojianli = (ImageView) findViewById(R.id.backTojianli);

        add_jnjj = (LinearLayout) findViewById(R.id.add_skill);
        jnjj_list = (ListView) findViewById(R.id.jnjj_list);
        noneWord = (TextView) findViewById(R.id.noneWord);
        edit_btn = (TextView) findViewById(R.id.edit_btn);

        backTojianli.setOnClickListener(this);
        add_jnjj.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        //初始化数据
        jnjj_datalist = new ArrayList<HashMap<String, String>>();

        JSONArray listSkill = wodejianliFragment.listSkill;
        if (listSkill.length() == 0) {
            //无值的时候空反馈
            noneWord.setVisibility(View.VISIBLE);
        } else {
            //有值的时候列表显示
            noneWord.setVisibility(View.GONE);
            for (int i = 0; i < listSkill.length(); i++) {
                HashMap<String, String> jnjj_data = new HashMap<String, String>();

                try {
                    LogUtils.v(listSkill.toString());
                    JSONObject jsonobj = (JSONObject) listSkill.get(i);
                    jnjj_data.put("id",jsonobj.get("id").toString());
                    jnjj_data.put("skillLevel",Constant.skill_list.get(Integer.parseInt(jsonobj.get("skillLevel").toString()) - 1));
                    jnjj_data.put("skillName", jsonobj.get("skillName").toString());
                    jnjj_datalist.add(jnjj_data);
                    LogUtils.v("jnjj_datalist"+jnjj_datalist.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            resumeAdapterJnjj=new ResumeAdapter_jnjj(this,jnjj_frag.this, jnjj_datalist, R.layout.wode_jnjj_edit_ever, new String[] {"id","skillLevel","skillName"},
                    new int[] {R.id.jnjj_list_id, R.id.skilldegress, R.id.jnjj_skill});
            jnjj_list.setAdapter(resumeAdapterJnjj);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.backTojianli:
                //返回到简历的总页面，
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                resumeAdapterJnjj.changeState(false);
                break;
            case R.id.add_skill:
                addjnjj();
                break;
            case R.id.edit_btn:
                if(edit_btn.getText().toString()=="保存"&&resumeAdapterJnjj.isNeedtoshow()){
                    edit_btn.setText("编辑");
                    resumeAdapterJnjj.changeState(false);
                    resumeAdapterJnjj.notifyDataSetChanged();
                }else{
                    edit_btn.setText("保存");
                    resumeAdapterJnjj.changeState(true);
                    resumeAdapterJnjj.notifyDataSetChanged();
                }
                break;
        }
    }

    private void addjnjj() {
        HashMap<String,String> map = new HashMap<String,String>();
        //这些都是默认值
        if(!checkIsSaved()){
            //如果内容未保存的话
            Toast.makeText(this, "有内容未保存，请保存后进行下一步", Toast.LENGTH_SHORT).show();
        }else {
            map.put("id","0");//未保存的id都是0,保存以后读取的时候就是从数据库读过来的内容
            map.put("skillLevel","了解");
            map.put("skillName", "技能名称");
            jnjj_datalist.add(map);
            LogUtils.v(jnjj_datalist.toString());
            //如果没有填写，单击添加之后产生一个新的
            if(resumeAdapterJnjj==null){

                resumeAdapterJnjj=new ResumeAdapter_jnjj(this,jnjj_frag.this, jnjj_datalist, R.layout.wode_jnjj_edit_ever, new String[] {"id","skillLevel","skillName"},
                        new int[] {R.id.jnjj_list_id, R.id.skilldegress, R.id.jnjj_skill});
                jnjj_list.setAdapter(resumeAdapterJnjj);
                //显示无内容的那个textview隐藏起来
                noneWord.setVisibility(View.GONE);
            }else{
                //已经填写，就动态刷新
                resumeAdapterJnjj.notifyDataSetChanged();//动态刷新
            }
        }
        resumeAdapterJnjj.setCurrentItemInt(-1);//这时候还没有对新增的内容进行过修改，设置为默认状态
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
        for (int i = 0; i < jnjj_datalist.size(); i++) {
            if (jnjj_datalist.get(i).get("id").toString() == "0") {
                return false;
            }
        }
        return true;
    }

    private void save() {
        //保存数据的http操作
        mapforhttp = new HashMap<String, String>();

        //没有选中的内容的时候
        if(resumeAdapterJnjj.getCurrentItemInt()==-1){
            Toast.makeText(jnjj_frag.this, "请填写内容然后保存", Toast.LENGTH_SHORT).show();
        }else {
            //获取最后修改的map
            HashMap<String, String> getforMap = resumeAdapterJnjj.getHashMap();
            LogUtils.v("jnjj_获取到的map值"+resumeAdapterJnjj.getHashMap() + "");

            //如果有id证明是修改，就需要补全id,userid,没哟肚饿话表示是新添加的内容
            if ((getforMap.containsKey("id"))) {
                //如果有id这个属性
                mapforhttp.put("id", getforMap.get("id"));
                mapforhttp.put("userId", MainActivity.userid);
            }

            mapforhttp.put("token", MainActivity.token);
            mapforhttp.put("skillLevel", getforMap.get("skillLevel"));
            mapforhttp.put("skillName", getforMap.get("skillName"));


            LogUtils.v("map"+mapforhttp.values().toString());
            String url = Constant.getUrl() + "app/user/addSkills.htmls";
            try {
                HttpThread ht = new HttpThread(url, mapforhttp) {
                    @Override
                    public void getObj(final JSONObject obj) throws JSONException {
                        if (obj != null) {
                            final String message = obj.get("message").toString();
                            LogUtils.v("-------jsonstr------"+obj.toString());
                            LogUtils.v("message: "+obj.get("message").toString());
                            //只能在handler中更新视图
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(jnjj_frag.this, message, Toast.LENGTH_SHORT).show();
                                    //更改文字提示
                                    edit_btn.setText("编辑");
                                    //去除单击保存事件，这个时候单击应该变成删除按钮出现
                                    edit_btn.setOnClickListener(jnjj_frag.this);
//                                    可以添加下一个了
                                    add_jnjj.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addjnjj();
                                        }
                                    });
                                    resumeAdapterJnjj.storagecount = 0;
                                    resumeAdapterJnjj.changeState(false);
                                    changeLocalMap(mapforhttp);
                                    resumeAdapterJnjj.notifyDataSetChanged();//刷新视图
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
        for(int i = 0 ;i < jnjj_datalist.size() ; i++){
            if(mapforhttp.containsKey("id")){
                //对应找到id相同的
                if(jnjj_datalist.get(i).get("id").toString()==mapforhttp.get("id").toString()){
                    jnjj_datalist.get(i).put("skillLevel", Constant.skill_list.get(Integer.parseInt(mapforhttp.get("skillLevel").toString())-1));
                    jnjj_datalist.get(i).put("skillName", mapforhttp.get("skillName").toString());
                }
            }else{
                if(jnjj_datalist.get(i).get("id").toString()=="0"){
                    jnjj_datalist.get(i).put("id","10000");//10000表示已经修改完毕
                    jnjj_datalist.get(i).put("skillLevel", Constant.skill_list.get(Integer.parseInt(mapforhttp.get("skillLevel").toString())-1));
                    jnjj_datalist.get(i).put("skillName", mapforhttp.get("skillName").toString());
                }
            }
        }
        LogUtils.v("after_editor"+jnjj_datalist.toString());
    }
}
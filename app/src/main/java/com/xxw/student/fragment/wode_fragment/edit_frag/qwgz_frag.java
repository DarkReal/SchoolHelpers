package com.xxw.student.fragment.wode_fragment.edit_frag;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.wodejianliFragment;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.framework.picker.DatePicker;
import com.xxw.student.view.framework.picker.OptionPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 简历--期望工作
 * Created by DarkReal on 2016/7/5.
 */
public class qwgz_frag extends Activity implements View.OnClickListener{
    private ImageView backTojianli;
    private TextView qwgz_job,qwgz_city,qwgz_day_require,DateBegin,DateEnd,qw_salarybyday,daogangrq;
    private TextView edit_btn;
    private String TAG = "qwgz_frag";
    private OptionPicker optionPicker;
    private DatePicker datePicker;
    private HashMap<String,String> mapforhttp;
    private String mapid;
    private MaterialDialog materialDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wode_qiwanggongzuo_edit);
        Commonhandler.comHandler = new Handler(){
            public void handleMessage (Message msg) {
                LogUtils.v("in hanlder");
                switch(msg.what) {
                    case -1://有修改值
                        LogUtils.v(msg.what + "");
                        edit_btn.setVisibility(View.VISIBLE);//可见
                        edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                save();
                            }
                        });
                        edit_btn.setText("保存");
                        break;
                }
            }
        };
        init();
    }
    //初始化载入数据
    private void init() {

        backTojianli = (ImageView) findViewById(R.id.backTojianli);
        qwgz_job = (TextView) findViewById(R.id.qwgz_job);
        qwgz_city = (TextView) findViewById(R.id.qwgz_city);
        qwgz_day_require = (TextView) findViewById(R.id.qwgz_day_require);
        DateBegin = (TextView) findViewById(R.id.DateBegin);
        DateEnd = (TextView) findViewById(R.id.DateEnd);
        qw_salarybyday = (TextView) findViewById(R.id.qw_salarybyday);
        daogangrq = (TextView) findViewById(R.id.daogangrq);
        edit_btn = (TextView) findViewById(R.id.edit_btn);
        edit_btn.setVisibility(View.GONE);//起初不可见 操作过一次以后必须可见

        backTojianli.setOnClickListener(this);
        qwgz_job.setOnClickListener(this);
        qwgz_city.setOnClickListener(this);
        qwgz_day_require.setOnClickListener(this);
        DateBegin.setOnClickListener(this);
        DateEnd.setOnClickListener(this);
        qw_salarybyday.setOnClickListener(this);
        daogangrq.setOnClickListener(this);


        JSONArray listJob = wodejianliFragment.listJob;
        if (listJob.length() == 0) {
            //无值的时候填默认值
//            并且这个时候给数据库插入一条默认id=0的值，方便之后修改
            qwgz_job.setText("单击编辑");
            qwgz_city.setText("单击编辑");
            qwgz_day_require.setText("5");
            DateBegin.setText("2016-04");
            DateEnd.setText("2016-07");
            qw_salarybyday.setText("单击编辑");
            daogangrq.setText("2016-07-01");

            insertone();//插入一条默认数据，以后改的都是它
        } else {
            //有值的时候设置值
            for (int i = 0; i < listJob.length(); i++) {

                try {
                    JSONObject jsonobj = (JSONObject) listJob.get(i);
                    //赋值
                    qwgz_job.setText(jsonobj.get("position").toString());
                    qwgz_city.setText(jsonobj.get("city").toString());
                    qwgz_day_require.setText(jsonobj.get("workDayByWeek").toString());
                    DateBegin.setText(jsonobj.get("workMouthBegin").toString());
                    DateEnd.setText(jsonobj.get("workMouthEnd").toString());
                    qw_salarybyday.setText(jsonobj.get("moneyByDay").toString());
                    daogangrq.setText(jsonobj.get("toDay").toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    //初始化：插入一条默认数据
    private void insertone() {

        mapforhttp = new HashMap<String, String>();

        mapforhttp.put("userId", MainActivity.userid);
        mapforhttp.put("token", MainActivity.token);
        mapforhttp.put("position", "单击编辑");
        mapforhttp.put("city", "单击编辑");
        mapforhttp.put("workDayByWeek", "5");
        mapforhttp.put("workMouthBegin", "2016-04");
        mapforhttp.put("workMouthEnd", "2016-07");
        mapforhttp.put("moneyByDay", "单击编辑");
        mapforhttp.put("toDay", "2016-07-01");

        LogUtils.v("map"+mapforhttp.values().toString());
        String url = Constant.getUrl() + "app/user/addExpectJob.htmls";
        try {
            HttpThread ht = new HttpThread(url, mapforhttp) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("---jsonstr---"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());
                        //只能在handler中更新视图
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog(qwgz_frag.this)
                                        .setTitle("警告")
                                        .autodismiss(2000)
                                        .setMessage(message)
                                        .show();
                                //更改文字提示
                                //去除单击保存事件，这个时候单击应该变成删除按钮出现
                                edit_btn.setVisibility(View.GONE);
                                try {
                                    mapid = obj.get("object").toString();
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


    @Override
    public void onClick(View v) {
        //公用的dialog内部属性
        LayoutInflater inflater = LayoutInflater.from(qwgz_frag.this);
        View viewinflator = inflater.inflate(R.layout.custom_alertdialog_edit, null);
        switch (v.getId()){
            case R.id.backTojianli:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

            case R.id.qwgz_job:

                final EditText contentView1 = new EditText(qwgz_frag.this);
                contentView1.setText(qwgz_job.getText().toString());
                contentView1.setTextColor(Color.GRAY);
                contentView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            contentView1.setText("");
                        }
                    }
                });
                materialDialog = new MaterialDialog(qwgz_frag.this);
                materialDialog.setContentView(contentView1);
                materialDialog.setTitle("期望工作")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            //单击确认之后发送请求
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                if (!qwgz_job.getText().toString().equals(contentView1.getText().toString())) {
                                    Log.v(TAG, "原值：" + qwgz_job.getText().toString() + "修改后的值" + contentView1.getText().toString());
                                    qwgz_job.setText(contentView1.getText().toString());
                                    changed(-1);
                                }
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();

                break;
            case R.id.qwgz_city:

                final EditText contentView2 = new EditText(qwgz_frag.this);
                contentView2.setText(qwgz_city.getText().toString());
                contentView2.setTextColor(Color.GRAY);
                contentView2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            contentView2.setText("");
                        }
                    }
                });
                materialDialog = new MaterialDialog(qwgz_frag.this);
                materialDialog.setContentView(contentView2);
                materialDialog.setTitle("期望城市")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            //单击确认之后发送请求
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                if (!qwgz_city.getText().toString().equals(contentView2.getText().toString())) {
                                    LogUtils.v("原值：" + qwgz_city.getText().toString() + "修改后的值" + contentView2.getText().toString());
                                    qwgz_city.setText(contentView2.getText().toString());
                                    changed(-1);
                                }
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.qwgz_day_require:
                Constant.setWorkDatePerWeek();//初始化
                optionPicker = new OptionPicker(qwgz_frag.this, Constant.workDatePerWeek);
                optionPicker.show();
                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        Toast.makeText(qwgz_frag.this, Constant.workDatePerWeek + "in_ResumeAdpater", Toast.LENGTH_SHORT).show();

                        if (!qwgz_day_require.getText().toString().equals(Constant.workDatePerWeek.get(position).toString())) {

                            qwgz_day_require.setText(Constant.workDatePerWeek.get(position).toString());
                            changed(-1);
                        }
                    }
                });
                break;
            case R.id.DateBegin:
                datePicker = new DatePicker(this, DatePicker.YEAR_MONTH);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        Toast.makeText(qwgz_frag.this, year + "-" + month, Toast.LENGTH_SHORT).show();
                        if (!DateBegin.getText().toString().equals(year + "-" + month)) {
                            Log.v(TAG, "原值：" + DateBegin.getText().toString() + "修改后的值" + year + "-" + month);
                            DateBegin.setText(year + "-" + month);

                            changed(-1);
                        }
                    }
                });
                break;
            case R.id.DateEnd:
                datePicker = new DatePicker(this, DatePicker.YEAR_MONTH);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        Toast.makeText(qwgz_frag.this, year + "-" + month, Toast.LENGTH_SHORT).show();
                        if (!DateEnd.getText().toString().equals(year + "-" + month)) {
                            LogUtils.v("原值：" + DateEnd.getText().toString() + "修改后的值" + year + "-" + month);
                            DateEnd.setText(year + "-" + month);
                            changed(-1);
                        }
                    }
                });
                break;
            case R.id.qw_salarybyday:

                final EditText contentView3 = new EditText(qwgz_frag.this);
                contentView3.setText(qw_salarybyday.getText().toString());
                contentView3.setTextColor(Color.GRAY);
                contentView3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            contentView3.setText("");
                        }
                    }
                });
                materialDialog = new MaterialDialog(qwgz_frag.this);
                materialDialog.setContentView(contentView3);
                materialDialog.setTitle("期望日薪")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            //单击确认之后发送请求
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                if (!qw_salarybyday.getText().toString().equals(contentView3.getText().toString())) {
                                    LogUtils.v("原值：" + qw_salarybyday.getText().toString() + "修改后的值" + contentView3.getText().toString());
                                    qw_salarybyday.setText(contentView3.getText().toString());
                                    changed(-1);
                                }
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.daogangrq:
                datePicker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month,String day) {
                        Toast.makeText(qwgz_frag.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                        if (!daogangrq.getText().toString().equals(year + "-" + month + "-" + day)) {
                            LogUtils.v("原值：" + daogangrq.getText().toString() + "修改后的值" + year + "-" + month + "-" + day);
                            daogangrq.setText(year + "-" + month + "-" + day);
                            changed(-1);
                        }
                    }
                });
                break;

        }
    }
    //改变了之后给主页面发消息，更改主页面的ui视图
    public void changed(int state){
        Message msg = new Message();
        msg.what = state;//-1表示有变动
        Commonhandler.comHandler.sendMessageDelayed(msg, 10);
    }
    private void save() {
        //保存数据的http操作
            mapforhttp = new HashMap<String, String>();
            mapforhttp.put("id",mapid);
            mapforhttp.put("userId", MainActivity.userid);
            mapforhttp.put("token", MainActivity.token);
            mapforhttp.put("position", qwgz_job.getText().toString().trim());
            mapforhttp.put("city", qwgz_city.getText().toString().trim());
            mapforhttp.put("workDayByWeek", qwgz_day_require.getText().toString().trim());
            mapforhttp.put("workMouthBegin", DateBegin.getText().toString().trim());
            mapforhttp.put("workMouthEnd", DateEnd.getText().toString().trim());
            mapforhttp.put("moneyByDay", qw_salarybyday.getText().toString().trim());
            mapforhttp.put("toDay", daogangrq.getText().toString().trim());


            LogUtils.v("map"+mapforhttp.values().toString());
            String url = Constant.getUrl() + "app/user/addExpectJob.htmls";
            try {
                HttpThread ht = new HttpThread(url, mapforhttp) {
                    @Override
                    public void getObj(final JSONObject obj) throws JSONException {
                        if (obj != null) {
                            final String message = obj.get("message").toString();
                            LogUtils.v("----jsonstr---"+obj.toString());
                            LogUtils.v("message: "+obj.get("message").toString());
                            //只能在handler中更新视图
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(qwgz_frag.this, message, Toast.LENGTH_SHORT).show();
                                    //更改文字提示
                                    edit_btn.setText("编辑");
                                    //去除单击保存事件，这个时候单击应该变成删除按钮出现
                                    edit_btn.setVisibility(View.GONE);
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

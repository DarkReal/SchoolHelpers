package com.xxw.student.fragment.login_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 忘记密码页
 * Created by DarkReal on 2016/7/13.
 */
public class forget_pass extends Activity {
    private EditText phone,yzm;
    private TextView getyzm;
    private MyCount mc;
    private Button reset_pass;
    private static String phonestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_forget_pass);
        Intent intent = getIntent();
        phonestr = intent.getStringExtra("phonenumber");
        phone = (EditText) findViewById(R.id.phone);
        yzm = (EditText) findViewById(R.id.yzm);
        getyzm = (TextView) findViewById(R.id.getyzm);
        reset_pass = (Button) findViewById(R.id.reset_pass);

        phone.setText(phonestr);
        getyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送验证码
                mc = new MyCount(60000, 1000);
                mc.start();

                //发送验证码
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone", phonestr);
                LogUtils.v("map" + map.values().toString());

                String url = Constant.getUrl() + "app/user/getPhoneCode.htmls";
                try {
                    HttpThread ht = new HttpThread(url, map) {
                        @Override
                        public void getObj(final JSONObject obj) throws JSONException {
                            if (obj != null) {
                                final String message = obj.get("message").toString();
                                LogUtils.v("---jsonstr---" + obj.toString());
                                LogUtils.v("message: " + obj.get("message").toString());

                                getHandler.mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (!obj.get("code").toString().equals("10000"))
                                                new MaterialDialog(forget_pass.this)
                                                        .setTitle("警告")
                                                        .autodismiss(2000)
                                                        .setMessage(message)
                                                        .show();
                                            else {
//                                                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

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
        });
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yzyzm();
            }
        });
    }
    //自定义倒计时类
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            getyzm.setText("重新获取");
            getyzm.setClickable(true);
            this.cancel();
        }
        @Override

        public void onTick(long millisUntilFinished) {
            getyzm.setText("(" + millisUntilFinished / 1000 + ")");
            getyzm.setClickable(false);
        }
    }
    private void yzyzm() {
        if(!yzm.getText().toString().equals("")){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("phone",phonestr);
            map.put("pCode", yzm.getText().toString());
            LogUtils.v("map" + map.values().toString());
            String url= Constant.getUrl()+"app/user/getCheckCode.htmls";
            //验证验证码
            try{

                HttpThread ht = new HttpThread(url,map){
                    @Override
                    public void getObj(final JSONObject obj) throws JSONException {
                        if(obj!=null){
                            final String message = obj.get("message").toString();
                            LogUtils.v("message: "+obj.get("message").toString());
                            getHandler.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!obj.get("code").toString().equals("10000"))
                                            new MaterialDialog(forget_pass.this)
                                                    .setTitle("警告")
                                                    .autodismiss(1000)
                                                    .setMessage(message)
                                                    .show();
                                        else {
                                            //验证成功跳转页面
//                                            new MaterialDialog(forget_pass.this)
//                                                    .setTitle("警告")
//                                                    .autodismiss(1000)
//                                                    .setMessage("验证成功")
//                                                    .show();
                                            Intent intent = new Intent();
                                            intent.setClass(forget_pass.this,reset_password.class);
                                            intent.putExtra("phonestr",phone.getText().toString());
                                            startActivity(intent);
                                            finish();
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
        }else{
            new MaterialDialog(forget_pass.this)
                    .setTitle("警告")
                    .autodismiss(1000)
                    .setMessage("请填写验证码")
                    .show();
        }
    }
}

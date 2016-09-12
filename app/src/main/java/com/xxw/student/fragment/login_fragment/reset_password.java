package com.xxw.student.fragment.login_fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xxw.student.LoginActivity;
import com.xxw.student.MainActivity;
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
 * 重置密码页
 * Created by DarkReal on 2016/7/13.
 */
public class reset_password extends Activity{
    private EditText newpass,newpassagain;
    private Button reset_now;
    private static String phonestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new_pass);
        Intent intent = getIntent();
        phonestr = intent.getStringExtra("phonestr");

        newpass = (EditText) findViewById(R.id.newpass);
        newpassagain = (EditText) findViewById(R.id.newpassagain);
        reset_now = (Button) findViewById(R.id.reset_now);


        reset_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查两次密码是否一致
                String newpassstr = newpass.getText().toString().trim().toLowerCase();
                String newpassagainstr = newpassagain.getText().toString().trim().toLowerCase();
                if (newpassstr.equals(""))
                    new MaterialDialog(reset_password.this)
                            .setTitle("警告")
                            .autodismiss(1000)
                            .setMessage("请填写新密码")
                            .show();
                else if (newpassagainstr.equals(""))
                    new MaterialDialog(reset_password.this)
                            .setTitle("警告")
                            .autodismiss(1000)
                            .setMessage("请再次确认密码")
                            .show();
                else if(!newpassstr.equals(newpassagainstr))
                    new MaterialDialog(reset_password.this)
                            .setTitle("警告")
                            .autodismiss(1000)
                            .setMessage("前后密码请保持一致")
                            .show();
                else{
                    //全部通过了就发送请求

                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("phone",phonestr);
                    map.put("password", newpassstr);
                    LogUtils.v("map" + map.values().toString());
                    String url= Constant.getUrl()+"app/user/editPassword.htmls";
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
                                                    new MaterialDialog(reset_password.this)
                                                            .setTitle("警告")
                                                            .autodismiss(1000)
                                                            .setMessage(message)
                                                            .show();
                                                else {
                                                    //验证成功跳转页面
                                                    Intent intent = new Intent();
                                                    intent.setClass(reset_password.this,LoginActivity.class);
                                                    //保存登录状态
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

                }
            }
        });


    }
}

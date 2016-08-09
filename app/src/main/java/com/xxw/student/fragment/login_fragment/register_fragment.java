package com.xxw.student.fragment.login_fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.My_Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 注册
 * Created by DarkReal on 2016/5/5.
 */
public class register_fragment extends Fragment {
    private View view;
    private Button free_register;
    private My_Toast my_toast;
    private EditText phone,username,password,password_again,verification;
    private TextView yzm;
    private HashMap<String,String> map;
    private String ToastText="";
    private Toast simpletoast;
    private String phonestr,usernamestr,passstr,passagainstr,verifistr;
    private JSONObject obj = new JSONObject();
    private MyCount mc;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.register,container,false);

        init();
        my_toast = My_Toast.createToastConfig();//初始化
        free_register = (Button) view.findViewById(R.id.free_register);
        free_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastText="";
                check();
                //如果校验通过
                if(!ToastText.equals("")){
                    simpletoast= Toast.makeText(view.getContext(), ToastText, Toast.LENGTH_SHORT);
                    simpletoast.show();
                }
                else{
                    //发送请求过去
                    yzyzm();
                    createUser();
                    Intent intent = new Intent();
                    intent.setClass(view.getContext(),main_login.class);
                    intent.putExtra("frag",0);
                    startActivity(intent);
                }
            }


        });
        return view;
    }
    //创建用户
    private void createUser() {
        map = new HashMap<String, String>();
        map.put("phone",phonestr);
        map.put("password", passstr);
        map.put("realName",usernamestr);
        LogUtils.v("map"+map.values().toString());
        String url= Constant.getUrl()+"app/user/createUser.htmls";
        //验证验证码
        try{

            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v(obj.get("message").toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                Intent intent = new Intent();
                                                intent.setClass(view.getContext(), MainActivity.class);
                                                savestatus();
                                                startActivity(intent);
                                                main_login main_login = (main_login) getActivity();
                                                main_login.finish();
                                            }

                                        }, 1500);
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

    private void yzyzm() {
        map = new HashMap<String, String>();
        map.put("phone",phonestr);
        map.put("pCode", verifistr);
        LogUtils.v("map"+map.values().toString());
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
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                Intent intent = new Intent();
                                                intent.setClass(view.getContext(), MainActivity.class);
                                                savestatus();
                                                startActivity(intent);
                                                main_login main_login = (main_login) getActivity();
                                                main_login.finish();
                                            }

                                        }, 1500);
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

    private void savestatus() {
        //Context.MODE_PRIVATE  只有这个应用可以读出来
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("phone", map.get("phone").toString());
        editor.putString("mobile",map.get("mobile").toString());
        editor.commit();//提交修改
    }

    public void init(){
        phone = (EditText) view.findViewById(R.id.phone);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        password_again = (EditText) view.findViewById(R.id.password_again);
        yzm = (TextView) view.findViewById(R.id.yzm);
        verification = (EditText) view.findViewById(R.id.verification);

        yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonestr = phone.getText().toString().trim().toLowerCase();
                if (phonestr.equals("")){
                    ToastText = "请填写手机号码";
                    Toast.makeText(view.getContext(), ToastText, Toast.LENGTH_SHORT).show();
                }else{
                    //启动倒计时
                    mc = new MyCount(60000, 1000);
                    mc.start();

                    //发送验证码
                    HashMap<String,String> map = new HashMap<String, String>();
                    map.put("phone",phonestr);
                    LogUtils.v("map"+map.values().toString());
                    String url = Constant.getUrl() + "app/user/getPhoneCode.htmls";
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
                                                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                                else {
                                                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
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
            }
        });
    }
    public void check() {
        phonestr = phone.getText().toString().trim().toLowerCase();
        usernamestr = username.getText().toString().trim().toLowerCase();
        passstr = password.getText().toString().trim().toLowerCase();
        passagainstr = password_again.getText().toString().trim().toLowerCase();
        verifistr = verification.getText().toString().trim();

        if (phonestr.equals(""))
            ToastText = "请填写手机号码";
        else if(verifistr.equals(""))
            ToastText = "请填写验证码";
        else if (usernamestr.equals(""))
            ToastText = "请填写用户名";
        else if (passstr.equals(""))
            ToastText = "请填写密码";
        else if(passagainstr.equals(""))
            ToastText = "请再次填写密码";
        else if(!passstr.equals(passagainstr)) {
            ToastText = "两次密码不一致";
            password_again.setText("");
        }

    }
    //自定义倒计时类
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            yzm.setText("重新获取");
            yzm.setClickable(true);
            this.cancel();
        }
        @Override

        public void onTick(long millisUntilFinished) {
            yzm.setText("(" + millisUntilFinished / 1000 + ")...");
            yzm.setClickable(false);
        }
    }

}

package com.xxw.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xxw.student.fragment.login_fragment.main_login;

/**
 * 登陆的Activity
 * Created by DarkReal on 2016/5/5.
 */
public class LoginActivity extends Activity {
    private Button login;
    private Button register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        login = (Button) this.findViewById(R.id.login);
        register = (Button) this.findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //与服务器端通信
                loginto();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, main_login.class);
                intent.putExtra("frag", "1");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.up_to, 0);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, main_login.class);
                intent.putExtra("frag", "0");
                startActivity(intent);
                //页面载入的时候的动画效果
                finish();
                overridePendingTransition(R.anim.up_to, 0);
            }
        });
    }

    private void  loginto(){
        /*String strUrl = "http://172.0.0.1:8080/sybserver/login";
        URL url = null;
        try{
            url = new URL(strUrl);
            HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
            urlconn.setDoInput(true);
            urlconn.setDoInput(true);
            urlconn.setRequestMethod("POST");
            urlconn.setUseCaches(false);
            urlconn.setRequestProperty("Content-Type","application/x-www-form-url");
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }*/
        /*String url = "http://192.168.10.76:8080/xybserver/login/loginfirst";
        new HttpThread(url,)*/
    }
}

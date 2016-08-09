package com.xxw.student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.xxw.student.utils.LogUtils;

/**
 * 进入应用的时候的启动页
 * Created by DarkReal on 2016/5/7.
 */
public class firstpage extends Activity {
    private static final String TAG = "---firstpage---";
    private String phone;
    private int status;//0代表未登录，1代表已经登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        ImageView icon=(ImageView)this.findViewById(R.id.icon_pic);
        ImageView icon_text = (ImageView) this.findViewById(R.id.icon_text);

        final SharedPreferences preferences = getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        LogUtils.v("----preference---" + preferences.getAll().size());
        if(preferences.getAll().size()==0){
            status=0;
        }else{
            phone = preferences.getString("phone","");
            LogUtils.v("----phone---"+phone.toString());
            if(phone!=""&&phone!=null){
                status=1;
            }else {
                status=0;
            }
        }

        AlphaAnimation aa=new AlphaAnimation(0.5f,1.0f);
        aa.setDuration(700);
        icon.startAnimation(aa);
        icon_text.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if (status == 0) {
                    intent.setClass(firstpage.this, LoginActivity.class);
                } else if (status == 1) {
                    intent.setClass(firstpage.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
                overridePendingTransition(0, R.anim.alpha_remove);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }
            //屏蔽返回键
        });
    }

}

package com.xxw.student;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.fragment.contentFragment_quanzi;
import com.xxw.student.fragment.contentFragment_shouye;
import com.xxw.student.fragment.contentFragment_wode;
import com.xxw.student.fragment.contentFragment_xiaozhitiao;
import com.xxw.student.fragment.titleFragment_quanzi;
import com.xxw.student.fragment.titleFragment_shouye;
import com.xxw.student.fragment.titleFragment_wode;
import com.xxw.student.fragment.titleFragment_xiaozhitiao;
import com.xxw.student.utils.LogUtils;

/**
 * 主Activity展示
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private boolean[] whatPage = {false,false,false,false};
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getWodeInfo();//获取
        setDefault();
        setBarEvent();
        setContentFragment();

        //页面跳转
        Intent tointent = getIntent();
        String page = tointent.getStringExtra("page");


        if(page!=null){
            if(page.contains("-")){
                String arr[] = page.split("-");
                topage = arr[1];
                changeFragment(Integer.parseInt(arr[0]));
                changePage(Integer.parseInt(arr[0]));
            }else{
                changeFragment(Integer.parseInt(page));
                changePage(Integer.parseInt(page));
            }

        }
    }


    private Fragment[] contentFragment = new Fragment[4];
    private Fragment[] titleFragment = new Fragment[4];
    private contentFragment_shouye cf_shouye  ;
    private contentFragment_quanzi cf_quanzi  ;
    private contentFragment_xiaozhitiao cf_xiaozhitiao  ;
    private contentFragment_wode cf_wode  ;

    private titleFragment_shouye tf_shouye ;
    private titleFragment_quanzi tf_quanzi ;
    private titleFragment_xiaozhitiao tf_xiaozhitiao ;
    private titleFragment_wode tf_wode ;

    private ImageView[] bottom_bar_image = new ImageView[4];
    private TextView[] bottom_bar_text = new TextView[4];

    public static String phone;//全局username对象
    public static String headpic;//全局头像
    public static String token;//全局token
    public static String userid;//全局userid
    public static String city;//全局城市
    public static String nickname;//昵称
    public static String topage;//公共的跳转到哪个页面
    public void setDefault(){
        bottom_bar_image[0] = (ImageView)findViewById(R.id.bottom_bar_image_shouye);
        bottom_bar_image[1] = (ImageView)findViewById(R.id.bottom_bar_image_quanzi);
        bottom_bar_image[2] = (ImageView)findViewById(R.id.bottom_bar_image_xiaozhitiao);
        bottom_bar_image[3] = (ImageView)findViewById(R.id.bottom_bar_image_wode);

        bottom_bar_text[0] = (TextView)findViewById(R.id.bottom_bar_text_shouye);
        bottom_bar_text[1] = (TextView)findViewById(R.id.bottom_bar_text_quanzi);
        bottom_bar_text[2] = (TextView)findViewById(R.id.bottom_bar_text_xiaozhitiao);
        bottom_bar_text[3] = (TextView)findViewById(R.id.bottom_bar_text_wode);

        cf_shouye = new contentFragment_shouye() ;
        contentFragment[0] = cf_shouye ;
        cf_quanzi = new contentFragment_quanzi() ;
        contentFragment[1] = cf_quanzi ;
        cf_xiaozhitiao = new contentFragment_xiaozhitiao() ;
        contentFragment[2] = cf_xiaozhitiao ;
        cf_wode = new contentFragment_wode() ;
        contentFragment[3] = cf_wode ;

        tf_shouye = new titleFragment_shouye();
        titleFragment[0] = tf_shouye;
        tf_quanzi = new titleFragment_quanzi();
        titleFragment[1] = tf_quanzi;
        tf_xiaozhitiao = new titleFragment_xiaozhitiao();
        titleFragment[2] = tf_xiaozhitiao;
        tf_wode = new titleFragment_wode();
        titleFragment[3] = tf_wode;

    }

    private LinearLayout shouye,quanzi,xiaozhitiao,wode;
    public void setBarEvent(){
        shouye = (LinearLayout)findViewById(R.id.bottom_bar_shouye);
        quanzi = (LinearLayout)findViewById(R.id.bottom_bar_quanzi);
        xiaozhitiao = (LinearLayout)findViewById(R.id.bottom_bar_xiaozhitiao);
        wode = (LinearLayout)findViewById(R.id.bottom_bar_wode);

        shouye.setOnClickListener(this);
        quanzi.setOnClickListener(this);
        xiaozhitiao.setOnClickListener(this);
        wode.setOnClickListener(this);
    }

    public void setContentFragment(){
        whatPage[0] = true ;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_content, contentFragment[0]);
        ft.replace(R.id.fragment_title, titleFragment[0]);
        ft.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bottom_bar_shouye :
                if(!whatPage[0]){
                    changeFragment(0);
                    changePage(0);
                }
                break;
            case R.id.bottom_bar_quanzi :
                if(!whatPage[1]){
                    changeFragment(1);
                    changePage(1);
                }
                break;
            case R.id.bottom_bar_xiaozhitiao :
                if(!whatPage[2]){
                    changeFragment(2);
                    changePage(2);
                }
                break;
            case R.id.bottom_bar_wode :
                if(!whatPage[3]){
                    changeFragment(3);
                    changePage(3);
                }
                break;
        }
    }



    private int[] selected_image = {R.drawable.icon_selected_shouye, R.drawable.icon_selected_quanzi,
            R.drawable.icon_selected_xiaozhitiao, R.drawable.icon_selected_wode};
    private int[] normal_image = {R.drawable.icon_nomal_shouye, R.drawable.icon_nomal_quanzi,
            R.drawable.icon_nomal_xiaozhitiao, R.drawable.icon_nomal_wode};
    //更换主体的fragment
    public void changeFragment(int index){
        for(int i =0;i<4;i++){
            if(i==index){
                bottom_bar_text[i].setTextColor(getResources().getColor(R.color.xiaozhitiao_message_lianxiren));
                bottom_bar_image[i].setImageResource(selected_image[i]);
            }else {
                bottom_bar_text[i].setTextColor(getResources().getColor(R.color.bottom_bar_textColor));
                bottom_bar_image[i].setImageResource(normal_image[i]);
            }
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_title,titleFragment[index]);
        ft.replace(R.id.main_content,contentFragment[index]);
        ft.commit();
    }

    public void changePage(int index){
        whatPage[index] = true ;
        for(int i=0;i<contentFragment.length;i++)
            if(i!=index)
                whatPage[i] = false ;
    }


    //获取我的信息
    public void getWodeInfo(){
        SharedPreferences preferences = getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);
        phone = preferences.getString("phone","");
        token = preferences.getString("token", "");
        userid = preferences.getString("id","");
        city = preferences.getString("currcity","");
        headpic = preferences.getString("headPic","");
        nickname = preferences.getString("nickname","");
        Toast.makeText(this, "当前用户:" + phone, Toast.LENGTH_SHORT).show();
    }
    public static void emptyAll(){
        MainActivity.token="";
        MainActivity.phone="";
        MainActivity.headpic="";
        MainActivity.userid="";
        MainActivity.city="";
        MainActivity.nickname="";
    }
}

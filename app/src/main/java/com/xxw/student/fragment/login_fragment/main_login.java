package com.xxw.student.fragment.login_fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.LogUtils;

/**
 * 包含login和register两个fragment的总activity
 * Created by DarkReal on 2016/5/5.
 */
public class main_login extends Activity implements View.OnClickListener,GestureDetector.OnGestureListener{
    private boolean[] page_select={false,false};

    private login_fragment login_fragment;
    private com.xxw.student.fragment.login_fragment.register_fragment register_fragment;
    private Fragment[] login_frag = new Fragment[2];

    private TextView[] select_btn = new TextView[2];
    private TextView free_register,login;
    private View[] below_line = new View[2];
    private View line1,line2;
    private Intent intent;
    private GestureDetector gestureDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_frag);
        init();
        setTxtEvent();
        setContentFragment();

    }


    private void init() {
        free_register = (TextView) findViewById(R.id.free_register);
        login = (TextView) findViewById(R.id.login);
        select_btn[0] = free_register;
        select_btn[1] = login;

        login_fragment= new login_fragment();
        register_fragment = new register_fragment();
        login_frag[0] = register_fragment;
        login_frag[1] = login_fragment;

        line1 = findViewById(R.id.register_line);
        line2 = findViewById(R.id.login_line);
        below_line[0] = line1;
        below_line[1] = line2;
        gestureDetector = new GestureDetector(this,this);
    }

    public void setTxtEvent() {
        for(TextView tv: select_btn){
            tv.setOnClickListener(this);
        }
    }


    public void setContentFragment(){
        page_select[0] = true ;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.login_frag, login_frag[0]);
        ft.commit();
        intent = getIntent();
        LogUtils.v(intent.getStringExtra("frag"));
        if(intent.getStringExtra("frag").toString()=="1"||intent.getStringExtra("frag").equals("1")){
            changeFragment(1);
            changePage(1);
            changeLine(1);
        }

    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()){
            case R.id.free_register :
                if(!page_select[0]){
                    LogUtils.v("select 1");

                    changePage(0);
                    changeLine(0);
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ft.replace(R.id.login_frag, login_frag[0]);
                    ft.commit();
                }
                break;
            case R.id.login :
                if(!page_select[1]){
                    LogUtils.v("select 2");
                    changePage(1);
                    changeLine(1);
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ft.replace(R.id.login_frag, login_frag[1]);
                    ft.commit();
                }
                break;
        }
    }

    private void changeLine(int index) {
        for(int j=0;j<below_line.length;j++){
            below_line[j].setVisibility(View.INVISIBLE);
        }
        below_line[index].setVisibility(View.VISIBLE);
    }

    public void changeFragment(int index){
        page_select[index] = true ;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.login_frag, login_frag[index]);
        ft.commit();
    }

    public void changePage(int index){
        page_select[0]=false;
        page_select[1]=false;
        page_select[index]=true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtils.v("down");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtils.v("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtils.v("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        LogUtils.v("onScroll------------------------------");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtils.v("onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.v("onFling");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogUtils.v("distanceX"+e1+"    distanceY"+e2);
        if((page_select[1])&&(e2.getX()-e1.getX()>50))
        {
            LogUtils.v("左滑");
            changePage(0);
            changeLine(0);
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.login_frag, login_frag[0]);
            ft.commit();

        }else if((page_select[0])&&(e1.getX()-e2.getX()>50)){
            LogUtils.v("右滑");
            changePage(1);
            changeLine(1);
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            ft.replace(R.id.login_frag, login_frag[1]);
            ft.commit();

        }
        return false;
    }
}

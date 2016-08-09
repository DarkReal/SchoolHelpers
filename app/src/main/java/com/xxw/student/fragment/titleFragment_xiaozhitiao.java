package com.xxw.student.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.fragment.xiaozhitiao_fragment.addfriendFragment;
import com.xxw.student.fragment.xiaozhitiao_fragment.lianxiren;

/**
 * 小纸条--头部
 * Created by xxw on 2016/3/27.
 */
public class titleFragment_xiaozhitiao extends Fragment implements View.OnClickListener,AnimationListener {
    private View rootView;
    private View contentView;
    private LinearLayout messageList;
    private int[] flag = new int[2];


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.main_title_xiaozhitiao,container,false);


        setDefault();

        return rootView;
    }

    private ImageButton addfrident;
    private TextView message,lianxiren;
    private View message_line,lianxiren_line;
    private TextView[] text = new TextView[2];
    private View[] line = new View[2];
    public void setDefault(){

        flag[0] = 1;
        flag[1] = 0;

        addfrident = (ImageButton)rootView.findViewById(R.id.addfrident);

        message = (TextView)rootView.findViewById(R.id.xiaozhitiao_message);
        text[0] = message;
        lianxiren = (TextView)rootView.findViewById(R.id.xiaozhitiao_lianxiren);
        text[1] = lianxiren;
        message_line = rootView.findViewById(R.id.xiaozhitiao_message_line);
        line[0] = message_line;
        lianxiren_line = rootView.findViewById(R.id.xiaozhitiao_lianxiren_line);
        line[1] = lianxiren_line;

        addfrident.setOnClickListener(this);
        message.setOnClickListener(this);
        lianxiren.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xiaozhitiao_message:
                if (flag[0]==0){
                    changeContent(0);
                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                    ft.replace(R.id.main_content, new contentFragment_xiaozhitiao());
                    ft.commit();
                    flag[0]=1;
                    flag[1]=0;
                }
                break;
            case R.id.xiaozhitiao_lianxiren:
                if (flag[1]==0){
                    changeContent(1);
                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ft.replace(R.id.main_content, new lianxiren());
                    ft.commit();

                    flag[0]=0;
                    flag[1]=1;
                }
                break;
            case R.id.addfrident:
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fillContent, new addfriendFragment());
                ft.commit();
                break;
        }
    }

    public void changeContent(int index){
        int index1 ;
        if (index==0)
            index1=1;
        else
            index1=0;
        text[index].setTextColor(getResources().getColor(R.color.xiaozhitiao_message_lianxiren));
        line[index].setBackgroundColor(getResources().getColor(R.color.xiaozhitiao_message_lianxiren));

        text[index1].setTextColor(Color.BLACK);
        line[index1].setBackgroundColor(Color.WHITE);


    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
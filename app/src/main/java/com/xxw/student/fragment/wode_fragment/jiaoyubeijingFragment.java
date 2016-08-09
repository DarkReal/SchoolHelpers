package com.xxw.student.fragment.wode_fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xxw.student.R;

/**
 * 我的-教育背景
 * Created by xxw on 2016/4/9.
 */
public class jiaoyubeijingFragment extends BaseFragment {
    private View rootView;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_jiaoyubeijing,container,false);

        setDefault();

        return rootView;
    }

    private ImageView back;
    private GestureDetector mGestureDetector;
    private LinearLayout root_jiaoyubeijing;
    public void setDefault(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_jiaoyubeijing);
        back.setOnClickListener(this);

        root_jiaoyubeijing = (LinearLayout)rootView.findViewById(R.id.root_jiaoyubeijing);
        root_jiaoyubeijing.setOnTouchListener(this);
        root_jiaoyubeijing.setLongClickable(true);

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)this);
    }

    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
}

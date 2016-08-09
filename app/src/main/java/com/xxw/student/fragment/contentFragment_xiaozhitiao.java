package com.xxw.student.fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.BaseFragment;
import com.xxw.student.myView.messageListView;
import com.xxw.student.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小纸条
 * Created by xxw on 2016/3/27.
 */
public class contentFragment_xiaozhitiao extends BaseFragment{

    private View rootView;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.main_content_xiaozhitiao, container, false);

        setDefault();

        return rootView;

    }

//    private LinearLayout content_xiaozhitiao ;
    private messageListView mlv;
    private GestureDetector mGestureDetector;
    private messageListView l ;
    private LinearLayout content_xiaozhitiao;
    private SimpleAdapter messageAdapter;
    private List<Map<String,Object>> messageLists;
    private String[] name = {"洛洛兽","小薇姐"};
    private String[] desc = {"[签名]欢迎打扰我...","[签名]正在学习中..."};
    private int[] image = {R.drawable.touxiang, R.drawable.touxiang};
    public void setDefault(){
        mlv = (messageListView)rootView.findViewById(R.id.messageLists);
        mlv.setActivity(getActivity(), R.id.fillContent);

        LogUtils.v(getActivity() + "," + R.id.fillContent);

        content_xiaozhitiao = (LinearLayout)rootView.findViewById(R.id.content_xiaozhitiao);
        content_xiaozhitiao.setOnTouchListener(this);
        content_xiaozhitiao.setLongClickable(true);
        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);//这句话到底什么意思？
        super.setmGestureDetector(mGestureDetector);

        messageLists = new ArrayList<Map<String, Object>>();
        for (int i=0;i<name.length;i++){
            Map<String,Object> messagelist = new HashMap<String, Object>();
            messagelist.put("name",name[i]);
            messagelist.put("desc",desc[i]);
            messagelist.put("image",image[i]);
            messageLists.add(messagelist);
        }
        messageAdapter = new SimpleAdapter(getActivity(),messageLists, R.layout.messagelist,
                new String[]{"image","name","desc"},new int[]{R.id.message_list_image, R.id.message_list_name, R.id.message_list_desc});
        l = (messageListView)rootView.findViewById(R.id.messageLists);
        l.setAdapter(messageAdapter);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtils.v("onTouch start");
//        return mGestureDetector.onTouchEvent(event);
        return true;
    }

}

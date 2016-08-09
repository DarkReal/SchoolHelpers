package com.xxw.student.fragment.xiaozhitiao_fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小纸条--添加好友
 * Created by xxw on 2016/4/10.
 */
public class addfriendFragment extends BaseFragment {


    private View rootView;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.xiaozhitiao_addfriend,container,false);

        setDefault();

        return rootView;
    }

    //定义并设置一些初始值
    private GestureDetector mGestureDetector;
    private LinearLayout root_addfriend;
    private ImageView backToxiaozhitiao_addfriend;
    private ListView addfriendListView;
    private List<Map<String,Object>> friendLists;
    private int[] image = {R.drawable.defaulthead, R.drawable.defaulthead};
    private String[] name = {"洛洛兽","小薇姐"};
    private String[] college = {"2013-信息电子工程学院","2013-信息电子工程学院"};
    private String[] from = {"来自同学院推荐","来自同学院推荐"};
    public void setDefault(){


        backToxiaozhitiao_addfriend = (ImageView)rootView.findViewById(R.id.backToxiaozhitiao_addfriend);
        backToxiaozhitiao_addfriend.setOnClickListener(this);

        root_addfriend = (LinearLayout)rootView.findViewById(R.id.root_addfriend);
        root_addfriend.setOnTouchListener(this);
        root_addfriend.setLongClickable(true);//为什么一定要设置这个？

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);//这句话到底什么意思？
        super.setmGestureDetector(mGestureDetector);

        addfriendListView = (ListView)rootView.findViewById(R.id.addfriendListView);

        friendLists = new ArrayList<Map<String, Object>>();

        for (int i = 0;i<image.length;i++){
            Map<String,Object> friendList = new HashMap<String,Object>();
            friendList.put("image",image[i]);
            friendList.put("name",name[i]);
            friendList.put("college",college[i]);
            friendList.put("from",from[i]);
            friendLists.add(friendList);
        }

        SimpleAdapter a = new SimpleAdapter(getActivity(),friendLists, R.layout.xiaozhitiao_friendlist,
                new String[]{"image","name","college","from"},new int[]{R.id.image, R.id.name, R.id.college, R.id.from});
        addfriendListView.setAdapter(a);
    }

}
package com.xxw.student.fragment.xiaozhitiao_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.xxw.student.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小纸条--联系人
 * Created by xxw on 2016/4/10.
 */
public class lianxiren extends Fragment {
    private View rootView;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.xiaozhitiao_lianxiren,container,false);

        setDefault();

        return rootView;
    }

    private ListView friendListView;
    private List<Map<String,Object>> friendLists;
    private int[] image = {R.drawable.defaulthead, R.drawable.defaulthead};
    private String[] name = {"洛洛兽","小薇姐"};
    private String[] desc = {"[签名]欢迎打扰我...","[签名]正在学习中..."};
    public void setDefault(){
        friendListView = (ListView)rootView.findViewById(R.id.friendLists);

        friendLists = new ArrayList<Map<String, Object>>();

        for (int i=0;i<image.length;i++){
            Map<String,Object> friendList = new HashMap<String,Object>();
            friendList.put("image",image[i]);
            friendList.put("name",name[i]);
            friendList.put("desc",desc[i]);
            friendLists.add(friendList);
        }
        SimpleAdapter a = new SimpleAdapter(getActivity(),friendLists, R.layout.messagelist,
                new String[]{"image","name","desc"},
                new int[]{R.id.message_list_image, R.id.message_list_name, R.id.message_list_desc});
        friendListView.setAdapter(a);
    }


}

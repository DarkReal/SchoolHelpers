package com.xxw.student.fragment.wode_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxw.student.R;

/**
 * 我的-总frag页面
 * Created by xxw on 2016/4/9.
 */
public class wodeFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.wode_content_wode,container,false);
    }
}

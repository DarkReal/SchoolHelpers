package com.xxw.student.fragment.wode_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xxw.student.R;

/**
 * Created by DarkReal on 2016/8/26.
 */
public class unread_tz extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tz_wd, container, false);
        return view;
    }
}

package com.xxw.student.myView;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.SimpleAdapter;

import com.xxw.student.utils.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * 消息自定义的适配器，有触摸判断
 * Created by xxw on 2016/4/15.
 */
public class messageSimpleAdapter extends SimpleAdapter {
    public messageSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    public boolean onTouchEvent(MotionEvent event){
        LogUtils.v("touched");
        return true;
    }

}

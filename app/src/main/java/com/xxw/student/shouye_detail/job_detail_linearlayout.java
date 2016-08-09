package com.xxw.student.shouye_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.xxw.student.R;
import com.xxw.student.utils.LogUtils;

/**
 * Created by DarkReal on 2016/4/18.
 */
public class job_detail_linearlayout extends LinearLayout {
    private LayoutInflater inflater;

    public job_detail_linearlayout(Context context) {
        super(context);
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.company_job_detail, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            boolean result = super.dispatchTouchEvent(ev);
            LogUtils.v("dispatch" + "### dispatchTouchEvent result = " + ev.getAction());

            return result;
    }


        @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}

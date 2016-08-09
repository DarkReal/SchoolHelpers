package com.xxw.student.myView;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.xxw.student.shouye_detail.job_detail;

/**
 * 职位公司的listview,公用
 * Created by xxw on 2016/4/16.
 */
public class zhiwei_gongsi_ListView extends baseListView{


    private Activity ss;
    private Fragment aal;
    public zhiwei_gongsi_ListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        super.setIsOpenSlipBack(true);

    }

    public void setA(Activity a){
        this.ss = a;
    }

    public void setA(Fragment a){
        this.aal = a;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (e.getY()>250&&e.getY()<370){
            Intent intent = new Intent();
            intent.setClass(ss,job_detail.class);
            aal.startActivity(intent);

        }
        return false;
    }

}

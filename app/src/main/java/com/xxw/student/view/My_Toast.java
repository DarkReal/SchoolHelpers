package com.xxw.student.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.R;

/**
 * 自定义Toast样式
 * Created by DarkReal on 2016/4/19.
 */
public class My_Toast {

    private static My_Toast my_toast;

    private Toast toast;

    private My_Toast(){
    }

    public static My_Toast createToastConfig(){
        if (my_toast==null) {
            my_toast = new My_Toast();
        }
        return my_toast;
    }

    public void ToastShow(Context context,ViewGroup root,String tvString){

        this.ToastShow(context, root, tvString, true);

    }
    public void ToastShow(Context context,ViewGroup root,String tvString,boolean isshort){


        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml, null);

        TextView text = (TextView) layout.findViewById(R.id.toast_id);
        text.setText(tvString);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if(isshort){
            toast.setDuration(Toast.LENGTH_SHORT);
        }else{
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setView(layout);
        toast.show();
    }
}
package com.xxw.student.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.framework.picker.DatePicker;
import com.xxw.student.view.framework.picker.OptionPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 公司列表的adapter
 * Created by DarkReal on 2016/7/24.
 */
public class CustomAdapter_companyList extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        教育背景
        TextView company_name;
        TextView company_desc;
        TextView eachid;
        TextView count_job;
        TextView company_city;
        ImageView company_pic;

    }
    private LayoutInflater mInflater;
    private List<HashMap<String, String>> list;
    private int layoutID;
    private String flag[];
    private int ItemIDs[];
    private Activity activity;
    private Context mcontext;
    public static String currentItem = "";//标记当前选中项
    private int currentItemInt = -1;
    private static View mConvertView;
    private static boolean needtoshow = false;//默认不展示

    private BitmapUtils bitmapUtils;

    public CustomAdapter_companyList(Context context, Activity activity, List<HashMap<String, String>> list,
                                     int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;
        this.bitmapUtils = new BitmapUtils(this.mcontext);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        mConvertView = convertView;
        TextViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            holder = new TextViewHolder();

            convertView = mInflater.inflate(layoutID, null);
            // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件
            holder.company_name = (TextView) convertView.findViewById(R.id.company_name);
            holder.company_desc = (TextView) convertView.findViewById(R.id.company_desc);
            holder.eachid = (TextView) convertView.findViewById(R.id.company_id);
            holder.count_job = (TextView) convertView.findViewById(R.id.count_job);
            holder.company_pic = (ImageView) convertView.findViewById(R.id.company_pic);
            holder.company_city = (TextView) convertView.findViewById(R.id.company_city);
            convertView.setTag(holder);

        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏

            holder = (TextViewHolder) convertView.getTag();
        }
        for (int i = 0; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
        }
        LogUtils.v(list.get(position).get("companyPic"));
        LogUtils.v(Constant.getUrl() + "upload/media/images/" + list.get(position).get("companyPic"));
        //初始化图片
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bitmapUtils.display(holder.company_pic, Constant.getUrl() + "upload/media/images/" + list.get(position).get("companyPic"));

        return convertView;
    }

}
